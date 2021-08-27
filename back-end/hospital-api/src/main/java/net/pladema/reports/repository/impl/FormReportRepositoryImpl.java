package net.pladema.reports.repository.impl;

import net.pladema.reports.repository.FormReportRepository;
import net.pladema.reports.repository.entity.FormVVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.Optional;

@Repository
public class FormReportRepositoryImpl implements FormReportRepository {

    private final EntityManager entityManager;

    public FormReportRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormVVo> getAppointmentFormVInfo(Integer appointmentId) {
        String query = "SELECT NEW net.pladema.reports.repository.entity.FormVVo(i.name, pe.firstName, pe.middleNames, "+
                "               pe.lastName, pe.otherLastNames, g.description, pe.birthDate, it.description, "+
                "               pe.identificationNumber, mc.name, pmca.affiliateNumber, ad.street, ad.number, ci.description) "+
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
                "           LEFT JOIN Address AS ad ON (pex.addressId = ad.id) " +
                "           LEFT JOIN City AS ci ON (ad.cityId = ci.id) " +
                "           LEFT JOIN IdentificationType AS it ON (it.id = pe.identificationTypeId) "+
                "           LEFT JOIN Gender AS g ON (pe.genderId = g.id) "+
                "       WHERE a.id = :appointmentId ";
        return entityManager.createQuery(query)
                .setParameter("appointmentId", appointmentId)
                .setMaxResults(1)
                .getResultList().stream().findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormVVo> getOutpatientFormVInfo(Integer outpatientId) {
        String query = "SELECT i.name, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, " +
                "              g.description, pe.birth_date, it.description as idType, pe.identification_number, oc.start_date, prob.descriptions as problems, "+
                "              ad.street, ad.number, ci.description as city"+
                "       FROM outpatient_consultation AS oc "+
                "           JOIN Institution AS i ON (oc.institution_id = i.id) " +
                "           JOIN Patient AS pa ON (oc.patient_id = pa.id) " +
                "           LEFT JOIN Person AS pe ON (pe.id = pa.person_id) " +
                "           LEFT JOIN Person_extended AS pex ON (pe.id = pex.person_id) " +
                "           LEFT JOIN Address AS ad ON (pex.address_id = ad.id) " +
                "           LEFT JOIN City AS ci ON (ad.city_id = ci.id) " +
                "           JOIN Identification_type AS it ON (it.id = pe.identification_type_id) " +
                "           JOIN Gender AS g ON (pe.gender_id = g.id) " +
                "           LEFT JOIN ( " +
                "               SELECT oc.id, STRING_AGG(sno.pt, '| ') as descriptions " +
                "               FROM outpatient_consultation oc " +
                "               JOIN document doc ON (oc.document_id = doc.id) " +
                "               JOIN document_health_condition dhc ON (doc.id = dhc.document_id) " +
                "               JOIN health_condition hc ON (dhc.health_condition_id = hc.id) " +
                "               JOIN snomed sno ON (hc.snomed_id = sno.id) " +
                "           GROUP BY oc.id " +
                "            ) prob ON (oc.id = prob.id) " +
                "        WHERE oc.id = :outpatientId ";
        Optional<Object[]> queryResult =  entityManager.createNativeQuery(query)
                .setParameter("outpatientId", outpatientId)
                .setMaxResults(1)
                .getResultList().stream().findFirst();

        Optional<FormVVo> result = queryResult.map(a -> new FormVVo(
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
                        (String) a[10],
                        (String) a[11],
                        (String) a[12],
                        (String) a[13]
                ));
        return result;
    }
}
