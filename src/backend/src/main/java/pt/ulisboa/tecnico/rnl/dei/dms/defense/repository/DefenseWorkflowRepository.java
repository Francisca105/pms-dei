package pt.ulisboa.tecnico.rnl.dei.dms.defense.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;

@Repository
@Transactional
public interface DefenseWorkflowRepository extends JpaRepository<DefenseWorkflow, Long> {
    
    @Query("SELECT t.state, COUNT(t) FROM DefenseWorkflow t GROUP BY t.state")
    List<Object[]> getStatistics();

}
