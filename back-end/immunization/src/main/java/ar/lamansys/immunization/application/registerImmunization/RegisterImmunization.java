package ar.lamansys.immunization.application.registerImmunization;

import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientException;
import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientExceptionEnum;
import ar.lamansys.immunization.domain.consultation.ImmunizePatientBo;
import ar.lamansys.immunization.domain.consultation.VaccineConsultationStorage;
import ar.lamansys.immunization.domain.immunization.ImmunizationDocumentStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegisterImmunization {

    private final Logger logger;

    private final VaccineConsultationStorage vaccineConsultationStorage;

    private final ImmunizationDocumentStorage immunizationDocumentStorage;

    public RegisterImmunization(VaccineConsultationStorage vaccineConsultationStorage,
                                ImmunizationDocumentStorage immunizationDocumentStorage) {
        this.vaccineConsultationStorage = vaccineConsultationStorage;
        this.immunizationDocumentStorage = immunizationDocumentStorage;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void run(ImmunizePatientBo immunizePatientBo) {
        logger.debug("Input parameters -> immunize patient {}", immunizePatientBo);
        assertContextValid(immunizePatientBo);
        //vaccineConsultationStorage.save(new VaccineConsultationBo());
        //immunizationDocumentStorage.save(new ImmunizationDocumentBo());
    }

    private void assertContextValid(ImmunizePatientBo immunizePatientBo) {
        if (immunizePatientBo.getInstitutionId() == null)
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
        if (immunizePatientBo.getPatientId() == null)
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
    }
}
