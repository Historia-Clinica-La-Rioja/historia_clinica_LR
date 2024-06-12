package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.DiagnosticReportInfoService;
import net.pladema.clinichistory.requests.transcribed.application.getbyappointmentid.GetTranscribedServiceRequestByAppointmentId;
import net.pladema.establishment.service.EquipmentService;
import net.pladema.establishment.service.OrchestratorService;
import net.pladema.establishment.service.domain.EquipmentBO;
import net.pladema.establishment.service.domain.OrchestratorBO;
import net.pladema.medicalconsultation.appointment.repository.EquipmentAppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.repository.HistoricAppointmentStateRepository;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.EquipmentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.appointment.service.exceptions.AlreadyPublishedWorklistException;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.modality.service.ModalityService;
import net.pladema.modality.service.domain.ModalityBO;
import net.pladema.patient.controller.service.PatientExternalService;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class EquipmentAppointmentServiceImpl implements EquipmentAppointmentService {

	private static final String OUTPUT = "Output -> {}";

	private final AppointmentService appointmentService;

	private final EquipmentService equipmentService;

	private final EquipmentDiaryService equipmentDiaryService;

	private final OrchestratorService orchestratorService;

	private final EquipmentAppointmentAssnRepository equipmentAppointmentAssnRepository;

	private final ModalityService modalityService;

	private final PatientExternalService patientExternalService;

	private final SharedReferenceCounterReference sharedReferenceCounterReference;

	private final AppointmentOrderImageService appointmentOrderImageService;

	private final DiagnosticReportInfoService diagnosticReportInfoService;

	private final GetTranscribedServiceRequestByAppointmentId getTranscribedServiceRequestByAppointmentId;

	private final HistoricAppointmentStateRepository historicAppointmentStateRepository;

	@Override
	public Optional<AppointmentBo> getEquipmentAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		AppointmentBo result = equipmentAppointmentAssnRepository.getEquipmentAppointment(appointmentId).stream()
				.findFirst()
				.map(AppointmentBo::fromAppointmentVo)
				.map(appointmentBo -> {
					Integer diaryId = appointmentBo.getDiaryId();
					setIsAppointmentProtected(appointmentBo, diaryId);
					appointmentBo.setOrderData(diagnosticReportInfoService.getByAppointmentId(appointmentId));
					appointmentBo.setTranscribedOrderData(getTranscribedServiceRequestByAppointmentId.run(appointmentId).orElse(null));
                    return appointmentBo;
                })
				.orElse(null);

		log.debug(OUTPUT, result);
		return Optional.ofNullable(result);
	}

	private void setIsAppointmentProtected(AppointmentBo appointment, Integer diaryId) {
		boolean isProtected = !sharedReferenceCounterReference.getProtectedAppointmentsIds(List.of(diaryId)).isEmpty();
		appointment.setProtected(isProtected);
	}

	@Override
	public boolean updateEquipmentState(Integer appointmentId, short appointmentStateId, Integer userId, String reason) {
		return false;
	}

	@Override
	public AppointmentBo updateEquipmentAppointment(UpdateAppointmentBo appointmentDto) {
		return null;
	}

	@Override
	public MqttMetadataBo publishWorkList(Integer institutionId, Integer appointmentId) throws AlreadyPublishedWorklistException {

		AppointmentBo appointment = appointmentService.getEquipmentAppointment(appointmentId).orElse(null);
		if (appointment == null){
			return null;
		}

		Integer diaryId = appointment.getDiaryId();
		CompleteEquipmentDiaryBo equipmentDiary = equipmentDiaryService.getEquipmentDiary(diaryId).orElse(null);
		if (equipmentDiary == null){
			return null;
		}

		Integer equipmentId = equipmentDiary.getEquipmentId();
		EquipmentBO equipmentBO =equipmentService.getEquipment(equipmentId);
		if (equipmentBO == null){
			return null;
		}

		Integer orchestratorId = equipmentBO.getOrchestratorId();
		OrchestratorBO orchestrator = orchestratorService.getOrchestrator(orchestratorId);
		if (orchestrator == null){
			return null;
		}

		ModalityBO modalityBO = modalityService.getModality(equipmentBO.getModalityId());
		if (modalityBO == null){
			return null;
		}

		Integer patientId =appointment.getPatientId();
		BasicPatientDto basicDataPatient = patientExternalService.getBasicDataFromPatient(patientId);
		if (basicDataPatient == null){
			return null;
		}

		boolean hasAlreadyPublishedWorkList = historicAppointmentStateRepository.hasHistoricallyConfirmedAtLeastOnes(appointmentId);
		if (hasAlreadyPublishedWorkList) {
			log.debug("Already published worklist from appointmentId {}", appointmentId);
			throw  new AlreadyPublishedWorklistException();
		}

		String identificationNumber = basicDataPatient.getIdentificationNumber();
		String identification = identificationNumber == null ?basicDataPatient.getId()+"": identificationNumber;
		String UID= "1." + ThreadLocalRandom.current().nextInt(1,10) + "." +
				ThreadLocalRandom.current().nextInt(1,10) + "." +
				ThreadLocalRandom.current().nextInt(1,100) + "." +
				ThreadLocalRandom.current().nextInt(1,100) + "." +
				ThreadLocalRandom.current().nextInt(1,1000) + "." +
				ThreadLocalRandom.current().nextInt(1,1000) + "." +
				identification;
		String date = appointment.getDate().toString().replace("-","");
		String time = appointment.getHour().toString().replace(":","") + "00";
		String birthDate = null;
		char gender = 0;
		BasicDataPersonDto person = basicDataPatient.getPerson();
		if (person != null) {
			birthDate = person.getBirthDate() != null ? person.getBirthDate().toString().replace("-", "") : null;

			gender = person.getGender() != null
					&& person.getGender().getDescription() != null
					&& basicDataPatient.getPerson().getGender().getDescription().toCharArray().length > 0 ? basicDataPatient.getPerson().getGender().getDescription().toCharArray()[0] : ' ';
		}
		String accessionNumber =   "    \"AccessionNumber\": \"" + institutionId +"-" + appointmentId + "\", \n";
		String studyInstanceUID =   "    \"StudyInstanceUID\": \"" + UID+ "\", \n";
		String aeTitle =   "    \"ScheduledStationAETitle\": \"" + equipmentBO.getAeTitle() + "\",\n";
		String startDate = "    \"ScheduledProcedureStepStartDate\": \"" + date + "\",\n";
		String startTime = "    \"ScheduledProcedureStepStartTime\": \"" + time + "\",\n";
		String patientIdStr = "    \"PatientID\": \"" + identification + "\",\n";
		String patientName = "    \"PatientName\": \"" + basicDataPatient.getLastName() + " " + basicDataPatient.getFirstName() + "\",\n";
		String patientBirthDate = "    \"PatientBirthDate\": \"" + birthDate + "\",\n";
		String patientSex = "    \"PatientSex\": \"" + gender + "\",\n";
		String studyDescription = "    \"StudyDescription\": \"" + "description order" + "\",\n";
		String modality = "    \"Modality\": \"" + modalityBO.getAcronym() + "\"\n";
		String json =  "{\n" + accessionNumber;
		if(!equipmentBO.getCreateId()){
				json += studyInstanceUID;
		}
		json += aeTitle
				+ startDate
				+ startTime
				+ patientIdStr
				+ patientName
				+ patientSex
				+ studyDescription
				+ patientBirthDate
				+ modality
				+ "}";

		String topic = orchestrator.getBaseTopic() + "/LISTATRABAJO";


		MqttMetadataBo data = new MqttMetadataBo(topic, json,false,2);

		if(!equipmentBO.getCreateId()){
			appointmentOrderImageService.setImageId(appointmentId,	UID);
		}
		else{
			appointmentOrderImageService.setImageId(appointmentId,	"AccessionNumber" + institutionId +"-" + appointmentId);
		}

		return data;
	}
}
