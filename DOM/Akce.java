public class Akce {
	String osobniCislo, pracoviste, nazev, typ, semestr;
	int id;
	boolean akce;

	public Akce(int id, boolean akce, 
                String osobniCislo, String pracoviste, 
                String nazev, String typ, String semestr)
	{
		this.id = id;
		this.akce = akce;
		this.osobniCislo = osobniCislo;
		this.pracoviste = pracoviste;
		this.nazev = nazev;
		this.typ = typ;
		this.semestr = semestr;
	}
}
