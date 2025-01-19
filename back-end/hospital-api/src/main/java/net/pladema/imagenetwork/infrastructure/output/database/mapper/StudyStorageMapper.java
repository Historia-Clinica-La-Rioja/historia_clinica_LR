package net.pladema.imagenetwork.infrastructure.output.database.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.imagenetwork.domain.ErrorDownloadStudyBo;
import net.pladema.imagenetwork.infrastructure.output.database.entity.ErrorDownloadStudy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface StudyStorageMapper {

    @Named("toErrorDownloadStudy")
    ErrorDownloadStudy toErrorDownloadStudy(ErrorDownloadStudyBo errorDownloadStudyBo);
}
