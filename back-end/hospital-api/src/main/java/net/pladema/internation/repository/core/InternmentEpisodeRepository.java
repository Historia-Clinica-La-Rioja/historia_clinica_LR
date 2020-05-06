package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.domain.InternmentSummaryVo;
import net.pladema.internation.repository.core.entity.InternmentEpisode;
import net.pladema.internation.service.internment.domain.BasicListedPatientBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InternmentEpisodeRepository extends JpaRepository<InternmentEpisode, Integer> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.internation.repository.core.domain.InternmentSummaryVo(" +
            "ie.id,  ie.creationable.createdOn, " +
            "ie.anamnesisDocId, da.statusId as anamnesisStatusId, " +
            "ie.epicrisisDocId, de.statusId as epicrisisStatusId, " +
            "b.id as bedId, b.bedNumber, " +
            "r.id as roomId, r.roomNumber, " +
            "cs.id as clinicalSpecialtyId, cs.name as specialty, " +
            "hpg.pk.healthcareProfessionalId)" +
            "FROM InternmentEpisode ie " +
            "JOIN Bed b ON (b.id = ie.bedId) " +
            "JOIN Room r ON (r.id = b.roomId) " +
            "LEFT JOIN ClinicalSpecialty cs ON (cs.id = ie.clinicalSpecialtyId) " +
            "LEFT JOIN HealthcareProfessionalGroup hpg ON (hpg.pk.internmentEpisodeId = ie.id and hpg.responsible = true) " +
            "LEFT JOIN Document da ON (da.id = ie.anamnesisDocId) " +
            "LEFT JOIN Document de ON (de.id = ie.epicrisisDocId) " +
            "WHERE ie.id = :internmentEpisodeId")
    Optional<InternmentSummaryVo> getSummary(@Param("internmentEpisodeId") Integer internmentEpisodeId);


    @Transactional(readOnly = true)
    @Query("SELECT ie.patientId " +
            "FROM InternmentEpisode ie " +
            "WHERE ie.id = :internmentEpisodeId")
    Optional<Integer> getPatient(@Param("internmentEpisodeId") Integer internmentEpisodeId);

    @Transactional
    @Modifying
    @Query("UPDATE InternmentEpisode AS ie " +
            "SET ie.anamnesisDocId = :anamnesisDocumentId, " +
            "ie.updateable.updatedOn = :today " +
            "WHERE ie.id = :internmentEpisodeId")
    void updateAnamnesisDocumentId(@Param("internmentEpisodeId") Integer internmentEpisodeId,
                                   @Param("anamnesisDocumentId") Long anamnesisDocumentId,
                                   @Param("today") LocalDateTime today);

    @Transactional
    @Modifying
    @Query("UPDATE InternmentEpisode AS ie " +
            "SET ie.epicrisisDocId = :epicrisisDocumentId, " +
            "ie.updateable.updatedOn = :today " +
            "WHERE ie.id = :internmentEpisodeId")
    void updateEpicrisisDocumentId(@Param("internmentEpisodeId") Integer internmentEpisodeId,
                                   @Param("epicrisisDocumentId") Long epicrisisDocumentId,
                                   @Param("today") LocalDateTime today);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.internation.service.internment.domain.BasicListedPatientBo(pa.id, pe.identificationTypeId, " +
            "pe.identificationNumber, pe.firstName, pe.lastName, pe.birthDate, pe.genderId, ie.id ) " +
            " FROM InternmentEpisode as ie " +
            " JOIN Patient as pa ON (ie.patientId = pa.id) " +
            " JOIN Person as pe ON (pa.personId = pe.id) "+
            " WHERE ie.institutionId = :institutionId ")
    List<BasicListedPatientBo> findAllPatientsListedData(@Param("institutionId") Integer institutionId);

}
