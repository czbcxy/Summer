package com.summer.framework.webmvc.servlet;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SunView {

    private File viewFile;
    private static final String regex = "#\\{[^\\}]+\\}";
    private static final Pattern compile = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    public SunView(File viewFile) {
        this.viewFile = viewFile;
    }

    public void render(Map<String, ?> model, HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        StringBuffer sb = new StringBuffer();
        RandomAccessFile r = new RandomAccessFile(this.viewFile, "r");
        String line = null;
        while (null != (line = r.readLine())) {
            line = new String(line.getBytes("ISO-8859-1"), "utf-8");
            Matcher matcher = compile.matcher(line);
            while (matcher.find()) {
                String key = matcher.group().replaceAll("#\\{|\\}", "");
                Object value = model.get(key);
                if (null == value) {
                    continue;
                }
                line = matcher.replaceFirst(value.toString());
                compile.matcher(line);
            }
            sb.append(line);
        }
        response.setContentType(ContentType.defaultContentType.value);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(sb.toString());
    }
}
