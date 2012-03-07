package jxt;

/**
 * Hlavní třída.
 *
 * Logika programu je natolik jednoduchá, že ji není třeba přesouvat 
 * do samostatné třídy.
 *
 * @author Maršálek Tomáš, A10B0632P
 */

import jxt.aplikacni.*;
import jxt.datova.*;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.Locale;

import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import java.text.Collator;


public class Hlavni {
    /**
     * Vstupní bod programu.
     *
      * @param argv Vektor argumentů.
     */
    public static void main (String[] argv) {
        PrintWriter out;
        String inFile, outFile;
        HashMap<String, Integer> pracoviste;
        HashMap<Predmet, Object> predmety;
        List<Predmet> list;
        ArrayList<Map.Entry<String, Integer>> tmpList;
        int i;

        inFile = outFile = null;
        out = null;

        pracoviste = new HashMap<String, Integer>();
        predmety = new HashMap<Predmet, Object>();

        for (String arg : argv) {
            if (arg.charAt(0) == '-') {
                if (arg.charAt(1) == 'i')
                    inFile = arg.substring(2);
                if (arg.charAt(1) == 'o')
                    outFile = arg.substring(2);
            }
        }


        list = new TxtReader().getData(new File(inFile));
        for (Predmet p : list) {
            if (!pracoviste.containsKey(p.pracoviste))
                pracoviste.put(p.pracoviste, 0);
            if (!predmety.containsKey(p))
                pracoviste.put(p.pracoviste, pracoviste.get(p.pracoviste) + 1);
            predmety.put(p, null);
        }

        tmpList = 
            new ArrayList<Map.Entry<String, Integer>>(pracoviste.entrySet());
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

        try {
            out = new PrintWriter(
                    new OutputStreamWriter(
                    new BufferedOutputStream(
                    new FileOutputStream(outFile)), "UTF-8"));

            out.printf("Počet všech rozvrhových akcí: %d%n", list.size());
            out.printf("Počet předmětů: %d%n", predmety.size());
            out.printf("Počet pracovišť: %d%n", pracoviste.size());

            i = 1;
            for (Map.Entry<String, Integer> entry : tmpList) {
                out.printf("%d. %s: %d%n", 
                    i, entry.getKey(), entry.getValue());
                i ++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
        }finally {
            out.close();
        }
    }
}
