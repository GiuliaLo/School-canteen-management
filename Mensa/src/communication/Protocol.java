package communication;

public abstract class Protocol {
	
	public static final int port = 21345;
	
	/* '|' usato da Indirizzo
	 * '_' usato da azione
	 * '&' usato da Menu
	 */
	
	public static final char fieldSeparator = '\t';
	public static final char listSeparator = ',';
	public static final char actionSeparator = '$';
	
	protected static enum azione {
		NUOVO_STUD,
		NUOVO_DOC,
		CANC_STUD,
		CANC_DOC,
		MOD_STUD,
		MOD_DOC,
		ELENCO_STUD,
		ELENCO_DOC,
		ELENCO_PIATTI,
		GET_STUD,
		GET_DOC,
		GET_PIATTO,
		MENU_GEN,
		GET_ALLERGICI
	}
}
