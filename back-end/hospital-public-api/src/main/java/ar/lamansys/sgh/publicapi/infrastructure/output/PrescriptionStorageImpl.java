package ar.lamansys.sgh.publicapi.infrastructure.output;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.publicapi.application.port.out.PrescriptionStorage;
import ar.lamansys.sgh.publicapi.domain.prescription.ChangePrescriptionStateBo;
import ar.lamansys.sgh.publicapi.domain.prescription.ChangePrescriptionStateMedicationBo;
import ar.lamansys.sgh.publicapi.domain.prescription.CommercialMedicationBo;
import ar.lamansys.sgh.publicapi.domain.prescription.GenericMedicationBo;
import ar.lamansys.sgh.publicapi.domain.prescription.InstitutionPrescriptionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.LineStatusBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PatientPrescriptionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionLineBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionProblemBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionProfessionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionProfessionalRegistrationBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionValidStatesEnum;
import ar.lamansys.sgh.publicapi.domain.prescription.ProfessionalPrescriptionBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrescriptionStorageImpl implements PrescriptionStorage {

	private final EntityManager entityManager;

	private final static Short RECETA = 5;
	private final static Short RECETA_DIGITAL = 14;
	private final static String CONFIRMADO = "59156000";
	private final static String ACTIVO = "55561003";
	private final static String COMPLETO = "255594003";
	private final static String CRONICO = "-55607006";

	private static final String ID_DIVIDER = "-";

	private final MedicationStatementCommercialRepository medicationStatementCommercialRepository;

	@Override
	public Optional<PrescriptionBo> getPrescriptionByIdAndDni(String prescriptionId, String identificationNumber) {
		String domainNumber = prescriptionId.split(ID_DIVIDER)[0];
		Integer numericPrescriptionId = Integer.valueOf(prescriptionId.split(ID_DIVIDER)[1]);
		String stringQuery = "select mr.id as mrid, ms.prescription_date, ms.due_date, " +
		"p2.first_name as p2fn, p2.last_name as p2ln, pe.name_self_determination, g.description as gd, spg.description as spgd, p2.birth_date, it.description as itd, p2.identification_number, " +
		"mc.name as mcn, mc.cuit, mcp.plan, pmc.affiliate_number, i.name, i.sisa_code, i.province_code, " +
		"CONCAT(a.street, ' ', a.number, ' ', case WHEN a.floor is not null THEN CONCAT('Piso ', a.floor) else '' END)," +
		"p3.first_name as p3fn, p3.last_name, it2.description as it2d, p3.identification_number as p3d, pe2.phone_number, pe2.email as EMAIL, ps.description as psd, " +
		"ps.sctid_code, " +
		"pln.license_number, case when pln.type_license_number = 1 then 'NACIONAL' else 'PROVINCIAL' end, ms.prescription_line_number as msid, msls.description as mssd, s.pt as spt, s.sctid as sid, " +
		"pt.description as ptd, s2.pt as s2pt, s2.sctid as s2id, " +
		"d2.doses_by_unit as unit_dose, d2.doses_by_day, d2.duration, '' as presentation, 0 as presentation_quantity " +
		"from medication_statement ms join document_medicamention_statement dms on ms.id = dms.medication_statement_id " +
		"join document d on d.id = dms.document_id " +
		"join medication_request mr on mr.id = d.source_id join patient p on p.id = ms.patient_id " +
		"join person p2 on p2.id = p.person_id " +
		"left join person_extended pe on pe.person_id = p2.id " +
		"join gender g on g.id = p2.gender_id " +
		"left join self_perceived_gender spg on spg.id = pe.gender_self_determination " +
		"join identification_type it on it.id = p2.identification_type_id " +
		"left join patient_medical_coverage pmc on mr.medical_coverage_id = pmc.id "+
		"left join medical_coverage mc on mc.id = pmc.medical_coverage_id " +
		"left join medical_coverage_plan mcp on mcp.medical_coverage_id = pmc.medical_coverage_id " +
		"left join institution i on i.id = d.institution_id " +
		"left join address a on a.id = i.address_id " +
		"join healthcare_professional hp on hp.id = mr.doctor_id " +
		"join person p3 on p3.id = hp.person_id " +
		"join identification_type it2 on it2.id = p3.identification_type_id " +
		"left join person_extended pe2 on pe2.person_id = p3.id " +
		"join professional_professions pp on pp.healthcare_professional_id = hp.id " +
		"join professional_specialty ps on ps.id = pp.professional_specialty_id " +
		"join professional_license_numbers pln on pln.professional_profession_id = pp.id " +
		"join health_condition hc on hc.id = ms.health_condition_id " +
		"join snomed s on s.id = hc.snomed_id " +
		"join problem_type pt on pt.id = hc.problem_id " +
		"join snomed s2 on ms.snomed_id = s2.id " +
		"left join dosage d2 on d2.id = ms.dosage_id " +
		"left join medication_statement_line_state msls on msls.id = ms.prescription_line_state " +
		"where p2.identification_number LIKE :identificationNumber " +
		"and mr.id = :numericPrescriptionId " +
		"and (d.type_id = " + RECETA + " or d.type_id = " + RECETA_DIGITAL + ") and hc.verification_status_id LIKE CAST(" + CONFIRMADO + "AS VARCHAR) " +
				"and (ms.status_id LIKE CAST(" + COMPLETO + "AS varchar) OR ms.status_id LIKE CAST(" + ACTIVO + "AS varchar)) " +
		"order by mr.id desc";

		Query query = entityManager.createNativeQuery(stringQuery)
				.setParameter("identificationNumber", identificationNumber)
				.setParameter("numericPrescriptionId", numericPrescriptionId);

		List<Object[]> queryResult = query.getResultList();
		var result = queryResult.stream()
				.map(this::processPrescriptionQuery)
				.collect(Collectors.toList());
		PrescriptionBo mergedResult = mergeResults(result);
		if(mergedResult.getPrescriptionId() != null) {
			mergedResult.setPrescriptionId(domainNumber + ID_DIVIDER + mergedResult.getPrescriptionId());
		}
		mergedResult.setDomain(domainNumber);
		return Optional.of(mergedResult);
	}

	@Override
	@Transactional
	@Modifying
	public void changePrescriptionState(ChangePrescriptionStateBo changePrescriptionLineStateBo, String prescriptionId, String identificationNumber) {

		Integer prescriptionIdInt = Integer.valueOf(changePrescriptionLineStateBo.getPrescriptionId().split(ID_DIVIDER)[1]);
		List<Integer> prescriptionLineNumbers = changePrescriptionLineStateBo.getChangePrescriptionStateLineMedicationList()
				.stream()
				.map(ChangePrescriptionStateMedicationBo::getPrescriptionLine)
				.collect(Collectors.toList());

		List<LineStatusBo> newStatus = changePrescriptionLineStateBo.getChangePrescriptionStateLineMedicationList()
				.stream()
				.map(cpb -> new LineStatusBo(null, cpb.getPrescriptionLine(), cpb.getPrescriptionStateId(), cpb.getDispensedMedicationBo().getPharmacyName()))
				.collect(Collectors.toList());

		List<LineStatusBo> linesStatus = getLineStatus(prescriptionIdInt, identificationNumber);

		assertAllLinesExists(linesStatus, newStatus);

		var actualLinesStatus = linesStatus.stream()
				.filter(ls -> isInNewStatus(ls.getPrescriptionLineNumber(), newStatus))
				.collect(Collectors.toList());

		assertValidStatusChanges(actualLinesStatus, newStatus);

		var entities = changePrescriptionLineStateBo.getChangePrescriptionStateLineMedicationList()
				.stream()
				.map(medicationBo -> mapTo(medicationBo, actualLinesStatus))
				.collect(Collectors.toList());

		changeMedicationsStatement(prescriptionLineNumbers, newStatus, prescriptionIdInt);
		medicationStatementCommercialRepository.deleteAllInBatch(entities);
		medicationStatementCommercialRepository.saveAll(entities);
		medicationStatementCommercialRepository.flush();
	}

	private boolean isInNewStatus(Integer prescriptionLineNumber, List<LineStatusBo> newStatus) {
		return newStatus.stream().anyMatch(ns -> ns.getPrescriptionLineNumber().equals(prescriptionLineNumber));
	}

	private void assertValidStatusChanges(List<LineStatusBo> linesStatus, List<LineStatusBo> newStatus) {
		boolean valid;
		boolean validPharmacyName = true;
		int i = 0;
		String lastStatusStr = "";
		String lastNewStatusStr = "";

		while (i < linesStatus.size()) {
			LineStatusBo oldS = linesStatus.get(i);
			LineStatusBo newS = findCorrespondingStatus(newStatus, oldS.getPrescriptionLineNumber());
			valid = PrescriptionValidStatesEnum.isValidTransition(
					oldS.getPrescriptionLineState(),
					newS.getPrescriptionLineState()
			);

			lastStatusStr = PrescriptionValidStatesEnum.map(oldS.getPrescriptionLineState()).toString();
			lastNewStatusStr = PrescriptionValidStatesEnum.map(newS.getPrescriptionLineState()).toString();
			if(PrescriptionValidStatesEnum.map(newS.getPrescriptionLineState()).equals(PrescriptionValidStatesEnum.CANCELADO)) {
				Integer id = getMedicationStamentId(linesStatus, newS.getPrescriptionLineNumber());
				if(id != null) {
					var medication = medicationStatementCommercialRepository.findById(id);
					if (medication.isPresent() && medication.get().getPharmacyName() != null && !medication.get().getPharmacyName().equals(newS.getPharmacyName())) {
						validPharmacyName = false;
					}
				}
			}

			if(!validPharmacyName) {
				throw new ConstraintViolationException(String.format("Para cancelar el renglón %s se debe mantener el mismo nombre de farmacia", linesStatus.get(i).getPrescriptionLineNumber()), Collections.emptySet());
			}
			if(!valid) {
				throw new ConstraintViolationException(String.format("El renglón %s no puede cambiar del estado %s al estado %s", linesStatus.get(i).getPrescriptionLineNumber(), lastStatusStr, lastNewStatusStr), Collections.emptySet());
			}

			i++;
		}
	}

	private LineStatusBo findCorrespondingStatus(List<LineStatusBo> newStatus, Integer prescriptionLineNumber) {
		return newStatus.stream()
				.filter(ls -> ls.getPrescriptionLineNumber().equals(prescriptionLineNumber))
				.findFirst().orElse(null);
	}

	private Integer getMedicationStamentId(List<LineStatusBo> linesStatus, Integer prescriptionLineNumber) {
		return linesStatus.stream()
				.filter(ls -> ls.getPrescriptionLineNumber().equals(prescriptionLineNumber))
				.map(LineStatusBo::getMedicationStatementId)
				.findFirst().orElse(-1);
	}

	private void assertAllLinesExists(List<LineStatusBo> linesStatus, List<LineStatusBo> newStatus) {

		var foundLines = linesStatus.stream()
				.map(LineStatusBo::getPrescriptionLineNumber)
				.collect(Collectors.toSet());

		var unexistentLines = newStatus.stream()
				.map(LineStatusBo::getPrescriptionLineNumber)
				.filter(ns -> !foundLines.contains(ns))
				.collect(Collectors.toList());

		if(unexistentLines.isEmpty()) {
			return;
		}

		if(unexistentLines.size() == 1) {
			throw new ConstraintViolationException("El renglón " + unexistentLines.get(0) + " no existe en la receta indicada", Collections.emptySet());
		} else {
			StringBuilder message = new StringBuilder().append("Los renglones ");
			for (int i = 0; i < unexistentLines.size(); i++) {
				message.append(unexistentLines.get(i));
				if(i == unexistentLines.size() - 2)
					message.append(" y ");
				else if(i < unexistentLines.size() - 1)
						message.append(", ");
			}
			message.append(" no existen en la receta indicada.");
			throw  new ConstraintViolationException(message.toString(), Collections.emptySet());
		}


	}

	private void changeMedicationsStatement(List<Integer> prescriptionLineNumbers, List<LineStatusBo> newStatus, Integer prescriptionId) {

		String updateQuery = "UPDATE medication_statement " +
				"SET prescription_line_state = :status " +
				"WHERE id = ( " +
					"SELECT ms2.id " +
					"FROM medication_statement ms2 " +
					"JOIN document_medicamention_statement dms ON ms2.id = dms.medication_statement_id " +
					"JOIN document d on d.id = dms.document_id " +
					"JOIN medication_request mr ON mr.id = d.source_id " +
					"WHERE mr.id = :prescriptionId and (d.type_id = " + RECETA + " or d.type_id = " + RECETA_DIGITAL + ") AND ms2.prescription_line_number = :lineNumber " +
				")";

		Query query = entityManager.createNativeQuery(updateQuery);
		for(int i = 0; i < prescriptionLineNumbers.size(); i++) {
			var stateId = newStatus.get(i).getPrescriptionLineState();
			var state = PrescriptionValidStatesEnum.map(stateId);
			var stateToSave = state.equals(PrescriptionValidStatesEnum.CANCELADO) ?
					1 : stateId;
			query.setParameter("status", stateToSave)
					.setParameter("prescriptionId", prescriptionId)
					.setParameter("lineNumber", prescriptionLineNumbers.get(i))
					.executeUpdate();
			entityManager.flush();
			entityManager.clear();
		}
	}

	private List<LineStatusBo> getLineStatus(Integer prescriptionId, String idNumber) {
		String getQuery = "select ms.id, ms.prescription_line_number, ms.prescription_line_state, pp.identification_number, msc.pharmacy_name " +
				"from medication_statement ms " +
				"join document_medicamention_statement dms on ms.id = dms.medication_statement_id " +
				"join document d on d.id = dms.document_id " +
				"join medication_request mr on mr.id = d.source_id " +
				"join patient p on p.id = d.patient_id " +
				"join person pp on pp.id = p.person_id " +
				"left join medication_statement_commercial msc on msc.id = ms.id " +
				"where mr.id = :prescriptionId and (d.type_id = " + RECETA + " or d.type_id = " + RECETA_DIGITAL +")";

		List<Object[]> queryResult = entityManager.createNativeQuery(getQuery)
				.setParameter("prescriptionId", prescriptionId)
				.getResultList();

		assertExistsPrescriptionAndDni(queryResult, idNumber);

		return queryResult.stream()
				.map(this::mapTo)
				.collect(Collectors.toList());

	}

	private void assertExistsPrescriptionAndDni(List<Object[]> queryResult, String idNumber) {
		if(queryResult.isEmpty() || !queryResult.get(0)[3].toString().equals(idNumber)) {
			throw new ConstraintViolationException("La receta y/o el dni suministrados no existen", Collections.emptySet());
		}
	}

	private LineStatusBo mapTo(Object[] line) {
		return LineStatusBo.builder()
				.medicationStatementId((Integer)line[0])
				.prescriptionLineNumber((Integer)line[1])
				.prescriptionLineState((Short)line[2])
				.pharmacyName((String)line[4])
				.build();
	}

	private MedicationStatementCommercial mapTo(ChangePrescriptionStateMedicationBo changePrescriptionStateMedicationBo, List<LineStatusBo> lineStatusBoList) {

		var lineNumberId = lineStatusBoList.stream()
				.filter(line -> line.getPrescriptionLineNumber().equals(changePrescriptionStateMedicationBo.getPrescriptionLine()))
				.map(LineStatusBo::getMedicationStatementId)
				.findFirst().orElse(-1);

		return new MedicationStatementCommercial(
				lineNumberId,
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getSnomedId(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getCommercialName(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getCommercialPresentation(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getSoldUnits(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getBrand(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getPrice(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getAffiliatePayment(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getMedicalCoveragePayment(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getPharmacyName(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getPharmacistName(),
				changePrescriptionStateMedicationBo.getDispensedMedicationBo().getObservations()
		);
	}

	private PrescriptionBo mergeResults(List<PrescriptionBo> unmergedResults) {

		PrescriptionBo result = new PrescriptionBo();
		if(unmergedResults.isEmpty()) {
			return result;
		}

		result.setDomain(unmergedResults.get(0).getDomain());
		result.setPrescriptionId(unmergedResults.get(0).getPrescriptionId());
		result.setPrescriptionDate(unmergedResults.get(0).getPrescriptionDate());
		result.setDueDate(unmergedResults.get(0).getDueDate());
		result.setPatientPrescriptionBo(unmergedResults.get(0).getPatientPrescriptionBo());
		result.setInstitutionPrescriptionBo(unmergedResults.get(0).getInstitutionPrescriptionBo());
		result.setPrescriptionsLineBo(unmergedResults.get(0).getPrescriptionsLineBo());
		ProfessionalPrescriptionBo professionalPrescriptionBo = unmergedResults.get(0).getProfessionalPrescriptionBo();
		List<PrescriptionProfessionBo> prescriptionProfessionBos = new ArrayList<>(professionalPrescriptionBo.getSpecialties());
		List<PrescriptionProfessionalRegistrationBo> prescriptionProfessionalRegistrationBos = new ArrayList<>(professionalPrescriptionBo.getRegistrations());
		List<PrescriptionLineBo> prescriptionLineBoList = new ArrayList<>(result.getPrescriptionsLineBo());

		for(int i = 1; i < unmergedResults.size(); i++) {
			var specialty = unmergedResults.get(i).getProfessionalPrescriptionBo().getSpecialties().get(0);
			var prescriptionRegistration = unmergedResults.get(i).getProfessionalPrescriptionBo().getRegistrations().get(0);
			if(!prescriptionProfessionalRegistrationBos.contains(prescriptionRegistration)) {
				prescriptionProfessionalRegistrationBos.add(prescriptionRegistration);
			}
			if(!prescriptionProfessionBos.contains(specialty)) {
				prescriptionProfessionBos.add(specialty);
			}
			if(!prescriptionLineBoList.contains(unmergedResults.get(i).getPrescriptionsLineBo().get(0))) {
				prescriptionLineBoList.add(unmergedResults.get(i).getPrescriptionsLineBo().get(0));
			}
		}
		professionalPrescriptionBo.setSpecialties(prescriptionProfessionBos);
		professionalPrescriptionBo.setRegistrations(prescriptionProfessionalRegistrationBos);
		result.setProfessionalPrescriptionBo(professionalPrescriptionBo);
		result.setPrescriptionsLineBo(prescriptionLineBoList);
		return result;
	}

	private PrescriptionBo processPrescriptionQuery(Object[] queryResult) {

		var dueDate = queryResult[2] != null ?
				((Date)queryResult[2]).toLocalDate() : ((Date)queryResult[1]).toLocalDate().plusDays(30);

		return new PrescriptionBo(
				"1",
				((Integer)queryResult[0]).toString(),
				((Date)queryResult[1]).toLocalDate().atStartOfDay(),
				dueDate.atStartOfDay(),
				new PatientPrescriptionBo(
						(String)queryResult[3],
						(String)queryResult[4],
						(String)queryResult[5],
						(String)queryResult[6],
						(String)queryResult[7],
						((Date)queryResult[8]).toLocalDate(),
						(String)queryResult[9],
						(String)queryResult[10],
						(String)queryResult[11],
						(String)queryResult[12],
						(String)queryResult[13],
						(String)queryResult[14]
				),
				new InstitutionPrescriptionBo(
						(String)queryResult[15],
						(String)queryResult[16],
						(String)queryResult[17],
						(String)queryResult[18]
				),
				new ProfessionalPrescriptionBo(
						(String)queryResult[19],
						(String)queryResult[20],
						(String)queryResult[21],
						(String)queryResult[22],
						(String)queryResult[23],
						(String)queryResult[24],
						List.of(new PrescriptionProfessionBo(
								(String)queryResult[25],
								(String)queryResult[26]
						)),
						List.of(new PrescriptionProfessionalRegistrationBo(
								(String)queryResult[27],
								(String)queryResult[28]
						))
				),
				List.of(new PrescriptionLineBo(
						(Integer)queryResult[29],
						dueDate.isBefore(LocalDate.now()) ? "VENCIDO" : (String)queryResult[30],
						new PrescriptionProblemBo(
								(String)queryResult[31],
								(String)queryResult[32],
								queryResult[33].equals(CRONICO) ? "Crónico" : "Agudo"
						),
						new GenericMedicationBo(
								(String)queryResult[34],
								(String)queryResult[35]
						),
						new CommercialMedicationBo(),
						queryResult[36] != null ? (Double)queryResult[36] : 0,
						queryResult[37] != null ? (Double) queryResult[37] : 0,
						queryResult[38] != null ? (Double)queryResult[38] : 1,
						(String)queryResult[39],
						(Integer)queryResult[40]
				))
		);
	}
}
