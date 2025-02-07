package ca.jrvs.apps.practice;

public class SimpleCalculatorImpl implements SimpleCalculator {

    @Override
    public int add(int x, int y) {
        return x + y;
    }

    @Override
    public int subtract(int x, int y) {
        return x - y;
    }

    @Override
    public int multiply(int x, int y) {
        return x * y;
    }

    @Override
    public double divide(int x, int y) {
        return 1.0 * x / y;
    }

    @Override
    public int power(int x, int y) {
        return (int) Math.pow(x, y);
    }

    @Override
    public int abs(int x) {
        return Math.abs(x);
    }

    @Override
    public double sqrt(int x) {
        return Math.sqrt(x);
    }

}
