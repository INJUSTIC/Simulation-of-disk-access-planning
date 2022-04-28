package comparators;

import dysk.Blok;

import java.util.Comparator;

public class ComparatorSSTF implements Comparator<Blok> {
    private int currPozycja;

    public ComparatorSSTF(int currPozycja) {
        this.currPozycja = currPozycja;
    }

    @Override
    public int compare(Blok o1, Blok o2) {
        if (Math.abs(currPozycja - o1.getPozycja()) == Math.abs(currPozycja - o2.getPozycja())) return 0;
        else if (Math.abs(currPozycja - o1.getPozycja()) < Math.abs(currPozycja - o2.getPozycja())) return -1;
        else return 1;
    }
}