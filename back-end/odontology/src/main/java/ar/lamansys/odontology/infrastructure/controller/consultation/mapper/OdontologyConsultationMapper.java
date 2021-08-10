package ar.lamansys.odontology.infrastructure.controller.consultation.mapper;

import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.infrastructure.controller.consultation.dto.OdontologyConsultationDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface OdontologyConsultationMapper {

    @Named("fromOdontologyConsultationDto")
    ConsultationBo fromOdontologyConsultationDto(OdontologyConsultationDto odontologyConsultationDto);

}
