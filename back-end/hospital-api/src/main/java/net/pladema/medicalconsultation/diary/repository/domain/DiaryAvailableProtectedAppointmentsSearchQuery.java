package net.pladema.medicalconsultation.diary.repository.domain;

import ar.lamansys.sgx.shared.repositories.QueryPart;
import lombok.NoArgsConstructor;
import net.pladema.address.controller.service.domain.DepartmentBo;
import net.pladema.establishment.service.domain.ClinicalSpecialtyBo;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryProtectedAppointmentsSearch;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableProtectedAppointmentsInfoBo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
public class DiaryAvailableProtectedAppointmentsSearchQuery {

	private Integer careLineId;
	private Integer clinicalSpecialtyId;
	private Integer departmentId;
	private Integer institutionId;
	private Boolean includeNameSelfDetermination;

	public DiaryAvailableProtectedAppointmentsSearchQuery(DiaryProtectedAppointmentsSearch diaryProtectedAppointmentsSearch) {
		this.careLineId = diaryProtectedAppointmentsSearch.getCareLineId();
		this.clinicalSpecialtyId = diaryProtectedAppointmentsSearch.getClinicalSpecialtyId();
		this.departmentId = diaryProtectedAppointmentsSearch.getDepartmentId();
		this.institutionId = diaryProtectedAppointmentsSearch.getInstitutionId();
		this.includeNameSelfDetermination = diaryProtectedAppointmentsSearch.getIncludeNameSelfDetermination();
	}

	public QueryPart select() {
		String select = "DISTINCT d.id, " +
				"d.protected_appointments_percentage, " +
				"d.appointment_duration, " +
				"d.start_date, " +
				"d.end_date, " +
				"i.id institutionId, "+
				"i.name institutionName, " +
				"dp.id departmentId, " +
				"dp.description departmentDescription, " +
				"p.first_name, " +
				"p.middle_names, " +
				"p.last_name, " +
				"p.other_last_names, " +
				"cs.id clinicalSpecialtyId, " +
				"cs.name clinicalSpecialtyName, " +
				"dof.description doctorOfficeDescription, " +
				"CASE WHEN dap.id IS NOT NULL THEN true ELSE false END";

		if (includeNameSelfDetermination)
				select = select + " , pe.name_self_determination";

		return new QueryPart(select);
	}

	public QueryPart from() {
		String from = "	{h-schema}diary d " +
				"	LEFT JOIN {h-schema}diary_associated_professional dap ON (d.id = dap.diary_id)" +
				"	LEFT JOIN {h-schema}doctors_office dof ON (d.doctors_office_id = dof.id) " +
				"	LEFT JOIN {h-schema}institution i ON (dof.institution_id = i.id) " +
				" 	LEFT JOIN {h-schema}address a ON (i.address_id = a.id) " +
				" 	LEFT JOIN {h-schema}city c ON (a.city_id = c.id) " +
				" 	LEFT JOIN {h-schema}department dp ON (c.department_id = dp.id) " +
				" 	LEFT JOIN {h-schema}clinical_specialty cs ON (d.clinical_specialty_id = cs.id) " +
				" 	LEFT JOIN {h-schema}healthcare_professional hp ON (d.healthcare_professional_id = hp.id) " +
				" 	LEFT JOIN {h-schema}person p ON (hp.person_id = p.id) ";

		if (includeNameSelfDetermination)
			from = from + "LEFT JOIN {h-schema}person_extended pe ON (pe.person_id = p.id) ";

		if (this.careLineId != null)
			from = from + "LEFT JOIN {h-schema}diary_care_line dcl on (d.id = dcl.diary_id) ";

		return new QueryPart(from);
	}

	public QueryPart where() {
		String whereClause = " dp.id = " + this.departmentId +
				" AND cs.id = " + this.clinicalSpecialtyId +
				" AND d.active = true " +
				" AND d.end_date >= CURRENT_DATE" +
				" AND (d.protected_appointments_percentage > 0) ";

		if (this.institutionId != null)
			whereClause = whereClause + " AND i.id = " + this.institutionId;

		if (this.careLineId != null)
			whereClause = whereClause + " AND dcl.care_line_id = " + this.careLineId + " AND dcl.deleted = false";

		return new QueryPart(whereClause);
	}

	public List<DiaryAvailableProtectedAppointmentsInfoBo> construct(List<Object[]> resultQuery){
		List<DiaryAvailableProtectedAppointmentsInfoBo> result = new ArrayList<>();

		Map<Integer, List<Object[]>> diaries = resultQuery
				.stream()
				.collect(Collectors.groupingBy(
						(Object[] t) -> (Integer)t[0],
						LinkedHashMap::new,
						toList())
				);
		diaries.forEach((id,v) -> {
			Object[] tuple = v.get(0);
			result.add(mapToDiaryAvailableProtectedAppointmentsBo(tuple));
		});
		return result;
	}

	private DiaryAvailableProtectedAppointmentsInfoBo mapToDiaryAvailableProtectedAppointmentsBo(Object[] tuple) {
		int index = 0;
		Integer diaryId = (Integer) tuple[index++];
		Short protectedAppointmentsPercentage = (Short) tuple[index++];
		Short appointmentDuration = (Short) tuple[index++];
		LocalDate startDate = ((Date) tuple[index++]).toLocalDate();
		LocalDate endDate = ((Date) tuple[index++]).toLocalDate();
		Integer institutionId = (Integer) tuple[index++];
		String institutionName = (String) tuple[index++];
		Short departmentId = (Short) tuple[index++];
		String departmentName = (String) tuple[index++];
		String professionalFirstName = (String) tuple[index++];
		String professionalMiddleNames = (String) tuple[index++];
		String professionalLastName = (String) tuple[index++];
		String professionalOtherLastName = (String) tuple[index++];
		Integer clinicalSpecialtyId = (Integer) tuple[index++];
		String clinicalSpecialtyName = (String) tuple[index++];
		String doctorOffice = (String) tuple[index++];
		Boolean isJointDiary = (Boolean) tuple[index++];

		String nameSelfDetermination = null;
		if (includeNameSelfDetermination)
			nameSelfDetermination = (String) tuple[index++];
		String professionalFullName = buildProfessionalFullName(professionalFirstName, professionalMiddleNames,
				professionalLastName, professionalOtherLastName, nameSelfDetermination);
		return new DiaryAvailableProtectedAppointmentsInfoBo(
				diaryId,
				protectedAppointmentsPercentage,
				appointmentDuration,
				startDate,
				endDate,
				new InstitutionBasicInfoBo(institutionId, institutionName),
				new DepartmentBo(departmentId, departmentName),
				professionalFullName,
				new ClinicalSpecialtyBo(clinicalSpecialtyId, clinicalSpecialtyName),
				doctorOffice,
				isJointDiary);
	}

	private String buildProfessionalFullName(String firstName, String middleNames,
											 String lastName, String otherLastName, String nameSelfDetermination) {
		String name = firstName;
		if (includeNameSelfDetermination)
			name = nameSelfDetermination != null ? nameSelfDetermination : firstName;

		return Stream.of(name, middleNames, lastName, otherLastName)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
	}

}