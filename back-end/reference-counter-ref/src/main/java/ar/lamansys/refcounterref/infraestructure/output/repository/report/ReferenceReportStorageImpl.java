package ar.lamansys.refcounterref.infraestructure.output.repository.report;

import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceReportStorage;

import ar.lamansys.refcounterref.application.port.ReferenceStudyStorage;
import ar.lamansys.refcounterref.domain.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferencePriority;

import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceAppointmentStateDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReferenceReportStorageImpl implements ReferenceReportStorage {

	private final EntityManager entityManager;

	private final ReferenceHealthConditionRepository referenceHealthConditionRepository;

	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	private final ReferenceStudyStorage referenceStudyStorage;

	private final SharedAppointmentPort sharedAppointmentPort;

	private final SharedPersonPort sharedPersonPort;

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

		List<ReferenceReportBo> result = mapToReferenceReportBo(queryResult);
		result = setReferenceDetails(result);

		return result.stream()
				.sorted(Comparator.comparing(ReferenceReportBo::getDate))
				.collect(Collectors.toList());
	}

	private String getOutpatientReferenceQueryFragment() {
		return "SELECT r.id, r.priority, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, " +
				"pex.name_self_determination, it.description, pe.identification_number, oc.created_on , cs2.name AS clinicalSpecialtyOrigin, " +
				"i.name AS institutionOrigin, cs.name AS clinicalSpecialtyDestination, " +
				"cl.description AS careLine, cr.closure_type_id, i2.name AS institutionDestination, r.service_request_id " +
				"FROM {h-schema}reference r " +
				"LEFT JOIN {h-schema}clinical_specialty cs ON (r.clinical_specialty_id = cs.id) " +
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
				"cl.description AS careLine, cr.closure_type_id, i2.name AS institutionDestination, r.service_request_id " +
				"FROM {h-schema}reference r " +
				"LEFT JOIN {h-schema}clinical_specialty cs ON (r.clinical_specialty_id = cs.id) " +
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

	private List<ReferenceReportBo> mapToReferenceReportBo(List<Object[]> queryResult) {
		List<ReferenceReportBo> result = new ArrayList<>();
		queryResult.forEach(row -> {
			ReferenceReportBo reference = ReferenceReportBo.builder()
					.id((Integer) row[0])
					.priority(EReferencePriority.map(((Integer) row[1]).shortValue()))
					.patientFirstName((String) row[2])
					.patientMiddleNames((String) row[3])
					.patientLastName((String) row[4])
					.patientOtherLastNames((String) row[5])
					.patientNameSelfDetermination((String) row[6])
					.identificationType((String) row[7])
					.identificationNumber((String) row[8])
					.date(row[9] != null ? ((Timestamp)row[9]).toLocalDateTime() : null)
					.clinicalSpecialtyOrigin((String) row[10])
					.institutionOrigin((String) row[11])
					.clinicalSpecialtyDestination((String) row[12])
					.careLine((String) row[13])
					.closureType(row[14] != null ? EReferenceClosureType.getById((Short) row[14]) : null)
					.institutionDestination((String) row[15])
					.serviceRequestId((Integer) row[16])
					.build();
			result.add(reference);
		});
		return result;
	}

	private List<ReferenceReportBo> setReferenceDetails(List<ReferenceReportBo> references) {
		List<Integer> referenceIds = references.stream().map(ReferenceReportBo::getId).collect(Collectors.toList());
		var referencesProblems = referenceHealthConditionRepository.getReferencesProblems(referenceIds);
		var referencesStudiesIds = references.stream().filter(r -> r.getServiceRequestId() != null).collect(Collectors.toMap(ReferenceReportBo::getServiceRequestId, ReferenceReportBo::getId));
		Map<Integer, SnomedBo> referencesProcedures = referenceStudyStorage.getReferencesProcedures(referencesStudiesIds);
		Map<Integer, List<Integer>> referenceAppointments = this.referenceAppointmentStorage.getReferenceAppointmentsIds(referenceIds);
		Map<Integer, ReferenceAppointmentStateDto> referencesAppointmentStateData = this.sharedAppointmentPort.getReferencesAppointmentState(referenceAppointments)
				.stream()
				.collect(Collectors.toMap(ReferenceAppointmentStateDto::getReferenceId, Function.identity()));
		return references.stream()
				.peek(ref -> {
					ref.setProblems(referencesProblems.stream()
							.filter(rp -> rp.getReferenceId().equals(ref.getId()))
							.map(rp -> rp.getSnomed().getPt()).collect(Collectors.toList()));
					var procedure = referencesProcedures.get(ref.getId());
					ref.setProcedure(procedure != null ? procedure.getPt() : null);
					var appointment = referencesAppointmentStateData.get(ref.getId());
						ref.setAppointmentStateId(appointment != null ? appointment.getAppointmentStateId() : null);
					ref.setPatientFullName(sharedPersonPort.parseCompletePersonName(ref.getPatientFirstName(), ref.getPatientMiddleNames(), ref.getPatientLastName(), ref.getPatientOtherLastNames(), ref.getPatientNameSelfDetermination()));
				}).collect(Collectors.toList());
	}
}
