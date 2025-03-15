package pt.ulisboa.tecnico.rnl.dei.dms.defense.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.repository.DefenseWorkflowRepository;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.service.ThesisWorkflowService;

import java.time.LocalDate;
import java.util.Optional;

class DefenseWorkflowServiceTest {

    @Mock
    private ThesisWorkflowService thesisWorkflowService;

    @Mock
    private DefenseWorkflowRepository defenseWorkflowRepository;

    @Mock
    private ThesisWorkflow thesisWorkflow;

    @InjectMocks
    private DefenseWorkflowService defenseWorkflowService;

    private DefenseWorkflow defenseWorkflow;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(thesisWorkflow.getState()).thenReturn(ThesisWorkflow.ThesisState.SUBMITTED_FENIX);

        defenseWorkflow = new DefenseWorkflow(thesisWorkflow);
        defenseWorkflow.setId(1L);
        defenseWorkflow.setState(DefenseWorkflow.DefenseState.NOT_SCHEDULED);
        defenseWorkflow.setScheduledDate(LocalDate.now().plusDays(10));

        when(defenseWorkflowRepository.findById(1L)).thenReturn(Optional.of(defenseWorkflow));
        when(defenseWorkflowRepository.save(any(DefenseWorkflow.class))).thenReturn(defenseWorkflow);
    }

    @Test
    void scheduleDefense_ValidDate_ChangesState() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        defenseWorkflowService.scheduleDefense(defenseWorkflow.getId(), futureDate);

        assertEquals(DefenseWorkflow.DefenseState.SCHEDULED_DEFENSE, defenseWorkflow.getState());
        assertEquals(futureDate, defenseWorkflow.getScheduledDate());
    }

    @Test
    void markUnderReview_ValidState_ChangesState() {
        defenseWorkflow.setState(DefenseWorkflow.DefenseState.SCHEDULED_DEFENSE);
        defenseWorkflow.setScheduledDate(LocalDate.now());

        defenseWorkflowService.markUnderReview(defenseWorkflow.getId());

        assertEquals(DefenseWorkflow.DefenseState.UNDER_REVIEW, defenseWorkflow.getState());
    }

    @Test
    void submitGrade_ValidGrade_UpdatesState() {
        defenseWorkflow.setState(DefenseWorkflow.DefenseState.UNDER_REVIEW);

        defenseWorkflowService.submitGrade(defenseWorkflow.getId(), 18.5);

        assertEquals(DefenseWorkflow.DefenseState.SUBMITTED_FENIX, defenseWorkflow.getState());
        assertEquals(18.5, defenseWorkflow.getGrade());
    }
}