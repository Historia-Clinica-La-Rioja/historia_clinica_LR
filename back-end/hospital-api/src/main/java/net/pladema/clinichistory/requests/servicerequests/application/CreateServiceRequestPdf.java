package net.pladema.clinichistory.requests.servicerequests.application;

import static ar.lamansys.sgx.shared.files.pdf.EPDFTemplate.FORM_REPORT;
import static ar.lamansys.sgx.shared.files.pdf.EPDFTemplate.RECIPE_ORDER_TABLE;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.DocumentAuthorFinder;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.pdf.GeneratedPdfResponseService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.GeneratedBlobBo;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.InternmentPatientService;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;
import net.pladema.clinichistory.requests.servicerequests.service.GetServiceRequestInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.FetchLastBedByEmergencyEpisodePatientDate;
import net.pladema.emergencycare.service.domain.EmergencyEpisodePatientBedRoomBo;
import net.pladema.establishment.service.FetchLastBedByInternmentPatientDate;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBo;
import net.pladema.establishment.service.domain.InternmentPatientBedRoomBo;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.service.PatientExternalMedicalCoverageService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.reports.service.domain.FormVBo;
import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateServiceRequestPdf {

    private final GetServiceRequestInfoService getServiceRequestInfoService;
    private final PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;
    private final SharedInstitutionPort sharedInstitutionPort;
    private final PatientExternalService patientExternalService;
    private final DocumentAuthorFinder documentAuthorFinder;
	private final GeneratedPdfResponseService generatedPdfResponseService;
	private final InstitutionService institutionService;
	private final HealthcareProfessionalService healthcareProfessionalService;
	private final GetLicenseNumberByProfessional getLicenseNumberByProfessional;
	private final SharedPersonPort sharedPersonPort;
	private final FetchLastBedByInternmentPatientDate fetchLastBedByInternmentPatientDate;
	private final InternmentPatientService internmentPatientService;
	private final FetchLastBedByEmergencyEpisodePatientDate fetchLastBedByEmergencyEpisodePatientDate;
	private final EmergencyCareEpisodeService emergencyCareEpisodeService;
	private final InternmentEpisodeService internmentEpisodeService;
	private final CreateDeliveryOrderBaseForm createDeliveryOrderBaseForm;
	private final CreateDeliveryOrderFormContext createDeliveryOrderFormContext;
	private final PatientMedicalCoverageService patientMedicalCoverageService;
	private final FeatureFlagsService featureFlagsService;

    public GeneratedBlobBo run(Integer institutionId, Integer patientId, Integer serviceRequestId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, serviceRequestId {}", institutionId, patientId, serviceRequestId);

        var storedFileBo = AppFeature.HABILITAR_NUEVO_FORMATO_PDF_ORDENES_PRESTACION.isActive()
                    ? createDeliveryOrderForm(patientId, serviceRequestId)
                    : createRecipeOrderTable(institutionId, patientId, serviceRequestId);

        log.debug("OUTPUT -> {}", storedFileBo);
        return storedFileBo;
    }

	private GeneratedBlobBo createDeliveryOrderForm(Integer patientId, Integer serviceRequestId) {

		ServiceRequestBo serviceRequest = getServiceRequestInfoService.run(serviceRequestId);
		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
		FormVBo baseFormV = createDeliveryOrderBaseForm.run(patientId, serviceRequest, patientDto);
		FormVBo formV = completeFormV(baseFormV, serviceRequest);
		Map<String, Object> context = createDeliveryOrderFormContext.run(formV, serviceRequest);

		return generatedPdfResponseService.generatePdf(FORM_REPORT,
				context,
				this.resolveNameFile(patientDto, serviceRequestId)
		);
	}

	private FormVBo completeFormV(FormVBo formV, ServiceRequestBo serviceRequest) {

		Integer patientId = formV.getHcnId();

		InstitutionBo institutionBo = institutionService.get(serviceRequest.getInstitutionId());
		String institutionName = institutionBo.getName();
		String institutionProvinceCode = institutionBo.getProvinceCode();

		HealthcareProfessionalBo professional = healthcareProfessionalService.findActiveProfessionalById(serviceRequest.getDoctorId());
		String completeProfessionalName = sharedPersonPort.getCompletePersonNameById(professional.getPersonId());

		List<ProfessionalLicenseNumberBo> licenses = getLicenseNumberByProfessional.run(professional.getId());
		ResponsibleDoctorBo responsibleDoctorBo = new ResponsibleDoctorBo(professional.getId(), professional.getFirstName(), professional.getLastName(), licenses
				.stream()
				.map(ProfessionalLicenseNumberBo::getCompleteTypeLicenseNumber)
				.collect(Collectors.toList()));
		List<String> responsibleNumberLicenses = responsibleDoctorBo.getLicenses();

		String problems = serviceRequest.getProblems().toString();

		PatientMedicalCoverageBo medicalCoverage = setMedicalCoverage(patientId, serviceRequest);
		InternmentPatientBedRoomBo ipbr = getInternmentLastBed(patientId, serviceRequest);
		EmergencyEpisodePatientBedRoomBo eepbr = getEmergencyEpisodeLastBed(patientId, serviceRequest);

		return mapToCompleteFormV(formV, institutionName, institutionProvinceCode, completeProfessionalName, responsibleNumberLicenses, problems, medicalCoverage, ipbr, eepbr);
	}

	private FormVBo mapToCompleteFormV(FormVBo formV,
									   String institutionName,
									   String institutionProvinceCode,
									   String completeProfessionalName,
									   List<String> responsibleNumberLicenses,
									   String problems,
									   PatientMedicalCoverageBo medicalCoverage,
									   InternmentPatientBedRoomBo ipbr,
									   EmergencyEpisodePatientBedRoomBo eepbr) {

		var medicalCoverageName = medicalCoverage != null
				? medicalCoverage.getMedicalCoverageName()
				: null;
		var medicalCoverageConditionValue = medicalCoverage != null
				? medicalCoverage.getConditionValue()
				: null;
		var medicalCoverageAffiliateNumber = medicalCoverage != null
				? medicalCoverage.getAffiliateNumber()
				: null;

		var bedNumber = ipbr != null
				? ipbr.getBed()
				: eepbr != null
				? eepbr.getBed()
				: null;

		var roomNumber = ipbr != null
				? ipbr.getRoom()
				: eepbr != null
				? eepbr.getRoom()
				: null;

		formV.setEstablishment(institutionName);
		formV.setEstablishmentProvinceCode(institutionProvinceCode);
		formV.setCompleteProfessionalName(completeProfessionalName);
		formV.setLicenses(responsibleNumberLicenses);
		formV.setProblems(problems);
		formV.setMedicalCoverage(medicalCoverageName);
		formV.setMedicalCoverageCondition(medicalCoverageConditionValue);
		formV.setAffiliateNumber(medicalCoverageAffiliateNumber);
		formV.setBedNumber(bedNumber);
		formV.setRoomNumber(roomNumber);

		return formV;
	}

	private PatientMedicalCoverageBo setMedicalCoverage(Integer patientId, ServiceRequestBo serviceRequest) {
		PatientMedicalCoverageBo medicalCoverage = new PatientMedicalCoverageBo();

		if (serviceRequest.getAssociatedSourceTypeId().equals(SourceType.HOSPITALIZATION))
			medicalCoverage = setInternmentMedicalCoverageOrder(patientId, serviceRequest);

		if (List.of(SourceType.OUTPATIENT, SourceType.EMERGENCY_CARE).contains(serviceRequest.getAssociatedSourceTypeId()))
			medicalCoverage = setOutpatientAndEmergencyCareEpisodeCoverageOrder(serviceRequest);

		return medicalCoverage.getMedicalCoverage() != null ? medicalCoverage : null;
	}

	private PatientMedicalCoverageBo setOutpatientAndEmergencyCareEpisodeCoverageOrder(ServiceRequestBo serviceRequestBo) {
		return patientMedicalCoverageService.getCoverage(serviceRequestBo.getMedicalCoverageId()).orElse(new PatientMedicalCoverageBo());
	}

	private PatientMedicalCoverageBo setInternmentMedicalCoverageOrder(Integer patientId, ServiceRequestBo serviceRequestBo) {
		Integer internmentEpisodeId = internmentPatientService.getInternmentEpisodeIdByDate(serviceRequestBo.getInstitutionId(), patientId, serviceRequestBo.getRequestDate());
		return internmentEpisodeService.getMedicalCoverage(internmentEpisodeId).orElse(new PatientMedicalCoverageBo());
	}

	private EmergencyEpisodePatientBedRoomBo getEmergencyEpisodeLastBed(Integer patientId, ServiceRequestBo serviceRequestBo) {
		if (serviceRequestBo.getAssociatedSourceTypeId().equals(SourceType.EMERGENCY_CARE)) {
			Integer emergencyId = emergencyCareEpisodeService.getEmergencyEpisodeEpisodeIdByDate(serviceRequestBo.getInstitutionId(), patientId, serviceRequestBo.getRequestDate());
			if (emergencyId != null)
				return fetchLastBedByEmergencyEpisodePatientDate.run(emergencyId, serviceRequestBo.getRequestDate(), serviceRequestBo.getInstitutionId());
		}
		return null;
	}

	private InternmentPatientBedRoomBo getInternmentLastBed(Integer patientId, ServiceRequestBo serviceRequestBo) {
		if (serviceRequestBo.getAssociatedSourceTypeId().equals(SourceType.HOSPITALIZATION)) {
			Integer internmentEpisodeId = internmentPatientService.getInternmentEpisodeIdByDate(serviceRequestBo.getInstitutionId(), patientId, serviceRequestBo.getRequestDate());
			if (internmentEpisodeId != null)
				return fetchLastBedByInternmentPatientDate.run(internmentEpisodeId, serviceRequestBo.getRequestDate());
		}
		return null;
	}

    private GeneratedBlobBo createRecipeOrderTable(Integer institutionId, Integer patientId, Integer serviceRequestId) {
        var serviceRequestBo = getServiceRequestInfoService.run(serviceRequestId);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        var institutionDto = sharedInstitutionPort.fetchInstitutionById(institutionId);
        var patientCoverageDto = patientExternalMedicalCoverageService.getCoverage(serviceRequestBo.getMedicalCoverageId());
        var context = createRecipeOrderTableContext(serviceRequestBo, patientDto, patientCoverageDto, institutionDto);

		return generatedPdfResponseService.generatePdf(RECIPE_ORDER_TABLE,
			context,
			this.resolveNameFile(patientDto, serviceRequestId)
		);
    }

    private Map<String, Object> createRecipeOrderTableContext(ServiceRequestBo serviceRequestBo,
                                                              BasicPatientDto patientDto,
                                                              PatientMedicalCoverageDto patientCoverageDto,
                                                              InstitutionInfoDto institutionDto) {
        log.trace("Input parameters -> serviceRequestBo {}, patientDto {}, patientCoverageDto {}, institutionInfoDto {}",
                serviceRequestBo, patientDto, patientCoverageDto, institutionDto);

        Map<String, Object> ctx = new HashMap<>();
        ctx.put("recipe", false);
        ctx.put("order", true);
        ctx.put("request", serviceRequestBo);
        ctx.put("patient", patientDto);
		var professional = documentAuthorFinder.getAuthor(serviceRequestBo.getId());
		ctx.put("professional", professional);
        ctx.put("professionalName", sharedPersonPort.getCompletePersonNameById(professional.getPersonId()));
        ctx.put("patientCoverage", patientCoverageDto);
        ctx.put("institution", institutionDto);
        var date = serviceRequestBo.getRequestDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        ctx.put("requestDate", date);
		ctx.put("selfPerceivedFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));

        log.trace("Output -> {}", ctx);
        return ctx;
    }

	private String resolveNameFile(BasicPatientDto patientDto, Integer serviceRequestId) {
		return String.format("%s%s", patientDto.getIdentificationNumber() != null ? patientDto.getIdentificationNumber().concat("_") : "", serviceRequestId);
	}
}
