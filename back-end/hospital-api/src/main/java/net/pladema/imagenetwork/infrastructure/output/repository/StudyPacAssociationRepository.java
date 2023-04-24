package net.pladema.imagenetwork.infrastructure.output.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.infrastructure.output.entity.StudyPacAssociation;
import net.pladema.imagenetwork.infrastructure.output.entity.StudyPacAssociationPK;

public interface StudyPacAssociationRepository extends JpaRepository<StudyPacAssociation, StudyPacAssociationPK> {

	@Transactional(readOnly = true)
	@Query("SELECT ps " +
			"FROM StudyPacAssociation AS spa " +
			"JOIN PacServer AS ps ON (spa.pk.pacServerId = ps.id)" +
			"WHERE spa.pk.studyInstanceUID = :studyInstanceUID")
	List<PacServer> findAllPacServerBy(@Param("studyInstanceUID") String imageId);
}
