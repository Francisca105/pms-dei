package pt.ulisboa.tecnico.rnl.dei.dms.defense;

import org.springframework.web.bind.annotation.*;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.dto.DefenseWorkflowDTO;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.service.DefenseWorkflowService;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/defense")
public class DefenseWorkflowController {
    private final DefenseWorkflowService defenseWorkflowService;

    public DefenseWorkflowController(DefenseWorkflowService defenseWorkflowService) {
        this.defenseWorkflowService = defenseWorkflowService;
    }

    @GetMapping
    public List<DefenseWorkflowDTO> getAllDefenseWorkflows() {
        return defenseWorkflowService.getAllDefenseWorkflows();
    }

    @GetMapping("/{id}")
    public DefenseWorkflowDTO getDefenseWorkflowById(@PathVariable Long id) {
        return defenseWorkflowService.getDefenseWorkflowById(id);
    }

    @PostMapping
    public DefenseWorkflowDTO createDefenseWorkflow(@RequestParam Long thesisWorkflowId) {
        return defenseWorkflowService.createDefenseWorkflow(thesisWorkflowId);
    }

    @DeleteMapping("/{id}")
    public void deleteDefenseWorkflow(@PathVariable Long id) {
        defenseWorkflowService.deleteDefenseWorkflow(id);
    }

    @PostMapping("/{id}/schedule")
    public DefenseWorkflowDTO scheduleDefense(@PathVariable Long id, @RequestBody LocalDate date) {
        return defenseWorkflowService.scheduleDefense(id, date);
    }

    @PostMapping("/{id}/mark-under-review")
    public DefenseWorkflowDTO markUnderReview(@PathVariable Long id) {
        return defenseWorkflowService.markUnderReview(id);
    }

    @PostMapping("/{id}/submit-grade")
    public DefenseWorkflowDTO submitGrade(@PathVariable Long id, @RequestBody Double grade) {
        return defenseWorkflowService.submitGrade(id, grade);
    }

    @GetMapping("/statistics")
    public Map<DefenseWorkflow.DefenseState, Long> getStatistics() {
        return defenseWorkflowService.getStatistics();
    }
}