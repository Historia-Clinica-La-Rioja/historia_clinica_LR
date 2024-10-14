package net.pladema.establishment.controller.mapper;

import java.util.List;

import net.pladema.clinichistory.hospitalization.service.domain.BedBo;
import net.pladema.establishment.controller.dto.BedSummaryDto;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.controller.dto.BedInfoDto;
import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.repository.entity.Bed;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

@Mapper(uses = {LocalDateMapper.class})
public interface BedMapper {

    @Named("toBedDto")
    BedDto toBedDto(Bed bed);

    @Named("toBedDtoFromBedBo")
    BedDto toBedDtoFromBedBo(BedBo bed);
    
    @Named("toListBedDto")
    @IterableMapping(qualifiedByName = "toBedDto")
    List<BedDto> toListBedDto(List<Bed> bedList);

    @Named("toBedInfoDto")
    @Mapping(target = "bed", source = "bed")
    @Mapping(target = "bed.room", source = "room")
    @Mapping(target = "bed.room.sector", source = "sector")
	@Mapping(target = "patient.person.gender.id", source = "patient.person.genderId")
	@Mapping(target = "patient.person.gender.description", source = "patient.person.genderDescription")
    BedInfoDto toBedInfoDto(BedInfoVo bedSummaryVo);
    
    @Named("toBedSummaryDto")
    @Mapping(target = "bed", source = "bed")
    BedSummaryDto toBedSummaryDto(BedSummaryVo bedSummary);
    
    @Named("toListBedSummaryDto")
    @IterableMapping(qualifiedByName = "toBedSummaryDto")
    List<BedSummaryDto> toListBedSummaryDto(List<BedSummaryVo> bedSummaryList);
}
