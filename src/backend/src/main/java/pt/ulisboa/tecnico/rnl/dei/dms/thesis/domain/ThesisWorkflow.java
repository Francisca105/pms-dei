package pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain;

import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "thesis_workflows")
public class ThesisWorkflow {

    public enum ThesisState {
        PROPOSAL_SUBMITTED, APPROVED_SC, PRESIDENT_ASSIGNED, DOCUMENT_SIGNED, SUBMITTED_FENIX
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

    public ThesisWorkflow() {
    }

    public ThesisWorkflow(Person student, ThesisState state, List<Person> jury, Person president, ThesisDocument signedDocument) {
        this.student = student;
        this.state = state;
        this.jury = jury;
        this.president = president;
        this.signedDocument = signedDocument;
    }

    public Long getId() {
        return id;
    }

    public Person getStudent() {
        return student;
    }

    public void setStudent(Person student) {
        this.student = student;
    }

    public ThesisState getState() {
        return state;
    }

    public void setState(ThesisState state) {
        this.state = state;
    }

    public List<Person> getJury() {
        return jury;
    }

    public void setJury(List<Person> jury) {
        this.jury = jury;
    }

    public Person getPresident() {
        return president;
    }

    public void setPresident(Person president) {
        this.president = president;
    }

    public ThesisDocument getSignedDocument() {
        return signedDocument;
    }

    public void setSignedDocument(ThesisDocument signedDocument) {
        this.signedDocument = signedDocument;
    }

    public void setSignedDocument(byte[] content, String filename) {
        this.signedDocument = new ThesisDocument(content, filename, this);
        this.state = ThesisState.DOCUMENT_SIGNED;
    }

    public void deleteSignedDocument() {
        this.signedDocument = null;
        this.state = ThesisState.PRESIDENT_ASSIGNED;
    }
}