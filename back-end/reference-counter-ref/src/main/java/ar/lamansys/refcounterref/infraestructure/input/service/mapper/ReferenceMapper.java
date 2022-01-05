package ar.lamansys.refcounterref.infraestructure.input.service.mapper;

import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ReferenceMapper {

        @Named("fromReferenceDto")
        ReferenceBo fromReferenceDto(ReferenceDto referenceDto);

        @Named("fromReferenceDtoList")
        @IterableMapping(qualifiedByName = "fromReferenceDto")
        List<ReferenceBo> fromReferenceDtoList(List<ReferenceDto> referenceDtoList);

}
