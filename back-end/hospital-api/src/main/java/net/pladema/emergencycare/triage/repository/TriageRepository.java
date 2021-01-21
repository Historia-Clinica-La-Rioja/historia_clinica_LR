package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.domain.TriageVo;
import net.pladema.emergencycare.triage.repository.entity.Triage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriageRepository extends JpaRepository<Triage, Integer> {

    @Query(" SELECT NEW net.pladema.emergencycare.triage.repository.domain.TriageVo(t, td, ece.emergencyCareTypeId, ece.doctorsOfficeId) " +
            " FROM Triage t " +
            " JOIN EmergencyCareEpisode ece ON (t.emergencyCareEpisodeId = ece.id) " +
            " LEFT JOIN TriageDetails td ON (t.id = td.triageId) " +
            " WHERE t.emergencyCareEpisodeId = :episodeId ")
    List<TriageVo> getAllByEpisodeId(@Param("episodeId") Integer episodeId);
}