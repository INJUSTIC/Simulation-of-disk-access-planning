package comparators;

import dysk.Blok;

import java.util.Comparator;

public class ComparatorEDF implements Comparator<Blok> {

    @Override
    public int compare(Blok o1, Blok o2) {
        if (o1.getDeadLine() == o2.getDeadLine()) return 0;
        else if (o1.getDeadLine() == -1 && o2.getDeadLine() != -1) return 1;
        else if((o2.getDeadLine() == -1 && o1.getDeadLine() != -1) || (o1.getDeadLine() < o2.getDeadLine())) return -1;
        else return 1;
    }
}
