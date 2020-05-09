package pl.kuglin.agile.reactive;

import java.util.concurrent.Callable;
import io.reactivex.rxjava3.functions.*;

public interface CallableRunnerFactory {
    <T> void createAndRun(Callable<? extends T> callable, Consumer<? super T> onSuccess, Consumer<? super Throwable> onError);
}
