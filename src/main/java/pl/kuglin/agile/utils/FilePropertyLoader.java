package pl.kuglin.agile.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static pl.kuglin.agile.utils.FilePropertyLoader.ExceptionMessage.FILE_CLOSE_ERROR;
import static pl.kuglin.agile.utils.FilePropertyLoader.ExceptionMessage.LOAD_FILE_ERROR;

/**
 * It's one use loader. You can only once load properties. After loading,
 * file will be closed, but properties will be persisted in properties field.
 */
public class FilePropertyLoader implements PropertyLoader {
    private Logger log = LoggerFactory.getLogger(getClass());

    private Properties properties = new Properties();
    private FileInputStream file;

    FilePropertyLoader() {
    }

    public FilePropertyLoader(String path) throws FileNotFoundException {
        this.file = new FileInputStream(path);
    }

    /**
     * Note that after loading properties file will be close.
     */
    public void loadProperties() {
        try {
            properties.load(file);
        } catch (IOException ex) {
            log.error(LOAD_FILE_ERROR.toString(), ex);
        }

        closeFile();
    }

    public Properties getProperties() {
        return properties;
    }

    private void closeFile() {
        try {
            if (file != null)
                file.close();
        } catch (IOException ex) {
            log.warn(FILE_CLOSE_ERROR.toString(), ex);
        }
    }

    enum ExceptionMessage {
        FILE_CLOSE_ERROR("File cannot be closed"),
        LOAD_FILE_ERROR("File cannot be load");

        private final String message;

        ExceptionMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
