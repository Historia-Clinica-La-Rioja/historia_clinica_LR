package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.domain.InternmentSummary;
import net.pladema.internation.repository.core.entity.InternmentEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface InternmentEpisodeRepository extends JpaRepository<InternmentEpisode, Integer> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.internation.repository.core.domain.InternmentSummary(" +
            "ie.id,  ie.creationable.createdOn, " +
            "b.id as bedId, b.bedNumber, " +
            "r.id as roomId, r.roomNumber, " +
            "cs.id as clinicalSpecialtyId, cs.name as specialty, " +
            "hpg.pk.healthcareProfessionalId)" +
            "FROM InternmentEpisode ie " +
            "JOIN Bed b ON (b.id = ie.bedId) " +
            "JOIN Room r ON (r.id = b.roomId) " +
            "JOIN ClinicalSpecialty cs ON (cs.id = ie.clinicalSpecialtyId) " +
            "JOIN HealthcareProfessionalGroup hpg ON (hpg.pk.internmentEpisodeId = ie.id and hpg.responsible = true) " +
            "WHERE ie.id = :internmentEpisodeId")
    Optional<InternmentSummary> getSummary(@Param("internmentEpisodeId") Integer internmentEpisodeId);


    @Transactional(readOnly = true)
    @Query("SELECT ie.patientId " +
            "FROM InternmentEpisode ie " +
            "WHERE ie.id = :internmentEpisodeId")
    Optional<Integer> getPatient(@Param("internmentEpisodeId") Integer internmentEpisodeId);
}
