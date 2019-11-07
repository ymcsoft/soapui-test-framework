package com.ymcsoft;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Internal file utilities
 * <p>
 * @author Yuri Moiseyenko
 * Created on 6/13/2017.
 */
class FileUtil {
    private static final Logger log = Logger.getLogger(FileUtil.class.getName());
    private FileUtil() {}

    static boolean filter(String str, Pattern p) {
        Matcher m = p.matcher(str);
        return m.find();
    }

    static void scanDirectory(File dir, List<Path> all, Pattern p) {
        log.info("Scanning directory " + dir.getAbsolutePath());
        if(dir.exists() && dir.isDirectory()) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir.toPath())) {
                for (Path child : ds) {
                    if (Files.isDirectory(child)) {
                        scanDirectory(child.toFile(), all, p);
                    }
                    else if(FileUtil.filter(child.toFile().getName(), p)) {
                        log.log(Level.INFO, "Found file " + child.toFile().getName());
                        all.add(child);
                    }
                }
            } catch (IOException e) {
                log.log(Level.ERROR, "Scanning directory " + dir.getAbsolutePath() + " failed.", e);
            }
        }
    }
}
