package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.hospitalization.repository.domain.summary.ResponsibleDoctorVo;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.sgx.auditable.entity.Updateable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, DocumentRepositoryCustom {

    @Transactional(readOnly = true)
    @Query(value = "select NEW net.pladema.clinichistory.hospitalization.repository.domain.summary.ResponsibleDoctorVo(" +
            "hp.id, p.firstName, p.lastName, hp.licenseNumber) " +
            "from Document d " +
            "join User u on (d.creationable.createdBy = u.id) " +
            "join HealthcareProfessional hp on (u.personId = hp.personId) " +
            "join Person p on (hp.personId = p.id) " +
            "where d.id = :documentId")
    ResponsibleDoctorVo getUserCreator(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query(value = "select new net.pladema.clinichistory.hospitalization.repository.domain.summary.ResponsibleDoctorVo(" +
            "hp.id, p.firstName, p.lastName, hp.licenseNumber) "+
            "from Document d " +
            "join HealthcareProfessionalGroup hcg on (d.sourceId = hcg.pk.internmentEpisodeId) " +
            "join HealthcareProfessional hp on (hcg.pk.healthcareProfessionalId = hp.id) " +
            "join Person p on (hp.personId = p.id) " +
            "where d.id = :documentId " +
            "and d.sourceTypeId = " + SourceType.INTERNACION+" "+
            "and hcg.responsible = true ")
    ResponsibleDoctorVo getResponsible(@Param("documentId") Long documentId);

    @Query(value = "SELECT d.updateable " +
            "FROM Document d " +
            "WHERE d.sourceId = :internmentEpisodeId " +
            "and d.sourceTypeId = " + SourceType.INTERNACION)
    List<Updateable> getUpdatablesDocuments(@Param("internmentEpisodeId") Integer internmentEpisodeId);
}
