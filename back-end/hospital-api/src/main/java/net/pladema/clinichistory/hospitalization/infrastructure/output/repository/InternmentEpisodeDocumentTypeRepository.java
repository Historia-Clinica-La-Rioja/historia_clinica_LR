package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;
import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.InternmentEpisodeDocumentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternmentEpisodeDocumentTypeRepository extends JpaRepository<InternmentEpisodeDocumentType, Integer> {
}
