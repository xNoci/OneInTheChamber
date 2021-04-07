package me.noci.oitc.utils;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    public static void copyDir(Path src, Path dest) {
        for (File file : Files.fileTreeTraverser().preOrderTraversal(src.toFile())) {
            try {
                java.nio.file.Files.copy(file.toPath(), dest.resolve(src.relativize(file.toPath())), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
