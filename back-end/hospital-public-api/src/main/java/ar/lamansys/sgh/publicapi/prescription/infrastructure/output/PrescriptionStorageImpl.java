package ar.lamansys.sgh.publicapi.prescription.infrastructure.output;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

import ar.lamansys.sgh.publicapi.prescription.domain.MultipleCommercialPrescriptionBo;

import ar.lamansys.sgh.publicapi.prescription.domain.MultipleCommercialPrescriptionLineBo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.publicapi.infrastructure.output.MedicationStatementCommercial;
import ar.lamansys.sgh.publicapi.infrastructure.output.MedicationStatementCommercialRepository;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionStorage;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.CommercialMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.GenericMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.InstitutionPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.LineStatusBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PatientPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionLineBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProblemBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProfessionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProfessionalRegistrationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionSpecialtyBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionValidStatesEnum;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionsDataBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ProfessionalPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgx.shared.token.JWTUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrescriptionStorageImpl implements PrescriptionStorage {

	private final EntityManager entityManager;

	private final static Short RECETA = 5;
	private final static Short RECETA_DIGITAL = 14;
	private final static Short RECETA_CANCELADA = 6;
	private final static String CONFIRMADO = "59156000";
	private final static String ACTIVO = "55561003";
	private final static String COMPLETO = "255594003";
	private final static String CRONICO = "-55607006";

	private static final String ID_DIVIDER = "-";

	private final String secret;

	private final Duration tokenExpiration;

	private final Integer domainNumber;

	private final MedicationStatementCommercialRepository medicationStatementCommercialRepository;

	public PrescriptionStorageImpl(EntityManager entityManager, @Value("${token.secret}") String secret,
								   @Value("${prescription.token.duration}") Duration tokenExpiration,
								   @Value("${prescription.domain.number}") Integer domainNumber,
								   MedicationStatementCommercialRepository medicationStatementCommercialRepository) {
		this.entityManager = entityManager;

		this.secret = secret;
		this.tokenExpiration = tokenExpiration;
		this.medicationStatementCommercialRepository = medicationStatementCommercialRepository;
		this.domainNumber = domainNumber;
	}

	@Override
	public Optional<PrescriptionBo> getPrescriptionByIdAndDni(PrescriptionIdentifier prescriptionIdentifier, String identificationNumber) {

		String stringQuery = "select mr.id as mrid, ms.prescription_date, ms.due_date, " +
		"p2.first_name as p2fn, p2.last_name as p2ln, pe.name_self_determination, g.description as gd, spg.description as spgd, p2.birth_date, it.description as itd, p2.identification_number, " +
		"mc.name as mcn, mc.cuit, mcp.plan, pmc.affiliate_number, i.name, i.sisa_code, i.province_code, " +
		"CONCAT(a.street, ' ', a.number, ' ', case WHEN a.floor is not null THEN CONCAT('Piso ', a.floor) else '' END)," +
		"p3.first_name as p3fn, p3.last_name, it2.description as it2d, p3.identification_number as p3d, pe2.phone_number, pe2.email as EMAIL, ps.description as psd, " +
		"ps.sctid_code as psc, " +
		"pln.license_number, case when pln.type_license_number = 1 then 'NACIONAL' else 'PROVINCIAL' end, ms.prescription_line_number as msid, msls.description as mssd, s.pt as spt, s.sctid as sid, " +
		"pt.description as ptd, s2.pt as s2pt, s2.sctid as s2id, " +
		"d2.doses_by_unit as unit_dose, d2.doses_by_day, d2.duration, '' as presentation, 0 as presentation_quantity, d.id, mr.is_archived, " +
		"case when d2.dose_quantity_id is null then null else q.value end, " +
		"msls.id as status_id " +
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
		"join healthcare_professional_specialty hps on hps.professional_profession_id = pp.id " +
		"join professional_specialty ps on ps.id = pp.professional_specialty_id " +
		"join professional_license_numbers pln on (pln.professional_profession_id = pp.id or pln.healthcare_professional_specialty_id = hps.id)" +
		"join health_condition hc on hc.id = ms.health_condition_id " +
		"join snomed s on s.id = hc.snomed_id " +
		"join problem_type pt on pt.id = hc.problem_id " +
		"join snomed s2 on ms.snomed_id = s2.id " +
		"left join dosage d2 on d2.id = ms.dosage_id " +
		"left join quantity q on d2.dose_quantity_id = q.id " +
		"left join medication_statement_line_state msls on msls.id = ms.prescription_line_state " +
		"where p2.identification_number LIKE :identificationNumber " +
		"and mr.id = :numericPrescriptionId " +
		"and (d.type_id = " + RECETA + " or d.type_id = " + RECETA_DIGITAL + ") and hc.verification_status_id LIKE CAST(" + CONFIRMADO + "AS VARCHAR) " +
				"and (ms.status_id LIKE CAST(" + COMPLETO + "AS varchar) OR ms.status_id LIKE CAST(" + ACTIVO + "AS varchar)) " +
		"order by mr.id desc";

		Query query = entityManager.createNativeQuery(stringQuery)
				.setParameter("identificationNumber", identificationNumber)
				.setParameter("numericPrescriptionId", prescriptionIdentifier.prescriptionId);

		List<Object[]> queryResult = query.getResultList();
		var result = queryResult.stream()
				.map(this::processPrescriptionQuery)
				.collect(Collectors.toList());
		PrescriptionBo mergedResult = mergeResults(result);
		if(mergedResult.getPrescriptionId() != null) {
			mergedResult.setPrescriptionId(domainNumber + ID_DIVIDER + mergedResult.getPrescriptionId());
		}
		mergedResult.setDomain(prescriptionIdentifier.domain);
		return Optional.of(mergedResult);
	}

	@Override
	@Transactional
	@Modifying
	public void changePrescriptionState(ChangePrescriptionStateBo changePrescriptionLineStateBo, PrescriptionIdentifier prescriptionIdentifier, String identificationNumber) throws PrescriptionNotFoundException {

		List<Integer> prescriptionLineNumbers = changePrescriptionLineStateBo.getChangePrescriptionStateLineMedicationList()
				.stream()
				.map(ChangePrescriptionStateMedicationBo::getPrescriptionLine)
				.collect(Collectors.toList());

		List<LineStatusBo> newStatus = changePrescriptionLineStateBo.getChangePrescriptionStateLineMedicationList()
				.stream()
				.map(cpb -> new LineStatusBo(null, cpb.getPrescriptionLine(), cpb.getPrescriptionStateId(), cpb.getDispensedMedicationBo().getPharmacyName()))
				.collect(Collectors.toList());

		List<LineStatusBo> linesStatus = getLineStatus(prescriptionIdentifier.prescriptionId, identificationNumber);

		assertAllLinesExists(linesStatus, newStatus);

		var actualLinesStatus = linesStatus.stream()
				.filter(ls -> isInNewStatus(ls.getPrescriptionLineNumber(), newStatus))
				.collect(Collectors.toList());

		assertValidStatusChanges(actualLinesStatus, newStatus);

		var entities = changePrescriptionLineStateBo.getChangePrescriptionStateLineMedicationList()
				.stream()
				.map(medicationBo -> mapTo(medicationBo, actualLinesStatus))
				.collect(Collectors.toList());

		changeMedicationsStatement(prescriptionLineNumbers, newStatus, prescriptionIdentifier.prescriptionId);
		Set<Integer> medicationStatementIds = entities.stream().map(MedicationStatementCommercial::getMedicationStatementId).collect(Collectors.toSet());
		medicationStatementCommercialRepository.logicalDeleteAllByMedicationStatementIds(medicationStatementIds);
		medicationStatementCommercialRepository.saveAll(entities);
		medicationStatementCommercialRepository.flush();
	}

	@Override
	public Optional<List<PrescriptionsDataBo>> getPrescriptionsDataByDni(String identificationNumber) {

		String stringQuery =
				"SELECT DISTINCT mr.id AS mrid, ms.prescription_date, ms.due_date, " +
				"p3.first_name AS p3fn, p3.last_name, it2.description AS it2d, p3.identification_number AS p3d, pe2.phone_number, pe2.email AS email, " +
				"ps.description AS psd, ps.sctid_code AS professional_specialty_snomed_code, pln.license_number, CASE WHEN pln.type_license_number = 1 THEN 'NACIONAL' ELSE 'PROVINCIAL' END, " +
				"doc.id, cs.name, cs.sctid_code AS specialty_snomed_code " +
				"FROM medication_statement ms " +
				"JOIN document_medicamention_statement dms ON ms.id = dms.medication_statement_id " +
				"JOIN document doc ON doc.id = dms.document_id " +
				"JOIN medication_request mr ON mr.id = doc.source_id " +
				"JOIN patient p ON p.id = ms.patient_id " +
				"JOIN person p2 ON p2.id = p.person_id " +
				"JOIN healthcare_professional hp ON hp.id = mr.doctor_id " +
				"JOIN person p3 ON p3.id = hp.person_id " +
				"JOIN identification_type it2 ON it2.id = p3.identification_type_id " +
				"LEFT JOIN person_extended pe2 ON pe2.person_id = p3.id " +
				"JOIN professional_professions pp ON pp.healthcare_professional_id = hp.id " +
				"JOIN healthcare_professional_specialty hps ON hps.professional_profession_id = pp.id " +
				"JOIN professional_specialty ps ON ps.id = pp.professional_specialty_id " +
				"JOIN professional_license_numbers pln ON (pln.professional_profession_id = pp.id OR pln.healthcare_professional_specialty_id = hps.id) " +
				"LEFT JOIN clinical_specialty cs ON (cs.id = mr.clinical_specialty_id) "+
				"WHERE p2.identification_number LIKE :identificationNumber " +
						"AND hps.deleted <> TRUE " +
						"AND (doc.type_id = 5 OR doc.type_id = 14) " +
						"AND ms.prescription_line_state = 1 " +
						"AND current_date - ms.due_date <= 30 " +
				"ORDER BY mr.id DESC";

		Query query = entityManager.createNativeQuery(stringQuery)
				.setParameter("identificationNumber", identificationNumber);
		List<Object[]> queryResult = query.getResultList();

		if(queryResult.isEmpty()) {
			return Optional.empty();
		}

		var lastId = (Integer)queryResult.get(0)[0];
		int firstIndex = 0;
		List<PrescriptionsDataBo> listResult = new ArrayList<>();

		for(int i = 0; i < queryResult.size(); i++) {
			var thisId = (Integer) queryResult.get(i)[0];

			if(!thisId.equals(lastId) ){
				listResult.add(processPrescriptionsDataQuery(queryResult.subList(firstIndex, i)));
				firstIndex = i;
				lastId = thisId;
			} else if (i == queryResult.size() - 1) {
				listResult.add(processPrescriptionsDataQuery(queryResult.subList(firstIndex, i + 1)));
			}
		}

		return Optional.of(listResult);

	}

	@Override
	public MultipleCommercialPrescriptionBo getMultipleCommercialPrescriptionByIdAndIdentificationNumber(PrescriptionIdentifier prescriptionIdentifier, String identificationNumber) {
		String stringQuery = "SELECT mr.id AS mrid, ms.prescription_date, ms.due_date, " +
				"p2.first_name AS p2fn, p2.last_name AS p2ln, pe.name_self_determination, g.description AS gd, spg.description AS spgd, p2.birth_date, it.description AS itd, p2.identification_number, " +
				"mc.name AS mcn, mc.cuit, mcp.plan, pmc.affiliate_number, i.name, i.sisa_code, i.province_code, " +
				"CONCAT(a.street, ' ', a.number, ' ', CASE WHEN a.floor IS NOT NULL THEN CONCAT('Piso ', a.floor) ELSE '' END)," +
				"p3.first_name AS p3fn, p3.last_name, it2.description AS it2d, p3.identification_number AS p3d, pe2.phone_number, pe2.email AS EMAIL, ps.description AS psd, " +
				"ps.sctid_code AS psc, " +
				"pln.license_number, CASE WHEN pln.type_license_number = 1 THEN 'NACIONAL' ELSE 'PROVINCIAL' END, ms.prescription_line_number AS msid, msls.description AS mssd, s.pt AS spt, s.sctid AS sid, " +
				"pt.description AS ptd, s2.pt AS s2pt, s2.sctid AS s2id, " +
				"d2.doses_by_unit AS unit_dose, d2.doses_by_day, d2.duration, '' AS presentation, 0 AS presentation_quantity, d.id, mr.is_archived, " +
				"CASE WHEN d2.dose_quantity_id IS NULL THEN NULL ELSE q.value END, " +
				"msls.id AS status_id, ms.id as medication_statement_id " +
				"FROM {h-schema}medication_statement ms " +
				"JOIN {h-schema}document_medicamention_statement dms ON (ms.id = dms.medication_statement_id) " +
				"JOIN {h-schema}document d ON (d.id = dms.document_id) " +
				"JOIN {h-schema}medication_request mr ON (mr.id = d.source_id) " +
				"JOIN {h-schema}patient p ON (p.id = ms.patient_id) " +
				"JOIN {h-schema}person p2 ON (p2.id = p.person_id) " +
				"LEFT JOIN {h-schema}person_extended pe ON (pe.person_id = p2.id) " +
				"JOIN {h-schema}gender g ON (g.id = p2.gender_id) " +
				"LEFT JOIN {h-schema}self_perceived_gender spg ON (spg.id = pe.gender_self_determination) " +
				"JOIN {h-schema}identification_type it ON (it.id = p2.identification_type_id) " +
				"LEFT JOIN {h-schema}patient_medical_coverage pmc ON (mr.medical_coverage_id = pmc.id) "+
				"LEFT JOIN {h-schema}medical_coverage mc ON (mc.id = pmc.medical_coverage_id) " +
				"LEFT JOIN {h-schema}medical_coverage_plan mcp ON (mcp.medical_coverage_id = pmc.medical_coverage_id) " +
				"LEFT JOIN {h-schema}institution i ON (i.id = d.institution_id) " +
				"LEFT JOIN {h-schema}address a ON (a.id = i.address_id) " +
				"JOIN {h-schema}healthcare_professional hp ON (hp.id = mr.doctor_id) " +
				"JOIN {h-schema}person p3 ON (p3.id = hp.person_id) " +
				"JOIN {h-schema}identification_type it2 ON (it2.id = p3.identification_type_id) " +
				"LEFT JOIN {h-schema}person_extended pe2 ON (pe2.person_id = p3.id) " +
				"JOIN {h-schema}professional_professions pp ON (pp.healthcare_professional_id = hp.id) " +
				"JOIN {h-schema}healthcare_professional_specialty hps ON (hps.professional_profession_id = pp.id) " +
				"JOIN {h-schema}professional_specialty ps ON (ps.id = pp.professional_specialty_id) " +
				"JOIN {h-schema}professional_license_numbers pln ON (pln.professional_profession_id = pp.id OR pln.healthcare_professional_specialty_id = hps.id)" +
				"JOIN {h-schema}health_condition hc ON (hc.id = ms.health_condition_id) " +
				"JOIN {h-schema}snomed s ON (s.id = hc.snomed_id) " +
				"JOIN {h-schema}problem_type pt ON (pt.id = hc.problem_id) " +
				"JOIN {h-schema}snomed s2 ON (ms.snomed_id = s2.id) " +
				"LEFT JOIN {h-schema}dosage d2 ON (d2.id = ms.dosage_id) " +
				"LEFT JOIN {h-schema}quantity q ON (d2.dose_quantity_id = q.id) " +
				"LEFT JOIN {h-schema}medication_statement_line_state msls ON (msls.id = ms.prescription_line_state) " +
				"WHERE p2.identification_number = :identificationNumber " +
				"AND mr.id = :numericPrescriptionId " +
				"AND (d.type_id = " + RECETA + " OR d.type_id = " + RECETA_DIGITAL + ") " +
				"AND hc.verification_status_id = '" + CONFIRMADO + "' " +
				"AND (ms.status_id = '" + COMPLETO + "' OR ms.status_id = '" + ACTIVO + "') " +
				"ORDER BY mr.id DESC";

		Query query = entityManager.createNativeQuery(stringQuery)
				.setParameter("identificationNumber", identificationNumber)
				.setParameter("numericPrescriptionId", prescriptionIdentifier.prescriptionId);

		List<Object[]> queryResult = query.getResultList();
		List<MultipleCommercialPrescriptionBo> result = queryResult.stream()
				.map(this::processMultipleCommercialPrescription)
				.collect(Collectors.toList());
		MultipleCommercialPrescriptionBo mergedResult = multipleCommercialsMergeResults(result);
		if (mergedResult.getPrescriptionId() != null)
			mergedResult.setPrescriptionId(domainNumber + ID_DIVIDER + mergedResult.getPrescriptionId());
		mergedResult.setDomain(prescriptionIdentifier.domain);
		mergedResult.getPrescriptionLines().forEach(line -> line.setCommercialMedications(fetchPrescriptionCommercials(line.getMedicationStatementId())));
		return mergedResult;
	}

	private MultipleCommercialPrescriptionBo processMultipleCommercialPrescription(Object[] queryResult) {
		LocalDate dueDate = queryResult[2] != null ? ((Date)queryResult[2]).toLocalDate() : ((Date)queryResult[1]).toLocalDate().plusDays(30);
		String accessId = JWTUtils.generate256(Map.of("accessId", queryResult[41].toString()), "prescription", secret, tokenExpiration);
		return new MultipleCommercialPrescriptionBo(
				domainNumber.toString(),
				((Integer) queryResult[0]).toString(),
				((Date) queryResult[1]).toLocalDate().atStartOfDay(),
				dueDate.atStartOfDay(),
				"api/external-document-access/download-prescription/" + accessId,
				queryResult[42] == null ? Boolean.FALSE : (Boolean) queryResult[42],
				new PatientPrescriptionBo(
						(String) queryResult[3],
						(String) queryResult[4],
						(String) queryResult[5],
						(String) queryResult[6],
						(String) queryResult[7],
						((Date) queryResult[8]).toLocalDate(),
						(String) queryResult[9],
						(String) queryResult[10],
						(String) queryResult[11],
						(String) queryResult[12],
						(String) queryResult[13],
						(String) queryResult[14]
				),
				new InstitutionPrescriptionBo(
						(String) queryResult[15],
						(String) queryResult[16],
						(String) queryResult[17],
						(String) queryResult[18]
				),
				new ProfessionalPrescriptionBo(
						(String) queryResult[19],
						(String) queryResult[20],
						(String) queryResult[21],
						(String) queryResult[22],
						(String) queryResult[23],
						(String) queryResult[24],
						List.of(new PrescriptionProfessionBo(
								(String) queryResult[25],
								(String) queryResult[26]
						)),
						List.of(new PrescriptionProfessionalRegistrationBo(
								(String) queryResult[27],
								(String) queryResult[28]
						))
				),
				List.of(new MultipleCommercialPrescriptionLineBo(
						(Integer) queryResult[29],
						queryResult[44].equals(RECETA_CANCELADA) || dueDate.isAfter(LocalDate.now()) ? (String)queryResult[30] : "VENCIDO",
						new PrescriptionProblemBo(
								(String) queryResult[31],
								(String) queryResult[32],
								queryResult[33].equals(CRONICO) ? "Crónico" : "Agudo"
						),
						new GenericMedicationBo(
								(String) queryResult[34],
								(String) queryResult[35]
						),
						null,
						queryResult[36] != null ? (Double) queryResult[36] : 0,
						queryResult[37] != null ? (Double) queryResult[37] : 0,
						queryResult[38] != null ? (Double) queryResult[38] : 1,
						(String) queryResult[39],
						(Integer) queryResult[40],
						queryResult[43] != null ? (Double)queryResult[43] : null,
						(Integer) queryResult[45]
				))
		);
	}

	private List<CommercialMedicationBo> fetchPrescriptionCommercials(Integer medicationStatementId) {
		String queryString = "SELECT msc.commercial_name, msc.snomed_id " +
				"FROM {h-schema}medication_statement ms " +
				"JOIN {h-schema}medication_statement_commercial msc ON (msc.medication_statement_id = ms.id) " +
				"WHERE ms.id = :medicationStatementId " +
				"AND msc.deleted = FALSE";

		List<Object[]> queryResult = entityManager.createNativeQuery(queryString)
				.setParameter("medicationStatementId", medicationStatementId)
				.getResultList();

		return queryResult.stream().map(commercialMedication -> new CommercialMedicationBo((String) commercialMedication[0], (String) commercialMedication[1])).collect(Collectors.toList());
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
			if(PrescriptionValidStatesEnum.map(newS.getPrescriptionLineState()).equals(PrescriptionValidStatesEnum.CANCELADO_DISPENSA)) {
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
			var stateToSave = state.equals(PrescriptionValidStatesEnum.CANCELADO_DISPENSA) ?
					1 : stateId;
			query.setParameter("status", stateToSave)
					.setParameter("prescriptionId", prescriptionId)
					.setParameter("lineNumber", prescriptionLineNumbers.get(i))
					.executeUpdate();
			entityManager.flush();
			entityManager.clear();
		}
	}

	private List<LineStatusBo> getLineStatus(Integer prescriptionId, String idNumber) throws PrescriptionNotFoundException {
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

	private void assertExistsPrescriptionAndDni(List<Object[]> queryResult, String idNumber) throws PrescriptionNotFoundException {
		if(queryResult.isEmpty() || !queryResult.get(0)[3].toString().equals(idNumber)) {
			throw new PrescriptionNotFoundException("No se encontró la receta en el dominio");
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
		result.setLink(unmergedResults.get(0).getLink());
		result.setIsArchived(unmergedResults.get(0).getIsArchived());
		result.setPatientPrescriptionBo(unmergedResults.get(0).getPatientPrescriptionBo());
		result.setInstitutionPrescriptionBo(unmergedResults.get(0).getInstitutionPrescriptionBo());
		result.setPrescriptionsLineBo(unmergedResults.get(0).getPrescriptionsLineBo());
		ProfessionalPrescriptionBo professionalPrescriptionBo = unmergedResults.get(0).getProfessionalPrescriptionBo();
		List<PrescriptionProfessionBo> prescriptionProfessionBos = new ArrayList<>(professionalPrescriptionBo.getProfessions());
		List<PrescriptionProfessionalRegistrationBo> prescriptionProfessionalRegistrationBos = new ArrayList<>(professionalPrescriptionBo.getRegistrations());
		List<PrescriptionLineBo> prescriptionLineBoList = new ArrayList<>(result.getPrescriptionsLineBo());

		for(int i = 1; i < unmergedResults.size(); i++) {
			var specialty = unmergedResults.get(i).getProfessionalPrescriptionBo().getProfessions().get(0);
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
		professionalPrescriptionBo.setProfessions(prescriptionProfessionBos);
		professionalPrescriptionBo.setRegistrations(prescriptionProfessionalRegistrationBos);
		result.setProfessionalPrescriptionBo(professionalPrescriptionBo);
		result.setPrescriptionsLineBo(prescriptionLineBoList);
		return result;
	}

	private MultipleCommercialPrescriptionBo multipleCommercialsMergeResults(List<MultipleCommercialPrescriptionBo> unmergedResults) {
		MultipleCommercialPrescriptionBo result = new MultipleCommercialPrescriptionBo();
		if (unmergedResults.isEmpty())
			return result;
		result.setDomain(unmergedResults.get(0).getDomain());
		result.setPrescriptionId(unmergedResults.get(0).getPrescriptionId());
		result.setPrescriptionDate(unmergedResults.get(0).getPrescriptionDate());
		result.setDueDate(unmergedResults.get(0).getDueDate());
		result.setLink(unmergedResults.get(0).getLink());
		result.setIsArchived(unmergedResults.get(0).getIsArchived());
		result.setPatientPrescription(unmergedResults.get(0).getPatientPrescription());
		result.setInstitutionPrescription(unmergedResults.get(0).getInstitutionPrescription());
		result.setPrescriptionLines(unmergedResults.get(0).getPrescriptionLines());
		ProfessionalPrescriptionBo professionalPrescriptionBo = unmergedResults.get(0).getProfessionalPrescription();
		List<PrescriptionProfessionBo> prescriptionProfessionBos = new ArrayList<>(professionalPrescriptionBo.getProfessions());
		List<PrescriptionProfessionalRegistrationBo> prescriptionProfessionalRegistrationBos = new ArrayList<>(professionalPrescriptionBo.getRegistrations());
		List<MultipleCommercialPrescriptionLineBo> prescriptionLineBoList = new ArrayList<>(result.getPrescriptionLines());
		for (int i = 1; i < unmergedResults.size(); i++) {
			PrescriptionProfessionBo specialty = unmergedResults.get(i).getProfessionalPrescription().getProfessions().get(0);
			PrescriptionProfessionalRegistrationBo prescriptionRegistration = unmergedResults.get(i).getProfessionalPrescription().getRegistrations().get(0);
			if (!prescriptionProfessionalRegistrationBos.contains(prescriptionRegistration))
				prescriptionProfessionalRegistrationBos.add(prescriptionRegistration);
			if (!prescriptionProfessionBos.contains(specialty))
				prescriptionProfessionBos.add(specialty);
			if (!prescriptionLineBoList.contains(unmergedResults.get(i).getPrescriptionLines().get(0)))
				prescriptionLineBoList.add(unmergedResults.get(i).getPrescriptionLines().get(0));
		}
		professionalPrescriptionBo.setProfessions(prescriptionProfessionBos);
		professionalPrescriptionBo.setRegistrations(prescriptionProfessionalRegistrationBos);
		result.setProfessionalPrescription(professionalPrescriptionBo);
		result.setPrescriptionLines(prescriptionLineBoList);
		return result;
	}

	private PrescriptionsDataBo processPrescriptionsDataQuery(List<Object[]> queryResult) {

		if(queryResult.isEmpty()) {
			return new PrescriptionsDataBo();
		}

		List<PrescriptionProfessionBo> prescriptionProfessionBos = new ArrayList<>();
		List<PrescriptionProfessionalRegistrationBo> prescriptionProfessionalRegistrationBos = new ArrayList<>();

		queryResult.forEach(row -> {
			prescriptionProfessionBos.add(new PrescriptionProfessionBo((String)row[9], (String)row[10]));
			prescriptionProfessionalRegistrationBos.add(new PrescriptionProfessionalRegistrationBo((String)row[11], (String)row[12]));
		});

		return new PrescriptionsDataBo(
				domainNumber.toString(),
				((Integer)queryResult.get(0)[0]).toString(),
				((Date)queryResult.get(0)[1]).toLocalDate().atStartOfDay(),
				queryResult.get(0)[2] != null ?
						((Date)queryResult.get(0)[2]).toLocalDate().atStartOfDay() :
						((Date)queryResult.get(0)[1]).toLocalDate().plusDays(30).atStartOfDay(),
				"api/external-document-access/download-prescription/" +
						JWTUtils.generate256(Map.of("accessId", queryResult.get(0)[13].toString()), "prescription", secret, tokenExpiration),
				new ProfessionalPrescriptionBo(
						(String)queryResult.get(0)[3],
						(String)queryResult.get(0)[4],
						(String)queryResult.get(0)[5],
						(String)queryResult.get(0)[6],
						(String)queryResult.get(0)[7],
						(String)queryResult.get(0)[8],
						prescriptionProfessionBos,
						prescriptionProfessionalRegistrationBos
				),
				new PrescriptionSpecialtyBo((String)queryResult.get(0)[14], (String)queryResult.get(0)[15])
		);
	}

	private PrescriptionBo processPrescriptionQuery(Object[] queryResult) {

		var dueDate = queryResult[2] != null ?
				((Date)queryResult[2]).toLocalDate() : ((Date)queryResult[1]).toLocalDate().plusDays(30);

		var accessId = JWTUtils.generate256(Map.of("accessId", queryResult[41].toString()), "prescription", secret, tokenExpiration);

		return new PrescriptionBo(
				domainNumber.toString(),
				((Integer)queryResult[0]).toString(),
				((Date)queryResult[1]).toLocalDate().atStartOfDay(),
				dueDate.atStartOfDay(),
				"api/external-document-access/download-prescription/" + accessId,
				queryResult[42] == null ? Boolean.FALSE : (Boolean)queryResult[42],
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
						queryResult[44].equals(RECETA_CANCELADA) || dueDate.isAfter(LocalDate.now()) ? (String)queryResult[30] : "VENCIDO",
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
						(Integer)queryResult[40],
						queryResult[43] != null ? (Double)queryResult[43] : null
				))
		);
	}
}
