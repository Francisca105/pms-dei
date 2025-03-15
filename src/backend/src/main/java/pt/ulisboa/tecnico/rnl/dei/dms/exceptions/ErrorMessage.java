package pt.ulisboa.tecnico.rnl.dei.dms.exceptions;

public enum ErrorMessage {
	THESIS_NOT_FOUND("Não existe nenhuma tese com o ID %s", 2002),
	THESIS_NAME_NOT_VALID("O nome da tese especificado não é válido.", 2003),
	THESIS_ALREADY_EXISTS("Já existe uma tese com o ID %s", 2005),
	THESIS_SUBMISSION_NOT_STUDENT("A submissão da tese não foi feita por um estudante.", 2006),
	THESIS_APPROVAL_NOT_SC("A aprovação da tese não foi feita por um membro do SC.", 2007),
	THESIS_STATE_TRANSITION_NOT_VALID("A transição de estado da tese não é válida.", 2008),
	THESIS_STATE_NOT_VALID("O estado da tese especificado não é válido.", 2009),
	THESIS_REVERT_NOT_SC("A reversão de estado da tese não foi feita por um membro do SC.", 2010),
	THESIS_REVERT_NOT_VALID("A reversão de estado da tese não é válida.", 2011),
	THESIS_REVERT_NOT_POSSIBLE("A reversão de estado da tese não é possível.", 2012),
	THESIS_PROPOSAL_INVALID_STATE("A submissão da proposta de tese não é possível neste estado.", 2013),
	THESIS_JURY_INVALID_SIZE("O tamanho do júri especificado não é válido.", 2014),
	THESIS_APPROVAL_INVALID_STATE("A aprovação da tese não é possível neste estado.", 2015),
	THESIS_PRESIDENT_INVALID_STATE("A atribuição de presidente da tese não é possível neste estado.", 2016),
	THESIS_PRESIDENT_NOT_IN_JURY("O presidente especificado não está no júri da tese.", 2017),
	THESIS_SIGN_INVALID_STATE("A assinatura do documento da tese não é possível neste estado.", 2018),
	THESIS_DOCUMENT_INVALID("O documento da tese especificado não é válido.", 2019),
	THESIS_FENIX_INVALID_STATE("A submissão da tese no Fénix não é possível neste estado.", 2020),
	THESIS_REVERT_INVALID_STATE("A reversão de estado da tese não é possível neste estado.", 2021),
	THESIS_JURY_DUPLICATE_MEMBERS("O júri especificado contém membros duplicados.", 2022),
	THESIS_JURY_NON_TEACHER_MEMBER("O júri especificado contém membros que não são professores.", 2023),
	THESIS_DOCUMENT_INVALID_DATE("A data de submissão do documento da tese não é válida.", 2024),
	THESIS_STATE_CHANGE_BLOCKED("A mudança de estado da tese não é possível.", 2025),
	THESIS_USER_ALREADY_HAS_THESIS("O utilizador já tem uma tese associada.", 2028),


	DEFENSE_GRADE_SUBMISSION_ERROR("A submissão da nota da defesa não é válida.", 3001),
	DEFENSE_GRADE_SUBMISSION_NOT_STATE("A submissão da nota da defesa não é possível neste estado.", 3002),
	DEFENSE_UNDER_REVIEW_NOT_STATE("A marcação de revisão da defesa não é possível neste estado.", 3003),
	DEFENSE_SCHEDULE_NOT_STATE("O agendamento da defesa não é possível neste estado.", 3004),
	DEFENSE_SCHEDULE_INVALID_DATE("A data de agendamento da defesa não é válida.", 3005),
	DEFENSE_SCHEDULE_PASS_DATE("A data de agendamento da defesa não pode ser no passado.", 3006),
	DEFENSE_GRADE_INVALID("A nota da defesa não é válida, deve ser um valor entre 0 e 20.", 3007),
	DEFENSE_REVERT_INVALID_STATE("A reversão de estado da defesa não é possível neste estado.", 3008),
	DEFENSE_UNDER_REVIEW_FUTURE_DATE("A marcação de revisão da defesa não pode ser feita antes da data de agendamento.", 3009),
	DEFENSE_NO_THESIS_WORKFLOW("A defesa não tem um workflow de tese associado.", 3010),
	DEFENSE_THESIS_NOT_COMPLETE("A tese associada à defesa não está completa.", 3011),
	NO_SUCH_DEFENSE_WORKFLOW("Não existe nenhuma defesa com o ID %s", 3012),
	DEFENSE_STUDENT_THESIS_NOT_FOUND("Não existe nenhuma tese associada ao estudante com o ID %s", 3013),

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
