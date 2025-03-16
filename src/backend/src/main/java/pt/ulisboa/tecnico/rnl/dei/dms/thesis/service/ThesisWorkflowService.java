package pt.ulisboa.tecnico.rnl.dei.dms.thesis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.dto.DefenseWorkflowDTO;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.service.DefenseWorkflowService;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.domain.Logs.LogType;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.dto.LogsDto;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.service.LogsService;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.person.dto.PersonDto;
import pt.ulisboa.tecnico.rnl.dei.dms.person.service.PersonService;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisDocument;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisDocumentDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisWorkflowDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.repository.ThesisWorkflowRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThesisWorkflowService {
    private final ThesisWorkflowRepository thesisWorkflowRepository;
    private final PersonService personService;
    private final DefenseWorkflowService defenseWorkflowService;

    @Autowired
    private LogsService logsService;

    @Autowired
    public ThesisWorkflowService(ThesisWorkflowRepository thesisWorkflowRepository,
                                 PersonService personService,
                                 @Lazy DefenseWorkflowService defenseWorkflowService) {
        this.thesisWorkflowRepository = thesisWorkflowRepository;
        this.personService = personService;
        this.defenseWorkflowService = defenseWorkflowService;
    }

    public List<ThesisWorkflowDto> getAllThesisWorkflows() {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching all thesis workflows"));
        return thesisWorkflowRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ThesisWorkflowDto getThesisWorkflowById(Long id) {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching thesis workflow by ID: " + id));
        return convertToDto(thesisWorkflowRepository.findById(id)
                .orElseThrow(() -> new DEIException(ErrorMessage.THESIS_NOT_FOUND, Long.toString(id))));
    }

    public ThesisWorkflowDto getThesisWorkflowByStudentId(Long studentId) {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching thesis workflow by student ID: " + studentId));
        ThesisWorkflow thesisWorkflow = thesisWorkflowRepository.findByStudentId(studentId);
        if (thesisWorkflow == null) {
            logError("Tried to fetch thesis workflow with non-existent student ID: " + studentId);
            return null;
        }
        return convertToDto(thesisWorkflow);
    }

    public ThesisWorkflow getThesisWorkflowByStudentIdEntity(Long studentId) {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching thesis workflow entity by student ID: " + studentId));
        ThesisWorkflow thesisWorkflow = thesisWorkflowRepository.findByStudentId(studentId);
        if (thesisWorkflow == null) {
            logError("Tried to fetch thesis workflow entity with non-existent student ID: " + studentId);
            return null;
        }
        return thesisWorkflow;
    }

    public ThesisWorkflowDto createThesisWorkflow(Long studentId) {
        logsService.createLog(new LogsDto(LogType.INFO, "Creating thesis workflow for student ID: " + studentId));
        Person student = personService.getPersonById(studentId);
        if (student.getType() != Person.PersonType.STUDENT) {
            logError("Tried to create thesis workflow for non-student ID: " + studentId);
            throw new DEIException(ErrorMessage.THESIS_SUBMISSION_NOT_STUDENT);
        }

        if(getThesisWorkflowByStudentId(studentId) != null) {
            logError("Tried to create thesis workflow for student ID that already has a thesis: " + studentId);
            throw new DEIException(ErrorMessage.THESIS_USER_ALREADY_HAS_THESIS);
        }

        ThesisWorkflow thesisWorkflow = new ThesisWorkflow(student);
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public void deleteThesisWorkflow(Long id) {
        logsService.createLog(new LogsDto(LogType.INFO, "Deleting thesis workflow with ID: " + id));
        thesisWorkflowRepository.deleteById(id);
    }

    public ThesisWorkflowDto submitProposal(Long id, List<Long> juryIds) {
        logsService.createLog(new LogsDto(LogType.INFO, "Submitting proposal for thesis workflow ID: " + id));
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        List<Person> jury = juryIds.stream()
            .map(juryId -> personService.getPersonById(juryId))
            .collect(Collectors.toList());
        
        thesisWorkflow.submitProposal(jury);
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public ThesisWorkflowDto approveProposal(Long id) {
        logsService.createLog(new LogsDto(LogType.INFO, "Approving proposal for thesis workflow ID: " + id));
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        thesisWorkflow.approveProposal();
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public ThesisWorkflowDto assignPresident(Long id, Long presidentId) {
        logsService.createLog(new LogsDto(LogType.INFO, "Assigning president for thesis workflow ID: " + id));
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        PersonDto president = personService.getPerson(presidentId);
        thesisWorkflow.assignPresident(president);
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public ThesisWorkflowDto signDocument(Long id, MultipartFile file) {
        logsService.createLog(new LogsDto(LogType.INFO, "Signing document for thesis workflow ID: " + id));
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        ThesisDocument document;
        try {
            document = new ThesisDocument(file.getBytes(), file.getOriginalFilename(), thesisWorkflow);
        } catch (IOException e) {
            logError("Error uploading file for thesis workflow ID: " + id + ", error: " + e.toString());
            throw new DEIException(ErrorMessage.FILE_UPLOAD_ERROR, e.toString());
        }
        document.setUploadDate(LocalDate.now());
        thesisWorkflow.signDocument(document);
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public ThesisWorkflowDto submitToFenix(Long id) {
        logsService.createLog(new LogsDto(LogType.INFO, "Submitting to Fenix for thesis workflow ID: " + id));
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        thesisWorkflow.submitToFenix();
        ThesisWorkflow saved = thesisWorkflowRepository.save(thesisWorkflow);

        Person student = saved.getStudent();
        if (student == null) {
            thesisWorkflow.getStudent();
        }

        DefenseWorkflowDTO existingDefenseWorkflow = defenseWorkflowService.getDefenseWorkflowByStudentId(student.getId());
        if (saved.getDefenseWorkflow() == null && existingDefenseWorkflow == null) {
            DefenseWorkflow defenseWorkflow = new DefenseWorkflow(saved);
            saved.setDefenseWorkflow(defenseWorkflow);
            defenseWorkflowService.createDefenseWorkflow(student.getId());
            saved = thesisWorkflowRepository.save(saved);
        }

        return convertToDto(saved);
    }

    public ThesisWorkflow getThesisWorkflowEntity(Long id) {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching thesis workflow entity by ID: " + id));
        return thesisWorkflowRepository.findById(id)
                .orElseThrow(() -> {
                    logError("Tried to fetch thesis workflow entity with non-existent ID: " + id);
                    return new DEIException(ErrorMessage.THESIS_NOT_FOUND, Long.toString(id));
                });
    }

    public List<ThesisWorkflowDto> getThesisWorkflowByState(ThesisWorkflow.ThesisState state) {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching thesis workflows by state: " + state));
        return thesisWorkflowRepository.findByState(state).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ThesisWorkflowDto setState(Long id, ThesisWorkflow.ThesisState state) {
        logsService.createLog(new LogsDto(LogType.INFO, "Setting state for thesis workflow ID: " + id + " to " + state));
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        thesisWorkflow.setState(state);
        thesisWorkflowRepository.save(thesisWorkflow);
        return new ThesisWorkflowDto(thesisWorkflow);
    }

    public Map<ThesisWorkflow.ThesisState, Long> getStatistics() {
        logsService.createLog(new LogsDto(LogType.INFO, "Fetching thesis workflow statistics"));
        ThesisWorkflow.ThesisState[] allStates = ThesisWorkflow.ThesisState.values();

        Map<ThesisWorkflow.ThesisState, Long> statistics = new HashMap<>();
        for (ThesisWorkflow.ThesisState state : allStates) {
            statistics.put(state, 0L); 
        }

        List<Object[]> results = thesisWorkflowRepository.getStatistics();
        for (Object[] result : results) {
            ThesisWorkflow.ThesisState state = (ThesisWorkflow.ThesisState) result[0]; 
            Long count = (Long) result[1]; 
            statistics.put(state, count); 
        }

        return statistics;
    }

    private ThesisWorkflowDto convertToDto(ThesisWorkflow thesisWorkflow) {
        ThesisWorkflowDto dto = new ThesisWorkflowDto();
        dto.setId(thesisWorkflow.getId());
        dto.setStudent(new PersonDto(thesisWorkflow.getStudent()));
        dto.setState(thesisWorkflow.getState());
        dto.setJury(thesisWorkflow.getJury().stream()
                .map(PersonDto::new)
                .collect(Collectors.toList()));
        dto.setPresident(thesisWorkflow.getPresident() != null ? new PersonDto(thesisWorkflow.getPresident()) : null);
        dto.setSignedDocument(thesisWorkflow.getSignedDocument() != null ? 
                new ThesisDocumentDto(thesisWorkflow.getSignedDocument()) : null);
        return dto;
    }

    private void logError(String message) {
        LogsDto log = new LogsDto(LogType.ERROR, message);
        logsService.createLog(log);
    }
}