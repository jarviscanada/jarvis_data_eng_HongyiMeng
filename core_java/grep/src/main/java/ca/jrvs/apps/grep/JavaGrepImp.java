package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class JavaGrepImp implements JavaGrep {

    private static final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);
    private String regex;
    private String rootPath;
    private String outFile;

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();

        List<File> files = listFiles(this.rootPath);
        for(File file : files) {
            List<String> lines = readLines(file);
            for(String line : lines) {
                if(containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }

        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> files = new ArrayList<>();

        listFilesHelper(rootDir, files);

        logger.debug("{} Files found: {}", files.size(), files);
        return files;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader((inputFile)))) {
            String line = br.readLine();
            while(line != null) {
                lines.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            logger.error("Error encountered while reading file {}, {}", inputFile.getAbsolutePath(), e.getMessage());
        }

        logger.debug("{} lines read from {}", lines.size(), inputFile.getAbsolutePath());
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        return Pattern.matches(this.regex, line);
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        logger.debug("Writing {} lines to {}", lines.size(), this.outFile);
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)))) {
            for(int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                logger.debug("Line {}: {}", i, line);
                bw.write(line);
                bw.newLine();
            }
        }
    }

    @Override
    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return this.regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return this.outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    protected void listFilesHelper(String rootDir, List<File> files) {
        File dir = new File(rootDir);

        File[] dirFiles = dir.listFiles();
        if(dirFiles != null) {
            for(File file : dirFiles) {
                if(file.isFile()) {
                    files.add(file);
                } else if(file.isDirectory()) {
                    listFilesHelper(file.getAbsolutePath(), files);
                }
            }
        }
    }

    public static void main(String[] args) {
        logger.debug("Program called with arguments {}", Arrays.toString(args));

        JavaGrepImp app = new JavaGrepImp();
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
