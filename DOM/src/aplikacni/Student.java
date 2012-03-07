package jxt.aplikacni;


import java.util.List;
import java.util.LinkedList;


public class Student {
    public String osobniCislo;
    public List<Akce> akce;

    public Student(String osobniCislo) {
        this.osobniCislo = osobniCislo;

        akce = new LinkedList<Akce>();
    }

    public boolean nestuduje() {
        return akce.isEmpty();
    }

    public void zapsatAkci(Akce a) {
        akce.add(a);
    }

    public void odepsatAkci(Akce a) {
        for (int i = akce.size() - 1; i >= 0; i--)
            if (akce.get(i).id == a.id) {
                akce.remove(i);
                return;
            }
        assert false;
    }

    public boolean equals(Object o) {
        return osobniCislo.equals(((Student) o).osobniCislo);
    }

    public int hashCode() {
        return osobniCislo.hashCode();
    }
}
