package pt.ulisboa.tecnico.rnl.dei.dms.logs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.logs.repository.LogsRepository;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.domain.Logs;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.dto.LogsDto;

@Service
@Transactional
public class LogsService {

	@Autowired
	private LogsRepository logsRepository;

	@Transactional
	public List<LogsDto> getLogs() {
		return logsRepository.findAll().stream()
				.map(LogsDto::new)
				.toList();
	}

	private LogsDto saveLogsDto(LogsDto LogsDto) {
		Logs logs = new Logs(LogsDto);
		logs = logsRepository.save(logs);
		return new LogsDto(logs);
	}

	@Transactional
	public LogsDto createLog(LogsDto LogsDto) {
		return saveLogsDto(LogsDto);
	}
}
