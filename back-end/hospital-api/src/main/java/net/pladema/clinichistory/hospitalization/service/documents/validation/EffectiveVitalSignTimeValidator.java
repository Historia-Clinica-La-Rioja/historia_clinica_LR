package net.pladema.clinichistory.hospitalization.service.documents.validation;

import net.pladema.clinichistory.documents.service.IDocumentBo;
import net.pladema.clinichistory.documents.service.ips.domain.ClinicalObservationBo;
import net.pladema.clinichistory.documents.service.ips.domain.VitalSignBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

public class EffectiveVitalSignTimeValidator {

    private static final Logger LOG = LoggerFactory.getLogger(EffectiveVitalSignTimeValidator.class);

    public boolean isValid(IDocumentBo IDocumentBo, LocalDate entryDate) {
        LOG.debug("Input parameters -> document {}", IDocumentBo);

        if (IDocumentBo == null) {
            LOG.error("Illegal method signature, expected a parameter of type DocumentDto.");
            return false;
        }

        VitalSignBo vitalSigns = IDocumentBo.getVitalSigns();
        if (vitalSigns == null)
            return true;

        validEffectiveClinicalObservation(vitalSigns, entryDate);
        return true;
    }

    private void validEffectiveClinicalObservation(VitalSignBo vitalSigns, LocalDate entryDate){
        validEffectiveClinicalObservation("Saturación de oxigeno", vitalSigns.getBloodOxygenSaturation(), entryDate);

        validEffectiveClinicalObservation("Tensión diastólica", vitalSigns.getDiastolicBloodPressure(), entryDate);

        validEffectiveClinicalObservation("Frecuencia cardíaca", vitalSigns.getHeartRate(), entryDate);

        validEffectiveClinicalObservation("Frecuencia respiratoria", vitalSigns.getRespiratoryRate(), entryDate);

        validEffectiveClinicalObservation("Tensión sistólica", vitalSigns.getSystolicBloodPressure(), entryDate);

        validEffectiveClinicalObservation("Temperatura corporal", vitalSigns.getTemperature(), entryDate);
    }

    private void validEffectiveClinicalObservation(String property, ClinicalObservationBo effectiveClinicalObservationDto, LocalDate entryDate){
        if (effectiveClinicalObservationDto == null)
            return;
        if (effectiveClinicalObservationDto.getEffectiveTime() == null)
            throw new ConstraintViolationException(property + ": La fecha de medición debe es obligatoria", Collections.emptySet());
        LocalDateTime datetime = effectiveClinicalObservationDto.getEffectiveTime();
        if (datetime.isAfter(LocalDateTime.now()))
            throw new ConstraintViolationException(property + ": La fecha de medición debe ser anterior o igual a la fecha y hora actual", Collections.emptySet());
        if (datetime.isBefore(entryDate.atStartOfDay()))
            throw new ConstraintViolationException(property + ": La fecha de medición debe ser posterior a la fecha de internación", Collections.emptySet());
    }
}