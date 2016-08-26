package me.zhangxudong.platform.common.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 封装各种格式的编码解码工具类.
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 */
public class EncodeHelper {

    private static final String DEFAULT_URL_ENCODING = "UTF-8";
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }

    /**
     * Hex解码.
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Base64编码.
     */
    public static String encodeBase64(byte[] input) {
        try {
            return new String(Base64.encodeBase64(input), DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Base64编码.
     */
    public static String encodeBase64(String input) {
        try {
            return new String(Base64.encodeBase64(input.getBytes(DEFAULT_URL_ENCODING)), DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Base64解码.
     */
    public static byte[] decodeBase64(String input) {
        try {
            return Base64.decodeBase64(input.getBytes(DEFAULT_URL_ENCODING));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Base64解码.
     */
    public static String decodeBase64String(String input) {
        try {
            return new String(Base64.decodeBase64(input.getBytes(DEFAULT_URL_ENCODING)), DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Base62编码。
     */
    public static String encodeBase62(byte[] input) {
        char[] chars = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
        }
        return new String(chars);
    }

    /**
     * Html 转码.
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * Html 解码.
     */
    public static String unescapeHtml(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    /**
     * Xml 转码.
     */
    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml10(xml);
    }

    /**
     * Xml 解码.
     */
    public static String unescapeXml(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(String part) {
        return Another.encodeURI(part);
    }

    /**
     * URL 解码, Encode默认为UTF-8.
     */
    public static String urlDecode(String part) {

        try {
            return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unchecked(e);
        }
    }
}

class Another {

    /**
     * 判断一个字符在某个环境下是否需要编码处理.
     */
    private interface EncodeJudge {

        /**
         * 字符是否需要编码.
         */
        boolean shouldEncode(char c);
    }

    /**
     * 对于字符是否需要编码处理的简单实现.俩个条件之一:( > '~')或(指定字符之一)则需要编码.
     */
    private static final class EncodeJudgeSimpleImpl implements EncodeJudge {
        private static final char TOP_CHAR = '~';
        private char[] inners;

        private EncodeJudgeSimpleImpl(char[] inners) {
            this.inners = inners;
        }

        public boolean shouldEncode(char c) {
            return c > TOP_CHAR || isDefined(c);

        }

        private boolean isDefined(char c) {
            for (char inner : inners) {
                if (c == inner) {
                    return true;
                }
            }
            return false;
        }
    }

    private static EncodeJudge uriJudge = new EncodeJudgeSimpleImpl("\t\n\r \"%<>[\\]^`{|}".toCharArray());

    private static EncodeJudge uriCoponentJudge =
            new EncodeJudgeSimpleImpl("\t\n\r \"#$%&+,/:;<=>?@[\\]^`{|}".toCharArray());

    private static char[] hexs = "0123456789ABCDEF".toCharArray();

    public static String encodeURI(String uri) {
        return encodeWithJudge(uri, uriJudge);
    }

    public static String encodeURIComponent(String uri) {
        return encodeWithJudge(uri, uriCoponentJudge);
    }

    private static void addByte(StringBuilder sb, byte b) {

        sb.append('%');
        sb.append(hexs[(b >> 4) & 0xf]);
        sb.append(hexs[b & 0xf]);
    }

    private static String encodeWithJudge(String uri, EncodeJudge judge) {
        StringBuilder sb = new StringBuilder(uri.length() * 6);
        boolean changed = false;
        for (char c : uri.toCharArray()) {
            if (!judge.shouldEncode(c)) {
                sb.append(c);
                continue;
            }

            changed = true;
            if (c < 128) { // 2 ^ 7
                addByte(sb, (byte) c);

            } else if (c < 2048) { // 2 ^ 11
                addByte(sb, (byte) (((c >> 6) & 0x1f) | 0xc0));
                addByte(sb, (byte) ((c & 0x3f) | 0x80));

            } else { // 2 ^ 16
                addByte(sb, (byte) (((c >> 12) & 0xf) | 0xe0));
                addByte(sb, (byte) (((c >> 6) & 0x3f) | 0x80));
                addByte(sb, (byte) ((c & 0x3f) | 0x80));

            }
        }

        return changed ? sb.toString() : uri;
    }

}
