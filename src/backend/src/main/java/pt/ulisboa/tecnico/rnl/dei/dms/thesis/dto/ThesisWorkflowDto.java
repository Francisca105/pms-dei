package pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto;

import pt.ulisboa.tecnico.rnl.dei.dms.person.dto.PersonDto;

import java.util.List;

public class ThesisWorkflowDto {
    private Long id;
    private PersonDto student;
    private String state;
    private List<PersonDto> jury;
    private PersonDto president;
    private ThesisDocumentDto signedDocument;

    public ThesisWorkflowDto() {
    }

    public ThesisWorkflowDto(Long id, PersonDto student, String state, List<PersonDto> jury, PersonDto president, ThesisDocumentDto signedDocument) {
        this.id = id;
        this.student = student;
        this.state = state;
        this.jury = jury;
        this.president = president;
        this.signedDocument = signedDocument;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonDto getStudent() {
        return student;
    }

    public void setStudent(PersonDto student) {
        this.student = student;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<PersonDto> getJury() {
        return jury;
    }

    public void setJury(List<PersonDto> jury) {
        this.jury = jury;
    }

    public PersonDto getPresident() {
        return president;
    }

    public void setPresident(PersonDto president) {
        this.president = president;
    }

    public ThesisDocumentDto getSignedDocument() {
        return signedDocument;
    }

    public void setSignedDocument(ThesisDocumentDto signedDocument) {
        this.signedDocument = signedDocument;
    }
}