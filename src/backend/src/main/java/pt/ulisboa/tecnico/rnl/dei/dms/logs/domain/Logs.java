package pt.ulisboa.tecnico.rnl.dei.dms.logs.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.dto.LogsDto;

@Entity
@Table(name = "logs")
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LogType logType;

    private LocalDateTime timestamp;
    private String message;

    public enum LogType {
        INFO, WARNING, ERROR
    }

    public Logs() {
    }

    public Logs(LogsDto logsDto) {
        this.logType = logsDto.getLogType();
        this.timestamp = logsDto.getTimestamp();
        this.message = logsDto.getMessage();
    }

    public Long getId() {
        return id;
    }

    public LogType getLogType() {
        return logType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
