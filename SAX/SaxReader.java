import java.util.LinkedList;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxReader extends DefaultHandler {
	boolean inTimetableActivity, inDepartment, inSubject;
	Predmet p;
	LinkedList<Predmet> result;
	

	public LinkedList<Predmet> getData(File f) {
		result = new LinkedList<Predmet>();

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();

			parser.parse(f, this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
		if (qName.equals("timetableActivity")) {
			p = new Predmet();
			inTimetableActivity = true;
		} else if (qName.equals("department"))
			inDepartment = true;
		else if (qName.equals("subject"))
			inSubject = true;
	}

	@Override
	public void endElement(String uri, String localName, String qName) 
                                                    throws SAXException {
		if (qName.equals("timetableActivity")) {
			inTimetableActivity = false;
			result.add(p);
		} else if (qName.equals("department"))
			inDepartment = false;
		else if (qName.equals("subject"))
			inSubject = false;
	}

	@Override
	public void characters(char[] ch, int start, int length)
                                                throws SAXException {
		if (inDepartment)
			p.pracoviste = new String(ch, start, length);
		else if (inSubject)
			p.nazev = new String(ch, start, length);
	}
}
