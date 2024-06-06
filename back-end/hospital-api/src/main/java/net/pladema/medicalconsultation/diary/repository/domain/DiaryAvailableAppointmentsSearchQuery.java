package net.pladema.medicalconsultation.diary.repository.domain;

import ar.lamansys.sgx.shared.repositories.QueryPart;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.controller.service.domain.DepartmentBo;
import net.pladema.establishment.service.domain.ClinicalSpecialtyBo;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.medicalconsultation.diary.domain.DiaryAppointmentsSearchBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableAppointmentsInfoBo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
@Setter
public class DiaryAvailableAppointmentsSearchQuery {

	private Integer careLineId;
	private List<Integer> clinicalSpecialtyIds;
	private Integer departmentId;
	private Integer institutionId;
	private Boolean includeNameSelfDetermination;
	private Integer practiceId;
	private Boolean regulationProtectedAppointment = false;
	private Integer healthcareProfessionalId;
	private Boolean protectedAppointment = false;
	private Boolean externalBookingAppointment = false;

	public DiaryAvailableAppointmentsSearchQuery(DiaryAppointmentsSearchBo diaryAppointmentsSearchBo) {
		this.careLineId = diaryAppointmentsSearchBo.getCareLineId();
		this.clinicalSpecialtyIds = diaryAppointmentsSearchBo.getClinicalSpecialtyIds();
		this.departmentId = diaryAppointmentsSearchBo.getDepartmentId();
		this.institutionId = diaryAppointmentsSearchBo.getInstitutionId();
		this.includeNameSelfDetermination = diaryAppointmentsSearchBo.getIncludeNameSelfDetermination();
		this.practiceId = diaryAppointmentsSearchBo.getPracticeId();
		this.regulationProtectedAppointment = diaryAppointmentsSearchBo.getRegulationProtectedAppointment();
		this.healthcareProfessionalId = diaryAppointmentsSearchBo.getHealthcareProfessionalId();
		this.externalBookingAppointment = diaryAppointmentsSearchBo.getExternalBookingAppointment();
		this.protectedAppointment = diaryAppointmentsSearchBo.getProtectedAppointment();
	}

	public QueryPart select() {
		String select = "DISTINCT d.id, " +
				"d.appointment_duration, " +
				"d.start_date, " +
				"d.end_date, " +
				"i.id institutionId, "+
				"i.name institutionName, " +
				"dpt.id departmentId, " +
				"dpt.description departmentDescription, " +
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
				"	JOIN {h-schema}doctors_office dof ON (d.doctors_office_id = dof.id) " +
				"	JOIN {h-schema}institution i ON (dof.institution_id = i.id) " +
				" 	JOIN {h-schema}address a ON (i.address_id = a.id) " +
				" 	JOIN {h-schema}city c ON (a.city_id = c.id) " +
				" 	JOIN {h-schema}department dpt ON (c.department_id = dpt.id) " +
				" 	LEFT JOIN {h-schema}clinical_specialty cs ON (d.clinical_specialty_id = cs.id) " +
				" 	JOIN {h-schema}healthcare_professional hp ON (d.healthcare_professional_id = hp.id) " +
				" 	JOIN {h-schema}person p ON (hp.person_id = p.id) " +
				"	LEFT JOIN {h-schema}diary_care_line dcl on (d.id = dcl.diary_id) " +
				"	JOIN {h-schema}diary_opening_hours doh on (d.id = doh.diary_id) ";

		if (includeNameSelfDetermination)
			from = from + "LEFT JOIN {h-schema}person_extended pe ON (pe.person_id = p.id) ";

		if (practiceId != null)
			from = from + "LEFT JOIN {h-schema}diary_practice dp ON (d.id = dp.diary_id) ";

		return new QueryPart(from);
	}

	public QueryPart where() {
		String whereClause = " dpt.id = " + this.departmentId +
				" AND d.active = true " +
				" AND d.end_date >= CURRENT_DATE";

		if (this.careLineId != null)
			whereClause = whereClause + " AND dcl.care_line_id = " + this.careLineId + " AND dcl.deleted = false ";

		if (this.institutionId != null)
			whereClause = whereClause + " AND i.id = " + this.institutionId;

		if (clinicalSpecialtyIds != null && !clinicalSpecialtyIds.isEmpty())
			whereClause = whereClause + " AND cs.id IN " + this.clinicalSpecialtyIds.toString().replaceAll("\\[([^\\[\\]]+)\\]", "($1)");

		if (practiceId != null) {
			whereClause = whereClause + " AND dp.snomed_id = " + this.practiceId +
							"AND (dp.deleted = false OR dp.deleted IS NULL) ";
		}

		if (regulationProtectedAppointment)
			whereClause = whereClause + " AND doh.regulation_protected_appointments_allowed = true";

		if (protectedAppointment)
			whereClause = whereClause + " AND doh.protected_appointments_allowed = true";

		if (externalBookingAppointment)
			whereClause = whereClause + " AND doh.external_appointments_allowed = true";

		if (healthcareProfessionalId != null)
			whereClause = whereClause + " AND (d.healthcare_professional_id = " + this.healthcareProfessionalId + " OR dap.healthcare_professional_id = " + this.healthcareProfessionalId + " ) ";

		return new QueryPart(whereClause);
	}

	public List<DiaryAvailableAppointmentsInfoBo> construct(List<Object[]> resultQuery){
		List<DiaryAvailableAppointmentsInfoBo> result = new ArrayList<>();

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

	private DiaryAvailableAppointmentsInfoBo mapToDiaryAvailableProtectedAppointmentsBo(Object[] tuple) {
		int index = 0;
		Integer diaryId = (Integer) tuple[index++];
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

		ClinicalSpecialtyBo clinicalSpecialtyBo = clinicalSpecialtyId != null ? new ClinicalSpecialtyBo(clinicalSpecialtyId, clinicalSpecialtyName) : null;

		return new DiaryAvailableAppointmentsInfoBo(
				diaryId,
				appointmentDuration,
				startDate,
				endDate,
				new InstitutionBasicInfoBo(institutionId, institutionName),
				new DepartmentBo(departmentId, departmentName),
				professionalFullName,
				clinicalSpecialtyBo,
				doctorOffice,
				isJointDiary
		);
	}

	private String buildProfessionalFullName(String firstName, String middleNames,
											 String lastName, String otherLastName, String nameSelfDetermination) {

		String fullName = lastName;
		if(!(otherLastName == null || otherLastName.isBlank()))
			fullName += " " + otherLastName;
		if(includeNameSelfDetermination && !(nameSelfDetermination == null || nameSelfDetermination.isBlank()))
			fullName += " " + nameSelfDetermination;
		else
			fullName += " " + firstName;
		if(!(includeNameSelfDetermination) && !(middleNames == null || middleNames.isBlank()))
			fullName += " " + middleNames;

		return fullName;
	}
}