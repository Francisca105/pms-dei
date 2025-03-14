package pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain;
import jakarta.persistence.*;

@Entity
@Table(name = "thesis_signed_documents")
public class ThesisDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] content;

    private String name;

    @OneToOne
    @JoinColumn(name = "thesis_workflow_id")
    private ThesisWorkflow thesisWorkflow;

    protected ThesisDocument() {}

    public ThesisDocument(byte[] content, String name, ThesisWorkflow workflow) {
        this.content = content;
        this.name = name;
        this.thesisWorkflow = workflow;
    }

    public byte[] getContent() { 
        return content; 
    }

    public void setContent(byte[] content) { 
        this.content = content; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public ThesisWorkflow getThesisWorkflow() {
        return thesisWorkflow; 
    }

    public void setThesisWorkflow(ThesisWorkflow thesisWorkflow) { 
        this.thesisWorkflow = thesisWorkflow; 
    }   
   
}