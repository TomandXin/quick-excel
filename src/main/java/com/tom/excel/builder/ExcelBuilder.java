package com.tom.excel.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base Excel Builder
 *
 * @author tomxin
 * @date 2018-10-14
 * @since v1.0.0
 */
public class ExcelBuilder<T> {

    private final Supplier<T> instant;

    private List<Consumer<T>> instantModified = new ArrayList<>(8);

    public ExcelBuilder(Supplier<T> supplier) {
        instant = supplier;
    }

    public static <T> ExcelBuilder<T> of(Supplier<T> supplier) {
        return new ExcelBuilder<T>(supplier);
    }

    public <U> ExcelBuilder<T> with(BiConsumer<T,U> biConsumer, U value) {
        Consumer<T> consumer = instant -> biConsumer.accept(instant,value);
        instantModified.add(consumer);
        return this;
    }

    public T build() {
        T value = instant.get();
        instantModified.forEach(consumer -> consumer.accept(value));
        instantModified.clear();
        return value;
    }
}
