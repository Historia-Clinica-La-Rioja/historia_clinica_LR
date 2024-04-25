package ar.lamansys.sgh.clinichistory.application.document.draft;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SetNullIdsDocumentElements {

    public void run(IDocumentBo documentBo) {
        log.debug("Input parameter -> documentBo {}", documentBo);

        setMainDiagnosis(documentBo);

        setDiagnosis(documentBo);

        setSurgeryProcedures(documentBo);

        setAnthropometricData(documentBo);

        setRiskFactors(documentBo);

        setMedications(documentBo);

        setPreMedications(documentBo);

        setHistories(documentBo);

        setProcedureDescription(documentBo);

        setAnestheticSubstances(documentBo.getAnestheticPlans());

        setAnalgesicTechniques(documentBo);

        setAnestheticTechniques(documentBo);

        setAnestheticSubstances(documentBo.getFluidAdministrations());

        setAnestheticSubstances(documentBo.getAnestheticAgents());

        setAnestheticSubstances(documentBo.getNonAnestheticDrugs());

        setAnestheticSubstances(documentBo.getAntibioticProphylaxis());

        setMeasuringPoints(documentBo);

        setPostAnesthesiaStatus(documentBo);

        log.debug("Output -> all id values have been setted with null");
    }

    private void setPostAnesthesiaStatus(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getPostAnesthesiaStatus())
                .ifPresent(c -> c.setId(null));
    }

    private void setMeasuringPoints(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getMeasuringPoints())
                .ifPresent(l -> l.forEach(e -> e.setId(null)));
    }

    private void setAnestheticTechniques(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getAnestheticTechniques())
                .ifPresent(l -> l.forEach(e -> e.setId(null)));
    }

    private void setAnalgesicTechniques(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getAnalgesicTechniques())
                .ifPresent(l -> l.forEach(e -> e.setId(null)));
    }

    private void setAnestheticSubstances(List<AnestheticSubstanceBo> substanceBos) {
        Optional.ofNullable(substanceBos)
                .ifPresent(l -> l.forEach(e -> e.setId(null)));
    }

    private void setProcedureDescription(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getProcedureDescription())
                .ifPresent(c -> c.setId(null));
    }

    private void setHistories(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getHistories())
                .ifPresent(l -> l.forEach(e -> e.setId(null)));
    }

    private void setPreMedications(IDocumentBo documentBo) {
        setAnestheticSubstances(documentBo.getPreMedications());
    }

    private void setMedications(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getMedications())
                .ifPresent(l -> l.forEach(e -> e.setId(null)));
    }

    private void setRiskFactors(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getRiskFactors())
                .ifPresent(riskFactorBo -> {
                    Optional.ofNullable(riskFactorBo.getDiastolicBloodPressure())
                            .ifPresent(c -> c.setId(null));
                    Optional.ofNullable(riskFactorBo.getSystolicBloodPressure())
                            .ifPresent(c -> c.setId(null));
                    Optional.ofNullable(riskFactorBo.getHematocrit())
                            .ifPresent(c -> c.setId(null));
                });
    }

    private void setAnthropometricData(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getAnthropometricData())
                .ifPresent(anthropometricDataBo -> {
                    Optional.ofNullable(anthropometricDataBo.getBloodType())
                            .ifPresent(c -> c.setId(null));
                    Optional.ofNullable(anthropometricDataBo.getHeight())
                            .ifPresent(c -> c.setId(null));
                    Optional.ofNullable(anthropometricDataBo.getWeight())
                            .ifPresent(c -> c.setId(null));
                });
    }

    private void setSurgeryProcedures(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getSurgeryProcedures())
                .ifPresent(l -> l.forEach(e -> e.setId(null)));
    }

    private void setDiagnosis(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getDiagnosis())
                .ifPresent(l -> l.forEach(e -> e.setId(null)));
    }

    private void setMainDiagnosis(IDocumentBo documentBo) {
        Optional.ofNullable(documentBo.getMainDiagnosis())
                .ifPresent(c -> c.setId(null));
    }
}
