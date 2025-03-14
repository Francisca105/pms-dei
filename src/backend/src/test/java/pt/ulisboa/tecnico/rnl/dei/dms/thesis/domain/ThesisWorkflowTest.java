package pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person.PersonType;

class ThesisWorkflowTest {

    @Test
    void createWorkflow_InitialStateCorrect() {
        Person student = new Person("Alice", "ist123", "alice@tecnico.pt", PersonType.STUDENT);
        ThesisWorkflow workflow = new ThesisWorkflow();
        workflow.setStudent(student);
        workflow.setState(ThesisWorkflow.ThesisState.PRESIDENT_ASSIGNED);

        assertEquals(ThesisWorkflow.ThesisState.PRESIDENT_ASSIGNED, workflow.getState());
        assertNull(workflow.getSignedDocument());
    }

    @Test
    void setSignedDocument_UpdatesStateAndAssociation() {
        ThesisWorkflow workflow = createWorkflowWithPresident();
        byte[] content = "PDF content".getBytes();
        
        workflow.setSignedDocument(content, "document.pdf");
        
        assertEquals(ThesisWorkflow.ThesisState.DOCUMENT_SIGNED, workflow.getState());
        assertNotNull(workflow.getSignedDocument());
        assertEquals("document.pdf", workflow.getSignedDocument().getName());
        assertEquals(workflow, workflow.getSignedDocument().getThesisWorkflow());
    }

    @Test
    void deleteSignedDocument_RevertsStateAndRemovesAssociation() {
        ThesisWorkflow workflow = createWorkflowWithDocument();
        
        workflow.deleteSignedDocument();
        
        assertEquals(ThesisWorkflow.ThesisState.PRESIDENT_ASSIGNED, workflow.getState());
        assertNull(workflow.getSignedDocument());
    }

    private ThesisWorkflow createWorkflowWithPresident() {
        Person student = new Person("Bob", "ist456", "bob@tecnico.pt", PersonType.STUDENT);
        Person president = new Person("Prof. X", "ist789", "x@tecnico.pt", PersonType.TEACHER);
        
        ThesisWorkflow workflow = new ThesisWorkflow();
        workflow.setStudent(student);
        workflow.setPresident(president);
        workflow.setState(ThesisWorkflow.ThesisState.PRESIDENT_ASSIGNED);
        return workflow;
    }

    private ThesisWorkflow createWorkflowWithDocument() {
        ThesisWorkflow workflow = createWorkflowWithPresident();
        workflow.setSignedDocument("signed".getBytes(), "doc1.pdf");
        return workflow;
    }
}