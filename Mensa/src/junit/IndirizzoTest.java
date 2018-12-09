package junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import database.Indirizzo;

public class IndirizzoTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testIndirizzo() {
		Indirizzo ind = new Indirizzo("via prova 11", "12345", "citta");
		assertEquals("via prova 11", ind.getVia());
		assertEquals("12345", ind.getCap());
		assertEquals("citta", ind.getCitta());
	}

	@Test
	public void testIndirizzoEmpty() {
		Indirizzo ind = new Indirizzo("", "", "");
		assertEquals("", ind.getVia());
		assertEquals("", ind.getCap());
		assertEquals("", ind.getCitta());
	}

	@Test
	public void testSetVia() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		String via = "via prova 11";
		ind.setVia(via);
		assertEquals(via, ind.getVia().toString());
	}
	
	@Test
	public void testSetViaEmpty() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		String via = "";
		ind.setVia(via);
		assertEquals(via, ind.getVia().toString());
	}

	@Test
	public void testSetCap() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		String cap = "11111";
		ind.setCap(cap);
		assertEquals(cap, ind.getCap().toString());
	}
	
	@Test
	public void testSetCapEmpty() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		String cap = "";
		ind.setCap(cap);
		assertEquals(cap, ind.getCap().toString());
	}

	@Test
	public void testSetCitta() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		String citta = "citta";
		ind.setCitta(citta);
		assertEquals(citta, ind.getCitta().toString());
	}
	
	@Test
	public void testSetCittaEmpty() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		String citta = "";
		ind.setCitta(citta);
		assertEquals(citta, ind.getCitta().toString());
	}

	@Test
	public void testGetVia() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		assertEquals("via", ind.getVia().toString());
	}

	@Test
	public void testGetViaEmpty() {
		Indirizzo ind = new Indirizzo("", "12345", "cit");
		assertEquals("", ind.getVia().toString());
	}
	
	@Test
	public void testGetCap() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		assertEquals("12345", ind.getCap().toString());
	}
	
	@Test
	public void testGetCapEmpty() {
		Indirizzo ind = new Indirizzo("via", "", "cit");
		assertEquals("", ind.getCap().toString());
	}

	@Test
	public void testGetCitta() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		assertEquals("cit", ind.getCitta().toString());
	}
	
	@Test
	public void testGetCittaEmpty() {
		Indirizzo ind = new Indirizzo("via", "12345", "");
		assertEquals("", ind.getCitta().toString());
	}

	@Test
	public void testToString() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		assertEquals("via|12345|cit", ind.toString());
	}
	
	@Test
	public void testToStringEmpty() {
		Indirizzo ind = new Indirizzo("", "", "");
		assertEquals("||", ind.toString());
	}

	@Test
	public void testFromString() {
		Indirizzo ind = new Indirizzo("via", "12345", "cit");
		assertEquals(ind.toString(), Indirizzo.fromString("via|12345|cit").toString());
	}

}
