/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SpisNapoi {

    protected Napoje[] napoj = new Napoje[20];
    public int ilosc;
    public double pieniadze;
    public int szukanyIndeks;

    public boolean wyswietl() {
        if (ilosc == 0) {
            return false;
        } else {
            for (int i = 0; i < ilosc; i++) {
                napoj[i].wyswietl();
            }
        }
        return true;
    }
    
//--------------------------------PLIK--------------------------------------
    
    public boolean odczyt(String nazwaAutomatu) {
        RandomAccessFile plikWejsciowy = null;
        try {
            plikWejsciowy = new RandomAccessFile(nazwaAutomatu, "r");
            plikWejsciowy.seek(0);
            for (int i = 0; i < napoj.length; i++) {
                napoj[i] = new Napoje();
                napoj[i].indeks = plikWejsciowy.readInt();
                napoj[i].nazwa = plikWejsciowy.readUTF();
                napoj[i].cena = plikWejsciowy.readDouble();
                napoj[i].ilosc = plikWejsciowy.readInt();
                if (napoj[i].indeks != 0) {
                    ilosc = i + 1;
                }
            }
            plikWejsciowy.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void zapis(String nazwaAutomatu) throws IOException {
        RandomAccessFile plikWyjsciowy = null;
        try {
            int i = 0;
            plikWyjsciowy = new RandomAccessFile(nazwaAutomatu, "rw");
            plikWyjsciowy.seek(0);
            while (plikWyjsciowy.getFilePointer() < plikWyjsciowy.length()) {
                plikWyjsciowy.writeInt(napoj[i].indeks);
                plikWyjsciowy.writeUTF(napoj[i].nazwa);
                plikWyjsciowy.writeDouble(napoj[i].cena);
                plikWyjsciowy.writeInt(napoj[i].ilosc);
                i++;
            }
        } finally {
            if (plikWyjsciowy != null) {
                plikWyjsciowy.close();
            }
        }
    }

//-------------------------------------PIENIADZE---------------------------------------
    
    public double kredyt(String portfel) throws IOException {
        BufferedReader plikWejsciowy = new BufferedReader(new FileReader(portfel));
        Automat.pustyPortfel(portfel);
        String pieniadzeString = plikWejsciowy.readLine();
        pieniadze = Double.parseDouble(pieniadzeString);
        plikWejsciowy.close();
        return pieniadze;
    }

    public void zapiszPieniadze(String portfel) throws IOException {
        FileWriter plikWyjsciowy = new FileWriter(portfel);
        Automat.pustyPortfel(portfel);
        plikWyjsciowy.write(String.valueOf(pieniadze));
        plikWyjsciowy.close();
    }

//-----------------------------------------KUPOWANIE---------------------------------------
    
    public void kup(int numer, String nazwaAutomatu, String portfel) throws IOException {
        kredyt(portfel);
        System.out.println("Kredyt: " + pieniadze + " zl");
        if (napoj[numer].cena > pieniadze) {
            System.out.println("Niewystarczajaca ilosc pieniedzy");
        } else {
            System.out.println("Prosze czekac...");
            napoj[numer].ilosc = napoj[numer].ilosc - 1;
            if (napoj[numer].ilosc == 0) {
                ilosc = ilosc - 1;
                Automat.usun(napoj[numer].nazwa, nazwaAutomatu);
                odczyt(nazwaAutomatu);
            }
            pieniadze = pieniadze - napoj[numer].cena;
            zapiszPieniadze(portfel);
            Automat.zmienIlosc(napoj[numer].nazwa, napoj[numer].ilosc, nazwaAutomatu);
            odczyt(nazwaAutomatu);
            System.out.println("Odbierz produkt");
        }
    }
    
//---------------------SORTOWANIE I WYSZUKIWANIE-----------------------

    public void sortujAlfabetycznie() {
        String[] tempNazwa = new String[ilosc];
        double[] tempCena = new double[ilosc];
        int[] tempIlosc = new int[ilosc];
        for (int i = 0; i < ilosc; i++) {
            for (int j = i; j < ilosc; j++) {
                if (napoj[i].nazwa.compareToIgnoreCase(napoj[j].nazwa) > 0) {
                    tempNazwa[i] = napoj[i].nazwa;
                    napoj[i].nazwa = napoj[j].nazwa;
                    napoj[j].nazwa = tempNazwa[i];

                    tempCena[i] = napoj[i].cena;
                    napoj[i].cena = napoj[j].cena;
                    napoj[j].cena = tempCena[i];

                    tempIlosc[i] = napoj[i].ilosc;
                    napoj[i].ilosc = napoj[j].ilosc;
                    napoj[j].ilosc = tempIlosc[i];
                }
            }
        }
    }

    public void sortujNajtansze() {
        String[] tempNazwa = new String[ilosc];
        double[] tempCena = new double[ilosc];
        int[] tempIlosc = new int[ilosc];
        for (int i = 0; i < ilosc; i++) {
            for (int j = i; j < ilosc; j++) {
                if (napoj[i].cena > napoj[j].cena) {
                    tempNazwa[i] = napoj[i].nazwa;
                    napoj[i].nazwa = napoj[j].nazwa;
                    napoj[j].nazwa = tempNazwa[i];

                    tempCena[i] = napoj[i].cena;
                    napoj[i].cena = napoj[j].cena;
                    napoj[j].cena = tempCena[i];

                    tempIlosc[i] = napoj[i].ilosc;
                    napoj[i].ilosc = napoj[j].ilosc;
                    napoj[j].ilosc = tempIlosc[i];
                }
            }
        }
    }

    public void sortujNajdrozsze() {
        String[] tempNazwa = new String[ilosc];
        double[] tempCena = new double[ilosc];
        int[] tempIlosc = new int[ilosc];
        for (int i = 0; i < ilosc; i++) {
            for (int j = i; j < ilosc; j++) {
                if (napoj[i].cena < napoj[j].cena) {
                    tempNazwa[i] = napoj[i].nazwa;
                    napoj[i].nazwa = napoj[j].nazwa;
                    napoj[j].nazwa = tempNazwa[i];

                    tempCena[i] = napoj[i].cena;
                    napoj[i].cena = napoj[j].cena;
                    napoj[j].cena = tempCena[i];

                    tempIlosc[i] = napoj[i].ilosc;
                    napoj[i].ilosc = napoj[j].ilosc;
                    napoj[j].ilosc = tempIlosc[i];
                }
            }
        }
    }

    public void sortujNajwiecej() {
        String[] tempNazwa = new String[ilosc];
        double[] tempCena = new double[ilosc];
        int[] tempIlosc = new int[ilosc];
        for (int i = 0; i < ilosc; i++) {
            for (int j = i; j < ilosc; j++) {
                if (napoj[i].ilosc < napoj[j].ilosc) {
                    tempNazwa[i] = napoj[i].nazwa;
                    napoj[i].nazwa = napoj[j].nazwa;
                    napoj[j].nazwa = tempNazwa[i];

                    tempCena[i] = napoj[i].cena;
                    napoj[i].cena = napoj[j].cena;
                    napoj[j].cena = tempCena[i];

                    tempIlosc[i] = napoj[i].ilosc;
                    napoj[i].ilosc = napoj[j].ilosc;
                    napoj[j].ilosc = tempIlosc[i];
                }
            }
        }
    }

    public void sortujNajmniej() {
        String[] tempNazwa = new String[ilosc];
        double[] tempCena = new double[ilosc];
        int[] tempIlosc = new int[ilosc];
        for (int i = 0; i < ilosc; i++) {
            for (int j = i; j < ilosc; j++) {
                if (napoj[i].ilosc > napoj[j].ilosc) {
                    tempNazwa[i] = napoj[i].nazwa;
                    napoj[i].nazwa = napoj[j].nazwa;
                    napoj[j].nazwa = tempNazwa[i];

                    tempCena[i] = napoj[i].cena;
                    napoj[i].cena = napoj[j].cena;
                    napoj[j].cena = tempCena[i];

                    tempIlosc[i] = napoj[i].ilosc;
                    napoj[i].ilosc = napoj[j].ilosc;
                    napoj[j].ilosc = tempIlosc[i];
                }
            }
        }
    }
    
    public boolean wyswietlPrzedzial(double najmniej, double najwiecej) {
        int istnieje = 0;
        for (int i = 0; i < ilosc; i++) {
            if (napoj[i].cena >= najmniej && napoj[i].cena <= najwiecej) {
                System.out.println(napoj[i].indeks + ". " + napoj[i].nazwa + " " + napoj[i].cena + " zl " + napoj[i].ilosc + " szt.");
                istnieje++;
            }
        }
        if (istnieje == 0) {
            return false;
        }
        return true;
    }
    
    public boolean szukajProdukt(String nazwaProduktu) {
        for (int i = 0; i < ilosc; i++) {
            if (napoj[i].nazwa.equals(nazwaProduktu)) {
                System.out.println(napoj[i].indeks + ". " + napoj[i].nazwa + " " + napoj[i].cena + " zl " + napoj[i].ilosc + " szt.");
                szukanyIndeks = napoj[i].indeks;
                return true;
            }
        }
        return false;
    }

}
