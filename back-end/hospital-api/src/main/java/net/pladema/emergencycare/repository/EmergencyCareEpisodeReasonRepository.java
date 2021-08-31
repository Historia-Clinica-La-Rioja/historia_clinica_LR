package net.pladema.emergencycare.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Reason;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisodeReason;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisodeReasonPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmergencyCareEpisodeReasonRepository extends JpaRepository<EmergencyCareEpisodeReason, EmergencyCareEpisodeReasonPK> {

    @Transactional(readOnly = true)
    @Query(value = " SELECT r " +
            " FROM EmergencyCareEpisodeReason ecer "+
            " JOIN Reason r ON ( ecer.pk.reasonId = r.id ) "+
            " WHERE ecer.pk.emergencyCareEpisodeId = :episodeId ")
    List<Reason> findByEpisodeId(@Param("episodeId") Integer episodeId);

    @Transactional
    @Modifying
    @Query(value = " DELETE " +
            " FROM EmergencyCareEpisodeReason ecer"+
            " WHERE ecer.pk.emergencyCareEpisodeId = :episodeId ")
    void deleteByEpisodeId(@Param("episodeId") Integer episodeId);
}
