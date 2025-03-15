package pt.ulisboa.tecnico.rnl.dei.dms.thesis.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.service.DefenseWorkflowService;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person.PersonType;
import pt.ulisboa.tecnico.rnl.dei.dms.person.dto.PersonDto;
import pt.ulisboa.tecnico.rnl.dei.dms.person.service.PersonService;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisDocumentDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisWorkflowDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.repository.ThesisWorkflowRepository;

public class ThesisWorkflowServiceTest {

    @Mock
    private ThesisWorkflowRepository thesisWorkflowRepository;

    @Mock
    private PersonService personService;

    @Mock
    private DefenseWorkflowService defenseWorkflowService;

    @InjectMocks
    private ThesisWorkflowService thesisWorkflowService;

    private Person student;
    private PersonDto studentDto;
    private Person teacher1;
    private Person teacher2;
    private PersonDto teacher1Dto;
    private PersonDto teacher2Dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        student = new Person("Alice", "ist123", "alice@tecnico.pt", PersonType.STUDENT);
        student.setId(1L);
        
        teacher1 = new Person("Prof. X", "ist456", "x@tecnico.pt", PersonType.TEACHER);
        teacher1.setId(2L);
        
        teacher2 = new Person("Prof. Y", "ist789", "y@tecnico.pt", PersonType.TEACHER);
        teacher2.setId(3L);
        
        studentDto = new PersonDto(student);
        teacher1Dto = new PersonDto(teacher1);
        teacher2Dto = new PersonDto(teacher2);
}

    @Test
    void createThesisWorkflow_ValidStudent_Succeeds() {
        ThesisWorkflowDto inputDto = new ThesisWorkflowDto();
        inputDto.setStudent(studentDto);

        when(personService.getPerson(1L)).thenReturn(studentDto);
        
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        workflow.setId(1L);
        when(thesisWorkflowRepository.save(any(ThesisWorkflow.class))).thenReturn(workflow);

        ThesisWorkflowDto dto = thesisWorkflowService.createThesisWorkflow(inputDto);
        
        assertNotNull(dto.getId());
        assertEquals(PersonType.STUDENT.toString(), dto.getStudent().getType());
    }

    @Test
    void submitProposal_ValidJury_ChangesState() {
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        workflow.setId(1L);
        
        when(thesisWorkflowRepository.findById(1L)).thenReturn(Optional.of(workflow));
        when(personService.getPersonById(2L)).thenReturn(teacher1);
        when(personService.getPersonById(3L)).thenReturn(teacher2);
        when(thesisWorkflowRepository.save(any(ThesisWorkflow.class))).thenReturn(workflow);

        ThesisWorkflowDto dto = thesisWorkflowService.submitProposal(1L, List.of(2L, 3L));
        
        assertEquals(ThesisWorkflow.ThesisState.PROPOSAL_SUBMITTED, dto.getState());
        assertEquals(2, dto.getJury().size());
    }

    @Test
    void submitProposal_InvalidState_ThrowsException() {
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        workflow.setId(1L);
        workflow.setState(ThesisWorkflow.ThesisState.PROPOSAL_SUBMITTED);
        
        when(thesisWorkflowRepository.findById(1L)).thenReturn(Optional.of(workflow));
        when(personService.getPerson(2L)).thenReturn(teacher1Dto);

        assertThrows(DEIException.class, () -> 
            thesisWorkflowService.submitProposal(1L, List.of(2L)));
    }

    @Test
    void approveProposal_ValidState_ChangesState() {
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        workflow.setId(1L);
        workflow.submitProposal(List.of(teacher1));
        
        when(thesisWorkflowRepository.findById(1L)).thenReturn(Optional.of(workflow));
        when(thesisWorkflowRepository.save(any(ThesisWorkflow.class))).thenReturn(workflow);

        ThesisWorkflowDto dto = thesisWorkflowService.approveProposal(1L);
        assertEquals(ThesisWorkflow.ThesisState.APPROVED_SC, dto.getState());
    }

    @Test
    void signDocument_ValidDocument_UpdatesState() {
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        workflow.setId(1L);
        workflow.submitProposal(List.of(teacher1));
        workflow.approveProposal();
        workflow.assignPresident(teacher1);
        
        when(thesisWorkflowRepository.findById(1L)).thenReturn(Optional.of(workflow));
        when(thesisWorkflowRepository.save(any(ThesisWorkflow.class))).thenReturn(workflow);

        ThesisDocumentDto docDto = new ThesisDocumentDto();
        docDto.setContent("content".getBytes());
        docDto.setName("doc.pdf");
        
        ThesisWorkflowDto dto = thesisWorkflowService.signDocument(1L, docDto);
        assertEquals(ThesisWorkflow.ThesisState.DOCUMENT_SIGNED, dto.getState());
    }
}