package main;

import dysk.Blok;
import dysk.Dysk;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

         //startPresentDataTest();

       startTest(10);
       startTest(100);
       startTest(1000);
    }
    public static void startTest(int ilosc) {
        Dysk dysk = new Dysk(ilosc, false);
        System.out.println("ilosc: " + ilosc);
        System.out.println("FCFS:" + dysk.startFCFS());
        System.out.println("SSTF:" + dysk.startSSTF());
        System.out.println("SCAN:" + dysk.startSCAN());
        System.out.println("CSCAN:" + dysk.startCSCAN());

        Dysk dysk2 = new Dysk(ilosc, true);
        System.out.println("EDF:" + dysk2.startEDF());
        System.out.println("FD-SCAN:" + dysk2.startFDSCAN());
        System.out.println();

    }
    public static void startPresentDataTest() {
        Dysk dysk = new Dysk(false);
        System.out.println("FCFS:" + dysk.startFCFS());
        System.out.println("SSTF:" + dysk.startSSTF());
        System.out.println("SCAN:" + dysk.startSCAN());
        System.out.println("CSCAN:" + dysk.startCSCAN());

        Dysk dysk2 = new Dysk(true);
        System.out.println("EDF:" + dysk2.startEDF());
        System.out.println("FD-SCAN:" + dysk2.startFDSCAN());
        System.out.println();
    }
}
