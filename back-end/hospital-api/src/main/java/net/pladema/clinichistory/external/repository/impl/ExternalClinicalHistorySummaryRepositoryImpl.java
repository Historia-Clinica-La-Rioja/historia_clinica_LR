package net.pladema.clinichistory.external.repository.impl;

import net.pladema.clinichistory.external.repository.ExternalClinicalHistorySummaryRepository;
import net.pladema.clinichistory.external.repository.domain.ExternalClinicalHistorySummaryVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ExternalClinicalHistorySummaryRepositoryImpl implements ExternalClinicalHistorySummaryRepository {

    private final EntityManager entityManager;

    public ExternalClinicalHistorySummaryRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExternalClinicalHistorySummaryVo> getAllExternalClinicalHistorySummary(Integer patientId) {
        String sqlString = "SELECT ec.id, ec.professionalSpecialty, ec.consultationDate, ec.professionalName, ec.notes, ec.institution"
                + "  FROM ExternalClinicalHistory AS ec "
                + "  JOIN Person AS pe ON ec.patientDocumentNumber= pe.identificationNumber "
                + "  JOIN Patient AS pa ON pa.personId=pe.id "
                + "  WHERE pa.id =:patientId "
                + "  AND ec.patientDocumentType = pe.identificationTypeId "
                + "  AND ec.patientGender= pe.genderId "
                + "  ORDER BY ec.consultationDate DESC";
        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .getResultList();
        List<ExternalClinicalHistorySummaryVo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new ExternalClinicalHistorySummaryVo(
                        (Integer) a[0],
                        (String) a[1],
                        (LocalDate) a[2],
                        (String) a[3],
                        (String) a[4],
                        (String) a[5])));
        return result;
    }
}
