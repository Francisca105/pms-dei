package pt.ulisboa.tecnico.rnl.dei.dms.defense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.dto.DefenseWorkflowDTO;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.repository.DefenseWorkflowRepository;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.domain.Logs.LogType;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.dto.LogsDto;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.service.LogsService;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.service.ThesisWorkflowService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefenseWorkflowService {
    private final DefenseWorkflowRepository defenseWorkflowRepository;
    private final ThesisWorkflowService thesisWorkflowService;

    @Autowired
    private LogsService logsService;
    
    @Autowired
    public DefenseWorkflowService(DefenseWorkflowRepository defenseWorkflowRepository,
                                  ThesisWorkflowService thesisWorkflowService) {
        this.defenseWorkflowRepository = defenseWorkflowRepository;
        this.thesisWorkflowService = thesisWorkflowService;
    }

    public List<DefenseWorkflowDTO> getAllDefenseWorkflows() {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching all defense workflows"));
        return defenseWorkflowRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DefenseWorkflowDTO getDefenseWorkflowById(Long id) {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching defense workflow by ID: " + id));
        return convertToDto(defenseWorkflowRepository.findById(id)
                .orElseThrow(() -> {
                    logError("Tried to fetch defense workflow with non-existent ID: " + id);
                    return new DEIException(ErrorMessage.NO_SUCH_DEFENSE_WORKFLOW, Long.toString(id));
                }));
    }

    public DefenseWorkflowDTO getDefenseWorkflowByStudentId(Long studentId) {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching defense workflow by student ID: " + studentId));
        DefenseWorkflow defenseWorkflow = defenseWorkflowRepository.findByStudentId(studentId);
        if (defenseWorkflow == null) {
            logError("Tried to fetch defense workflow with non-existent student ID: " + studentId);
            return null;
        }
        return convertToDto(defenseWorkflow);
    }

    public DefenseWorkflowDTO getDefenseWorkflowByThesisWorkflowId(Long thesisWorkflowId) {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching defense workflow by thesis workflow ID: " + thesisWorkflowId));
        DefenseWorkflow defenseWorkflow = defenseWorkflowRepository.findByThesisWorkflowId(thesisWorkflowId);
        if (defenseWorkflow == null) {
            logError("Tried to fetch defense workflow with non-existent thesis workflow ID: " + thesisWorkflowId);
            return null;
        }
        return convertToDto(defenseWorkflow);
    }

    public void deleteDefenseWorkflow(Long id) {
        logsService.createLog(new LogsDto(LogType.INFO, "Deleting defense workflow with ID: " + id));
        defenseWorkflowRepository.deleteById(id);
    }

    public DefenseWorkflowDTO scheduleDefense(Long id, LocalDate date) {
        logsService.createLog(new LogsDto(LogType.INFO, "Scheduling defense for ID: " + id + " on date: " + date));
        DefenseWorkflow defenseWorkflow = getDefenseWorkflowEntity(id);
        defenseWorkflow.scheduleDefense(date);
        return convertToDto(defenseWorkflowRepository.save(defenseWorkflow));
    }

    public DefenseWorkflowDTO markUnderReview(Long id) {
        logsService.createLog(new LogsDto(LogType.INFO, "Marking defense workflow ID: " + id + " as under review"));
        DefenseWorkflow defenseWorkflow = getDefenseWorkflowEntity(id);
        defenseWorkflow.markUnderReview();
        return convertToDto(defenseWorkflowRepository.save(defenseWorkflow));
    }

    public DefenseWorkflowDTO submitGrade(Long id, Double grade) {
        logsService.createLog(new LogsDto(LogType.INFO, "Submitting grade for defense workflow ID: " + id));
        DefenseWorkflow defenseWorkflow = getDefenseWorkflowEntity(id);
        defenseWorkflow.submitGrade(grade);
        return convertToDto(defenseWorkflowRepository.save(defenseWorkflow));
    }

    public DefenseWorkflowDTO createDefenseWorkflow(Long thesisId) {
        logsService.createLog(new LogsDto(LogType.INFO, "Creating defense workflow for thesis ID: " + thesisId));
        ThesisWorkflow thesisWorkflowEntity = thesisWorkflowService.getThesisWorkflowEntity(thesisId);
        if (thesisWorkflowEntity == null) {
            logError("Tried to create defense workflow for non-existent thesis ID: " + thesisId);
            throw new DEIException(ErrorMessage.DEFENSE_THESIS_NOT_FOUND, Long.toString(thesisId));
        }

        DefenseWorkflow defenseWorkflow = new DefenseWorkflow(thesisWorkflowEntity);
        return convertToDto(defenseWorkflowRepository.save(defenseWorkflow));
    }

    private DefenseWorkflow getDefenseWorkflowEntity(Long id) {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching defense workflow entity by ID: " + id));
        return defenseWorkflowRepository.findById(id)
                .orElseThrow(() -> {
                    logError("Tried to fetch defense workflow entity with non-existent ID: " + id);
                    return new DEIException(ErrorMessage.NO_SUCH_DEFENSE_WORKFLOW, Long.toString(id));
                });
    }

    public DefenseWorkflowDTO setState(Long id, DefenseWorkflow.DefenseState state) {
        logsService.createLog(new LogsDto(LogType.INFO, "Setting state for defense workflow ID: " + id + " to " + state));
        DefenseWorkflow defenseWorkflow = getDefenseWorkflowEntity(id);
        defenseWorkflow.setState(state);
        return convertToDto(defenseWorkflowRepository.save(defenseWorkflow));
    }

    public Map<DefenseWorkflow.DefenseState, Long> getStatistics() {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching defense workflow statistics"));
        DefenseWorkflow.DefenseState[] allStates = DefenseWorkflow.DefenseState.values();

        Map<DefenseWorkflow.DefenseState, Long> statistics = new HashMap<>();
        for (DefenseWorkflow.DefenseState state : allStates) {
            statistics.put(state, 0L); 
        }

        List<Object[]> results = defenseWorkflowRepository.getStatistics();
        for (Object[] result : results) {
            DefenseWorkflow.DefenseState state = (DefenseWorkflow.DefenseState) result[0]; 
            Long count = (Long) result[1]; 
            statistics.put(state, count); 
        }

        return statistics;
    }

    private DefenseWorkflowDTO convertToDto(DefenseWorkflow defenseWorkflow) {
        DefenseWorkflowDTO dto = new DefenseWorkflowDTO();
        dto.setId(defenseWorkflow.getId());
        dto.setThesisWorkflowId(defenseWorkflow.getThesisWorkflow().getId());
        dto.setState(defenseWorkflow.getState());
        dto.setScheduledDate(defenseWorkflow.getScheduledDate());
        dto.setGrade(defenseWorkflow.getGrade());
        return dto;
    }

    private void logError(String message) {
        LogsDto log = new LogsDto(LogType.ERROR, message);
        logsService.createLog(log);
    }
}