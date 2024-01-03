package net.pladema.violencereport.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.violencereport.domain.CoordinationActionBo;
import net.pladema.violencereport.domain.CoordinationInsideHealthSectorBo;
import net.pladema.violencereport.domain.CoordinationOutsideHealthSectorBo;
import net.pladema.violencereport.domain.ViolenceEpisodeDetailBo;
import net.pladema.violencereport.domain.ViolenceReportActorBo;
import net.pladema.violencereport.domain.ViolenceReportAggressorBo;
import net.pladema.violencereport.domain.ViolenceReportBo;

import net.pladema.violencereport.domain.ViolenceReportImplementedActionsBo;
import net.pladema.violencereport.domain.ViolenceReportVictimBo;
import net.pladema.violencereport.domain.enums.EAggressorRelationship;
import net.pladema.violencereport.domain.enums.EHealthInstitutionOrganization;
import net.pladema.violencereport.domain.enums.EHealthSystemOrganization;
import net.pladema.violencereport.domain.enums.EInstitutionReportPlace;
import net.pladema.violencereport.domain.enums.EInstitutionReportReason;
import net.pladema.violencereport.domain.enums.EKeeperRelationship;
import net.pladema.violencereport.domain.enums.EMunicipalGovernmentDevice;
import net.pladema.violencereport.domain.enums.ENationalGovernmentDevice;
import net.pladema.violencereport.domain.enums.EProvincialGovernmentDevice;
import net.pladema.violencereport.domain.enums.ESexualViolenceAction;
import net.pladema.violencereport.domain.enums.EVictimKeeperReportPlace;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportActorDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportDto;

import net.pladema.violencereport.infrastructure.input.rest.dto.aggressordetail.ViolenceReportAggressorDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.episodedetail.ViolenceEpisodeDetailDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.CoordinationActionDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.CoordinationInsideHealthSectorDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.CoordinationOutsideHealthSectorDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.ViolenceReportImplementedActionsDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.victimdetail.ViolenceReportVictimDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface ViolenceReportMapper {

	@Mapping(target = "lastName", source = "actorPersonalData.lastName")
	@Mapping(target = "firstName", source = "actorPersonalData.firstName")
	@Mapping(target = "age", source = "actorPersonalData.age")
	@Mapping(target = "address", source = "actorPersonalData.address")
	@Mapping(target = "municipalityId", source = "actorPersonalData.municipalityId")
	@Mapping(target = "relationshipWithVictimId", source = "relationshipWithVictim.id")
	@Named("fromViolenceReportKeeperDto")
	ViolenceReportActorBo fromViolenceReportKeeperDto(ViolenceReportActorDto<EKeeperRelationship> violenceReportActorDto);

	@Mapping(target = "hasIncome", source = "incomeData.hasIncome")
	@Mapping(target = "worksAtFormalSector", source = "incomeData.worksAtFormalSector")
	@Mapping(target = "hasDisability", source = "disabilityData.hasDisability")
	@Mapping(target = "disabilityCertificateStatusId", source = "disabilityData.disabilityCertificateStatus.id")
	@Mapping(target = "isInstitutionalized", source = "institutionalizedData.isInstitutionalized")
	@Mapping(target = "institutionalizedDetails", source = "institutionalizedData.institutionalizedDetails")
	@Mapping(target = "keeperData", source = "keeperData", qualifiedByName = "fromViolenceReportKeeperDto")
	@Named("fromViolenceReportVictimDto")
	ViolenceReportVictimBo fromViolenceReportVictimDto(ViolenceReportVictimDto violenceReportVictimDto);

	@Mapping(target = "violenceTowardsUnderageTypeId", source = "violenceTowardsUnderage.type.id")
	@Mapping(target = "schooled", source = "violenceTowardsUnderage.schooled")
	@Mapping(target = "schoolLevelId", source = "violenceTowardsUnderage.schoolLevel.id")
	@Mapping(target = "riskLevelId", source = "riskLevel.id")
	@Named("fromViolenceEpisodeDetailDto")
	ViolenceEpisodeDetailBo fromViolenceEpisodeDetailDto (ViolenceEpisodeDetailDto violenceEpisodeDetailDto);

	@Mapping(target = "lastName", source = "actorPersonalData.lastName")
	@Mapping(target = "firstName", source = "actorPersonalData.firstName")
	@Mapping(target = "age", source = "actorPersonalData.age")
	@Mapping(target = "address", source = "actorPersonalData.address")
	@Mapping(target = "municipalityId", source = "actorPersonalData.municipalityId")
	@Mapping(target = "relationshipWithVictimId", source = "relationshipWithVictim.id")
	@Named("fromViolenceReportAggressorDto")
	ViolenceReportActorBo fromViolenceReportAggressorDto(ViolenceReportActorDto<EAggressorRelationship> violenceReportActorDto);

	@Mapping(target = "aggressorData", source = "aggressorData", qualifiedByName = "fromViolenceReportAggressorDto")
	@Mapping(target = "belongsToSecurityForces", source = "securityForceRelatedData.belongsToSecurityForces")
	@Mapping(target = "inDuty", source = "securityForceRelatedData.inDuty")
	@Mapping(target = "securityForceTypeId", source = "securityForceRelatedData.securityForceTypes.id")
	@Mapping(target = "livesWithVictimId", source = "livesWithVictim.id")
	@Mapping(target = "relationshipLengthId", source = "relationshipLength.id")
	@Mapping(target = "violenceViolenceFrequencyId", source = "violenceViolenceFrequency.id")
	@Mapping(target = "hasPreviousEpisodesId", source = "hasPreviousEpisodes.id")
	@Named("fromViolenceReportAggressorDto")
	ViolenceReportAggressorBo fromViolenceReportAggressorDto (ViolenceReportAggressorDto violenceReportAggressorDto);

	@IterableMapping(qualifiedByName = "fromViolenceReportAggressorDto")
	@Named("fromViolenceReportAggressorDtoList")
	List<ViolenceReportAggressorBo> fromViolenceReportAggressorDtoList (List<ViolenceReportAggressorDto> violenceReportAggressorDtos);

	default List<Short> fromEHealthSystemOrganization(List<EHealthSystemOrganization> eHealthSystemOrganizations) {
		if (eHealthSystemOrganizations != null)
			return eHealthSystemOrganizations.stream().map(EHealthSystemOrganization::getId).collect(Collectors.toList());
		return null;
	}

	@Mapping(target = "organizations", expression = "java(fromEHealthSystemOrganization(coordinationActionDto.getOrganizations()))")
	@Named("fromHealthSystemCoordinationActionDto")
	CoordinationActionBo fromHealthSystemCoordinationActionDto (CoordinationActionDto<EHealthSystemOrganization> coordinationActionDto);

	default List<Short> fromEHealthInstitutionOrganization(List<EHealthInstitutionOrganization> eHealthInstitutionOrganizations) {
		if (eHealthInstitutionOrganizations != null)
			return eHealthInstitutionOrganizations.stream().map(EHealthInstitutionOrganization::getId).collect(Collectors.toList());
		return null;
	}

	@Mapping(target = "organizations", expression = "java(fromEHealthInstitutionOrganization(coordinationActionDto.getOrganizations()))")
	@Named("fromHealthInstitutionCoordinationActionDto")
	CoordinationActionBo fromHealthInstitutionCoordinationActionDto (CoordinationActionDto<EHealthInstitutionOrganization> coordinationActionDto);


	@Mapping(target = "healthSystemOrganization", source = "healthSystemOrganization", qualifiedByName = "fromHealthSystemCoordinationActionDto")
	@Mapping(target = "healthInstitutionOrganization", source = "healthInstitutionOrganization", qualifiedByName = "fromHealthInstitutionCoordinationActionDto")
	@Mapping(target = "wereInternmentIndicatedId", source = "wereInternmentIndicated.id")
	@Named("fromCoordinationInsideHealthSectorDto")
	CoordinationInsideHealthSectorBo fromCoordinationInsideHealthSectorDto (CoordinationInsideHealthSectorDto coordinationInsideHealthSectorDto);

	default List<Short> fromEMunicipalGovernmentDevice(List<EMunicipalGovernmentDevice> eMunicipalGovernmentDevices) {
		if (eMunicipalGovernmentDevices != null)
			return eMunicipalGovernmentDevices.stream().map(EMunicipalGovernmentDevice::getId).collect(Collectors.toList());
		return null;
	}

	default List<Short> fromEProvincialGovernmentDevice(List<EProvincialGovernmentDevice> eProvincialGovernmentDevices) {
		if (eProvincialGovernmentDevices != null)
			return eProvincialGovernmentDevices.stream().map(EProvincialGovernmentDevice::getId).collect(Collectors.toList());
		return null;
	}

	default List<Short> fromENationalGovernmentDevice(List<ENationalGovernmentDevice> eNationalGovernmentDevices) {
		if (eNationalGovernmentDevices != null)
			return eNationalGovernmentDevices.stream().map(ENationalGovernmentDevice::getId).collect(Collectors.toList());
		return null;
	}

	@Mapping(target = "municipalGovernmentDeviceIds", expression = "java(fromEMunicipalGovernmentDevice(coordinationInsideHealthSectorDto.getMunicipalGovernmentDevices()))")
	@Mapping(target = "provincialGovernmentDeviceIds", expression = "java(fromEProvincialGovernmentDevice(coordinationInsideHealthSectorDto.getProvincialGovernmentDevices()))")
	@Mapping(target = "nationalGovernmentDeviceIds", expression = "java(fromENationalGovernmentDevice(coordinationInsideHealthSectorDto.getNationalGovernmentDevices()))")
	@Named("fromCoordinationOutsideHealthSectorDto")
	CoordinationOutsideHealthSectorBo fromCoordinationOutsideHealthSectorDto (CoordinationOutsideHealthSectorDto coordinationInsideHealthSectorDto);

	default List<Short> fromEVictimKeeperReportPlace(List<EVictimKeeperReportPlace> eVictimKeeperReportPlaces) {
		if (eVictimKeeperReportPlaces != null)
			return eVictimKeeperReportPlaces.stream().map(EVictimKeeperReportPlace::getId).collect(Collectors.toList());
		return null;
	}

	default List<Short> fromEInstitutionReportReason(List<EInstitutionReportReason> eInstitutionReportReasons) {
		if (eInstitutionReportReasons != null)
			return eInstitutionReportReasons.stream().map(EInstitutionReportReason::getId).collect(Collectors.toList());
		return null;
	}

	default List<Short> fromEInstitutionReportPlace(List<EInstitutionReportPlace> eInstitutionReportPlaces) {
		if (eInstitutionReportPlaces != null)
			return eInstitutionReportPlaces.stream().map(EInstitutionReportPlace::getId).collect(Collectors.toList());
		return null;
	}

	default List<Short> fromESexualViolenceAction(List<ESexualViolenceAction> eSexualViolenceActions) {
		if (eSexualViolenceActions != null)
			return eSexualViolenceActions.stream().map(ESexualViolenceAction::getId).collect(Collectors.toList());
		return null;
	}

	@Mapping(target = "coordinationInsideHealthSector", source = "healthCoordination.coordinationInsideHealthSector", qualifiedByName = "fromCoordinationInsideHealthSectorDto")
	@Mapping(target = "coordinationOutsideHealthSector", source = "healthCoordination.coordinationOutsideHealthSector", qualifiedByName = "fromCoordinationOutsideHealthSectorDto")
	@Mapping(target = "werePreviousEpisodesWithVictimOrKeeper", source = "victimKeeperReport.werePreviousEpisodesWithVictimOrKeeper")
	@Mapping(target = "reportPlaceIds", expression = "java(fromEVictimKeeperReportPlace(violenceReportImplementedActionsDto.getVictimKeeperReport().getReportPlaces()))")
	@Mapping(target = "reportWasDoneByInstitution", source = "institutionReport.reportWasDoneByInstitution")
	@Mapping(target = "reportReasonIds", expression = "java(fromEInstitutionReportReason(violenceReportImplementedActionsDto.getInstitutionReport().getReportReasons()))")
	@Mapping(target = "institutionReportPlaceIds", expression = "java(fromEInstitutionReportPlace(violenceReportImplementedActionsDto.getInstitutionReport().getInstitutionReportPlaces()))")
	@Mapping(target = "wasSexualViolence", source = "sexualViolence.wasSexualViolence")
	@Mapping(target = "implementedActionIds", expression = "java(fromESexualViolenceAction(violenceReportImplementedActionsDto.getSexualViolence().getImplementedActions()))")
	@Named("fromViolenceReportImplementedActionsDto")
	ViolenceReportImplementedActionsBo fromViolenceReportImplementedActionsDto (ViolenceReportImplementedActionsDto violenceReportImplementedActionsDto);

	@Mapping(target = "victimData", source = "victimData", qualifiedByName = "fromViolenceReportVictimDto")
	@Mapping(target = "episodeData", source = "episodeData", qualifiedByName = "fromViolenceEpisodeDetailDto")
	@Mapping(target = "aggressors", source = "aggressorData", qualifiedByName = "fromViolenceReportAggressorDtoList")
	@Mapping(target = "implementedActions", source = "implementedActions", qualifiedByName = "fromViolenceReportImplementedActionsDto")
	ViolenceReportBo fromViolenceReportDto(ViolenceReportDto violenceReportDto);

}
