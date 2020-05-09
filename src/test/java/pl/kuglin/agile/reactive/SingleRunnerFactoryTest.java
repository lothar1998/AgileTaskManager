package pl.kuglin.agile.reactive;

import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Callable;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SingleRunnerFactoryTest {

    @Spy
    private final CallableRunnerFactory runner = new SingleRunnerFactory();

    @Captor
    private ArgumentCaptor<Callable<?>> callableCaptor;
    @Captor
    private ArgumentCaptor<Consumer<? super Object>> onSuccessCaptor;
    @Captor
    private ArgumentCaptor<Consumer<? super Throwable>> onErrorCaptor;

    @Test
    void createAndRunTest(){
        Callable<Object> runnable = () -> null;
        Consumer<Object> onSuccess = (o) -> { };
        Consumer<? super Throwable> onError = (throwable) -> {};

        runner.createAndRun(runnable, onSuccess, onError);

        verify(runner).createAndRun(callableCaptor.capture(), onSuccessCaptor.capture(), onErrorCaptor.capture());

        assertEquals(runnable, callableCaptor.getValue());
        assertEquals(onSuccess, onSuccessCaptor.getValue());
        assertEquals(onError, onErrorCaptor.getValue());
    }

}