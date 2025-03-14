package pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person.PersonType;

class ThesisDocumentTest {

    @Test
    void createDocument_WithValidData_Succeeds() {
        ThesisWorkflow workflow = new ThesisWorkflow();
        workflow.setStudent(new Person("Carol", "ist000", "carol@tecnico.pt", PersonType.STUDENT));
        
        ThesisDocument doc = new ThesisDocument("content".getBytes(), "thesis.pdf", workflow);
        
        assertEquals("thesis.pdf", doc.getName());
        assertArrayEquals("content".getBytes(), doc.getContent());
        assertEquals(workflow, doc.getThesisWorkflow());
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
        
        workflow.setSignedDocument(doc);
        
        assertEquals(doc, workflow.getSignedDocument());
        assertEquals(workflow, doc.getThesisWorkflow());
    }
}