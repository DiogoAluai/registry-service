package daluai.services.registry_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * File system utils.
 */
public final class FileSystemUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemUtils.class);

    /**
     * Try to create file and needed directories.
     * Keep in mind that directories under root (/) are usually impossible to create due to permissions.
     */
    public static void tryCreatingFile(File file) {
        File directory = file.getParentFile();
        if (directory == null) {
            throw new IllegalStateException("Storage directory is null");
        }
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IllegalStateException("Could not create storage directory");
            }
        }

        try {
            if (file.createNewFile()) {
                LOG.info("Storage file created: {}", file.getAbsolutePath());
            } else {
                throw new IllegalStateException("Failed to create storage file");
            }
        } catch (IOException e) {
            LOG.error("Failed to create storage", e);
            throw new IllegalStateException("Failed to create storage file");
        }
    }

    private FileSystemUtils() {
        // prevent utils class instantiation
    }
}
