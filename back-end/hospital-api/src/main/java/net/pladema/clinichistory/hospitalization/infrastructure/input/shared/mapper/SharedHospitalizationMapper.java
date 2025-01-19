package net.pladema.clinichistory.hospitalization.infrastructure.input.shared.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.EditableDocumentDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.domain.HospitalizationDocumentBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface SharedHospitalizationMapper {

    @Named("fromHospitalizationDocumentBo")
    HospitalizationDocumentBo fromHospitalizationDocumentBo(EditableDocumentDto editableDocumentDto);
}
