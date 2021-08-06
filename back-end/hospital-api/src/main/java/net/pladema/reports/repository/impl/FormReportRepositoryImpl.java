package net.pladema.reports.repository.impl;

import net.pladema.reports.repository.FormReportRepository;
import net.pladema.reports.repository.entity.FormVVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class FormReportRepositoryImpl implements FormReportRepository {

    private final EntityManager entityManager;

    public FormReportRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormVVo> getFormVInfo(Integer appointmentId) {
        String query = "SELECT NEW net.pladema.reports.repository.entity.FormVVo(i.name, pe.firstName, pe.middleNames, "+
                "               pe.lastName, pe.otherLastNames, g.description, pe.birthDate, it.description, "+
                "               pe.identificationNumber, mc.name, pmca.affiliateNumber, ad.street, ad.number) "+
                "       FROM Appointment AS a "+
                "           JOIN AppointmentAssn AS assn ON (a.id = assn.pk.appointmentId) "+
                "           JOIN Diary AS d ON (assn.pk.diaryId = d.id) "+
                "           JOIN DoctorsOffice AS doff ON (d.doctorsOfficeId = doff.id) "+
                "           JOIN Institution AS i ON (doff.institutionId = i.id) "+
                "           LEFT JOIN PatientMedicalCoverageAssn AS pmca ON (a.patientMedicalCoverageId = pmca.id) "+
                "           LEFT JOIN MedicalCoverage AS mc ON (pmca.medicalCoverageId = mc.id) "+
                "           JOIN Patient AS pa ON (a.patientId = pa.id) "+
                "           LEFT JOIN Person AS pe ON (pe.id = pa.personId) "+
                "           LEFT JOIN PersonExtended AS pex ON (pe.id = pex.id) " +
                "           LEFT JOIN Address AS ad ON (pex.addressId = ad.id)" +
                "           LEFT JOIN IdentificationType AS it ON (it.id = pe.identificationTypeId) "+
                "           LEFT JOIN Gender AS g ON (pe.genderId = g.id) "+
                "       WHERE a.id = :appointmentId ";
        return entityManager.createQuery(query)
                .setParameter("appointmentId", appointmentId)
                .setMaxResults(1)
                .getResultList().stream().findFirst();
    }
}
