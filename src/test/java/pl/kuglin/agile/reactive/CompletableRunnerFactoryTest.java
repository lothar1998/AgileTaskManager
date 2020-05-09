package pl.kuglin.agile.reactive;

import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CompletableRunnerFactoryTest {

    @Spy
    private final ActionRunnerFactory runner = new CompletableRunnerFactory();

    @Captor
    private ArgumentCaptor<Action> runnableCaptor;
    @Captor
    private ArgumentCaptor<Action> onSuccessCaptor;
    @Captor
    private ArgumentCaptor<Consumer<? super Throwable>> onErrorCaptor;

    @Test
    void createAndRunTest(){
        Action runnable = () -> { };
        Action onSuccess = () -> { };
        Consumer<? super Throwable> onError = (throwable) -> {};

        runner.createAndRun(runnable, onSuccess, onError);

        verify(runner).createAndRun(runnableCaptor.capture(), onSuccessCaptor.capture(), onErrorCaptor.capture());

        assertEquals(runnable, runnableCaptor.getValue());
        assertEquals(onSuccess, onSuccessCaptor.getValue());
        assertEquals(onError, onErrorCaptor.getValue());
    }

}