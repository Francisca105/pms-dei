package pt.ulisboa.tecnico.rnl.dei.dms.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.DEIException;
import pt.ulisboa.tecnico.rnl.dei.dms.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.rnl.dei.dms.person.domain.Person;
import pt.ulisboa.tecnico.rnl.dei.dms.person.dto.PersonDto;
import pt.ulisboa.tecnico.rnl.dei.dms.person.repository.PersonRepository;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.domain.Logs.LogType;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.dto.LogsDto;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.service.LogsService;

@Service
@Transactional
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
    private LogsService logsService;

	private Person fetchPersonOrThrow(long id) {
		return personRepository.findById(id)
				.orElseThrow(() -> {
					logError("Tried to fetch person with non-existent ID: " + id);
					return new DEIException(ErrorMessage.NO_SUCH_PERSON, Long.toString(id));
				});
	}

	@Transactional
	public List<PersonDto> getPeople() {
		return personRepository.findAll().stream()
				.map(PersonDto::new)
				.toList();
	}

	@Transactional
	public List<PersonDto> getStudents() {
		return personRepository.findAllByType(Person.PersonType.STUDENT).stream()
				.map(PersonDto::new)
				.toList();
	}

	private PersonDto savePersonDto(Long id, PersonDto personDto) {
		Person person = new Person(personDto);
		person.setId(id);
		return new PersonDto(personRepository.save(person));
	}

	
	private void checkPersonDto(PersonDto personDto) {
		if (personDto.getName() == null) {
			logError("Tried to create or update person with null name");
			throw new DEIException(ErrorMessage.PERSON_NAME_NOT_VALID);
		}
		if (personDto.getIstId() == null) {
			logError("Tried to create or update person with null IST ID");
			throw new DEIException(ErrorMessage.PERSON_ISTID_NOT_VALID);
		}
		if (personDto.getEmail() == null || !personDto.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
			logError("Tried to create or update person with invalid email");
			throw new DEIException(ErrorMessage.PERSON_EMAIL_NOT_VALID);
		}
		if (personDto.getType() == null) {
			logError("Tried to create or update person with null type");
			throw new DEIException(ErrorMessage.PERSON_TYPE_NOT_VALID);
		}
	}

	@Transactional
	public PersonDto createPerson(PersonDto personDto) {
		checkPersonDto(personDto); // ensure valid

		if (personRepository.existsById(personDto.getId())) {
			throw new DEIException(ErrorMessage.PERSON_ALREADY_EXISTS, Long.toString(personDto.getId()));
		}

		logsService.createLog(new LogsDto(
                LogType.INFO,
                "Created person with IST ID: " + personDto.getIstId()
        ));

		return savePersonDto(null, personDto);
	}

	@Transactional
	public PersonDto getPerson(long id) {
		return new PersonDto(fetchPersonOrThrow(id));
	}

	@Transactional
	public PersonDto updatePerson(long id, PersonDto personDto) {
		fetchPersonOrThrow(id); // ensure exists
		checkPersonDto(personDto); // ensure valid

		logsService.createLog(new LogsDto(
				LogType.INFO,
				"Updated person with IST ID: " + personDto.getIstId()
		));

		return savePersonDto(id, personDto);
	}

	@Transactional
	public void deletePerson(long id) {
		fetchPersonOrThrow(id); // ensure exists

		logsService.createLog(new LogsDto(
				LogType.INFO,
				"Deleted person with ID: " + id
		));

		personRepository.deleteById(id);
	}

	private void logError(String message) {
		LogsDto log = new LogsDto(LogType.ERROR, message);
		logsService.createLog(log);
	}
}
