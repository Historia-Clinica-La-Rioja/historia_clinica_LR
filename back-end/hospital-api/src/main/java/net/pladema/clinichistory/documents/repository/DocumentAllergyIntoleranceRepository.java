package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.DocumentAllergyIntolerance;
import net.pladema.clinichistory.documents.repository.entity.DocumentAllergyIntolerancePK;
import net.pladema.clinichistory.documents.repository.generalstate.domain.AllergyConditionVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.AllergyIntoleranceVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentAllergyIntoleranceRepository extends JpaRepository<DocumentAllergyIntolerance, DocumentAllergyIntolerancePK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.documents.repository.generalstate.domain.AllergyConditionVo(" +
            "ai.id, s, ai.statusId, ai.verificationStatusId, ai.categoryId," +
            "ai.startDate) " +
            "FROM DocumentAllergyIntolerance da " +
            "JOIN AllergyIntolerance ai ON (da.pk.allergyIntoleranceId = ai.id) " +
            "JOIN Snomed s ON (s.id = ai.snomedId) " +
            "WHERE da.pk.documentId = :documentId " +
            "AND ai.verificationStatusId NOT IN ('"+ AllergyIntoleranceVerificationStatus.ERROR+"')")
    List<AllergyConditionVo> getAllergyIntoleranceStateFromDocument(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.documents.repository.generalstate.domain.AllergyConditionVo(" +
            "ai.id, s, ai.statusId, aics.description as status, " +
            "ai.verificationStatusId, aivs.description as verification, " +
            "ai.categoryId, ai.startDate) " +
            "FROM DocumentAllergyIntolerance da " +
            "JOIN AllergyIntolerance ai ON (da.pk.allergyIntoleranceId = ai.id) " +
            "JOIN Snomed s ON (s.id = ai.snomedId) " +
            "JOIN AllergyIntoleranceClinicalStatus aics ON (aics.id = ai.statusId) " +
            "JOIN AllergyIntoleranceVerificationStatus aivs ON (aivs.id = ai.verificationStatusId) " +
            "WHERE da.pk.documentId = :documentId ")
    List<AllergyConditionVo> getAllergyIntoleranceStateFromDocumentToReport(@Param("documentId") Long documentId);
}
