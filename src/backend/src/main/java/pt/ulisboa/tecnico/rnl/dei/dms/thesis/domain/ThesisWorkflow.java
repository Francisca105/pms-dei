package pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person.PersonType;
import pt.ulisboa.tecnico.rnl.dei.dms.person.dto.PersonDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisDocumentDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisWorkflowDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "thesis_workflows")
public class ThesisWorkflow {

    public enum ThesisState {
        NOT_STARTED, PROPOSAL_SUBMITTED, APPROVED_SC, PRESIDENT_ASSIGNED, DOCUMENT_SIGNED, SUBMITTED_FENIX
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Person student;

    @Enumerated(EnumType.STRING)
    private ThesisState state;

    @ManyToMany
    @JoinTable(
        name = "thesis_jury",
        joinColumns = @JoinColumn(name = "thesis_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Person> jury;

    @ManyToOne
    @JoinColumn(name = "president_id")
    private Person president;

    @OneToOne(mappedBy = "thesisWorkflow", cascade = CascadeType.ALL)
    private ThesisDocument signedDocument;

    @OneToOne(mappedBy = "thesisWorkflow", cascade = CascadeType.ALL)
    private DefenseWorkflow defenseWorkflow;


    public ThesisWorkflow() {
        this.setState(ThesisState.NOT_STARTED);
    }

    public ThesisWorkflow(Person student) {
        this();
        setStudent(student);
    }

    public ThesisWorkflow(ThesisWorkflowDto thesisWorkflowDto) {
        this();
        setStudent(thesisWorkflowDto.getStudent());
        setState(thesisWorkflowDto.getState());
        setJury(thesisWorkflowDto.getJury().stream().map(Person::new).toList());
        setPresident(thesisWorkflowDto.getPresident());
        setSignedDocument(new ThesisDocument(thesisWorkflowDto.getSignedDocument()));
    }


    public void submitProposal(List<Person> jury) {
        if (!getState().equals(ThesisState.NOT_STARTED)) {
            throw new DEIException(ErrorMessage.THESIS_PROPOSAL_INVALID_STATE);
        }
        setJury(jury);
        setState(ThesisState.PROPOSAL_SUBMITTED);
    }

    public void approveProposal() {
        if (!getState().equals(ThesisState.PROPOSAL_SUBMITTED)) {
            throw new DEIException(ErrorMessage.THESIS_APPROVAL_INVALID_STATE);
        }
        setState(ThesisState.APPROVED_SC);
    }

    public void assignPresident(Person president) {
        if (!getState().equals(ThesisState.APPROVED_SC)) {
            throw new DEIException(ErrorMessage.THESIS_PRESIDENT_INVALID_STATE);
        }
        if (!getJury().contains(president)) {
            throw new DEIException(ErrorMessage.THESIS_PRESIDENT_NOT_IN_JURY);
        }
        setPresident(president);
        setState(ThesisState.PRESIDENT_ASSIGNED);
    }

    public void signDocument(ThesisDocument document) {
        if (!getState().equals(ThesisState.PRESIDENT_ASSIGNED)) {
            throw new DEIException(ErrorMessage.THESIS_SIGN_INVALID_STATE);
        }
        if (document == null || document.getContent() == null) {
            throw new DEIException(ErrorMessage.THESIS_DOCUMENT_INVALID);
        }
        setSignedDocument(document);
        setState(ThesisState.DOCUMENT_SIGNED);
    }

    public void submitToFenix() {
        if (!getState().equals(ThesisState.DOCUMENT_SIGNED)) {
            throw new DEIException(ErrorMessage.THESIS_FENIX_INVALID_STATE);
        }
        setState(ThesisState.SUBMITTED_FENIX);
    }

    public void setState(ThesisState targetState) {
        if (this.defenseWorkflow != null) {
            DefenseWorkflow.DefenseState defenseState = this.defenseWorkflow.getState();
            if (defenseState != null && defenseState.ordinal() > DefenseWorkflow.DefenseState.SCHEDULED_DEFENSE.ordinal()) {
                throw new DEIException(ErrorMessage.THESIS_STATE_CHANGE_BLOCKED);
            }
        }

        this.state = targetState;

        switch (targetState) {
            case NOT_STARTED:
                this.jury = new ArrayList<>();
                this.president = null;
                this.signedDocument = null;
                break;
            case PROPOSAL_SUBMITTED:
                this.president = null;
                this.signedDocument = null;
                break;
            case APPROVED_SC:
                this.president = null;
                this.signedDocument = null;
                break;
            case PRESIDENT_ASSIGNED:
                this.signedDocument = null;
                break;
            case DOCUMENT_SIGNED:
                break;
            case SUBMITTED_FENIX:
                break;
            default:
                break;
        }
    }

    public void setJury(List<Person> jury) {
        if (jury == null || jury.size() < 1 || jury.size() > 5) {
            throw new DEIException(ErrorMessage.THESIS_JURY_INVALID_SIZE);
        }

        Set<Person> jurySet = new HashSet<>(jury);
        if (jurySet.size() != jury.size()) {
            throw new DEIException(ErrorMessage.THESIS_JURY_DUPLICATE_MEMBERS);
        }
        for (Person member : jury) {
            if (member.getType() != PersonType.TEACHER) {
                throw new DEIException(ErrorMessage.THESIS_JURY_NON_TEACHER_MEMBER);
            }
        }

        this.jury = jury;
    }

    public void setPresident(Person president) {
        if (president != null && !getJury().contains(president)) {
            throw new DEIException(ErrorMessage.THESIS_PRESIDENT_NOT_IN_JURY);
        }
        this.president = president;
    }

    public void setSignedDocument(ThesisDocument document) {
        if (document != null && document.getContent() == null) {
            throw new DEIException(ErrorMessage.THESIS_DOCUMENT_INVALID);
        }
        this.signedDocument = document;
    }
    
    public void setStudent(Person student) { 
        this.student = student; 
    }
    public void setStudent(PersonDto student) {
        if (student == null) {
            throw new DEIException(ErrorMessage.NO_SUCH_PERSON);
        }
        if (student.getType() != PersonType.STUDENT.toString()) {
            throw new DEIException(ErrorMessage.THESIS_SUBMISSION_NOT_STUDENT);
        }
        setStudent(new Person(student));
    }

    public void setPresident(PersonDto president) {
        if (president != null) {
            setPresident(new Person(president));
        }
    }

    public void setSignedDocument(ThesisDocumentDto signedDocument) {
        if (signedDocument != null) {
            setSignedDocument(new ThesisDocument(signedDocument));
        }
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Person getStudent() { return student; }
    public List<Person> getJury() { return jury; }
    public Person getPresident() { return president; }
    public ThesisDocument getSignedDocument() { return signedDocument; }
    public ThesisState getState() { return state; }
    public void setDefenseWorkflow(DefenseWorkflow defenseWorkflow) { this.defenseWorkflow = defenseWorkflow; }
    public DefenseWorkflow getDefenseWorkflow() { return defenseWorkflow; }
}