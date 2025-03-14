package pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto;

import pt.ulisboa.tecnico.rnl.dei.dms.person.dto.PersonDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow.ThesisState;

import java.util.List;

public class ThesisWorkflowDto {
    private Long id;
    private PersonDto student;
    private ThesisState state;
    private List<PersonDto> jury;
    private PersonDto president;
    private ThesisDocumentDto signedDocument;

    public ThesisWorkflowDto() {
    }

    public ThesisWorkflowDto(ThesisWorkflow thesisWorkflow) {
        this.id = thesisWorkflow.getId();
        this.student = new PersonDto(thesisWorkflow.getStudent());
        this.state = thesisWorkflow.getState();
        this.jury = thesisWorkflow.getJury().stream().map(PersonDto::new).toList();
        this.president = thesisWorkflow.getPresident() != null ? new PersonDto(thesisWorkflow.getPresident()) : null;
        this.signedDocument = thesisWorkflow.getSignedDocument() != null ? new ThesisDocumentDto(thesisWorkflow.getSignedDocument()) : null;
    }

    public ThesisWorkflowDto(Long id, PersonDto student, ThesisState state, List<PersonDto> jury, PersonDto president, ThesisDocumentDto signedDocument) {
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

    public ThesisState getState() {
        return state;
    }

    public void setState(ThesisState state) {
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