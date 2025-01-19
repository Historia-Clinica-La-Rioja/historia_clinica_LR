package ar.lamansys.refcounterref.infraestructure.input.service.mapper;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceRequestDto;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface ReferenceMapper {

        @Named("fromCompleteReferenceDto")
        CompleteReferenceBo fromCompleteReferenceDto(CompleteReferenceDto completeReferenceDto);

        @Named("fromCompleteReferenceDtoList")
        @IterableMapping(qualifiedByName = "fromCompleteReferenceDto")
        List<CompleteReferenceBo> fromCompleteReferenceDtoList(List<CompleteReferenceDto> completeReferenceDtoList);

		@Named("fromReferenceRequestBo")
		@Mapping(target = "closureTypeId", source="closureType.id")
		@Mapping(target = "closureTypeDescription", source="closureType.description")
		ReferenceRequestDto fromReferenceRequestBo(ReferenceRequestBo referenceRequestBo);

		@Named("fromReferenceDto")
		ReferenceBo fromReferenceDto(ReferenceDto referenceDto);

}
