package net.pladema.emergencycare.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareEvolutionNoteDetailsVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEvolutionNote;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyCareEvolutionNoteRepository extends SGXAuditableEntityJPARepository<EmergencyCareEvolutionNote, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareEvolutionNoteDetailsVo(ecen, d) " +
			"FROM EmergencyCareEvolutionNote ecen " +
			"JOIN Document d ON (d.id = ecen.documentId) " +
			"WHERE d.typeId IN (" +
				DocumentType.EMERGENCY_CARE_EVOLUTION_NOTE + ", " +
				DocumentType.NURSING_EMERGENCY_CARE_EVOLUTION +
			")" +
			"AND d.sourceId = :episodeId " +
			"AND d.deleteable.deleted IS FALSE")
	List<EmergencyCareEvolutionNoteDetailsVo> findAllByEpisodeId(@Param("episodeId") Integer episodeId);

	@Transactional(readOnly = true)
	@Query("SElECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareEvolutionNoteDetailsVo(ecen, d) " +
			"FROM EmergencyCareEvolutionNote ecen " +
			"JOIN Document d ON (d.id = ecen.documentId) " +
			"WHERE ecen.documentId = :documentId")
	Optional<EmergencyCareEvolutionNoteDetailsVo> findByDocumentId(@Param("documentId") Long documentId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE EmergencyCareEvolutionNote ecen " +
			"SET ecen.deleteable.deleted = true, " +
			"ecen.deleteable.deletedOn = CURRENT_TIMESTAMP, " +
			"ecen.deleteable.deletedBy = ?#{ principal.userId } " +
			"WHERE ecen.documentId = :documentId")
	void deleteByDocumentId(@Param("documentId") Long documentId);

}
