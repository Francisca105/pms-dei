package pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person.PersonType;

class ThesisDocumentTest {
    Person student = new Person("Zé", "ist000", "ze@tecnico.pt", PersonType.STUDENT);
    Person teacher1 = new Person("Prof. Z", "ist100", "prof.z@tecnico.pt", PersonType.TEACHER);
    Person teacher2 = new Person("Prof. X", "ist200", "prof.x@tecnico.pt", PersonType.TEACHER);

    @Test
    void createDocument_WithValidData_Succeeds() {
        ThesisWorkflow workflow = createWorkflow();
        
        ThesisDocument doc = new ThesisDocument("content".getBytes(), "thesis.pdf", workflow);
        doc.setUploadDate(LocalDate.now());
        
        assertEquals("thesis.pdf", doc.getName());
        assertArrayEquals("content".getBytes(), doc.getContent());
        assertEquals(workflow, doc.getThesisWorkflow());
        assertNotNull(doc.getUploadDate());
    }

    @Test
    void updateDocument_ChangesContentAndName() {
        ThesisDocument doc = new ThesisDocument("old".getBytes(), "old.pdf", null);
        doc.setContent("new".getBytes());
        doc.setName("new.pdf");
        
        assertArrayEquals("new".getBytes(), doc.getContent());
        assertEquals("new.pdf", doc.getName());
    }

    @Test
    void linkDocumentToWorkflow_BidirectionalAssociation() {
        ThesisWorkflow workflow = new ThesisWorkflow();
        ThesisDocument doc = new ThesisDocument("data".getBytes(), "doc.pdf", workflow);
        doc.setUploadDate(LocalDate.now());

        workflow.setSignedDocument(doc);
        
        assertEquals(doc, workflow.getSignedDocument());
        assertEquals(workflow, doc.getThesisWorkflow());
    }

    private ThesisWorkflow createWorkflow() {
        ThesisWorkflow workflow = new ThesisWorkflow(student);
        workflow.submitProposal(List.of(teacher1, teacher2));
        workflow.approveProposal();
        workflow.assignPresident(teacher2);
        return workflow;
    }
}