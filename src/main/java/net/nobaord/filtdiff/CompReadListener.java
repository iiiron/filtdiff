package net.nobaord.filtdiff;

import java.util.List;

public interface CompReadListener<T> {
    List<T> next();

    boolean hasNext();

    void reset();
}
