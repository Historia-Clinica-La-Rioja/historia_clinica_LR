package ar.lamansys.nursing.application;

import static ar.lamansys.nursing.domain.NursingConsultationInfoBo.newNursingConsultationInfoBo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.lamansys.nursing.domain.CreateNursingConsultationServiceRequestBo;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.SharedCreateConsultationServiceRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.nursing.application.exceptions.NursingConsultationException;
import ar.lamansys.nursing.application.exceptions.NursingConsultationExceptionEnum;
import ar.lamansys.nursing.application.port.NursingAppointmentStorage;
import ar.lamansys.nursing.application.port.NursingConsultationStorage;
import ar.lamansys.nursing.application.port.NursingDoctorStorage;
import ar.lamansys.nursing.application.port.NursingDocumentStorage;
import ar.lamansys.nursing.domain.NursingConsultationBo;
import ar.lamansys.nursing.domain.doctor.DoctorInfoBo;
import ar.lamansys.nursing.domain.document.NursingDocumentBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.DocumentAppointmentDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;

@Service
public class CreateNursingConsultation {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNursingConsultation.class);

	private final SharedAppointmentPort sharedAppointmentPort;
    private final DateTimeProvider dateTimeProvider;
    private final NursingDoctorStorage nursingDoctorStorage;
    private final NursingConsultationStorage nursingConsultationStorage;
    private final NursingDocumentStorage nursingDocumentStorage;
    private final NursingAppointmentStorage nursingAppointmentStorage;
	private final SharedCreateConsultationServiceRequest sharedCreateConsultationServiceRequest;

	private final SharedPatientPort sharedPatientPort;

    public CreateNursingConsultation(SharedAppointmentPort sharedAppointmentPort, DateTimeProvider dateTimeProvider,
    	NursingDoctorStorage nursingDoctorStorage, NursingConsultationStorage nursingConsultationStorage,
    	NursingDocumentStorage nursingDocumentStorage, NursingAppointmentStorage nursingAppointmentStorage,
		SharedCreateConsultationServiceRequest sharedCreateConsultationServiceRequest, SharedPatientPort sharedPatientPort
	)
	{
		this.sharedAppointmentPort = sharedAppointmentPort;
		this.dateTimeProvider = dateTimeProvider;
        this.nursingDoctorStorage = nursingDoctorStorage;
        this.nursingConsultationStorage = nursingConsultationStorage;
        this.nursingDocumentStorage = nursingDocumentStorage;
        this.nursingAppointmentStorage = nursingAppointmentStorage;
        this.sharedCreateConsultationServiceRequest = sharedCreateConsultationServiceRequest;
        this.sharedPatientPort = sharedPatientPort;
    }

    @Transactional
    public void run(
    	NursingConsultationBo nursingConsultationBo,
    	List<CreateNursingConsultationServiceRequestBo> serviceRequestsToCreate)
	{
        LOG.debug("Input parameters -> nursingConsultationBo {}", nursingConsultationBo);

        if (nursingConsultationBo == null)
            throw new NursingConsultationException(NursingConsultationExceptionEnum.NULL_NURSING_CONSULTATION, "La información de la consulta es obligatoria");

        var doctorInfoBo = nursingDoctorStorage.getDoctorInfo().orElseThrow(() ->
                new NursingConsultationException(NursingConsultationExceptionEnum.INVALID_DOCTOR, "El identificador del profesional es inválido"));

        assertContextValid(nursingConsultationBo, doctorInfoBo);

        LocalDate now = dateTimeProvider.nowDate();

		setPatientMedicalCoverageIfEmpty(nursingConsultationBo, doctorInfoBo);

		var encounterId = nursingConsultationStorage.save(
                newNursingConsultationInfoBo(
                        nursingConsultationBo,
                        doctorInfoBo.getId(),
                        now,
                        false
				));

        Long documentId = nursingDocumentStorage.save(new NursingDocumentBo(null, nursingConsultationBo, encounterId, doctorInfoBo.getId(), now));
        Integer appointmentId = nursingAppointmentStorage.run(nursingConsultationBo.getPatientId(), doctorInfoBo.getId(), now);
		if(appointmentId != null)
			this.sharedAppointmentPort.saveDocumentAppointment(new DocumentAppointmentDto(documentId, appointmentId));

		/**
		 * Create a service request for each procedure
		 */
		BasicPatientDto patientDto = sharedPatientPort.getBasicDataFromPatient(nursingConsultationBo.getPatientId());
		createServiceRequest(
				doctorInfoBo.getId(),
				serviceRequestsToCreate,
				nursingConsultationBo.getPatientMedicalCoverageId(),
				patientDto,
				nursingConsultationBo.getInstitutionId(),
				encounterId);
    }

	private void createServiceRequest(
		Integer doctorId,
		List<CreateNursingConsultationServiceRequestBo> procedures,
		Integer medicalCoverageId,
		BasicPatientDto patientDto,
		Integer institutionId,
		Integer newConsultationId
	)
	{
		for (int i = 0; i < procedures.size(); i++) {
			var procedure = procedures.get(i);
			if (procedure != null) {

				String categoryId = procedure.getCategoryId();
				String healthConditionSctid = procedure.getHealthConditionSctid();
				String healthConditionPt = procedure.getHealthConditionPt();
				SnomedDto snomed = new SnomedDto(procedure.getSnomedSctid(), procedure.getSnomedPt());
				Boolean createWithStatusFinal = procedure.getCreationStatusIsFinal();
				Optional<SharedAddObservationsCommandVo> addObservationsCommand = procedure.getObservations();

				Integer patientId = patientDto.getId();
				Short patientGenderId = patientDto.getPerson().getGender().getId();
				Short patientAge = patientDto.getPerson().getAge();

				sharedCreateConsultationServiceRequest.createNursingServiceRequest(doctorId, categoryId, institutionId,
						healthConditionSctid, healthConditionPt, medicalCoverageId, newConsultationId, snomed.getSctid(), snomed.getPt(),
						createWithStatusFinal, addObservationsCommand, patientId, patientGenderId, patientAge);
			}
		}
	}

	private void setPatientMedicalCoverageIfEmpty(NursingConsultationBo nursingConsultationBo, DoctorInfoBo doctorInfoBo) {
		if (nursingConsultationBo.getPatientMedicalCoverageId() == null) {
			nursingConsultationBo.setPatientMedicalCoverageId(
					nursingAppointmentStorage.getPatientMedicalCoverageId(nursingConsultationBo.getPatientId(), doctorInfoBo.getId())
			);
		}
	}

	private void assertContextValid(NursingConsultationBo consultationBo, DoctorInfoBo doctorInfoBo) {
        if (consultationBo.getInstitutionId() == null)
            throw new NursingConsultationException(NursingConsultationExceptionEnum.NULL_INSTITUTION_ID.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
        if (consultationBo.getPatientId() == null)
            throw new NursingConsultationException(NursingConsultationExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
        if (consultationBo.getClinicalSpecialtyId() == null)
            throw new NursingConsultationException(NursingConsultationExceptionEnum.NULL_CLINICAL_SPECIALTY_ID, "El id de especialidad es obligatorio");
        if (consultationBo.getProblem() == null)
            throw new NursingConsultationException(NursingConsultationExceptionEnum.NULL_PROBLEM, "El problema es obligatorio");
        if (!doctorInfoBo.hasSpecialty(consultationBo.getClinicalSpecialtyId()))
            throw new NursingConsultationException(NursingConsultationExceptionEnum.INVALID_CLINICAL_SPECIALTY_ID, "El doctor no posee la especialidad indicada");
    }

}
