package jxt.aplikacni;

/**
 * Třída Předmět.
 *
 * Paměťová struktura pro uchování všech potřebných informací
 * o jednom předmětu.
 *
 * @author Maršálek Tomáš, A10B0632P
 */

public class Predmet {
    public String pracoviste, nazev, semestr;

    @Override
    /**
     * Rovnost dvou Předmětů.
     *
     * @param o Předmět k porovnání.
     * @return true pokud se rovnají, false když ne.
     */
    public boolean equals(Object o) {
        return pracoviste.equals(((Predmet) o).pracoviste) 
                 && nazev.equals(((Predmet) o).nazev)
                 && semestr.equals(((Predmet) o).semestr);
    }


    @Override
    /**
     * Hash kód Předmětu.
     *
     * @return Hash kód, který je určen pracovištěm, názvem a semestrem.
     */
    public int hashCode() {
        return (pracoviste + nazev + semestr).hashCode();
    }
}
