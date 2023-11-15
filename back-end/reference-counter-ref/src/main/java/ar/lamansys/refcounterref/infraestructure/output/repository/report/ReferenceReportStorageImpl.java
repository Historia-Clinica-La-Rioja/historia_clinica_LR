package ar.lamansys.refcounterref.infraestructure.output.repository.report;

import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceReportStorage;

import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferencePriority;

import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import ar.lamansys.refcounterref.domain.report.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.report.ReferenceReportFilterBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceAppointmentStateDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigDecimal;
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

	private static final String SELECT_INFO = "SELECT DISTINCT r.id, r.priority, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, " +
			"pex.name_self_determination, it.description, pe.identification_number, oc.created_on, cs2.name AS clinicalSpecialtyOrigin, " +
			"i.name AS institutionOrigin, cs.name as clinicalSpecialtyDestination, " +
			"cl.description AS careLine, cr.closure_type_id, i2.name AS institutionDestination, s.id AS snomedId, s.sctid, s.pt, ra.appointment_id ";

	private static final String SELECT_COUNT = "SELECT COUNT(DISTINCT r.id) as total ";

	private static final Short ASSIGNED_STATE = 1;

	private static final Short CONFIRMED_STATE = 2;

	private static final Integer NO_VALUE = -1;


	private final EntityManager entityManager;

	private final ReferenceHealthConditionRepository referenceHealthConditionRepository;

	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	private final SharedAppointmentPort sharedAppointmentPort;

	private final SharedPersonPort sharedPersonPort;

	@Override
	public Page<ReferenceReportBo> fetchReferencesReport(ReferenceReportFilterBo filter, Pageable pageable) {
		String condition = addFilter(filter);
		String sqlQueryData = SELECT_INFO + getOutpatientReferenceFromStatement(filter) + condition + " UNION ALL " +
				SELECT_INFO + getOdontologyReferenceFromStatement(filter) + condition;
		String sqlCountQuery = SELECT_COUNT + getOutpatientReferenceFromStatement(filter) + condition + " UNION ALL " +
				SELECT_COUNT + getOdontologyReferenceFromStatement(filter) + condition;
		return executeQueryAndProcessResults(sqlQueryData, sqlCountQuery, filter, pageable);
	}

	private String addFilter(ReferenceReportFilterBo filter) {
		StringBuilder condition = new StringBuilder();
		if (filter.getDestinationInstitutionId() != null)
			condition.append("AND r.destination_institution_id = ").append(filter.getDestinationInstitutionId());

		if (filter.getOriginInstitutionId() != null)
			condition.append(" AND oc.institution_id = ").append(filter.getOriginInstitutionId());

		if (filter.getHealthcareProfessionalId() != null)
			condition.append(" AND oc.doctor_id = ").append(filter.getHealthcareProfessionalId());

		if (filter.getClosureTypeId() != null) {
            if (filter.getClosureTypeId().equals(NO_VALUE)) {
                condition.append(" AND cr.closure_type_id IS null ");
            } else {
                condition.append(" AND cr.closure_type_id = ").append(filter.getClosureTypeId());
            }
        }

		if (filter.getClinicalSpecialtyId() != null)
			condition.append(" AND r.clinical_specialty_id = ").append(filter.getClinicalSpecialtyId());
		if (filter.getPriorityId() != null)
			condition.append(" AND r.priority = ").append(filter.getPriorityId());
		if (filter.getProcedureId() != null)
			condition.append(" AND s.id = ").append(filter.getProcedureId());
		if (filter.getIdentificationNumber() != null)
			condition.append(" AND pe.identification_number = '").append(filter.getIdentificationNumber()).append("' ");
        if (filter.getAppointmentStateId() != null && filter.getAppointmentStateId().equals(NO_VALUE.shortValue()))
			condition.append("AND ra.appointment_id IS null ");
		
		if (filter.getManagerUserId() != null) {
			condition.append(" AND igu.user_id = ").append(filter.getManagerUserId());
			condition.append(" AND igu.deleted IS FALSE ");
			condition.append(" AND igi.deleted IS FALSE ");
		}

		return condition.toString();
	}

	private Page<ReferenceReportBo> executeQueryAndProcessResults(String sqlQueryData, String sqlCountQuery,
																  ReferenceReportFilterBo filter, Pageable pageable) {

		var query = entityManager.createNativeQuery(sqlQueryData)
				.setParameter("from", filter.getFrom())
				.setParameter("to", filter.getTo());

		if (filter.getAppointmentStateId() != null && filter.getAppointmentStateId() != NO_VALUE.shortValue()) {
			List<ReferenceReportBo> result = executeQueryAndSetReferenceDetails(query);

			result = result.stream()
					.filter( r -> {
						Short appointmentId = r.getAppointmentStateId();
						if (appointmentId != null) {
							return filter.getAppointmentStateId().equals(ASSIGNED_STATE)
									? r.getAppointmentStateId().equals(ASSIGNED_STATE) || r.getAppointmentStateId().equals(CONFIRMED_STATE)
									: r.getAppointmentStateId().equals(filter.getAppointmentStateId());
						}
						return false;
					})
					.sorted(Comparator.comparing(ReferenceReportBo::getDate))
					.collect(Collectors.toList());

			return createPage(result, pageable);

		} else {
			query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
			query.setMaxResults(pageable.getPageSize());

			List<ReferenceReportBo> result = executeQueryAndSetReferenceDetails(query);
			result = result.stream()
					.sorted(Comparator.comparing(ReferenceReportBo::getDate))
					.collect(Collectors.toList());

			return createPage(result, pageable, sqlCountQuery, filter);
		}
	}

	private String getOutpatientReferenceFromStatement(ReferenceReportFilterBo filter) {
		return 	"FROM {h-schema}reference r " +
				"LEFT JOIN {h-schema}clinical_specialty cs ON (r.clinical_specialty_id = cs.id) " +
				"JOIN {h-schema}outpatient_consultation oc ON (r.encounter_id = oc.id) " +
				"JOIN {h-schema}institution i ON (oc.institution_id = i.id) " +
				"JOIN {h-schema}clinical_specialty cs2 ON (oc.clinical_specialty_id = cs2.id) " +
				"JOIN {h-schema}patient p ON (oc.patient_id = p.id) " +
				"JOIN {h-schema}person pe ON (p.person_id = pe.id) " +
				"JOIN {h-schema}person_extended pex ON (pe.id = pex.person_id) " +
				"LEFT JOIN {h-schema}reference_appointment ra ON (r.id = ra.reference_id) " +
				(filter.getManagerUserId() != null ?
						"JOIN {h-schema}institutional_group_institution igi ON (igi.institution_id = r.destination_institution_id) " +
						"JOIN {h-schema}institutional_group_user igu ON (igi.institutional_group_id = igu.institutional_group_id) " : "") +
				"LEFT JOIN {h-schema}document d ON (r.service_request_id = d.source_id AND d.type_id = 6)  " +
				"LEFT JOIN {h-schema}document_diagnostic_report ddr ON (d.id = ddr.document_id) " +
				"LEFT JOIN {h-schema}diagnostic_report dr ON (ddr.diagnostic_report_id = dr.id) " +
				"LEFT JOIN {h-schema}snomed s ON (dr.snomed_id = s.id) " +
				"LEFT JOIN {h-schema}institution i2 ON (r.destination_institution_id = i2.id) " +
				"LEFT JOIN {h-schema}identification_type it ON (pe.identification_type_id = it.id) " +
				"LEFT JOIN {h-schema}care_line cl ON (r.care_line_id = cl.id) " +
				"LEFT JOIN {h-schema}counter_reference cr ON (r.id = cr.reference_id) " +
				"WHERE (oc.start_date >= :from AND oc.start_date <= :to) ";
	}

	private String getOdontologyReferenceFromStatement(ReferenceReportFilterBo filter) {
		return 	"FROM {h-schema}reference r " +
				"LEFT JOIN {h-schema}clinical_specialty cs ON (r.clinical_specialty_id = cs.id) " +
				"JOIN {h-schema}odontology_consultation oc ON (r.encounter_id = oc.id) " +
				"JOIN {h-schema}institution i ON (oc.institution_id = i.id)" +
				"JOIN {h-schema}clinical_specialty cs2 ON (oc.clinical_specialty_id = cs2.id) " +
				"JOIN {h-schema}patient p ON (oc.patient_id = p.id) " +
				"JOIN {h-schema}person pe ON (p.person_id = pe.id) " +
				"JOIN {h-schema}person_extended pex ON (pe.id = pex.person_id) " +
				"LEFT JOIN {h-schema}reference_appointment ra ON (r.id = ra.reference_id) " +
				(filter.getManagerUserId() != null ?
						"JOIN {h-schema}institutional_group_institution igi ON (igi.institution_id = r.destination_institution_id) " +
						"JOIN {h-schema}institutional_group_user igu ON (igi.institutional_group_id = igu.institutional_group_id) " : "") +
				"LEFT JOIN {h-schema}document d ON (r.service_request_id = d.source_id AND d.type_id = 6)  " +
				"LEFT JOIN {h-schema}document_diagnostic_report ddr ON (d.id = ddr.document_id) " +
				"LEFT JOIN {h-schema}diagnostic_report dr ON (ddr.diagnostic_report_id = dr.id) " +
				"LEFT JOIN {h-schema}snomed s ON (dr.snomed_id = s.id) " +
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
					.procedure(new SnomedBo((Integer) row[16], (String) row[17],(String) row[18]))
					.build();
			result.add(reference);
		});
		return result;
	}

	private void setReferenceDetails(List<ReferenceReportBo> references) {
		List<Integer> referenceIds = references.stream().map(ReferenceReportBo::getId).collect(Collectors.toList());
		var referencesProblems = referenceHealthConditionRepository.getReferencesProblems(referenceIds);
		Map<Integer, List<Integer>> referenceAppointments = this.referenceAppointmentStorage.getReferenceAppointmentsIds(referenceIds);
		Map<Integer, ReferenceAppointmentStateDto> referencesAppointmentStateData = this.sharedAppointmentPort.getReferencesAppointmentState(referenceAppointments)
				.stream()
				.collect(Collectors.toMap(ReferenceAppointmentStateDto::getReferenceId, Function.identity()));
		references.forEach(ref -> {
			ref.setProblems(referencesProblems.stream().filter(rp -> rp.getReferenceId().equals(ref.getId())).map(rp -> rp.getSnomed().getPt()).collect(Collectors.toList()));
			var appointment = referencesAppointmentStateData.get(ref.getId());
			ref.setAppointmentStateId(appointment != null ? appointment.getAppointmentStateId() : null);
			ref.setPatientFullName(sharedPersonPort.parseCompletePersonName(ref.getPatientFirstName(), ref.getPatientMiddleNames(), ref.getPatientLastName(), ref.getPatientOtherLastNames(), ref.getPatientNameSelfDetermination()));
		});
	}

	private List<ReferenceReportBo> executeQueryAndSetReferenceDetails(Query query) {
		var queryResult = query.getResultList();
		List<ReferenceReportBo> result = mapToReferenceReportBo(queryResult);
		setReferenceDetails(result);
		return result;
	}

	private Page<ReferenceReportBo> createPage(List<ReferenceReportBo> references, Pageable pageable) {
		int totalAmount = references.size();
		int initialIndex = pageable.getPageSize() * pageable.getPageNumber();
		int finalIndex = initialIndex + pageable.getPageSize();
		List<ReferenceReportBo> result = initialIndex < totalAmount
				? references.subList(initialIndex, Math.min(finalIndex, totalAmount))
				: references;
		return new PageImpl<>(result, pageable, totalAmount);
	}

	private Page<ReferenceReportBo> createPage(List<ReferenceReportBo> result, Pageable pageable,
											   String sqlCountQuery, ReferenceReportFilterBo filter) {
		return new PageImpl<>(result, pageable, getTotalAmountOfElements(sqlCountQuery, filter.getFrom(), filter.getTo()));
	}

	private long getTotalAmountOfElements(String sqlCountQuery, LocalDate from, LocalDate to) {
		String query = String.format("SELECT SUM(t.total) FROM ( %s) t", sqlCountQuery);
		return ((BigDecimal) entityManager.createNativeQuery(query)
				.setParameter("from", from)
				.setParameter("to", to)
				.getSingleResult()).longValue();
	}

}
