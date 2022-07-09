package io.github.guoyixing.collusion.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 名称转换器
 *
 * @author 敲代码的旺财
 * @date 2022/7/8 14:29
 */
public class NameConversionUtils {

    /**
     * 驼峰命名法转下划线命名法
     *
     * @param camelCase 驼峰命名的名称
     * @return 下划线命名的名称
     */
    public static String camelCase2UnderScoreCase(String camelCase) {
        if (camelCase == null) {
            return null;
        }

        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(camelCase);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase(Locale.ROOT));
        }
        matcher.appendTail(sb);
        String underScoreCase = sb.toString();
        if (underScoreCase.startsWith("_")) {
            return underScoreCase.substring(1);
        }
        return underScoreCase;
    }

}
