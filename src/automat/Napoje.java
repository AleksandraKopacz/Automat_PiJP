package automat;

public class Napoje {

    public int indeks;
    public String nazwa;
    public double cena;
    public int ilosc;

    public void wyswietl() {
        System.out.println(indeks + ". " + nazwa + " " + cena + " zl " + ilosc + " szt.");
    }

}
