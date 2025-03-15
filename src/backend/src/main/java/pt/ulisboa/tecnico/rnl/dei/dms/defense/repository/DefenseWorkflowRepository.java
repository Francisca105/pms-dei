package pt.ulisboa.tecnico.rnl.dei.dms.defense.repository;

import java.util.List;

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

    @Query("SELECT d FROM DefenseWorkflow d WHERE d.thesisWorkflow.student.id = :studentId")
    DefenseWorkflow findByStudentId(Long studentId);

    @Query("SELECT d FROM DefenseWorkflow d WHERE d.thesisWorkflow.thesis.id = :thesisId")
    DefenseWorkflow findByThesisId(Long thesisId);
}
