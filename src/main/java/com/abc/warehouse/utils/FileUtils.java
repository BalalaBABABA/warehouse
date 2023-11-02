package com.abc.warehouse.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    public static String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

    public static void zipFolder(String sourceFolderPath, String zipFilePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);

        File sourceFolder = new File(sourceFolderPath);
        zipFiles(sourceFolder, sourceFolder.getName(), zos);

        zos.closeEntry();
        zos.close();
        fos.close();
    }

    private static void zipFiles(File fileSource, String parentPath, ZipOutputStream zos) throws IOException {
        if (fileSource.isFile()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(fileSource);
            zos.putNextEntry(new ZipEntry(parentPath + "/" + fileSource.getName()));

            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            fis.close();
        } else if (fileSource.isDirectory()) {
            File[] files = fileSource.listFiles();
            if (files != null) {
                for (File file : files) {
                    zipFiles(file, parentPath + "/" + fileSource.getName(), zos);
                }
            }
        }
    }
}
