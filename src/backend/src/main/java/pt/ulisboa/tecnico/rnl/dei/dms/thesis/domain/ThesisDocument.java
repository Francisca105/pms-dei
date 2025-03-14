package pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain;
import java.time.LocalDate;

import jakarta.persistence.*;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisDocumentDto;

@Entity
@Table(name = "thesis_signed_documents")
public class ThesisDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private byte[] content;

    private LocalDate uploadDate;

    @OneToOne
    @JoinColumn(name = "thesis_workflow_id")
    private ThesisWorkflow thesisWorkflow;

    public ThesisDocument() {}

    public ThesisDocument(byte[] content, String name, ThesisWorkflow workflow) {
        this.content = content;
        this.name = name;
        this.thesisWorkflow = workflow;
    }

    public ThesisDocument(ThesisDocumentDto thesisDocumentDto) {
        this.content = thesisDocumentDto.getContent();
        this.name = thesisDocumentDto.getName();
        this.uploadDate = thesisDocumentDto.getUploadDate();
    }

    public void setContent(byte[] content) { 
        if(content == null) {
            throw new DEIException(ErrorMessage.THESIS_DOCUMENT_INVALID);
        }
        this.content = content; 
    }

    public void setUploadDate(LocalDate uploadDate) { 
        if(uploadDate == null) {
            throw new DEIException(ErrorMessage.THESIS_DOCUMENT_INVALID_DATE);
        }
        this.uploadDate = uploadDate; 
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public byte[] getContent() { return content; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ThesisWorkflow getThesisWorkflow() { return thesisWorkflow; }
    public void setThesisWorkflow(ThesisWorkflow thesisWorkflow) { this.thesisWorkflow = thesisWorkflow; }   
    public LocalDate getUploadDate() { return uploadDate; }
}