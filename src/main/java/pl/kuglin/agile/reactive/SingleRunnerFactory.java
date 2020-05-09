package pl.kuglin.agile.reactive;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.Callable;
import io.reactivex.rxjava3.functions.*;

public class SingleRunnerFactory implements CallableRunnerFactory {
    @Override
    public <T> void createAndRun(Callable<? extends T> callable, Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
        Single<? extends T> single = Single.fromCallable(callable).subscribeOn(Schedulers.io());
        single.subscribe(onSuccess, onError);
    }
}
