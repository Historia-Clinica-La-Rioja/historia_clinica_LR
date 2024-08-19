package net.pladema.reports.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.hierarchicalunits.FetchDescendantsByHierarchicalUnitId;
import net.pladema.medicalconsultation.appointment.repository.AppointmentStateRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.reports.application.ports.AppointmentConsultationSummaryStorage;
import net.pladema.reports.domain.AppointmentConsultationSummaryBo;
import net.pladema.reports.domain.ReportSearchFilterBo;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentConsultationSummaryStorageImpl implements AppointmentConsultationSummaryStorage {

	private final EntityManager entityManager;

	private final FetchDescendantsByHierarchicalUnitId fetchDescendantsByHierarchicalUnitId;

	private final AppointmentStateRepository appointmentStateRepository;

	@Override
	public List<AppointmentConsultationSummaryBo> fetchAppointmentConsultationSummary(ReportSearchFilterBo filter){

		LocalDateTime startDate = filter.getFromDate().atTime(3, 0);
		LocalDateTime endDate = filter.getToDate().atTime(3, 0).plusDays(1);
		boolean doUnion = true;

		String selectCommon = "CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) < 1 AND p2.gender_id = 1 THEN 1 END) AS INTEGER) AS ageRange0to1F, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) < 1 AND p2.gender_id = 2 THEN 1 END) AS INTEGER) AS ageRange0to1M, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) < 1 AND p2.gender_id = 3 THEN 1 END) AS INTEGER) AS ageRange0to1X, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 1 AND 4 AND p2.gender_id = 1 THEN 1 END) AS INTEGER) AS ageRange1to4F, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 1 AND 4 AND p2.gender_id = 2 THEN 1 END) AS INTEGER) AS ageRange1to4M, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 1 AND 4 AND p2.gender_id = 3 THEN 1 END) AS INTEGER) AS ageRange1to4X, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 5 AND 9 AND p2.gender_id = 1 THEN 1 END) AS INTEGER) AS ageRange5to9F, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 5 AND 9 AND p2.gender_id = 2 THEN 1 END) AS INTEGER) AS ageRange5to9M, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 5 AND 9 AND p2.gender_id = 3 THEN 1 END) AS INTEGER) AS ageRange5to9X, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 10 AND 14 AND p2.gender_id = 1 THEN 1 END) AS INTEGER) AS ageRange10to14F, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 10 AND 14 AND p2.gender_id = 2 THEN 1 END) AS INTEGER) AS ageRange10to14M, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 10 AND 14 AND p2.gender_id = 3 THEN 1 END) AS INTEGER) AS ageRange10to14X, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 15 AND 19 AND p2.gender_id = 1 THEN 1 END) AS INTEGER) AS ageRange15to19F, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 15 AND 19 AND p2.gender_id = 2 THEN 1 END) AS INTEGER) AS ageRange15to19M, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 15 AND 19 AND p2.gender_id = 3 THEN 1 END) AS INTEGER) AS ageRange15to19X, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 20 AND 34 AND p2.gender_id = 1 THEN 1 END) AS INTEGER) AS ageRange20to34F, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 20 AND 34 AND p2.gender_id = 2 THEN 1 END) AS INTEGER) AS ageRange20to34M, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 20 AND 34 AND p2.gender_id = 3 THEN 1 END) AS INTEGER) AS ageRange20to34X, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 35 AND 49 AND p2.gender_id = 1 THEN 1 END) AS INTEGER) AS ageRange35to49F, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 35 AND 49 AND p2.gender_id = 2 THEN 1 END) AS INTEGER) AS ageRange35to49M, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 35 AND 49 AND p2.gender_id = 3 THEN 1 END) AS INTEGER) AS ageRange35to49X, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 50 AND 64 AND p2.gender_id = 1 THEN 1 END) AS INTEGER) AS ageRange50to64F, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 50 AND 64 AND p2.gender_id = 2 THEN 1 END) AS INTEGER) AS ageRange50to64M, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) BETWEEN 50 AND 64 AND p2.gender_id = 3 THEN 1 END) AS INTEGER) AS ageRange50to64X, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) > 65 AND p2.gender_id = 1 THEN 1 END) AS INTEGER) AS ageRangeOver65F, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) > 65 AND p2.gender_id = 2 THEN 1 END) AS INTEGER) AS ageRangeOver65M, " +
				"CAST(COUNT(CASE WHEN extract(year FROM age(apt.date_type_id, p2.birth_date)) > 65 AND p2.gender_id = 3 THEN 1 END) AS INTEGER) AS ageRangeOver65X, " +
				"CAST(COUNT(CASE WHEN p2.gender_id IS NULL OR extract(year FROM age(apt.date_type_id, p2.birth_date)) IS NULL THEN 1 END) AS INTEGER) AS unspecified, " +
				"CAST(COUNT(apt.id) AS INTEGER) AS total, " +
				"CAST(COUNT(CASE WHEN apt.patient_medical_coverage_id IS NOT NULL THEN 1 END) AS INTEGER) AS hasCoverage, " +
				"CAST(COUNT(CASE WHEN apt.patient_medical_coverage_id IS NULL THEN 1 END) AS INTEGER) AS noCoverage ";

		String whereCommon = "WHERE i.id = :institutionId " +
				"AND apt.date_type_id BETWEEN :startDate AND :endDate ";

		String sqlQuery = "SELECT " +
				"hut.description as hierarchical_unit_type, " +
				"hu.alias as hierarchical_unit, " +
				"cs.name AS clinicalSpecialty, " +
				selectCommon +
				"FROM {h-schema}institution i " +
				"JOIN {h-schema}doctors_office dof ON (i.id = dof.institution_id) " +
				"JOIN {h-schema}diary d ON (dof.id = d.doctors_office_id) " +
				"JOIN {h-schema}appointment_assn aa ON (d.id = aa.diary_id) " +
				"JOIN {h-schema}appointment apt ON (aa.appointment_id = apt.id) " +
				"LEFT JOIN {h-schema}hierarchical_unit hu ON (d.hierarchical_unit_id = hu.id) " +
				"LEFT JOIN {h-schema}hierarchical_unit_type hut on (hu.type_id = hut.id) " +
				"LEFT JOIN {h-schema}patient p ON (apt.patient_id = p.id) " +
				"LEFT JOIN {h-schema}person p2 ON (p.person_id = p2.id) " +
				"LEFT JOIN {h-schema}clinical_specialty cs ON (d.clinical_specialty_id = cs.id) " +
				"JOIN {h-schema}healthcare_professional hp ON (hp.id = d.healthcare_professional_id) " +
				whereCommon;

		String sqlSecondQuery = "SELECT " +
				"'Imágenes' as hierarchical_unit_type, " +
				"'' as hierarchical_unit, " +
				"'' AS clinicalSpecialty, " +
				selectCommon +
				"FROM {h-schema}institution i " +
				"LEFT JOIN {h-schema}sector s ON (s.institution_id = i.id) " +
				"LEFT JOIN {h-schema}equipment e ON (e.sector_id = s.id) " +
				"LEFT JOIN {h-schema}equipment_diary ed ON (ed.equipment_id = e.id) " +
				"LEFT JOIN {h-schema}equipment_appointment_assn eaa ON (eaa.equipment_diary_id = ed.id) " +
				"LEFT JOIN {h-schema}appointment apt on (apt.id = eaa.appointment_id) " +
				"JOIN {h-schema}patient p ON (apt.patient_id = p.id) " +
				"JOIN {h-schema}person p2 ON (p.person_id = p2.id) " +
				whereCommon;

		if (filter.getHierarchicalUnitTypeId() != null){
			sqlQuery += " AND hu.type_id = " + filter.getHierarchicalUnitTypeId();
			doUnion = false;
		}

		if (filter.getHierarchicalUnitId() != null){
			sqlQuery += filter.isIncludeHierarchicalUnitDescendants() ? " AND hu.id IN (:hierarchicalUnitIds) " : " AND hu.id = " + filter.getHierarchicalUnitId();
			doUnion = false;
		}

		if (filter.getClinicalSpecialtyId() != null){
			sqlQuery += " AND d.clinical_specialty_id = " + filter.getClinicalSpecialtyId();
			doUnion = false;
		}

		if (filter.getDoctorId() != null){
			sqlQuery += " AND hp.id = " + filter.getDoctorId();
			doUnion = false;
		}

		if (filter.getAppointmentStateId() != null) {
			String appointmentStateFilter = " AND apt.appointment_state_id = " + filter.getAppointmentStateId();
			sqlQuery += appointmentStateFilter;
			sqlSecondQuery += appointmentStateFilter;
		}

		sqlQuery += "GROUP BY " +
				"hu.alias, " +
				"hut.description, " +
				"cs.name ";

		sqlSecondQuery += "GROUP BY " +
				"i.id; ";

		if (doUnion)
			sqlQuery += "UNION " +
					sqlSecondQuery;
		else sqlQuery += "; ";

		var query = entityManager.createNativeQuery(sqlQuery);

		if (filter.getHierarchicalUnitId() != null && filter.isIncludeHierarchicalUnitDescendants())
			query.setParameter("hierarchicalUnitIds", getHierarchicalUnitIds(filter.getHierarchicalUnitId()));

		query.setParameter("institutionId", filter.getInstitutionId());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		List<Object[]> appointmentConsultationSummaryData = query.getResultList();
		List<AppointmentConsultationSummaryBo> result = new ArrayList<>();
		appointmentConsultationSummaryData.forEach(acs ->
				result.add(new AppointmentConsultationSummaryBo(
						(String) acs[0],
						(String) acs[1],
						(String) acs[2],
						(Integer) acs[3],
						(Integer) acs[4],
						(Integer) acs[5],
						(Integer) acs[6],
						(Integer) acs[7],
						(Integer) acs[8],
						(Integer) acs[9],
						(Integer) acs[10],
						(Integer) acs[11],
						(Integer) acs[12],
						(Integer) acs[13],
						(Integer) acs[14],
						(Integer) acs[15],
						(Integer) acs[16],
						(Integer) acs[17],
						(Integer) acs[18],
						(Integer) acs[19],
						(Integer) acs[20],
						(Integer) acs[21],
						(Integer) acs[22],
						(Integer) acs[23],
						(Integer) acs[24],
						(Integer) acs[25],
						(Integer) acs[26],
						(Integer) acs[27],
						(Integer) acs[28],
						(Integer) acs[29],
						(Integer) acs[30],
						(Integer) acs[31],
						(Integer) acs[32],
						(Integer) acs[33]
				))
		);
		log.debug("Appointment consultation summary size -> {} ", result.size());
		return result.stream()
				.sorted(Comparator.comparing(AppointmentConsultationSummaryBo::getHierarchicalUnitType))
				.collect(Collectors.toList());
	}

	@Override
	public AppointmentState getAppointmentStateById(Short id) {
		return appointmentStateRepository.getById(id);
	}

	private List<Integer> getHierarchicalUnitIds(Integer hierarchicalUnitId) {
		var result = fetchDescendantsByHierarchicalUnitId.run(hierarchicalUnitId);
		result.add(hierarchicalUnitId);
		return result;
	}

}
