package optic_fusion1.kitsune.tool.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import me.coley.cafedude.InvalidClassException;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.Tool;
import static optic_fusion1.kitsune.util.I18n.tl;
import optic_fusion1.kitsune.util.Utils;
import static optic_fusion1.kitsune.util.Utils.checkFileExists;
import static optic_fusion1.kitsune.util.Utils.writeToFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassWriter;

public class FixTool extends Tool {

    public FixTool() {
        super("fix", tl("ft_desc"));
    }

    @Override
    public void run(List<String> args) {
        if (args.isEmpty()) {
            LOGGER.info(tl("not_enough_args") + " " + tl("ft_usage"));
            return;
        }
        File input = new File(args.get(0));
        if (!checkFileExists(input)) {
            LOGGER.info(tl("file_does_not_exist", input.toPath()));
            return;
        }
        if (!input.getName().endsWith(".jar") && !input.getName().endsWith(".class")) {
            LOGGER.info(tl("file_invalid_extension", input.toPath()));
            return;
        }
        if (input.isDirectory()) {
            LOGGER.info(tl("file_is_directory", input.toPath()));
            return;
        }
        if (input.getName().endsWith(".jar")) {
            File output = new File(args.get(0) + "-fixed.jar");
            if (!output.exists()) {
                try {
                    output.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(FixTool.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            handleJarFile(input, output);
            return;
        }
        handleClassFile(input);
    }

    // TODO: Clean these methods up
    private void handleClassFile(File inputFile) {
        if (inputFile.getName().endsWith(".class/")) {
            try {
                FileUtils.copyFile(inputFile, new File(inputFile.getName().replace(".class/", ".class")));
            } catch (IOException ex) {
                Logger.getLogger(FixTool.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        File output = new File(inputFile.getName().replace(".class", "-fixed.class"));
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            byte[] data = IOUtils.toByteArray(inputStream);
            ClassWriter classWriter = Utils.stripIllegalAttributesAndData(data);
            LOGGER.info(tl("writing_to_jar_file", output));
            try (FileOutputStream outputStream = new FileOutputStream(output)) {
                writeToFile(outputStream, new ByteArrayInputStream(classWriter.toByteArray()));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StringsTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StringsTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidClassException ex) {
            Logger.getLogger(FixTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(FixTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleJarFile(File inputFile, File outputFile) {
        ZipOutputStream out = null;
        try (ZipFile zipFile = new ZipFile(inputFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            out = new ZipOutputStream(new FileOutputStream(outputFile));
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.getName().endsWith(".class/")) {
                    LOGGER.info(tl("invalid_extension", entry.getName()));
                    ZipEntry outEntry = new ZipEntry(entry.getName().replace(".class/", ".class"));
                    out.putNextEntry(outEntry);
                    LOGGER.info(tl("writing_to_jar_file", entry.getName()));
                    writeToFile(out, zipFile.getInputStream(entry));
                    continue;
                }
                if (entry.getName().endsWith(".class")) {
                    byte[] data = IOUtils.toByteArray(zipFile.getInputStream(entry));
                    ClassWriter classWriter = Utils.stripIllegalAttributesAndData(data);
                    ZipEntry newEntry = new ZipEntry(entry.getName());
                    out.putNextEntry(newEntry);
                    LOGGER.info(tl("writing_to_jar_file", entry.getName()));
                    writeToFile(out, new ByteArrayInputStream(classWriter.toByteArray()));
                    continue;
                }
                out.putNextEntry(entry);
                LOGGER.info(tl("writing_to_jar_file", entry.getName()));
                writeToFile(out, zipFile.getInputStream(entry));
            }
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(FixTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(FixTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
