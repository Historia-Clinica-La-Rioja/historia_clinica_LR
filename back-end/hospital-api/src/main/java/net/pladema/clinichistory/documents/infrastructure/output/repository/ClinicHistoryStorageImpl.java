package net.pladema.clinichistory.documents.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.documents.application.ClinicHistoryStorage;

import net.pladema.clinichistory.documents.domain.CHAnamnesisBo;
import net.pladema.clinichistory.documents.domain.CHAnestheticReportBo;
import net.pladema.clinichistory.documents.domain.CHCounterReferenceBo;
import net.pladema.clinichistory.documents.domain.CHDocumentBo;
import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;

import net.pladema.clinichistory.documents.domain.CHEpicrisisBo;
import net.pladema.clinichistory.documents.domain.CHEvolutionNoteBo;
import net.pladema.clinichistory.documents.domain.CHIndicationBo;
import net.pladema.clinichistory.documents.domain.CHMedicationRequestBo;
import net.pladema.clinichistory.documents.domain.CHNursingConsultationBo;
import net.pladema.clinichistory.documents.domain.CHOdontologyBo;
import net.pladema.clinichistory.documents.domain.CHOutpatientBo;
import net.pladema.clinichistory.documents.domain.CHServiceRequestBo;
import net.pladema.clinichistory.documents.domain.CHTriageBo;
import net.pladema.clinichistory.documents.domain.ECHDocumentType;
import net.pladema.clinichistory.documents.domain.ECHEncounterType;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;

import net.pladema.clinichistory.documents.domain.HistoricClinicHistoryDownloadBo;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.HistoricClinicHistoryDownload;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import net.pladema.person.service.PersonService;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClinicHistoryStorageImpl implements ClinicHistoryStorage {

    private final VClinicHistoryRepository repository;
    private final HistoricClinicHistoryDownloadRepository historicClinicHistoryDownloadRepository;
	private final PersonService personService;

    @Override
    public List<CHDocumentSummaryBo> getPatientClinicHistory(Integer patientId, LocalDate from, LocalDate to) {

		List<CHDocumentSummaryBo> result = new ArrayList<>();
		LocalDateTime fromDateTime = LocalDateTime.of(from, LocalTime.MIN);
		LocalDateTime toDateTime = LocalDateTime.of(to, LocalTime.MAX);

		List<CHDocumentSummaryBo> internmentResult = repository.getInternmentPatientClinicHistory(patientId, fromDateTime, toDateTime);
		List<CHDocumentSummaryBo> emergencyCareResult = repository.getEmergencyCarePatientClinicHistory(patientId, fromDateTime, toDateTime);

		List<CHDocumentSummaryBo> outpatientResult = repository.getOutpatientConsultationPatientClinicHistory(patientId, fromDateTime, toDateTime);
		outpatientResult.addAll(repository.getOutpatientServiceRequestPatientClinicHistory(patientId, fromDateTime, toDateTime));
		outpatientResult.addAll(repository.getCounterReferencePatientClinicHistory(patientId, fromDateTime, toDateTime));
		List<Object[]> nursingOutpatientResult = repository.getNursingOutpatientConsultationPatientClinicHistory(patientId, fromDateTime, toDateTime);
		List<Object[]> odontologyResult = repository.getOdontologyPatientClinicHistory(patientId, fromDateTime, toDateTime);


		result.addAll(internmentResult);
	    result.addAll(emergencyCareResult);
		result.addAll(outpatientResult);
		result.addAll(mapObjectToCHDocumentSummaryBo(nursingOutpatientResult));
		result.addAll(mapObjectToCHDocumentSummaryBo(odontologyResult));
		result
				.stream()
				.map(this::setDocumentInfo)
				.collect(Collectors.toList());
		return result;
    }

    @Override
    public List<CHDocumentBo> getClinicHistoryDocuments(List<Long> ids) {
        List<VClinicHistory> result = repository.findAllById(ids);

		return result.stream()
				.map(this::mapToBo)
				.filter(Objects::nonNull)
				.sorted(Comparator.comparing(CHDocumentBo::getStartDate).reversed().thenComparing(Comparator.comparing(CHDocumentBo::getCreatedOn).reversed()))
				.collect(Collectors.toList());
    }

    @Override
    public Integer savePatientClinicHistoryLastPrint(Integer userId, Integer patientId, Integer institutionId) {
        return historicClinicHistoryDownloadRepository
                .save(new HistoricClinicHistoryDownload(null, userId, patientId, LocalDateTime.now(), institutionId)).getId();
    }

    @Override
    public Optional<HistoricClinicHistoryDownloadBo> getPatientClinicHistoryLastDownload(Integer patientId, Integer institutionId) {
        var lastDownload = historicClinicHistoryDownloadRepository.getPatientClinicHistoryHistoricDownloads(patientId, institutionId).stream().findFirst();
        return lastDownload.map(HistoricClinicHistoryDownloadBo::new);
    }

	private List<CHDocumentSummaryBo> mapObjectToCHDocumentSummaryBo(List<Object[]> results) {
		return results.stream()
				.map(record -> new CHDocumentSummaryBo(
						((BigInteger) record[0]).longValue(),
						(Integer) record[1],
						((Timestamp) record[2]).toLocalDateTime(),
						((Timestamp) record[3]).toLocalDateTime(),
						(Integer) record[4],
						(String) record[5],
						((Number) record[6]).shortValue(),
						((Number) record[7]).shortValue(),
						((Number) record[8]).shortValue()
				))
				.collect(Collectors.toList());
	}

	private CHDocumentSummaryBo setDocumentInfo(CHDocumentSummaryBo chDocumentSummaryBo) {
		String personName = personService.getCompletePersonNameById(chDocumentSummaryBo.getCreatedByPersonId());
		chDocumentSummaryBo.setProfessional(personName);
		List<String> problems = repository.getProblems(chDocumentSummaryBo.getId());
		chDocumentSummaryBo.setProblems(String.join(", ", problems));
		chDocumentSummaryBo.setDocumentType(getDocumentType(chDocumentSummaryBo.getTypeId()));
		chDocumentSummaryBo.setEncounterType(getEncounterType(chDocumentSummaryBo.getSourceTypeId().shortValue(), chDocumentSummaryBo.getRequestSourceTypeId().shortValue()));
		return chDocumentSummaryBo;
	}

    private CHDocumentBo mapToBo(VClinicHistory row) {
        ECHEncounterType encounterType = getEncounterType(row.getSourceTypeId(), row.getRequestSourceTypeId());
        ECHDocumentType documentType = getDocumentType(row.getDocumentTypeId());
        if (row.getDocumentTypeId().equals(EDocumentType.ORDER.getId()))
            return new CHServiceRequestBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.OUTPATIENT.getId()))
            return new CHOutpatientBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.ODONTOLOGY.getId()))
            return new CHOdontologyBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.COUNTER_REFERENCE.getId()))
            return new CHCounterReferenceBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.RECIPE.getId()) || row.getDocumentTypeId().equals(EDocumentType.DIGITAL_RECIPE.getId()))
            return new CHMedicationRequestBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.NURSING.getId()))
            return new CHNursingConsultationBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.ANAMNESIS.getId()))
            return new CHAnamnesisBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.EVALUATION_NOTE.getId()) ||
                row.getDocumentTypeId().equals(EDocumentType.NURSING_EVOLUTION_NOTE.getId()) ||
                row.getDocumentTypeId().equals(EDocumentType.EMERGENCY_CARE_EVOLUTION.getId()) ||
				row.getDocumentTypeId().equals(EDocumentType.NURSING_EMERGENCY_CARE_EVOLUTION.getId()))
            return new CHEvolutionNoteBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.EPICRISIS.getId()))
            return new CHEpicrisisBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.INDICATION.getId()))
            return new CHIndicationBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.TRIAGE.getId()))
            return new CHTriageBo(row, encounterType, documentType);
        if (row.getDocumentTypeId().equals(EDocumentType.ANESTHETIC_REPORT.getId()))
            return new CHAnestheticReportBo(row, encounterType, documentType);
        return null;
    }

    private ECHEncounterType getEncounterType(Short sourceTypeId, Short requestSourceTypeId) {
        if (sourceTypeId.equals(ESourceType.HOSPITALIZATION.getId())
                || (sourceTypeId.equals(ESourceType.ORDER.getId()) && (requestSourceTypeId.equals(ESourceType.HOSPITALIZATION.getId()))))
            return ECHEncounterType.HOSPITALIZATION;
        if (sourceTypeId.equals(ESourceType.EMERGENCY_CARE.getId())
                || (sourceTypeId.equals(ESourceType.ORDER.getId()) && (requestSourceTypeId.equals(ESourceType.EMERGENCY_CARE.getId()))))
            return ECHEncounterType.EMERGENCY_CARE;
        return ECHEncounterType.OUTPATIENT;
    }

    private ECHDocumentType getDocumentType(Short documentTypeId) {
        if (documentTypeId.equals(EDocumentType.EPICRISIS.getId()))
            return ECHDocumentType.EPICRISIS;
        if (documentTypeId.equals(EDocumentType.RECIPE.getId()) || documentTypeId.equals(EDocumentType.INDICATION.getId()))
            return ECHDocumentType.MEDICAL_PRESCRIPTIONS;
        if (documentTypeId.equals(EDocumentType.ORDER.getId()))
            return ECHDocumentType.REPORTS;
        if (documentTypeId.equals(EDocumentType.ANESTHETIC_REPORT.getId()))
            return ECHDocumentType.ANESTHETIC_REPORTS;
        if (documentTypeId.equals(EDocumentType.EMERGENCY_CARE.getId()) || documentTypeId.equals(EDocumentType.IMMUNIZATION.getId()))
            return ECHDocumentType.OTHER;
        if (documentTypeId.equals(EDocumentType.SURGICAL_HOSPITALIZATION_REPORT.getId()))
            return ECHDocumentType.NOT_SUPPORTED;
        return ECHDocumentType.CLINICAL_NOTES;
    }

}
