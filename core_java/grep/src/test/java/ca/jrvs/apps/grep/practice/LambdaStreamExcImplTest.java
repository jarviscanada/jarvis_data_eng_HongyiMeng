package ca.jrvs.apps.grep.practice;

import ca.jrvs.apps.practice.LambdaStreamExcImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LambdaStreamExcImplTest {

    private static LambdaStreamExcImpl lambdaStreamExc;

    @BeforeAll
    private static void setup() {
        lambdaStreamExc = new LambdaStreamExcImpl();
    }

    @Test
    public void givenStrings_whenCreateStrStream_thenStreamContainsStrings() {
        String[] strs = {"a", "b", "c"};

        Stream<String> stream =  lambdaStreamExc.createStrStream(strs);

        assertEquals(3, stream.count());
    }

    @Test
    public void givenStrings_whenToUpperCase_thenStreamContainsUppercaseStrings() {
        String[] strs = {"a", "b", "c"};

        Stream<String> stream =  lambdaStreamExc.toUpperCase(strs);
        Optional<String> firstEle = stream.findFirst();

        assertTrue(firstEle.isPresent());
        assertEquals("A", firstEle.get());
    }

    @Test
    public void givenStringStreamAndPattern_whenFilter_thenStreamRemovesElementsContainingPattern() {
        Stream<String> stream = Stream.of("a", "aa", "b", "c", "e");

        Stream<String> filteredStream =  lambdaStreamExc.filter(stream, "a");

        assertEquals(3, filteredStream.count());
    }

    @Test
    public void givenInts_whenCreateIntStream_thenStreamContainsInts() {
        int[] ints = {1, 2, 3};

        IntStream stream =  lambdaStreamExc.createIntStream(ints);

        assertEquals(3, stream.count());
    }

    @Test
    public void givenStream_whenToList_thenCreateList() {
        Stream<String> stream = Stream.of("a", "aa", "b", "c", "e");

        List<String> list =  lambdaStreamExc.toList(stream);

        assertEquals(5, list.size());
    }

    @Test
    public void givenIntStream_whenToList_thenCreateList() {
        IntStream stream = IntStream.of(1, 2, 3);

        List<Integer> list =  lambdaStreamExc.toList(stream);

        assertEquals(3, list.size());
    }

    @Test
    public void givenStartAndEnd_whenCreateIntStream_thenStreamContainsIntsRangingFromStartToEndInclusive() {

        IntStream stream =  lambdaStreamExc.createIntStream(2, 4);

        assertEquals(3, stream.count());
    }

    @Test
    public void givenIntStream_whenSquareRootIntStream_thenStreamContainsSquaredRootValues() {
        IntStream stream =  IntStream.of(4, 9);

        DoubleStream roots = lambdaStreamExc.squareRootIntStream(stream);
        OptionalDouble first = roots.findFirst();

        assertTrue(first.isPresent());
        assertEquals(2.0, first.getAsDouble());
    }

    @Test
    public void givenIntStream_whenGetOdd_thenStreamOddValuesOnly() {
        IntStream stream =  IntStream.of(1, 9, 3, 4, 5, 6);

        IntStream odds = lambdaStreamExc.getOdd(stream);

        assertEquals(4, odds.count());
    }

    @Test
    public void givenPrefixSuffix_whenPassingMessageIntoLambdaPrinter_thenMessageIsWrappedBetweenPrefixAndSuffix() {
        String prefix = "start>";
        String suffix = "<end";
        String message = "Message body";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Consumer<String> printer = lambdaStreamExc.getLambdaPrinter(prefix, suffix);
        printer.accept(message);

        assertEquals(prefix + message + suffix + "\r\n", outContent.toString());
        System.setOut(originalOut);
    }

    @Test
    public void givenStringsAndLambdaPrinter_whenPrintingMessages_thenStringsArePrintedWithPrinter() {
        String prefix = "msg:";
        String suffix = "!";
        String[] messages = {"a", "b", "c"};
        String expected = "msg:a!\r\nmsg:b!\r\nmsg:c!\r\n";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Consumer<String> printer = lambdaStreamExc.getLambdaPrinter(prefix, suffix);
        lambdaStreamExc.printMessages(messages, printer);

        assertEquals(expected, outContent.toString());
        System.setOut(originalOut);
    }

    @Test
    public void givenIntsAndLambdaPrinter_whenPrintingOdds_thenOddNumbersArePrintedWithPrinter() {
        String prefix = "odd number:";
        String suffix = "!";
        IntStream intStream = lambdaStreamExc.createIntStream(0, 5);
        String expected = "odd number:1!\r\nodd number:3!\r\nodd number:5!\r\n";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Consumer<String> printer = lambdaStreamExc.getLambdaPrinter(prefix, suffix);
        lambdaStreamExc.printOdd(intStream, printer);

        assertEquals(expected, outContent.toString());
        System.setOut(originalOut);
    }

    @Test
    public void givenIntegerListStream_whenCallingFlatNestedInt_thenFlatten() {
        Stream<List<Integer>> stream = Stream.of(Arrays.asList(1, 3), Collections.singletonList(2));

        Stream<Integer> out = lambdaStreamExc.flatNestedInt(stream);

        assertEquals(3, out.count());
    }

    @Test
    public void givenIntegerListStream_whenCallingFlatNestedInt_thenSquare() {
        Stream<List<Integer>> stream = Stream.of(Collections.singletonList(2));

        Stream<Integer> out = lambdaStreamExc.flatNestedInt(stream);
        Optional<Integer> first = out.findFirst();

        assertTrue(first.isPresent());
        assertEquals(4, first.get());
    }

}
