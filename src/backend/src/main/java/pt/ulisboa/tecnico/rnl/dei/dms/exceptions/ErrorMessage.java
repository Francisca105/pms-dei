package pt.ulisboa.tecnico.rnl.dei.dms.exceptions;

public enum ErrorMessage {
	THESIS_NOT_FOUND("Não existe nenhuma tese com o ID %s", 2002),
	THESIS_NAME_NOT_VALID("O nome da tese especificado não é válido.", 2003),
	THESIS_STATUS_NOT_VALID("O estado da tese especificado não é válido.", 2004),
	THESIS_ALREADY_EXISTS("Já existe uma tese com o ID %s", 2005),

	NO_SUCH_PERSON("Não existe nenhuma pessoa com o ID %s", 1001),
	PERSON_NAME_NOT_VALID("O nome da pessoa especificado não é válido.", 1002),
	PERSON_ISTID_NOT_VALID("O IST ID da pessoa especificado não é válido.", 1003),
	PERSON_EMAIL_NOT_VALID("O email da pessoa especificado não é válido.", 1004),
	PERSON_TYPE_NOT_VALID("A categoria da pessoa especificada não é válido.", 1004),
	PERSON_ALREADY_EXISTS("Já existe uma pessoa com o ID %s", 1003);

	private final String label;
	private final int code;

	ErrorMessage(String label, int code) {
		this.label = label;
		this.code = code;
	}

	public String getLabel() {
		return this.label;
	}

	public int getCode() {
		return this.code;
	}
}
