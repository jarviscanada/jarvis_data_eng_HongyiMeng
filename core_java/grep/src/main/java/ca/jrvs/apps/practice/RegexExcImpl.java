package ca.jrvs.apps.practice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImpl implements RegexExc {

    @Override
    public boolean matchJpeg(String filename) {
        Pattern pattern = Pattern.compile(".*\\.jpe?g", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }

    @Override
    public boolean matchIp(String ip) {
        return Pattern.matches("(\\d{1,3}.){3}\\d{1,3}", ip);
    }

    @Override
    public boolean isEmptyLine(String line) {
        return Pattern.matches("^\\s*$", line);
    }
}
