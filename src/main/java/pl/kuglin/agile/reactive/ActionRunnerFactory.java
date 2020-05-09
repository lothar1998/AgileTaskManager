package pl.kuglin.agile.reactive;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.*;

public interface ActionRunnerFactory {
    void createAndRun(Action runnable, Action onSuccess, Consumer<? super Throwable> onError);
}
