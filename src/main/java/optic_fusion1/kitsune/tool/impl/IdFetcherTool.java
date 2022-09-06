package optic_fusion1.kitsune.tool.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.Tool;
import static optic_fusion1.kitsune.util.I18n.tl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class IdFetcherTool extends Tool {

    private static final List<String> VALID_FILE_EXTENSIONS = new ArrayList<>();
    private boolean logInnerFiles;

    public IdFetcherTool() {
        super("idfetcher", tl("ift_desc"));
        VALID_FILE_EXTENSIONS.add("jar");
        VALID_FILE_EXTENSIONS.add("zip");
        VALID_FILE_EXTENSIONS.add("rar");
        VALID_FILE_EXTENSIONS.add("jarinjar");
    }

    @Override
    public void run(List<String> args) {
        if (args.isEmpty()) {
            LOGGER.info(tl("not_enough_args") + " " + tl("ift_usage"));
            return;
        }
        if (args.contains("--logInnerFiles")) {
            args.remove("--logInnerFiles");
            logInnerFiles = true;
        }
        if (!args.get(0).equalsIgnoreCase("all")) {
            File file = new File(args.get(0));
            if (!file.exists()) {
                LOGGER.info(tl("file_does_not_exist", file.getName()));
            }
            LOGGER.info(DigestUtils.sha1Hex(file.getName()));
            return;
        }
        File directory = new File(args.get(1));
        if (!directory.isDirectory()) {
            LOGGER.info(tl("file_is_not_directory", directory.getName()));
            return;
        }
        for (File file : directory.listFiles()) {
            LOGGER.info("\n\n\n\n");
            LOGGER.info(tl("processing", file.getName()));
            LOGGER.info(DigestUtils.sha1Hex(file.getName()));
            if (logInnerFiles) {
                String extension = FilenameUtils.getExtension(file.getName());
                if (!VALID_FILE_EXTENSIONS.contains(extension)) {
                    LOGGER.info(tl("file_is_not_zip", file.getName()));
                    continue;
                }
                handleFile(file);
            }
        }
    }

    private void handleFile(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = zipEntries.nextElement();
                if (isActuallyZipFile(zipFile, entry)) {
                    // TODO: Handle nested entries
                    LOGGER.info(entry.getName() + " is actually a ZipFile.");
                }
                LOGGER.info(entry.getName() + ": " + DigestUtils.sha1Hex(entry.getName()));
            }
        } catch (IOException ex) {
            LOGGER.exception(ex);
        }
    }

    private boolean isActuallyZipFile(ZipFile zipFile, ZipEntry entry) throws IOException {
        String zipEntryExtension;
        try {
            zipEntryExtension = FilenameUtils.getExtension(entry.getName());
        } catch (IllegalArgumentException e) {
            LOGGER.info(entry.getName() + " can't get file extension");
            return false;
        }
        byte[] bytes = IOUtils.toByteArray(zipFile.getInputStream(entry));
        if (bytes != null && bytes.length >= 2) {
            char c = (char) bytes[0];
            char d = (char) bytes[1];
            if (c == 'P' && d == 'K') {
                return !VALID_FILE_EXTENSIONS.contains(zipEntryExtension);
            }
        }
        return false;
    }

}
