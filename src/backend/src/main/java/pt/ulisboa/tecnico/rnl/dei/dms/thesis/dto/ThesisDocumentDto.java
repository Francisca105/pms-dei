package pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto;

public class ThesisDocumentDto {
    private Long id;
    private String name;
    private byte[] content;
    private Long thesisWorkflowId;

    public ThesisDocumentDto() {
    }

    public ThesisDocumentDto(Long id, String name, byte[] content, Long thesisWorkflowId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.thesisWorkflowId = thesisWorkflowId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Long getThesisWorkflowId() {
        return thesisWorkflowId;
    }

    public void setThesisWorkflowId(Long thesisWorkflowId) {
        this.thesisWorkflowId = thesisWorkflowId;
    }
}