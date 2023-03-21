package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.EpisodeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SavedEpisodeDocumentRepository extends JpaRepository<EpisodeDocument, Integer> {
}
