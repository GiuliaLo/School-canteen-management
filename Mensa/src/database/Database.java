package database;

import java.sql.*;
import java.util.ArrayList;

import database.Piatto.TipoPiatto;

public class Database implements DataAccess {
	Menu menuDelGiorno;
	
	private static final String un = "root";
	private static final String pw = "password";
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	private Connection con;
	
	public Database() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		con  = DriverManager.getConnection(
	         "jdbc:mysql://localhost/MensaScolastica?verifyServerCertificate=false&useSSL=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowMultiQueries=true",un,pw);
	}
		
	@Override
	public int aggiungiStudente(String nome, String cognome, String telefono, String cellulare, Indirizzo indirizzo,
			ArrayList<String> note, ArrayList<Allergene> allergie) throws Exception {
		if (nome.contains("'"))
			nome = nome.replace("'", "''");
		if (cognome.contains("'"))
			cognome = cognome.replace("'", "''");
		String n = note.toString();
		if (note.isEmpty())
			n = "";
		else
			n = n.substring(1, n.length()-1);
		
		Statement stmt = con.createStatement();
		String content = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', 'studente')",
				nome, cognome, telefono, cellulare, indirizzo.getVia().replace("'", "''"), indirizzo.getCap(), indirizzo.getCitta().replace("'", "''"), n);
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, telefono, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		
		ResultSet rs = stmt.getGeneratedKeys();
		
		if (rs.next()) {
			int uid = rs.getInt(1);
			
			if (! allergie.isEmpty()) {
				String query = "INSERT INTO Allergico(idPersona, idAllergene) VALUES ";
				ArrayList<String> contents = new ArrayList<>(allergie.size());
				
				for (Allergene a : allergie)
					contents.add(String.format("(%d, '%s')", uid, a.name()));
				
				String values = String.join(", ", contents);
				query += values + ";";
				stmt.executeUpdate(query);
			}
			
			rs.close();
			stmt.close();
			return uid;
		}
		
		return 0;
	}

	@Override
	public int aggiungiDocente(String nome, String cognome, String cellulare, Indirizzo indirizzo,
			ArrayList<String> note, ArrayList<Allergene> allergie) throws Exception {
		if (nome.contains("'"))
			nome = nome.replace("'", "''");
		if (cognome.contains("'"))
			cognome = cognome.replace("'", "''");
		String n = note.toString();
		if (note.isEmpty())
			n = "";
		else
			n = n.substring(1, n.length()-1);
		
		Statement stmt = con.createStatement();
		String content = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', 'docente')",
				nome, cognome, cellulare, indirizzo.getVia().replace("'", "''"), indirizzo.getCap(), indirizzo.getCitta().replace("'", "''"), n);
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		
		ResultSet rs = stmt.getGeneratedKeys();
		
		if (rs.next()) {
			int uid = rs.getInt(1);
			
			if (! allergie.isEmpty()) {
				String query = "INSERT INTO Allergico(idPersona, idAllergene) VALUES ";
				ArrayList<String> contents = new ArrayList<>(allergie.size());
				
				for (Allergene a : allergie)
					contents.add(String.format("(%d, '%s')", uid, a.name()));
				
				String values = String.join(", ", contents);
				query += values + ";";
				stmt.executeUpdate(query);
			}
			
			rs.close();
			stmt.close();
			return uid;
		}
		return 0;
	}

	@Override
	public int cancellaStudente(int key) throws Exception {
		return cancellaPersona(key);
	}

	@Override
	public int cancellaDocente(int key) throws Exception {
		return cancellaPersona(key);
	}
	
	public int cancellaPersona(int key) throws Exception {
		Statement stmt = con.createStatement();
		stmt.executeUpdate("DELETE FROM Persona WHERE idPersona = " + Integer.toString(key));
		stmt.close();
		return 0;
	}
	
	@Override
	public int modificaStudente(int key, String campo, Object info) throws Exception {
		return modificaPersona(key, campo, info);
	}

	@Override
	public int modificaDocente(int key, String campo, Object info) throws Exception {
		return modificaPersona(key, campo, info);
	}
	
	public int modificaPersona(int key, String campo, Object obj) throws Exception {
		if ( !( obj instanceof String ))
			throw new Exception("Il campo " + campo + " deve essere di tipo Stringa (invece che " + obj.getClass().getSimpleName() + ").");	
		
		String info = (String)obj;
		
		String query = "";
		Allergene all;
		switch (campo) {
		case "allergie_add":
			all = Allergene.values()[Integer.parseInt((String)info) - 1];
			
			try (ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Allergico " + "WHERE idPersona = " + key + " " + "AND  idAllergene = '" + all.name() + "';")) {
				if (rs.next() == true)
					return 0;
			}
			
			query = "INSERT INTO Allergico (idPersona, idAllergene) "
					+ "VALUES (" + key + ", '" + all.name() + "');";
			break;
		case "allergie_del":
			all = Allergene.values()[Integer.parseInt((String)info) - 1];
			query = "DELETE FROM Allergico "
					+ "WHERE idPersona = " + key + " AND "
					+ "idAllergene = '" + all.name() + "';";
			break;
			
		case "note_add":
			query = "UPDATE Persona "
					+ "SET `note` = CONCAT(`note`, ', ', '" + info.replace("'", "''") + "') "
					+ "WHERE idPersona = " + key + " "
						+ "AND `note` is not null "
						+ "AND LENGTH(`note`) > 0;" + "\n"
					+ "UPDATE Persona " + "\n"
					+ "SET `note` = '" + info.replace("'", "''") + "' " + "\n"
					+ "WHERE idPersona = " + key + " " + "\n"
						+ "AND (" + "\n"
							+ "`note` is null " + "\n"
							+ "OR LENGTH(`note`) = 0" + "\n"
						+ ");";
			break;
		case "note_del":
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT `note` FROM Persona WHERE idPersona = " + key +";");
			rs.next();
			String note = rs.getString("note");
			
			String nStr = "";
	   	if (!note.isEmpty()) {
	   		String[] no = note.split(",");
	   		for (int i = 0; i < no.length; i++)
	   			if (i != Integer.parseInt((String)obj))
	   				nStr = nStr + no[i].trim() + ", ";
	   		if (nStr.endsWith(", "))
	   			nStr = nStr.substring(0, nStr.length()-2);
	   	}
	   	
			query = "UPDATE Persona SET `note` = '" + nStr + "' WHERE idPersona = " + key + ";";

			/*
			query = "UPDATE Persona "
					+ "SET `note` = TRIM(REPLACE(REPLACE(`note`, '" + info.replace("'", "''") + "', ''), ', , ', ', ')) "
					+ "WHERE idPersona = " + key + " AND "
							+ "`note` is not null;";
			*/
			
			break;
			
		case "indirizzo":
			Indirizzo ind = Indirizzo.fromString((String)info);
			
			query = "UPDATE Persona "
					+ "SET `citta` = '" + ind.getCitta().replace("'", "''") + "', "
					+ "`via` = '" + ind.getVia().replace("'", "''") + "', "
					+ "`cap` = '" + ind.getCap().replace("'", "''") + "' "
					+ "WHERE idPersona = " + key + ";";
			break;
		default:
			query = "UPDATE Persona "
					+ "SET `" + campo + "` = '" + info.replace("'", "''") + "' "
					+ "WHERE idPersona = " + key + ";";
		}
		
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
		return 0;
	}
	
	@Override
	public ArrayList<Studente> elencoStudenti() throws Exception {
		ArrayList<Studente> listaStud = new ArrayList<>();
		
		Statement stmt = con.createStatement();
		Statement stmtAll = con.createStatement();
		ResultSet rs = stmt.executeQuery(
				"SELECT idPersona, nome, cognome, telefono, cellulare, via, cap, citta, note "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente';");
		
		while (rs.next()) {
			int id = rs.getInt("idPersona");
	      String nome = rs.getString("nome");
	      String cognome = rs.getString("cognome");
	      String telefono = rs.getString("telefono");
	      String cellulare = rs.getString("cellulare");
	      String via = rs.getString("via");
	      String cap = rs.getString("cap");
	      String citta = rs.getString("citta");
	      String note = rs.getString("note");
	      
	      Indirizzo ind = new Indirizzo(via, cap, citta);
	      ArrayList<String> notes = null;
	   	if (!note.isEmpty()) {
	   		String[] no = note.split(",");
	   		notes = new ArrayList<String>(no.length);
	   		for (int i=0; i<no.length; i++)
	   				notes.add(no[i].trim());
	   	}
	      
	      //leggo allergie
	      ResultSet rsAll = stmtAll.executeQuery("SELECT idAllergene FROM Allergico WHERE idPersona = " + id + ";");
	      ArrayList<Allergene> allergie = new ArrayList<>(); 
	      while (rsAll.next()) {
	      	Allergene all = Allergene.valueOf(rsAll.getString("idAllergene"));
	      	allergie.add(all);
	      }
	      rsAll.close();
	      
	      //aggiungo studente a lista
	      Studente stud = new Studente(id, nome, cognome, telefono, cellulare, ind, notes, allergie);
	      listaStud.add(stud);
	      
		}
		rs.close();
	   stmt.close();
	   stmtAll.close();
	   
		return listaStud;
	}

	@Override
	public Studente getStudente(int id) throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT nome, cognome, telefono, cellulare, via, cap, citta, note "
				+ "FROM Persona "
				+ "WHERE idPersona = " + id);
		
		rs.next();
      String nome = rs.getString("nome");
      String cognome = rs.getString("cognome");
      String telefono = rs.getString("telefono");
      String cellulare = rs.getString("cellulare");
      String via = rs.getString("via");
      String cap = rs.getString("cap");
      String citta = rs.getString("citta");
      String note = rs.getString("note");
      
      Indirizzo ind = new Indirizzo(via, cap, citta);
      ArrayList<String> notes = null;
   	if (!note.isEmpty()) {
   		notes = new ArrayList<>();
   		String[] no = note.split(",");
   		for (int i=0; i<no.length; i++)
   				notes.add(no[i].trim());
   	}
   	
   	//leggo allergie
   	Statement stmtAll = con.createStatement();
		ResultSet rsAll = stmtAll.executeQuery("SELECT idAllergene FROM Allergico WHERE idPersona = " + id);
      ArrayList<Allergene> allergie = new ArrayList<>(); 
      while (rsAll.next()) {
      	Allergene all = Allergene.valueOf(rsAll.getString("idAllergene"));
      	allergie.add(all);
      }
      rsAll.close();
   	
		Studente stud = new Studente(id, nome, cognome, telefono, cellulare, ind, notes, allergie);
		return stud;
	}

	@Override
	public ArrayList<Docente> elencoDocenti() throws Exception {
		Statement stmt = con.createStatement();
		Statement stmtAll = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona, nome, cognome, cellulare, via, cap, citta, note "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente';");
		
		ArrayList<Docente> listaDoc = new ArrayList<>();
		
		while (rs.next()) {
			int id = rs.getInt("idPersona");
	      String nome = rs.getString("nome");
	      String cognome = rs.getString("cognome");
	      String cellulare = rs.getString("cellulare");
	      String via = rs.getString("via");
	      String cap = rs.getString("cap");
	      String citta = rs.getString("citta");
	      String note = rs.getString("note");
	      
	      Indirizzo ind = new Indirizzo(via, cap, citta);
	      ArrayList<String> notes = null;
	   	if (!note.isEmpty()) {
	   		notes = new ArrayList<>();
	   		String[] no = note.split(",");
	   		for (int i=0; i<no.length; i++)
	   				notes.add(no[i].trim());
	   	}
	      
	      //leggo allergie
	      ResultSet rsAll = stmtAll.executeQuery("SELECT idAllergene FROM Allergico WHERE idPersona = " + id);
	      ArrayList<Allergene> allergie = new ArrayList<>(); 
	      while (rsAll.next()) {
	      	Allergene all = Allergene.valueOf(rsAll.getString("idAllergene"));
	      	allergie.add(all);
	      }
	      rsAll.close();
	      
	      //aggiungo studente a lista
	      Docente doc = new Docente(id, nome, cognome, cellulare, ind, notes, allergie);
	      listaDoc.add(doc);
	      
		}
		rs.close();
	   stmt.close();
	   
		return listaDoc;
	}

	@Override
	public Docente getDocente(int id) throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT nome, cognome, cellulare, via, cap, citta, note "
				+ "FROM Persona "
				+ "WHERE idPersona = " + id);
		
		rs.next();
      String nome = rs.getString("nome");
      String cognome = rs.getString("cognome");
      String cellulare = rs.getString("cellulare");
      String via = rs.getString("via");
      String cap = rs.getString("cap");
      String citta = rs.getString("citta");
      String note = rs.getString("note");
      
      Indirizzo ind = new Indirizzo(via, cap, citta);
      ArrayList<String> notes = null;
   	if (!note.isEmpty()) {
   		notes = new ArrayList<>();
   		String[] no = note.split(",");
   		for (int i=0; i<no.length; i++)
   				notes.add(no[i].trim());
   	}
   	
   	//leggo allergie
   	Statement stmtAll = con.createStatement();
		ResultSet rsAll = stmtAll.executeQuery("SELECT idAllergene FROM Allergico WHERE idPersona = " + id);
      ArrayList<Allergene> allergie = new ArrayList<>(); 
      while (rsAll.next()) {
      	Allergene all = Allergene.valueOf(rsAll.getString("idAllergene"));
      	allergie.add(all);
      }
      rsAll.close();
   	
		Docente doc = new Docente(id, nome, cognome, cellulare, ind, notes, allergie);
		return doc;
	}

	@Override
	public ArrayList<Piatto> elencoPiatti() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Piatto");
		
		ArrayList<Piatto> listaPiatti = new ArrayList<>();
		
		while (rs.next()) {
	      int id = rs.getInt("idPiatto");
	      String tipo = rs.getString("tipo");
	      String nome = rs.getString("nome");
	      
	      //leggo allergie
	      Statement stmtAll = con.createStatement();
			ResultSet rsAll = stmtAll.executeQuery("SELECT allergene FROM Contiene WHERE idPiatto = " + id);
	      ArrayList<Allergene> allergeni = new ArrayList<>(); 
	      while (rsAll.next()) {
	      	Allergene all = Allergene.valueOf(rsAll.getString("allergene"));
	      	allergeni.add(all);
	      }
	      rsAll.close();
	      
	      Piatto p = new Piatto(id, TipoPiatto.valueOf(tipo), nome, allergeni);
	      listaPiatti.add(p);
	      
		}
      return listaPiatti;
	}

	@Override
	public ArrayList<Piatto> elencoPiatti(String tipo) throws Exception {
		Statement stmt = con.createStatement();
		String query;
		if (!tipo.equals("dessert"))
			query = "SELECT * FROM Piatto WHERE tipo = '" + tipo + "';";
		else
			query = "SELECT * FROM Piatto WHERE tipo = 'frutta' OR tipo = 'dolce';";
		ResultSet rs = stmt.executeQuery(query);
		
		ArrayList<Piatto> listaPiatti = new ArrayList<>();
		
		while (rs.next()) {
	      int id = rs.getInt("idPiatto");
	      String nome = rs.getString("nome");
	      String t = rs.getString("tipo");
	      
	      //leggo allergie
	      Statement stmtAll = con.createStatement();
			ResultSet rsAll = stmtAll.executeQuery("SELECT allergene FROM Contiene WHERE idPiatto = " + id);
	      ArrayList<Allergene> allergeni = new ArrayList<>(); 
	      while (rsAll.next()) {
	      	Allergene all = Allergene.valueOf(rsAll.getString("allergene"));
	      	allergeni.add(all);
	      }
	      rsAll.close();

	      Piatto p = new Piatto(id, TipoPiatto.valueOf(t), nome, allergeni);
	      listaPiatti.add(p);	 
		}
		
      return listaPiatti;
	}

	@Override
	public Piatto getPiatto(int id) throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Piatto WHERE idPiatto = " + id);
				
		rs.next();
      String tipo = rs.getString("tipo");
      String nome = rs.getString("nome");
      
      //leggo allergie
      Statement stmtAll = con.createStatement();
		ResultSet rsAll = stmtAll.executeQuery("SELECT allergene FROM Contiene WHERE idPiatto = " + id);
      ArrayList<Allergene> allergeni = new ArrayList<>(); 
      while (rsAll.next()) {
      	Allergene all = Allergene.valueOf(rsAll.getString("allergene"));
      	allergeni.add(all);
      }
      rsAll.close();
      
      Piatto p = new Piatto(id, TipoPiatto.valueOf(tipo), nome, allergeni);
      
      return p;
	}

	@Override
	public Menu generaMenu(int primo, int secondo, int dessert) throws Exception {
		Piatto p = getPiatto(primo);
		Piatto s = getPiatto(secondo);
		Piatto d = getPiatto(dessert);
		menuDelGiorno = new Menu(p, s, d);
		return menuDelGiorno;
	}

	@Override
	public ArrayList<ArrayList<Persona>> getAllergici() throws Exception {
		ArrayList<Allergene> allergeniPrimo = null;
		ArrayList<Allergene> allergeniSecondo = null;
		ArrayList<Allergene> allergeniDessert = null;
		ArrayList<ArrayList<Persona>> listaAll = new ArrayList<>(3);
		ArrayList<Persona> listaPrimo = new ArrayList<Persona>();
		ArrayList<Persona> listaSecondo = new ArrayList<Persona>();
		ArrayList<Persona> listaDessert = new ArrayList<Persona>();
		
		Piatto pr = menuDelGiorno.getPrimo();
		if (!(pr.getAllergeni() == null)) {
			allergeniPrimo = pr.getAllergeni();
		}
		
		Piatto sec = menuDelGiorno.getSecondo();
		if (!(sec.getAllergeni() == null)) {
			allergeniSecondo = sec.getAllergeni();
		}
		
		Piatto des = menuDelGiorno.getDessert();
		if (!(des.getAllergeni() == null)) {
			allergeniDessert = des.getAllergeni();
		}
		
		Statement stmt = con.createStatement();
		
		if (allergeniPrimo != null)
			for (Allergene a : allergeniPrimo) {
				ResultSet rs = stmt.executeQuery("SELECT idPersona FROM Allergico WHERE idAllergene = '" + a.toString() + "';");
				while (rs.next()) {
					int id = rs.getInt("idPersona");
					
					Statement stmtPers = con.createStatement();
					ResultSet res = stmtPers.executeQuery("SELECT tipo FROM Persona WHERE idPersona = '" + id + "';");
					res.next();
					String tipo = res.getString("tipo");
					if (tipo.equals("studente")) {
						Studente s = getStudente(id);
						if (!listaPrimo.toString().contains(s.toString()))
							listaPrimo.add(s);
					}
					else {
						Docente d = getDocente(id);
						if (!listaPrimo.toString().contains(d.toString()))
							listaPrimo.add(d);
					}
					
					res.close();
					stmtPers.close();
				}
				rs.close();
			}
		
		if (allergeniSecondo != null)
			for (Allergene a : allergeniSecondo) {
				ResultSet rs = stmt.executeQuery("SELECT idPersona FROM Allergico WHERE idAllergene = '" + a.toString() + "';");
				while (rs.next()) {
					int id = rs.getInt("idPersona");
					
					Statement stmtPers = con.createStatement();
					ResultSet res = stmtPers.executeQuery("SELECT tipo FROM Persona WHERE idPersona = '" + id + "';");
					res.next();
					String tipo = res.getString("tipo");
					if (tipo.equals("studente")) {
						Studente s = getStudente(id);
						if (!listaSecondo.toString().contains(s.toString()))
							listaSecondo.add(s);
					}
					else {
						Docente d = getDocente(id);
						if (!listaSecondo.toString().contains(d.toString()))
							listaSecondo.add(d);
					}
					
					res.close();
					stmtPers.close();
				}
				rs.close();
			}
		
		if (allergeniDessert != null)
			for (Allergene a : allergeniDessert) {
				ResultSet rs = stmt.executeQuery("SELECT idPersona FROM Allergico WHERE idAllergene = '" + a.toString() + "';");
				while (rs.next()) {
					int id = rs.getInt("idPersona");
					
					Statement stmtPers = con.createStatement();
					ResultSet res = stmtPers.executeQuery("SELECT tipo FROM Persona WHERE idPersona = '" + id + "';");
					res.next();
					String tipo = res.getString("tipo");
					if (tipo.equals("studente")) {
						Studente s = getStudente(id);
						if (!listaDessert.toString().contains(s.toString()))
							listaDessert.add(s);
					}
					else {
						Docente d = getDocente(id);
						if (!listaDessert.toString().contains(d.toString()))
							listaDessert.add(d);
					}
					res.close();
					stmtPers.close();
				}
				rs.close();
			}
		
		//stmt.close();
		
		listaAll.add(listaPrimo);
		listaAll.add(listaSecondo);
		listaAll.add(listaDessert);
		
		return listaAll;
	}

}
