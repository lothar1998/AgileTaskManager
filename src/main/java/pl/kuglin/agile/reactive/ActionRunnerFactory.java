package pl.kuglin.agile.reactive;

import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

public interface ActionRunnerFactory {
    void createAndRun(Action runnable, Action onSuccess, Consumer<? super Throwable> onError);
}
