import java.util.LinkedList;
import java.util.StringTokenizer;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileReader;
import java.io.IOException;


public class TxtReader {
	LinkedList<Predmet> getData(File f) {
		LinkedList<Predmet> result;
		BufferedReader r;
		StringTokenizer tok;
		String line;

		result = new LinkedList<Predmet>();
		line = null;
		try {
			r = new BufferedReader(new FileReader(f));

			while  ((line = r.readLine()) != null) {
				tok = new StringTokenizer(line, ";");
				Predmet c = new Predmet();

				tok.nextToken();
				c.pracoviste = tok.nextToken();
				c.nazev = tok.nextToken();
				
				result.add(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
