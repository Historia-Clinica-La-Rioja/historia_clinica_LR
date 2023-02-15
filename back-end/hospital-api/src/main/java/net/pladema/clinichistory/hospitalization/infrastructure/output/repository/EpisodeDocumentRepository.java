package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.VEpisodeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeDocumentRepository extends JpaRepository<VEpisodeDocument, Integer> {

	List<VEpisodeDocument> findAllByInternmentEpisodeId(Integer internmentEpisodeId);

}
