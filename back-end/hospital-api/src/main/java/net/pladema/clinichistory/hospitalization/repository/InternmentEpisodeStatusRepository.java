package net.pladema.clinichistory.hospitalization.repository;

import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternmentEpisodeStatusRepository extends JpaRepository<InternmentEpisodeStatus, Short> {

}
