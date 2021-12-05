package ar.lamansys.nursing.infrastructure.input.rest.mapper;

import ar.lamansys.nursing.domain.NursingConsultationBo;
import ar.lamansys.nursing.infrastructure.input.rest.dto.NursingConsultationDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface NursingConsultationMapper {

    @Named("fromNursingConsultationDto")
    NursingConsultationBo fromNursingConsultationDto(NursingConsultationDto nursingConsultationDto);

}
