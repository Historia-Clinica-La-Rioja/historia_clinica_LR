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

import net.pladema.violencereport.domain.ViolenceReportFilterOptionBo;
import net.pladema.violencereport.domain.ViolenceReportImplementedActionsBo;
import net.pladema.violencereport.domain.ViolenceReportSituationBo;
import net.pladema.violencereport.domain.ViolenceReportSituationEvolutionBo;
import net.pladema.violencereport.domain.ViolenceReportVictimBo;
import net.pladema.violencereport.domain.enums.EAggressorRelationship;
import net.pladema.violencereport.domain.enums.ECriminalRecordStatus;
import net.pladema.violencereport.domain.enums.EDisabilityCertificateStatus;
import net.pladema.violencereport.domain.enums.EHealthInstitutionOrganization;
import net.pladema.violencereport.domain.enums.EHealthSystemOrganization;
import net.pladema.violencereport.domain.enums.EInstitutionReportPlace;
import net.pladema.violencereport.domain.enums.EInstitutionReportReason;
import net.pladema.violencereport.domain.enums.EIntermentIndicationStatus;
import net.pladema.violencereport.domain.enums.EKeeperRelationship;
import net.pladema.violencereport.domain.enums.ELiveTogetherStatus;
import net.pladema.violencereport.domain.enums.EMunicipalGovernmentDevice;
import net.pladema.violencereport.domain.enums.ENationalGovernmentDevice;
import net.pladema.violencereport.domain.enums.EProvincialGovernmentDevice;
import net.pladema.violencereport.domain.enums.ERelationshipLength;
import net.pladema.violencereport.domain.enums.ESchoolLevel;
import net.pladema.violencereport.domain.enums.ESecurityForceType;
import net.pladema.violencereport.domain.enums.ESexualViolenceAction;
import net.pladema.violencereport.domain.enums.EVictimKeeperReportPlace;
import net.pladema.violencereport.domain.enums.EViolenceEvaluationRiskLevel;
import net.pladema.violencereport.domain.enums.EViolenceFrequency;
import net.pladema.violencereport.domain.enums.EViolenceTowardsUnderageType;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportActorDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportDto;

import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportFilterOptionDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportSituationDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportSituationEvolutionDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.aggressordetail.ViolenceReportAggressorDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.episodedetail.ViolenceEpisodeDetailDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.episodedetail.ViolenceTowardsUnderageDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.CoordinationActionDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.CoordinationInsideHealthSectorDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.CoordinationOutsideHealthSectorDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.ViolenceReportImplementedActionsDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.victimdetail.ViolenceReportVictimDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class, EDisabilityCertificateStatus.class, EKeeperRelationship.class, EViolenceTowardsUnderageType.class,
		ESchoolLevel.class, EViolenceEvaluationRiskLevel.class, EAggressorRelationship.class, ESecurityForceType.class, ELiveTogetherStatus.class,
		ERelationshipLength.class, EViolenceFrequency.class, ECriminalRecordStatus.class, EIntermentIndicationStatus.class})
public interface ViolenceReportMapper {

	@Mapping(target = "lastName", source = "actorPersonalData.lastName")
	@Mapping(target = "firstName", source = "actorPersonalData.firstName")
	@Mapping(target = "age", source = "actorPersonalData.age")
	@Mapping(target = "address.homeAddress", source = "actorPersonalData.address.homeAddress")
	@Mapping(target = "address.cityId", source = "actorPersonalData.address.city.id")
	@Mapping(target = "address.municipalityId", source = "actorPersonalData.address.municipality.id")
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
	@Mapping(target = "address.homeAddress", source = "actorPersonalData.address.homeAddress")
	@Mapping(target = "address.cityId", source = "actorPersonalData.address.city.id")
	@Mapping(target = "address.municipalityId", source = "actorPersonalData.address.municipality.id")
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

	@Mapping(target = "municipalGovernmentDeviceIds", expression = "java(fromEMunicipalGovernmentDevice(coordinationOutsideHealthSectorDto.getMunicipalGovernmentDevices()))")
	@Mapping(target = "provincialGovernmentDeviceIds", expression = "java(fromEProvincialGovernmentDevice(coordinationOutsideHealthSectorDto.getProvincialGovernmentDevices()))")
	@Mapping(target = "nationalGovernmentDeviceIds", expression = "java(fromENationalGovernmentDevice(coordinationOutsideHealthSectorDto.getNationalGovernmentDevices()))")
	@Named("fromCoordinationOutsideHealthSectorDto")
	CoordinationOutsideHealthSectorBo fromCoordinationOutsideHealthSectorDto (CoordinationOutsideHealthSectorDto coordinationOutsideHealthSectorDto);

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
	@Mapping(target = "otherInstitutionReportPlace", source = "institutionReport.otherInstitutionReportPlace")
	@Mapping(target = "wasSexualViolence", source = "sexualViolence.wasSexualViolence")
	@Mapping(target = "implementedActionIds", expression = "java(fromESexualViolenceAction(violenceReportImplementedActionsDto.getSexualViolence().getImplementedActions()))")
	@Named("fromViolenceReportImplementedActionsDto")
	ViolenceReportImplementedActionsBo fromViolenceReportImplementedActionsDto (ViolenceReportImplementedActionsDto violenceReportImplementedActionsDto);

	@Mapping(target = "victimData", source = "victimData", qualifiedByName = "fromViolenceReportVictimDto")
	@Mapping(target = "episodeData", source = "episodeData", qualifiedByName = "fromViolenceEpisodeDetailDto")
	@Mapping(target = "aggressors", source = "aggressorData", qualifiedByName = "fromViolenceReportAggressorDtoList")
	@Mapping(target = "implementedActions", source = "implementedActions", qualifiedByName = "fromViolenceReportImplementedActionsDto")
	ViolenceReportBo fromViolenceReportDto(ViolenceReportDto violenceReportDto);

	@Mapping(target = "riskLevel", source = "riskLevelId")
	@Named("toViolenceReportSituationDto")
	ViolenceReportSituationDto toViolenceReportSituationDto(ViolenceReportSituationBo violenceReportSituationBo);

	@Mapping(source = "lastName", target = "actorPersonalData.lastName")
	@Mapping(source = "firstName", target = "actorPersonalData.firstName")
	@Mapping(source = "age", target = "actorPersonalData.age")
	@Mapping(source = "address.homeAddress", target = "actorPersonalData.address.homeAddress")
	@Mapping(source = "address.municipalityId", target = "actorPersonalData.address.municipality.id")
	@Mapping(source = "address.municipalityName", target = "actorPersonalData.address.municipality.description")
	@Mapping(source = "address.cityId", target = "actorPersonalData.address.city.id")
	@Mapping(source = "address.cityName", target = "actorPersonalData.address.city.description")
	@Mapping(source = "address.provinceId", target = "actorPersonalData.address.municipality.provinceId")
	@Mapping(source = "relationshipWithVictimId", target = "relationshipWithVictim")
	@Named("toViolenceReportKeeperDto")
	ViolenceReportActorDto<EKeeperRelationship> toViolenceReportKeeperDto(ViolenceReportActorBo violenceReportActorBo);

	@Mapping(source = "hasIncome", target = "incomeData.hasIncome")
	@Mapping(source = "worksAtFormalSector", target = "incomeData.worksAtFormalSector")
	@Mapping(source = "hasDisability", target = "disabilityData.hasDisability")
	@Mapping(source = "disabilityCertificateStatusId", target = "disabilityData.disabilityCertificateStatus", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	@Mapping(source = "isInstitutionalized", target = "institutionalizedData.isInstitutionalized")
	@Mapping(source = "institutionalizedDetails", target = "institutionalizedData.institutionalizedDetails")
	@Mapping(source = "keeperData", target = "keeperData", qualifiedByName = "toViolenceReportKeeperDto")
	@Named("toViolenceReportVictimDto")
	ViolenceReportVictimDto toViolenceReportVictimDto(ViolenceReportVictimBo violenceReportVictimBo);

	@Mapping(target = "violenceTowardsUnderage", expression = "java(toViolenceTowardsUnderageDto(violenceEpisodeDetailBo))")
	@Mapping(source = "riskLevelId", target = "riskLevel", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	@Named("toViolenceEpisodeDetailDto")
	ViolenceEpisodeDetailDto toViolenceEpisodeDetailDto (ViolenceEpisodeDetailBo violenceEpisodeDetailBo);

	@Named("toViolenceTowardsUnderageDto")
	default ViolenceTowardsUnderageDto toViolenceTowardsUnderageDto(ViolenceEpisodeDetailBo violenceEpisodeDetailBo) {
		Short violenceTowardsUnderageTypeId = violenceEpisodeDetailBo.getViolenceTowardsUnderageTypeId();
		Short schoolLevelId = violenceEpisodeDetailBo.getSchoolLevelId();
		Boolean schooled = violenceEpisodeDetailBo.getSchooled();
		if (violenceTowardsUnderageTypeId == null && schoolLevelId == null && schooled == null)
			return null;
		ViolenceTowardsUnderageDto result = new ViolenceTowardsUnderageDto();
		result.setSchooled(violenceEpisodeDetailBo.getSchooled());
		if (violenceTowardsUnderageTypeId != null)
			result.setType(EViolenceTowardsUnderageType.map(violenceTowardsUnderageTypeId));
		if (schoolLevelId != null)
			result.setSchoolLevel(ESchoolLevel.map(schoolLevelId));
		return result;
	}

	@Mapping(source = "lastName", target = "actorPersonalData.lastName")
	@Mapping(source = "firstName", target = "actorPersonalData.firstName")
	@Mapping(source = "age", target = "actorPersonalData.age")
	@Mapping(source = "address.homeAddress", target = "actorPersonalData.address.homeAddress")
	@Mapping(source = "address.municipalityId", target = "actorPersonalData.address.municipality.id")
	@Mapping(source = "address.municipalityName", target = "actorPersonalData.address.municipality.description")
	@Mapping(source = "address.cityId", target = "actorPersonalData.address.city.id")
	@Mapping(source = "address.cityName", target = "actorPersonalData.address.city.description")
	@Mapping(source = "relationshipWithVictimId", target = "relationshipWithVictim")
	@Named("toViolenceReportAggressorDto")
	ViolenceReportActorDto<EAggressorRelationship> toViolenceReportAggressorDto(ViolenceReportActorBo violenceReportActorBo);

	@Mapping(source = "aggressorData", target = "aggressorData", qualifiedByName = "toViolenceReportAggressorDto")
	@Mapping(source = "belongsToSecurityForces", target = "securityForceRelatedData.belongsToSecurityForces")
	@Mapping(source = "inDuty", target = "securityForceRelatedData.inDuty")
	@Mapping(source = "securityForceTypeId", target = "securityForceRelatedData.securityForceTypes", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	@Mapping(source = "livesWithVictimId", target = "livesWithVictim")
	@Mapping(source = "relationshipLengthId", target = "relationshipLength")
	@Mapping(source = "violenceViolenceFrequencyId", target = "violenceViolenceFrequency")
	@Mapping(source = "hasPreviousEpisodesId", target = "hasPreviousEpisodes")
	@Named("toViolenceReportAggressorDto")
	ViolenceReportAggressorDto toViolenceReportAggressorDto (ViolenceReportAggressorBo violenceReportAggressorBo);

	@IterableMapping(qualifiedByName = "toViolenceReportAggressorDto")
	@Named("toViolenceReportAggressorDtoList")
	List<ViolenceReportAggressorDto> toViolenceReportAggressorDtoList (List<ViolenceReportAggressorBo> violenceReportAggressorBos);

	default List<EHealthSystemOrganization> toEHealthSystemOrganization(List<Short> eHealthSystemOrganizationIds) {
		if (eHealthSystemOrganizationIds != null)
			return eHealthSystemOrganizationIds.stream().map(EHealthSystemOrganization::map).collect(Collectors.toList());
		return null;
	}

	@Mapping(target = "organizations", expression = "java(toEHealthSystemOrganization(coordinationActionBo.getOrganizations()))")
	@Named("toHealthSystemCoordinationActionDto")
	CoordinationActionDto<EHealthSystemOrganization> toHealthSystemCoordinationActionDto (CoordinationActionBo coordinationActionBo);

	default List<EHealthInstitutionOrganization> toEHealthInstitutionOrganization(List<Short> eHealthInstitutionOrganizationIds) {
		if (eHealthInstitutionOrganizationIds != null)
			return eHealthInstitutionOrganizationIds.stream().map(EHealthInstitutionOrganization::map).collect(Collectors.toList());
		return null;
	}

	@Mapping(target = "organizations", expression = "java(toEHealthInstitutionOrganization(coordinationActionBo.getOrganizations()))")
	@Named("toHealthInstitutionCoordinationActionDto")
	CoordinationActionDto<EHealthInstitutionOrganization> toHealthInstitutionCoordinationActionDto (CoordinationActionBo coordinationActionBo);

	@Mapping(source = "healthSystemOrganization", target = "healthSystemOrganization", qualifiedByName = "toHealthSystemCoordinationActionDto")
	@Mapping(source = "healthInstitutionOrganization", target = "healthInstitutionOrganization", qualifiedByName = "toHealthInstitutionCoordinationActionDto")
	@Mapping(source = "wereInternmentIndicatedId", target = "wereInternmentIndicated")
	@Named("toCoordinationInsideHealthSectorDto")
	CoordinationInsideHealthSectorDto toCoordinationInsideHealthSectorDto (CoordinationInsideHealthSectorBo coordinationInsideHealthSectorBo);

	default List<EMunicipalGovernmentDevice> toEMunicipalGovernmentDevice(List<Short> eMunicipalGovernmentDeviceIds) {
		if (eMunicipalGovernmentDeviceIds != null)
			return eMunicipalGovernmentDeviceIds.stream().map(EMunicipalGovernmentDevice::map).collect(Collectors.toList());
		return null;
	}

	default List<EProvincialGovernmentDevice> toEProvincialGovernmentDevice(List<Short> eProvincialGovernmentDeviceIds) {
		if (eProvincialGovernmentDeviceIds != null)
			return eProvincialGovernmentDeviceIds.stream().map(EProvincialGovernmentDevice::map).collect(Collectors.toList());
		return null;
	}

	default List<ENationalGovernmentDevice> toENationalGovernmentDevice(List<Short> eNationalGovernmentDeviceIds) {
		if (eNationalGovernmentDeviceIds != null)
			return eNationalGovernmentDeviceIds.stream().map(ENationalGovernmentDevice::map).collect(Collectors.toList());
		return null;
	}

	@Mapping(target = "municipalGovernmentDevices", expression = "java(toEMunicipalGovernmentDevice(coordinationOutsideHealthSectorBo.getMunicipalGovernmentDeviceIds()))")
	@Mapping(target = "provincialGovernmentDevices", expression = "java(toEProvincialGovernmentDevice(coordinationOutsideHealthSectorBo.getProvincialGovernmentDeviceIds()))")
	@Mapping(target = "nationalGovernmentDevices", expression = "java(toENationalGovernmentDevice(coordinationOutsideHealthSectorBo.getNationalGovernmentDeviceIds()))")
	@Named("toCoordinationOutsideHealthSectorDto")
	CoordinationOutsideHealthSectorDto toCoordinationOutsideHealthSectorDto (CoordinationOutsideHealthSectorBo coordinationOutsideHealthSectorBo);

	default List<EVictimKeeperReportPlace> toEVictimKeeperReportPlace(List<Short> eVictimKeeperReportPlaceIds) {
		if (eVictimKeeperReportPlaceIds != null)
			return eVictimKeeperReportPlaceIds.stream().map(EVictimKeeperReportPlace::map).collect(Collectors.toList());
		return null;
	}

	default List<EInstitutionReportReason> toEInstitutionReportReason(List<Short> eInstitutionReportReasonIds) {
		if (eInstitutionReportReasonIds != null)
			return eInstitutionReportReasonIds.stream().map(EInstitutionReportReason::map).collect(Collectors.toList());
		return null;
	}

	default List<EInstitutionReportPlace> toEInstitutionReportPlace(List<Short> eInstitutionReportPlaceIds) {
		if (eInstitutionReportPlaceIds != null)
			return eInstitutionReportPlaceIds.stream().map(EInstitutionReportPlace::map).collect(Collectors.toList());
		return null;
	}

	default List<ESexualViolenceAction> toESexualViolenceAction(List<Short> eSexualViolenceActionIds) {
		if (eSexualViolenceActionIds != null)
			return eSexualViolenceActionIds.stream().map(ESexualViolenceAction::map).collect(Collectors.toList());
		return null;
	}

	@Mapping(source = "coordinationInsideHealthSector", target = "healthCoordination.coordinationInsideHealthSector", qualifiedByName = "toCoordinationInsideHealthSectorDto")
	@Mapping(source = "coordinationOutsideHealthSector", target = "healthCoordination.coordinationOutsideHealthSector", qualifiedByName = "toCoordinationOutsideHealthSectorDto")
	@Mapping(source = "werePreviousEpisodesWithVictimOrKeeper", target = "victimKeeperReport.werePreviousEpisodesWithVictimOrKeeper")
	@Mapping(target = "victimKeeperReport.reportPlaces", expression = "java(toEVictimKeeperReportPlace(violenceReportImplementedActionsBo.getReportPlaceIds()))")
	@Mapping(source = "reportWasDoneByInstitution", target = "institutionReport.reportWasDoneByInstitution")
	@Mapping(target = "institutionReport.reportReasons", expression = "java(toEInstitutionReportReason(violenceReportImplementedActionsBo.getReportReasonIds()))")
	@Mapping(target = "institutionReport.institutionReportPlaces", expression = "java(toEInstitutionReportPlace(violenceReportImplementedActionsBo.getInstitutionReportPlaceIds()))")
	@Mapping(target = "institutionReport.otherInstitutionReportPlace", source = "otherInstitutionReportPlace")
	@Mapping(source = "wasSexualViolence", target = "sexualViolence.wasSexualViolence")
	@Mapping(target = "sexualViolence.implementedActions", expression = "java(toESexualViolenceAction(violenceReportImplementedActionsBo.getImplementedActionIds()))")
	@Named("toViolenceReportImplementedActionsDto")
	ViolenceReportImplementedActionsDto toViolenceReportImplementedActionsDto (ViolenceReportImplementedActionsBo violenceReportImplementedActionsBo);

	@Mapping(target = "victimData", source = "victimData", qualifiedByName = "toViolenceReportVictimDto")
	@Mapping(target = "episodeData", source = "episodeData", qualifiedByName = "toViolenceEpisodeDetailDto")
	@Mapping(target = "aggressorData", source = "aggressors", qualifiedByName = "toViolenceReportAggressorDtoList")
	@Mapping(target = "implementedActions", source = "implementedActions", qualifiedByName = "toViolenceReportImplementedActionsDto")
	ViolenceReportDto toViolenceReportDto(ViolenceReportBo violenceReportBo);

	@Named("toViolenceReportSituationEvolutionDto")
	ViolenceReportSituationEvolutionDto toViolenceReportSituationEvolutionDto(ViolenceReportSituationEvolutionBo violenceReportSituationEvolutionBo);

	@IterableMapping(qualifiedByName = "toViolenceReportSituationEvolutionDto")
	@Named("toViolenceReportSituationEvolutionDtoList")
	List<ViolenceReportSituationEvolutionDto> toViolenceReportSituationEvolutionDtoList(List<ViolenceReportSituationEvolutionBo> violenceReportSituationEvolutionBos);

	@Named("toViolenceReportFilterOptionDto")
	ViolenceReportFilterOptionDto toViolenceReportFilterOptionDto(ViolenceReportFilterOptionBo violenceReportFilterOptionBo);

}
