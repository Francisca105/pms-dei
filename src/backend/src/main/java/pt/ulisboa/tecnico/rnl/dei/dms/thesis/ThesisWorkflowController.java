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

    @PostMapping
    public ThesisWorkflowDto createThesisWorkflow(@RequestBody ThesisWorkflowDto thesisWorkflowDto) {
        return thesisWorkflowService.createThesisWorkflow(thesisWorkflowDto);
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

    @GetMapping("/statistics")
    public Map<ThesisWorkflow.ThesisState, Long> getStatistics() {
        return thesisWorkflowService.getStatistics();
    }
}