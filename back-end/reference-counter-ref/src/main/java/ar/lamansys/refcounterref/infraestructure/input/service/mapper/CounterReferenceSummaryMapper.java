package ar.lamansys.refcounterref.infraestructure.input.service.mapper;

import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(uses = {LocalDateMapper.class})
public interface CounterReferenceSummaryMapper {

    @Named("fromCounterReferenceSummaryBo")
	@Mapping(target = "closureType", source="closureType.description")
    CounterReferenceSummaryDto fromCounterReferenceSummaryBo(CounterReferenceSummaryBo counterReferenceSummaryBo);

}