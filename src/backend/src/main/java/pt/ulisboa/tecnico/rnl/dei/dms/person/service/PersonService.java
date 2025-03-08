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
@Service
@Transactional
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	private Person fetchPersonOrThrow(long id) {
		return personRepository.findById(id)
				.orElseThrow(() -> new DEIException(ErrorMessage.NO_SUCH_PERSON, Long.toString(id)));
	}

	@Transactional
	public List<PersonDto> getPeople() {
		return personRepository.findAll().stream()
				.map(PersonDto::new)
				.toList();
	}

	private PersonDto savePersonDto(Long id, PersonDto personDto) {
		Person person = new Person(personDto);
		person.setId(id);
		return new PersonDto(personRepository.save(person));
	}

	@Transactional
	public PersonDto createPerson(PersonDto personDto) {
		if (personDto.getName() == null) {
			throw new DEIException(ErrorMessage.PERSON_NAME_NOT_VALID);
		}
		if (personDto.getIstId() == null) {
			throw new DEIException(ErrorMessage.PERSON_ISTID_NOT_VALID);
		}
		if (personDto.getType() == null) {
			throw new DEIException(ErrorMessage.PERSON_TYPE_NOT_VALID);
		}
		if (personRepository.existsById(personDto.getId())) {
			throw new DEIException(ErrorMessage.PERSON_ALREADY_EXISTS, Long.toString(personDto.getId()));
		}

		return savePersonDto(null, personDto);
	}

	@Transactional
	public PersonDto getPerson(long id) {
		return new PersonDto(fetchPersonOrThrow(id));
	}

	@Transactional
	public PersonDto updatePerson(long id, PersonDto personDto) {
		fetchPersonOrThrow(id); // ensure exists

		return savePersonDto(id, personDto);
	}

	@Transactional
	public void deletePerson(long id) {
		fetchPersonOrThrow(id); // ensure exists

		personRepository.deleteById(id);
	}
}
