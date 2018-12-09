package userInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import communication.Client;
import database.*;

public class Presentation {

	private DataAccess dataAccess;
	private Scanner input;
	
	public static void main(String[] args) throws IOException {
		try {
			Presentation p = new Presentation(args);
			p.run();
		} catch(Exception e) {
			System.out.println("Il server non è in ascolto");
			e.printStackTrace();
			return;
		}
	}
	
	public Presentation (String[] args) throws Exception {
		if (args.length == 0)
			dataAccess = new Client("localhost");	
		else 
			try {
				dataAccess = new Client(InetAddress.getByName(args[0]));
			} catch (Exception e) {
				System.err.println("! L'argomento non è un indirizzo, uso localhost\n");
				dataAccess = new Client("localhost");
				e.printStackTrace();
			}
				
		input = new Scanner(System.in);
	}
		
	public void run() {
		System.out.println("Programma di gestione mensa scolastica\n");
		System.out.println("Digitare il numero corrispondente all'azione da eseguire:");
		
		do {
			System.out.println("\n[1] Gestione studenti\n[2] Gestione docenti\n[3] Gestione menù\n");
			int scelta = -1;
			try {
				scelta = checkInput();
			} catch(InputMismatchException e) {
				e.printStackTrace();
			}

			switch(scelta) {
			case 1:
				studMenu();
				break;
			case 2:
				docMenu();
				break;
			case 3:
				menuMenu();
				break;
			default:
				System.out.println("Inserire un numero valido:");
				break;
			}
		} while (true);
	}
	
	
	
	
	//STUDENTI
	private void studMenu() {
		input.nextLine();
		
		do {
			System.out.println("\n[1] Aggiungi nuovo studente\n[2] Modifica dati studente\n"
					+ "[3] Elimina studente\n[4] Vedi lista studenti\n[0] Torna al menù precedente\n");
			
			int scelta = checkInput();
			
			switch(scelta) {
			case 0:
				return;
			case 1:
				nuovoStud();
				break;		
			case 2:
				modStud();
				break;
			case 3:
				elimStud();
				break;
			case 4:
				try {
					ArrayList<Studente> lista = dataAccess.elencoStudenti();
					for (Studente s : lista)
						System.out.println(s);
					if (lista.isEmpty())
						System.out.println("\nNon è stato inserito nessuno studente\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("\nInserire un numero valido:\n");
			}
		} while (true);
	}
	
	private void nuovoStud() {
		input.nextLine();
		
		System.out.println("\nInserisci nome:");
		String nome = input.nextLine().trim();
		System.out.println("Cognome:");
		String cognome = input.nextLine().trim();
		String telefono;
		while (true) {
			System.out.println("Numero di telefono:");
			telefono = input.nextLine().trim();
			if (telefono.matches("\\d+"))
				break;
			System.out.println("!!! Il dato inserito non è un numero\n");
		}
		String cellulare;
		while (true) {
			System.out.println("Cellulare:");
			cellulare = input.nextLine().trim();	
			if (cellulare.matches("\\d+"))
				break;
			System.out.println("!!! Il dato inserito non è un numero\n");
		}
		System.out.println("Indirizzo (via):");
		String via = input.nextLine().trim();
		String cap;
		while (true) {
			System.out.println("CAP:");
			cap = input.nextLine().trim();
			if (cap.matches("[0-9]{5}"))
				break;
			System.out.println("!!! Il dato inserito non è un CAP valido (5 cifre)\n");
		}
		System.out.println("Città:");
		String citta = input.nextLine().trim();
		System.out.println("Note (separare con virgola):");
		String note = input.nextLine();
		//chiedo il numero corrispondente all'allergia (vedi file ReadMe) per limitare gli errori di digitazione
		String allergie = "";
		while (true) {
			System.out.println("Allergie (separare con virgola: 1 = glutine, 2 = crostacei, 3 = uova, " + 
					" 4 = pesce, 5 = arachidi, 6 = soia, 7 = latte, 8 = frutta a guscio, 9 = sedano, 10 = senape, " + 
					" 11 = sesamo, 12 = solfiti, 13 = lupini, 14 = molluschi):");
			allergie = input.nextLine();
			if (allergie.isEmpty())
				break;
			String[] allList = allergie.split(",");
			boolean check = false;
			for (int i = 0; i<allList.length; i++)
				if (allList[i].matches("\\d+") && Integer.parseInt(allList[i].trim()) <= 14 && Integer.parseInt(allList[i].trim()) >= 1)
					check = true;
			if (check == true)
				break;
			else
				System.out.println("!!! Sono stati inseriti dei valori non validi\n");
		}
			
		ArrayList<String> note_list = new ArrayList<String>(Arrays.asList(note.split(",")));
		ArrayList<String> allergie_list = new ArrayList<String>(Arrays.asList(allergie.split(",")));
		ArrayList<Allergene> allergeni = new ArrayList<Allergene>(allergie_list.size());
		for (String a : allergie_list)
			try {
				Allergene allergeneInserito = Allergene.values()[Integer.parseInt(a.trim())-1];
				allergeni.add(allergeneInserito);
			} catch (IllegalArgumentException e) {
				if (!a.isEmpty())
					System.out.println("C'è stato un problema con l'individuazione di un allergene, verrà ignorato");
			}
		
		try {
			int idStudente = dataAccess.aggiungiStudente(
				nome,
				cognome,
				telefono,
				cellulare,
				new Indirizzo(via, cap, citta),
				note_list,
				allergeni
			);
			System.out.println("\n---Inserito studente---");
			System.out.println(dataAccess.getStudente(idStudente).toString());
		} catch(Exception e) {
			System.out.println("! Errore nell'inserimento dello studente: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println();
	}
	
	private void modStud() {
		input.nextLine();
		
		System.out.println("Inserisci ID studente da modificare:");
		int key = input.nextInt();
		boolean esiste = false;
		try {
			//stampo i dati dello studente indicato
			System.out.println(dataAccess.getStudente(key).toString());
			esiste = true;
		} catch (Exception e1) {
			System.err.println(e1.getMessage());
			System.out.println("L'ID indicato non esiste\n");
			return;
		}
		
		if (esiste) {
			System.out.println("\nQuale campo vuoi modificare?");
			System.out.println("[1] Nome\n[2] Cognome\n[3] Telefono\n[4] Cellulare\n"
					+ "[5] Indirizzo\n[6] Note\n[7] Allergie\n[0] Indietro");
			int scelta = input.nextInt();
			input.nextLine();
			
			String info;
			switch(scelta) {
		
			case 1:
				System.out.println("Nuovo nome: ");
				info = input.nextLine();
				try {
					dataAccess.modificaStudente(key, "nome", info);
					System.out.println("\n---Modifica effettuata---");
					System.out.println(dataAccess.getStudente(key).toString());
				} catch (Exception e) {
					System.out.println("! Errore nella modifica dello studente " + e.getMessage());
					e.printStackTrace();
				}
				System.out.println();
				break;
				
			case 2:
				System.out.println("Nuovo cognome: ");
				info = input.nextLine();
				try {
					dataAccess.modificaStudente(key, "cognome", info);
					System.out.println("\n---Modifica effettuata---");
					System.out.println(dataAccess.getStudente(key).toString());
				} catch (Exception e) {
					System.out.println("! Errore nella modifica dello studente " + e.getMessage());
					e.printStackTrace();
				}
				System.out.println();
				break;
				
			case 3:
				System.out.println("Nuovo telefono:");
				info = input.nextLine().trim();
				while (! info.matches("\\d+")) {
					System.out.println("!!! Il dato inserito non è un numero\n");
					System.out.println("Nuovo telefono:");
					info = input.nextLine().trim();
				}
				try {
					dataAccess.modificaStudente(key, "telefono", info);
					System.out.println("\n---Modifica effettuata---");
					System.out.println(dataAccess.getStudente(key).toString());
				} catch (Exception e) {
					System.out.println("! Errore nella modifica dello studente " + e.getMessage());
					e.printStackTrace();
				}
				System.out.println();
				break;
				
			case 4:
				while (true) {
					System.out.println("Nuovo cellulare:");
					info = input.nextLine().trim();	
					if (info.matches("\\d+"))
						break;
					System.out.println("!!! Il dato inserito non è un numero\n");
				}
				try {
					dataAccess.modificaStudente(key, "cellulare", info);
					System.out.println("\n---Modifica effettuata---");
					System.out.println(dataAccess.getStudente(key).toString());
				} catch (Exception e) {
					System.out.println("! Errore nella modifica dello studente " + e.getMessage());
					e.printStackTrace();
				}
				System.out.println();
				break;
				
			case 5:
				System.out.println("Nuovo indirizzo: ");
				System.out.println("Via:");
				String via = input.nextLine().trim();
				String cap;
				while (true) {
					System.out.println("CAP:");
					cap = input.nextLine().trim();
					if (cap.matches("[0-9]{5}"))
						break;
					System.out.println("!!! Il dato inserito non è un CAP valido (5 cifre)\n");
				}
				System.out.println("Città: ");
				String citta = input.nextLine().trim();
				info = new Indirizzo(via, cap, citta).toString();
				try {
					dataAccess.modificaStudente(key, "indirizzo", info);
					System.out.println("\n---Modifica effettuata---");
					System.out.println(dataAccess.getStudente(key).toString());
				} catch (Exception e) {
					System.out.println("! Errore nella modifica dello studente " + e.getMessage());
					e.printStackTrace();
				}
				System.out.println();
				break;
				
			case 6:
				System.out.println("Vuoi aggiungere o eliminare una nota?");
				System.out.println("[1] Aggiungi\n[2] Elimina");
				int n = checkInput();
				input.nextLine();
				if (n == 1) {
					System.out.println("Nuova nota: ");
					info = input.nextLine();
					try {
						dataAccess.modificaStudente(key, "note_add", info);	//1 per volta
						System.out.println("\n---Modifica effettuata---");
						System.out.println(dataAccess.getStudente(key).toString());
					} catch (Exception e) {
						System.out.println("! Errore nella modifica dello studente " + e.getMessage());
						e.printStackTrace();
					}
				}
				else if (n == 2) {
					System.out.println("Indica la posizione della nota da eliminare (es. \"Note: nota1, nota2, nota3\" posizione nota1 = 1, posizione nota2 = 2...)");
					int pos = input.nextInt();
					input.nextLine();
					try {
						dataAccess.modificaStudente(key, "note_del", pos);
						System.out.println("\n---Modifica effettuata---");
						System.out.println(dataAccess.getStudente(key).toString());
					} catch (Exception e) {
						System.out.println("!!! Questa nota non esiste");
						System.out.println("! Errore nella modifica dello studente: " + e.getMessage());
						e.printStackTrace();
					}
				}
				else
					System.out.println("!!! Scelta non valida");
				System.out.println();
				break;
				
			case 7:
				System.out.println("Vuoi aggiungere o eliminare un'allergia?");	//1 per volta
				System.out.println("[1] Aggiungi\n[2] Elimina");
				int num = checkInput();
				input.nextLine();
				
				if (num == 1) {
					//chiedo il numero corrispondente all'allergia (vedi file ReadMe) per limitare gli errori di digitazione
					while (true) {
						System.out.println("Nuova allergia (1 = glutine, 2 = crostacei, 3 = uova, "
								+ "4 = pesce, 5 = arachidi, 6 = soia, 7 = latte, 8 = frutta a guscio, 9 = sedano, 10 = senape, "
								+ "11 = sesamo, 12 = solfiti, 13 = lupini, 14 = molluschi): ");
						info = input.nextLine();
						if (info.matches("\\d+") && Integer.parseInt(info.trim()) <= 14 && Integer.parseInt(info.trim()) >= 1)
							break;
						System.out.println("!!! Il valore inserito non è valido\n");
					}
					try {
						dataAccess.modificaStudente(key, "allergie_add", info);
						System.out.println("\n---Modifica effettuata---");
						System.out.println(dataAccess.getStudente(key).toString());
					} catch (Exception e) {
						System.out.println("! Errore nella modifica dello studente " + e.getMessage());
						e.printStackTrace();
					}
				}
				else if (num == 2) {
					//chiedo il numero corrispondente all'allergia (vedi file ReadMe) per limitare gli errori di digitazione
					System.out.println("Indica il codice dell'allergia da eliminare (1 = glutine, 2 = crostacei, 3 = uova, "
							+ "4 = pesce, 5 = arachidi, 6 = soia, 7 = latte, 8 = frutta a guscio, 9 = sedano, 10 = senape, "
							+ "11 = sesamo, 12 = solfiti, 13 = lupini, 14 = molluschi)");
					int pos = input.nextInt();
					try {
						dataAccess.modificaStudente(key, "allergie_del", pos);
						System.out.println("\n---Modifica effettuata---");
						System.out.println(dataAccess.getStudente(key).toString());
					} catch (Exception e) {
						System.out.println("! Errore nella modifica dello studente " + e.getMessage());
						e.printStackTrace();
					}
				}
				else
					System.out.println("! Questo numero non è valido");
				System.out.println();
				break;
			}
		}
	}
	
	private void elimStud() {
		input.nextLine();
		
		System.out.println("Inserisci ID studente da eliminare: ");
		int key = input.nextInt();
		boolean esiste = false;
		try {
			System.out.println(dataAccess.getStudente(key).toString());
			esiste = true;
		} catch (Exception e1) {
			System.err.println(e1.getMessage());
			System.out.println("\n! L'ID selezionato non esiste");
		}
		
		if (esiste) {
			try {
				dataAccess.cancellaStudente(key);
				System.out.println("\n---Studente eliminato---");
			} catch (Exception e) {
				System.out.println("! Errore nell'eliminazione dello studente " + e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println();
	}
	
	
	
	
	//DOCENTI
	private void docMenu() {
		input.nextLine();
		
		do {
			System.out.println("[1] Aggiungi nuovo docente\n[2] Modifica dati docente\n"
					+ "[3] Elimina docente\n[4] Vedi lista docenti\n[0] Torna al menù precedente\n");
			
			int scelta = checkInput();
			
			switch(scelta) {
			case 0:
				return;
			case 1:
				nuovoDoc();
				break;
			case 2:
				modDoc();
				break;
			case 3:
				elimDoc();
				break;
			case 4:
				try {
					ArrayList<Docente> lista = dataAccess.elencoDocenti();
					for (Docente d : lista)
						System.out.println(d);
					if (lista.isEmpty())
						System.out.println("\nNon è stato inserito nessun docente\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("\nInserire un numero valido:\n");
			}
		} while (true);
	}
	
	private void nuovoDoc() {
		input.nextLine();
		
		System.out.println("\nInserisci nome:");
		String nome = input.nextLine().trim();
		System.out.println("Cognome:");
		String cognome = input.nextLine().trim();
		String cellulare;
		while (true) {
			System.out.println("Cellulare:");
			cellulare = input.nextLine().trim();	
			if (cellulare.matches("\\d+"))
				break;
			System.out.println("!!! Il dato inserito non è un numero\n");
		}
		System.out.println("Indirizzo (via):");
		String via = input.nextLine().trim();
		String cap;
		while (true) {
			System.out.println("CAP:");
			cap = input.nextLine().trim();
			if (cap.matches("[0-9]{5}"))
				break;
			System.out.println("!!! Il dato inserito non è un CAP valido (5 cifre)\n");
		}
		System.out.println("Città: ");
		String citta = input.nextLine().trim();
		System.out.println("Note (separare con virgola):");
		String note = input.nextLine();
		//chiedo il numero corrispondente all'allergia (vedi file ReadMe) per limitare gli errori di digitazione
		String allergie = "";
		while (true) {
			System.out.println("Allergie (separare con virgola: 1 = glutine, 2 = crostacei, 3 = uova, " + 
					" 4 = pesce, 5 = arachidi, 6 = soia, 7 = latte, 8 = frutta a guscio, 9 = sedano, 10 = senape, " + 
					" 11 = sesamo, 12 = solfiti, 13 = lupini, 14 = molluschi):");
			allergie = input.nextLine();
			if (allergie.isEmpty())
				break;
			String[] allList = allergie.split(",");
			boolean check = false;
			for (int i = 0; i<allList.length; i++)
				if (allList[i].matches("\\d+") && Integer.parseInt(allList[i].trim()) <= 14 && Integer.parseInt(allList[i].trim()) >= 1)
					check = true;
			if (check == true)
				break;
			else
				System.out.println("!!! Sono stati inseriti dei valori non validi\n");
		}
		
		ArrayList<String> note_list = new ArrayList<String>(Arrays.asList(note.split(",")));
		ArrayList<String> allergie_list = new ArrayList<String>(Arrays.asList(allergie.split(",")));
		ArrayList<Allergene> allergeni = new ArrayList<Allergene>(allergie_list.size());
		for (String a : allergie_list)
			try {
				Allergene allergeneInserito = Allergene.values()[Integer.parseInt(a.trim())-1];
				allergeni.add(allergeneInserito);	
			} catch (IllegalArgumentException e) {
				if (!a.isEmpty())
					System.out.println("Allergene non valido: " + a + ", verrà ignorato");
			}
		
		try {
			int idDocente = dataAccess.aggiungiDocente(
				nome,
				cognome,
				cellulare,
				new Indirizzo(via, cap, citta),
				note_list,
				allergeni
			);
			System.out.println("\n---Inserito docente---");
			System.out.println(dataAccess.getDocente(idDocente).toString());
		} catch(Exception e) {
			System.out.println("Errore nell'inserimento del docente: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println();
	}
	
	private void modDoc() {
		input.nextLine();
		
		System.out.println("Inserisci ID docente da modificare: ");
		int key = input.nextInt();
		boolean esiste = false;
		try {
			System.out.println(dataAccess.getDocente(key).toString());
			esiste = true;
		} catch (Exception e1) {
			System.err.println(e1.getMessage());
			System.out.println("L'ID indicato non esiste\n");
		}
		if (esiste) {
			System.out.println("\nQuale campo vuoi modificare?");
			System.out.println("[1] Nome\n[2] Cognome\n[3] Cellulare\n"
					+ "[4] Indirizzo\n[5] Note\n[6] Allergie\n[0] Indietro");
			int scelta = input.nextInt();
			input.nextLine();
			
			String info;
			switch(scelta) {
			
			case 1:
				System.out.println("Nuovo nome: ");
				info = input.nextLine();
				try {
					dataAccess.modificaDocente(key, "nome", info);
					System.out.println("Docente modificato");
					System.out.println(dataAccess.getDocente(key).toString());
				} catch (Exception e) {
					System.out.println("Errore nella modifica del docente" + e.getMessage());
					e.printStackTrace();
				}
				System.out.println();
				break;
				
			case 2:
				System.out.println("Nuovo cognome: ");
				info = input.nextLine();
				try {
					dataAccess.modificaDocente(key, "cognome", info);
					System.out.println("Docente modificato");
					System.out.println(dataAccess.getDocente(key).toString());
				} catch (Exception e) {
					System.out.println("Errore nella modifica del docente" + e.getMessage());
					e.printStackTrace();
				}
				System.out.println();
				break;
				
			case 3:
				System.out.println("Nuovo cellulare: ");
				info = input.nextLine();
				try {
					dataAccess.modificaDocente(key, "cellulare", info);
					System.out.println("Docente modificato");
					System.out.println(dataAccess.getDocente(key).toString());
				} catch (Exception e) {
					System.out.println("Errore nella modifica del docente" + e.getMessage());
					e.printStackTrace();
				}
				System.out.println();
					break;
				
			case 4:
				System.out.println("Nuovo indirizzo: ");
				System.out.println("Via:");
					String via = input.nextLine().trim();
				System.out.println("CAP: ");
					String cap = input.nextLine().trim();
					// cap.matches("[0..9](5)");
				System.out.println("Città: ");
					String citta = input.nextLine().trim();
				info = new Indirizzo(via, cap, citta).toString();
				try {
					dataAccess.modificaDocente(key, "indirizzo", info);
					System.out.println("Docente modificato");
					System.out.println(dataAccess.getDocente(key).toString());
				} catch (Exception e) {
					System.out.println("Errore nella modifica del docente" + e.getMessage());
					e.printStackTrace();
				}
				System.out.println();
				break;
				
			case 5:
				System.out.println("Vuoi aggiungere o eliminare una nota?");	//1 per volta
				System.out.println("[1] Aggiungi\n[2] Elimina");
				int n = input.nextInt();
				input.nextLine();
				
				if (n == 1) {
					System.out.println("Nuova nota: ");
					info = input.nextLine();				
					try {
						dataAccess.modificaDocente(key, "note_add", info);			
						System.out.println("Docente modificato");				
						System.out.println(dataAccess.getDocente(key).toString());					
					} catch (Exception e) {
						System.out.println("Errore nella modifica del docente: " + e.getMessage());
						e.printStackTrace();
					}
				}
				else if (n == 2) {
					System.out.println("Indica la posizione della nota da eliminare (es. \"Note: nota1, nota2, nota3\" posizione nota1 = 1, posizione nota2 = 2...)");
					int pos = input.nextInt();
					input.nextLine();
					try {
						dataAccess.modificaDocente(key, "note_del", pos);
						System.out.println("Docente modificato");
						System.out.println(dataAccess.getDocente(key).toString());
					} catch (Exception e) {
						System.out.println("Errore nella modifica del docente" + e.getMessage());
						e.printStackTrace();
					}
				}
				else
					System.out.println("Questa nota non esiste");
				System.out.println();
				break;
				
			case 6:
				System.out.println("Vuoi aggiungere o eliminare un'allergia?");	//1 per volta
				System.out.println("[1] Aggiungi\n[2] Elimina");
				int num = input.nextInt();
				input.nextLine();
				
				if (num == 1) {
					while (true) {
						System.out.println("Nuova allergia (1 = glutine, 2 = crostacei, 3 = uova, "
								+ "4 = pesce, 5 = arachidi, 6 = soia, 7 = latte, 8 = frutta a guscio, 9 = sedano, 10 = senape, "
								+ "11 = sesamo, 12 = solfiti, 13 = lupini, 14 = molluschi: ");
						info = input.nextLine();
						if (info.matches("\\d+") && Integer.parseInt(info.trim()) <= 14 && Integer.parseInt(info.trim()) >= 1)
							break;
						System.out.println("!!! Il valore inserito non è valido\n");
					}
					try {
						dataAccess.modificaDocente(key, "allergie_add", info);
						System.out.println("Modifica effettuata");
						System.out.println(dataAccess.getDocente(key).toString());
					} catch (Exception e) {
						System.out.println("Errore nella modifica del docente: " + e.getMessage());
						e.printStackTrace();
					}
				}
				else if (num == 2) {
					System.out.println("Indica il codice dell'allergia da eliminare (1 = glutine, 2 = crostacei, 3 = uova, "
							+ "4 = pesce, 5 = arachidi, 6 = soia, 7 = latte, 8 = frutta a guscio, 9 = sedano, 10 = senape, "
							+ "11 = sesamo, 12 = solfiti, 13 = lupini, 14 = molluschi)");
					int pos = input.nextInt();
					try {
						dataAccess.modificaDocente(key, "allergie_del", pos);
						System.out.println("Modifica effettuata");
						System.out.println(dataAccess.getDocente(key).toString());
					} catch (Exception e) {
						System.out.println("Errore nella modifica del docente: " + e.getMessage());
						e.printStackTrace();
					}
				}
				else
					System.out.println("Questo numero non è valido");
				System.out.println();
				break;
			}
		}
	}
	
	private void elimDoc() {
		input.nextLine();
		
		System.out.println("Inserisci ID docente da eliminare: ");
		int key = input.nextInt();
		boolean esiste = false;
		try {
			System.out.println(dataAccess.getDocente(key).toString());
			esiste = true;
		} catch (Exception e1) {
			System.err.println(e1.getMessage());
			System.out.println("\n! L'ID selezionato non esiste");
		}

		if (esiste) {
			try {
				dataAccess.cancellaDocente(key);
				System.out.println("\n---Docente eliminato---");
			} catch (Exception e) {
				System.out.println("! Errore nell'eliminazione del docente " + e.getMessage());
			}
		}
		System.out.println();
	}
	
	
	
	
	//MENU
	private void menuMenu() {
		boolean menuCreato = false;
		
		input.nextLine();
		
		do {
			System.out.println("[1] Crea menù del giorno\n[2] Vedi lista utenti allergici\n[3] Vedi elenco piatti\n[0] Torna al menù precedente\n");

			int scelta = checkInput();
			switch(scelta) {
			case 0:
				return;
			case 1:
				menuCreato = creaMenu();
				break;
			case 2:
				if (menuCreato == true)
					listaAllergici();
				else
					System.out.println("Devi creare un menù prima di poter vedere gli utenti allergici\n");
				break;
			case 3:
				try {
					ArrayList<Piatto> lista = dataAccess.elencoPiatti();
					for (Piatto p : lista)
						System.out.println(p);
					System.out.println();
					if (lista.isEmpty())
						System.out.println("Non è stato inserito nessun piatto");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Inserire un numero valido:");
			}
		} while (true);
	}
	
	private boolean creaMenu() {
		input.nextLine();
		
		//PRIMO
		System.out.println("Scegli un primo");		//scrivere il codice del piatto
		try {
			ArrayList<Piatto> listaPrimi = dataAccess.elencoPiatti("primo");
			System.out.println("Lista primi disponibili:");
			for (Piatto p : listaPrimi)
				System.out.println(p);
			System.out.println();
			if (listaPrimi.isEmpty())
				System.out.println("Non è stato inserito nessun piatto");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String scelta;
		int primo = 0;
		String primoScelto;
		try {
			do {
				scelta = input.nextLine();
				while (!scelta.matches("\\d+")) {
					System.out.println("\n! Inserire un numero\n");
					scelta = input.nextLine();
				}
				primo = Integer.parseInt(scelta);
				primoScelto = dataAccess.getPiatto(primo).toString();
				if (! primoScelto.contains("primo"))
					System.out.println("\n! Inserisci l'ID di un primo\n");
			} while (! primoScelto.contains("primo"));
			System.out.println(primoScelto);
		} catch (Exception e) {
			System.err.println("Errore nella scelta del piatto" + e.getMessage());
		}
		System.out.println();
		
		//SECONDO
		System.out.println("Scegli un secondo");
		try {
			ArrayList<Piatto> listaSecondi = dataAccess.elencoPiatti("secondo");
			System.out.println("Lista secondi disponibili:");
			for (Piatto p : listaSecondi)
				System.out.println(p);
			System.out.println();
			if (listaSecondi.isEmpty())
				System.out.println("Non è stato inserito nessun piatto\n");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		int secondo = 0;
		String secondoScelto;
		try {
			do {
				scelta = input.nextLine();
				while (!scelta.matches("\\d+")) {
					System.out.println("\n! Inserire un numero\n");
					scelta = input.nextLine();
				}
				secondo = Integer.parseInt(scelta);
				secondoScelto = dataAccess.getPiatto(secondo).toString();
				if (! secondoScelto.contains("secondo"))
					System.out.println("\n! Inserisci l'ID di un secondo\n");
			} while (! secondoScelto.contains("secondo"));
			System.out.println(secondoScelto);
		} catch (Exception e) {
			System.err.println("Errore nella scelta del piatto " + e.getMessage());
		}
		System.out.println();
		
		//DOLCE/FRUTTA
		System.out.println("Scegli un dolce o frutta");
		try {
			ArrayList<Piatto> listaDessert = dataAccess.elencoPiatti("dessert");
			System.out.println("Lista dolci o frutta disponibili:");
			for (Piatto p : listaDessert)
				System.out.println(p);
			System.out.println();
			if (listaDessert.isEmpty())
				System.out.println("Non è stato inserito nessun piatto\n");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		int dessert = 0;
		String dessertScelto;
		try {
			do {
				scelta = input.nextLine();
				while (!scelta.matches("\\d+")) {
					System.out.println("\n! Inserire un numero\n");
					scelta = input.nextLine();
				}
				dessert = Integer.parseInt(scelta);
				dessertScelto = dataAccess.getPiatto(dessert).toString();
				if (! (dessertScelto.contains("frutta") || dessertScelto.contains("dolce")))
					System.out.println("\n! Inserisci l'ID di un dessert\n");
			} while (! (dessertScelto.contains("frutta") || dessertScelto.contains("dolce")));
			System.out.println(dessert);
		} catch (Exception e) {
			System.err.println("Errore nella scelta del piatto " + e.getMessage());
		}
		
		try {
			Menu menuDelGiorno = dataAccess.generaMenu(primo, secondo, dessert);
			String[] menuPerStampa = menuDelGiorno.toString().split("&");
			System.out.println(String.format("\n---Menù del giorno creato---\n%s\n%s\n%s", menuPerStampa[0], menuPerStampa[1], menuPerStampa[2]));
			System.out.println();
		} catch (Exception e) {
			System.err.println("Errore nella creazione del menù " + e.getMessage());
		}
		return true;
	}
	
	private void listaAllergici() {

		ArrayList<ArrayList<Persona>> listaAll;
		try {
			listaAll = dataAccess.getAllergici();
			ArrayList<Persona> listaPrimo = listaAll.get(0);
			ArrayList<Persona> listaSecondo = listaAll.get(1);
			ArrayList<Persona> listaDessert = listaAll.get(2);

		System.out.println("\n---Allergici al primo---");
		if (listaPrimo.isEmpty())
			System.out.println("Nessuno");
		else {
			System.out.println("-Studenti:");
			boolean trovato = false;
			for (int j = 0; j<listaPrimo.size(); j++) {
				if (listaPrimo.get(j) instanceof Studente) {
					System.out.println(listaPrimo.get(j).toString());
					trovato = true;
				}
			}
			if (!trovato)
				System.out.println("Nessuno");
			
			trovato = false;
			System.out.println("\n-Docenti:");
			for (int j = 0; j<listaPrimo.size(); j++) {
				if (listaPrimo.get(j) instanceof Docente) {
					System.out.println(listaPrimo.get(j).toString());
					trovato = true;
				}
			}
			if (!trovato)
				System.out.println("Nessuno");
		}
			
			System.out.println("\n---Allergici al secondo---");
			if (listaSecondo.isEmpty())
				System.out.println("Nessuno");
			else {
				System.out.println("-Studenti:");	
				boolean trovato = false;
				for (int j = 0; j<listaSecondo.size(); j++) {
					if (listaSecondo.get(j) instanceof Studente) {
						System.out.println(listaSecondo.get(j).toString());
						trovato = true;
					}
				}
				if (!trovato)
					System.out.println("Nessuno");
				
				trovato = false;
				System.out.println("\n-Docenti:");
				for (int j = 0; j<listaSecondo.size(); j++) {
					if (listaSecondo.get(j) instanceof Docente) {
						System.out.println(listaSecondo.get(j).toString());
						trovato = true;
					}
				}	
				if (!trovato)
					System.out.println("Nessuno");
			}
			
			System.out.println("\n---Allergici al dolce/frutta---");
			if (listaDessert.isEmpty())
				System.out.println("Nessuno");
			else {
				System.out.println("-Studenti:");
				boolean trovato = false;
				for (int j = 0; j<listaDessert.size(); j++) {
					if (listaDessert.get(j) instanceof Studente) {
						System.out.println(listaDessert.get(j).toString());
						trovato = true;
					}
				}
				if (!trovato)
					System.out.println("Nessuno");
				
				trovato = false;
				System.out.println("\n-Docenti:");
				for (int j = 0; j<listaDessert.size(); j++) {
					if (listaDessert.get(j) instanceof Docente) {
						System.out.println(listaDessert.get(j).toString());
						trovato = true;
					}
				}
				if (!trovato)
					System.out.println("Nessuno");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("!!! Errore nel recupero della lista utenti allergici");
		}
	}
 
	private int checkInput() {
		String in = input.next();
		int numPerDefault = 9;	//numero non usato nelle scelte quindi usa caso default dello switch
		if (in.matches("\\d"))
			return Integer.parseInt(in);
		else
			return numPerDefault;
	}
}