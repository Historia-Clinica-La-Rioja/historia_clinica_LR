package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.OtherRiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.OtherRiskFactorDto;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface OtherRiskFactorMapper {

	@Named("fromOtherRiskFactorDto")
	OtherRiskFactorBo fromOtherRiskFactorDto(OtherRiskFactorDto otherRiskFactorDto);

	@Named("fromRiskFactorBo")
	OtherRiskFactorDto fromOtherRiskFactorBo(OtherRiskFactorBo otherRiskFactorBo);

}
