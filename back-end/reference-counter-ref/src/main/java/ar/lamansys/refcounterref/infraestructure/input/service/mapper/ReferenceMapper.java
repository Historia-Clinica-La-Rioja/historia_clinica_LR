package ar.lamansys.refcounterref.infraestructure.input.service.mapper;

import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ReferenceMapper {

        @Named("fromCompleteReferenceDto")
        CompleteReferenceBo fromCompleteReferenceDto(CompleteReferenceDto completeReferenceDto);

        @Named("fromCompleteReferenceDtoList")
        @IterableMapping(qualifiedByName = "fromCompleteReferenceDto")
        List<CompleteReferenceBo> fromCompleteReferenceDtoList(List<CompleteReferenceDto> completeReferenceDtoList);

}
