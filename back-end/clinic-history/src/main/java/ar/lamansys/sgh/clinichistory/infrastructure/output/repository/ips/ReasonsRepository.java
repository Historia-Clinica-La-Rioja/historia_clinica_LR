package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReasonsRepository extends JpaRepository<Reason, String> {

	@Query(value = "SELECT r.id, r.description " +
			"FROM reasons r " +
			"JOIN emergency_care_evolution_note_reason ecenr on ecenr.reason_id = r.id " +
			"JOIN emergency_care_evolution_note ecen on ecen.id = ecenr.emergency_care_evolution_note_id " +
			"WHERE ecenr.emergency_care_evolution_note_id =:emergencyCareEvolutionNoteId", nativeQuery = true)
	List<Reason> findAllByEmergencyCareEvolutionNoteId(@Param("emergencyCareEvolutionNoteId") Integer emergencyCareEvolutionNoteId);
}
