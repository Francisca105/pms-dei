package pt.ulisboa.tecnico.rnl.dei.dms.logs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.rnl.dei.dms.logs.dto.LogsDto;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.service.LogsService;

@RestController
public class LogController {
	@Autowired
	private LogsService logsService;

	@GetMapping("/logs")
	public List<LogsDto> getLogs() {
		return logsService.getLogs();
	}
}