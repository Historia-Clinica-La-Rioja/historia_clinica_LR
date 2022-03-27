package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorObservationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorsReportDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(uses = {LocalDateMapper.class})
public interface RiskFactorMapper {

    @Named("toRiskFactorsObservationDto")
    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
	NewRiskFactorsObservationDto toRiskFactorsObservationDto(RiskFactorBo riskFactorBo);

    @Named("fromRiskFactorsObservationDto")
	RiskFactorBo fromRiskFactorsObservationDto(NewRiskFactorsObservationDto riskFactorsObservationDto);

    @Named("fromRiskFactorDto")
	RiskFactorBo fromRiskFactorDto(RiskFactorDto riskFactorDto);

    @Named("fromRiskFactorBo")
	RiskFactorDto fromRiskFactorBo(RiskFactorBo riskFactorBo);

    @Named("toRiskFactorsReportDto")
	RiskFactorsReportDto toRiskFactorsReportDto(RiskFactorBo riskFactors);

    @Named("fromRiskFactorObservationBo")
    @Mapping(target = "riskFactorObservation", source = "riskFactor")
	RiskFactorObservationDto fromRiskFactorObservationBo(RiskFactorObservationBo riskFactorObservationBo);


}
