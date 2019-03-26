package net.nobaord.filtdiff;

import net.noboard.invoker.Consumer;
import net.noboard.invoker.Invoker;
import net.noboard.invoker.parallel.ParallelInvoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Comparer<U, T extends CompElement<U>, K extends CompElement<U>> {

    private List<T> diffListA = new ArrayList<>();

    private List<K> diffListB = new ArrayList<>();

    private List<CompReadListener<T>> listenerAs;

    private List<CompReadListener<K>> listenerBs;

    public Comparer(CompReadListener<T> listenerA, CompReadListener<K> listenerB) {
        this.listenerAs = new ArrayList<>();
        this.listenerAs.add(listenerA);
        this.listenerBs = new ArrayList<>();
        this.listenerBs.add(listenerB);
    }

    public Comparer() {
        this.listenerAs = new ArrayList<>();
        this.listenerBs = new ArrayList<>();
    }

    public List<T> getDiffListA() {
        return diffListA;
    }

    public List<K> getDiffListB() {
        return diffListB;
    }

    public void addListenerA(CompReadListener<T> listenerA) {
        this.listenerAs.add(listenerA);
    }

    public void addListenerB(CompReadListener<K> listenerB) {
        this.listenerBs.add(listenerB);
    }

    public void autoComp() {
        final int[] aIndex = {0};
        final int[] bIndex = {0};
        final boolean[] hasNext = new boolean[1];
        final CompReadListener<T>[] listenerA = new CompReadListener[]{null};
        final CompReadListener<K>[] listenerB = new CompReadListener[]{null};
        ParallelInvoker parallelInvoker = new ParallelInvoker();
        while (true) {
            hasNext[0] = false;
            parallelInvoker.call(new Consumer<Invoker>() {
                @Override
                public void accept(Invoker invoker) {
                    while (true) {
                        if (listenerA[0] != null && listenerA[0].hasNext()) {
                            addToA(listenerA[0].next());
                            hasNext[0] =true;
                            break;
                        } else if (aIndex[0] < listenerAs.size()) {
                            listenerA[0] = listenerAs.get(aIndex[0]++);
                        } else {
                            break;
                        }
                    }
                }
            }).and(new Consumer<Invoker>() {
                @Override
                public void accept(Invoker invoker) {
                    while (true) {
                        if (listenerB[0] != null && listenerB[0].hasNext()) {
                            addToB(listenerB[0].next());
                            hasNext[0] =true;
                            break;
                        } else if (bIndex[0] < listenerBs.size()) {
                            listenerB[0] = listenerBs.get(bIndex[0]++);
                        } else {
                            break;
                        }
                    }
                }
            }).start();

            if (hasNext[0]) {
                this.comp();
            } else {
                break;
            }
        }
    }

    private void comp() {
        initList(diffListA);
        initList(diffListB);
        comp(diffListA, diffListB);
    }

    private void comp(List<? extends CompElement<U>> list, List<? extends CompElement<U>> list2) {
        int iA = 0;
        int iB = 0;
        while (iA < list.size() && iB < list2.size()) {
            if (list.get(iA).compareTo(list2.get(iB)) == 0) {
                list.remove(iA);
                list2.remove(iB);
            } else if (list.get(iA).compareTo(list2.get(iB)) < 0) {
                iA++;
            } else if (list.get(iA).compareTo(list2.get(iB)) > 0) {
                iB++;
            }
        }
    }

    private void initList(List<? extends CompElement<U>> list) {
        sort(list);
    }

    private void sort(List<? extends CompElement<U>> list) {
        Collections.sort(list, new Comparator<CompElement<U>>() {
            public int compare(CompElement<U> o1, CompElement<U> o2) {
                return o1.compareTo(o2);
            }
        });
    }


    private void addToA(List<T> list) {
        diffListA.addAll(list);
    }

    private void addToB(List<K> list) {
        diffListB.addAll(list);
    }
}
