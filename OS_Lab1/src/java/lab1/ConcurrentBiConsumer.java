package lab1;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public abstract class ConcurrentBiConsumer<T, U> implements BiConsumer<T, U> {
    private final AtomicBoolean called = new AtomicBoolean(false);

    public abstract void acceptOnce(T t, U u);

    @Override
    public void accept(T t, U u) {
        if (called.compareAndSet(false, true))
            acceptOnce(t, u);
    }
}
