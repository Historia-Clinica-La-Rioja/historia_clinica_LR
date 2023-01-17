package net.pladema.patient.infrastructure.output.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentAllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentDiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentIndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentMedicamentionStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.AllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.IndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProceduresRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.AllergyIntolerance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Inmunization;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyDiagnostic;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Procedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Indication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;
import net.pladema.patient.infrastructure.output.repository.entity.EMergeTable;
import net.pladema.patient.infrastructure.output.repository.entity.MergedPatientItem;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MergeClinicHistoryStorageImpl implements MergeClinicHistoryStorage {

	private final MergedPatientItemRepository mergedPatientItemRepository;
	private final InternmentEpisodeRepository internmentEpisodeRepository;
	private final DocumentRepository documentRepository;
	private final DocumentHealthConditionRepository documentHealthConditionRepository;
	private final HealthConditionRepository healthConditionRepository;
	private final DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository;
	private final AllergyIntoleranceRepository allergyIntoleranceRepository;
	private final DocumentImmunizationRepository documentImmunizationRepository;
	private final ImmunizationRepository immunizationRepository;
	private final DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository;
	private final MedicationStatementRepository medicationStatementRepository;
	private final DocumentProcedureRepository documentProcedureRepository;
	private final ProceduresRepository proceduresRepository;
	private final DocumentOdontologyDiagnosticRepository documentOdontologyDiagnosticRepository;
	private final OdontologyDiagnosticRepository odontologyDiagnosticRepository;
	private final DocumentOdontologyProcedureRepository documentOdontologyProcedureRepository;
	private final OdontologyProcedureRepository odontologyProcedureRepository;
	private final DocumentRiskFactorRepository documentRiskFactorRepository;
	private final ObservationRiskFactorRepository observationRiskFactorRepository;
	private final DocumentLabRepository documentLabRepository;
	private final ObservationLabRepository observationLabRepository;
	private final DocumentDiagnosticReportRepository documentDiagnosticReportRepository;
	private final DiagnosticReportRepository diagnosticReportRepository;
	private final DocumentIndicationRepository documentIndicationRepository;
	private final IndicationRepository indicationRepository;

	@Override
	public List<Integer> getInternmentEpisodesIds(List<Integer> oldPatients) {
		return internmentEpisodeRepository.getInternmentEpisodeFromPatients(oldPatients);
	}

	@Override
	public List<Long> getDocumentsIds(List<Integer> ieIds) {
		return documentRepository.getIdsBySourceIdType(ieIds, ESourceType.HOSPITALIZATION.getId());
	}

	@Override
	public void modifyInternmentEpisode(List<Integer> ieIds, Integer newPatient) {
		log.debug("Internment episode ids to modify {}", ieIds);

		List<InternmentEpisode> ie = internmentEpisodeRepository.findAllById(ieIds);

		if (!ieIds.isEmpty()) {
			internmentEpisodeRepository.updatePatient(ieIds, newPatient);
			ie.forEach(i -> saveMergedItem(EMergeTable.INTERNMENT_EPISODE, i.getId(), i.getPatientId(), newPatient));
		}
	}

	private MergedPatientItem saveMergedItem(EMergeTable m, Integer id, Integer oldPatientId, Integer newPatientId) {
		return mergedPatientItemRepository.save(new MergedPatientItem(
				m.getTableName(),
				m.getColumnIdName(),
				id,
				oldPatientId,
				newPatientId
		));
	}

	@Override
	public void modifyHealthCondition(List<Long> dIds, Integer newPatient) {

		List<HealthCondition> hc = documentHealthConditionRepository.getHealthConditionFromDocuments(dIds);

		List<Integer> hcIds = hc.stream().map(h -> h.getId()).collect(Collectors.toList());

		log.debug("Health condition ids to modify {}", hcIds);

		if (!hcIds.isEmpty()) {
			healthConditionRepository.updatePatient(hcIds, newPatient);
			hc.forEach(h -> saveMergedItem(EMergeTable.HEALTH_CONDITION, h.getId(), h.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyAllergyIntolerance(List<Long> dIds, Integer newPatient) {
		List<AllergyIntolerance> ai = documentAllergyIntoleranceRepository.getAllergyIntoleranceFromDocuments(dIds);

		List<Integer> aiIds = ai.stream().map(a -> a.getId()).collect(Collectors.toList());

		log.debug("Allergy intolerance ids to modify {}", aiIds);

		if (!aiIds.isEmpty()) {
			allergyIntoleranceRepository.updatePatient(aiIds, newPatient);
			ai.forEach(a -> saveMergedItem(EMergeTable.ALLERGY_INTOLERANCE, a.getId(), a.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyImmunization(List<Long> dIds, Integer newPatient) {
		List<Inmunization> i = documentImmunizationRepository.getImmunizationFromDocuments(dIds);

		List<Integer> iIds = i.stream().map(imm -> imm.getId()).collect(Collectors.toList());

		log.debug("Immunization ids to modify {}", iIds);

		if (!iIds.isEmpty()) {
			immunizationRepository.updatePatient(iIds, newPatient);
			i.forEach(imm -> saveMergedItem(EMergeTable.INMUNIZATION, imm.getId(), imm.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyMedicationStatement(List<Long> dIds, Integer newPatient) {
		List<MedicationStatement> ms = documentMedicamentionStatementRepository.getMedicationStatementFromDocuments(dIds);

		List<Integer> msIds = ms.stream().map(m -> m.getId()).collect(Collectors.toList());

		log.debug("Medication Statement ids to modify {}", msIds);

		if (!msIds.isEmpty()) {
			medicationStatementRepository.updatePatient(msIds, newPatient);
			ms.forEach(m -> saveMergedItem(EMergeTable.MEDICATION_STATEMENT, m.getId(), m.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyProcedure(List<Long> dIds, Integer newPatient) {
		List<Procedure> p = documentProcedureRepository.getProcedureFromDocuments(dIds);

		List<Integer> pIds = p.stream().map(proc -> proc.getId()).collect(Collectors.toList());

		log.debug("Procedure ids to modify {}", pIds);

		if (!pIds.isEmpty()) {
			proceduresRepository.updatePatient(pIds, newPatient);
			p.forEach(proc -> saveMergedItem(EMergeTable.PROCEDURE, proc.getId(), proc.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyOdontologyDiagnostic(List<Long> dIds, Integer newPatient) {
		List<OdontologyDiagnostic> od = documentOdontologyDiagnosticRepository.getOdontologyDiagnosticFromDocuments(dIds);

		List<Integer> odIds = od.stream().map(o -> o.getId()).collect(Collectors.toList());

		log.debug("Odontology diagnostic ids to modify {}", odIds);

		if (!odIds.isEmpty()) {
			odontologyDiagnosticRepository.updatePatient(odIds, newPatient);
			od.forEach(o -> saveMergedItem(EMergeTable.ODONTOLOGY_DIAGNOSTIC, o.getId(), o.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyOdontologyProcedure(List<Long> dIds, Integer newPatient) {
		List<OdontologyProcedure> op = documentOdontologyProcedureRepository.getOdontologyProcedureFromDocuments(dIds);

		List<Integer> opIds = op.stream().map(o -> o.getId()).collect(Collectors.toList());

		log.debug("Odontology procedure ids to modify {}", opIds);

		if (!opIds.isEmpty()) {
			odontologyProcedureRepository.updatePatient(opIds, newPatient);
			op.forEach(o -> saveMergedItem(EMergeTable.ODONTOLOGY_PROCEDURE, o.getId(), o.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyObservationVitalSign(List<Long> dIds, Integer newPatient) {
		List<ObservationRiskFactor> orf = documentRiskFactorRepository.getRiskFactorFromDocuments(dIds);

		List<Integer> orfIds = orf.stream().map(or -> or.getId()).collect(Collectors.toList());

		log.debug("Observation vital sign ids to modify {}", orfIds);

		if (!orfIds.isEmpty()) {
			observationRiskFactorRepository.updatePatient(orfIds, newPatient);
			orf.forEach(or -> saveMergedItem(EMergeTable.OBSERVATION_VITAL_SIGN, or.getId(), or.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyObservationLab(List<Long> dIds, Integer newPatient) {
		List<ObservationLab> ol = documentLabRepository.getObservationLabFromDocuments(dIds);

		List<Integer> olIds = ol.stream().map(o -> o.getId()).collect(Collectors.toList());

		log.debug("Observation lab ids to modify {}", olIds);

		if (!olIds.isEmpty()) {
			observationLabRepository.updatePatient(olIds, newPatient);
			ol.forEach(o -> saveMergedItem(EMergeTable.OBSERVATION_LAB, o.getId(), o.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyDiagnosticReport(List<Long> dIds, Integer newPatient) {
		List<DiagnosticReport> dr = documentDiagnosticReportRepository.getDiagnosticReportFromDocuments(dIds);

		List<Integer> drIds = dr.stream().map(d -> d.getId()).collect(Collectors.toList());

		log.debug("Diagnostic report ids to modify {}", drIds);

		if (!drIds.isEmpty()) {
			diagnosticReportRepository.updatePatient(drIds, newPatient);
			dr.forEach(d -> saveMergedItem(EMergeTable.DIAGNOSTIC_REPORT, d.getId(), d.getPatientId(), newPatient));
		}
	}

	@Override
	public void modifyIndication(List<Long> dIds, Integer newPatient) {
		List<Indication> i = documentIndicationRepository.getIndicationFromDocuments(dIds);

		List<Integer> iIds = i.stream().map(ind -> ind.getId()).collect(Collectors.toList());

		log.debug("Indication ids to modify {}", iIds);

		if (!iIds.isEmpty()) {
			indicationRepository.updatePatient(iIds, newPatient);
			i.forEach(ind -> saveMergedItem(EMergeTable.INDICATION, ind.getId(), ind.getPatientId(), newPatient));
		}
	}


}
