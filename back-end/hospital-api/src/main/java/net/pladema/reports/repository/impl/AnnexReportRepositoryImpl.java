package net.pladema.reports.repository.impl;

import net.pladema.reports.repository.AnnexReportRepository;
import net.pladema.reports.repository.entity.AnnexIIVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.Optional;

@Repository
public class AnnexReportRepositoryImpl implements AnnexReportRepository {

    private final EntityManager entityManager;

    public AnnexReportRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnnexIIVo> getAppointmentAnnexInfo(Integer appointmentId) {
        String query = "SELECT NEW net.pladema.reports.repository.entity.AnnexIIVo(i.name, pe.firstName, pe.middleNames, " +
                "           pe.lastName, pe.otherLastNames, it.description, pe.identificationNumber, " +
                "           g.description, pe.birthDate, aps.description, a.dateTypeId, mc.name, pmca.affiliateNumber) " +
                "       FROM Appointment AS a " +
                "           JOIN AppointmentAssn AS assn ON (a.id = assn.pk.appointmentId) " +
                "           JOIN Diary AS d ON (assn.pk.diaryId = d.id) " +
                "           JOIN DoctorsOffice AS doff ON (d.doctorsOfficeId = doff.id) " +
                "           JOIN Institution AS i ON (doff.institutionId = i.id) " +
                "           JOIN AppointmentState AS aps ON (a.appointmentStateId = aps.id) " +
                "           LEFT JOIN PatientMedicalCoverageAssn AS pmca ON (a.patientMedicalCoverageId = pmca.id) " +
                "           LEFT JOIN MedicalCoverage AS mc ON (pmca.medicalCoverageId = mc.id) " +
                "           JOIN Patient AS pa ON (a.patientId = pa.id) " +
                "           LEFT JOIN Person AS pe ON (pe.id = pa.personId) " +
                "           LEFT JOIN IdentificationType AS it ON (it.id = pe.identificationTypeId) " +
                "           LEFT JOIN Gender AS g ON (pe.genderId = g.id) " +
                "       WHERE a.id = :appointmentId ";

        return entityManager.createQuery(query)
                .setParameter("appointmentId", appointmentId)
                .setMaxResults(1)
                .getResultList().stream().findFirst();
    }

    @Override
    public Optional<AnnexIIVo> getOutpatientAnnexInfo(Integer outpatientId) {
        String query = "SELECT i.name as institution, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, g.description, " +
                "               pe.birth_date, it.description as idType, pe.identification_number, oc.start_date, pr.proced as hasProcedures, cs.name " +
                "       FROM outpatient_consultation AS oc " +
                "           JOIN Institution AS i ON (oc.institution_id = i.id) " +
                "           JOIN Patient AS pa ON (oc.patient_id = pa.id) " +
                "           LEFT JOIN Person AS pe ON (pe.id = pa.person_id) " +
                "           LEFT JOIN Identification_type AS it ON (it.id = pe.identification_type_id)" +
                "           LEFT JOIN Gender AS g ON (pe.gender_id = g.id) " +
                "           LEFT JOIN clinical_specialty AS cs ON (oc.clinical_specialty_id = cs.id)" +
                "           LEFT JOIN ( " +
                "               SELECT oc.id, CAST(1 AS BIT) as proced " +
                "               FROM outpatient_consultation oc " +
                "               JOIN document_procedure dp ON (oc.document_id = dp.document_id) " +
                "               GROUP BY proced, oc.id " +
                "           ) pr ON (oc.id = pr.id)"+
                "       WHERE OC.id = :outpatientId ";
        Optional<Object[]> queryResult =  entityManager.createNativeQuery(query)
                .setParameter("outpatientId", outpatientId)
                .setMaxResults(1)
                .getResultList().stream().findFirst();

        Optional<AnnexIIVo> result = queryResult.map(a -> new AnnexIIVo(
                (String) a[0],
                (String) a[1],
                (String) a[2],
                (String) a[3],
                (String) a[4],
                (String) a[5],
                a[6] != null ? ((Date) a[6]).toLocalDate() : null,
                (String) a[7],
                (String) a[8],
                a[9] != null ? ((Date) a[9]).toLocalDate() : null,
                (Boolean) a[10],
                (String) a[11]
        ));
        return result;
    }
}
