package net.pladema.reports.repository;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultationStorageImpl implements ConsultationStorage {

    private final EntityManager entityManager;

    public ConsultationStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ConsultationsVo> fetchAllByPatientId(Integer patientId) {
        String sqlString = "WITH t AS ("
                +"  SELECT oc.id, d.id as doc_id, oc.start_date AS start_date, oc.patient_id, "
                +"  oc.clinical_specialty_id, oc.doctor_id "
                +"  FROM {h-schema}outpatient_consultation AS oc "
                +  "JOIN {h-schema}document AS d ON (d.source_id = oc.id AND d.source_type_id = 1)"
                +"  WHERE oc.patient_id = :patientId "
                +"  AND oc.billable = true "
                +"  UNION ALL "
                +"  SELECT vc.id, d.id as doc_id, vc.performed_date AS start_date, vc.patient_id, "
                +"  vc.clinical_specialty_id, vc.doctor_id "
                +"  FROM {h-schema}vaccine_consultation AS vc "
                +  "JOIN {h-schema}document AS d ON (d.source_id = vc.id AND d.source_type_id = 5)"
                +"  WHERE vc.patient_id = :patientId "
                +"  AND vc.billable = true "
                +"  ) "
                +"  SELECT t.id, t.doc_id, t.start_date, cs.name, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names "
                +"  FROM t "
                +"  LEFT JOIN {h-schema}clinical_specialty AS cs ON (cs.id = t.clinical_specialty_id) "
                +"  JOIN {h-schema}healthcare_professional AS hp ON (hp.id = t.doctor_id) "
                +"  JOIN {h-schema}person AS pe ON (pe.id = hp.person_id) "
                +"  ORDER BY t.start_date DESC";
        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("patientId", patientId)
                .getResultList();
        List<ConsultationsVo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new ConsultationsVo(
                        (Integer) a[0],
                        a[1] != null ? ((BigInteger)a[1]).longValue() : null,
                        a[2] != null ? ((Date)a[2]).toLocalDate() : null,
                        (String) a[3],
                        (String) a[4],
                        (String) a[5],
                        (String) a[6],
                        (String) a[7]))
        );
        return result;
    }
}
