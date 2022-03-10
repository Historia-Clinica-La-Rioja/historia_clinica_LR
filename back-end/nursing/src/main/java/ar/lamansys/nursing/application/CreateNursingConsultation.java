package ar.lamansys.nursing.application;

import ar.lamansys.nursing.application.exceptions.NursingConsultationException;
import ar.lamansys.nursing.application.exceptions.NursingConsultationExceptionEnum;
import ar.lamansys.nursing.application.port.NursingConsultationStorage;
import ar.lamansys.nursing.application.port.NursingDoctorStorage;
import ar.lamansys.nursing.domain.NursingConsultationBo;
import ar.lamansys.nursing.application.port.NursingAppointmentStorage;
import ar.lamansys.nursing.domain.document.NursingDocumentBo;
import ar.lamansys.nursing.application.port.NursingDocumentStorage;
import ar.lamansys.nursing.domain.doctor.DoctorInfoBo;
import ar.lamansys.nursing.domain.NursingConsultationInfoBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class CreateNursingConsultation {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNursingConsultation.class);

    private final DateTimeProvider dateTimeProvider;
    private final NursingDoctorStorage nursingDoctorStorage;
    private final NursingConsultationStorage nursingConsultationStorage;
    private final NursingDocumentStorage nursingDocumentStorage;
    private final NursingAppointmentStorage nursingAppointmentStorage;

    public CreateNursingConsultation(DateTimeProvider dateTimeProvider,
                                     NursingDoctorStorage nursingDoctorStorage,
                                     NursingConsultationStorage nursingConsultationStorage,
                                     NursingDocumentStorage nursingDocumentStorage,
                                     NursingAppointmentStorage nursingAppointmentStorage) {
        this.dateTimeProvider = dateTimeProvider;
        this.nursingDoctorStorage = nursingDoctorStorage;
        this.nursingConsultationStorage = nursingConsultationStorage;
        this.nursingDocumentStorage = nursingDocumentStorage;
        this.nursingAppointmentStorage = nursingAppointmentStorage;
    }

    @Transactional
    public void run(NursingConsultationBo nursingConsultationBo) {
        LOG.debug("Input parameters -> nursingConsultationBo {}", nursingConsultationBo);

        if (nursingConsultationBo == null)
            throw new NursingConsultationException(NursingConsultationExceptionEnum.NULL_NURSING_CONSULTATION, "La información de la consulta es obligatoria");

        var doctorInfoBo = nursingDoctorStorage.getDoctorInfo().orElseThrow(() ->
                new NursingConsultationException(NursingConsultationExceptionEnum.INVALID_DOCTOR, "El identificador del profesional es inválido"));

        assertContextValid(nursingConsultationBo, doctorInfoBo);

        LocalDate now = dateTimeProvider.nowDate();
        Integer medicalCoverageId = nursingAppointmentStorage.getPatientMedicalCoverageId(nursingConsultationBo.getPatientId(), doctorInfoBo.getId());

        var encounterId = nursingConsultationStorage.save(
                new NursingConsultationInfoBo(null,
                        nursingConsultationBo,
                        medicalCoverageId,
                        doctorInfoBo.getId(),
                        now,
                        false));

        nursingDocumentStorage.save(new NursingDocumentBo(null, nursingConsultationBo, encounterId, doctorInfoBo.getId(), now));
        nursingAppointmentStorage.run(nursingConsultationBo.getPatientId(), doctorInfoBo.getId(), now);

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
