import net.nobaord.filtdiff.CompElement;
import net.nobaord.filtdiff.CompReadListener;
import net.nobaord.filtdiff.Comparer;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] arg) {
        Comparer<String, CompElement<String>, CompElement<String>> comparer = new Comparer<>();
        comparer.addListenerA(new CompReadListener<CompElement<String>>() {

            int count = 10;

            int i = 0;

            @Override
            public List<CompElement<String>> next() {
                List<CompElement<String>> list = new ArrayList<>();
                final int ai = i;
                for (int t = 0; t < 100000; t++) {
                    list.add(new CompElement<String>() {
                        @Override
                        public String compKey() {
                            return String.valueOf(ai);
                        }

                        @Override
                        public int compareTo(CompElement<String> o) {
                            return this.compKey().compareTo(o.compKey());
                        }
                    });
                }
                i++;
                return list;
            }

            @Override
            public boolean hasNext() {
                return i <= count;
            }

            @Override
            public void reset() {

            }
        });
        comparer.addListenerB(new CompReadListener<CompElement<String>>() {

            int count = 10;

            int i = 0;

            @Override
            public List<CompElement<String>> next() {
                List<CompElement<String>> list = new ArrayList<>();
                final int ai = i;
                for (int t = 100000; t > 0; t--) {
                    list.add(new CompElement<String>() {
                        @Override
                        public String compKey() {
                            return String.valueOf(ai);
                        }

                        @Override
                        public int compareTo(CompElement<String> o) {
                            return this.compKey().compareTo(o.compKey());
                        }
                    });
                }
                i++;
                return list;
            }

            @Override
            public boolean hasNext() {
                return i <= count;
            }

            @Override
            public void reset() {

            }
        });
        comparer.autoComp();
        System.out.println(comparer.getDiffListA());
        System.out.println(comparer.getDiffListB());
    }
}
