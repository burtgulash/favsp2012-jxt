public class Predmet {
	String pracoviste, nazev, semestr, typ;
	int id;
	int pocet;

	public Predmet(int id, String pracoviste, String nazev, 
                     String semestr, String typ)
	{
		this.id = id;
		this.nazev = nazev;
		this.typ = typ;
		this.pracoviste = pracoviste;
		this.semestr = semestr;
	}

	public boolean equals(Object o) {
		return id == ((Predmet) o).id;
	}

	public int hashCode() {
		return id;
	}
}
