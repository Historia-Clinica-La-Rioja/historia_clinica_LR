package net.pladema.staff.repository.domain;


import ar.lamansys.sgx.shared.repositories.QueryPart;
import net.pladema.staff.domain.HealthcareProfessionalSearchBo;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class HealthcareProfessionalSearchQuery {

	private final Integer departmentId;
	private final Integer institutionId;
	private final Integer clinicalSpecialtyId;
	private final Integer practiceId;
	private final String professionalERolIds;
	private static final String FROM =  "	{h-schema}healthcare_professional hp " +
			"	JOIN {h-schema}person p ON hp.person_id = p.id " +
			"	JOIN {h-schema}user_person up ON p.id = up.person_id " +
			"	JOIN {h-schema}user_role ur ON up.user_id = ur.user_id " +
			" 	JOIN {h-schema}institution i ON ur.institution_id = i.id " +
			" 	JOIN {h-schema}address a ON i.address_id = a.id " +
			" 	JOIN {h-schema}city c ON a.city_id = c.id " +
			" 	LEFT JOIN {h-schema}person_extended pe on (p.id = pe.person_id) ";

	public HealthcareProfessionalSearchQuery(HealthcareProfessionalSearchBo healthcareProfessionalSearchBo, List<Short> professionalERolIds){
		this.departmentId = healthcareProfessionalSearchBo.getDepartmentId();
		this.institutionId = healthcareProfessionalSearchBo.getInstitutionId();
		this.clinicalSpecialtyId = healthcareProfessionalSearchBo.getClinicalSpecialtyId();
		this.practiceId = healthcareProfessionalSearchBo.getPracticeId();
		this.professionalERolIds = professionalERolIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
	}

	public QueryPart select() {
		String select = "DISTINCT hp.id AS hp_id, " +
				"hp.license_number, " +
				"p.first_name, " +
				"p.last_name, " +
				"p.identification_number, "+
				"p.id AS person_id, " +
				"pe.name_self_determination, " +
				"p.middle_names, " +
				"p.other_last_names ";

		return new QueryPart(select);
	}

	public QueryPart from() {
		String from = FROM;

		if (clinicalSpecialtyId != null || practiceId != null) {
			from = from + "	LEFT JOIN {h-schema}diary d on (hp.id = d.healthcare_professional_id) ";

			if (clinicalSpecialtyId != null)
				from = from + "LEFT JOIN {h-schema}clinical_specialty cs on (d.clinical_specialty_id = cs.id) ";

			if (practiceId != null)
				from = from + "LEFT JOIN {h-schema}diary_practice dp on (d.id = dp.diary_id) ";
		}

		return new QueryPart(from);
	}

	public QueryPart fromSecondQuery() {
		String from = FROM;

		if (clinicalSpecialtyId != null || practiceId != null) {
			from = from + "	LEFT JOIN {h-schema}diary_associated_professional dap on (hp.id = dap.healthcare_professional_id) "+
					"	LEFT JOIN {h-schema}diary d on (dap.diary_id = d.id) ";

			if (clinicalSpecialtyId != null)
				from = from + "LEFT JOIN {h-schema}clinical_specialty cs on (d.clinical_specialty_id = cs.id) ";

			if (practiceId != null)
				from = from + "LEFT JOIN {h-schema}diary_practice dp on (d.id = dp.diary_id) ";
		}

		return new QueryPart(from);
	}

	public QueryPart where() {

		String whereClause = " c.department_id = " + departmentId + " " + " AND ur.role_id in (" + professionalERolIds + ") " + " AND hp.deleted = false " + " AND ur.deleted = false ";

		if (institutionId != null) whereClause = whereClause + " AND i.id = " + institutionId + " ";

		if (clinicalSpecialtyId != null || practiceId != null) {
			whereClause = whereClause + " AND d.end_date >= CURRENT_DATE ";

			if (clinicalSpecialtyId != null)
				whereClause = whereClause + " AND cs.id = " + clinicalSpecialtyId + " ";

			if (practiceId != null) {
				whereClause = whereClause + " AND dp.snomed_id = " + practiceId + " " + "AND (dp.deleted = false OR dp.deleted IS NULL) ";
			}
		}

		return new QueryPart(whereClause);
	}

	public List<HealthcareProfessionalVo> construct(List<Object[]> resultQuery){
		return resultQuery.stream()
				.map( o -> new HealthcareProfessionalVo(
						(Integer) o[0],
						(String) o[1],
						(String) o[2],
						(String) o[3],
						(String) o[4],
						(Integer) o[5],
						(String) o[6],
						(String) o[7],
						(String) o[8])
				).collect(toList());
	}
}
