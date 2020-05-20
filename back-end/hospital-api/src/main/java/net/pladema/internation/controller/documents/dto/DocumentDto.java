package net.pladema.internation.controller.documents.dto;

import javax.annotation.Nullable;

import net.pladema.internation.controller.ips.dto.VitalSignDto;

public interface DocumentDto {

    @Nullable
    VitalSignDto getVitalSigns();

}
