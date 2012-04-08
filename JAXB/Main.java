import java.io.*;
import java.util.*;
import java.math.BigInteger;
import javax.xml.bind.*;
import jxtsp.*;

public class Main {
	public static void main(String[] args) throws Exception {
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
			predmet.den       = activity.getTime().getDay();
			predmet.zacatek   = activity.getTime().getStartTime();
			predmet.konec     = activity.getTime().getEndTime();
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


		// TODO yo, sort tha shit.

		// TODO yo, print tha shit.

		// TODO yo, xml tha shit.

		System.out.println(rozvrhoveAkce.getValue());
	}
}

