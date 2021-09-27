package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.application.odontogram.GetToothService;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import ar.lamansys.odontology.domain.consultation.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.consultation.ToothIndicesStorage;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.EOdontologyIndexBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ToothIndicesBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            CpoCeoIndicesBo diagnosticsIndices = computeIndices(patientId, diagnostics);
            diagnosticsIndices.setConsultationDate(LocalDateTime.now());
            diagnosticsIndices.setPermanentTeethPresent(consultationBo.getPermanentTeethPresent());
            diagnosticsIndices.setTemporaryTeethPresent(consultationBo.getTemporaryTeethPresent());
            consultationCpoCeoIndicesStorage.saveIndices(consultationId, diagnosticsIndices);
        }
        CpoCeoIndicesBo completeIndices = computeIndices(patientId, dentalActions);
        completeIndices.setConsultationDate(LocalDateTime.now());
        completeIndices.setPermanentTeethPresent(consultationBo.getPermanentTeethPresent());
        completeIndices.setTemporaryTeethPresent(consultationBo.getTemporaryTeethPresent());
        consultationCpoCeoIndicesStorage.saveIndices(consultationId, completeIndices);
    }

    private CpoCeoIndicesBo computeIndices(Integer patientId, List<ConsultationDentalActionBo> dentalActions) {
        LOG.debug("Input parameters -> patientId {}, dentalActions {}", patientId, dentalActions);
        Map<String, ToothIndicesBo> teethIndices = fetchPreviousTeethIndices(patientId);
        dentalActions.forEach(action -> applyDentalAction(teethIndices, action));

        CpoCeoIndicesBo result = new CpoCeoIndicesBo();
        teethIndices.values().forEach(toothIndices -> {
            EOdontologyIndexBo index = toothIndices.computeToothResultingIndex();
            if (toothIndices.isTemporary())
                updateTemporaryIndices(result, index);
            else
                updatePermanentIndices(result, index);
        });
        toothIndicesStorage.save(patientId, new ArrayList<>(teethIndices.values()));
        LOG.debug("Output -> {}", result);
        return result;
    }

    private void updatePermanentIndices(CpoCeoIndicesBo indices, EOdontologyIndexBo newIndex) {
        LOG.debug("Input parameters -> result {}, newIndex {}", indices, newIndex);
        switch (newIndex) {
            case CAVITIES:
                indices.setPermanentC(indices.getPermanentC() + 1);
                return;
            case LOST:
                indices.setPermanentP(indices.getPermanentP() + 1);
                return;
            case FIXED:
                indices.setPermanentO(indices.getPermanentO() + 1);
        }
    }

    private void updateTemporaryIndices(CpoCeoIndicesBo indices, EOdontologyIndexBo newIndex) {
        LOG.debug("Input parameters -> indices {}, newIndex {}", indices, newIndex);
        switch (newIndex) {
            case CAVITIES:
                indices.setTemporaryC(indices.getTemporaryC() + 1);
                return;
            case LOST:
                indices.setTemporaryE(indices.getTemporaryE() + 1);
                return;
            case FIXED:
                indices.setTemporaryO(indices.getTemporaryO() + 1);
        }
    }

    private void applyDentalAction(Map<String, ToothIndicesBo> teethIndices, ConsultationDentalActionBo action) {
        LOG.debug("Input parameters -> teethIndices {}, action {}", teethIndices, action);
        String toothId = action.getTooth().getSctid();
        ToothIndicesBo toothIndices = teethIndices.get(toothId);
        if (toothIndices == null) {
            ToothBo toothBo = getToothService.run(toothId);
            toothIndices = new ToothIndicesBo(toothId, toothBo.isTemporary());
        }
        toothIndices.apply(action);
        teethIndices.put(toothId, toothIndices);
    }

    private Map<String, ToothIndicesBo> fetchPreviousTeethIndices(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<ToothIndicesBo> teethIndicesList = toothIndicesStorage.getTeethIndices(patientId);
        return toToothIndicesMap(teethIndicesList);
    }

    private Map<String, ToothIndicesBo> toToothIndicesMap(List<ToothIndicesBo> teethIndicesList) {
        Map<String, ToothIndicesBo> teethIndicesMap = new HashMap<>();
        teethIndicesList.forEach(toothIndices -> teethIndicesMap.put(toothIndices.getToothId(), toothIndices));
        return teethIndicesMap;
    }
}
