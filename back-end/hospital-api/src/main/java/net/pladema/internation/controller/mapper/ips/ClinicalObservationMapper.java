package net.pladema.internation.controller.mapper.ips;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.dto.ips.ClinicalObservationDto;
import net.pladema.internation.service.domain.ips.ClinicalObservationBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface ClinicalObservationMapper  {

    @Named("mapClinicalObs")
    default ClinicalObservationBo mapClinicalObs(ClinicalObservationDto source, boolean toDelete) {
        if ( source == null ) {
            return null;
        }
        ClinicalObservationBo result = new ClinicalObservationBo();
        result.setId(source.getId());
        result.setValue(source.getValue());
        result.setDeleted(toDelete);
        return result;
    }
}
