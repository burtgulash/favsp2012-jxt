package jxt.aplikacni;


public class Predmet {
    public String pracoviste, nazev, semestr;
    public int naPrednasce, naCviceni, naSeminari;

    public Predmet(String pracoviste, String nazev, String semestr) {
        this.pracoviste = pracoviste;
        this.nazev = nazev;
        this.semestr = semestr;
    }

    public boolean maPrednasku() {
        return naPrednasce > 0;
    }

    public boolean maCviceni() {
        return naCviceni > 0;
    }

    public boolean maSeminar() {
        return naSeminari > 0;
    }

    @Override
    public boolean equals(Object o) {
        Predmet p = (Predmet) o;
        return pracoviste.equals(p.pracoviste)
            && nazev.equals(p.nazev)
            && semestr.equals(p.semestr);
    }

    @Override
    public int hashCode() {
        return (pracoviste + nazev + semestr).hashCode();
    }
}
