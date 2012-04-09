import javax.xml.datatype.XMLGregorianCalendar;
import java.text.Collator;
import java.util.Locale;
import jxtsp.*;

/**
 * Datový kontejner pro předmět, který není jednorázová akce. Tedy má čas i
 * místnost.
 *
 * @author Tomáš Maršálek, A10B0632P
 */
public class Predmet implements Comparable<Predmet> {
	public String nazev, budova, mistnost;
	public KindType typ;
	public TermType semestr;
	public DayType den;
	public XMLGregorianCalendar zacatek, konec;


	@Override
	/**
	 * Porovnávací metoda využívaná pro řazení dle zadání: Nejprve podle
	 * semestru, pak podle dnů v týdnu, pak podle času začátku akce, pak podle
	 * typu akce a nakonec podle jména předmětu.
	 *
	 * @param p druhý předmět pro porovnání.
	 */
	public int compareTo(Predmet p) {
		int cmp;

		cmp = semestr.compareTo(p.semestr);
		if (cmp != 0)
			return cmp;

		cmp = den.compareTo(p.den);
		if (cmp != 0)
			return cmp;

		cmp = zacatek.compare(p.zacatek);
		if (cmp != 0)
			return cmp;

		cmp = typ.compareTo(p.typ);
		if (cmp != 0)
			return cmp;

		return Collator.getInstance(new Locale("cs", "CZ"))
                           .compare(nazev, p.nazev);
	}
}
