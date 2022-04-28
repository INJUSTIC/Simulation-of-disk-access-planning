package comparators;

import dysk.Blok;

import java.util.Comparator;

public class ComparatorSCAN implements Comparator<Blok> {
    private int typSortowania;

    public ComparatorSCAN(int typSortowania) {
        //1 albo -1
        this.typSortowania = typSortowania;
    }

    @Override
    public int compare(Blok o1, Blok o2) {
        if (o1.getPozycja() == o2.getPozycja()) return 0;
        else if(o1.getPozycja() < o2.getPozycja()) return -1*typSortowania;
        else return typSortowania;
    }
}
