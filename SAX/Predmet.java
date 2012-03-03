public class Predmet {
	String pracoviste, nazev;

	@Override
	public boolean equals(Object o) {
		return pracoviste.equals(((Predmet) o).pracoviste) 
                 && nazev.equals(((Predmet) o).nazev);
	}

	@Override
	public int hashCode() {
		return (pracoviste + nazev).hashCode();
	}
}
