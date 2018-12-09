package communication;

import java.util.ArrayList;

import database.Allergene;
import database.DataAccess;
import database.Database;
import database.Docente;
import database.Indirizzo;
import database.Persona;
import database.Piatto;
import database.Studente;
import file.Filenames;

public class Server extends Protocol implements Runnable {
	private Network net;
	private static DataAccess db;
	
	public static void main(String[] args) {
		
		String actualPath = System.getProperty("user.dir");
		if (actualPath.endsWith("bin"))
			Filenames.setRoot("../");
		// System.out.println(System.getProperty("user.dir"));
		
		try {
			// db = new FileManager();
			db = new Database();
			while(true) {
				System.out.println("In attesa di client...");
				Server s = new Server();
				
				System.out.println("Nuovo client ricevuto!");
				Thread t = new Thread(s);
				t.start();
			}
		} catch (Exception e) {
			System.out.println("Impossibile avviare il server");
			e.printStackTrace();
		}
	}
	
	public Server() throws Exception {
      net = new Network(this, Protocol.port);
	}
	
	public void run() {
		try {
			while(true) {
				net.serve();
			}
		} catch(Exception e) {
			System.out.println("Connessione terminata");
			net.close();
		}
	}
	
	/*
	 * Il server riceve la richiesta come 
	 * azione + $ + info
	 * eseguo lo split su "$"
	 * azione è composta da 2 parti: una indica il tipo di azione da eseguire e l'altra, separata da "_", su quale tipo di oggetto eseguirla
	 * la divido nelle due parti e vado alla funzione indicata dal tipo di azione, passando come parametro la seconda parte di azione e le info
	 * con uno switch case scelgo la funzione appropriata, se richiesto dalla funzione ricostruirò l'oggetto dalla stringa info per passarlo al Database
	 */
	
	public Object manage(String request) throws Exception {
		
		String[] dati = request.split("\\"+actionSeparator+"");
		if (dati.length < 2) {
			String[] d = {dati[0], ""};
			dati = d;
		}
		
		String[] azioni = dati[0].split("_");
		
		Object retValue = 0;
		switch (azioni[0]) {
		case "NUOVO":
			retValue = "" + nuovoRecord(azioni[1], dati[1]);
			break;
		case "CANC":
			retValue = "" + cancRecord(azioni[1], dati[1]);
			break;
		case "MOD":
			retValue = "" + modRecord(azioni[1], dati[1]);
			break;
		case "ELENCO":
			retValue = mostraElenco(azioni[1], dati[1]);
			break;
		case "GET":
			retValue = getRecord(azioni[1], dati[1]);
			break;
		case "MENU":
			retValue = "" + opMenu(azioni[1], dati[1]);
			break;
		}
		
		return retValue;
	}
	
	public int nuovoRecord(String tipoRecord, String valRecord) throws Exception {
		int value = 0;
		if (tipoRecord.equalsIgnoreCase("STUD")) {
			String[] strStud = valRecord.split(fieldSeparator+"");
	   	
	   	String n = strStud[0].trim();
	   	String c = strStud[1].trim();
	   	String tel = strStud[2].trim();
	   	String cell = strStud[3].trim();
	   	Indirizzo ind = Indirizzo.fromString(strStud[4].trim());
	   	ArrayList<String> note = new ArrayList<>();
	   	if (strStud.length > 5 && !strStud[5].trim().isEmpty()) {
	   		String[] no = strStud[5].split(",");
	   		for (int i=0; i<no.length; i++)
	   			note.add(no[i].trim());
	   	}
	   	ArrayList<Allergene> a = new ArrayList<>();
	   	if (strStud.length > 6 && !strStud[6].trim().isEmpty()) {
	   		String[] all = strStud[6].split(",");
	   		for (int i=0; i<all.length; i++)
	   			a.add(Allergene.valueOf(all[i].trim()));
	   	}
			value = db.aggiungiStudente(n, c, tel, cell, ind, note, a);
		}
				
		else if (tipoRecord.equalsIgnoreCase("DOC")) {
			String[] str = valRecord.split(fieldSeparator+"");
	   	
	   	String n = str[0].trim();
	   	String c = str[1].trim();
	   	String cell = str[2].trim();
	   	Indirizzo ind = Indirizzo.fromString(str[3].trim());
	   	ArrayList<String> note = new ArrayList<>();
	   	if (str.length > 4 && !str[4].trim().isEmpty()) {
	   		String[] no = str[4].split(",");
	   		for (int i=0; i<no.length; i++)
	   			note.add(no[i].trim());
	   	}
	   	ArrayList<Allergene> a = new ArrayList<>();
	   	if (str.length > 5 && !str[5].trim().isEmpty()) {
	   		String[] all = str[5].split(",");
	   		for (int i=0; i<all.length; i++)
	   			a.add(Allergene.valueOf(all[i].trim()));
	   	}
	   	value = db.aggiungiDocente(n, c, cell, ind, note, a);
		}		
				
		return value;
	}
	
	public int cancRecord(String tipoRecord, String valRecord) {
		int value = 0;
		
		if (tipoRecord.equalsIgnoreCase("STUD"))
			try {
				db.cancellaStudente(Integer.parseInt(valRecord));
			} catch (Exception e) {
				e.printStackTrace();
			}
		else if (tipoRecord.equalsIgnoreCase("DOC"))
			try {
				db.cancellaDocente(Integer.parseInt(valRecord));
			} catch (Exception e) {
				e.printStackTrace();
			}
		else value = 1;
				
		return value;
	}
	
	public int modRecord(String tipoRecord, String valRecord) {
		int value = 0;
		
		if (tipoRecord.equalsIgnoreCase("STUD")) {
			String[] str = valRecord.split(fieldSeparator+"");
	   	try {
				db.modificaStudente(Integer.parseInt(str[0]), str[1].trim(), str[2].trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (tipoRecord.equalsIgnoreCase("DOC")) {
			System.out.println("entrato nel mod doc\n");
			String[] str = valRecord.split(fieldSeparator+"");
			try {
				db.modificaDocente(Integer.parseInt(str[0]), str[1].trim(), str[2].trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	public Object mostraElenco(String tipoRecord, String valRecord) throws Exception {
		
		if (tipoRecord.equalsIgnoreCase("STUD")) {
			ArrayList<Studente> listaStud = db.elencoStudenti();
			
			ArrayList<String> list = new ArrayList<>();
			for (Studente s : listaStud)
				list.add(s.toString());
			
			return list;
		}
		else if (tipoRecord.equalsIgnoreCase("DOC")) {
			ArrayList<Docente> listaDoc = db.elencoDocenti();
			
			ArrayList<String> list = new ArrayList<>();
			for (Docente d : listaDoc)
				list.add(d.toString());
			
			return list;
		}
		else if (tipoRecord.equalsIgnoreCase("PIATTI")) {
			if (valRecord.equalsIgnoreCase("")) {
				ArrayList<Piatto> listaPiatti = db.elencoPiatti();
			
				ArrayList<String> list = new ArrayList<>();
				for (Piatto p : listaPiatti)
					list.add(p.toString());
				
				return list;
			}
			else {	//piatti di un certo tipo (valRecord)
				ArrayList<Piatto> listaTipo = db.elencoPiatti(valRecord.trim());
				
				ArrayList<String> list = new ArrayList<>();
				for (Piatto p : listaTipo)
					list.add(p.toString());
				
				return list;
			}
		}
		else
			return "";
	}
	
	public Object getRecord(String tipoRecord, String valRecord) throws NumberFormatException, Exception {
		if (tipoRecord.equalsIgnoreCase("STUD")) {
			Studente stud = db.getStudente(Integer.parseInt(valRecord));
			return stud.toString();
		}
		if (tipoRecord.equalsIgnoreCase("DOC")) {
			Docente doc = db.getDocente(Integer.parseInt(valRecord));
			return doc.toString();
		}
		if (tipoRecord.equalsIgnoreCase("PIATTO")) {
			Piatto p = db.getPiatto(Integer.parseInt(valRecord));
			return p.toString();
		}
		else if (tipoRecord.equalsIgnoreCase("ALLERGICI")) {
			ArrayList<ArrayList<Persona>> listaAll = db.getAllergici();

			ArrayList<ArrayList<String>> ret = new ArrayList<>(listaAll.size());
			for (ArrayList<Persona> listaPiatto : listaAll) {
				ArrayList<String> retPiatto = new ArrayList<>(listaPiatto.size());
				for (Persona p : listaPiatto)
					retPiatto.add(p.toString());
				ret.add(retPiatto);
			}
			
			return ret;
		} else {
			return "";
		}
	}
	
	public String opMenu(String tipoRecord, String valRecord) throws NumberFormatException, Exception {
		String value = null;
		String[] s = valRecord.split(fieldSeparator+"");
		value = db.generaMenu(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2])).toString();
		return value;
	}
	
}
