package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationCriticality;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationType;

import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.IsolationAlertBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.IsolationAlertDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.mapstruct.MappingTarget;

import java.util.stream.Collectors;

@Mapper(uses = {LocalDateMapper.class})
public interface IsolationAlertMapper {
	IsolationAlertDto map(IsolationAlertBo isolationAlert);
	IsolationAlertBo map(IsolationAlertDto isolationAlert);

	 @AfterMapping
	 default void afterMapping(@MappingTarget IsolationAlertDto target, IsolationAlertBo source) {
	 	var typesDto = source.getTypeIds().stream().map(type -> {
			var mData = new MasterDataDto();
			mData.setDescription(EIsolationType.map(type).getDescription());
			mData.setId(type);
			return mData;
		}).collect(Collectors.toList());
		target.setTypes(typesDto);

		var criticalityDto = new MasterDataDto();
		criticalityDto.setId(source.getCriticalityId());
		criticalityDto.setDescription(EIsolationCriticality.map(source.getCriticalityId()).getDescription());
		target.setCriticality(criticalityDto);
	 }

	@AfterMapping
	default void afterMapping(@MappingTarget IsolationAlertBo target, IsolationAlertDto source) {
		var typesBo = source.getTypes().stream().map(type -> type.getId().shortValue()).collect(Collectors.toList());
		target.setTypeIds(typesBo);

		target.setCriticalityId(source.getCriticality().getId().shortValue());
	}
}
