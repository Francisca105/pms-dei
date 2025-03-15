package pt.ulisboa.tecnico.rnl.dei.dms.thesis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.defense.service.DefenseWorkflowService;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.person.dto.PersonDto;
import pt.ulisboa.tecnico.rnl.dei.dms.person.service.PersonService;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisDocument;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisDocumentDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.dto.ThesisWorkflowDto;
import pt.ulisboa.tecnico.rnl.dei.dms.thesis.repository.ThesisWorkflowRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThesisWorkflowService {
    private final ThesisWorkflowRepository thesisWorkflowRepository;
    private final PersonService personService;
    private final DefenseWorkflowService defenseWorkflowService;

    @Autowired
    public ThesisWorkflowService(ThesisWorkflowRepository thesisWorkflowRepository,
                                 PersonService personService,
                                 @Lazy DefenseWorkflowService defenseWorkflowService) {
        this.thesisWorkflowRepository = thesisWorkflowRepository;
        this.personService = personService;
        this.defenseWorkflowService = defenseWorkflowService;
    }

    public List<ThesisWorkflowDto> getAllThesisWorkflows() {
        return thesisWorkflowRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ThesisWorkflowDto getThesisWorkflowById(Long id) {
        return convertToDto(thesisWorkflowRepository.findById(id)
                .orElseThrow(() -> new DEIException(ErrorMessage.NO_SUCH_THESIS_WORKFLOW, Long.toString(id))));
    }

    public ThesisWorkflowDto createThesisWorkflow(ThesisWorkflowDto thesisWorkflowDto) {
        Person student = new Person(personService.getPerson(thesisWorkflowDto.getStudent().getId()));
        if (student.getType() != Person.PersonType.STUDENT) {
            throw new DEIException(ErrorMessage.THESIS_SUBMISSION_NOT_STUDENT);
        }

        ThesisWorkflow thesisWorkflow = new ThesisWorkflow(student);
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public void deleteThesisWorkflow(Long id) {
        thesisWorkflowRepository.deleteById(id);
    }

    public ThesisWorkflowDto submitProposal(Long id, List<Long> juryIds) {
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        List<Person> jury = juryIds.stream()
            .map(juryId -> personService.getPersonById(juryId))
            .collect(Collectors.toList());
        
        thesisWorkflow.submitProposal(jury);
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public ThesisWorkflowDto approveProposal(Long id) {
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        thesisWorkflow.approveProposal();
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public ThesisWorkflowDto assignPresident(Long id, Long presidentId) {
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        Person president = new Person(personService.getPerson(presidentId));
        thesisWorkflow.assignPresident(president);
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public ThesisWorkflowDto signDocument(Long id, ThesisDocumentDto documentDto) {
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        ThesisDocument document = new ThesisDocument(documentDto.getContent(), documentDto.getName(), thesisWorkflow);
        document.setUploadDate(LocalDate.now());
        thesisWorkflow.signDocument(document);
        return convertToDto(thesisWorkflowRepository.save(thesisWorkflow));
    }

    public ThesisWorkflowDto submitToFenix(Long id) {
        ThesisWorkflow thesisWorkflow = getThesisWorkflowEntity(id);
        thesisWorkflow.submitToFenix();
        ThesisWorkflow saved = thesisWorkflowRepository.save(thesisWorkflow);

        if (saved.getDefenseWorkflow() == null) {
            DefenseWorkflow defenseWorkflow = new DefenseWorkflow(saved);
            defenseWorkflowService.createDefenseWorkflow(defenseWorkflow.getId());
            saved.setDefenseWorkflow(defenseWorkflow);
            saved = thesisWorkflowRepository.save(saved);
        }

        return convertToDto(saved);
    }

    public ThesisWorkflow getThesisWorkflowEntity(Long id) {
        return thesisWorkflowRepository.findById(id)
                .orElseThrow(() -> new DEIException(ErrorMessage.NO_SUCH_THESIS_WORKFLOW, Long.toString(id)));
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
}