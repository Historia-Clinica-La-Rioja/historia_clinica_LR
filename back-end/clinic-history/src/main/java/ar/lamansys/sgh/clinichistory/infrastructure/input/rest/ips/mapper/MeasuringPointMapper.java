package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.MeasuringPointDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface MeasuringPointMapper {


    @Named("toMeasuringPointDto")
    MeasuringPointDto toMeasuringPointDto(MeasuringPointBo measuringPointBo);

    @Named("toMeasuringPointBo")
    MeasuringPointBo toMeasuringPointBo(MeasuringPointDto measuringPointDto);

    @Named("toListMeasuringPointBo")
    @IterableMapping(qualifiedByName = "toMeasuringPointBo")
    List<MeasuringPointBo> toListMeasuringPointBo(List<MeasuringPointDto> MeasuringPointDto);

    @Named("toListMeasuringPointDto")
    @IterableMapping(qualifiedByName = "toMeasuringPointDto")
    List<MeasuringPointDto> toListMeasuringPointDto(List<MeasuringPointBo> MeasuringPointBo);
}
