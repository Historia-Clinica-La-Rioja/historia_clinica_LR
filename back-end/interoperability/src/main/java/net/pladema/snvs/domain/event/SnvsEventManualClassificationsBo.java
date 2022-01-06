package net.pladema.snvs.domain.event;

import lombok.Getter;
import net.pladema.snowstorm.services.domain.ManualClassificationBo;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoEnumException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;

import java.util.List;

@Getter
public class SnvsEventManualClassificationsBo {

    private final SnvsEventBo snvsEventBo;

    private final List<ManualClassificationBo> manualClassifications;

    public SnvsEventManualClassificationsBo(SnvsEventBo snvsEventBo, List<ManualClassificationBo> manualClassifications) throws SnvsEventInfoBoException {
        this.snvsEventBo = snvsEventBo;
        if (manualClassifications == null)
            throw new SnvsEventInfoBoException(SnvsEventInfoBoEnumException.NULL_MANUAL_CLASSIFICATIONS,"La clasificaciones manual son es obligatorias");
        if (manualClassifications.isEmpty())
            throw new SnvsEventInfoBoException(SnvsEventInfoBoEnumException.EMPTY_MANUAL_CLASSIFICATIONS,"Se espera al menos una clasificaciones manual");
        this.manualClassifications = manualClassifications;
    }

    public void joinManualClassifications(List<ManualClassificationBo> manualClassifications) {
        this.manualClassifications.addAll(manualClassifications);
    }
}