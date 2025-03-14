package pt.ulisboa.tecnico.rnl.dei.dms.defense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.rnl.dei.dms.defense.domain.DefenseWorkflow;

@Repository
@Transactional
public interface DefenseWorkflowRepository extends JpaRepository<DefenseWorkflow, Long> {
    
}
