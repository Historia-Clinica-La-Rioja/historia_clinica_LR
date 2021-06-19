package net.pladema.clinichistory.hospitalization.controller.documents;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.VitalSignDto;

import javax.annotation.Nullable;

public interface DocumentDto {

    @Nullable
    VitalSignDto getVitalSigns();

}
