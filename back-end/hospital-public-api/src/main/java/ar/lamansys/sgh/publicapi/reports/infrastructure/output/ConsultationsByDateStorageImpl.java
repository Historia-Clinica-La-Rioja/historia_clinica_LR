package ar.lamansys.sgh.publicapi.reports.infrastructure.output;

import static java.util.stream.Collectors.groupingBy;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.reports.domain.ClinicalSpecialtyBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateTimeBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.TimeBo;
import ar.lamansys.sgh.publicapi.reports.application.port.out.ConsultationsByDateStorage;
import ar.lamansys.sgh.publicapi.reports.domain.HierarchicalUnitBo;
import ar.lamansys.sgh.publicapi.reports.domain.IdentificationBo;
import ar.lamansys.sgh.publicapi.reports.domain.MedicalCoverageBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate.ConsultationBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate.ConsultationItemWithDateBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ConsultationsByDateStorageImpl implements ConsultationsByDateStorage {

	private final EntityManager entityManager;

	@Override
	public List<ConsultationBo> fetchConsultations(LocalDate dateFrom, LocalDate dateUntil, Integer institutionId, Integer hierarchicalUnitId) {
		log.debug("Find consultations");

		final String WHERE_INSTITUTION_ID = institutionId == null ? "" : " AND i.id = " + institutionId;
		final String WHERE_HIERARCHICAL_UNIT_ID = hierarchicalUnitId == null ? "" : " AND coalesce(hu.closest_service_id, hu2.closest_service_id) = " + hierarchicalUnitId;

		String sqlString = " select a.id as appointment_id, oc.id as outpatient_consultation_id, a.date_type_id, a.hour, " +
				" i.sisa_code, i.name as inst_name, " +
				" coalesce(cs.id, cs2.id) as cs_id, coalesce(cs.sctid_code, cs2.sctid_code) as cs_snomed_code, coalesce(cs.name, cs2.name) as cs_name, " +
				" it.description as type_id, coalesce(pp.identification_number, bp.identification_number) as dni, " +
				" coalesce(mc_consultation.name, mc.name) as m_cov, coalesce(hi_consultation.rnos, hi.rnos) as rnos, " +
				" coalesce (g.description, gb.description) as gender, " +
				" coalesce (pp.birth_date, bp.birth_date) as birthdate, " +
				" CASE WHEN a.appointment_state_id = 5 THEN (a.date_type_id - cast(pp.birth_date as date)) / 365 ELSE NULL END AS edad, " +
				" d2.description as depto, c.description as ciudad, " +
				" as2.description as as_desc, r.id, r.description as motivo, s3.sctid, proc.cie10_codes as cie_proc, s3.pt as procedimiento, " +
				" s2.sctid as hc_snomed, hc.cie10_codes as hc_cie, s2.pt as problema, " +
				" coalesce(hu.closest_service_id, hu2.closest_service_id) as consultation_hierarchical_unit_service_id, " +
				" coalesce(hu3.alias, hu4.alias) as consultation_hierarchical_unit_service_name, " +
				" coalesce(hu.id, hu2.id) as diary_hierarchical_unit_id, " +
				" coalesce(hu.alias, hu2.alias) 	as diary_hierarchical_unit_name, " +
				" ba.appointment_id as booking_id, " +
				" coalesce(hu.type_id, hu2.type_id) as type_consulta, " +
				" coalesce(hu3.type_id, hu4.type_id) as type_diary, " +
				" proc.performed_date, hc.start_date, hc.inactivation_date " +
				" from appointment a " +
				" left join booking_appointment ba on ba.appointment_id = a.id " +
				" left join booking_person bp on ba.booking_person_id = bp.id " +
				" left join gender gb on gb.id = bp.gender_id " +
				" join appointment_state as2 on as2.id = a.appointment_state_id " +
				" left join document_appointment da on a.id = da.appointment_id " +
				" left join document doc on doc.id = da.document_id " +
				" left join outpatient_consultation oc on oc.document_id = da.document_id " +
				" left join clinical_specialty cs on cs.id = oc.clinical_specialty_id " +
				" join appointment_assn aa on aa.appointment_id = a.id " +
				" join diary d on d.id = aa.diary_id " +
				" join clinical_specialty cs2 on cs2.id = d.clinical_specialty_id " +
				" left join hierarchical_unit hu on oc.hierarchical_unit_id = hu.id " +
				" left join hierarchical_unit hu2 on hu2.id = d.hierarchical_unit_id " +
				" left join hierarchical_unit hu3 on hu3.id = hu.closest_service_id " +
				" left join hierarchical_unit hu4 on hu4.id = hu2.closest_service_id " +
				" join doctors_office do2 on do2.id = d.doctors_office_id " +
				" join institution i on do2.institution_id = i.id " +
				" left join patient p on p.id = a.patient_id " +
				" left join person pp on pp.id = p.person_id " +
				" left join identification_type it on it.id = pp.identification_type_id " +
				" left join patient_medical_coverage pmc on pmc.id = a.patient_medical_coverage_id " +
				" left join patient_medical_coverage pmc_consultation on pmc_consultation.id = oc.patient_medical_coverage_id " +
				" left join medical_coverage mc on mc.id = pmc.medical_coverage_id " +
				" left join medical_coverage mc_consultation on mc_consultation.id = pmc_consultation.medical_coverage_id " +
				" left join health_insurance hi on hi.id = mc.id " +
				" left join health_insurance hi_consultation on hi_consultation.id = mc_consultation.id " +
				" left join gender g on g.id = pp.gender_id " +
				" left join person_extended pe on pp.id = pe.person_id " +
				" left join address a2 on pe.address_id = a2.id " +
				" left join city c on c.id = a2.city_id " +
				" left join department d2 on d2.id = c.department_id " +
				" left join document_procedure dp on dp.document_id = doc.id " +
				" left join procedures proc on proc.id = dp.procedure_id " +
				" left join snomed s3 on s3.id = proc.snomed_id " +
				" left join document_health_condition dhc on dhc.document_id = doc.id " +
				" left join health_condition hc on hc.id = dhc.health_condition_id " +
				" left join snomed s2 on s2.id = hc.snomed_id " +
				" left join outpatient_consultation_reasons ocr on ocr.outpatient_consultation_id = oc.id " +
				" left join reasons r on r.id = ocr.reason_id " +
				" where a.deleted IS NOT TRUE AND as2.id <> 4 AND as2.id <> 7 AND d.deleted IS NOT TRUE AND hu.deleted IS NOT TRUE AND hu2.deleted IS NOT TRUE " +
				" AND a.date_type_id BETWEEN :dateFrom AND :dateUntil " + WHERE_INSTITUTION_ID + WHERE_HIERARCHICAL_UNIT_ID;

		List<Object[]> rows = entityManager.createNativeQuery(sqlString)
				.setParameter("dateFrom", dateFrom)
				.setParameter("dateUntil", dateUntil)
				.getResultList();

		return mergeConsultations(processRows(rows));
	}

	private List<ConsultationBo> mergeConsultations(List<ConsultationBo> unmergedConsultationsWithoutHU) {
		var unmergedList = unmergedConsultationsWithoutHU.stream().collect(groupingBy(ConsultationBo::getAppointmentId));

		return unmergedList.values().stream()
				.flatMap(List::stream)
				.collect(Collectors.groupingBy(ConsultationBo::getAppointmentId))
				.values().stream()
				.map(consultationByInstitutionBos -> {
					List<ConsultationItemWithDateBo> mergedReasons = consultationByInstitutionBos.stream()
							.flatMap(consultation -> consultation.getReasons().stream()).distinct().collect(Collectors.toList());

					List<ConsultationItemWithDateBo> mergedProcedures = consultationByInstitutionBos.stream()
							.flatMap(consultation -> consultation.getProcedures().stream()).distinct().collect(Collectors.toList());

					List<ConsultationItemWithDateBo> mergedProblems = consultationByInstitutionBos.stream()
							.flatMap(consultation -> consultation.getProblems().stream()).distinct().collect(Collectors.toList());

					ConsultationBo mergedConsultation = consultationByInstitutionBos.get(0); // Get the first element since all have the same appointmentId
					mergedConsultation.setReasons(mergedReasons);
					mergedConsultation.setProcedures(mergedProcedures);
					mergedConsultation.setProblems(mergedProblems);
					return mergedConsultation;
				})
				.sorted((o1, o2) -> {
					if (o1.getAppointmentId().equals(o2.getAppointmentId())) return 0;
					return o1.getAppointmentId() > o2.getAppointmentId() ? -1 : 1;
				})
				.collect(Collectors.toList());
	}



	private List<ConsultationBo> processRows(List<Object[]> rows) {
		return rows.stream()
				.map(this::processRow)
				.collect(Collectors.toList());
	}

	private ConsultationBo processRow(Object[] row) {
		Integer appointmentId = (Integer) row[0];
		Integer consultationId = (Integer) row[1];
		DateTimeBo appointmentDate = new DateTimeBo(
				new DateBo(((Date)row[2]).toLocalDate().getYear(),
							((Date)row[2]).toLocalDate().getMonthValue(),
						((Date)row[2]).toLocalDate().getDayOfMonth()
				),
				new TimeBo(((Time)row[3]).toLocalTime().getHour(),
						((Time)row[3]).toLocalTime().getMinute(),
						((Time)row[3]).toLocalTime().getSecond()
				)
		);
		String sisaCode = (String)row[4];
		String institution = (String)row[5];

		HierarchicalUnitBo serviceHU = row[27] == null ?
				new HierarchicalUnitBo(null, null, null) :
				new HierarchicalUnitBo(
					(Integer) row[27],
					(String) row[28],
					((Integer) row[33]).shortValue()
		);

		HierarchicalUnitBo HUBo = row[29] == null ?
				new HierarchicalUnitBo(null, null, null) :
				new HierarchicalUnitBo(
					(Integer) row[29],
					(String) row[30],
					((Integer) row[32]).shortValue()
		);

		ClinicalSpecialtyBo clinicalSpecialtyBo = new ClinicalSpecialtyBo((Integer) row[6],
				(String) row[8],
				(String) row[7]
		);
		IdentificationBo identificationBo = new IdentificationBo((String) row[9], (String) row[10]);
		MedicalCoverageBo medicalCoverageBo = new MedicalCoverageBo((String) row[11], (Integer) row[12]);
		String officialGender = (String) row [13];
		DateBo birthdate = row[14] != null ? new DateBo(
				((Date) row [14]).toLocalDate().getYear(),
				((Date) row [14]).toLocalDate().getMonthValue(),
				((Date) row [14]).toLocalDate().getDayOfMonth()
		) : null;
		Integer age = (Integer) row [15];
		String department = (String) row [16];
		String city = (String) row [17];
		String appointmentState = (String) row [18];
		String appointmentBookingChannel = row[31] == null ? "Presencial" : "Turnos online";

		List<ConsultationItemWithDateBo> reasons;
		if(row [19] == null) reasons = List.of(); else
			reasons = List.of(new ConsultationItemWithDateBo(row[19].toString(),
					row[20] != null ? row[20].toString() : null,
					null,
					null,
					null));

		List<ConsultationItemWithDateBo> procedures;
		if(row[21] == null) procedures = List.of(); else
			procedures = List.of(new ConsultationItemWithDateBo(row [21].toString(),
					row [23] != null ? row [23].toString() : null,
					row [22] != null ? row [22].toString() : null,
					row [34] != null ? ((Date)row[34]).toLocalDate(): null,
					null
					));

		List<ConsultationItemWithDateBo> problems;
		if(row[24] == null) problems = List.of(); else
			problems = List.of(new ConsultationItemWithDateBo(row [24].toString(),
					row [26] != null ? (String) row [26] : null,
					row [25] != null ? (String) row [25] : null,
					row [35] != null ? ((Date)row[35]).toLocalDate(): null,
					row [36] != null ? ((Date)row[36]).toLocalDate(): null));

		return new ConsultationBo(
				appointmentId,
				consultationId,
				appointmentDate,
				sisaCode,
				institution,
				serviceHU,
				HUBo,
				clinicalSpecialtyBo,
				identificationBo,
				medicalCoverageBo,
				officialGender,
				birthdate,
				age,
				department,
				city,
				appointmentState,
				appointmentBookingChannel,
				reasons,
				procedures,
				problems
		);
	}
}
