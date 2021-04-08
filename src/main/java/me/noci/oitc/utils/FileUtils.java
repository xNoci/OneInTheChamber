package me.noci.oitc.utils;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    public static boolean moveFile(Path src, Path dest) {
        try {
            java.nio.file.Files.move(src, dest, StandardCopyOption.ATOMIC_MOVE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyFile(File file, Path src, Path dest) {
        try {
            java.nio.file.Files.copy(file.toPath(), dest.resolve(src.relativize(file.toPath())), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyDir(Path src, Path dest) {
        for (File file : Files.fileTreeTraverser().preOrderTraversal(src.toFile())) {
            if (!copyFile(file, src, dest)) {
                return false;
            }
        }
        return true;
    }

}
