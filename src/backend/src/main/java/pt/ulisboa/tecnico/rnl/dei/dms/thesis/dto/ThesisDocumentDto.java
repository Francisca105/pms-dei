package pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto;

import java.time.LocalDate;

import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisDocument;

public class ThesisDocumentDto {
    private Long id;
    private String name;
    private byte[] content;
    private Long thesisWorkflowId;
    private LocalDate uploadDate;

    public ThesisDocumentDto() {
    }

    public ThesisDocumentDto(Long id, String name, byte[] content, Long thesisWorkflowId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.thesisWorkflowId = thesisWorkflowId;
    }

    public ThesisDocumentDto(ThesisDocument thesisDocument) {
        this.id = thesisDocument.getId();
        this.name = thesisDocument.getName();
        this.content = thesisDocument.getContent();
        this.thesisWorkflowId = thesisDocument.getThesisWorkflow().getId();
        this.uploadDate = thesisDocument.getUploadDate();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public byte[] getContent() { return content; }
    public void setContent(byte[] content) { this.content = content; }
    public Long getThesisWorkflowId() { return thesisWorkflowId; }
    public void setThesisWorkflowId(Long thesisWorkflowId) { this.thesisWorkflowId = thesisWorkflowId; }
    public LocalDate getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDate uploadDate) { this.uploadDate = uploadDate; }
}