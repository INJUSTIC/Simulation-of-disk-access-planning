package comparators;

import dysk.Blok;

import java.util.Comparator;

public class ComparatorFCFS implements Comparator<Blok> {

    @Override
    public int compare(Blok o1, Blok o2) {
        if (o1.getCzasZgloszenia() == o2.getCzasZgloszenia()) return 0;
        else if(o1.getCzasZgloszenia() < o2.getCzasZgloszenia()) return -1;
        else return 1;
    }
}
