package dysk;

import java.util.Random;

public class Blok {
    private int czasZgloszenia;
    private int pozycja;
    private int deadLine;
    public Blok(Blok blok) {
        this.czasZgloszenia = blok.czasZgloszenia;
        this.pozycja = blok.pozycja;
        this.deadLine = blok.deadLine;
    }
    public Blok(int czasZgloszenia, int pozycja) {
        this.czasZgloszenia = czasZgloszenia;
        this.pozycja = pozycja;
        this.deadLine = -1;
    }
    public Blok(int czasZgloszenia, int pozycja, int deadLine) {
        this.czasZgloszenia = czasZgloszenia;
        this.pozycja = pozycja;
        this.deadLine = deadLine;
    }

    public int getCzasZgloszenia() {
        return czasZgloszenia;
    }

    public int getPozycja() {
        return pozycja;
    }

    public int getDeadLine() {
        return deadLine;
    }
}
