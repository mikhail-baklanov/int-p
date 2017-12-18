package ru.relex.intertrust.suppression.interfaces;

import ru.relex.intertrust.suppression.Result;

import java.util.List;

/**
 * Визуализирует список результатов.
 */
public interface ListPrinter {
    void visualize(List<Result> list);
}
