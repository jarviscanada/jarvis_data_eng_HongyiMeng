package ca.jrvs.apps.grep.practice;

import ca.jrvs.apps.practice.RegexExcImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegexExcImplTest {

    private static RegexExcImpl regexExc;

    @BeforeAll
    private static void setup() {
        regexExc = new RegexExcImpl();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc.jpeg", ".JPG", "abc.jPeG"})
    public void givenJpegWhenMatchJpegThenReturnTrue(String filename) {
        boolean result = regexExc.matchJpeg(filename);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc.jpeg.txt", "abc.jg", "abc.jpeeeg"})
    public void givenNonJpegWhenMatchJpegThenReturnFalse(String filename) {
        boolean result = regexExc.matchJpeg(filename);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.0.0.0", "999.999.999.999", "6.32.65.564"})
    public void givenIpWhenMatchIpThenReturnTrue(String ip) {
        boolean result = regexExc.matchIp(ip);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.0", "0.0.0", "0.0.0.0.0", "32.4354.456.34", "jse.234.24.4", "...."})
    public void givenNonIpWhenMatchIpThenReturnFalse(String ip) {
        boolean result = regexExc.matchIp(ip);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n", "\t\t\n "})
    public void givenEmptyLineWhenCallingIsEmptyLineThenReturnTrue(String line) {
        boolean result = regexExc.isEmptyLine(line);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {" h", "32", "\t t \n"})
    public void givenNonEmptyLineWhenCallingIsEmptyLineThenReturnFalse(String line) {
        boolean result = regexExc.isEmptyLine(line);
        assertFalse(result);
    }
}
