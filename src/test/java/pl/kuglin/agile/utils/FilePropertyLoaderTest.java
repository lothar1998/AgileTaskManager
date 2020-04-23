package pl.kuglin.agile.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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

@ExtendWith(MockitoExtension.class)
class FilePropertyLoaderTest {

    private static final String EXPECTED_RESULT = "result";
    private static final String PROPERTY_NAME = "property";
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

    @Test
    void constructorFileNotFoundExceptionTest() {
        assertThrows(FileNotFoundException.class, () -> new FilePropertyLoader(WRONG_PATH));
    }

    @Test
    void loadThrowsIOExceptionTest() throws IOException {
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Throwable> throwable = ArgumentCaptor.forClass(Throwable.class);

        doAnswer(invocation -> {
            throw new IOException();
        }).when(properties).load(any(InputStream.class));

        filePropertyLoader.loadProperties();

        verify(log).error(message.capture(), throwable.capture());

        assertEquals(FilePropertyLoader.ExceptionMessage.LOAD_FILE_ERROR.toString(), message.getValue());
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

        assertEquals(PROPERTY_VALUE, filePropertyLoader.getProperty(PROPERTY_KEY));
    }

    @Test
    void getPropertyTest() {
        when(properties.getProperty(anyString())).thenAnswer(invocation -> EXPECTED_RESULT);

        String obtainedPropertyValue = filePropertyLoader.getProperty(PROPERTY_NAME);

        assertEquals(EXPECTED_RESULT, obtainedPropertyValue);
    }

    @Test
    void getPropertiesTest() {
        Properties obtainedProperties = filePropertyLoader.getProperties();

        assertEquals(properties, obtainedProperties);
    }

    @Test
    void closeFileThrowsIOExceptionTest() throws IOException {
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Throwable> throwable = ArgumentCaptor.forClass(Throwable.class);

        doAnswer(invocation -> {
            throw new IOException();
        }).when(file).close();

        filePropertyLoader.loadProperties();

        verify(log).warn(message.capture(), throwable.capture());

        assertEquals(FilePropertyLoader.ExceptionMessage.FILE_CLOSE_ERROR.toString(), message.getValue());
        assertEquals(IOException.class, throwable.getValue().getClass());
    }
}