package pt.ulisboa.tecnico.rnl.dei.dms.logs.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.logs.domain.Logs;
import pt.ulisboa.tecnico.rnl.dei.dms.logs.domain.Logs.LogType;

@Repository
@Transactional
public interface LogsRepository extends JpaRepository<Logs, Long> {
  
    List<Logs> findByLogType(LogType logType);

    List<Logs> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
