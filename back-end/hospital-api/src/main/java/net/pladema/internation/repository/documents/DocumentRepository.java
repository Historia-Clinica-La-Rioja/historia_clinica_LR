package net.pladema.internation.repository.documents;

import net.pladema.internation.repository.internment.domain.summary.ResponsibleDoctorVo;
import net.pladema.internation.repository.documents.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Transactional(readOnly = true)
    @Query(value = "select NEW net.pladema.internation.repository.internment.domain.summary.ResponsibleDoctorVo(" +
            "hp.id, p.firstName, p.lastName, hp.licenseNumber) " +
            "from Document d " +
            "join User u on (d.creationable.createdBy = u.id) " +
            "join HealthcareProfessional hp on (u.personId = hp.personId) " +
            "join Person p on (hp.personId = p.id) " +
            "where d.id = :documentId")
    ResponsibleDoctorVo getUserCreator(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query(value = "select new net.pladema.internation.repository.internment.domain.summary.ResponsibleDoctorVo(" +
            "hp.id, p.firstName, p.lastName, hp.licenseNumber) "+
            "from Document d " +
            "join HealthcareProfessionalGroup hcg on (d.internmentEpisodeId = hcg.pk.internmentEpisodeId) " +
            "join HealthcareProfessional hp on (hcg.pk.healthcareProfessionalId = hp.id) " +
            "join Person p on (hp.personId = p.id) " +
            "where d.id = :documentId " +
            "and hcg.responsible = true ")
    ResponsibleDoctorVo getResponsible(@Param("documentId") Long documentId);

}
