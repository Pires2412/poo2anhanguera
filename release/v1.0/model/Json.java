package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Leitor e escritor de JSON minimalista, sem dependências externas.
 *
 * <p>Os valores são representados pelos tipos Java padrão: {@link Map} para
 * objetos, {@link List} para arrays, {@link String}, {@link Double} para
 * números, {@link Boolean} e {@code null}. É suficiente para persistir o
 * ranking e o estado do jogo do Tetris.
 */
public final class Json {

    private Json() {}

    /**
     * Serializa um valor para texto JSON formatado.
     *
     * @param value o valor ({@link Map}, {@link List}, {@link String},
     *              {@link Number}, {@link Boolean} ou {@code null})
     * @return o texto JSON correspondente
     */
    public static String write(Object value) {
        StringBuilder sb = new StringBuilder();
        writeValue(sb, value, 0);
        return sb.toString();
    }

    private static void indent(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) sb.append("  ");
    }

    private static void writeValue(StringBuilder sb, Object value, int level) {
        if (value == null) {
            sb.append("null");
        } else if (value instanceof String s) {
            writeString(sb, s);
        } else if (value instanceof Boolean b) {
            sb.append(b.booleanValue() ? "true" : "false");
        } else if (value instanceof Number n) {
            writeNumber(sb, n);
        } else if (value instanceof Map<?, ?> m) {
            writeObject(sb, m, level);
        } else if (value instanceof List<?> l) {
            writeArray(sb, l, level);
        } else {
            writeString(sb, value.toString());
        }
    }

    private static void writeNumber(StringBuilder sb, Number n) {
        if (n instanceof Double || n instanceof Float) {
            double d = n.doubleValue();
            if (!Double.isInfinite(d) && !Double.isNaN(d) && d == Math.rint(d)) {
                sb.append(Long.toString((long) d));
            } else {
                sb.append(Double.toString(d));
            }
        } else {
            sb.append(n.toString());
        }
    }

    private static void writeString(StringBuilder sb, String s) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        sb.append('"');
    }

    private static void writeObject(StringBuilder sb, Map<?, ?> map, int level) {
        if (map.isEmpty()) {
            sb.append("{}");
            return;
        }
        sb.append("{\n");
        int count = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            indent(sb, level + 1);
            writeString(sb, String.valueOf(entry.getKey()));
            sb.append(": ");
            writeValue(sb, entry.getValue(), level + 1);
            if (++count < map.size()) sb.append(',');
            sb.append('\n');
        }
        indent(sb, level);
        sb.append('}');
    }

    private static void writeArray(StringBuilder sb, List<?> list, int level) {
        if (list.isEmpty()) {
            sb.append("[]");
            return;
        }
        boolean simple = list.stream().allMatch(
                v -> v == null || v instanceof Number || v instanceof Boolean || v instanceof String);

        if (simple) {
            sb.append('[');
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(", ");
                writeValue(sb, list.get(i), level);
            }
            sb.append(']');
            return;
        }

        sb.append("[\n");
        for (int i = 0; i < list.size(); i++) {
            indent(sb, level + 1);
            writeValue(sb, list.get(i), level + 1);
            if (i < list.size() - 1) sb.append(',');
            sb.append('\n');
        }
        indent(sb, level);
        sb.append(']');
    }

    /**
     * Analisa um texto JSON e devolve a estrutura de valores Java equivalente.
     *
     * @param text o texto JSON
     * @return o valor raiz ({@link Map}, {@link List}, {@link String},
     *         {@link Double}, {@link Boolean} ou {@code null})
     * @throws RuntimeException se o texto for JSON inválido
     */
    public static Object parse(String text) {
        return new Parser(text).parseRoot();
    }

    /** Analisador recursivo descendente usado internamente por {@link Json#parse}. */
    private static final class Parser {

        private final String s;
        private int i = 0;

        Parser(String s) {
            this.s = s;
        }

        Object parseRoot() {
            skipWhitespace();
            Object value = parseValue();
            skipWhitespace();
            return value;
        }

        private Object parseValue() {
            skipWhitespace();
            char c = peek();
            return switch (c) {
                case '{' -> parseObject();
                case '[' -> parseArray();
                case '"' -> parseString();
                case 't', 'f' -> parseBoolean();
                case 'n' -> parseNull();
                default -> parseNumber();
            };
        }

        private Map<String, Object> parseObject() {
            Map<String, Object> map = new LinkedHashMap<>();
            expect('{');
            skipWhitespace();
            if (peek() == '}') {
                i++;
                return map;
            }
            while (true) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                expect(':');
                Object value = parseValue();
                map.put(key, value);
                skipWhitespace();
                char c = next();
                if (c == '}') break;
                if (c != ',') throw error("',' ou '}' esperado");
            }
            return map;
        }

        private List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            expect('[');
            skipWhitespace();
            if (peek() == ']') {
                i++;
                return list;
            }
            while (true) {
                Object value = parseValue();
                list.add(value);
                skipWhitespace();
                char c = next();
                if (c == ']') break;
                if (c != ',') throw error("',' ou ']' esperado");
            }
            return list;
        }

        private String parseString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (true) {
                char c = next();
                if (c == '"') break;
                if (c == '\\') {
                    char e = next();
                    switch (e) {
                        case '"' -> sb.append('"');
                        case '\\' -> sb.append('\\');
                        case '/' -> sb.append('/');
                        case 'n' -> sb.append('\n');
                        case 'r' -> sb.append('\r');
                        case 't' -> sb.append('\t');
                        case 'b' -> sb.append('\b');
                        case 'f' -> sb.append('\f');
                        case 'u' -> {
                            String hex = s.substring(i, i + 4);
                            i += 4;
                            sb.append((char) Integer.parseInt(hex, 16));
                        }
                        default -> throw error("escape invalido: \\" + e);
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        private Boolean parseBoolean() {
            if (s.startsWith("true", i)) {
                i += 4;
                return Boolean.TRUE;
            }
            if (s.startsWith("false", i)) {
                i += 5;
                return Boolean.FALSE;
            }
            throw error("booleano invalido");
        }

        private Object parseNull() {
            if (s.startsWith("null", i)) {
                i += 4;
                return null;
            }
            throw error("valor nulo invalido");
        }

        private Double parseNumber() {
            int start = i;
            if (peek() == '-') i++;
            while (i < s.length() && isNumberChar(s.charAt(i))) i++;
            String token = s.substring(start, i);
            return Double.valueOf(token);
        }

        private boolean isNumberChar(char c) {
            return (c >= '0' && c <= '9') || c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-';
        }

        private void skipWhitespace() {
            while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++;
        }

        private char peek() {
            if (i >= s.length()) throw error("fim inesperado");
            return s.charAt(i);
        }

        private char next() {
            if (i >= s.length()) throw error("fim inesperado");
            return s.charAt(i++);
        }

        private void expect(char c) {
            char actual = next();
            if (actual != c) throw error("'" + c + "' esperado, encontrado '" + actual + "'");
        }

        private RuntimeException error(String message) {
            return new RuntimeException("JSON invalido na posicao " + i + ": " + message);
        }
    }
}
