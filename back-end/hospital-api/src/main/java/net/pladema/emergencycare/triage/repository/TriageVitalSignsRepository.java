package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.TriageVitalSigns;
import net.pladema.emergencycare.triage.repository.entity.TriageVitalSignsPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriageVitalSignsRepository extends JpaRepository<TriageVitalSigns, TriageVitalSignsPk> {

    @Query(" SELECT tvs.pk.observationVitalSignId " +
            " FROM TriageVitalSigns tvs " +
            " WHERE tvs.pk.triageId = :triageId ")
    List<Integer> getVitalSignIds(@Param("triageId") Integer triageId);

}
