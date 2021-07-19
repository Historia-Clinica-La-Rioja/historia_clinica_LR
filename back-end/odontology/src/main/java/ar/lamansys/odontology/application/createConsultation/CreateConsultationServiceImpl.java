package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationException;
import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationExceptionEnum;
import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import ar.lamansys.odontology.domain.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.OdontologyDocumentStorage;
import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProceduresStorage;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalDiagnosticBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalProcedureBo;
import ar.lamansys.odontology.domain.consultation.OdontologyDocumentBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CreateConsultationServiceImpl implements CreateConsultationService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateConsultationServiceImpl.class);

    private final DiagnosticStorage diagnosticStorage;

    private final ProceduresStorage proceduresStorage;

    private final OdontologyConsultationStorage odontologyConsultationStorage;

    private final OdontologyDocumentStorage odontologyDocumentStorage;

    public CreateConsultationServiceImpl(DiagnosticStorage diagnosticStorage,
                                         ProceduresStorage proceduresStorage,
                                         OdontologyConsultationStorage odontologyConsultationStorage,
                                         OdontologyDocumentStorage odontologyDocumentStorage) {
        this.diagnosticStorage = diagnosticStorage;
        this.proceduresStorage = proceduresStorage;
        this.odontologyConsultationStorage = odontologyConsultationStorage;
        this.odontologyDocumentStorage = odontologyDocumentStorage;
    }

    @Override
    @Transactional
    public void run(ConsultationBo consultationBo) {
        LOG.debug("Input parameter -> odontologyConsultationBo {}", consultationBo);
        if (consultationBo == null)
            throw new CreateConsultationException(CreateConsultationExceptionEnum.NULL_CONSULTATION,
                    "La información de la consulta es obligatoria");
        assertContextValid(consultationBo);

        Integer encounterId = odontologyConsultationStorage.save();

        odontologyDocumentStorage.save(new OdontologyDocumentBo(consultationBo, encounterId));

        LOG.debug("No output");
    }

    private void assertContextValid(ConsultationBo consultationBo) {
        if (consultationBo.getInstitutionId() == null)
            throw new CreateConsultationException(CreateConsultationExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
        if (consultationBo.getPatientId() == null)
            throw new CreateConsultationException(CreateConsultationExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
    }

}
