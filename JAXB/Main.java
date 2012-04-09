import java.io.*;
import java.util.*;
import java.math.BigInteger;
import javax.xml.bind.*;
import jxtsp.*;

/**
 * Hlavní třída, logika programu.
 *
 * @author Tomáš Maršálek, A10B0632P
 */
public class Main {
	/**
	 * Vstupní metoda programu.
	 *
	 * @param args Argumenty příkazové řádky.
	 * @throws Exception Výjimky nejsou kontrolovány.
	 */
	public static void main(String[] args) throws Exception {
		String x, y, i, d;

		x = y = i = d = null;

		for (String arg : args)
			if (arg.charAt(0) == '-') {
				if (arg.charAt(1) == 'x')
					x = arg.substring(2);
				if (arg.charAt(1) == 'y')
					y = arg.substring(2);
				if (arg.charAt(1) == 'i')
					i = arg.substring(2);
				if (arg.charAt(1) == 'd')
					d = arg.substring(2);
			}


		JAXBContext c = JAXBContext.newInstance("jxtsp");
		Unmarshaller u = c.createUnmarshaller();
		JAXBElement<?> rozvrhoveAkce = (JAXBElement<?>) u.unmarshal(
						new File("../zadani/data/rozvrhove-akce.xml"));
		JAXBElement<?> predzapisoveAkce = (JAXBElement<?>) u.unmarshal(
						new File("../zadani/data/predzapisove-akce.xml"));

		String zvoleneOsobniCislo = "A10B0632P";

		Map<BigInteger, Object> zapsane = new HashMap<BigInteger, Object>();
		Map<BigInteger, Predmet> index = new HashMap<BigInteger, Predmet>();


		// Získat všechny předzápisové akce zvoleného studenta.
		// Uložit je do množiny id předmětů.
		for (EventType event : 
                      ((PreregistrationEventsType) predzapisoveAkce
                                                   .getValue()).getEvent())
		{
			String osobniCislo = event.getParameters()
                                 .getActor().getPersonalNumber();
			if (osobniCislo.equals(zvoleneOsobniCislo)) {
				ProcessedDataType data = event.getProcessedData();
				BigInteger id = data.getTimetableAction().getId();

				if (data.getActivity() == ActivityType.INSERT) 
					zapsane.put(id, null);
				else
					zapsane.remove(id);
			}
		}


		// Vybudovat index pro snadné dohledání údajů o předmětu.
		for (TimetableActivityType activity : 
                      ((TimetableActivitiesOfFAVStudentsType) rozvrhoveAkce
                                          .getValue()).getTimetableActivity())
		{
			Predmet predmet   = new Predmet();

			predmet.nazev     = activity.getSubject().getValue();
			predmet.typ       = activity.getSubject().getKind();

			// Jednorázová akce, nemá čas.
			if (activity.getTime() == null)
				continue;
			predmet.den       = activity.getTime().getDay();
			predmet.semestr   = activity.getTime().getTerm();
			predmet.zacatek   = activity.getTime().getStartTime();
			predmet.konec     = activity.getTime().getEndTime();

			// Jednorázová akce, nemá místo.
			if (activity.getPlace() == null)
				continue;
			predmet.budova    = activity.getPlace().getBuilding();
			predmet.mistnost  = activity.getPlace().getRoomNumber();

			index.put(activity.getId(), predmet);
		}


		// Join on ID.
		List<BigInteger> zapsanaId = 
                                 new LinkedList<BigInteger>(zapsane.keySet());
		List<Predmet> predmety = new ArrayList<Predmet>(zapsanaId.size());
		for (BigInteger id : zapsanaId)
			predmety.add(index.get(id));

		// Seřadit podle přirozeného řazení předmětů.
		Collections.sort(predmety);


		// Vytvořit adresář pro výstupní soubory.
		new File(d).mkdirs();


		// Zapsat předměty do kontrolního textového souboru.
		PrintWriter out = new PrintWriter(
							new OutputStreamWriter(
							new BufferedOutputStream(
							new FileOutputStream(
								d + "rozvrh_" + i + ".txt")), "UTF-8"));

		for (Predmet p : predmety)
			out.printf("%s;%s;%s;%s;%s;%s;%s%n", p.nazev, p.typ, p.den, 
									p.zacatek, p.konec, p.budova, p.mistnost);
		out.close();
	}
}

