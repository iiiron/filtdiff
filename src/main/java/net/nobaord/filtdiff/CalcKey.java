package net.nobaord.filtdiff;

public interface CalcKey<T,K> {
    K key(T value);
}
