package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import database.Allergene;
import database.Piatto;
import database.Piatto.TipoPiatto;

public class PiattoTest {

	@Test
	public void testGetKey() {
		Piatto p = Piatto.fromString("123\tprimo\tCaciotte al pesto\t");
		assertEquals(123, p.getKey());
	}

	@Test
	public void testGetTipo() {
		Piatto p = Piatto.fromString("123\tprimo\tCaciotte al pesto\t");
		assertEquals(TipoPiatto.primo, p.getTipo());
	}

	@Test
	public void testGetTipoFail() {
		try {
			Piatto.fromString("123\tbevanda\tCaciotte al pesto\t");
			fail("Bevanda non dovrebbe essere accettato");
		} catch (Exception e) {
			assert(true);
		}
	}

	@Test
	public void testGetTipoStr() {
		Piatto p = Piatto.fromString("123\tprimo\tCaciotte al pesto\t");
		assertEquals("primo", p.getTipoStr());
	}

	@Test
	public void testGetNome() {
		Piatto p = Piatto.fromString("123\tprimo\tCaciotte al pesto\t");
		assertEquals("Caciotte al pesto", p.getNome());
	}

	@Test
	public void testGetAllergeni() {
		Piatto p = Piatto.fromString("123\tprimo\tCaciotte al pesto\t[glutine,latte,banane]");
		ArrayList<Allergene> allergeni = p.getAllergeni();
		assertTrue(allergeni.contains(Allergene.glutine));
		assertTrue(allergeni.contains(Allergene.latte));
		for (Allergene a : Allergene.values())
			if (a == Allergene.glutine || a == Allergene.latte)
				;
			else
				assertFalse(allergeni.contains(a));
	}

	@Test
	public void testGetAllergeniEmpty() {
		Piatto p = Piatto.fromString("123\tprimo\tCaciotte al pesto\t[]");
		ArrayList<Allergene> allergeni = p.getAllergeni();
		assertEquals(null, allergeni);
	}

	@Test
	public void testToString() {
		Piatto p = Piatto.fromString("123\tprimo\tCaciotte al pesto\t");
		assertEquals("123\tprimo\tCaciotte al pesto\t[]", p.toString());
		Piatto p1 = Piatto.fromString("123\tprimo\tCaciotte al pesto\t[glutine,latte]");
		assertEquals("123\tprimo\tCaciotte al pesto\t[glutine, latte]", p1.toString());
	}

}
