package ar.lamansys.refcounterref.infraestructure.output.repository.report;

import ar.lamansys.refcounterref.application.port.ReferenceReportStorage;

import ar.lamansys.refcounterref.domain.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferencePriority;

import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionRepository;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReferenceReportStorageImpl implements ReferenceReportStorage {

	private final EntityManager entityManager;

	private final FeatureFlagsService featureFlagsService;

	private final ReferenceHealthConditionRepository referenceHealthConditionRepository;

	@Override
	public List<ReferenceReportBo> fetchReceivedReferencesReport(Integer institutionId, LocalDate from, LocalDate to) {
		log.debug("Fetch received references at the institution {} from {} to {}", institutionId, from, to);
		String condition = "AND r.destination_institution_id = " + institutionId;
		String sqlString = getOutpatientReferenceQueryFragment() + condition + " UNION ALL " + getOdontologyReferenceQueryFragment() + condition;
		return executeQueryAndProcessResults(sqlString, from, to);
	}

	@Override
	public List<ReferenceReportBo> fetchRequestedReferencesReport(Integer institutionId, Integer healthcareProfessionalId,
																  LocalDate from, LocalDate to) {
		log.debug("Fetch requested references at the institution {} from {} to {}", institutionId, from, to);
		String condition = "AND oc.institution_id = " + institutionId;
		if (healthcareProfessionalId != null)
			condition += " AND oc.doctor_id = " + healthcareProfessionalId;
		String sqlString = getOutpatientReferenceQueryFragment() + condition + " UNION ALL " + getOdontologyReferenceQueryFragment() + condition;
		return executeQueryAndProcessResults(sqlString, from, to);
	}

	private List<ReferenceReportBo> executeQueryAndProcessResults(String sqlString, LocalDate from, LocalDate to) {
		var query = entityManager.createNativeQuery(sqlString)
				.setParameter("from", from)
				.setParameter("to", to);

		var queryResult = query.getResultList();
		boolean includeNameSelfDetermination = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);

		List<ReferenceReportBo> result = mapToReferenceReportBo(queryResult, includeNameSelfDetermination);
		result = setReferenceProblems(result);

		return result.stream()
				.sorted(Comparator.comparing(ReferenceReportBo::getDate))
				.collect(Collectors.toList());
	}

	private String getOutpatientReferenceQueryFragment() {
		return "SELECT r.id, r.priority, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, " +
				"pex.name_self_determination, it.description, pe.identification_number, oc.created_on , cs2.name AS clinicalSpecialtyOrigin, " +
				"i.name AS institutionOrigin, cs.name AS clinicalSpecialtyDestination, " +
				"cl.description AS careLine, cr.closure_type_id, i2.name AS institutionDestination " +
				"FROM {h-schema}reference r " +
				"JOIN {h-schema}clinical_specialty cs ON (r.clinical_specialty_id = cs.id) " +
				"JOIN {h-schema}outpatient_consultation oc ON (r.encounter_id = oc.id) " +
				"JOIN {h-schema}institution i ON (oc.institution_id = i.id) " +
				"JOIN {h-schema}clinical_specialty cs2 ON (oc.clinical_specialty_id = cs2.id) " +
				"JOIN {h-schema}patient p ON (oc.patient_id = p.id) " +
				"JOIN {h-schema}person pe ON (p.person_id = pe.id) " +
				"JOIN {h-schema}person_extended pex ON (pe.id = pex.person_id) " +
				"LEFT JOIN {h-schema}institution i2 ON (r.destination_institution_id = i2.id) " +
				"LEFT JOIN {h-schema}identification_type it ON (pe.identification_type_id = it.id) " +
				"LEFT JOIN {h-schema}care_line cl ON (r.care_line_id = cl.id) " +
				"LEFT JOIN {h-schema}counter_reference cr ON (r.id = cr.reference_id) " +
				"WHERE (oc.start_date >= :from AND oc.start_date <= :to) ";
	}

	private String getOdontologyReferenceQueryFragment() {
		return "SELECT r.id, r.priority, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, " +
				"pex.name_self_determination, it.description, pe.identification_number, oc.created_on, cs2.name AS clinicalSpecialtyOrigin, " +
				"i.name AS institutionOrigin, cs.name as clinicalSpecialtyDestination, " +
				"cl.description AS careLine, cr.closure_type_id, i2.name AS institutionDestination " +
				"FROM {h-schema}reference r " +
				"JOIN {h-schema}clinical_specialty cs ON (r.clinical_specialty_id = cs.id) " +
				"JOIN {h-schema}odontology_consultation oc ON (r.encounter_id = oc.id) " +
				"JOIN {h-schema}institution i ON (oc.institution_id = i.id)" +
				"JOIN {h-schema}clinical_specialty cs2 ON (oc.clinical_specialty_id = cs2.id) " +
				"JOIN {h-schema}patient p ON (oc.patient_id = p.id) " +
				"JOIN {h-schema}person pe ON (p.person_id = pe.id) " +
				"JOIN {h-schema}person_extended pex ON (pe.id = pex.person_id) " +
				"LEFT JOIN {h-schema}institution i2 ON (r.destination_institution_id = i2.id) " +
				"LEFT JOIN {h-schema}identification_type it ON (pe.identification_type_id = it.id) " +
				"LEFT JOIN {h-schema}care_line cl ON (r.care_line_id = cl.id) " +
				"LEFT JOIN {h-schema}counter_reference cr ON (r.id = cr.reference_id) " +
				"WHERE (oc.performed_date >= :from AND oc.performed_date <= :to) ";
	}

	private List<ReferenceReportBo> mapToReferenceReportBo(List<Object[]> queryResult, boolean includeNameSelfDetermination) {
		List<ReferenceReportBo> result = new ArrayList<>();
		queryResult.forEach(row -> {
			ReferenceReportBo reference = ReferenceReportBo.builder()
					.referenceId((Integer) row[0])
					.priority(EReferencePriority.map(((Integer) row[1]).shortValue()))
					.patientFullName(getPatientFullName(includeNameSelfDetermination,
							(String) row[2],
							(String) row[3],
							(String) row[4],
							(String) row[5],
							(String) row[6]))
					.identificationType((String) row[7])
					.identificationNumber((String) row[8])
					.date(row[9] != null ? ((Timestamp)row[9]).toLocalDateTime() : null)
					.clinicalSpecialtyOrigin((String) row[10])
					.institutionOrigin((String) row[11])
					.clinicalSpecialtyDestination((String) row[12])
					.careLine((String) row[13])
					.closureType(row[14] != null ? EReferenceClosureType.getById((Short) row[14]) : null)
					.institutionDestination((String) row[15])
					.build();
			result.add(reference);
		});
		return result;
	}

	public String getPatientFullName(boolean includeNameSelfDetermination, String firstName,
									 String middleNames, String lastName, String otherLastNames,
									 String nameSelfDetermination) {
		String name = null;
		if (includeNameSelfDetermination && !(nameSelfDetermination == null || nameSelfDetermination.isBlank())) {
			name = nameSelfDetermination;
			middleNames = null;
		} else name = firstName;

		return Stream.of(name, middleNames, lastName, otherLastNames)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
	}

	private List<ReferenceReportBo> setReferenceProblems(List<ReferenceReportBo> result) {
		List<Integer> referenceIds = result.stream().map(ReferenceReportBo::getReferenceId).collect(Collectors.toList());
		List<ReferenceProblemBo> problems = this.referenceHealthConditionRepository.getReferencesProblems(referenceIds);
		return result.stream().peek(r -> {
			List<String> problemsSctid = problems.stream()
					.filter(p -> p.getReferenceId().equals(r.getReferenceId()))
					.collect(Collectors.toList())
					.stream()
					.map(ps -> ps.getSnomed().getPt())
					.collect(Collectors.toList());
			r.setProblems(problemsSctid);
		}).collect(Collectors.toList());
	}

}
