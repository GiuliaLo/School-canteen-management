package junit;

import static org.junit.Assert.*;
import java.sql.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import database.Allergene;
import database.Database;
import database.Docente;
import database.Indirizzo;
import database.Menu;
import database.Persona;
import database.Piatto;
import database.Piatto.TipoPiatto;
import database.Studente;

public class DatabaseTest {
	Connection con;
	Menu menuDelGiorno;
	
	@Before
	public void setUp() throws Exception {	
		final String un = "root";
		final String pw = "password";
		final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
		
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection("jdbc:mysql://localhost/MensaScolastica?verifyServerCertificate=false&useSSL=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowMultiQueries=true",un,pw);
		
		String piatto1 = String.format("%d\t%s\t%s\t%s", 1, TipoPiatto.primo, "Pasta al pomodoro", "[glutine]");
		String piatto2 = String.format("%d\t%s\t%s\t%s", 6, TipoPiatto.secondo, "Bastoncini di pesce con puré di patate", "[glutine, latte, pesce, uova]");
		String piatto3 = String.format("%d\t%s\t%s\t%s", 11, TipoPiatto.dolce, "Budino al cioccolato", "[arachidi, glutine, latte, uova]");
		menuDelGiorno = new Menu(Piatto.fromString(piatto1), Piatto.fromString(piatto2), Piatto.fromString(piatto3));
	}

	@Test
	public void testAggiungiStudente() throws Exception {
		Database db = new Database();
		String nome = "nome";
		String cognome = "cognome";
		String telefono = "00000";
		String cellulare = "000000";
		Indirizzo indirizzo = new Indirizzo("via prova 1", "11111", "citta");
		ArrayList<String> note = new ArrayList<>();
		ArrayList<Allergene> allergie = new ArrayList<>();
		
		int uid = db.aggiungiStudente(nome, cognome, telefono, cellulare, indirizzo, note, allergie);
		Studente s = new Studente(uid, nome, cognome, telefono, cellulare, indirizzo, note, allergie);
				
		assertEquals(s.toString(), db.getStudente(uid).toString());
	}
	
	@Test
	public void testAggiungiStudenteSpecial() throws Exception {
		Database db = new Database();
		String nome = "no'me";
		String cognome = "O'neal";
		String telefono = "00000";
		String cellulare = "000000";
		Indirizzo indirizzo = new Indirizzo("via all'orto 1", "11111", "cit'tà");
		ArrayList<String> note = new ArrayList<>();
		ArrayList<Allergene> allergie = new ArrayList<>();
		
		int uid = db.aggiungiStudente(nome, cognome, telefono, cellulare, indirizzo, note, allergie);
		Studente s = new Studente(uid, nome, cognome, telefono, cellulare, indirizzo, note, allergie);
				
		assertEquals(s.toString(), db.getStudente(uid).toString());
	}
	
	@Test
	public void testAggiungiStudenteNoteAll() throws Exception {
		Database db = new Database();
		String nome = "nome";
		String cognome = "cognome";
		String telefono = "00000";
		String cellulare = "000000";
		Indirizzo indirizzo = new Indirizzo("via prova 1", "11111", "citta");
		ArrayList<String> note = new ArrayList<>();
		note.add("nota1");
		note.add("nota2");
		ArrayList<Allergene> allergie = new ArrayList<>();
		allergie.add(Allergene.crostacei);
		
		int uid = db.aggiungiStudente(nome, cognome, telefono, cellulare, indirizzo, note, allergie);
		Studente s = new Studente(uid, nome, cognome, telefono, cellulare, indirizzo, note, allergie);
				
		assertEquals(s.toString(), db.getStudente(uid).toString());
	}
	
	@Test
	public void testAggiungiStudenteEmpty() throws Exception {
		Database db = new Database();
		String nome = "";
		String cognome = "";
		String telefono = "";
		String cellulare = "";
		Indirizzo indirizzo = new Indirizzo("", "", "");
		ArrayList<String> note = new ArrayList<>();
		ArrayList<Allergene> allergie = new ArrayList<>();
		
		int uid = db.aggiungiStudente(nome, cognome, telefono, cellulare, indirizzo, note, allergie);
		Studente s = new Studente(uid, nome, cognome, telefono, cellulare, indirizzo, note, allergie);
		
		assertEquals(s.toString(), db.getStudente(uid).toString());
	}

	@Test
	public void testAggiungiDocente() throws Exception {
		Database db = new Database();
		String nome = "nome";
		String cognome = "cognome";
		String cellulare = "000000";
		Indirizzo indirizzo = new Indirizzo("via prova 1", "11111", "citta");
		ArrayList<String> note = new ArrayList<>();
		ArrayList<Allergene> allergie = new ArrayList<>();
		
		int uid = db.aggiungiDocente(nome, cognome, cellulare, indirizzo, note, allergie);
		Docente d = new Docente(uid, nome, cognome, cellulare, indirizzo, note, allergie);
				
		assertEquals(d.toString(), db.getDocente(uid).toString());
	}
	
	@Test
	public void testAggiungiDocenteSpecial() throws Exception {
		Database db = new Database();
		String nome = "no'me";
		String cognome = "O'neal";
		String cellulare = "000000";
		Indirizzo indirizzo = new Indirizzo("via all'orto 1", "11111", "cit'tà");
		ArrayList<String> note = new ArrayList<>();
		ArrayList<Allergene> allergie = new ArrayList<>();
		
		int uid = db.aggiungiDocente(nome, cognome, cellulare, indirizzo, note, allergie);
		Docente d = new Docente(uid, nome, cognome, cellulare, indirizzo, note, allergie);
				
		assertEquals(d.toString(), db.getDocente(uid).toString());
	}
	
	@Test
	public void testAggiungiDocenteNoteAll() throws Exception {
		Database db = new Database();
		String nome = "nome";
		String cognome = "cognome";
		String cellulare = "000000";
		Indirizzo indirizzo = new Indirizzo("via prova 1", "11111", "citta");
		ArrayList<String> note = new ArrayList<>();
		note.add("nota1");
		note.add("nota2");
		ArrayList<Allergene> allergie = new ArrayList<>();
		allergie.add(Allergene.crostacei);
		
		int uid = db.aggiungiDocente(nome, cognome, cellulare, indirizzo, note, allergie);
		Docente d = new Docente(uid, nome, cognome, cellulare, indirizzo, note, allergie);
				
		assertEquals(d.toString(), db.getDocente(uid).toString());
	}
	
	@Test
	public void testAggiungiDocenteEmpty() throws Exception {
		Database db = new Database();
		String nome = "";
		String cognome = "";
		String cellulare = "";
		Indirizzo indirizzo = new Indirizzo("", "", "");
		ArrayList<String> note = new ArrayList<>();
		ArrayList<Allergene> allergie = new ArrayList<>();
		
		int uid = db.aggiungiDocente(nome, cognome, cellulare, indirizzo, note, allergie);
		Docente d = new Docente(uid, nome, cognome, cellulare, indirizzo, note, allergie);
		
		assertEquals(d.toString(), db.getDocente(uid).toString());
	}

	@Test
	public void testCancellaStudente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		Database db = new Database();
		assertEquals(0, db.cancellaStudente(id));
		try {
			db.getStudente(id);
			fail("non è stato cancellato");
		} catch (Exception e) {
			System.out.println("OK cancellato");
		}
	}
	
	@Test
	public void testCancellaStudenteAssente() throws Exception {
		int id = -1;	//non esiste
		Database db = new Database();
		assertNotEquals(-1, db.cancellaStudente(id));
	}

	@Test
	public void testCancellaDocente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		Database db = new Database();
		assertEquals(0, db.cancellaDocente(id));
		try {
			db.getDocente(id);
			fail("non è stato cancellato");
		} catch (Exception e) {
			System.out.println("OK cancellato");
		}
	}
	
	@Test
	public void testCancellaDocenteFail() throws Exception {
		int id = -1;	//non esiste
		Database db = new Database();
		assertNotEquals(-1, db.cancellaDocente(id));
	}

	@Test
	public void testModificaStudenteNome() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "nome", "nuovoNome");
		rs = stmt.executeQuery("SELECT nome FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovoNome = rs.getString("nome");
		assertEquals("nuovoNome", nuovoNome);
	}
	
	@Test
	public void testModificaStudenteCognome() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "cognome", "nuovoCognome");
		rs = stmt.executeQuery("SELECT cognome FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovoCognome = rs.getString("cognome");
		assertEquals("nuovoCognome", nuovoCognome);
	}

	@Test
	public void testModificaStudenteTelefono() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "telefono", "427189");
		rs = stmt.executeQuery("SELECT telefono FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovoTelefono = rs.getString("telefono");
		assertEquals("427189", nuovoTelefono);
	}
	
	@Test
	public void testModificaStudenteCellulare() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "cellulare", "427189");
		rs = stmt.executeQuery("SELECT cellulare FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovoCellulare = rs.getString("cellulare");
		assertEquals("427189", nuovoCellulare);
	}
	
	@Test
	public void testModificaStudenteIndirizzo() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "indirizzo", "via aaa 11|11111|citta11");
		rs = stmt.executeQuery("SELECT via, cap, citta FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovaVia = rs.getString("via");
		int nuovoCap = rs.getInt("cap");
		String nuovaCitta = rs.getString("citta");
		assertEquals("via aaa 11", nuovaVia);
		assertEquals(11111, nuovoCap);
		assertEquals("citta11", nuovaCitta);
	}
	
	@Test
	public void testModificaStudenteNoteAdd() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "note_add", "notaaaa");
		rs = stmt.executeQuery("SELECT note FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuoveNote = rs.getString("note");
		assertTrue(nuoveNote.contains("notaaaa"));
	}
	
	@Test
	public void testModificaStudenteNoteDelPresente() throws Exception {
		int id;
		String nome = "nome1";
		String cognome = "cognome1";
		String telefono = "01010101";
		String cellulare = "10101010";
		Indirizzo indirizzo = new Indirizzo("via a 10", "01100", "citta1");
		ArrayList<String> note = new ArrayList<>();
		note.add("nota1");
		ArrayList<Allergene> allergie = new ArrayList<>();
		allergie.add(Allergene.glutine);
		
		
		String n = note.toString();
		n = n.substring(1, n.length()-1);
		String content = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', 'studente')",
				nome, cognome, telefono, cellulare, indirizzo.getVia(), indirizzo.getCap(), indirizzo.getCitta(), n);
		
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, telefono, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		rs.close();
		
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + id + ", 'glutine');");
		
		Database db = new Database();
		db.modificaStudente(id, "note_del", "0");
		rs = stmt.executeQuery("SELECT note FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuoveNote = rs.getString("note");
		assertFalse(nuoveNote.contains("nota1"));
	}
	
	@Test
	public void testModificaStudenteNoteDelAssente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1");
		rs.next();
		int id = rs.getInt("idPersona");
		stmt.executeUpdate("UPDATE Persona SET note = '' WHERE idPersona = " + id );
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "note_del", "0");
		rs = stmt.executeQuery("SELECT note FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuoveNote = rs.getString("note");
		assertEquals("", nuoveNote);
	}
	
	@Test
	public void testModificaStudenteAllergieAddAssente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		stmt.executeUpdate("DELETE FROM Allergico "
				+ "WHERE idPersona = " + id + " "
						+ "AND idAllergene = 'glutine';");
		
		Database db = new Database();
		db.modificaStudente(id, "allergie_add", "1");	//testo glutine
		
		assertTrue(stmt.execute("SELECT idPersona FROM Allergico WHERE idAllergene = 'glutine' AND idPersona = " + id));
	}
	
	@Test
	public void testModificaStudenteAllergieAddPresente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		try {
			stmt.executeUpdate("INSERT INTO Allergico "
				+ "(" + id + ", 'glutine');");
		} catch (SQLException e) {
			
		}
		
		Database db = new Database();
		db.modificaStudente(id, "allergie_add", "1");	//testo glutine
		assertTrue(stmt.execute("SELECT idPersona FROM Allergico WHERE idAllergene = 'glutine' AND idPersona = " + id));
	}
	
	@Test
	public void testModificaStudenteAllergieDelPresente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		try {
			stmt.executeUpdate("INSERT INTO Allergico "
				+ "(" + id + ", 'crostacei');");
		} catch (SQLException e) {
			
		}
		
		Database db = new Database();
		db.modificaStudente(id, "allergie_del", "2");
		
		ResultSet rs2 = stmt.executeQuery("SELECT idPersona FROM Allergico WHERE idAllergene = 'crostacei' AND idPersona = " + id);
		assertFalse(rs2.next());
	}
	
	@Test
	public void testModificaStudenteAllergieDelAssente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'studente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		stmt.executeUpdate("DELETE FROM Allergico "
				+ "WHERE idPersona = " + id + " "
						+ "AND idAllergene = 'crostacei';");
		rs.close();
		
		Database db = new Database();
		db.modificaStudente(id, "allergie_del", "2");
		
		ResultSet rs2 = stmt.executeQuery("SELECT idPersona FROM Allergico WHERE idAllergene = 'crostacei' AND idPersona = " + id);
		assertFalse(rs2.next());
	}
	
	@Test
	public void testModificaDocenteNome() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "nome", "nuovoNome");
		rs = stmt.executeQuery("SELECT nome FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovoNome = rs.getString("nome");
		assertEquals("nuovoNome", nuovoNome);
	}
	
	@Test
	public void testModificaDocenteCognome() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "cognome", "nuovoCognome");
		rs = stmt.executeQuery("SELECT cognome FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovoCognome = rs.getString("cognome");
		assertEquals("nuovoCognome", nuovoCognome);
	}

	@Test
	public void testModificaDocenteTelefono() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "telefono", "427189");
		rs = stmt.executeQuery("SELECT telefono FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovoTelefono = rs.getString("telefono");
		assertEquals("427189", nuovoTelefono);
	}
	
	@Test
	public void testModificaDocenteCellulare() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "cellulare", "427189");
		rs = stmt.executeQuery("SELECT cellulare FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovoCellulare = rs.getString("cellulare");
		assertEquals("427189", nuovoCellulare);
	}
	
	@Test
	public void testModificaDocenteIndirizzo() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "indirizzo", "via aaa 11|11111|citta11");
		rs = stmt.executeQuery("SELECT via, cap, citta FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuovaVia = rs.getString("via");
		int nuovoCap = rs.getInt("cap");
		String nuovaCitta = rs.getString("citta");
		assertEquals("via aaa 11", nuovaVia);
		assertEquals(11111, nuovoCap);
		assertEquals("citta11", nuovaCitta);
	}
	
	@Test
	public void testModificaDocenteNoteAdd() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "note_add", "notaaaa");
		rs = stmt.executeQuery("SELECT note FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuoveNote = rs.getString("note");
		assertTrue(nuoveNote.contains("notaaaa"));
	}
	
	@Test
	public void testModificaDocenteNoteDelPresente() throws Exception {
		int id;
		String nome = "nome1";
		String cognome = "cognome1";
		String cellulare = "10101010";
		Indirizzo indirizzo = new Indirizzo("via a 10", "01100", "citta1");
		ArrayList<String> note = new ArrayList<>();
		note.add("nota1");
		ArrayList<Allergene> allergie = new ArrayList<>();
		allergie.add(Allergene.glutine);
		
		
		String n = note.toString();
		n = n.substring(1, n.length()-1);
		String content = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', 'studente')",
				nome, cognome, cellulare, indirizzo.getVia(), indirizzo.getCap(), indirizzo.getCitta(), n);
		
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		rs.close();
		
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + id + ", 'glutine');");
		
		Database db = new Database();
		db.modificaStudente(id, "note_del", "0");
		rs = stmt.executeQuery("SELECT note FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuoveNote = rs.getString("note");
		assertFalse(nuoveNote.contains("notaaaa"));
	}
	
	@Test
	public void testModificaDocenteNoteDelAssente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1");
		rs.next();
		int id = rs.getInt("idPersona");
		stmt.executeUpdate("UPDATE Persona SET note = '' WHERE idPersona = " + id );
		rs.close();
		Database db = new Database();
		db.modificaStudente(id, "note_del", "0");
		rs = stmt.executeQuery("SELECT note FROM Persona WHERE idPersona = " + id);
		rs.next();
		String nuoveNote = rs.getString("note");
		assertEquals("", nuoveNote);
	}
	
	@Test
	public void testModificaDocenteAllergieAddAssente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		stmt.executeUpdate("DELETE FROM Allergico "
				+ "WHERE idPersona = " + id + " "
						+ "AND idAllergene = 'glutine';");
		
		Database db = new Database();
		db.modificaStudente(id, "allergie_add", "1");	//testo glutine
		
		assertTrue(stmt.execute("SELECT idPersona FROM Allergico WHERE idAllergene = 'glutine' AND idPersona = " + id));
	}
	
	@Test
	public void testModificaDocenteAllergieAddPresente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		try {
			stmt.executeUpdate("INSERT INTO Allergico "
				+ "(" + id + ", 'glutine');");
		} catch (SQLException e) {
			
		}
		
		Database db = new Database();
		db.modificaStudente(id, "allergie_add", "1");	//testo glutine
		assertTrue(stmt.execute("SELECT idPersona FROM Allergico WHERE idAllergene = 'glutine' AND idPersona = " + id));
	}
	
	@Test
	public void testModificaDocenteAllergieDelPresente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		rs.close();
		try {
			stmt.executeUpdate("INSERT INTO Allergico "
				+ "(" + id + ", 'crostacei');");
		} catch (SQLException e) {
			
		}
		
		Database db = new Database();
		db.modificaStudente(id, "allergie_del", "2");
		
		ResultSet rs2 = stmt.executeQuery("SELECT idPersona FROM Allergico WHERE idAllergene = 'crostacei' AND idPersona = " + id);
		assertFalse(rs2.next());
	}
	
	@Test
	public void testModificaDocenteAllergieDelAssente() throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT idPersona "
				+ "FROM Persona "
				+ "WHERE tipo = 'docente' "
				+ "LIMIT 1;");
		rs.next();
		int id = rs.getInt("idPersona");
		stmt.executeUpdate("DELETE FROM Allergico "
				+ "WHERE idPersona = " + id + " "
						+ "AND idAllergene = 'crostacei';");
		rs.close();
		
		Database db = new Database();
		db.modificaStudente(id, "allergie_del", "2");
		
		ResultSet rs2 = stmt.executeQuery("SELECT idPersona FROM Allergico WHERE idAllergene = 'crostacei' AND idPersona = " + id);
		assertFalse(rs2.next());
	}

	@Test
	public void testElencoStudenti() throws Exception {
		Statement stmt = con.createStatement();
		stmt.executeUpdate("DELETE FROM Persona WHERE tipo = 'studente'");
		
		int id1;
		int id2;
		String nome = "nome1";
		String cognome = "cognome1";
		String telefono = "01010101";
		String cellulare = "10101010";
		Indirizzo indirizzo = new Indirizzo("via a 10", "01100", "citta1");
		ArrayList<String> note = new ArrayList<>();
		note.add("nota1");
		ArrayList<Allergene> allergie = new ArrayList<>();
		allergie.add(Allergene.glutine);
		
		String n = note.toString();
		n = n.substring(1, n.length()-1);
		String content = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', 'studente')",
				nome, cognome, telefono, cellulare, indirizzo.getVia(), indirizzo.getCap(), indirizzo.getCitta(), n);
		
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, telefono, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		id1 = rs.getInt(1);
		
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + id1 + ", 'glutine');");
		
		Studente stud1 = new Studente(id1, nome, cognome, telefono, cellulare, indirizzo, note, allergie);
		
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, telefono, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		rs = stmt.getGeneratedKeys();
		rs.next();
		id2 = rs.getInt(1);
		
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + id2 + ", 'glutine');");
		
		Studente stud2 = new Studente(id2, nome, cognome, telefono, cellulare, indirizzo, note, allergie);
		
		ArrayList<Studente> listaStudenti = new ArrayList<>();
		listaStudenti.add(stud1);
		listaStudenti.add(stud2);

		Database db = new Database();
		assertEquals(listaStudenti.toString(), db.elencoStudenti().toString());
	}

	@Test
	public void testGetStudente() throws Exception {
		int id;
		String nome = "nome1";
		String cognome = "cognome1";
		String telefono = "01010101";
		String cellulare = "10101010";
		Indirizzo indirizzo = new Indirizzo("via a 10", "01100", "citta1");
		ArrayList<String> note = new ArrayList<>();
		note.add("nota1");
		ArrayList<Allergene> allergie = new ArrayList<>();
		allergie.add(Allergene.glutine);
		
		
		String n = note.toString();
		n = n.substring(1, n.length()-1);
		String content = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', 'studente')",
				nome, cognome, telefono, cellulare, indirizzo.getVia(), indirizzo.getCap(), indirizzo.getCitta(), n);
		
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, telefono, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + id + ", 'glutine');");
		
		Database db = new Database();
		Studente stud = new Studente(id, nome, cognome, telefono, cellulare, indirizzo, note, allergie);
		Studente s = db.getStudente(id);
		
		assertEquals(stud.toString(), s.toString());
	}

	@Test
	public void testElencoDocenti() throws Exception {
		Statement stmt = con.createStatement();
		stmt.executeUpdate("DELETE FROM Persona WHERE tipo = 'docente'");
		
		int id1;
		int id2;
		String nome = "nome1";
		String cognome = "cognome1";
		String cellulare = "10101010";
		Indirizzo indirizzo = new Indirizzo("via a 10", "01100", "citta1");
		ArrayList<String> note = new ArrayList<>();
		note.add("nota1");
		ArrayList<Allergene> allergie = new ArrayList<>();
		allergie.add(Allergene.glutine);
		
		String n = note.toString();
		n = n.substring(1, n.length()-1);
		String content = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', 'docente')",
				nome, cognome, cellulare, indirizzo.getVia(), indirizzo.getCap(), indirizzo.getCitta(), n);
		
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		id1 = rs.getInt(1);
		
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + id1 + ", 'glutine');");
		
		Docente doc1 = new Docente(id1, nome, cognome, cellulare, indirizzo, note, allergie);
		
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		rs = stmt.getGeneratedKeys();
		rs.next();
		id2 = rs.getInt(1);
		
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + id2 + ", 'glutine');");
		
		Docente doc2 = new Docente(id2, nome, cognome, cellulare, indirizzo, note, allergie);
		
		ArrayList<Docente> listaDocenti = new ArrayList<>();
		listaDocenti.add(doc1);
		listaDocenti.add(doc2);

		Database db = new Database();
		assertEquals(listaDocenti.toString(), db.elencoDocenti().toString());	}

	@Test
	public void testGetDocente() throws Exception {
		int id;
		String nome = "nome1";
		String cognome = "cognome1";
		String cellulare = "10101010";
		Indirizzo indirizzo = new Indirizzo("via a 10", "01100", "citta1");
		ArrayList<String> note = new ArrayList<>();
		note.add("nota1");
		ArrayList<Allergene> allergie = new ArrayList<>();
		allergie.add(Allergene.glutine);
		
		
		String n = note.toString();
		n = n.substring(1, n.length()-1);
		String content = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', 'docente')",
				nome, cognome, cellulare, indirizzo.getVia(), indirizzo.getCap(), indirizzo.getCitta(), n);
		
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Persona"
				+ "(nome, cognome, cellulare, via, cap, citta, note, tipo) VALUES"
				+ content + ";", Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + id + ", 'glutine');");
		
		Database db = new Database();
		Docente doc = new Docente(id, nome, cognome, cellulare, indirizzo, note, allergie);
		Docente d = db.getDocente(id);
		
		assertEquals(doc.toString(), d.toString());
	}

	@Test
	public void testElencoPiatti() throws Exception {
		String piatto1 = String.format("%d\t%s\t%s\t%s", 1, TipoPiatto.primo, "Pasta al pomodoro", "[glutine]");
		String piatto2 = String.format("%d\t%s\t%s\t%s", 2, TipoPiatto.primo, "Pasta al pesto", "[frutta_a_guscio, glutine]");
		String piatto3 = String.format("%d\t%s\t%s\t%s", 3, TipoPiatto.primo, "Riso olio e parmigiano", "[glutine, latte]");
		String piatto4 = String.format("%d\t%s\t%s\t%s", 4, TipoPiatto.primo, "Lasagne alla bolognese", "[glutine, sedano, uova]");
		String piatto5 = String.format("%d\t%s\t%s\t%s", 5, TipoPiatto.secondo, "Petto di pollo con zucchine", "[]");
		String piatto6 = String.format("%d\t%s\t%s\t%s", 6, TipoPiatto.secondo, "Bastoncini di pesce con puré di patate", "[glutine, latte, pesce, uova]");
		String piatto7 = String.format("%d\t%s\t%s\t%s", 7, TipoPiatto.secondo, "Frittata con carote", "[uova]");
		String piatto8 = String.format("%d\t%s\t%s\t%s", 8, TipoPiatto.secondo, "Bresaola al limone con spinaci", "[]");
		String piatto9 = String.format("%d\t%s\t%s\t%s", 9, TipoPiatto.frutta, "Macedonia", "[]");
		String piatto10 = String.format("%d\t%s\t%s\t%s", 10, TipoPiatto.frutta, "Frutta di stagione", "[]");
		String piatto11 = String.format("%d\t%s\t%s\t%s", 11, TipoPiatto.dolce, "Budino al cioccolato", "[arachidi, glutine, latte, uova]");
		String piatto12 = String.format("%d\t%s\t%s\t%s", 12, TipoPiatto.dolce, "Crostata ai frutti di bosco", "[glutine, uova]");
		String piatto13 = String.format("%d\t%s\t%s\t%s", 13, TipoPiatto.dolce, "Gelato", "[latte, soia]");

		ArrayList<Piatto> listaPiatti = new ArrayList<>();
		listaPiatti.add(Piatto.fromString(piatto1));
		listaPiatti.add(Piatto.fromString(piatto2));
		listaPiatti.add(Piatto.fromString(piatto3));
		listaPiatti.add(Piatto.fromString(piatto4));
		listaPiatti.add(Piatto.fromString(piatto5));
		listaPiatti.add(Piatto.fromString(piatto6));
		listaPiatti.add(Piatto.fromString(piatto7));
		listaPiatti.add(Piatto.fromString(piatto8));
		listaPiatti.add(Piatto.fromString(piatto9));
		listaPiatti.add(Piatto.fromString(piatto10));
		listaPiatti.add(Piatto.fromString(piatto11));
		listaPiatti.add(Piatto.fromString(piatto12));
		listaPiatti.add(Piatto.fromString(piatto13));
		
		Database db = new Database();
		assertEquals(listaPiatti.toString(), db.elencoPiatti().toString());
	}

	@Test
	public void testElencoPiattiPrimo() throws Exception {
		String piatto1 = String.format("%d\t%s\t%s\t%s", 1, TipoPiatto.primo, "Pasta al pomodoro", "[glutine]");
		String piatto2 = String.format("%d\t%s\t%s\t%s", 2, TipoPiatto.primo, "Pasta al pesto", "[frutta_a_guscio, glutine]");
		String piatto3 = String.format("%d\t%s\t%s\t%s", 3, TipoPiatto.primo, "Riso olio e parmigiano", "[glutine, latte]");
		String piatto4 = String.format("%d\t%s\t%s\t%s", 4, TipoPiatto.primo, "Lasagne alla bolognese", "[glutine, sedano, uova]");
		
		ArrayList<Piatto> listaPrimi = new ArrayList<>();
		listaPrimi.add(Piatto.fromString(piatto1));
		listaPrimi.add(Piatto.fromString(piatto2));
		listaPrimi.add(Piatto.fromString(piatto3));
		listaPrimi.add(Piatto.fromString(piatto4));
		
		Database db = new Database();
		assertEquals(listaPrimi.toString(), db.elencoPiatti("primo").toString());
	}
	
	@Test
	public void testElencoPiattiSecondo() throws Exception {
		String piatto1 = String.format("%d\t%s\t%s\t%s", 5, TipoPiatto.secondo, "Petto di pollo con zucchine", "[]");
		String piatto2 = String.format("%d\t%s\t%s\t%s", 6, TipoPiatto.secondo, "Bastoncini di pesce con puré di patate", "[glutine, latte, pesce, uova]");
		String piatto3 = String.format("%d\t%s\t%s\t%s", 7, TipoPiatto.secondo, "Frittata con carote", "[uova]");
		String piatto4 = String.format("%d\t%s\t%s\t%s", 8, TipoPiatto.secondo, "Bresaola al limone con spinaci", "[]");
		
		ArrayList<Piatto> listaSecondi = new ArrayList<>();
		listaSecondi.add(Piatto.fromString(piatto1));
		listaSecondi.add(Piatto.fromString(piatto2));
		listaSecondi.add(Piatto.fromString(piatto3));
		listaSecondi.add(Piatto.fromString(piatto4));
		
		Database db = new Database();
		assertEquals(listaSecondi.toString(), db.elencoPiatti("secondo").toString());
	}
	
	@Test
	public void testElencoPiattiDessert() throws Exception {
		String piatto1 = String.format("%d\t%s\t%s\t%s", 9, TipoPiatto.frutta, "Macedonia", "[]");
		String piatto2 = String.format("%d\t%s\t%s\t%s", 10, TipoPiatto.frutta, "Frutta di stagione", "[]");
		String piatto3 = String.format("%d\t%s\t%s\t%s", 11, TipoPiatto.dolce, "Budino al cioccolato", "[arachidi, glutine, latte, uova]");
		String piatto4 = String.format("%d\t%s\t%s\t%s", 12, TipoPiatto.dolce, "Crostata ai frutti di bosco", "[glutine, uova]");
		String piatto5 = String.format("%d\t%s\t%s\t%s", 13, TipoPiatto.dolce, "Gelato", "[latte, soia]");

		ArrayList<Piatto> listaDessert = new ArrayList<>();
		listaDessert.add(Piatto.fromString(piatto1));
		listaDessert.add(Piatto.fromString(piatto2));
		listaDessert.add(Piatto.fromString(piatto3));
		listaDessert.add(Piatto.fromString(piatto4));
		listaDessert.add(Piatto.fromString(piatto5));
		
		Database db = new Database();
		assertEquals(listaDessert.toString(), db.elencoPiatti("dessert").toString());
	}

	@Test
	public void testGetPiatto() throws Exception {
		String piatto = String.format("%d\t%s\t%s\t%s", 1, TipoPiatto.primo, "Pasta al pomodoro", "[glutine]");
		
		Database db = new Database();
		assertEquals(piatto, db.getPiatto(1).toString());
	}

	@Test
	public void testGeneraMenu() throws Exception {
		String piatto1 = String.format("%d\t%s\t%s\t%s", 1, TipoPiatto.primo, "Pasta al pomodoro", "[glutine]");
		String piatto2 = String.format("%d\t%s\t%s\t%s", 5, TipoPiatto.secondo, "Petto di pollo con zucchine", "[]");
		String piatto3 = String.format("%d\t%s\t%s\t%s", 9, TipoPiatto.frutta, "Macedonia", "[]");

		Menu menu = new Menu(Piatto.fromString(piatto1), Piatto.fromString(piatto2), Piatto.fromString(piatto3));
		
		Database db = new Database();
		assertEquals(menu.toString(), db.generaMenu(1, 5, 9).toString());
	}

	@Test
	public void testGetAllergici() throws Exception {
		//aggiungere 2 studenti e 2 docenti nel DB se non ci sono
		
		Statement stmt = con.createStatement();
		stmt.executeUpdate("DELETE FROM Allergico");
		
		ResultSet rs = stmt.executeQuery("SELECT idPersona FROM Persona WHERE tipo = 'studente' LIMIT 2");
		rs.next();
		int idStud1 = rs.getInt("idPersona");
		rs.next();
		int idStud2 = rs.getInt("idPersona");
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + idStud1 + ", 'glutine'), (" + idStud1 + ", 'latte'), (" + idStud2 + ", 'pesce')");
		
		rs = stmt.executeQuery("SELECT idPersona FROM Persona WHERE tipo = 'docente' LIMIT 2");
		rs.next();
		int idDoc1 = rs.getInt("idPersona");
		rs.next();
		int idDoc2 = rs.getInt("idPersona");
		stmt.executeUpdate("INSERT INTO Allergico (idPersona, idAllergene) VALUES (" + idDoc1 + ", 'latte'), (" + idDoc1 + ", 'uova'), (" + idDoc2 + ", 'crostacei')");
		
		Database db = new Database();
		
		ArrayList<Persona> listaPrimo = new ArrayList<Persona>();
		listaPrimo.add(db.getStudente(idStud1));
		
		ArrayList<Persona> listaSecondo = new ArrayList<Persona>();
		listaSecondo.add(db.getStudente(idStud1));
		listaSecondo.add(db.getStudente(idStud2));
		listaSecondo.add(db.getDocente(idDoc1));
		
		ArrayList<Persona> listaDessert = new ArrayList<Persona>();
		listaDessert.add(db.getStudente(idStud1));
		listaDessert.add(db.getDocente(idDoc1));
		
		ArrayList<ArrayList<Persona>> listaAll = new ArrayList<>(3);
		listaAll.add(listaPrimo);
		listaAll.add(listaSecondo);
		listaAll.add(listaDessert);
		
		db.generaMenu(1, 6, 11);
		
		boolean result = false;
		ArrayList<ArrayList<Persona>> listaDB = db.getAllergici(); 
		for (ArrayList<Persona> lp : listaDB) {
			for (Persona p : lp) {
				if (!lp.toString().contains(p.toString())) {
					result = false;
					break;
				}
				result = true;
			}
		}
		assertTrue(result);
	}

}
