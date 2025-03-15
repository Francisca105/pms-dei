package pt.ulisboa.tecnico.rnl.dei.dms.thesis.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.thesis.domain.ThesisWorkflow;

@Repository
@Transactional
public interface ThesisWorkflowRepository extends JpaRepository<ThesisWorkflow, Long> {
    
    @Query("SELECT t.state, COUNT(t) FROM ThesisWorkflow t GROUP BY t.state")
    List<Object[]> getStatistics();

}
