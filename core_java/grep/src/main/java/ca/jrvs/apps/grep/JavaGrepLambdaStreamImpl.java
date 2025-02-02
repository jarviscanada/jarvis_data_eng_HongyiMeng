package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaGrepLambdaStreamImpl extends JavaGrepImpl {

    private static final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaStreamImpl.class);

    @Override
    public void process() throws IOException {
        List<String> matchedLines;

        List<File> files = listFiles(getRootPath());

        matchedLines = files.stream()
                .flatMap(file -> readLines(file).stream())
                .filter(this::containsPattern)
                .collect(Collectors.toList());

        writeToFile(matchedLines);
    }

    @Override
    protected void listFilesHelper(String rootDir, List<File> files) {
        logger.debug("Calling list files helper (Stream/Lambda)");
        File dir = new File(rootDir);

        File[] dirFiles = dir.listFiles();
        if(dirFiles != null) {
            Arrays.stream(dirFiles).forEach(
                    file -> {
                        if(file.isFile()) {
                            files.add(file);
                        } else if(file.isDirectory()) {
                            listFilesHelper(file.getAbsolutePath(), files);
                        }
                    }
            );
        }
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        logger.debug("Writing {} lines to {}", lines.size(), getOutFile());
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getOutFile())))) {
            lines.forEach(line -> {
                try {
                    bw.write(line);
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) {
        logger.debug("Grep App (Lambda/Stream) called with arguments {}", Arrays.toString(args));

        JavaGrepLambdaStreamImpl app = new JavaGrepLambdaStreamImpl();
        app.setRegex(args[0]);
        app.setRootPath(args[1]);
        app.setOutFile(args[2]);

        try {
            app.process();
        } catch (IOException e) {
            logger.error("Error encountered while processing regex with args: {}, {}",
                    Arrays.toString(args), e.getMessage());
        }
    }
}
