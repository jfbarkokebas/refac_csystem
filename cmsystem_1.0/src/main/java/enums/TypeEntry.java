package enums;

/**
 * Types: SIMPLE_ENTRY, REDUCE_DEBIT, PAY_OFF_DEBIT
 */
public enum TypeEntry {

	SIMPLE_ENTRY("SIMPLE_ENTRY"), REDUCE_DEBIT("REDUCE_DEBIT"), PAY_OFF_DEBIT("PAY_OFF_DEBIT");

	private final String description;

	TypeEntry(String descricao) {
		this.description = descricao;
	}

	public String getDescription() {
		return description;
	}

//	public static TypeEntry fromDescription(String strEnum) {
//
//		for (TypeEntry type : values()) {
//			if (type.getDescription().equalsIgnoreCase(strEnum)) {
//				return type;
//			}
//		}
//		throw new IllegalArgumentException("Tipo inválido de TypeEntry: " + strEnum);
//	}

	public static TypeEntry fromParameter(String strEnum) {

		if (strEnum == null) {
			throw new IllegalArgumentException("O tipo de entrada precisa ser válido");
		}

		for (TypeEntry type : values()) {
			if (type.getDescription().equalsIgnoreCase(strEnum)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Tipo inválido de TypeEntry: " + strEnum);
	}

}
