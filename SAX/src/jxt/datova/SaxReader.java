package jxt.datova;

/**
 * SaxReader 
 * slouží k přečtení souboru rozvrhove-akce.xml a předání výsledku
 * Hlavní třídě.
 *
 * @author Maršálek Tomáš, A10B0632P
 */

import jxt.aplikacni.Predmet;

import java.util.List;
import java.util.LinkedList;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxReader extends DefaultHandler {
    boolean inDepartment, inSubject;
    Predmet p;
    List<Predmet> vysledek;
  

    /**
     * Vrací všechna potřebná data získaná z xml souboru pro další zpracování.
     *
     * @param f Xml soubor rozvrhove-akce.xml.
     * @return Data uložená v seznamu.
     */
    public List<Predmet> getData(File f) {
        vysledek = new LinkedList<Predmet>();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            parser.parse(f, this);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return vysledek;
    }

    @Override
    /**
     * Handler pro parser, když narazí na otevírací element.
     *
     * @param uri uri
     * @param localName Nepracuje s jmennými prostory, proto je vždy prázdný.
     * @param qName Úplné jméno elementu.
     * @param attributes Atributy elementu.
     * @throws SAXException
     */
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (qName.equals("timetableActivity"))
            p = new Predmet();
        else if (qName.equals("department"))
            inDepartment = true;
        else if (qName.equals("subject"))
            inSubject = true;
        else if (qName.equals("time"))
            p.semestr = attributes.getValue("term");
    }

    @Override

    /**
     * Handler pro parser, když narazí na uzavírací element.
     *
     * @param uri uri
     * @param localName Nepracuje s jmennými prostory, proto je vždy prázdný.
     * @param qName Úplné jméno elementu.
     * @throws SAXException
     */
    public void endElement(String uri, String localName, String qName) 
                                                    throws SAXException {
        if (qName.equals("timetableActivity"))
            vysledek.add(p);
        else if (qName.equals("department"))
            inDepartment = false;
        else if (qName.equals("subject"))
            inSubject = false;
    }

    @Override
    /**
     * Handler pro parser, když narazí na text uvnitř elementů.
     * 
     * @param ch Text, na který parser narazil.
     * @param start Startovní pozice v ch.
     * @param length Počet znaků pro použití z ch.
     * @throws SAXException
     */
    public void characters(char[] ch, int start, int length)
                                                throws SAXException {
        if (inDepartment)
            p.pracoviste = new String(ch, start, length);
        else if (inSubject)
            p.nazev = new String(ch, start, length);
    }
}
