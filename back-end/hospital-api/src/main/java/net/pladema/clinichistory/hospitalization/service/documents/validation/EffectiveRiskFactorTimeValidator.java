package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.Collections;

public class EffectiveRiskFactorTimeValidator {

    private static final Logger LOG = LoggerFactory.getLogger(EffectiveRiskFactorTimeValidator.class);

    public boolean isValid(IDocumentBo IDocumentBo, LocalDateTime entryDate) {
        LOG.debug("Input parameters -> document {}", IDocumentBo);

        if (IDocumentBo == null) {
            LOG.error("Illegal method signature, expected a parameter of type DocumentDto.");
            return false;
        }

        RiskFactorBo riskFactors = IDocumentBo.getRiskFactors();
        if (riskFactors == null)
            return true;

        validEffectiveClinicalObservation(riskFactors, entryDate);
        return true;
    }

    private void validEffectiveClinicalObservation(RiskFactorBo riskFactors, LocalDateTime entryDate){
        validEffectiveClinicalObservation("Saturación de oxigeno", riskFactors.getBloodOxygenSaturation(), entryDate);

        validEffectiveClinicalObservation("Tensión diastólica", riskFactors.getDiastolicBloodPressure(), entryDate);

        validEffectiveClinicalObservation("Frecuencia cardíaca", riskFactors.getHeartRate(), entryDate);

        validEffectiveClinicalObservation("Frecuencia respiratoria", riskFactors.getRespiratoryRate(), entryDate);

        validEffectiveClinicalObservation("Tensión sistólica", riskFactors.getSystolicBloodPressure(), entryDate);

        validEffectiveClinicalObservation("Temperatura corporal", riskFactors.getTemperature(), entryDate);
    }

    private void validEffectiveClinicalObservation(String property, ClinicalObservationBo effectiveClinicalObservationDto, LocalDateTime entryDate){
        if (effectiveClinicalObservationDto == null)
            return;
        if (effectiveClinicalObservationDto.getEffectiveTime() == null)
            throw new ConstraintViolationException(property + ": La fecha de medición debe es obligatoria", Collections.emptySet());
        LocalDateTime datetime = effectiveClinicalObservationDto.getEffectiveTime();
        if (datetime.isAfter(LocalDateTime.now()))
            throw new ConstraintViolationException(property + ": La fecha de medición debe ser anterior o igual a la fecha y hora actual", Collections.emptySet());
        if (datetime.isBefore(entryDate))
            throw new ConstraintViolationException(property + ": La fecha de medición debe ser posterior a la fecha de internación", Collections.emptySet());
    }
}