package ar.lamansys.odontology.infrastructure.controller.consultation.mapper;

import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import ar.lamansys.odontology.infrastructure.controller.consultation.dto.OdontologyConsultationIndicesDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface CpoCeoIndicesMapper {

    @Named("fromCpoCeoIndicesBo")
    @Mapping(target = "date", source = "consultationDate")
    OdontologyConsultationIndicesDto fromCpoCeoIndicesBo(CpoCeoIndicesBo cpoCeoIndicesBo);

    @Named("fromCpoCeoIndicesBoList")
    @IterableMapping(qualifiedByName = "fromCpoCeoIndicesBo")
    List<OdontologyConsultationIndicesDto> fromCpoCeoIndicesBoList(List<CpoCeoIndicesBo> cpoCeoIndicesBoList);

}
