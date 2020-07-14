package net.pladema.clinichistory.hospitalization.controller.documents;

import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.VitalSignDto;

import javax.annotation.Nullable;

public interface DocumentDto {

    @Nullable
    VitalSignDto getVitalSigns();

}
