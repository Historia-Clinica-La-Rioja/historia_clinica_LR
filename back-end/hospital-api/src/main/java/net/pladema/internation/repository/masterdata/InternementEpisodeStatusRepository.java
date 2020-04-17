package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.InternmentEpisodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternementEpisodeStatusRepository extends JpaRepository<InternmentEpisodeStatus, Short> {

}
