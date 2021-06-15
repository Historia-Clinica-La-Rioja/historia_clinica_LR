package net.pladema.reports.util;

public class Queries {

    public static String GET_MONTHLY_REPORT =
    "SELECT " +
            " p.description as provincia, d.description as municipio, i.sisa_code as cod_estable, i.name as institucion, " +
            " CONCAT(pe.last_name, ' ', pe.other_last_names) as apellidosPaciente, CONCAT(pe.first_name, ' ', pe.middle_names) as nombresPaciente, " +
            " pe.identification_type_id as tipoDocumento, pe.identification_number as numeroDocumento, pe.birth_date as fechaNacimiento, " +
            " g.description as genero, CONCAT(a2.street, ' ', a2.number, ' ', a2.floor, ' ', a2.apartment, ' ', c2.description) as domicilio, " +
            " px.phone_number as numeroTelefono, px.email as email, mc.name as nombreCobertura, pmc.affiliate_number, oc.start_date as fechaInicio, " +
            " cs.name as especialidad, CONCAT(p2.last_name, ' ', p2.other_last_names, ' ',p2.first_name, ' ', p2.middle_names) as nombresApellidosProfesional, " +
            " r.description as razonConsulta " +
            "FROM " +
            "   outpatient_consultation oc " +
            "   JOIN institution i ON (oc.institution_id = i.id) " +
            "   JOIN address a ON (i.address_id = a.id) " +
            "   LEFT JOIN city c ON (a.city_id = c.id) " +
            "   JOIN department d ON (c.department_id = d.id) " +
            "   JOIN province p ON (d.province_id = p.id) " +
            "   JOIN patient pa ON (oc.patient_id = pa.id) " +
            "   JOIN person pe ON (pa.person_id = pe.id) " +
            "   LEFT JOIN person_extended px ON (px.person_id = pe.id) " +
            "   LEFT JOIN gender g ON (px.gender_self_determination = g.id) " +
            "   LEFT JOIN address a2 ON (px.address_id = a2.id) " +
            "   LEFT JOIN city c2 ON (a2.city_id = c2.id) " +
            "   LEFT JOIN patient_medical_coverage pmc ON (pmc.patient_id = pa.id) " +
            "   LEFT JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
            "   LEFT JOIN clinical_specialty cs ON (oc.clinical_specialty_id = cs.id) " +
            "   JOIN healthcare_professional hp ON (oc.doctor_id = hp.id) " +
            "   JOIN person p2 ON (hp.person_id = p2.id) " +
            "   LEFT JOIN outpatient_consultation_reasons ocr ON (oc.id = ocr.outpatient_consultation_id) " +
            "   LEFT JOIN reasons r ON (ocr.reason_id = r.description) " +
           "WHERE" +
            "   i.id = :institutionId " +
            "   AND oc.start_date between :startDate AND :endDate " +
            "       %s " + // filtro especialidadId
            "       %s ";  // filtro doctorId
}
