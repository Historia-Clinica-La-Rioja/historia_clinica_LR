package net.pladema.emergencycare.controller.mapper;

import net.pladema.emergencycare.service.domain.DischargeTypeBo;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface DischargeTypeMasterDataMapper {

    @Named("fromDischargeTypeBo")
    MasterDataDto fromDischargeTypeBo(DischargeTypeBo dischargeTypeBo);

    @Named("fromListDischargeTypeBo")
    @IterableMapping(qualifiedByName = "fromDischargeTypeBo")
    List<MasterDataDto> fromListDischargeTypeBo(List<DischargeTypeBo> dischargeTypeBos);

}
