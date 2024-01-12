package net.pladema.clinichistory.requests.servicerequests.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.files.images.ImageFileService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.SnomedValidator;
import net.pladema.clinichistory.requests.controller.dto.TranscribedPrescriptionDto;
import net.pladema.clinichistory.requests.servicerequests.repository.TranscribedServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.TranscribedServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.service.CreateTranscribedServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;

@Service
public class CreateTranscribedServiceRequestServiceImpl implements CreateTranscribedServiceRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateTranscribedServiceRequestServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";

	private static final String VERIFICATION_STATUS_PRESUMPTIVE = "76104008";

	public static final String PROBLEM = "55607006";

    private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;

	private final DiagnosticReportRepository diagnosticReportRepository;

	private final HealthConditionRepository healthConditionRepository;

	private final ImageFileService imageFileService;

	private final DateTimeProvider dateTimeProvider;

	private final CalculateCie10Facade calculateCie10Facade;

	private final SnomedService snomedService;

    public CreateTranscribedServiceRequestServiceImpl(TranscribedServiceRequestRepository transcribedServiceRequestRepository, DiagnosticReportRepository diagnosticReportRepository, HealthConditionRepository healthConditionRepository, ImageFileService imageFileService, DateTimeProvider dateTimeProvider, CalculateCie10Facade calculateCie10Facade, SnomedService snomedService){
        this.transcribedServiceRequestRepository = transcribedServiceRequestRepository;
		this.diagnosticReportRepository = diagnosticReportRepository;
		this.healthConditionRepository = healthConditionRepository;
		this.imageFileService = imageFileService;
		this.dateTimeProvider = dateTimeProvider;
		this.calculateCie10Facade = calculateCie10Facade;
		this.snomedService = snomedService;
	}

    @Override
    public Integer execute(TranscribedPrescriptionDto transcribedPrescriptionDto, BasicPatientDto patientDto) {
        LOG.debug("Input parameters -> transcribedPrescriptionDto {}, patientDto {}", transcribedPrescriptionDto, patientDto);
		TranscribedServiceRequestBo transcribedServiceRequestBo = new TranscribedServiceRequestBo(transcribedPrescriptionDto, patientDto.getId());
        assertRequiredFields(transcribedServiceRequestBo);
		PatientInfoBo patientInfoBo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
		Integer studyId = diagnosticReportRepository.save(getNewDiagnosticReport(patientInfoBo, transcribedServiceRequestBo)).getId();

        TranscribedServiceRequest newServiceRequest = createServiceRequest(transcribedServiceRequestBo, studyId);
		transcribedServiceRequestBo.setTranscribedServiceRequestId(newServiceRequest.getId());
		LOG.debug(OUTPUT, transcribedServiceRequestBo);
        return newServiceRequest.getId();
    }

    private void assertRequiredFields(TranscribedServiceRequestBo transcribedServiceRequestBo) {
        Assert.notNull(transcribedServiceRequestBo, "La orden es obligatoria");
        Assert.notNull(transcribedServiceRequestBo.getPatientId(), "El paciente es obligatorio");
        Assert.notNull(transcribedServiceRequestBo.getHealthcareProfessionalName(), "El nombre del médico es obligatorio");
        Assert.notNull(transcribedServiceRequestBo.getStudy(), "La orden tiene que tener asociada al menos un estudio");
		Assert.notNull(transcribedServiceRequestBo.getHealthCondition(), "El estudio tiene que estar asociado a un problema");
        SnomedValidator snomedValidator =  new SnomedValidator();
		snomedValidator.isValid(transcribedServiceRequestBo.getHealthCondition());
		snomedValidator.isValid(transcribedServiceRequestBo.getStudy());
    }

	private Integer getSnomedId(SnomedBo snomedTerm) {
		return snomedService.getSnomedId(snomedTerm).orElseGet(() -> snomedService.createSnomedTerm(snomedTerm));
	}

	private DiagnosticReport getNewDiagnosticReport(PatientInfoBo patientInfoBo, TranscribedServiceRequestBo transcribedServiceRequestBo) {
		LOG.debug("Input parameters -> patientInfoBo {}, transcribedServiceRequestBo {}", patientInfoBo, transcribedServiceRequestBo);
		DiagnosticReport result = new DiagnosticReport();
		result.setPatientId(patientInfoBo.getId());
		result.setSnomedId(getSnomedId(transcribedServiceRequestBo.getStudy()));
		result.setObservations(transcribedServiceRequestBo.getObservations());
		HealthCondition savedHealthCondition = healthConditionRepository.save(buildBasicHealthCondition(patientInfoBo, transcribedServiceRequestBo.getHealthCondition()));
		Integer healthConditionId = savedHealthCondition.getId();
		result.setHealthConditionId(healthConditionId);
		transcribedServiceRequestBo.setCie10Codes(savedHealthCondition.getCie10Codes());
		LOG.debug(OUTPUT, result);
		return result;
	}

	private HealthCondition buildBasicHealthCondition(PatientInfoBo patientInfoBo, SnomedBo healthCondition) {
		LOG.debug("Input parameters -> patientInfoBo {}, healthCondition {}", patientInfoBo, healthCondition);
		HealthCondition newHealthCondition = new HealthCondition();
		newHealthCondition.setSnomedId(getSnomedId(healthCondition));
		String cie10Codes = calculateCie10Facade.execute(healthCondition.getSctid(), new Cie10FacadeRuleFeature(patientInfoBo.getGenderId(), patientInfoBo.getAge()));
		newHealthCondition.setPatientId(patientInfoBo.getId());
		newHealthCondition.setCie10Codes(cie10Codes);
		newHealthCondition.setStatusId(ConditionClinicalStatus.ACTIVE);
		newHealthCondition.setVerificationStatusId(VERIFICATION_STATUS_PRESUMPTIVE);
		newHealthCondition.setStartDate(dateTimeProvider.nowDate());
		newHealthCondition.setProblemId(PROBLEM);
		LOG.debug(OUTPUT, healthCondition);
		return newHealthCondition;
	}

    private TranscribedServiceRequest createServiceRequest(TranscribedServiceRequestBo transcribedServiceRequestBo, Integer studyId) {
        TranscribedServiceRequest newTranscribedServiceRequest = new TranscribedServiceRequest(
				studyId,
				transcribedServiceRequestBo.getHealthcareProfessionalName(),
				transcribedServiceRequestBo.getInstitutionName(),
				transcribedServiceRequestBo.getPatientId()
        );
        return this.transcribedServiceRequestRepository.save(newTranscribedServiceRequest);
    }
}
