package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.documents.validation.SnomedValidator;
import net.pladema.clinichistory.requests.servicerequests.repository.TranscribedServiceRequestDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.TranscribedServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.TranscribedServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.TranscribedServiceRequestDiagnosticReport;
import net.pladema.clinichistory.requests.servicerequests.service.CreateTranscribedServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateTranscribedServiceRequestServiceImpl implements CreateTranscribedServiceRequestService {
	
    public static final String OUTPUT = "Output -> {}";

    private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;

	private final DiagnosticReportRepository diagnosticReportRepository;

	private final HealthConditionRepository healthConditionRepository;

	private final DateTimeProvider dateTimeProvider;

	private final CalculateCie10Facade calculateCie10Facade;

	private final SnomedService snomedService;

	private final TranscribedServiceRequestDiagnosticReportRepository transcribedServiceRequestDiagnosticReportRepository;

	@Transactional
    @Override
    public Integer execute(TranscribedServiceRequestBo transcribedServiceRequestBo) {
        log.debug("Input parameters -> transcribedServiceRequestBo {}", transcribedServiceRequestBo);

        this.assertRequiredFields(transcribedServiceRequestBo);

		HealthConditionBo healthConditionBo = transcribedServiceRequestBo.getHealthCondition();
		HealthCondition savedHealthCondition = this.buildBasicHealthCondition(transcribedServiceRequestBo);
		healthConditionBo.setId(savedHealthCondition.getId());

		TranscribedServiceRequest newServiceRequest = this.createServiceRequest(transcribedServiceRequestBo);
		transcribedServiceRequestBo.setId(newServiceRequest.getId());

		this.saveDiagnosticReports(transcribedServiceRequestBo);

		log.debug(OUTPUT, transcribedServiceRequestBo);
        return newServiceRequest.getId();
    }

    private void assertRequiredFields(TranscribedServiceRequestBo transcribedServiceRequestBo) {
        Assert.notNull(transcribedServiceRequestBo, "La orden es obligatoria");
        Assert.notNull(transcribedServiceRequestBo.getPatientId(), "El paciente es obligatorio");
        Assert.notNull(transcribedServiceRequestBo.getHealthcareProfessionalName(), "El nombre del médico es obligatorio");
		Assert.notEmpty(transcribedServiceRequestBo.getDiagnosticReports(), "La orden tiene que tener asociada al menos un estudio");
		HealthConditionBo healthCondition = transcribedServiceRequestBo.getHealthCondition();
		Assert.notNull(healthCondition, "Los estudios tienen que estar asociados a un problema");
        SnomedValidator snomedValidator =  new SnomedValidator();
		snomedValidator.isValid(healthCondition.getSnomed());
		transcribedServiceRequestBo.getDiagnosticReports().forEach(dr -> {
			snomedValidator.isValid(dr.getSnomed());
		});
    }

	private HealthCondition buildBasicHealthCondition(TranscribedServiceRequestBo transcribedServiceRequestBo) {
		HealthConditionBo healthConditionBo = transcribedServiceRequestBo.getHealthCondition();
		PatientInfoBo patientInfoBo = transcribedServiceRequestBo.getPatientInfo();

		String cie10Codes = calculateCie10Facade.execute(healthConditionBo.getSnomed().getSctid(), new Cie10FacadeRuleFeature(patientInfoBo.getGenderId(), patientInfoBo.getAge()));
		healthConditionBo.setCie10codes(cie10Codes);
		healthConditionBo.setStatusId(ConditionClinicalStatus.ACTIVE);
		healthConditionBo.setVerificationId(ConditionVerificationStatus.PRESUMPTIVE);

		HealthCondition healthCondition = new HealthCondition();
		healthCondition.setPatientId(patientInfoBo.getId());
		healthCondition.setSnomedId(getSnomedId(healthConditionBo.getSnomed()));
		healthCondition.setCie10Codes(cie10Codes);
		healthCondition.setStatusId(healthConditionBo.getStatusId());
		healthCondition.setVerificationStatusId(healthConditionBo.getVerificationId());
		healthCondition.setStartDate(dateTimeProvider.nowDate());
		healthCondition.setProblemId(ProblemType.PROBLEM);

		return healthConditionRepository.save(healthCondition);
	}

	private Integer getSnomedId(SnomedBo snomedTerm) {
		return snomedService.getSnomedId(snomedTerm).orElseGet(() -> snomedService.createSnomedTerm(snomedTerm));
	}

	private TranscribedServiceRequest createServiceRequest(TranscribedServiceRequestBo transcribedServiceRequestBo) {
		TranscribedServiceRequest newTranscribedServiceRequest = new TranscribedServiceRequest(
				transcribedServiceRequestBo.getHealthcareProfessionalName(),
				transcribedServiceRequestBo.getInstitutionName(),
				transcribedServiceRequestBo.getPatientId(),
				transcribedServiceRequestBo.getObservations()
		);
		return transcribedServiceRequestRepository.save(newTranscribedServiceRequest);
	}

	private void saveDiagnosticReports(TranscribedServiceRequestBo transcribedServiceRequestBo) {

		HealthConditionBo healthConditionBo = transcribedServiceRequestBo.getHealthCondition();

		transcribedServiceRequestBo.getDiagnosticReports().forEach(diagnosticReportBo -> {
			DiagnosticReport diagnosticReport = new DiagnosticReport();
			diagnosticReport.setPatientId(transcribedServiceRequestBo.getPatientId());
			diagnosticReport.setSnomedId(getSnomedId(diagnosticReportBo.getSnomed()));
			diagnosticReport.setHealthConditionId(healthConditionBo.getId());

			diagnosticReport = diagnosticReportRepository.save(diagnosticReport);
			diagnosticReportBo.setId(diagnosticReport.getId());
			diagnosticReportBo.setHealthCondition(healthConditionBo);

			transcribedServiceRequestDiagnosticReportRepository.save(new TranscribedServiceRequestDiagnosticReport(transcribedServiceRequestBo.getId(), diagnosticReportBo.getId()));
		});
	}
}
