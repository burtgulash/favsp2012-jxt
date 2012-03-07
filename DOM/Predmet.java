import java.util.Map;
import java.util.HashMap;


public class Predmet {
	String pracoviste, nazev, semestr, typ;
	int id;
	Map<String, Object> studenti;

	public Predmet(int id, String pracoviste, String nazev, 
                     String semestr, String typ)
	{
		this.id = id;
		this.nazev = nazev;
		this.typ = typ;
		this.pracoviste = pracoviste;
		this.semestr = semestr;

		studenti = new HashMap<String, Object>();
	}

	public void vlozitStudenta(String osobniCislo) {
		studenti.put(osobniCislo, null);
	}

	public int pocet() {
		return studenti.size();
	}

	public boolean equals(Object o) {
		return id == ((Predmet) o).id;
	}

	public int hashCode() {
		return id;
	}
}
