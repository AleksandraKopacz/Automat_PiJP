package automat;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Automat {

//-------------------------------MODYFIKACJA-------------------------------------------
    public static void modyfikacja(String nazwaAutomatu) throws IOException {
        Scanner in = new Scanner(System.in);
        int tryb, powrot;
        do {
            System.out.println("AUTOMAT " + nazwaAutomatu);
            wyswietlAutomat(nazwaAutomatu);
            do {
                System.out.println("MODYFIKUJ AUTOMAT\nWybierz co chcesz zrobic\n1. Dodaj napoj\n2. Usun napoj\n3. Zmien cene\n4. Zmien ilosc");
                tryb = in.nextInt();
            } while (tryb != 1 && tryb != 2 && tryb != 3 && tryb != 4);
            switch (tryb) {
                case 1:
                    dodaj(nazwaAutomatu);
                    break;
                case 2:
                    System.out.println("Podaj nazwe produktu:");
                    in.nextLine();
                    String usunNazwa = in.nextLine();
                    usun(usunNazwa, nazwaAutomatu);
                    break;
                case 3:
                    zmienCene(nazwaAutomatu);
                    break;
                case 4:
                    int nowaIlosc;
                    System.out.println("Podaj nazwe produktu, ktorego ilosc ma zostac zmieniona:");
                    in.nextLine();
                    String Nazwa = in.nextLine();
                    do {
                        System.out.println("Podaj nowa ilosc produktu:");
                        nowaIlosc = in.nextInt();
                    } while (nowaIlosc <= 0);
                    zmienIlosc(Nazwa, nowaIlosc, nazwaAutomatu);
                    break;
            }
            do {
                System.out.println("Czy chcesz zmodyfikowac automat ponownie?\n1. Tak\n2. Nie");
                powrot = in.nextInt();
            } while (powrot != 1 && powrot != 2);
        } while (powrot != 2);
    }

    public static void dodaj(String nazwaAutomatu) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Podaj nazwe produktu:");
        String nowyNazwa = in.nextLine();
        double nowyCena;
        do {
            System.out.println("Podaj cene produktu:");
            nowyCena = in.nextDouble();
        } while (nowyCena <= 0);
        int nowyIlosc;
        do {
            System.out.println("Podaj ilosc produktow:");
            nowyIlosc = in.nextInt();
        } while (nowyIlosc <= 0);

        int istnieje = 0;
        RandomAccessFile plikWyjsciowy = null;
        try {
            plikWyjsciowy = new RandomAccessFile(nazwaAutomatu, "rw");
            plikWyjsciowy.seek(0);

            int[] Indeks = new int[20];
            String[] Produkt = new String[20];
            double[] Cena = new double[20];
            int[] Ilosc = new int[20];
            int i = 0;
            while (plikWyjsciowy.getFilePointer() < plikWyjsciowy.length()) {
                Indeks[i] = plikWyjsciowy.readInt();
                Produkt[i] = plikWyjsciowy.readUTF();
                Cena[i] = plikWyjsciowy.readDouble();
                Ilosc[i] = plikWyjsciowy.readInt();
                if (Produkt[i].equals(nowyNazwa)) {
                    istnieje = 1;
                }
                i++;
            }

            if (istnieje != 0) {
                System.out.println("Produkt juz jest w automacie");
            } else if (Produkt[Produkt.length - 1] != null) {
                System.out.println("Automat jest juz pelen. Nalezy usunac jakis produkt przed dodaniem nowego.");
            } else {
                plikWyjsciowy.writeInt(i + 1);
                plikWyjsciowy.writeUTF(nowyNazwa);
                plikWyjsciowy.writeDouble(nowyCena);
                plikWyjsciowy.writeInt(nowyIlosc);
            }
        } finally {
            if (plikWyjsciowy != null) {
                plikWyjsciowy.close();
            }
        }
    }

    public static void usun(String usunNazwa, String nazwaAutomatu) throws IOException {
        Scanner in = new Scanner(System.in);

        RandomAccessFile plikWyjsciowy = null;
        try {
            plikWyjsciowy = new RandomAccessFile(nazwaAutomatu, "rw");
            plikWyjsciowy.seek(0);

            int[] Indeks = new int[20];
            String[] Produkt = new String[20];
            double[] Cena = new double[20];
            int[] Ilosc = new int[20];
            String[] tempProdukt = new String[19];
            double[] tempCena = new double[19];
            int[] tempIlosc = new int[19];
            int i = 0;
            int indeks = 0;
            while (plikWyjsciowy.getFilePointer() < plikWyjsciowy.length()) {
                Indeks[i] = plikWyjsciowy.readInt();
                Produkt[i] = plikWyjsciowy.readUTF();
                Cena[i] = plikWyjsciowy.readDouble();
                Ilosc[i] = plikWyjsciowy.readInt();
                if (Produkt[i].equals(usunNazwa)) {
                    indeks = i;
                }
                i++;
            }
            System.out.println(indeks);
            for (int j = 0; j < indeks; j++) {
                tempProdukt[j] = Produkt[j];
                tempCena[j] = Cena[j];
                tempIlosc[j] = Ilosc[j];
            }
            for (int j = indeks; j < tempProdukt.length; j++) {
                tempProdukt[j] = Produkt[j + 1];
                tempCena[j] = Cena[j + 1];
                tempIlosc[j] = Ilosc[j + 1];
            }
            for (int j = Indeks.length - 1; j > 0; j--) {
                if (Indeks[j] != 0) {
                    Indeks[j] = 0;
                    break;
                }
            }
            plikWyjsciowy.setLength(0);
            for (int j = 0; j < tempProdukt.length; j++) {
                if (tempProdukt[j] == null) {
                    break;
                }
                plikWyjsciowy.writeInt(Indeks[j]);
                plikWyjsciowy.writeUTF(tempProdukt[j]);
                plikWyjsciowy.writeDouble(tempCena[j]);
                plikWyjsciowy.writeInt(tempIlosc[j]);
            }
        } catch (EOFException e) {
            System.out.println("Taki produkt nie istnieje");
        } finally {
            if (plikWyjsciowy != null) {
                plikWyjsciowy.close();
            }
        }
    }

    public static void zmienCene(String nazwaAutomatu) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Podaj nazwe produktu, ktorego cena ma zostac zmieniona:");
        String Nazwa = in.nextLine();
        double nowaCena;
        do {
            System.out.println("Podaj nowa cene produktu:");
            nowaCena = in.nextDouble();
        } while (nowaCena <= 0);

        RandomAccessFile plikWyjsciowy = null;
        try {
            plikWyjsciowy = new RandomAccessFile(nazwaAutomatu, "rw");
            plikWyjsciowy.seek(0);

            int istnieje = 0;
            int indeks = 0;
            int[] Indeks = new int[20];
            String[] Produkt = new String[20];
            double[] Cena = new double[20];
            int[] Ilosc = new int[20];
            int i = 0;
            while (plikWyjsciowy.getFilePointer() < plikWyjsciowy.length()) {
                Indeks[i] = plikWyjsciowy.readInt();
                Produkt[i] = plikWyjsciowy.readUTF();
                Cena[i] = plikWyjsciowy.readDouble();
                Ilosc[i] = plikWyjsciowy.readInt();
                if (Produkt[i].equals(Nazwa)) {
                    istnieje = 1;
                    indeks = i;
                }
                i++;
            }
            if (istnieje == 1) {
                Cena[indeks] = nowaCena;
                System.out.println("Cena zostala zmieniona");
            } else {
                System.out.println("Taki produkt nie istnieje");
            }
            plikWyjsciowy.setLength(0);
            for (int j = 0; j < Produkt.length; j++) {
                if (Produkt[j] == null) {
                    break;
                }
                plikWyjsciowy.writeInt(Indeks[j]);
                plikWyjsciowy.writeUTF(Produkt[j]);
                plikWyjsciowy.writeDouble(Cena[j]);
                plikWyjsciowy.writeInt(Ilosc[j]);
            }
        } finally {
            if (plikWyjsciowy != null) {
                plikWyjsciowy.close();
            }
        }
    }

    public static void zmienIlosc(String Nazwa, int nowaIlosc, String nazwaAutomatu) throws IOException {
        RandomAccessFile plikWyjsciowy = null;
        try {
            plikWyjsciowy = new RandomAccessFile(nazwaAutomatu, "rw");
            plikWyjsciowy.seek(0);

            int istnieje = 0;
            int indeks = 0;
            int[] Indeks = new int[20];
            String[] Produkt = new String[20];
            double[] Cena = new double[20];
            int[] Ilosc = new int[20];
            int i = 0;
            while (plikWyjsciowy.getFilePointer() < plikWyjsciowy.length()) {
                Indeks[i] = plikWyjsciowy.readInt();
                Produkt[i] = plikWyjsciowy.readUTF();
                Cena[i] = plikWyjsciowy.readDouble();
                Ilosc[i] = plikWyjsciowy.readInt();
                if (Produkt[i].equals(Nazwa)) {
                    istnieje = 1;
                    indeks = i;
                }
                i++;
            }
            if (istnieje == 1) {
                Ilosc[indeks] = nowaIlosc;
            } else {
                System.out.println("Taki produkt nie istnieje");
            }
            plikWyjsciowy.setLength(0);
            for (int j = 0; j < Produkt.length; j++) {
                if (Produkt[j] == null) {
                    break;
                }
                plikWyjsciowy.writeInt(Indeks[j]);
                plikWyjsciowy.writeUTF(Produkt[j]);
                plikWyjsciowy.writeDouble(Cena[j]);
                plikWyjsciowy.writeInt(Ilosc[j]);
            }
        } finally {
            if (plikWyjsciowy != null) {
                plikWyjsciowy.close();
            }
        }
    }

//--------------------------------KUPOWANIE-------------------------------------------
    public static void kupowanie(String nazwaAutomatu, String portfel) throws IOException {
        Scanner in = new Scanner(System.in);
        SpisNapoi spis = new SpisNapoi();
        int tryb, powrot, numer;
        System.out.println("KUP NAPOJ");
        do {
            spis.odczyt(nazwaAutomatu);
            do {
                System.out.println("Jak chcesz wyswietlic automat?\n1. Domyslnie\n2. Sortuj alfabetycznie\n3. Sortuj od najtanszego\n4. Sortuj od najdrozszego\n5. Sortuj od najwiekszej ilosci\n6. Sortuj od najmniejszej ilosci\n7. Wyswietl w przedziale cenowym\n8. Znajdz produkt");
                tryb = in.nextInt();
            } while (tryb < 1 || tryb > 8);
            switch (tryb) {
                case 1:
                    if (!spis.wyswietl()) {
                        System.out.println("Automat jest pusty");
                        break;
                    }
                    do {
                        System.out.println("Podaj numer produktu, ktory chcesz kupic");
                        numer = in.nextInt();
                    } while (numer < 1 || numer > spis.ilosc);
                    spis.kup(numer - 1, nazwaAutomatu, portfel);
                    break;
                case 2:
                    spis.sortujAlfabetycznie();
                    if (!spis.wyswietl()) {
                        System.out.println("Automat jest pusty");
                        break;
                    }
                    do {
                        System.out.println("Podaj numer produktu, ktory chcesz kupic");
                        numer = in.nextInt();
                    } while (numer < 1 || numer > spis.ilosc);
                    spis.kup(numer - 1, nazwaAutomatu, portfel);
                    break;
                case 3:
                    spis.sortujNajtansze();
                    if (!spis.wyswietl()) {
                        System.out.println("Automat jest pusty");
                        break;
                    }
                    do {
                        System.out.println("Podaj numer produktu, ktory chcesz kupic");
                        numer = in.nextInt();
                    } while (numer < 1 || numer > spis.ilosc);
                    spis.kup(numer - 1, nazwaAutomatu, portfel);
                    break;
                case 4:
                    spis.sortujNajdrozsze();
                    if (!spis.wyswietl()) {
                        System.out.println("Automat jest pusty");
                        break;
                    }
                    do {
                        System.out.println("Podaj numer produktu, ktory chcesz kupic");
                        numer = in.nextInt();
                    } while (numer < 1 || numer > spis.ilosc);
                    spis.kup(numer - 1, nazwaAutomatu, portfel);
                    break;
                case 5:
                    spis.sortujNajwiecej();
                    if (!spis.wyswietl()) {
                        System.out.println("Automat jest pusty");
                        break;
                    }
                    do {
                        System.out.println("Podaj numer produktu, ktory chcesz kupic");
                        numer = in.nextInt();
                    } while (numer < 1 || numer > spis.ilosc);
                    spis.kup(numer - 1, nazwaAutomatu, portfel);
                    break;
                case 6:
                    spis.sortujNajmniej();
                    if (!spis.wyswietl()) {
                        System.out.println("Automat jest pusty");
                        break;
                    }
                    do {
                        System.out.println("Podaj numer produktu, ktory chcesz kupic");
                        numer = in.nextInt();
                    } while (numer < 1 || numer > spis.ilosc);
                    spis.kup(numer - 1, nazwaAutomatu, portfel);
                    break;
                case 7:
                    double najmniej,
                     najwiecej;
                    do {
                        System.out.println("Cena od:");
                        najmniej = in.nextDouble();
                        System.out.println("Cena do:");
                        najwiecej = in.nextDouble();
                    } while (najmniej <= 0 && najwiecej <= 0);

                    if (!spis.wyswietlPrzedzial(najmniej, najwiecej)) {
                        System.out.println("Brak produktow w podanym przedziale");
                        break;
                    }
                    do {
                        System.out.println("Podaj numer produktu, ktory chcesz kupic");
                        numer = in.nextInt();
                    } while (numer < 1 || numer > spis.ilosc);
                    spis.kup(numer - 1, nazwaAutomatu, portfel);
                    break;
                case 8:
                    String szukanyProdukt;
                    System.out.println("Podaj nazwe szukanego produktu");
                    in.nextLine();
                    szukanyProdukt = in.nextLine();
                    if (!spis.szukajProdukt(szukanyProdukt)) {
                        System.out.println("Szukany produkt nie istnieje");
                        break;
                    }
                    do {
                        System.out.println("Podaj numer produktu, ktory chcesz kupic");
                        numer = in.nextInt();
                    } while (numer != spis.szukanyIndeks);
                    spis.kup(numer - 1, nazwaAutomatu, portfel);
                    break;
            }
            do {
                System.out.println("Czy chcesz kupic ponownie?\n1. Tak\n2. Nie");
                powrot = in.nextInt();
            } while (powrot != 1 && powrot != 2);
        } while (powrot != 2);
    }

//-------------------------------PORTFEL------------------------------------
    public static void portfel(String portfel) throws IOException {
        Scanner in = new Scanner(System.in);
        String zawartoscString;
        double pieniadze, zawartoscDouble;
        int powrot;
        do {
            do {
                System.out.println("Podaj ilosc pieniedzy jaka chcesz dodac do portfela. Nie moze byc to liczba ujemna lub 0.");
                pieniadze = in.nextDouble();
            } while (pieniadze <= 0);
            BufferedReader plikWejsciowy = new BufferedReader(new FileReader(portfel));
            pustyPortfel(portfel);
            zawartoscString = plikWejsciowy.readLine();
            zawartoscDouble = Double.parseDouble(zawartoscString);
            pieniadze = pieniadze + zawartoscDouble;
            plikWejsciowy.close();
            FileWriter plikWyjsciowy = new FileWriter(portfel);
            plikWyjsciowy.write(String.valueOf(pieniadze));
            System.out.println("W portfelu znajduje sie teraz " + pieniadze + " zl");
            plikWyjsciowy.close();
            do {
                System.out.println("Czy chcesz zmodyfikowac portfel ponownie?\n1. Tak\n2. Nie");
                powrot = in.nextInt();
            } while (powrot != 1 && powrot != 2);
        } while (powrot != 2);
    }

    public static void pustyPortfel(String portfel) throws IOException {
        BufferedReader plikWejsciowy = new BufferedReader(new FileReader(portfel));
        String zawartosc = plikWejsciowy.readLine();
        plikWejsciowy.close();
        FileWriter plikWyjsciowy = new FileWriter(portfel);
        if (zawartosc == null) {
            plikWyjsciowy.write("0");
        } else {
            plikWyjsciowy.write(zawartosc);
        }
        plikWyjsciowy.close();
    }

//-------------------------------WYSWIETL ZAWARTOSC PLIKU------------------------------------
    public static void wyswietlAutomat(String nazwaAutomatu) throws IOException {
        RandomAccessFile plikWejsciowy = null;
        try {
            plikWejsciowy = new RandomAccessFile(nazwaAutomatu, "r");
            while (plikWejsciowy.getFilePointer() < plikWejsciowy.length()) {
                System.out.println(plikWejsciowy.readInt() + ". " + plikWejsciowy.readUTF() + " " + plikWejsciowy.readDouble() + " zl " + plikWejsciowy.readInt() + " szt.");
            }
        } finally {
            if (plikWejsciowy != null) {
                plikWejsciowy.close();
            }
        }
    }

//---------------------------------------------------------------------------------------
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner in = new Scanner(System.in);
        int tryb, powrot;
        SpisNapoi spis = new SpisNapoi();
        String nazwaAutomatu;
        String portfel;
        do {
            do {
                System.out.println("MENU GLOWNE\nPodaj nazwe pliku z automatem");
                nazwaAutomatu = in.nextLine();
                System.out.println("Podaj nazwe pliku z portfelem");
                portfel = in.nextLine();
                System.out.println("Wybierz co chcesz zrobic:\n1. Modyfikuj automat\n2. Kup napoj\n3. Modyfikuj portfel");
                tryb = in.nextInt();
            } while (tryb != 1 && tryb != 2 && tryb != 3);

            switch (tryb) {
                case 1:
                    modyfikacja(nazwaAutomatu);
                    break;
                case 2:
                    kupowanie(nazwaAutomatu, portfel);
                    break;
                case 3:
                    portfel(portfel);
                    break;
            }
            do {
                System.out.println("Czy chcesz wrocic do menu glownego?\n1. Tak\n2. Nie");
                powrot = in.nextInt();
            } while (powrot != 1 && powrot != 2);
        } while (powrot != 2);
    }

}
