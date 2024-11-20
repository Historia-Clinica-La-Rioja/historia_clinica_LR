package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationCriticality;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationType;

import ar.lamansys.sgh.clinichistory.domain.isolation.FetchPatientIsolationAlertBo;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertAuthorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.dto.IsolationAlertAuthorDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.dto.PatientCurrentIsolationAlertDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;

import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.IsolationAlertDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {LocalDateMapper.class})
public interface IsolationAlertMapper {
	IsolationAlertDto map(IsolationAlertBo isolationAlert);
	IsolationAlertBo map(IsolationAlertDto isolationAlert);
	PatientCurrentIsolationAlertDto map(FetchPatientIsolationAlertBo bo);
	List<PatientCurrentIsolationAlertDto> map(List<FetchPatientIsolationAlertBo> bo);
	IsolationAlertAuthorDto map(IsolationAlertAuthorBo author);

	 @AfterMapping
	 default void afterMapping(@MappingTarget IsolationAlertDto target, IsolationAlertBo source) {
		target.setTypes(toTypeDtos(source.getTypeIds()));
		target.setCriticality(toCriticalityDto(source.getCriticalityId()));
	 }

	@AfterMapping
	default void afterMapping(@MappingTarget IsolationAlertBo target, IsolationAlertDto source) {
		var typesBo = source.getTypes().stream().map(type -> type.getId().shortValue()).collect(Collectors.toList());
		target.setTypeIds(typesBo);
		target.setCriticalityId(source.getCriticality().getId().shortValue());
	}

	@AfterMapping
	default void afterMapping(@MappingTarget PatientCurrentIsolationAlertDto target, FetchPatientIsolationAlertBo source) {
		target.setTypes(toTypeDtos2(source.getTypes()));
		target.setCriticality(toCriticalityDto2(source.getCriticality()));
		target.setStatus(toStatusDto2(source.getStatus()));
	}

	private static MasterDataDto toStatusDto2(EIsolationStatus status) {
		return MasterDataDto.fromShort(status.getId(), status.getDescription());
	}

	private static MasterDataDto toCriticalityDto2(EIsolationCriticality criticality) {
		return MasterDataDto.fromShort(criticality.getId(), criticality.getDescription());
	}

	private static List<MasterDataDto> toTypeDtos2(List<EIsolationType> types) {
		return types.stream()
		.map(type -> MasterDataDto.fromShort(type.getId(), type.getDescription()))
		.collect(Collectors.toList());
	}

	private static MasterDataDto toCriticalityDto(Short criticalityId) {
		return MasterDataDto.fromShort(
			criticalityId,
			EIsolationCriticality.map(criticalityId).getDescription()
		);
	}

	private static List<MasterDataDto> toTypeDtos(List<Short> typeIds) {
		return typeIds.stream()
			.map(type -> MasterDataDto.fromShort(type, EIsolationType.map(type).getDescription()))
			.collect(Collectors.toList());
	}
}
