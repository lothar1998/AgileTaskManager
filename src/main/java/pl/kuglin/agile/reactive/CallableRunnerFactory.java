package pl.kuglin.agile.reactive;

import io.reactivex.rxjava3.functions.Consumer;

import java.util.concurrent.Callable;

public interface CallableRunnerFactory {
    <T> void createAndRun(Callable<? extends T> callable, Consumer<? super T> onSuccess, Consumer<? super Throwable> onError);
}
