package com.ymcsoft;

import com.ymcsoft.test.soapui.SoapApiTest;
import org.apache.commons.cli.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


/**
 * App entry point
 * <p>
 * @author Yuri Moiseyenko
 * Created on 6/12/2017.
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static Pattern FileFilterPattern;
    private static Options options;

    static{
        FileFilterPattern  = Pattern.compile("\\.xml");
        init();
    }

    public static void init() {
        options = new Options();

        options.addOption( "h", "help", false, "Show help." );
        options.addOption( "d", "directory", true, "Directory containing SoapUI projects to be tested.");
        options.addOption( "p", "filter-pattern", true, "Regular expression filter pattern to search the directory." );
    }

    public static void main(String[] args) {

        File dir = processCommandLineArgs(args);

        if(dir == null) {
            dir = new File(".");
        }
        List<Path> projectFiles = new ArrayList<Path>();
        FileUtil.scanDirectory(dir, projectFiles, FileFilterPattern);
        for (Path path: projectFiles) {
            log.info("Testing project " + path.toFile().getName());
            SoapApiTest test = new SoapApiTest(dir.getAbsolutePath(), path.toFile().getName(), null);
            try {
                test.runAllTestsInSuites(new HashMap<String, String>());
            } catch (Throwable throwable) {
                log.log(Level.FATAL, "Error occurred: " +  throwable.getMessage(), throwable);
            }
            log.info("Finished testing project " + path.toFile().getName());
        }
        System.exit(0);
    }

    private static File processCommandLineArgs(String[] args) {
        File dir = null;
        // create the parser
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args );

            if(line.hasOption("h")) {
                help();
            }

            if(line.hasOption("d")) {
                dir = new File(line.getOptionValue("d"));
            } else {
                help();
            }

            if(line.hasOption("p")) {
                FileFilterPattern = Pattern.compile(line.getOptionValue("p"));
            }
        }
        catch( ParseException exp ) {
            // oops, something went wrong
            log.log(Level.ERROR, "Parsing failed.  Reason: " + exp.getMessage(), exp );
            System.exit(1);
        }
        catch (Throwable throwable) {
            log.log(Level.FATAL, "Exception: " + throwable.getMessage(), throwable);
            help();
        }
        return dir;
    }

    private static void help() {
        HelpFormatter formatter = new HelpFormatter();
        String jarName = new java.io.File(Main.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
        formatter.printHelp(" java -jar -Dsoapui.log4j.config=log4j.xml " + jarName, options);
        System.exit(0);
    }
}
