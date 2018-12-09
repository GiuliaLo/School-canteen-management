package database;
import java.util.ArrayList;

public class Studente extends Persona {
	
	private String telefono;
	
	public Studente() {
		
	}
	
	//costruttore per dati da utente
	public Studente(String nome, String cognome, String telefono, String cellulare,
			 Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) {
		super(nome, cognome, cellulare, indirizzo, note, allergie);
		this.telefono = telefono;
	}
	
	//costruttore per dati da file
	public Studente(int key, String nome, String cognome, String telefono, String cellulare,
			 Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) {
		super(key, nome, cognome, cellulare, indirizzo, note, allergie);
		this.telefono = telefono;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	@Override
	public String toString() {
		String noteStr;
		String allergieStr;
		
		if (getNote() == null)
			noteStr = "[]";
		else
			noteStr = getNote().toString();
		
		if (getAllergie() == null)
			allergieStr = "[]";
		else
			allergieStr = getAllergie().toString();
		
		return String.format("%d\t%s\t%s\t%s\t%s\t%s\t%s\t%s", 
         getKey(), getNome(), getCognome(), getTelefono(), getCellulare(), getIndirizzo(), noteStr, allergieStr);
	}
	
	
	public static Studente fromString(String infoStud) {
		infoStud = infoStud.trim();
		String[] str = infoStud.split("\\t");
   	
   	int key = Integer.parseInt(str[0].trim());
   	String n = str[1].trim();
   	String c = str[2].trim();
   	String tel = str[3].trim();
   	String cell = str[4].trim();
   	Indirizzo ind = Indirizzo.fromString(str[5].trim());
   	ArrayList<String> note = null;
   	if (str.length > 6 && !str[6].equals("[]")) {
   		note = new ArrayList<>();
   		String[] no = str[6].substring(1, str[6].length()-1).split(",");
   		for (int i=0; i<no.length; i++)
   				note.add(no[i].trim());
   	}
   	ArrayList<Allergene> a = null;
   	if (str.length > 7 && !str[7].equals("[]")) {
   		a = new ArrayList<>();
   		String[] all = str[7].substring(1, str[7].length()-1).split(",");
   		for (int i=0; i<all.length; i++)
   			try {
   				a.add(Allergene.valueOf(all[i].trim()));
   			} catch(Exception e) {
   				System.err.println(e.getMessage());
   				e.printStackTrace();
   			}
   	}
   	
   	Studente studente = new Studente(key, n, c, tel, cell, ind, note, a);
   	return studente;
	}
	
}
