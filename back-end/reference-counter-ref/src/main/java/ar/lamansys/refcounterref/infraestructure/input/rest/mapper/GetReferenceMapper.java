package ar.lamansys.refcounterref.infraestructure.input.rest.mapper;

import ar.lamansys.refcounterref.domain.reference.ReferenceGetBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceGetDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceSummaryDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface GetReferenceMapper {

    @Named("fromReferenceGetBo")
    ReferenceGetDto fromReferenceGetBo(ReferenceGetBo referenceBo);

    @Named("fromListReferenceGetBo")
    @IterableMapping(qualifiedByName = "fromReferenceGetBo")
    List<ReferenceGetDto> fromListReferenceGetBo(List<ReferenceGetBo> referenceGetBoList);

	@Named("toReferenceSummaryDtoList")
	List<ReferenceSummaryDto> toReferenceSummaryDtoList(List<ReferenceSummaryBo> referenceSummaryBoList);
}
