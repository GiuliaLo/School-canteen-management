package communication;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import database.*;

public class Client extends Protocol implements DataAccess {
	private Network net;

	public Client(InetAddress inetaddr) throws Exception {
		net = new Network(inetaddr, Protocol.port);
	}
	public Client(String inetaddr) throws Exception {
		net = new Network(inetaddr, Protocol.port);
	}
	
	/*
	 * La richiesta viene passata al server nel seguente modo:
	 * azione (definita in Protocol) + actionSeparator ($) + eventuali informazioni da passare alla funzione indicata da azione (non servono nel caso l'azione sia di tipo ELENCO)
	 * es. : azione = NUOVO_STUD indica che deve essere eseguita la funzione per creare un nuovo studente
	 * 		info = i dati del nuovo studente, che vengono passati in un'unica stringa secondo il formato definito dalla toString della classe studente
	 */
	
	public int aggiungiStudente(String nome, String cognome, String telefono, String cellulare,
			 Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) throws Exception {
		
		String info;
		ArrayList<String> allergie_name = new ArrayList<String>(allergie.size());
		for (Allergene a : allergie)
			allergie_name.add(a.name());
		String[] fields = {
			nome,
			cognome,
			telefono,
			cellulare,
			indirizzo.toString(),
			String.join(listSeparator+"", note),
			String.join(listSeparator+"", allergie_name),
		};
		info = String.join(fieldSeparator+"", new ArrayList<String>(Arrays.asList(fields)));
		
		net.writeLine(azione.NUOVO_STUD.name() + actionSeparator + info);
		
		String answ = null;
		while (answ == null)
			answ = net.readLine();	
		
		if (answ.isEmpty())
			throw new Exception(net.readLine());
		return Integer.parseInt(answ);
	}
	
	public int aggiungiDocente(String nome, String cognome, String cellulare,
			 Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) throws Exception {
		String info;
		ArrayList<String> allergie_name = new ArrayList<String>(allergie.size());
		for (Allergene a : allergie)
			allergie_name.add(a.name());
		String[] fields = {
			nome,
			cognome,
			cellulare,
			indirizzo.toString(),
			String.join(listSeparator+"", note),
			String.join(listSeparator+"", allergie_name),
		};
		info = String.join(fieldSeparator+"", new ArrayList<String>(Arrays.asList(fields)));
		
		net.writeLine(azione.NUOVO_DOC.name() + actionSeparator + info);
		
		String answ = null;
		while (answ == null)
			answ = net.readLine();
		
		if (answ.isEmpty())
			throw new Exception(net.readLine());
		return Integer.parseInt(answ);
	}
	
	public int cancellaStudente(int key) throws Exception {
		net.writeLine(azione.CANC_STUD.name() + actionSeparator + key);
		String answ = net.readLine();
		if (answ.isEmpty())
			throw new Exception(net.readLine());
		return Integer.parseInt(answ);
	}
	
	public int cancellaDocente(int key) throws Exception {
		net.writeLine(azione.CANC_DOC.name() + actionSeparator + key);
		String answ = net.readLine();
		if (answ.isEmpty())
			throw new Exception(net.readLine());
		return Integer.parseInt(answ);
	}
	
	public int modificaStudente(int key, String campo, Object info) throws Exception {
		String str;
		String risultato;
		
		if (campo.equals("note_del")) {
			int pos = (int)info;
			str = String.join(fieldSeparator+"", Integer.toString(key), campo, Integer.toString(--pos));
		}
		else {
			if (info instanceof Integer)				 
				risultato = "" + (Integer)info;			
			else
				risultato = "" + (String)info;
			str = String.join(fieldSeparator+"", Integer.toString(key), campo, risultato);
		}
		net.writeLine(azione.MOD_STUD.name() + actionSeparator + str);

		String answ = net.readLine();
		if (answ.isEmpty())
			throw new Exception(net.readLine());
		return Integer.parseInt(answ);
	}
	
	public int modificaDocente(int key, String campo, Object info) throws Exception {
		String str;
		String risultato;
	
		if (campo.equals("note_del")) {
			int pos = (int)info;			
			str = String.join(fieldSeparator+"", Integer.toString(key), campo, Integer.toString(--pos));
		}
		else {	
			if (info instanceof Integer)				 
				risultato = "" + (Integer)info;			
			else
				risultato = "" + (String)info;			
			
			str = String.join(fieldSeparator+"", Integer.toString(key), campo, risultato);
		}
		net.writeLine(azione.MOD_DOC.name() + actionSeparator + str);

		String answ = net.readLine();
		if (answ.isEmpty())
			throw new Exception(net.readLine());
		return Integer.parseInt(answ);
	}
	
	public ArrayList<Studente> elencoStudenti() throws Exception {
		net.writeLine(azione.ELENCO_STUD.name() + actionSeparator + "");
		
		ArrayList<String> answ = net.readList();
		ArrayList<Studente> listaStudenti = new ArrayList<>();
		for (String stud : answ) {
			Studente s = Studente.fromString(stud);
			listaStudenti.add(s);
		}
		return listaStudenti;
	}
	
	public ArrayList<Docente> elencoDocenti() throws Exception {
		net.writeLine(azione.ELENCO_DOC.name() + actionSeparator + "");

		ArrayList<String> answ = net.readList();
		ArrayList<Docente> listaDocenti = new ArrayList<>();
		for (String doc : answ) {
			Docente d = Docente.fromString(doc);
			listaDocenti.add(d);
		}
		return listaDocenti;
	}
	
	public ArrayList<Piatto> elencoPiatti() throws Exception {
		net.writeLine(azione.ELENCO_PIATTI.name() + actionSeparator + "");
		
		ArrayList<String> answ = net.readList();
		ArrayList<Piatto> listaPiatti = new ArrayList<>();
		for (String pi : answ) {
			Piatto p = Piatto.fromString(pi);
			listaPiatti.add(p);
		}
		return listaPiatti;
	}
	
	public ArrayList<Piatto> elencoPiatti(String tipo) throws Exception {
		net.writeLine(azione.ELENCO_PIATTI.name() + actionSeparator + tipo);

		ArrayList<String> answ = net.readList();
		ArrayList<Piatto> listaPiatti = new ArrayList<>();
		for (String pi : answ) {
			Piatto p = Piatto.fromString(pi);
			listaPiatti.add(p);
		}
		return listaPiatti;
	}
	
	public Menu generaMenu(int primo, int secondo, int dessert) throws Exception {
		String str = "" + primo + '\t' + secondo + '\t' + dessert;
		net.writeLine(azione.MENU_GEN.name() + actionSeparator + str);
		
		String answ = null;
		while (answ == null)
			answ = net.readLine();
		
		if (answ.length() == 0) {
			throw new Exception(net.readLine());
		}
		
		Menu menu = Menu.fromString(answ);
		
		return menu;
	}
	
	public Studente getStudente(int id) throws Exception {
		net.writeLine(azione.GET_STUD.name() + actionSeparator + id);
		
		String answ = null;
		while (answ == null)
			answ = net.readLine();

		if (answ.length() == 0) {
			throw new Exception(net.readLine());
		}
		Studente stud = Studente.fromString(answ);
		
		return stud;
	}
	
	public Docente getDocente(int id) throws Exception {
		net.writeLine(azione.GET_DOC.name() + actionSeparator + id);
		
		String answ = null;
		while (answ == null)
			answ = net.readLine();
		
		if (answ.length() == 0) {
			throw new Exception(net.readLine());
		}
		Docente doc = Docente.fromString(answ);		
		return doc;
	}
	
	public Piatto getPiatto(int id) throws Exception {
		net.writeLine(azione.GET_PIATTO.name() + actionSeparator + id);
		
		String answ = net.readLine();
		if (answ.isEmpty())
			throw new Exception(net.readLine());
		Piatto piatto = Piatto.fromString(answ);
		
		return piatto;
	}

	public ArrayList<ArrayList<Persona>> getAllergici() {
		ArrayList<ArrayList<Persona>> listaAll = new ArrayList<>(3);
		ArrayList<Persona> listaPrimo = new ArrayList<Persona>();
		ArrayList<Persona> listaSecondo = new ArrayList<Persona>();
		ArrayList<Persona> listaDessert = new ArrayList<Persona>();

		net.writeLine(azione.GET_ALLERGICI.name() + actionSeparator + "");
		
		try {
			ArrayList<ArrayList<String>> res = net.readListOfList();
			for (String r : res.get(0)) {
				try {
					Studente s = Studente.fromString(r);
					listaPrimo.add(s);
				} catch (Exception e) {
					Docente d = Docente.fromString(r);
					listaPrimo.add(d);
				}
			}
			
			for (String r : res.get(1)) {
				try {
					Studente s = Studente.fromString(r);
					listaSecondo.add(s);
				} catch (Exception e) {
					Docente d = Docente.fromString(r);
					listaSecondo.add(d);
				}
			}
			
			for (String r : res.get(2)) {
				try {
					Studente s = Studente.fromString(r);
					listaDessert.add(s);
				} catch (Exception e) {
					Docente d = Docente.fromString(r);
					listaDessert.add(d);
				}
			}
	
			listaAll.add(listaPrimo);
			listaAll.add(listaSecondo);
			listaAll.add(listaDessert);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listaAll;
	}

}
