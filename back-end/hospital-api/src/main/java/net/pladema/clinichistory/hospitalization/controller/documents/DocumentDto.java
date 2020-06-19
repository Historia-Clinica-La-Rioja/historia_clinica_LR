package net.pladema.clinichistory.hospitalization.controller.documents;

import net.pladema.clinichistory.ips.controller.dto.VitalSignDto;

import javax.annotation.Nullable;

public interface DocumentDto {

    @Nullable
    VitalSignDto getVitalSigns();

}
