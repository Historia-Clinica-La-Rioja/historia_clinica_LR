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
import ar.lamansys.sgx.shared.strings.StringHelper;
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
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.service.PersonService;
import net.pladema.reports.controller.dto.FormVDto;
import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	private final PersonService personService;
	private final SharedPersonPort sharedPersonPort;
	private final FetchLastBedByInternmentPatientDate fetchLastBedByInternmentPatientDate;
	private final InternmentPatientService internmentPatientService;
	private final FetchLastBedByEmergencyEpisodePatientDate fetchLastBedByEmergencyEpisodePatientDate;
	private final EmergencyCareEpisodeService emergencyCareEpisodeService;
	private final InternmentEpisodeService internmentEpisodeService;

    public StoredFileBo run(Integer institutionId, Integer patientId, Integer serviceRequestId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, serviceRequestId {}", institutionId, patientId, serviceRequestId);

        StoredFileBo storedFileBo = AppFeature.HABILITAR_NUEVO_FORMATO_PDF_ORDENES_PRESTACION.isActive()
                    ? createDeliveryOrderForm(institutionId, patientId, serviceRequestId)
                    : createRecipeOrderTable(institutionId, patientId, serviceRequestId);

        log.debug("OUTPUT -> {}", storedFileBo);
        return storedFileBo;
    }

    private StoredFileBo createDeliveryOrderForm(Integer institutionId, Integer patientId, Integer serviceRequestId) {
		log.debug("Input parameters -> institutionId {}, patientId {}, serviceRequestId {}", institutionId, patientId, serviceRequestId);
		InstitutionBo institutionBo = institutionService.get(institutionId);
		Person person = personService.findByPatientId(patientId).orElseThrow();
		ServiceRequestBo serviceRequestBo = getServiceRequestInfoService.run(serviceRequestId);
		Integer internmentEpisodeId = internmentPatientService.internmentEpisodeInProcess(institutionId, patientId).getId();
		var medicalCoverageOpt = internmentEpisodeService.getMedicalCoverage(internmentEpisodeId);
		PatientMedicalCoverageBo medicalCoverage = null;
		if (medicalCoverageOpt.isPresent())
			medicalCoverage = medicalCoverageOpt.get();
		HealthcareProfessionalBo professional = healthcareProfessionalService.findActiveProfessionalById(serviceRequestBo.getDoctorId());
		List<ProfessionalLicenseNumberBo> licenses = getLicenseNumberByProfessional.run(professional.getId());
		ResponsibleDoctorBo responsibleDoctorBo = new ResponsibleDoctorBo(professional.getId(), professional.getFirstName(), professional.getLastName(), licenses
				.stream()
				.map(ProfessionalLicenseNumberBo::getCompleteTypeLicenseNumber)
				.collect(Collectors.toList()));
		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
		InternmentPatientBedRoomBo ipbr = getInternmentLastBed(institutionId, patientId, serviceRequestBo);
		EmergencyEpisodePatientBedRoomBo eepbr = getEmergencyEpisodeLastBed(institutionId, patientId, serviceRequestBo);
		String bedNumber = ipbr != null ? ipbr.getBed() : eepbr != null ? eepbr.getBed() : null;
		String roomNumber = ipbr != null ? ipbr.getRoom() : eepbr != null ? eepbr.getRoom() : null;
		FormVDto formVDto = mapToFormVDto(institutionBo, person, serviceRequestBo, patientId, responsibleDoctorBo, bedNumber, roomNumber, sharedPersonPort.getCompletePersonNameById(professional.getPersonId()), medicalCoverage);
        Map<String, Object> context = createDeliveryOrderFormContext(formVDto, serviceRequestBo);
        String template = "form_report";

        return new StoredFileBo(pdfService.generate(template, context),
                MediaType.APPLICATION_PDF_VALUE,
				String.format("%s_%s.pdf", patientDto.getIdentificationNumber(), serviceRequestId));
    }

	private EmergencyEpisodePatientBedRoomBo getEmergencyEpisodeLastBed(Integer institutionId, Integer patientId, ServiceRequestBo serviceRequestBo) {
		if (serviceRequestBo.getAssociatedSourceTypeId().equals(SourceType.EMERGENCY_CARE)) {
			Integer emergencyId = emergencyCareEpisodeService.getEmergencyEpisodeEpisodeIdByDate(institutionId, patientId, serviceRequestBo.getRequestDate());
			if (emergencyId != null)
				return fetchLastBedByEmergencyEpisodePatientDate.run(emergencyId, serviceRequestBo.getRequestDate(), institutionId);
		}
		return null;
	}

	private InternmentPatientBedRoomBo getInternmentLastBed(Integer institutionId, Integer patientId, ServiceRequestBo serviceRequestBo) {
		if (serviceRequestBo.getAssociatedSourceTypeId().equals(SourceType.HOSPITALIZATION)) {
			Integer internmentEpisodeId = internmentPatientService.getInternmentEpisodeIdByDate(institutionId, patientId, serviceRequestBo.getRequestDate());
			if (internmentEpisodeId != null)
				return fetchLastBedByInternmentPatientDate.run(internmentEpisodeId, serviceRequestBo.getRequestDate());
		}
		return null;
	}

	private FormVDto mapToFormVDto(InstitutionBo institutionBo,
								   Person person,
								   ServiceRequestBo serviceRequestBo,
								   Integer patientId,
								   ResponsibleDoctorBo responsibleDoctorBo,
								   String bedNumber,
								   String roomNumber,
								   String completeProfessionalName,
								   PatientMedicalCoverageBo medicalCoverageBo) {
		return new FormVDto(institutionBo.getName(),
				StringHelper.reverseString(sharedPersonPort.getCompletePersonNameById(person.getId())),
				sharedPersonPort.getPersonContactInfoById(person.getId()),
				serviceRequestBo.getRequestDate().toLocalDate(),
				medicalCoverageBo != null ? medicalCoverageBo.getMedicalCoverageName(): null,
				serviceRequestBo.getProblems().toString(),
				medicalCoverageBo != null ? medicalCoverageBo.getConditionValue(): null,
				institutionBo.getProvinceCode(),
				patientId,
				completeProfessionalName,
				responsibleDoctorBo.getLicenses(),
				bedNumber,
				roomNumber
		);
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
				String.format("%s_%s.pdf", patientDto.getIdentificationNumber(), serviceRequestId));
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

    private Map<String, Object> createDeliveryOrderFormContext(FormVDto formVDto, ServiceRequestBo serviceRequestBo) {
		log.trace("Input parameters -> formVDto {}, serviceRequestBo {}", formVDto, serviceRequestBo);
        Map<String, Object> ctx = new HashMap<>();
		ctx.put("completePatientName", formVDto.getCompletePatientName());
		ctx.put("address", formVDto.getAddress());
		ctx.put("reportDate", formVDto.getReportDate());
		ctx.put("hcnId", formVDto.getHcnId());
		ctx.put("medicalCoverage", formVDto.getMedicalCoverage());
		ctx.put("establishment", formVDto.getEstablishment());
		ctx.put("code", formVDto.getEstablishmentProvinceCode());
		ctx.put("ce", serviceRequestBo.getAssociatedSourceTypeId().equals(SourceType.OUTPATIENT));
		ctx.put("problems", serviceRequestBo.getDiagnosticReports().get(0).getHealthCondition().getSnomedPt());
		ctx.put("studies", serviceRequestBo.getDiagnosticReports().stream().map(diag -> diag.getSnomed().getPt()).collect(Collectors.toList()));
		ctx.put("cie10Codes", serviceRequestBo.getDiagnosticReports().get(0).getHealthCondition().getCie10codes());
		ctx.put("completeProfessionalName", formVDto.getCompleteProfessionalName());
		ctx.put("licenses", formVDto.getLicenses());
		ctx.put("medicalCoverageCondition", formVDto.getMedicalCoverageCondition());
		ctx.put("room", formVDto.getRoomNumber());
		ctx.put("bed", formVDto.getBedNumber());
        log.trace("Output -> {}", ctx);
        return ctx;
    }
}
