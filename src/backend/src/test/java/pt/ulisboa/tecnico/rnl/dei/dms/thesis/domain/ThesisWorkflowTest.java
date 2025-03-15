package pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow.ThesisState;

import java.util.List;

class ThesisWorkflowTest {
    private final Person student = new Person("Alice", "ist123", "alice@tecnico.pt", Person.PersonType.STUDENT);
    private final Person teacher1 = new Person("Prof. X", "ist456", "x@tecnico.pt", Person.PersonType.TEACHER);
    private final Person teacher2 = new Person("Prof. Y", "ist789", "y@tecnico.pt", Person.PersonType.TEACHER);

    @Test
    void submitProposal_Valid() {
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        workflow.submitProposal(List.of(teacher1));
        
        assertEquals(ThesisState.PROPOSAL_SUBMITTED, workflow.getState());
        assertEquals(1, workflow.getJury().size());
    }

    @Test
    void submitProposal_InvalidJurySize() {
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        assertThrows(DEIException.class, () -> workflow.submitProposal(List.of()));
        assertThrows(DEIException.class, () -> workflow.submitProposal(List.of(teacher1, teacher2, teacher1, teacher2, teacher1, teacher2)));
        assertThrows(DEIException.class, () -> workflow.submitProposal(List.of(teacher1, teacher1, teacher1)));
        assertThrows(DEIException.class, () -> workflow.submitProposal(List.of(student)));
    }

    @Test
    void approveProposal_InvalidState() {
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        assertThrows(DEIException.class, workflow::approveProposal);
    }

    @Test
    void assignPresident_NotInJury() {
        ThesisWorkflow workflow = createApprovedWorkflow();
        Person invalidPresident = new Person("Prof. Z", "ist000", "z@tecnico.pt", Person.PersonType.TEACHER);
        assertThrows(DEIException.class, () -> workflow.assignPresident(invalidPresident));
    }

    @Test
    void setState_WithAdvancedDefense_ThrowsException() {
        ThesisWorkflow thesis = new ThesisWorkflow();
        thesis.setState(ThesisState.SUBMITTED_FENIX); 

        DefenseWorkflow defense = new DefenseWorkflow(thesis);
        defense.setState(DefenseWorkflow.DefenseState.UNDER_REVIEW);

        assertThrows(DEIException.class, () -> thesis.setState(ThesisState.APPROVED_SC));
    }

    @Test
    void setState_WithScheduledDefense_Allowed() {
        ThesisWorkflow thesis = new ThesisWorkflow();
        thesis.setState(ThesisState.SUBMITTED_FENIX);

        DefenseWorkflow defense = new DefenseWorkflow(thesis);
        defense.setState(DefenseWorkflow.DefenseState.SCHEDULED_DEFENSE); 

        assertDoesNotThrow(() -> thesis.setState(ThesisState.APPROVED_SC));
    }

    private ThesisWorkflow createApprovedWorkflow() {
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        workflow.submitProposal(List.of(teacher1));
        workflow.approveProposal();
        return workflow;
    }

    private ThesisWorkflow createPresidentAssignedWorkflow() {
        ThesisWorkflow workflow = createApprovedWorkflow();
        workflow.assignPresident(teacher1);
        return workflow;
    }
}