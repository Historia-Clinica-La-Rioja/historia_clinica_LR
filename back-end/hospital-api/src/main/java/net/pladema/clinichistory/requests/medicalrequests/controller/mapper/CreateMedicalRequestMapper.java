package net.pladema.clinichistory.requests.medicalrequests.controller.mapper;

import net.pladema.clinichistory.requests.medicalrequests.controller.dto.NewMedicalRequestDto;
import net.pladema.clinichistory.requests.medicalrequests.service.domain.MedicalRequestBo;
import org.mapstruct.Mapper;

@Mapper
public interface CreateMedicalRequestMapper {

    MedicalRequestBo toMedicalRequestBo(NewMedicalRequestDto newMedicalRequestDto);
}
