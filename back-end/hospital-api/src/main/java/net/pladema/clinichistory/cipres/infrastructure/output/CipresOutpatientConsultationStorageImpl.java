package net.pladema.clinichistory.cipres.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.MedicationSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.cipres.application.port.CipresOutpatientConsultationStorage;
import net.pladema.clinichistory.cipres.application.port.CipresOutpatientConsultationSummaryStorage;
import net.pladema.clinichistory.cipres.domain.CipresOutpatientBasicDataBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationSummaryStorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CipresOutpatientConsultationStorageImpl implements CipresOutpatientConsultationStorage {

	@Value("${app.cipres.consultations.days.ago:7}")
	private Integer DAYS_AGO;

	@Value("${app.cipres.consultations.quantity:500}")
	private Integer LIMIT;
	
	private final OutpatientConsultationSummaryStorage outpatientConsultationSummaryStorage;

	private final CipresOutpatientConsultationSummaryStorage cipresOutpatientConsultationSummaryStorage;

	private final DocumentService documentService;

	@Override
	public List<CipresOutpatientBasicDataBo> getOutpatientConsultationsData() {

		List<CipresOutpatientBasicDataBo> outpatientConsultations = getOutpatientConsultations();

		List<Integer> outpatientConsultationIds = outpatientConsultations.stream().map(CipresOutpatientBasicDataBo::getId).collect(Collectors.toList());

		List<HealthConditionSummaryVo> healthConditions = outpatientConsultationSummaryStorage.getHealthConditionsByOutpatientIds(outpatientConsultationIds);
		List<ProcedureSummaryBo> procedures = outpatientConsultationSummaryStorage.getProceduresByOutpatientIds(outpatientConsultationIds);
		List<MedicationSummaryBo> medications = outpatientConsultationSummaryStorage.getMedicationsByOutpatientIds(outpatientConsultationIds);

		Map<Integer, List<SnomedBo>> procedureMap = procedures.stream()
				.collect(Collectors.groupingBy(ProcedureSummaryBo::getConsultationId,
						Collectors.mapping(p -> new SnomedBo(p.getSnomedSctid(), p.getSnomedPt()), Collectors.toList())));

		Map<Integer, List<SnomedBo>> healthConditionMap = healthConditions.stream()
				.collect(Collectors.groupingBy(HealthConditionSummaryVo::getConsultationID,
						Collectors.mapping(hc -> new SnomedBo(hc.getSnomedSctid(), hc.getSnomedPt()), Collectors.toList())));

		Map<Integer, List<SnomedBo>> medicationMap = medications.stream()
				.collect(Collectors.groupingBy(MedicationSummaryBo::getEncounterId,
						Collectors.mapping(m -> new SnomedBo(m.getSnomedSctid(), m.getSnomedPt()), Collectors.toList())));

		outpatientConsultations.forEach(oc -> {
			normalizeDate(oc);
			oc.setProcedures(procedureMap.getOrDefault(oc.getId(), Collections.emptyList()));
			oc.setProblems(healthConditionMap.getOrDefault(oc.getId(), Collections.emptyList()));
			oc.setMedications(medicationMap.getOrDefault(oc.getId(), Collections.emptyList()));
			oc.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(oc.getDocumentId()));
			oc.setRiskFactorData(documentService.getRiskFactorStateFromDocument(oc.getDocumentId()));
		});

		log.debug("Output size -> {} ", outpatientConsultations.size());
		return outpatientConsultations;
	}

	@Override
	public CipresOutpatientBasicDataBo getOutpatientConsultationData(Integer cipresEncounterId) {
		log.debug("Input parameter -> cipresEncounterId {} ", cipresEncounterId);

		CipresOutpatientBasicDataBo result = this.cipresOutpatientConsultationSummaryStorage.getOutpatientConsultationByCipresEncounterId(cipresEncounterId);

		List<HealthConditionSummaryVo> healthConditions = outpatientConsultationSummaryStorage.getHealthConditionsByOutpatientIds(List.of(result.getId()));
		List<ProcedureSummaryBo> procedures = outpatientConsultationSummaryStorage.getProceduresByOutpatientIds(List.of(result.getId()));
		List<MedicationSummaryBo> medications = outpatientConsultationSummaryStorage.getMedicationsByOutpatientIds(List.of(result.getId()));

		result.setProcedures(procedures.stream().map(p -> new SnomedBo(p.getSnomedSctid(), p.getSnomedPt())).collect(Collectors.toList()));
		result.setProblems(healthConditions.stream().map(hc -> new SnomedBo(hc.getSnomedSctid(), hc.getSnomedPt())).collect(Collectors.toList()));
		result.setMedications(medications.stream().map(m -> new SnomedBo(m.getSnomedSctid(), m.getSnomedPt())).collect(Collectors.toList()));
		result.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(result.getDocumentId()));
		result.setRiskFactorData(documentService.getRiskFactorStateFromDocument(result.getDocumentId()));

		normalizeDate(result);

		log.debug("Output result -> {} ", result);

		return result;
	}

	private List<CipresOutpatientBasicDataBo> getOutpatientConsultations() {
		LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
		LocalTime currentTime = LocalTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));

		LocalTime startMorning = LocalTime.of(0, 0);
		LocalTime endMorning = LocalTime.of(6, 0);

		if ((currentTime.isAfter(startMorning) || currentTime.equals(startMorning)) && (currentTime.isBefore(endMorning) || currentTime.equals(endMorning)))
			return cipresOutpatientConsultationSummaryStorage.getOutpatientConsultationsForSendOrResend(LIMIT, currentDateTime.minusDays(DAYS_AGO), currentDateTime);
		else
			return cipresOutpatientConsultationSummaryStorage.getOutpatientConsultationsForSend(LIMIT, currentDateTime.minusDays(DAYS_AGO), currentDateTime);
	}

	private void normalizeDate(CipresOutpatientBasicDataBo consultation) {
		LocalTime startHour = LocalTime.of(0, 0);
		LocalTime endHour = LocalTime.of(3, 0);
		LocalTime consultationTime = consultation.getDate().toLocalTime();
		if ((consultationTime.equals(startHour) || consultationTime.isAfter(startHour)) && consultationTime.isBefore(endHour))
			consultation.setDate(consultation.getDate().minusDays(1));
	}

}
