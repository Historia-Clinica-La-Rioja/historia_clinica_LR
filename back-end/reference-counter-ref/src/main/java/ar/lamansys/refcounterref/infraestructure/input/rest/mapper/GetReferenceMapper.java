package ar.lamansys.refcounterref.infraestructure.input.rest.mapper;

import ar.lamansys.refcounterref.domain.report.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceCompleteDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.ReferenceReportDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceCompleteDataDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceDataDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceSummaryDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface GetReferenceMapper {

    @Named("fromReferenceDataBo")
	ReferenceDataDto fromReferenceDataBo(ReferenceDataBo referenceBo);

    @Named("fromListReferenceDataBo")
    @IterableMapping(qualifiedByName = "fromReferenceDataBo")
    List<ReferenceDataDto> fromListReferenceDataBo(List<ReferenceDataBo> referenceDataBoList);

	@Named("toReferenceSummaryDtoList")
	List<ReferenceSummaryDto> toReferenceSummaryDtoList(List<ReferenceSummaryBo> referenceSummaryBoList);

	@Named("toReferenceReportDto")
	@Mapping(target = "procedure", source = "procedure.pt")
	ReferenceReportDto toReferenceReportDto(ReferenceReportBo referenceReportBo);

	@Named("toReferenceCompleteDataDto")
	ReferenceCompleteDataDto toReferenceCompleteDataDto(ReferenceCompleteDataBo referenceCompleteDataBo);

}
