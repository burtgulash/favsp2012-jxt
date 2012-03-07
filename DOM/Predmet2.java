public class Predmet2 {
	String pracoviste, nazev, semestr;
	int naPrednasce, naCviceni, naSeminari;

	public Predmet2(String pracoviste, String nazev, String semestr) {
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
		Predmet2 p = (Predmet2) o;
		return pracoviste.equals(p.pracoviste)
            && nazev.equals(p.nazev)
            && semestr.equals(p.semestr);
	}

	@Override
	public int hashCode() {
		return (pracoviste + nazev + semestr).hashCode();
	}
}
