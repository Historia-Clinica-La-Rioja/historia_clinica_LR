package ar.lamansys.odontology.infrastructure.controller.consultation.mapper;

import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.infrastructure.controller.consultation.dto.OdontologyConsultationDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface OdontologyConsultationMapper {

    @Named("fromOdontologyConsultationDto")
    ConsultationBo fromOdontologyConsultationDto(OdontologyConsultationDto odontologyConsultationDto);

}
