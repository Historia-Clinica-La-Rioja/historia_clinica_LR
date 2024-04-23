package net.pladema.emergencycare.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareEvolutionNote;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyCareEvolutionNoteRepository extends SGXAuditableEntityJPARepository<EmergencyCareEvolutionNote, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT ecen " +
			"FROM EmergencyCareEvolutionNote ecen " +
			"JOIN Document d ON (d.id = ecen.documentId) " +
			"WHERE d.typeId = " + DocumentType.EMERGENCY_CARE_EVOLUTION_NOTE + " " +
			"AND d.sourceId = :episodeId")
	List<EmergencyCareEvolutionNote> findAllByEpisodeId(@Param("episodeId") Integer episodeId);

	@Transactional(readOnly = true)
	@Query("SElEcT ecen " +
			"FROM EmergencyCareEvolutionNote ecen " +
			"WHERE ecen.documentId = :documentId")
	Optional<EmergencyCareEvolutionNote> findByDocumentId(@Param("documentId") Long documentId);

}
