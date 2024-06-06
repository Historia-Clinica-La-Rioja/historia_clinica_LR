package net.pladema.reports.imageNetworkProductivity.infrastructure.input.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.reports.imageNetworkProductivity.domain.ImageNetworkProductivityFilterBo;

import net.pladema.reports.imageNetworkProductivity.infrastructure.input.dto.ImageNetworkProductivityFilterDto;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = LocalDateMapper.class)
public interface ImageNetworkProductivityReportMapper {

	@Named("fromImageNetworkProductivityFilterDto")
	ImageNetworkProductivityFilterBo fromImageNetworkProductivityFilterDto(ImageNetworkProductivityFilterDto imageNetworkProductivityFilterDto);

}
