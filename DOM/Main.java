import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import java.text.Collator;

public class Main {
	public static void main(String[] argv) {
		List<Akce> akce;
		ArrayList<Map.Entry<String, Integer>> tmpList;
		String inFile, outFile, stringIdentifier;
		Student student;
		Integer id;
		int pocet, i, pocetZruseni, pocetSkutecneZapsanych;

		Map<String, Student> studenti;
		Map<String, Integer> pracoviste;
		Map<String, Object> predmety;
		Map<String, Integer> idPredmetu;
		Map<Integer, Predmet> prednasky;
		Map<Integer, Predmet> cviceni;
		Map<Integer, Predmet> seminare;


		inFile = outFile = null;
		pocet = pocetZruseni = pocetSkutecneZapsanych = 0;

		studenti = new HashMap<String, Student>();
		pracoviste = new HashMap<String, Integer>();
		predmety = new HashMap<String, Object>();
		idPredmetu = new HashMap<String, Integer>();
		prednasky = new HashMap<Integer, Predmet>();
		cviceni = new HashMap<Integer, Predmet>();
		seminare = new HashMap<Integer, Predmet>();

		inFile = argv[0];

		akce = new TxtReader().getData(inFile);

		for (Akce a : akce) {
			if (!studenti.containsKey(a.osobniCislo)) {
				student = new Student(a.osobniCislo);
				studenti.put(a.osobniCislo, student);
			}

			if (a.akce)
				studenti.get(a.osobniCislo).zapsatAkci(a);
			else {
				studenti.get(a.osobniCislo).odepsatAkci(a);
				pocetZruseni ++;
			}
		}

		for (Student s : studenti.values()) {
			if (s.nestuduje()) {
				studenti.remove(s.osobniCislo);
				continue;
			}
				
			for (Akce a : s.akce) {
				stringIdentifier = a.pracoviste + a.nazev + a.semestr;

				if (a.typ.equals("Př")) {
					if (!prednasky.containsKey(a.id)) {
						prednasky.put(a.id, new Predmet(a.id, a.pracoviste, 
                                                   a.nazev, a.semestr, a.typ));
						idPredmetu.put(stringIdentifier + a.typ, a.id);
						predmety.put(stringIdentifier, null);
					}
					prednasky.get(a.id).pocet ++;
				} else if (a.typ.equals("Cv")) {
					if (!cviceni.containsKey(a.id)) {
						cviceni.put(a.id, new Predmet(a.id, a.pracoviste, 
                                                   a.nazev, a.semestr, a.typ));
						idPredmetu.put(stringIdentifier + a.typ, a.id);
						predmety.put(stringIdentifier, null);
					}
					cviceni.get(a.id).pocet ++;
				} else if (a.typ.equals("Se")) {
					if (!seminare.containsKey(a.id)) {
						seminare.put(a.id, new Predmet(a.id, a.pracoviste, 
                                                   a.nazev, a.semestr, a.typ));
						idPredmetu.put(stringIdentifier + a.typ, a.id);
						predmety.put(stringIdentifier, null);
					}
					seminare.get(a.id).pocet ++;
				} else 
					assert false;

				pocetSkutecneZapsanych ++;
			}
		}

		for (Predmet p : prednasky.values()) {
			if (!pracoviste.containsKey(p.pracoviste))
				pracoviste.put(p.pracoviste, 0);
			pracoviste.put(p.pracoviste, 
                           pracoviste.get(p.pracoviste) + 1);

			id = idPredmetu.get(p.pracoviste
                              + p.nazev
                              + p.semestr 
                              + "Cv");
			if (id != null)
				cviceni.remove(id);

			id = idPredmetu.get(p.pracoviste
                              + p.nazev
                              + p.semestr 
                              + "Se");
			if (id != null)
				seminare.remove(id);
		}

		for (Predmet p : cviceni.values()) {
			if (!pracoviste.containsKey(p.pracoviste))
				pracoviste.put(p.pracoviste, 0);
			pracoviste.put(p.pracoviste, 
                           pracoviste.get(p.pracoviste) + 1);

			id = idPredmetu.get(p.pracoviste
                                      + p.nazev
                                      + p.semestr 
                                      + "Se");
			if (id != null)
				seminare.remove(id);
		}

		for (Predmet p : seminare.values()) {
			if (!pracoviste.containsKey(p.pracoviste))
				pracoviste.put(p.pracoviste, 0);
			pracoviste.put(p.pracoviste, 
                           pracoviste.get(p.pracoviste) + 1);
		}

		for (String p : pracoviste.keySet())
			if (pracoviste.get(p) == 0)
				pracoviste.remove(p);

		tmpList = new ArrayList<Map.Entry<String, Integer>>
                                              (pracoviste.entrySet());
		Collections.sort(tmpList, new Comparator<Map.Entry<String, Integer>>() {
						public int compare(Map.Entry a, Map.Entry b) {
							int c = ((Integer) a.getValue()).compareTo(
									((Integer) b.getValue()));

							if (c != 0)
								return c;
							return Collator.getInstance(new Locale("cs", "CZ"))
								   .compare(a.getKey(), b.getKey());
						}
					});

		System.out.printf("Počet všech předzápisových akcí: %d%n", akce.size());
		System.out.printf("Počet zrušených akcí (delete): %d%n", pocetZruseni);
		System.out.printf("Počet skutečně zapsaných akcí: %d%n", 
                                                       pocetSkutecneZapsanych);
		System.out.printf("Počet studentů: %d%n", studenti.size());
/*
		System.out.printf("Počet předmětů: %d%n", prednasky.size()
                                                + cviceni.size()
                                                + seminare.size());
*/
		System.out.printf("Počet předmětů: %d%n", predmety.size());
		System.out.printf("Počet pracovišť: %d%n", pracoviste.size());

		i = 1;
		for (Map.Entry<String, Integer> entry : tmpList) {
			System.out.printf("%d. %s: %d%n", i, 
                                      entry.getKey(), entry.getValue());
			i++;
		}
	}
}
