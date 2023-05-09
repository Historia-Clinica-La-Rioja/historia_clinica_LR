package net.pladema.clinichistory.documents.infrastructure.output.repository;

import ar.lamansys.refcounterref.infraestructure.output.repository.counterreference.CounterReferenceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.clinichistory.documents.domain.CHDocumentBo;

import net.pladema.clinichistory.documents.domain.ClinicalRecordBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.medicalconsultation.appointment.service.DocumentAppointmentService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.staff.application.ports.HealthcareProfessionalStorage;
import net.pladema.staff.domain.ProfessionBo;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;

import net.pladema.user.service.HospitalUserService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClinicHistoryContextBuilder {

	private final PatientExternalService patientExternalService;
	private final SharedInstitutionPort sharedInstitutionPort;
	private final HealthcareProfessionalStorage healthcareProfessionalStorage;
	private final LocalDateMapper localDateMapper;
	private final HospitalUserService hospitalUserService;
	private final OutpatientConsultationRepository outpatientConsultationRepository;
	private final ServiceRequestRepository serviceRequestRepository;
	private final CounterReferenceRepository counterReferenceRepository;
	private final MedicationRequestRepository medicationRequestRepository;
	private final PatientMedicalCoverageService patientMedicalCoverageService;
	private final DocumentAppointmentService documentAppointmentService;
	private final DiaryService diaryService;
	private final FeatureFlagsService featureFlagsService;

	public ClinicHistoryContextBuilder(PatientExternalService patientExternalService,
									   SharedInstitutionPort sharedInstitutionPort,
									   HealthcareProfessionalStorage healthcareProfessionalStorage,
									   LocalDateMapper localDateMapper,
									   HospitalUserService hospitalUserService,
									   OutpatientConsultationRepository outpatientConsultationRepository,
									   ServiceRequestRepository serviceRequestRepository,
									   CounterReferenceRepository counterReferenceRepository,
									   MedicationRequestRepository medicationRequestRepository,
									   PatientMedicalCoverageService patientMedicalCoverageService,
									   DocumentAppointmentService documentAppointmentService,
									   DiaryService diaryService,
									   FeatureFlagsService featureFlagsService) {
		this.patientExternalService = patientExternalService;
		this.sharedInstitutionPort = sharedInstitutionPort;
		this.healthcareProfessionalStorage = healthcareProfessionalStorage;
		this.localDateMapper = localDateMapper;
		this.hospitalUserService = hospitalUserService;
		this.outpatientConsultationRepository = outpatientConsultationRepository;
		this.serviceRequestRepository = serviceRequestRepository;
		this.counterReferenceRepository = counterReferenceRepository;
		this.medicationRequestRepository = medicationRequestRepository;
		this.patientMedicalCoverageService = patientMedicalCoverageService;
		this.documentAppointmentService = documentAppointmentService;
		this.diaryService = diaryService;
		this.featureFlagsService = featureFlagsService;
	}


	public Map<String, Object> buildContext(CHDocumentBo document, Integer currentInstitutionId){
		Map<String, Object> ctx = new HashMap<>();
		boolean selfPerceived = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);
		//Patient Info
		ctx.put("patient", patientExternalService.getBasicDataFromPatient(document.getPatientId()));
		ctx.put("patientAge", document.getPatientAgePeriod().substring(1, document.getPatientAgePeriod().indexOf("Y")));
		//Encounter Info
		ctx.put("encounterId", document.getSourceId());
		ctx.put("encounterType", document.getEncounterType().getValue());
		ctx.put("startDate", document.getStartDate());
		ctx.put("endDate", document.getEndDate());

		ctx.put("institution", document.getInstitution());
		//Find the appointment related, if exists
		var appointment = documentAppointmentService.getDocumentAppointmentForDocument(document.getId());
		appointment.ifPresent(documentAppointment -> {
			var diary = diaryService.getCompleteDiaryByAppointment(documentAppointment.getAppointmentId());
			diary.ifPresent(completeDiaryBo ->{
				ctx.put("sector", completeDiaryBo.getSectorDescription());
				ctx.put("place", completeDiaryBo.getDoctorsOfficeDescription());
			});
		});
		getMedicalCoverageId(document).ifPresent(id -> {
			var patientMedicalCoverage = patientMedicalCoverageService.getCoverage(id);
			ctx.put("medicalCoverage", patientMedicalCoverage.orElse(null));
		});
		//Professional Info
		var professionalInformation = healthcareProfessionalStorage.fetchProfessionalByUserId(document.getCreatedBy());
		ctx.put("professionalCompleteName", (selfPerceived && professionalInformation.getNameSelfDetermination() != null && !professionalInformation.getNameSelfDetermination().isBlank() ? professionalInformation.getNameSelfDetermination() : professionalInformation.getFirstName()) + ' ' + professionalInformation.getLastName());
		var professionalRelatedProfession = professionalInformation.getProfessions().stream().filter(profession -> profession.getSpecialties().stream().anyMatch(specialty -> specialty.getSpecialty().getName().equals(document.getClinicalSpecialty()))).findFirst();
		ctx.put("professionalProfession", professionalRelatedProfession.<Object>map(ProfessionBo::getDescription).orElse(null));
		professionalRelatedProfession.ifPresent(profession -> {
			ctx.put("clinicalSpecialty", document.getClinicalSpecialty());

			var nationalLicenseData = profession.getLicenses().stream().filter(license -> license.getType().equals(ELicenseNumberTypeBo.NATIONAL)).findFirst();
			nationalLicenseData.ifPresent(licenseNumberBo -> ctx.put("nationalLicense", licenseNumberBo.getNumber()));

			var stateProvinceData = profession.getLicenses().stream().filter(license -> license.getType().equals(ELicenseNumberTypeBo.PROVINCE)).findFirst();
			stateProvinceData.ifPresent(licenseNumberBo -> ctx.put("stateLicense", licenseNumberBo.getNumber()));
		});
		//Clinical Records
		List<ClinicalRecordBo> clinicalRecords = document.getClinicalRecords();
		ctx.put("clinicalRecords", clinicalRecords);
		//Footer Info
		var printDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3"));
		var userInfo = hospitalUserService.getUserPersonInfo(UserInfo.getCurrentAuditor());
		ctx.put("user", (selfPerceived && userInfo.getNameSelfDetermination() != null && userInfo.getNameSelfDetermination().isBlank() ? userInfo.getNameSelfDetermination() : userInfo.getFirstName()) + ' ' + userInfo.getLastName());
		ctx.put("printDate", LocalDate.from(printDateTime));
		ctx.put("printTime", String.valueOf(printDateTime.getHour()) + ':' + String.valueOf(printDateTime.getMinute()));
		ctx.put("currentInstitution", sharedInstitutionPort.fetchInstitutionById(currentInstitutionId).getName());

		return ctx;
	}

	private List<ClinicalRecordBo> toClinicalRecords(List<String> list){
		List<ClinicalRecordBo> result = new ArrayList<>();
		for (String item: list){
			int index = item.indexOf(":");
			result.add(new ClinicalRecordBo(item.substring(0, index), item.substring(index + 1)));
		}
		return result;
	}

	private Optional<Integer> getMedicalCoverageId(CHDocumentBo documentBo){
		switch (documentBo.getDocumentTypeId()){
			case (DocumentType.OUTPATIENT):
				return outpatientConsultationRepository.getPatientMedicalCoverageId(documentBo.getSourceId());
			case (DocumentType.RECIPE):
				return serviceRequestRepository.getMedicalCoverageId(documentBo.getSourceId());
			case (DocumentType.COUNTER_REFERENCE):
				return counterReferenceRepository.getPatientMedicalCoverageId(documentBo.getSourceId());
			case (DocumentType.ORDER):
				return medicationRequestRepository.getMedicalCoverageId(documentBo.getSourceId());
			default:
				return Optional.empty();
		}
	}

}
