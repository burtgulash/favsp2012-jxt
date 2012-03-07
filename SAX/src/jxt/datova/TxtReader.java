package jxt.datova;

/**
 * TxtReader 
 * slouží k přečtení souboru rozvrhove-akce.txt a předání výsledku
 * Hlavní třídě.
 *
 * @author Maršálek Tomáš, A10B0632P
 */

import jxt.aplikacni.Predmet;

import java.util.List;
import java.util.LinkedList;
import java.util.StringTokenizer;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;


public class TxtReader {
    /**
     * Vrací všechna potřebná data získaná z txt souboru pro další zpracování.
     *
     * @param f Txt soubor rozvrhove-akce.txt.
     * @return Data uložená v seznamu.
     */
    public List<Predmet> getData(File f) {
        LinkedList<Predmet> vysledek;
        BufferedReader r;
        StringTokenizer tok;
        String line;

        vysledek = new LinkedList<Predmet>();
        line = null;
        try {
            r = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(f), "UTF-8"));

            while  ((line = r.readLine()) != null) {
                tok = new StringTokenizer(line, ";");
                Predmet c = new Predmet();

                tok.nextToken();
                c.pracoviste = tok.nextToken();
                c.nazev = tok.nextToken();
                tok.nextToken();
                c.semestr = tok.nextToken();
              
                vysledek.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return vysledek;
    }
}
