package pt.ulisboa.tecnico.rnl.dei.dms.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;

@Repository
@Transactional
public interface ThesisWorkflowRepository extends JpaRepository<ThesisWorkflow, Long> {
    
}
