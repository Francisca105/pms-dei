package pt.ulisboa.tecnico.rnl.dei.dms.thesis;

import org.springframework.web.bind.annotation.*;

import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisDocumentDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisWorkflowDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.service.ThesisWorkflowService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/thesis")
public class ThesisWorkflowController {
    private final ThesisWorkflowService thesisWorkflowService;

    public ThesisWorkflowController(ThesisWorkflowService thesisWorkflowService) {
        this.thesisWorkflowService = thesisWorkflowService;
    }

    @GetMapping
    public List<ThesisWorkflowDto> getAllThesisWorkflows() {
        return thesisWorkflowService.getAllThesisWorkflows();
    }

    @GetMapping("/{id}")
    public ThesisWorkflowDto getThesisWorkflowById(@PathVariable Long id) {
        return thesisWorkflowService.getThesisWorkflowById(id);
    }

    @GetMapping("/student/{studentId}")
    public ThesisWorkflowDto getThesisWorkflowByStudentId(@PathVariable Long studentId) {
        return thesisWorkflowService.getThesisWorkflowByStudentId(studentId);
    }

    @PostMapping
    public ThesisWorkflowDto createThesisWorkflow(@RequestBody Long studentId) {
        return thesisWorkflowService.createThesisWorkflow(studentId);
    }

    @DeleteMapping("/{id}")
    public void deleteThesisWorkflow(@PathVariable Long id) {
        thesisWorkflowService.deleteThesisWorkflow(id);
    }

    @PostMapping("/{id}/submit-proposal")
    public ThesisWorkflowDto submitProposal(@PathVariable Long id, @RequestBody List<Long> juryIds) {
        return thesisWorkflowService.submitProposal(id, juryIds);
    }

    @PostMapping("/{id}/approve-proposal")
    public ThesisWorkflowDto approveProposal(@PathVariable Long id) {
        return thesisWorkflowService.approveProposal(id);
    }

    @PostMapping("/{id}/assign-president/{presidentId}")
    public ThesisWorkflowDto assignPresident(@PathVariable Long id, @PathVariable Long presidentId) {
        return thesisWorkflowService.assignPresident(id, presidentId);
    }

    @PostMapping("/{id}/sign-document")
    public ThesisWorkflowDto signDocument(@PathVariable Long id, @RequestBody ThesisDocumentDto documentDto) {
        return thesisWorkflowService.signDocument(id, documentDto);
    }

    @PostMapping("/{id}/submit-fenix")
    public ThesisWorkflowDto submitToFenix(@PathVariable Long id) {
        return thesisWorkflowService.submitToFenix(id);
    }

    @GetMapping("/proposal")
    public List<ThesisWorkflowDto> getProposals() {
        return thesisWorkflowService.getThesisWorkflowByState(ThesisWorkflow.ThesisState.PROPOSAL_SUBMITTED);
    }

    @GetMapping("/approved")
    public List<ThesisWorkflowDto> getApproved() {
        return thesisWorkflowService.getThesisWorkflowByState(ThesisWorkflow.ThesisState.APPROVED_SC);
    }

    @GetMapping("/not-started")
    public List<ThesisWorkflowDto> getNotStarted() {
        return thesisWorkflowService.getThesisWorkflowByState(ThesisWorkflow.ThesisState.NOT_STARTED);
    }

    @GetMapping("/president-assigned")
    public List<ThesisWorkflowDto> getPresidentAssigned() {
        return thesisWorkflowService.getThesisWorkflowByState(ThesisWorkflow.ThesisState.PRESIDENT_ASSIGNED);
    }

    @GetMapping("/document-signed")
    public List<ThesisWorkflowDto> getDocumentSigned() {
        return thesisWorkflowService.getThesisWorkflowByState(ThesisWorkflow.ThesisState.DOCUMENT_SIGNED);
    }

    @GetMapping("/fenix-submitted")
    public List<ThesisWorkflowDto> getFenixSubmitted() {
        return thesisWorkflowService.getThesisWorkflowByState(ThesisWorkflow.ThesisState.SUBMITTED_FENIX);
    }

    @PostMapping("/{id}/set-state/{state}")
    public ThesisWorkflowDto setState(@PathVariable Long id, @PathVariable ThesisWorkflow.ThesisState state) {
        return thesisWorkflowService.setState(id, state);
    }

    @GetMapping("/statistics")
    public Map<ThesisWorkflow.ThesisState, Long> getStatistics() {
        return thesisWorkflowService.getStatistics();
    }
}