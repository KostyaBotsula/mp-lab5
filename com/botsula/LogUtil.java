package com.botsula;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@Log
public class LogUtil implements Serializable {

    public static final String RESPONSE_CODE = "responseCode";
    public static final String ENDPOINT = "endpoint";
    public static final String METHOD = "method";
    public static final String DATE_STRING = "dateString";
    public static final String OUT_DATE_FORMAT = "dd/MMM/yyyy";

    private static final String LOG_PATTERN =
            "^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+) (\\S+) (\\S+)\" (\\d{3}) (\\d+)";
    private static final Pattern PATTERN = Pattern.compile(LOG_PATTERN);
    private static final DateTimeFormatter LOG_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(OUT_DATE_FORMAT, Locale.US);

    private String method;
    private String endpoint;
    private String responseCode;
    private String dateString;

    public static LogUtil parseLogLine(String line) {
        try {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) {
                throw new RuntimeException("Error parsing logline");
            }

            LocalDate logDateTime = LocalDate.parse(matcher.group(4), LOG_FORMATTER);

            return new LogUtil(matcher.group(5), matcher.group(6), matcher.group(8), logDateTime.format(DATE_FORMATTER));
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getResponseCode() {
        try {
            return Integer.valueOf(responseCode);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}