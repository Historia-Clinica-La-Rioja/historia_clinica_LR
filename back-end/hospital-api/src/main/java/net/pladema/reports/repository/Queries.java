package net.pladema.reports.repository;

public class Queries {

    public static String GET_MONTHLY_REPORT =
    "SELECT " +
            " p.description as provincia, d.description as municipio, i.sisa_code as cod_estable, i.name as institucion, " +
            " CONCAT(pe.last_name, ' ', pe.other_last_names) as apellidosPaciente, CONCAT(pe.first_name, ' ', pe.middle_names) as nombresPaciente, " +
            " it.description as tipoDocumento, pe.identification_number as numeroDocumento, to_char(pe.birth_date,'DD/MM/YYYY') as fechaNacimiento, " +
            " g.description as genero, CONCAT(a2.street, ' ', a2.number, ' ', a2.floor, ' ', a2.apartment, ' ', c2.description) as domicilio, " +
            " px.phone_number as numeroTelefono, px.email as email, " + //coverage.nombreCobertura as nombreCobertura, coverage.affiliate_number, " +
            " to_char(oc.start_date,'DD/MM/YYYY') as fechaInicio, cs.name as especialidad, " +
            " CONCAT(p2.last_name, ' ', p2.other_last_names, ' ',p2.first_name, ' ', p2.middle_names) as nombresApellidosProfesional, ocr.reasons as razonConsulta, " +
            " prob.descriptions as problems, vs.weight, vs.heigth, vs.systolic, vs.diastolic " +
            " FROM " +
            "   outpatient_consultation oc " +
            "   JOIN institution i ON (oc.institution_id = i.id) " +
            "   JOIN address a ON (i.address_id = a.id) " +
            "   LEFT JOIN city c ON (a.city_id = c.id) " +
            "   JOIN department d ON (c.department_id = d.id) " +
            "   JOIN province p ON (d.province_id = p.id) " +
            "   JOIN patient pa ON (oc.patient_id = pa.id) " +
            "   JOIN person pe ON (pa.person_id = pe.id) " +
            "   LEFT JOIN identification_type it ON (pe.identification_type_id = it.id) "+
            "   LEFT JOIN person_extended px ON (px.person_id = pe.id) " +
            "   LEFT JOIN gender g ON (px.gender_self_determination = g.id) " +
            "   LEFT JOIN address a2 ON (px.address_id = a2.id) " +
            "   LEFT JOIN city c2 ON (a2.city_id = c2.id) " +
            /*"   LEFT JOIN ( " +
            "       SELECT distinct on (pmc.patient_id)pmc.patient_id, pmc.affiliate_number, mc.name AS nombreCobertura " +
            "           FROM patient_medical_coverage pmc " +
            "           JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
            "           LEFT JOIN appointment ap ON (pmc.id = ap.patient_medical_coverage_id) " +
            "        ) coverage ON (pa.id = coverage.patient_id) " +*/
            "   LEFT JOIN clinical_specialty cs ON (oc.clinical_specialty_id = cs.id) " +
            "   JOIN healthcare_professional hp ON (oc.doctor_id = hp.id) " +
            "   JOIN person p2 ON (hp.person_id = p2.id) " +
            "   LEFT JOIN (" +
            "       SELECT ocr.outpatient_consultation_id as id, STRING_AGG(r.description, ', ') as reasons " +
            "          FROM outpatient_consultation_reasons ocr " +
            "          JOIN reasons r ON (ocr.reason_id = r.id) " +
            "          GROUP BY ocr.outpatient_consultation_id "  +
            "  ) ocr ON (oc.id = ocr.id )  "  +
            "   LEFT JOIN (" +
            "       SELECT oc.id, STRING_AGG(sno.pt, ', ') as descriptions " +
            "           FROM outpatient_consultation oc " +
            "           JOIN document doc ON (oc.document_id = doc.id)" +
            "           JOIN document_health_condition dhc ON (doc.id = dhc.document_id)" +
            "           JOIN health_condition hc ON (dhc.health_condition_id = hc.id)" +
            "           JOIN snomed sno ON (hc.snomed_id = sno.id) " +
            "           GROUP BY oc.id " +
            "  ) prob ON (oc.id = prob.id) " +
            "   LEFT JOIN (" +
            "       SELECT oc.id, " +
            "           MAX(CASE WHEN ovs.loinc_code='8480-6' THEN ovs.value END) as systolic, " +
            "           MAX(CASE WHEN ovs.loinc_code='8462-4' THEN ovs.value END) as diastolic, " +
            "           MAX(CASE WHEN ovs.loinc_code='8302-2' THEN ovs.value END) as heigth, " +
            "           MAX(CASE WHEN ovs.loinc_code='29463-7' THEN ovs.value END) as weight " +
            "       FROM outpatient_consultation oc " +
            "       LEFT JOIN document_vital_sign dvs ON (oc.document_id = dvs.document_id) " +
            "       LEFT JOIN observation_vital_sign ovs ON (dvs.observation_vital_sign_id = ovs.id) " +
            "       GROUP BY oc.id " +
            "       ORDER BY oc.id "+
            " ) vs ON (oc.id = vs.id) " +
            "WHERE" +
            "   i.id = :institutionId " +
            "   AND oc.billable = true " +
            "   AND oc.start_date BETWEEN :startDate AND :endDate " +
            "       %s " + // filtro especialidadId
            "       %s ";  // filtro doctorId
}
