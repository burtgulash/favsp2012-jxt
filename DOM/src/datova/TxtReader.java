package jxt.datova;

import jxt.aplikacni.*;


import java.util.List;
import java.util.LinkedList;
import java.util.StringTokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class TxtReader {
    public List<Akce> getData(String filename) {
        BufferedReader reader;
        List<Akce> vysledek;
        String line, akce, pracoviste, nazev, typ, osobniCislo, semestr;
        StringTokenizer tok;
        Akce a;
        int id;

        line = null;
        vysledek = new LinkedList<Akce>();

        try {
            reader = new BufferedReader(new FileReader(filename));

            while ((line = reader.readLine()) != null) {
                tok = new StringTokenizer(line, ";");

                osobniCislo = tok.nextToken();
                akce = tok.nextToken();
                id = Integer.parseInt(tok.nextToken());
                pracoviste = tok.nextToken();
                nazev = tok.nextToken();
                typ = tok.nextToken();
                semestr = tok.nextToken();

                if (akce.equals("insert"))
                    a = new Akce(id, true, osobniCislo,
                                           pracoviste, nazev, typ, semestr);
                else
                    a = new Akce(id, false, osobniCislo,
                                            pracoviste, nazev, typ, semestr);

                vysledek.add(a);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return vysledek;
    }
}
