import java.io.*;
import java.util.*;
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
	}
}

