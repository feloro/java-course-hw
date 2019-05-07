package ru.ifmo.rain.istomin.parallel;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class CallableTask<T,R> implements Callable<R> {

    private Function<? super T, ? extends R> f;
    private T arg;


    @Override
    public R call() throws Exception {
        return f.apply(arg);
    }

    public CallableTask(Function<? super T, ? extends R> f, T arg) {
        this.f = f;
        this.arg = arg;
    }
}
