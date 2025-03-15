package pt.ulisboa.tecnico.rnl.dei.dms.defense.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.dto.DefenseWorkflowDTO;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person.PersonType;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;

import java.time.LocalDate;

class DefenseWorkflowTest {
    Person student = new Person("Zé", "ist000", "ze@tecnico.pt", PersonType.STUDENT);
    Person teacher1 = new Person("Prof. Z", "ist100", "prof.z@tecnico.pt", PersonType.TEACHER);
    Person teacher2 = new Person("Prof. X", "ist200", "prof.x@tecnico.pt", PersonType.TEACHER);

    @Test
    void createWorkflow_InitialStateIsNotScheduled() {
        DefenseWorkflow workflow = new DefenseWorkflow();
        assertEquals(DefenseWorkflow.DefenseState.NOT_SCHEDULED, workflow.getState());
        assertNull(workflow.getScheduledDate());
    }

    @Test
    void scheduleDefense_ValidTransition() {
        DefenseWorkflow workflow = new DefenseWorkflow();
        LocalDate futureDate = LocalDate.now().plusDays(10);
        
        workflow.scheduleDefense(futureDate);
        
        assertEquals(DefenseWorkflow.DefenseState.SCHEDULED_DEFENSE, workflow.getState());
        assertEquals(futureDate, workflow.getScheduledDate());
    }

    @Test
    void scheduleDefense_PastDate_ThrowsException() {
        DefenseWorkflow workflow = new DefenseWorkflow();
        LocalDate pastDate = LocalDate.now().minusDays(1);
        
        assertThrows(DEIException.class, () -> workflow.scheduleDefense(pastDate));
    }

    @Test
    void markUnderReview_ValidTransition() {
        DefenseWorkflow workflow = createScheduledWorkflow(LocalDate.now());
        workflow.markUnderReview();
        assertEquals(DefenseWorkflow.DefenseState.UNDER_REVIEW, workflow.getState());
    }

    @Test
    void markUnderReview_BeforeScheduledDate_ThrowsException() {
        DefenseWorkflow workflow = createScheduledWorkflow(LocalDate.now().plusDays(1));
        
        assertThrows(DEIException.class, workflow::markUnderReview);
    }

    @Test
    void submitGrade_ValidTransition() {
        DefenseWorkflow workflow = createUnderReviewWorkflow();
        workflow.submitGrade(18.5);
        
        assertEquals(DefenseWorkflow.DefenseState.SUBMITTED_FENIX, workflow.getState());
        assertEquals(18.5, workflow.getGrade());
    }

    @Test
    void submitGrade_InvalidGrade_ThrowsException() {
        DefenseWorkflow workflow = createUnderReviewWorkflow();
        
        assertThrows(DEIException.class, () -> workflow.submitGrade(-5.0));
        assertThrows(DEIException.class, () -> workflow.submitGrade(25.0));
    }

    @Test
    void revertState_FromUnderReviewToScheduled() {
        DefenseWorkflow workflow = createUnderReviewWorkflow();
        workflow.setState(DefenseWorkflow.DefenseState.SCHEDULED_DEFENSE);
        
        assertEquals(DefenseWorkflow.DefenseState.SCHEDULED_DEFENSE, workflow.getState());
        assertNull(workflow.getGrade());
    }

    @Test
    void createDTOFromEntity_AllFieldsMappedCorrectly() {
        ThesisWorkflow thesis = createThesisWorkflow();
        DefenseWorkflow entity = new DefenseWorkflow(thesis);
        entity.setId(1L);
        entity.scheduleDefense(LocalDate.now());
        entity.markUnderReview();
        entity.submitGrade(17.0);

        DefenseWorkflowDTO dto = new DefenseWorkflowDTO(entity);
        
        assertEquals(1L, dto.getId());
        assertEquals(thesis.getId(), dto.getThesisWorkflowId());
        assertEquals(DefenseWorkflow.DefenseState.SUBMITTED_FENIX, dto.getState());
        assertEquals(LocalDate.now(), dto.getScheduledDate());
        assertEquals(17.0, dto.getGrade());
    }

    @Test
    void setThesisWorkflow_ValidState() {
        ThesisWorkflow thesisWorkflow = new ThesisWorkflow();
        thesisWorkflow.setState(ThesisWorkflow.ThesisState.SUBMITTED_FENIX);

        DefenseWorkflow defenseWorkflow = new DefenseWorkflow();
        defenseWorkflow.setThesisWorkflow(thesisWorkflow);

        assertEquals(thesisWorkflow, defenseWorkflow.getThesisWorkflow());
    }

    @Test
    void setThesisWorkflow_InvalidState() {
        ThesisWorkflow thesisWorkflow = new ThesisWorkflow();
        thesisWorkflow.setState(ThesisWorkflow.ThesisState.DOCUMENT_SIGNED);

        DefenseWorkflow defenseWorkflow = new DefenseWorkflow();
        assertThrows(DEIException.class, () -> defenseWorkflow.setThesisWorkflow(thesisWorkflow));
    }

    @Test
    void setThesisWorkflow_NullThesisWorkflow() {
        DefenseWorkflow defenseWorkflow = new DefenseWorkflow();
        assertThrows(DEIException.class, () -> defenseWorkflow.setThesisWorkflow(null));
    }

    @Test
    void settersAndGetters_UpdateValuesCorrectly() {
        DefenseWorkflowDTO dto = new DefenseWorkflowDTO();
        dto.setId(2L);
        dto.setThesisWorkflowId(5L);
        dto.setState(DefenseWorkflow.DefenseState.UNDER_REVIEW);
        dto.setScheduledDate(LocalDate.now());
        dto.setGrade(15.5);
        
        assertEquals(2L, dto.getId());
        assertEquals(5L, dto.getThesisWorkflowId());
        assertEquals(DefenseWorkflow.DefenseState.UNDER_REVIEW, dto.getState());
        assertEquals(LocalDate.now(), dto.getScheduledDate());
        assertEquals(15.5, dto.getGrade());
    }

    private DefenseWorkflow createScheduledWorkflow(LocalDate date) {
        DefenseWorkflow workflow = new DefenseWorkflow();
        workflow.scheduleDefense(date);
        return workflow;
    }

    private DefenseWorkflow createUnderReviewWorkflow() {
        DefenseWorkflow workflow = createScheduledWorkflow(LocalDate.now());
        workflow.markUnderReview();
        return workflow;
    }

    private ThesisWorkflow createThesisWorkflow() {
        ThesisWorkflow thesis = new ThesisWorkflow(student);
        thesis.setState(ThesisWorkflow.ThesisState.SUBMITTED_FENIX);
        return thesis;
    }
}