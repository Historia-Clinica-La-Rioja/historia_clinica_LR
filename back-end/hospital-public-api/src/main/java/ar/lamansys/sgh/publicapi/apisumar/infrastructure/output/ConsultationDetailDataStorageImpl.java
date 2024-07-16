package ar.lamansys.sgh.publicapi.apisumar.infrastructure.output;

import ar.lamansys.sgh.publicapi.apisumar.application.port.out.ConsultationDetailDataStorage;

import ar.lamansys.sgh.publicapi.apisumar.domain.ConsultationDetailDataBo;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultationDetailDataStorageImpl implements ConsultationDetailDataStorage {

	private static final Logger LOG = LoggerFactory.getLogger(ConsultationDetailDataStorageImpl.class);

	private final EntityManager entityManager;

	public ConsultationDetailDataStorageImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public List<ConsultationDetailDataBo> getConsultationsData(String sisaCode, LocalDateTime startDate, LocalDateTime endDate) {
		LOG.debug("sisaCode -> {}", sisaCode);

		String stringQuery = "WITH documents_filtered AS ( " +
				"SELECT * FROM document d " +
				"WHERE d.created_on BETWEEN :startDate AND :endDate " +
				"ORDER BY d.id, d.created_on DESC " +
				"), source AS ( " +
				"SELECT d.*, ie.patient_medical_coverage_id, hpg.healthcare_professional_id FROM documents_filtered d INNER JOIN internment_episode ie ON ie.id=d.source_id AND d.source_type_id=0 LEFT JOIN healthcare_professional_group hpg ON hpg.internment_episode_id=ie.id " +
				"UNION ALL " +
				"SELECT d.*, oc.patient_medical_coverage_id, oc.doctor_id AS healthcare_professional_id FROM documents_filtered d INNER JOIN outpatient_consultation oc ON oc.document_id=d.id " +
				"UNION ALL " +
				"SELECT d.*, mr.medical_coverage_id as patient_medical_coverage_id, mr.doctor_id AS healthcare_professional_id FROM documents_filtered d INNER JOIN medication_request mr ON mr.id=d.source_id AND d.source_type_id=2 " +
				"UNION ALL " +
				"SELECT d.*, sr.medical_coverage_id as patient_medical_coverage_id, sr.doctor_id AS healthcare_professional_id FROM documents_filtered d INNER JOIN service_request sr ON sr.id=d.source_id AND d.source_type_id=3 " +
				"UNION ALL " +
				"SELECT d.*, ece.patient_medical_coverage_id, hp.id AS healthcare_professional_id FROM documents_filtered d INNER JOIN emergency_care_episode ece ON ece.id=d.source_id AND d.source_type_id=4 " +
				"LEFT JOIN users us ON ece.created_by=us.id LEFT JOIN user_person u_p ON u_p.user_id=us.id LEFT JOIN healthcare_professional hp ON hp.person_id=u_p.person_id " +
				"UNION ALL " +
				"SELECT d.*, vc.patient_medical_coverage_id, vc.doctor_id AS healthcare_professional_id FROM documents_filtered d INNER JOIN vaccine_consultation vc ON vc.id=d.source_id AND d.source_type_id=5 " +
				"UNION ALL " +
				"SELECT d.*, oc.patient_medical_coverage_id, oc.doctor_id AS healthcare_professional_id FROM documents_filtered d INNER JOIN odontology_consultation oc ON oc.id=d.source_id AND d.source_type_id=6 " +
				"UNION ALL " +
				"SELECT d.*, nc.patient_medical_coverage_id, nc.doctor_id AS healthcare_professional_id FROM documents_filtered d INNER JOIN nursing_consultation nc ON nc.id=d.source_id AND d.source_type_id=7 " +
				"), source_ordered AS ( " +
				"SELECT * FROM source s ORDER BY s.created_on ASC " +
				") " +
				"SELECT concat(ins.name, '(SISA: ', ins.sisa_code, ' | CUIT: ', ins.cuit, ')') AS institution, " +
				"st.description as origin, " +
				"cs.name as operativeUnit, " +
				"concat(p.first_name, ' ', p.middle_names, ' ', p.last_name, ' ', p.other_last_names) as lender, " +
				"p.identification_number as lenderIdentificationNumber, " +
				"b.created_on AT TIME ZONE 'UTC-3' as attentionDate, " +
				"pp.identification_number as patientIdentificationNumber, " +
				"concat(pp.first_name , ' ', pp.last_name) as patientName, " +
				"g.description as patientSex, " +
				"gg.description as patientGender, " +
				"pe.name_self_determination as patientSelfPerceivedName, " +
				"pp.birth_date as patientBirthDate, " +
				"case " +
				"when (EXTRACT(MONTH FROM b.updated_on) - EXTRACT(MONTH FROM pp.birth_date)) < 0 then " +
				"concat((EXTRACT(YEAR FROM b.updated_on) - EXTRACT(YEAR FROM pp.birth_date)) -1, ' Años, ', 12 +(EXTRACT(MONTH FROM b.updated_on) - EXTRACT(MONTH FROM pp.birth_date)), ' Meses') " +
				"else concat((EXTRACT(YEAR FROM b.updated_on) - EXTRACT(YEAR FROM pp.birth_date)), ' Años, ', (EXTRACT(MONTH FROM b.updated_on) - EXTRACT(MONTH FROM pp.birth_date)), ' Meses') " +
				"end as patientAgeTurn, " +
				"case " +
				"when (EXTRACT(MONTH FROM CURRENT_TIMESTAMP) - EXTRACT(MONTH FROM pp.birth_date)) < 0 then " +
				"concat((EXTRACT(YEAR FROM CURRENT_TIMESTAMP) - EXTRACT(YEAR FROM pp.birth_date)) -1, ' Años, ', 12 +(EXTRACT(MONTH FROM CURRENT_TIMESTAMP) - EXTRACT(MONTH FROM pp.birth_date)), ' Meses') " +
				"else concat((EXTRACT(YEAR FROM CURRENT_TIMESTAMP) - EXTRACT(YEAR FROM pp.birth_date)), ' Años, ', (EXTRACT(MONTH FROM CURRENT_TIMESTAMP) - EXTRACT(MONTH FROM pp.birth_date)), ' Meses') " +
				"end as patientAge, " +
				"et.pt as ethnicity, " +
				"concat(mc.name, '(RNOS: ', hi.rnos, ')') as medicalCoverage, " +
				"concat(a.street, ' N° ', a.number, case when a.floor is not null then concat(' Piso: ', a.floor, 'Departamento: ', a.apartment) end) as address, " +
				"c.description as location, " +
				"ed.description as instructionLevel, " +
				"lab.description as workSituation, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=1) as systolicPressure, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=2) as diastolicPressure, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=3) as meanArterialPressure, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=4) as temperature, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=5) as heartRate, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=6) as respiratoryRate, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=7) as bloodOxygenSaturation, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=8) as height, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=9) as weight, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=10) as bmi, " +
				"(select ovs.value FROM document_vital_sign dvs INNER JOIN observation_vital_sign ovs on dvs.observation_vital_sign_id=ovs.id inner join snomed s_vs on ovs.snomed_id=s_vs.id where dvs.document_id=b.id and s_vs.id=1409) as headCircumference, " +
				"case b.source_type_id " +
				"when 1 then (select string_agg(concat(r.description, '(SNOMED: ', r.id, ')'), ', ') FROM outpatient_consultation_reasons ocr inner join reasons r on ocr.reason_id=r.id where ocr.outpatient_consultation_id=b.source_id AND b.source_type_id=1) " +
				"when 4 then (select string_agg(concat(r.description, '(SNOMED: ', r.id, ')'), ', ') FROM emergency_care_episode_reason ecer inner join reasons r on ecer.reason_id=r.id where ecer.emergency_care_episode_id=b.source_id AND b.source_type_id=4) " +
				"when 6 then (select string_agg(concat(r.description, '(SNOMED: ', r.id, ')'), ', ') FROM odontology_consultation_reason ocr inner join odontology_reason r on ocr.reason_id=r.id where ocr.odontology_consultation_id=b.source_id AND b.source_type_id=6) " +
				"end as reasons, " +
				"(select string_agg(concat(s_proc.pt, '(', ps.description, ' | SNOMED: ', s_proc.sctid, ' | CIE10: ', proc.cie10_codes, ')'), ', ') from document_procedure dp inner join procedures proc on dp.procedure_id=proc.id inner join procedures_status ps on proc.status_id=ps.id inner join snomed s_proc on proc.snomed_id=s_proc.id where dp.document_id=b.id) as procedures, " +
				"(select string_agg(concat(s_p.pt, '(SNOMED: ', s_p.sctid, ' | CIE10: ', op.cie10_codes, ')[diente: ', stp.pt, ' | sctid: ', stp.sctid, '][superficie: ', sfp.pt, ' | sctid', sfp.sctid, ']'), ', ') from document_odontology_procedure dop INNER JOIN odontology_procedure op on dop.odontology_procedure_id=op.id INNER JOIN snomed s_p on op.snomed_id=s_p.id " +
				"LEFT JOIN snomed stp ON op.tooth_id=stp.id LEFT JOIN snomed sfp ON op.surface_id=sfp.id WHERE dop.document_id=b.id AND b.source_type_id=6) as dentalProcedures, " +
				"(SELECT concat(oci.permanent_c, '|', oci.permanent_p, '|', oci.permanent_o) FROM odontology_consultation_indices oci WHERE oci.odontology_consultation_id=b.source_id AND b.source_type_id=6) AS cpo, " +
				"(SELECT concat(oci.temporary_c, '|', oci.temporary_e, '|', oci.temporary_o) FROM odontology_consultation_indices oci WHERE oci.odontology_consultation_id=b.source_id AND b.source_type_id=6) AS ceo, " +
				"(select string_agg(concat(s_prob.pt,' (',ccs.description,' & ',cvs.description,' - Fecha: ',hc.start_date, ' - Tipo: ', pt.description,')', '[SNOMED: ', s_prob.sctid, ' | CIE10: ', hc.cie10_codes, ']'), ', ') " +
				"from document_health_condition dhc inner join health_condition hc on dhc.health_condition_id=hc.id inner join problem_type pt on hc.problem_id=pt.id inner join snomed s_prob on hc.snomed_id=s_prob.id inner join condition_clinical_status ccs on hc.status_id=ccs.id inner join condition_verification_status cvs on hc.verification_status_id=cvs.id where dhc.document_id=b.id and hc.problem_id<>'57177007') as problems, " +
				"(select string_agg(concat(s_med.pt, '[SNOMED: ', s_med.sctid, ' | CIE10: ', ms.cie10_codes, '] (status: ', mss.description, ')'), ', ') FROM document_medicamention_statement dms inner join medication_statement ms on dms.medication_statement_id=ms.id inner join medication_statement_status mss on ms.status_id=mss.id inner join snomed s_med on ms.snomed_id=s_med.id WHERE dms.document_id=b.id) as medication, " +
				"(select string_agg(ev.description, ', ') from note ev where b.other_note_id=ev.id) as evolution " +
				"FROM source_ordered b " +
				"LEFT JOIN source_type st ON b.source_type_id=st.id " +
				"LEFT JOIN institution ins ON b.institution_id=ins.id " +
				"LEFT JOIN clinical_specialty cs ON b.clinical_specialty_id=cs.id " +
				"LEFT JOIN users us ON b.created_by=us.id " +
				"LEFT JOIN user_person up ON up.user_id=us.id " +
				"LEFT JOIN person p ON up.person_id=p.id " +
				"LEFT JOIN healthcare_professional hp ON hp.person_id=p.id " +
				"LEFT JOIN patient pa ON b.patient_id=pa.id " +
				"INNER JOIN person pp ON pa.person_id=pp.id " +
				"LEFT JOIN person_extended pe ON pe.person_id=pp.id " +
				"LEFT JOIN ethnicity et ON pe.ethnicity_id=et.id " +
				"LEFT JOIN education_level ed ON pe.education_level_id=ed.id " +
				"LEFT JOIN occupation lab ON pe.occupation_id=lab.id " +
				"LEFT JOIN gender gg ON pe.gender_self_determination=gg.id " +
				"LEFT JOIN address a ON pe.address_id=a.id " +
				"LEFT JOIN city c ON a.city_id=c.id " +
				"INNER JOIN gender g ON pp.gender_id=g.id " +
				"LEFT JOIN patient_medical_coverage pmc ON pmc.id=b.patient_medical_coverage_id " +
				"LEFT JOIN medical_coverage mc ON pmc.medical_coverage_id=mc.id " +
				"LEFT JOIN health_insurance hi ON hi.id=mc.id " +
				"WHERE ins.sisa_code = :sisaCode";

		Query query = entityManager.createNativeQuery(stringQuery)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("sisaCode", sisaCode);

		List<Object[]> queryResult = query.getResultList();

		List<ConsultationDetailDataBo> result = queryResult
				.stream()
				.map(this::processConsultationQuery)
				.collect(Collectors.toList());
		return result;
	}

	private ConsultationDetailDataBo mergeResults(List<ConsultationDetailDataBo> unmergedResults) {

		ConsultationDetailDataBo result = new ConsultationDetailDataBo();
		if (unmergedResults.isEmpty()) {
			return result;
		}

		result.setInstitution(unmergedResults.get(0).getInstitution());
		result.setOrigin(unmergedResults.get(0).getOrigin());
		result.setOperativeUnit(unmergedResults.get(0).getOperativeUnit());
		result.setLender(unmergedResults.get(0).getLender());
		result.setLenderIdentificationNumber(unmergedResults.get(0).getLenderIdentificationNumber());
		result.setAttentionDate(unmergedResults.get(0).getAttentionDate());
		result.setPatientIdentificationNumber(unmergedResults.get(0).getPatientIdentificationNumber());
		result.setPatientName(unmergedResults.get(0).getPatientName());
		result.setPatientSex(unmergedResults.get(0).getPatientSex());
		result.setPatientGender(unmergedResults.get(0).getPatientGender());
		result.setPatientSelfPerceivedName(unmergedResults.get(0).getPatientSelfPerceivedName());
		result.setPatientBirthDate(unmergedResults.get(0).getPatientBirthDate());
		result.setPatientAgeTurn(unmergedResults.get(0).getPatientAgeTurn());
		result.setPatientAge(unmergedResults.get(0).getPatientAge());
		result.setEthnicity(unmergedResults.get(0).getEthnicity());
		result.setMedicalCoverage(unmergedResults.get(0).getMedicalCoverage());
		result.setAddress(unmergedResults.get(0).getAddress());
		result.setLocation(unmergedResults.get(0).getLocation());
		result.setInstructionLevel(unmergedResults.get(0).getInstructionLevel());
		result.setWorkSituation(unmergedResults.get(0).getWorkSituation());
		result.setSystolicPressure(unmergedResults.get(0).getSystolicPressure());
		result.setDiastolicPressure(unmergedResults.get(0).getDiastolicPressure());
		result.setMeanArterialPressure(unmergedResults.get(0).getMeanArterialPressure());
		result.setTemperature(unmergedResults.get(0).getTemperature());
		result.setHeartRate(unmergedResults.get(0).getHeartRate());
		result.setRespiratoryRate(unmergedResults.get(0).getRespiratoryRate());
		result.setBloodOxygenSaturation(unmergedResults.get(0).getBloodOxygenSaturation());
		result.setHeight(unmergedResults.get(0).getHeight());
		result.setWeight(unmergedResults.get(0).getWeight());
		result.setBmi(unmergedResults.get(0).getBmi());
		result.setHeadCircumference(unmergedResults.get(0).getHeadCircumference());
		result.setReasons(unmergedResults.get(0).getReasons());
		result.setProcedures(unmergedResults.get(0).getProcedures());
		result.setDentalProcedures(unmergedResults.get(0).getDentalProcedures());
		result.setCpo(unmergedResults.get(0).getCpo());
		result.setCeo(unmergedResults.get(0).getCeo());
		result.setProblems(unmergedResults.get(0).getProblems());
		result.setMedication(unmergedResults.get(0).getMedication());
		result.setEvolution(unmergedResults.get(0).getEvolution());

		return result;
	}

	private ConsultationDetailDataBo processConsultationQuery(Object[] queryResult) {
		return new ConsultationDetailDataBo(
				(String) queryResult[0],
				(String) queryResult[1],
				(String) queryResult[2],
				(String) queryResult[3],
				(String) queryResult[4],
				((Timestamp)queryResult[5]),
				(String) queryResult[6],
				(String) queryResult[7],
				(String) queryResult[8],
				(String) queryResult[9],
				(String) queryResult[10],
				((Date)queryResult[11]),
				(String) queryResult[12],
				(String) queryResult[13],
				(String) queryResult[14],
				(String) queryResult[15],
				(String) queryResult[16],
				(String) queryResult[17],
				(String) queryResult[18],
				(String) queryResult[19],
				(String) queryResult[20],
				(String) queryResult[21],
				(String) queryResult[22],
				(String) queryResult[23],
				(String) queryResult[24],
				(String) queryResult[25],
				(String) queryResult[26],
				(String) queryResult[27],
				(String) queryResult[28],
				(String) queryResult[29],
				(String) queryResult[30],
				(String) queryResult[31],
				(String) queryResult[32],
				(String) queryResult[33],
				(String) queryResult[34],
				(String) queryResult[35],
				(String) queryResult[36],
				(String) queryResult[37],
				(String) queryResult[38]
		);
	}
}
