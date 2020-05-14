package pl.kuglin.agile.reactive;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CompletableRunnerFactory implements ActionRunnerFactory {
    @Override
    public void createAndRun(Action runnable, Action onSuccess, Consumer<? super Throwable> onError) {
        Completable completable = Completable.fromAction(runnable).subscribeOn(Schedulers.io());
        completable.subscribe(onSuccess, onError);
    }
}
