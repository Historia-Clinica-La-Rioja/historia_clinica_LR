package net.pladema.imagenetwork.derivedstudies.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import lombok.AllArgsConstructor;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.RequiredArgsConstructor;
import net.pladema.establishment.service.EquipmentService;
import net.pladema.establishment.service.OrchestratorService;
import net.pladema.establishment.service.PacServerService;
import net.pladema.establishment.service.domain.EquipmentBO;
import net.pladema.establishment.service.domain.OrchestratorBO;
import net.pladema.establishment.service.domain.PacServerBO;
import net.pladema.imagenetwork.application.savepacsherestudyishosted.SavePacWhereStudyIsHosted;
import net.pladema.imagenetwork.derivedstudies.repository.MoveStudiesRepository;
import net.pladema.imagenetwork.derivedstudies.repository.entity.MoveStudies;
import net.pladema.imagenetwork.derivedstudies.service.MoveStudiesService;
import net.pladema.imagenetwork.derivedstudies.service.domain.MoveStudiesBO;
import net.pladema.imagenetwork.domain.StudyPacBo;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.scheduledjobs.jobs.MoveStudiesJob;
import net.pladema.patient.controller.service.PatientExternalService;

@Service
@RequiredArgsConstructor
public class MoveStudiesServiceImpl implements MoveStudiesService {

	private static final String FINISHED = "FINISHED";

	private static final String PENDING = "PENDING";

	private static final String RESULT_OK = "200";

	private static final Integer ZERO_ATTEMPTS = 0;

	private final MoveStudiesRepository moveStudiesRepository;
	private final AppointmentService appointmentService;
	private final EquipmentService equipmentService;
	private final AppointmentOrderImageService appointmentOrderImageService;
	private final EquipmentDiaryService equipmentDiaryService;

	private final OrchestratorService orchestratorService;

	private final PacServerService pacServerService;

	private final MqttClientService mqttClientService;
	private final SavePacWhereStudyIsHosted savePacWhereStudyIsHosted;

	private final PatientExternalService patientExternalService;
	@Override
	public Integer save(MoveStudiesBO moveStudyBO) {
		MoveStudies moveStudy = new MoveStudies( moveStudyBO.getAppointmentId(),
				moveStudyBO.getOrchestratorId(),
				moveStudyBO.getImageId(),
				moveStudyBO.getPacServerId(),
				moveStudyBO.getPriority(),
				new Date(),
				moveStudyBO.getPriorityMax(),
				moveStudyBO.getAttempsNumber(),
				moveStudyBO.getStatus(),
				moveStudyBO.getInstitutionId());
		return moveStudiesRepository.save(moveStudy).getId();
	}

	@Override
	public Integer create(Integer appointmentId, Integer institutionId) {
		List<PacServerBO> pacServers = pacServerService.getAllPacServer();
		String imageId = appointmentOrderImageService.getImageId(appointmentId).orElse("none");
		MoveStudiesBO moveStudyBO = new MoveStudiesBO();
		if ( !pacServers.isEmpty() && !imageId.equals("none")){
			moveStudyBO.setAppointmentId(appointmentId);
			moveStudyBO.setStatus(MoveStudiesJob.PENDING);
			moveStudyBO.setPriorityMax(0);
			moveStudyBO.setAttempsNumber(0);
			Integer pacServerId = pacServers.get(0).getId();
			moveStudyBO.setPacServerId(pacServerId);

			AppointmentBo appointment = appointmentService.getEquipmentAppointment(appointmentId).orElse(null);
			Integer diaryId = appointment.getDiaryId();
			CompleteEquipmentDiaryBo equipmentDiary = equipmentDiaryService.getEquipmentDiary(diaryId).orElse(null);
			Integer equipmentId = equipmentDiary.getEquipmentId();
			EquipmentBO equipmentBO = equipmentService.getEquipment(equipmentId);
			Integer orchestratorId = equipmentBO.getOrchestratorId();
			moveStudyBO.setOrchestratorId(orchestratorId);
			moveStudyBO.setImageId(imageId);
			moveStudyBO.setPriority(5);
			moveStudyBO.setInstitutionId(institutionId);
			return save(moveStudyBO);
		}
		return -1;

	}

	@Override
	public List<MoveStudiesBO> getMoveStudies() {
		List<MoveStudies> listMoveStudies = moveStudiesRepository.getListMoveStudies("FINISHED");
		List<MoveStudiesBO> result = listMoveStudies.stream().map(this::createMoveStudyBOInstance).collect(Collectors.toList());
		return result;
	}

	@Override
	public void updateSize(Integer idMove, Integer size, String imageId) {
		moveStudiesRepository.updateSize(idMove, size);
		MoveStudies moveStudy = moveStudiesRepository.findById(idMove).orElse(null);
		if (moveStudy!= null &&
				!moveStudy.getImageId().equals(imageId) &&
				!"none".equals(imageId) ){
			appointmentOrderImageService.setImageId(moveStudy.getAppointmentId(), imageId);
			moveStudiesRepository.updateImageId(idMove,imageId);
		}
	}

	@Transactional
	public void updateFailedCurrentDate(Integer orchestratorId){
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
		LocalDate localDate = now.toLocalDate();
		moveStudiesRepository.updateFailedCurrentDate(localDate,orchestratorId,FINISHED,RESULT_OK,PENDING,ZERO_ATTEMPTS);
	}

	@Override
	public Optional<Integer> findInstitutionId(Integer idMove) {
		return moveStudiesRepository.findInstitutionId(idMove);
	}

	@Override
	public void updateStatus(Integer idMove, String status) {
		moveStudiesRepository.updateStatus(idMove, status);
	}

	@Override
	public void updateStatusAndResult(Integer idMove, String status, String result) {
		moveStudiesRepository.updateStatusandResult(idMove, status, result);
		if(MoveStudiesJob.MOVING.equals(status)){
			moveStudiesRepository.updateBeginOfMove(idMove, new Date());
		}
		if ("200".equals(result)) {
			moveStudiesRepository.updateEndOfMove(idMove, new Date());
			Optional<MoveStudies> moveStudy = moveStudiesRepository.findById(idMove);
			if (moveStudy.isPresent()) {
				MoveStudies ms = moveStudy.get();
				savePacWhereStudyIsHosted.run(new StudyPacBo(ms.getImageId(), ms.getPacServerId()));
				Optional<String> imageId = appointmentOrderImageService.getImageId(ms.getAppointmentId());
				if (imageId.isPresent()){
					if (!imageId.get().equals(ms.getImageId())){
						appointmentOrderImageService.setImageId(ms.getAppointmentId(), ms.getImageId());
					}
				}
			}
		} 
		else if (FINISHED.equals(status) && !result.contains("Error") &&  !result.contains("401")){

			Integer appointmentId = moveStudiesRepository.findById(idMove)
						.map(MoveStudies::getAppointmentId)
						.orElse(null);
			if (appointmentId!= null){
				AppointmentBo appointment = appointmentService.getEquipmentAppointment(appointmentId).orElse(null);
				if (appointment != null){
					Integer patientId = appointment.getPatientId();
					BasicPatientDto basicDataPatient = patientExternalService.getBasicDataFromPatient(patientId);
					if(basicDataPatient!= null){
						String identificationNumber = basicDataPatient.getIdentificationNumber();
						String identification = identificationNumber == null ?basicDataPatient.getId()+"": identificationNumber;
						findStudyByPatientDNI(idMove, appointmentId, identification);
					}
				}
			}
		}

	}

	public void findStudyByPatientDNI(Integer idMove,Integer appointmentId, String patientDni){
		Optional<MoveStudies> optionalMoveStudies = moveStudiesRepository.findById(idMove);

		if(optionalMoveStudies.isPresent()) {
			MoveStudies moveStudy = optionalMoveStudies.get();
			moveStudy.getOrchestratorId();
			OrchestratorBO orchestrator = orchestratorService.getOrchestrator(moveStudy.getOrchestratorId());
			if (orchestrator.getFindStudies()) {
				String topic = orchestrator.getBaseTopic() + "/OBTENERESTUDIO";
				String patientIDJson = "   \"PatientID\": \"" + patientDni + "\", \n";
				String appointmentIdJson = "   \"appointmentId\": \"" + appointmentId + "\", \n";
				String idMoveJson = "    \"idMove\": \"" + idMove + "\"\n";
				String json = "{\n" + patientIDJson + appointmentIdJson + idMoveJson + "}";

				MqttMetadataBo data = new MqttMetadataBo(topic, json, false, 2);
				mqttClientService.publish(data);
			}
		}
	}

	@Override
	public void updateAttemps(Integer idMove, Integer attemps) {
		moveStudiesRepository.updateAttemps(idMove,attemps);
	}

	@Override
	public void getSizeFromOrchestrator(Integer idMove) {

		Optional<MoveStudies> optionalMoveStudies = moveStudiesRepository.findById(idMove);

		if(optionalMoveStudies.isPresent()){
			MoveStudies moveStudy = optionalMoveStudies.get();
			moveStudy.getOrchestratorId();
			OrchestratorBO orchestrator = orchestratorService.getOrchestrator(moveStudy.getOrchestratorId());
			String topic = orchestrator.getBaseTopic() + "/SIZE";
			String studyInstanceUID =   "   \"StudyInstanceUID\": \"" + moveStudy.getImageId()+ "\", \n";
			String idMoveJson =  "    \"IdMove\": \"" +idMove + "\"\n";
			String json =  "{\n"
					+ studyInstanceUID
					+ idMoveJson
					+ "}";



			MqttMetadataBo data = new MqttMetadataBo(topic, json,false,2);
			mqttClientService.publish(data);
		}

	}

	@Override
	public Integer getInstitutionByAppointmetId(Integer appointmentId) {
		return moveStudiesRepository.getInstitutionIdByAppointmetId(appointmentId)
				.orElse(null);
	}

	@Override
	public Optional<Integer> getSizeImageByAppointmentId(Integer appointmentId) {
		return moveStudiesRepository.getSizeImageByAppointmentId(appointmentId);
	}

	private MoveStudiesBO createMoveStudyBOInstance(MoveStudies moveStudy){
		MoveStudiesBO moveStudyBO = new MoveStudiesBO();
		moveStudyBO.setId(moveStudy.getId());
		moveStudyBO.setAppointmentId(moveStudy.getAppointmentId());
		moveStudyBO.setAttempsNumber(moveStudy.getAttempsNumber());
		moveStudyBO.setStatus(moveStudy.getStatus());
		moveStudyBO.setImageId(moveStudy.getImageId());
		moveStudyBO.setPacServerId(moveStudy.getPacServerId());
		moveStudyBO.setOrchestratorId(moveStudy.getOrchestratorId());
		moveStudyBO.setPriority(moveStudy.getPriority());
		moveStudyBO.setDerivedDate(moveStudy.getMoveDate());
		moveStudyBO.setPriorityMax(moveStudy.getPriorityMax());
		moveStudyBO.setResult(moveStudy.getResult());
		moveStudyBO.setSizeImage(moveStudy.getSizeImage());
		moveStudyBO.setBeginOfMove(moveStudy.getBeginOfMove());
		moveStudyBO.setEndOfMove(moveStudy.getEndOfMove());
		return moveStudyBO;
	}
}
