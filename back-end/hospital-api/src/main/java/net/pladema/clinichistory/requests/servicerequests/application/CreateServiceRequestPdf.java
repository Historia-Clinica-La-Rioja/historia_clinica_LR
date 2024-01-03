package net.pladema.clinichistory.requests.servicerequests.application;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.DocumentAuthorFinder;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
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
import net.pladema.reports.controller.dto.FormVDto;
import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateServiceRequestPdf {

    private final GetServiceRequestInfoService getServiceRequestInfoService;
    private final PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;
    private final SharedInstitutionPort sharedInstitutionPort;
    private final PatientExternalService patientExternalService;
    private final DocumentAuthorFinder documentAuthorFinder;
    private final FeatureFlagsService featureFlagsService;
    private final PdfService pdfService;
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

    public StoredFileBo run(Integer institutionId, Integer patientId, Integer serviceRequestId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, serviceRequestId {}", institutionId, patientId, serviceRequestId);

        StoredFileBo storedFileBo = AppFeature.HABILITAR_NUEVO_FORMATO_PDF_ORDENES_PRESTACION.isActive()
                    ? createDeliveryOrderForm(patientId, serviceRequestId)
                    : createRecipeOrderTable(institutionId, patientId, serviceRequestId);

        log.debug("OUTPUT -> {}", storedFileBo);
        return storedFileBo;
    }

	private StoredFileBo createDeliveryOrderForm(Integer patientId, Integer serviceRequestId) {

		ServiceRequestBo serviceRequestBo = getServiceRequestInfoService.run(serviceRequestId);
		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
		FormVDto baseFormVDto = createDeliveryOrderBaseForm.run(patientId, serviceRequestBo, patientDto);
		FormVDto formVDto = completeFormV(baseFormVDto, serviceRequestBo);
		Map<String, Object> context = createDeliveryOrderFormContext.run(formVDto, serviceRequestBo);
        String template = "form_report";

        return new StoredFileBo(pdfService.generate(template, context),
                MediaType.APPLICATION_PDF_VALUE,
				this.resolveNameFile(patientDto, serviceRequestId));
	}

	private FormVDto completeFormV(FormVDto formVDto, ServiceRequestBo serviceRequestBo) {

		Integer patientId = formVDto.getHcnId();

		InstitutionBo institutionBo = institutionService.get(serviceRequestBo.getInstitutionId());
		String institutionName = institutionBo.getName();
		String institutionProvinceCode = institutionBo.getProvinceCode();

		HealthcareProfessionalBo professional = healthcareProfessionalService.findActiveProfessionalById(serviceRequestBo.getDoctorId());
		String completeProfessionalName = sharedPersonPort.getCompletePersonNameById(professional.getPersonId());

		List<ProfessionalLicenseNumberBo> licenses = getLicenseNumberByProfessional.run(professional.getId());
		ResponsibleDoctorBo responsibleDoctorBo = new ResponsibleDoctorBo(professional.getId(), professional.getFirstName(), professional.getLastName(), licenses
				.stream()
				.map(ProfessionalLicenseNumberBo::getCompleteTypeLicenseNumber)
				.collect(Collectors.toList()));
		List<String> responsibleNumberLicenses = responsibleDoctorBo.getLicenses();

		String problems = serviceRequestBo.getProblems().toString();

		PatientMedicalCoverageDto medicalCoverage = setMedicalCoverage(patientId, serviceRequestBo);
		InternmentPatientBedRoomBo ipbr = getInternmentLastBed(patientId, serviceRequestBo);
		EmergencyEpisodePatientBedRoomBo eepbr = getEmergencyEpisodeLastBed(patientId, serviceRequestBo);

		return mapToCompleteFormV(formVDto, institutionName, institutionProvinceCode, completeProfessionalName, responsibleNumberLicenses, problems, medicalCoverage, ipbr, eepbr);
	}

	private FormVDto mapToCompleteFormV(FormVDto formVDto,
										String institutionName,
										String institutionProvinceCode,
										String completeProfessionalName,
										List<String> responsibleNumberLicenses,
										String problems,
										PatientMedicalCoverageDto medicalCoverage,
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

		formVDto.setEstablishment(institutionName);
		formVDto.setEstablishmentProvinceCode(institutionProvinceCode);
		formVDto.setCompleteProfessionalName(completeProfessionalName);
		formVDto.setLicenses(responsibleNumberLicenses);
		formVDto.setProblems(problems);
		formVDto.setMedicalCoverage(medicalCoverageName);
		formVDto.setMedicalCoverageCondition(medicalCoverageConditionValue);
		formVDto.setAffiliateNumber(medicalCoverageAffiliateNumber);
		formVDto.setBedNumber(bedNumber);
		formVDto.setRoomNumber(roomNumber);

		return formVDto;
	}

	private PatientMedicalCoverageDto setMedicalCoverage(Integer patientId, ServiceRequestBo serviceRequestBo) {
		PatientMedicalCoverageDto medicalCoverage = new PatientMedicalCoverageDto();

		if (serviceRequestBo.getAssociatedSourceTypeId().equals(SourceType.HOSPITALIZATION))
			medicalCoverage = setInternmentMedicalCoverageOrder(patientId, serviceRequestBo);

		if (List.of(SourceType.OUTPATIENT, SourceType.EMERGENCY_CARE).contains(serviceRequestBo.getAssociatedSourceTypeId()))
			medicalCoverage = setOutpatientAndEmergencyCareEpisodeCoverageOrder(serviceRequestBo);

		return medicalCoverage.getMedicalCoverage() != null ? medicalCoverage : null;
	}

	private PatientMedicalCoverageDto setOutpatientAndEmergencyCareEpisodeCoverageOrder(ServiceRequestBo serviceRequestBo) {
		PatientMedicalCoverageDto medicalCoverage = new PatientMedicalCoverageDto();
		PatientMedicalCoverageDto patientMedicalCoverageDto = patientExternalMedicalCoverageService.getCoverage(serviceRequestBo.getMedicalCoverageId());
		medicalCoverage.setMedicalCoverage(patientMedicalCoverageDto != null ? patientMedicalCoverageDto.getMedicalCoverage(): null);
		medicalCoverage.setCondition(patientMedicalCoverageDto != null ? patientMedicalCoverageDto.getCondition(): null);
		medicalCoverage.setAffiliateNumber(patientMedicalCoverageDto != null ? patientMedicalCoverageDto.getAffiliateNumber(): null);
		return medicalCoverage;
	}

	private PatientMedicalCoverageDto setInternmentMedicalCoverageOrder(Integer patientId, ServiceRequestBo serviceRequestBo) {
		PatientMedicalCoverageDto medicalCoverage = new PatientMedicalCoverageDto();
		Integer internmentEpisodeId = internmentPatientService.getInternmentEpisodeIdByDate(serviceRequestBo.getInstitutionId(), patientId, serviceRequestBo.getRequestDate());
		internmentEpisodeService.getMedicalCoverage(internmentEpisodeId)
				.ifPresent(medicalCoverageBo -> {
					medicalCoverage.setMedicalCoverage(medicalCoverageBo.getMedicalCoverage().newInstance());
					medicalCoverage.setCondition(medicalCoverageBo.getCondition());
					medicalCoverage.setAffiliateNumber(medicalCoverageBo.getAffiliateNumber());
				});
		return medicalCoverage;
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

    private StoredFileBo createRecipeOrderTable(Integer institutionId, Integer patientId, Integer serviceRequestId) {
        var serviceRequestBo = getServiceRequestInfoService.run(serviceRequestId);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        var institutionDto = sharedInstitutionPort.fetchInstitutionById(institutionId);
        var patientCoverageDto = patientExternalMedicalCoverageService.getCoverage(serviceRequestBo.getMedicalCoverageId());
        var context = createRecipeOrderTableContext(serviceRequestBo, patientDto, patientCoverageDto, institutionDto);

        String template = "recipe_order_table";

        return new StoredFileBo(pdfService.generate(template, context),
                MediaType.APPLICATION_PDF_VALUE,
				this.resolveNameFile(patientDto, serviceRequestId));
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
        ctx.put("professional", documentAuthorFinder.getAuthor(serviceRequestBo.getId()));
        ctx.put("patientCoverage", patientCoverageDto);
        ctx.put("institution", institutionDto);
        var date = serviceRequestBo.getRequestDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        ctx.put("nameSelfDeterminationFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
        ctx.put("requestDate", date);

        log.trace("Output -> {}", ctx);
        return ctx;
    }

	private String resolveNameFile(BasicPatientDto patientDto, Integer serviceRequestId) {
		return String.format("%s%s.pdf", patientDto.getIdentificationNumber() != null ? patientDto.getIdentificationNumber().concat("_") : "", serviceRequestId);
	}
}
