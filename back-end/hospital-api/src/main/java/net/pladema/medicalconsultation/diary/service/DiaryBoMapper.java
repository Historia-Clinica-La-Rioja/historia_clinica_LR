package net.pladema.medicalconsultation.diary.service;

import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface DiaryBoMapper {

    @Named("toOpeningHours")
    OpeningHours toOpeningHours(OpeningHoursBo openingHoursBo);
}
