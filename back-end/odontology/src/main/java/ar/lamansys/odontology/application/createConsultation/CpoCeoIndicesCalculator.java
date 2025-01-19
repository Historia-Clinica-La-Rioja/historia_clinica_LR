package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.application.odontogram.GetToothService;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import ar.lamansys.odontology.application.odontogram.ports.OdontologyConsultationStorage;
import ar.lamansys.odontology.application.odontogram.ports.ToothIndicesStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CpoCeoIndicesCalculator {

    private static final Logger LOG = LoggerFactory.getLogger(CpoCeoIndicesCalculator.class);

    private final GetToothService getToothService;

    private final ConsultationCpoCeoIndicesStorage consultationCpoCeoIndicesStorage;

    private final OdontologyConsultationStorage odontologyConsultationStorage;

    private final ToothIndicesStorage toothIndicesStorage;

    public CpoCeoIndicesCalculator(GetToothService getToothService,
                                   ConsultationCpoCeoIndicesStorage consultationCpoCeoIndicesStorage,
                                   OdontologyConsultationStorage odontologyConsultationStorage,
                                   ToothIndicesStorage toothIndicesStorage) {
        this.getToothService = getToothService;
        this.consultationCpoCeoIndicesStorage = consultationCpoCeoIndicesStorage;
        this.odontologyConsultationStorage = odontologyConsultationStorage;
        this.toothIndicesStorage = toothIndicesStorage;
    }

    public void run(ConsultationBo consultationBo) {
        LOG.debug("Input parameters -> consultationBo {}", consultationBo);

        Integer patientId = consultationBo.getPatientId();
        Integer consultationId = consultationBo.getConsultationId();
        List<ConsultationDentalActionBo>dentalActions = consultationBo.getDentalActions();

        if (!odontologyConsultationStorage.hasPreviousConsultations(patientId)){
            List<ConsultationDentalActionBo> diagnostics = (dentalActions != null) ?
                    dentalActions.stream().filter(ConsultationDentalActionBo::isDiagnostic).collect(Collectors.toList()) : null;
			CpoCeoIndicesBo diagnosticsIndices = toothIndicesStorage.computeIndices(patientId, dentalActions);
            diagnosticsIndices.setConsultationDate(LocalDateTime.now());
            diagnosticsIndices.setPermanentTeethPresent(consultationBo.getPermanentTeethPresent());
            diagnosticsIndices.setTemporaryTeethPresent(consultationBo.getTemporaryTeethPresent());
            consultationCpoCeoIndicesStorage.saveIndices(consultationId, diagnosticsIndices);
        }
		CpoCeoIndicesBo completeIndices = toothIndicesStorage.computeIndices(patientId, dentalActions);
        completeIndices.setConsultationDate(LocalDateTime.now());
        completeIndices.setPermanentTeethPresent(consultationBo.getPermanentTeethPresent());
        completeIndices.setTemporaryTeethPresent(consultationBo.getTemporaryTeethPresent());
        consultationCpoCeoIndicesStorage.saveIndices(consultationId, completeIndices);
    }

}
