package net.pladema.imagenetwork.infrastructure.output.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.infrastructure.output.database.entity.StudyPacAssociation;
import net.pladema.imagenetwork.infrastructure.output.database.entity.StudyPacAssociationPK;

@Repository
public interface StudyPacAssociationRepository extends JpaRepository<StudyPacAssociation, StudyPacAssociationPK> {

	@Transactional(readOnly = true)
	@Query("SELECT ps " +
			"FROM StudyPacAssociation AS spa " +
			"JOIN PacServer AS ps ON (spa.pk.pacServerId = ps.id)" +
			"WHERE spa.pk.studyInstanceUID = :studyInstanceUID")
	List<PacServer> findAllPacServerBy(@Param("studyInstanceUID") String imageId);
}
