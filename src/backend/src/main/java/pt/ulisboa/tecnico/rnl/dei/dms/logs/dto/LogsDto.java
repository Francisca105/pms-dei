package pt.ulisboa.tecnico.rnl.dei.dms.logs.dto;

import pt.ulisboa.tecnico.rnl.dei.dms.logs.domain.Logs;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.domain.Logs.LogType;
import java.time.LocalDateTime;

public class LogsDto {
    private Long id;
    private LogType logType;
    private LocalDateTime timestamp;
    private String message;

    public LogsDto() {
    }

    public LogsDto(Long id, LogType logType, String message) {
        setId(id);
        setLogType(logType);
        setTimestamp(LocalDateTime.now());
        setMessage(message);
    }

    public LogsDto(LogType logType, String message) {
        this(null, logType, message);
    }

    public LogsDto(Logs logs) {
        this(logs.getId(), logs.getLogType(), logs.getMessage());
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
