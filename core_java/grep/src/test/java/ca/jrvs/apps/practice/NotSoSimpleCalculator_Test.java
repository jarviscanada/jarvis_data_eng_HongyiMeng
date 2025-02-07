package ca.jrvs.apps.practice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotSoSimpleCalculator_Test {
    NotSoSimpleCalculator calc;

    @Mock
    SimpleCalculator mockSimpleCalc;

    @BeforeEach
    void init() {
        calc = new NotSoSimpleCalculatorImpl(mockSimpleCalc);
    }

    @Test
    public void test_power() {
        when(mockSimpleCalc.power(2, 3)).thenReturn(8);
        int expected = 8;
        int actual = calc.power(2, 3);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_abs() {
        when(mockSimpleCalc.abs(10)).thenReturn(10);
        int expected = 10;
        int actual = calc.abs(10);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_sqrt() {
        when(mockSimpleCalc.sqrt(9)).thenReturn(3.0);
        double expected = 3.0;
        double actual = calc.sqrt(9);

        Assertions.assertEquals(expected, actual);
    }
}
