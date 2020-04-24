package pl.kuglin.agile.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static pl.kuglin.agile.utils.FilePropertyLoader.ExceptionMessage.FILE_CLOSE_ERROR;
import static pl.kuglin.agile.utils.FilePropertyLoader.ExceptionMessage.LOAD_FILE_ERROR;

@ExtendWith(MockitoExtension.class)
class FilePropertyLoaderTest {

    private static final String WRONG_PATH = "wrong path";
    private static final String PROPERTY_KEY = "key";
    private static final String PROPERTY_VALUE = "value";
    @InjectMocks
    private final FilePropertyLoader filePropertyLoader = new FilePropertyLoader();
    @Mock
    private FileInputStream file;
    @Spy
    private Properties properties;
    @Mock
    private Logger log;
    @Captor
    private ArgumentCaptor<Enum<FilePropertyLoader.ExceptionMessage>> message;
    @Captor
    private ArgumentCaptor<Throwable> throwable;

    @Test
    void constructorFileNotFoundExceptionTest() {
        assertThrows(FileNotFoundException.class, () -> new FilePropertyLoader(WRONG_PATH));
    }

    @Test
    void loadThrowsIOExceptionTest() throws IOException {
       doThrow(IOException.class).when(properties).load(any(InputStream.class));

        filePropertyLoader.loadProperties();

        verify(log).error(anyString(), message.capture(), throwable.capture());

        assertEquals(LOAD_FILE_ERROR, message.getValue());
        assertEquals(IOException.class, throwable.getValue().getClass());
    }

    @Test
    void loadPropertiesTest() throws IOException {
        doAnswer(invocation -> {
            Properties properties = (Properties) invocation.getMock();
            properties.setProperty(PROPERTY_KEY, PROPERTY_VALUE);
            return null;
        }).when(properties).load(any(InputStream.class));

        filePropertyLoader.loadProperties();

        assertEquals(PROPERTY_VALUE, filePropertyLoader.getProperties().getProperty(PROPERTY_KEY));
    }

    @Test
    void getPropertiesTest() {
        Properties obtainedProperties = filePropertyLoader.getProperties();

        assertEquals(properties, obtainedProperties);
    }

    @Test
    void closeFileThrowsIOExceptionTest() throws IOException {
        doThrow(IOException.class).when(file).close();

        filePropertyLoader.loadProperties();

        verify(log).warn(anyString(), message.capture(), throwable.capture());

        assertEquals(FILE_CLOSE_ERROR, message.getValue());
        assertEquals(IOException.class, throwable.getValue().getClass());
    }
}