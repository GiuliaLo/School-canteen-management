package database;
import java.util.ArrayList;

public class Docente extends Persona {
	
	public Docente() {
		//super()
	}
	
	//costruttore per dati da utente (senza key)
	public Docente(String nome, String cognome, String cellulare,
			Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) {
		super(nome, cognome, cellulare, indirizzo, note, allergie);
	}
	
	//costruttore per dati da file (con key)
	public Docente(int key, String nome, String cognome, String cellulare,
			Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) {
		super(key, nome, cognome, cellulare, indirizzo, note, allergie);
	}

	public static Docente fromString(String infoDoc) {		
		infoDoc = infoDoc.trim();
		String[] str = infoDoc.split("\\t");
   	
   	int key = Integer.parseInt(str[0].trim());
   	String n = str[1].trim();
   	String c = str[2].trim();
   	String cell = str[3].trim();
   	Indirizzo ind = Indirizzo.fromString(str[4].trim());
   	ArrayList<String> note = null;
   	if (str.length > 5 && !str[5].equals("[]")) {
   		note = new ArrayList<>();
   		String[] no = str[5].substring(1, str[5].length()-1).split(",");
   		for (int i=0; i<no.length; i++)
   			note.add(no[i].trim());
   	}
   	ArrayList<Allergene> a = null;
   	if (str.length > 6 && !str[6].equals("[]")) {
   		a = new ArrayList<>();
   		String[] all = str[6].substring(1, str[6].length()-1).split(",");
   		for (int i=0; i<all.length; i++)
   			try {
   				a.add(Allergene.valueOf(all[i].trim()));
   			} catch (Exception e) {
   				System.err.println(e.getMessage());
   				e.printStackTrace();
   			}
   	}
   	
   	Docente docente = new Docente(key, n, c, cell, ind, note, a);
   	return docente;
	}
}
