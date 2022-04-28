package dysk;

import comparators.ComparatorEDF;
import comparators.ComparatorFCFS;
import comparators.ComparatorSCAN;
import comparators.ComparatorSSTF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Dysk {
    private Blok[] kolejka;
    private int currPozycja = 0;

    public Dysk(int ilosc, boolean hasPriority) {
        kolejka = new Blok[ilosc];
        for(int i = 0; i < ilosc; i++) {
            Random random = new Random();
            int czasZgloszenia = 1;
            int pozycja = random.nextInt(1,ilosc/10 + 1);
            int deadline = -1;
            if (hasPriority && random.nextInt(1, 4) == 1) deadline = 100 + czasZgloszenia + random.nextInt(1, 100);
            kolejka[i] = new Blok(czasZgloszenia, pozycja, deadline);
        }
    }

    //Presentation data
    public Dysk(boolean hasPriority) {
        if (hasPriority) {
            kolejka = new Blok[]{
                    new Blok(0, 60),
                    new Blok(40, 80, 100),
                    new Blok(70, 10, 180),
                    new Blok(70, 30, 300),
                    new Blok(125, 75),
                    new Blok(150, 1),
            };
        } else {
            kolejka = new Blok[]{
                    new Blok(0, 60),
                    new Blok(40, 80),
                    new Blok(70, 10),
                    new Blok(70, 30),
                    new Blok(125, 75),
                    new Blok(150, 1),
            };
        }
    }

    /* public int startFCFS() {
        return startFCFS(kolejka);
    }*/
    public int startSSTF() {
        return startSSTF(kolejka, 0);
    }
   /* public int startSCAN() {
        return startSCAN(kolejka);
    }
    public int startCSCAN() {
        return startCSCAN(kolejka);
    }*/
    public int startEDF() {
        return startEDF(kolejka, 0);
    }

    public int startFCFS() {
        ArrayList<Blok> kolejkaCopy = new ArrayList<>();
        for (Blok blok: kolejka) {
            kolejkaCopy.add(new Blok(blok));
        }
        int sumaPrzemieszczen = 0;
        Collections.sort(kolejkaCopy, new ComparatorFCFS());
        while (kolejkaCopy.size() != 0) {
            sumaPrzemieszczen += Math.abs(currPozycja - kolejkaCopy.get(0).getPozycja());
            currPozycja = kolejkaCopy.get(0).getPozycja();
            kolejkaCopy.remove(0);
        }

        currPozycja = 0;
        return sumaPrzemieszczen;
    }

    private int startSSTF(Blok[] kolejka, int t) {
        ArrayList<Blok> kolejkaCopy = new ArrayList<>();
        for (Blok blok: kolejka) {
            kolejkaCopy.add(new Blok(blok));
        }

        ArrayList<Blok> availableBlok = new ArrayList<>();
        int sumaPrzemieszczen = 0;

        while (kolejkaCopy.size() != 0 || availableBlok.size() != 0) {
            updateAvailable(kolejkaCopy, availableBlok, t);
            if (availableBlok.size() != 0) {
                Collections.sort(availableBlok, new ComparatorSSTF(currPozycja));
                //EDF
                Collections.sort(availableBlok, new ComparatorEDF());
                if (availableBlok.get(0).getDeadLine() > 0) {
                    ArrayList<Blok> bloks = new ArrayList();
                    bloks.addAll(availableBlok);
                    bloks.addAll(kolejkaCopy);
                    sumaPrzemieszczen += startEDF(bloks.toArray(new Blok[bloks.size()]),t);
                    availableBlok.clear();
                    kolejkaCopy.clear();
                    return sumaPrzemieszczen;
                }
                //EDF
                int roznica =  currPozycja - availableBlok.get(0).getPozycja();
                while(availableBlok.get(0).getPozycja() != currPozycja) {
                    Blok old = availableBlok.get(0);
                    sumaPrzemieszczen++;
                    t++;
                    if (roznica > 0) {
                        currPozycja--;
                    }
                    else {
                        currPozycja++;
                    }

                    updateAvailable(kolejkaCopy, availableBlok, t);
                    Collections.sort(availableBlok, new ComparatorSSTF(currPozycja));
                    //EDF
                    Collections.sort(availableBlok, new ComparatorEDF());
                    if (availableBlok.get(0).getDeadLine() > 0) {
                        ArrayList<Blok> bloks = new ArrayList();
                        bloks.addAll(availableBlok);
                        bloks.addAll(kolejkaCopy);
                        sumaPrzemieszczen += startEDF(bloks.toArray(new Blok[bloks.size()]),t);
                        return sumaPrzemieszczen;
                    }
                    //EDF
                    if (old != availableBlok.get(0)) {
                        roznica = currPozycja - availableBlok.get(0).getPozycja();
                    }
                }
                currPozycja = availableBlok.get(0).getPozycja();
                availableBlok.remove(0);
            }
            else {
                t++;
            }
        }

        currPozycja = 0;
        return sumaPrzemieszczen;
    }

    public int startSCAN() {
        ArrayList<Blok> kolejkaCopy = new ArrayList<>();
        for (Blok blok: kolejka) {
            kolejkaCopy.add(new Blok(blok));
        }

        ArrayList<Blok> availableBlok = new ArrayList<>();
        int sumaPrzemieszczen = 0;
        int t = 0;
        while (kolejkaCopy.size() != 0 || availableBlok.size() != 0) {
            updateAvailable(kolejkaCopy, availableBlok, t);
            if (availableBlok.size() != 0) {
                Collections.sort(availableBlok, new ComparatorSCAN(1));
                Blok curr = null;
                while (!availableBlok.isEmpty() && currPozycja < availableBlok.get(availableBlok.size() - 1).getPozycja()) {
                    currPozycja++;
                    for (int i = 0; i < availableBlok.size(); i++) {
                        if (availableBlok.get(i).getPozycja() >= currPozycja){
                            curr = availableBlok.get(i);
                            break;
                        }
                    }
                    if (curr == null)  curr = availableBlok.get(0);
                    if (currPozycja == curr.getPozycja()) {
                        availableBlok.remove(curr);
                    }
                    t++;
                    sumaPrzemieszczen++;
                    updateAvailable(kolejkaCopy, availableBlok, t);
                    Collections.sort(availableBlok, new ComparatorSCAN(1));
                }
            }
            if (availableBlok.size() != 0) {
                Collections.sort(availableBlok, new ComparatorSCAN(-1));
                Blok curr = null;
                while (!availableBlok.isEmpty() && currPozycja > availableBlok.get(availableBlok.size() - 1).getPozycja()) {
                    currPozycja--;
                    for (int i = 0; i < availableBlok.size(); i++) {
                        if (availableBlok.get(i).getPozycja() >= currPozycja){
                            curr = availableBlok.get(i);
                            break;
                        }
                    }
                    if (curr == null)  curr = availableBlok.get(0);
                    if (currPozycja == curr.getPozycja()) {
                        availableBlok.remove(curr);
                    }
                    t++;
                    sumaPrzemieszczen++;
                    updateAvailable(kolejkaCopy, availableBlok, t);
                    Collections.sort(availableBlok, new ComparatorSCAN(-1));
                }
            }
            else {
                t++;
            }
        }

        currPozycja = 0;
        return sumaPrzemieszczen;
    }

    public int startCSCAN() {
        ArrayList<Blok> kolejkaCopy = new ArrayList<>();
        for (Blok blok: kolejka) {
            kolejkaCopy.add(new Blok(blok));
        }

        ArrayList<Blok> availableBlok = new ArrayList<>();
        int sumaPrzemieszczen = 0;
        int t = 0;
        while (kolejkaCopy.size() != 0 || availableBlok.size() != 0) {
            updateAvailable(kolejkaCopy, availableBlok, t);
            if (availableBlok.size() != 0) {
                Collections.sort(availableBlok, new ComparatorSCAN(1));
                Blok curr;
                while (!availableBlok.isEmpty() && currPozycja < availableBlok.get(availableBlok.size() - 1).getPozycja()) {
                    int i;
                    currPozycja++;
                    for (i = 0; i < availableBlok.size(); i++) {
                        if (availableBlok.get(i).getPozycja() >= currPozycja) break;
                    }
                    curr = availableBlok.get(i);
                    if (currPozycja == curr.getPozycja()) {
                        availableBlok.remove(curr);
                    }
                    t++;
                    sumaPrzemieszczen++;
                    updateAvailable(kolejkaCopy, availableBlok, t);
                    Collections.sort(availableBlok, new ComparatorSCAN(1));
                }
                if (!availableBlok.isEmpty()) {
                    sumaPrzemieszczen += currPozycja - availableBlok.get(0).getPozycja();
                    currPozycja = availableBlok.get(0).getPozycja();
                    availableBlok.remove(0);
                }
            }

            else {
                t++;
            }
        }

        currPozycja = 0;
        return sumaPrzemieszczen;
    }

    private int startEDF(Blok[] kolejka, int t) {
        ArrayList<Blok> kolejkaCopy = new ArrayList<>();
        for (Blok blok: kolejka) {
            kolejkaCopy.add(new Blok(blok));
        }

        ArrayList<Blok> availableBlok = new ArrayList<>();
        int sumaPrzemieszczen = 0;

        while (kolejkaCopy.size() != 0 || availableBlok.size() != 0) {
            updateAvailable(kolejkaCopy, availableBlok, t);
            if (availableBlok.size() != 0) {
                Collections.sort(availableBlok, new ComparatorEDF());
                if (availableBlok.get(0).getDeadLine() < 0) {
                    ArrayList<Blok> bloks = new ArrayList();
                    bloks.addAll(availableBlok);
                    bloks.addAll(kolejkaCopy);
                    sumaPrzemieszczen += startSSTF(bloks.toArray(new Blok[bloks.size()]),t);
                    return sumaPrzemieszczen;
                }
                int roznica =  currPozycja - availableBlok.get(0).getPozycja();
                while(availableBlok.get(0).getPozycja() != currPozycja) {
                    Blok old = availableBlok.get(0);
                    sumaPrzemieszczen++;
                    t++;
                    if (roznica > 0) {
                        currPozycja--;
                    }
                    else {
                        currPozycja++;
                    }
                    updateAvailable(kolejkaCopy, availableBlok, t);
                    Collections.sort(availableBlok, new ComparatorEDF());
                    if (old != availableBlok.get(0)) {
                        roznica = currPozycja - availableBlok.get(0).getPozycja();
                    }
                }
                currPozycja = availableBlok.get(0).getPozycja();
                availableBlok.remove(0);
            }
            else {
                t++;
            }
        }

        currPozycja = 0;
        return sumaPrzemieszczen;
    }

    public int startFDSCAN() {
        ArrayList<Blok> kolejkaCopy = new ArrayList<>();
        for (Blok blok: kolejka) {
            kolejkaCopy.add(new Blok(blok));
        }

        ArrayList<Blok> availableBlok = new ArrayList<>();
        int sumaPrzemieszczen = 0;
        int t = 0;
        while (kolejkaCopy.size() != 0 || availableBlok.size() != 0) {
            updateAvailable(kolejkaCopy, availableBlok, t);
            if (availableBlok.size() != 0) {
                Collections.sort(availableBlok, new ComparatorSCAN(1));
                Blok curr = null;
                while (!availableBlok.isEmpty() && currPozycja < availableBlok.get(availableBlok.size() - 1).getPozycja()) {
                    currPozycja++;
                    int i;
                    for (i = 0; i < availableBlok.size(); i++) {
                        if (availableBlok.get(i).getPozycja() >= currPozycja) break;
                    }
                    for (int j = i; j < availableBlok.size()-i; j++) {
                        if (availableBlok.get(j).getDeadLine() > -1) {
                            curr = availableBlok.get(j);
                            break;
                        }
                    }
                    if(curr == null) curr = availableBlok.get(0);
                    if (currPozycja == curr.getPozycja()) {
                        availableBlok.remove(curr);
                        curr = null;
                    }

                    //t++;
                    sumaPrzemieszczen++;
                    updateAvailable(kolejkaCopy, availableBlok, t);
                    Collections.sort(availableBlok, new ComparatorSCAN(1));
                    t++;
                }
            }
            if (availableBlok.size() != 0) {
                Collections.sort(availableBlok, new ComparatorSCAN(-1));
                Blok curr = null;
                while (!availableBlok.isEmpty() && currPozycja > availableBlok.get(availableBlok.size() - 1).getPozycja()) {
                    currPozycja--;
                    int i;
                    for (i = 0; i < availableBlok.size(); i++) {
                        if (availableBlok.get(i).getPozycja() <= currPozycja) break;
                    }

                    for (int j = i; j < availableBlok.size()-i; j++) {
                        if (availableBlok.get(j).getDeadLine() > -1) {
                            curr = availableBlok.get(j);
                            break;
                        }
                    }
                    if(curr == null) curr = availableBlok.get(i);
                    if (currPozycja == curr.getPozycja()) {
                        availableBlok.remove(curr);
                        curr = null;
                    }

                    //t++;
                    sumaPrzemieszczen++;
                    updateAvailable(kolejkaCopy, availableBlok, t);
                    Collections.sort(availableBlok, new ComparatorSCAN(-1));
                    t++;
                }
            }
            else {
                t++;
            }
        }

        currPozycja = 0;
        return sumaPrzemieszczen;
    }

    private void updateAvailable(ArrayList<Blok> bloky, ArrayList<Blok> aval, double t) {
        for (int i = 0; i < bloky.size(); i++) {
            if (bloky.get(i).getCzasZgloszenia() <= t) {
                boolean isFree = true;
                for (int j = 0; j < aval.size(); j++) {
                    if (aval.get(j).getPozycja() == bloky.get(i).getPozycja()) {
                        isFree = false;
                        break;
                    }
                }
                if (isFree) {
                    aval.add(bloky.get(i));
                    bloky.remove(i);
                    i--;
                }
            }
        }
    }
}
