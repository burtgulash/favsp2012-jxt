import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Iterator;

import java.text.Collator;

import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;


public class Hlavni {
    public static void main(String[] argv) {
        String inFile, outFile;
        int pocetZruseni = 0;
        int pocetSkutecneZapsanych = 0;

        Map<String, Student> studenti = new HashMap<String, Student>();
        Map<String, Predmet2> predmety = new HashMap<String, Predmet2>();
        Map<String, Integer> pracoviste = new HashMap<String, Integer>();

        inFile = outFile = null;
        for (String arg : argv) {
            if (arg.charAt(0) == '-') {
                if (arg.charAt(1) == 'i')
                    inFile = arg.substring(2);
                if (arg.charAt(1) == 'o')
                    outFile = arg.substring(2);
            }
        }

        List<Akce> akce = new TxtReader().getData(inFile);


        for (Akce a : akce) {
            if (!studenti.containsKey(a.osobniCislo)) {
                Student student = new Student(a.osobniCislo);
                studenti.put(a.osobniCislo, student);
            }

            if (a.akce)
                studenti.get(a.osobniCislo).zapsatAkci(a);
            else {
                studenti.get(a.osobniCislo).odepsatAkci(a);
                pocetZruseni ++;
            }
        }

        Iterator<Student> it = studenti.values().iterator();
        while (it.hasNext()) {
            Student s = it.next();

            if (s.nestuduje()) {
                it.remove();
                continue;
            }

            for (Akce a : s.akce) {
                String identifikator = a.pracoviste + a.nazev + a.semestr;

                if (!predmety.containsKey(identifikator))
                    predmety.put(identifikator,
                         new Predmet2(a.pracoviste, a.nazev, a.semestr));

                if (a.typ.equals("Př"))
                    predmety.get(identifikator).naPrednasce++;
                else if (a.typ.equals("Cv"))
                    predmety.get(identifikator).naCviceni++;
                else if (a.typ.equals("Se"))
                    predmety.get(identifikator).naSeminari++;
                else
                    assert false;

                pocetSkutecneZapsanych ++;
            }
        }

        for (Predmet2 p : predmety.values()) {
            if (!pracoviste.containsKey(p.pracoviste))
                pracoviste.put(p.pracoviste, 0);

            if (p.maPrednasku())
                pracoviste.put(p.pracoviste,
                     pracoviste.get(p.pracoviste) + p.naPrednasce);
            else if (p.maCviceni())
                pracoviste.put(p.pracoviste,
                     pracoviste.get(p.pracoviste) + p.naCviceni);
            else if(p.maSeminar())
                pracoviste.put(p.pracoviste,
                     pracoviste.get(p.pracoviste) + p.naSeminari);
        }


        List<Map.Entry<String, Integer>> tmpList =
                     new ArrayList<Map.Entry<String, Integer>>
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

        PrintWriter out = null;
        try {
            out = new PrintWriter(
                    new OutputStreamWriter(
                    new BufferedOutputStream(
                    new FileOutputStream(outFile)), "UTF-8"));

            out.printf("Počet všech předzápisových akcí: %d%n", akce.size());
            out.printf("Počet zrušených akcí (delete): %d%n", pocetZruseni);
            out.printf("Počet skutečně zapsaných akcí: %d%n",
                                             pocetSkutecneZapsanych);
            out.printf("Počet studentů: %d%n", studenti.size());
            out.printf("Počet předmětů: %d%n", predmety.size());
            out.printf("Počet pracovišť: %d%n", pracoviste.size());

            int i = 1;
            for (Map.Entry<String, Integer> en : tmpList)
                out.printf("%d. %s: %d%n", i++, en.getKey(), en.getValue());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            System.exit(1);
        } finally {
            out.close();
        }
    }
}
