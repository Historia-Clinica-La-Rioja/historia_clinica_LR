package net.pladema.emergencycare.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.PersonAgeDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper.OutpatientConsultationMapper;
import net.pladema.emergencycare.controller.mapper.EmergencyCareClinicalSpecialtySectorMapper;
import net.pladema.emergencycare.controller.mapper.MasterDataMapper;
import net.pladema.emergencycare.domain.ChangeEmergencyCareEpisodeAttentionPlaceBo;
import net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceBo;
import net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceDetailBo;
import net.pladema.emergencycare.domain.EmergencyCareBedDetailBo;
import net.pladema.emergencycare.domain.EmergencyCareDoctorsOfficeDetailBo;
import net.pladema.emergencycare.domain.EmergencyCareSectorHasAttentionPlaceBo;
import net.pladema.emergencycare.domain.EmergencyCareShockRoomDetailBo;
import net.pladema.emergencycare.infrastructure.input.rest.dto.ChangeEmergencyCareEpisodeAttentionPlaceDto;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareAttentionPlaceDetailDto;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareAttentionPlaceDto;

import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareBedDetailDto;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareBedDto;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareDoctorsOfficeDetailDto;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareDoctorsOfficeDto;

import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareSectorHasAttentionPlaceDto;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareShockRoomDetailDto;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.emergencycare.triage.infrastructure.input.rest.mapper.TriageMapper;
import net.pladema.establishment.domain.bed.EmergencyCareBedBo;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;
import net.pladema.medicalconsultation.shockroom.infrastructure.controller.dto.ShockroomDto;
import net.pladema.person.repository.domain.PersonAgeBo;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {EmergencyCareClinicalSpecialtySectorMapper.class, MasterDataMapper.class, OutpatientConsultationMapper.class,
		LocalDateMapper.class, TriageMapper.class})
public interface EmergencyCareAttentionPlaceMapper {

	@Named("toDtoList")
	@IterableMapping(qualifiedByName = "toDto")
	List<EmergencyCareAttentionPlaceDto> toDtoList(List<EmergencyCareAttentionPlaceBo> emergencyCareAttentionPlaceBoList);

	@Named("toDto")
	EmergencyCareAttentionPlaceDto toDto(EmergencyCareAttentionPlaceBo emergencyCareAttentionPlaceBo);

	@Named("toDoctorsOfficeDtoList")
	@IterableMapping(qualifiedByName = "toDoctorsOfficeDto")
	List<EmergencyCareDoctorsOfficeDto> toDoctorsOfficeDtoList(List<DoctorsOfficeBo> doctorsOfficeBoList) ;

	@Named("toDoctorsOfficeDto")
	EmergencyCareDoctorsOfficeDto toDoctorsOfficeDto(DoctorsOfficeBo doctorsOfficeBo);

	@Named("toEmergencyCareBedDtoList")
	@IterableMapping(qualifiedByName = "toEmergencyCareBedDto")
	List<EmergencyCareBedDto> toEmergencyCareBedDtoList(List<EmergencyCareBedBo> emergencyCareBedBoList) ;

	@Named("toEmergencyCareBedDto")
	EmergencyCareBedDto toEmergencyCareBedDto(EmergencyCareBedBo emergencyCareBedBo);

	@Named("toEmergencyCareBedDetailDto")
	@Mapping(target = "type", ignore = true)
	@Mapping(target = "state", ignore = true)
	@Mapping(target = "lastTriage", source = "lastTriage", qualifiedByName = "toEmergencyCareEpisodeListTriageDto")
	EmergencyCareBedDetailDto toEmergencyCareBedDetailDto(
		EmergencyCareBedDetailBo emergencyCareBedDetailBo,
		@Context Boolean selfDeterminedNameFFIsOn
	);

	@Named("toPersonAgeDto")
	PersonAgeDto toPersonAgeDto(PersonAgeBo personAgeBo);

	@Named("toEmergencyCareShockRoomDetailDto")
	@Mapping(target = "type", ignore = true)
	@Mapping(target = "state", ignore = true)
	@Mapping(target = "lastTriage", source = "lastTriage", qualifiedByName = "toEmergencyCareEpisodeListTriageDto")
	EmergencyCareShockRoomDetailDto toEmergencyCareShockRoomDetailDto(
		EmergencyCareShockRoomDetailBo emergencyCareShockRoomDetailBo,
		@Context Boolean selfDeterminedNameFFIsOn
	);

	@Named("toEmergencyCareDoctorsOfficeDetailDto")
	@Mapping(target = "type", ignore = true)
	@Mapping(target = "state", ignore = true)
	@Mapping(target = "lastTriage", source = "lastTriage", qualifiedByName = "toEmergencyCareEpisodeListTriageDto")
	EmergencyCareDoctorsOfficeDetailDto toEmergencyCareDoctorsOfficeDetailDto(
		EmergencyCareDoctorsOfficeDetailBo emergencyCareDoctorsOfficeDetailBo,
		@Context Boolean selfDeterminedNameFFIsOn);

	@Named("toEmergencyCareSectorHasAttentionPlaceDto")
	EmergencyCareSectorHasAttentionPlaceDto toEmergencyCareSectorHasAttentionPlaceDto(EmergencyCareSectorHasAttentionPlaceBo emergencyCareSectorHasAttentionPlaceBo);

	@Named("toShockroomDto")
	List<ShockroomDto> toShockroomDto(List<ShockRoomBo> shockRoomBo);

	@Named("toChangeEmergencyCareEpisodeAttentionPlaceBo")
	ChangeEmergencyCareEpisodeAttentionPlaceBo toChangeEmergencyCareEpisodeAttentionPlaceBo(ChangeEmergencyCareEpisodeAttentionPlaceDto changeEmergencyCareEpisodeAttentionPlaceDto);

	@AfterMapping
	default void masterDataEmergencyCareAttentionMapping(@MappingTarget EmergencyCareAttentionPlaceDetailDto target,
														 EmergencyCareAttentionPlaceDetailBo emergencyCareAttentionPlaceDetailBo) {
		target.setType(EnumWriter.write(EEmergencyCareType.getById(emergencyCareAttentionPlaceDetailBo.getEmergencyCareTypeId())));
		target.setState(EnumWriter.write(EEmergencyCareState.getById(emergencyCareAttentionPlaceDetailBo.getEmergencyCareStateId())));
	}

	@AfterMapping
	default void emergencyCareDoctorsOfficeDetailDtoAfterMapping(
		@MappingTarget EmergencyCareAttentionPlaceDetailDto target,
		EmergencyCareAttentionPlaceDetailBo source,
		@Context Boolean selfDeterminedNameFFIsOn
	) {
		if (source.getProfessional() != null && target.getProfessional() != null) {
			target.getProfessional().setFullName(source.getProfessional().getFullName(selfDeterminedNameFFIsOn));
		}
	}
}
