package pt.ulisboa.tecnico.rnl.dei.dms.defense.dto;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow.DefenseState;
import java.time.LocalDate;

public class DefenseWorkflowDTO {

    private Long id;
    private Long thesisWorkflowId;
    private DefenseState state;
    private LocalDate scheduledDate;
    private Double grade;

    public DefenseWorkflowDTO() {
    }

    public DefenseWorkflowDTO(Long id, Long thesisWorkflowId, DefenseState state, LocalDate scheduledDate, Double grade) {
        this.id = id;
        this.thesisWorkflowId = thesisWorkflowId;
        this.state = state;
        this.scheduledDate = scheduledDate;
        this.grade = grade;
    }

    public DefenseWorkflowDTO(DefenseWorkflow defenseWorkflow) {
        this.id = defenseWorkflow.getId();
        this.thesisWorkflowId = defenseWorkflow.getThesisWorkflow().getId();
        this.state = defenseWorkflow.getState();
        this.scheduledDate = defenseWorkflow.getScheduledDate();
        this.grade = defenseWorkflow.getGrade();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getThesisWorkflowId() { return thesisWorkflowId; }
    public void setThesisWorkflowId(Long thesisWorkflowId) { this.thesisWorkflowId = thesisWorkflowId; }
    public DefenseState getState() { return state; }
    public void setState(DefenseState state) { this.state = state; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }
}
