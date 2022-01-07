package ar.lamansys.refcounterref.infraestructure.input.rest.mapper;


import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceBo;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.counterreference.CounterReferenceDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface CounterReferenceMapper {

    @Named("fromCounterReferenceDto")
    CounterReferenceBo fromCounterReferenceDto(CounterReferenceDto counterReferenceDto);

}