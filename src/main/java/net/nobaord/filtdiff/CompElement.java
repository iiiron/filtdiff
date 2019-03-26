package net.nobaord.filtdiff;

public interface CompElement<T> extends Comparable<CompElement<T>> {

    /**
     * 对比关键字
     * @return
     */
    T compKey();
}
