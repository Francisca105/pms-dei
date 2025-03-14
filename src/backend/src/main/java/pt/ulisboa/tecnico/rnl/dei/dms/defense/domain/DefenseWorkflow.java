package pt.ulisboa.tecnico.rnl.dei.dms.defense.domain;

import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "defense_workflows")
public class DefenseWorkflow {

    public enum DefenseState {
        NOT_SCHEDULED, SCHEDULED_DEFENSE, UNDER_REVIEW, SUBMITTED_FENIX
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "thesis_workflow_id", nullable = false)
    private ThesisWorkflow thesisWorkflow;

    @Enumerated(EnumType.STRING)
    private DefenseState state;

    private LocalDate scheduledDate;
    private Double grade;

    public DefenseWorkflow() {
        setState(DefenseState.NOT_SCHEDULED); // INITIAL STATE
    }

    public DefenseWorkflow(ThesisWorkflow thesisWorkflow) {
        setThesisWorkflow(thesisWorkflow);
        setState(DefenseState.NOT_SCHEDULED);
    }

    public void scheduleDefense(LocalDate date) {
        if (getState() != DefenseState.NOT_SCHEDULED) {
            throw new DEIException(ErrorMessage.DEFENSE_SCHEDULE_NOT_STATE);
        }
        if (date.isBefore(LocalDate.now())) {
            throw new DEIException(ErrorMessage.DEFENSE_SCHEDULE_PASS_DATE);
        }
        setScheduledDate(date);
        setState(DefenseState.SCHEDULED_DEFENSE);
    }

    public void markUnderReview() {
        if (getState() != DefenseState.SCHEDULED_DEFENSE ) {
            throw new DEIException(ErrorMessage.DEFENSE_UNDER_REVIEW_NOT_STATE);
        }
        if (getScheduledDate().isAfter(LocalDate.now())) {
            throw new DEIException(ErrorMessage.DEFENSE_UNDER_REVIEW_FUTURE_DATE);
        }
        setState(DefenseState.UNDER_REVIEW);
    }

    public void submitGrade(Double grade) {
        if (getState().ordinal() < DefenseState.SCHEDULED_DEFENSE.ordinal()) {
            throw new DEIException(ErrorMessage.DEFENSE_GRADE_SUBMISSION_NOT_STATE);
        }
        
        setGrade(grade);
        setState(DefenseState.SUBMITTED_FENIX);
    }

    public void revertState(DefenseState targetState) {
        if (targetState.ordinal() >= getState().ordinal()) {
            throw new DEIException(ErrorMessage.DEFENSE_REVERT_INVALID_STATE);
        }
        setState(targetState);
        if (targetState == DefenseState.NOT_SCHEDULED) {
            setScheduledDate(null);
            setGrade(null);
        } else if (targetState == DefenseState.SCHEDULED_DEFENSE) {
            setGrade(null);
        }
    }

    public Long getId() {
        return id; 
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ThesisWorkflow getThesisWorkflow() {
        return thesisWorkflow;
    }

    public void setThesisWorkflow(ThesisWorkflow thesisWorkflow) {
        this.thesisWorkflow = thesisWorkflow;
    }

    public DefenseState getState() {
        return state;
    }

    public void setState(DefenseState state) {
        this.state = state;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        if (scheduledDate != null && scheduledDate.isBefore(LocalDate.now())) {
            throw new DEIException(ErrorMessage.DEFENSE_SCHEDULE_PASS_DATE);
        }
        this.scheduledDate = scheduledDate;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        if (grade != null && (grade < 0 || grade > 20)) {
            throw new DEIException(ErrorMessage.DEFENSE_GRADE_INVALID);
        }
        this.grade = grade;
    }
}