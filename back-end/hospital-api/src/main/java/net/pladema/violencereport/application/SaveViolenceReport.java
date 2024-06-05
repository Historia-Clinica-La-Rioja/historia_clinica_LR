package net.pladema.violencereport.application;

import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.cipres.domain.SnomedBo;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.violencereport.domain.CoordinationActionBo;
import net.pladema.violencereport.domain.CoordinationInsideHealthSectorBo;
import net.pladema.violencereport.domain.ViolenceReportActorBo;
import net.pladema.violencereport.domain.ViolenceReportAggressorBo;
import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.domain.enums.EHealthInstitutionOrganization;
import net.pladema.violencereport.domain.enums.EHealthSystemOrganization;
import net.pladema.violencereport.domain.enums.EInstitutionReportPlace;
import net.pladema.violencereport.infrastructure.output.repository.HealthInstitutionOrganizationCoordinationRepository;
import net.pladema.violencereport.infrastructure.output.repository.HealthSystemOrganizationCoordinationRepository;
import net.pladema.violencereport.infrastructure.output.repository.InstitutionReportPlaceRepository;
import net.pladema.violencereport.infrastructure.output.repository.InstitutionReportReasonRepository;
import net.pladema.violencereport.infrastructure.output.repository.MunicipalStateDeviceRepository;
import net.pladema.violencereport.infrastructure.output.repository.NationalStateDeviceRepository;
import net.pladema.violencereport.infrastructure.output.repository.ProvincialStateDeviceRepository;
import net.pladema.violencereport.infrastructure.output.repository.SexualViolenceActionRepository;
import net.pladema.violencereport.infrastructure.output.repository.VictimKeeperReportPlaceRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceModalityRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportAggressorRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportKeeperRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceTypeRepository;
import net.pladema.violencereport.infrastructure.output.repository.embedded.HealthInstitutionOrganizationCoordinationPK;
import net.pladema.violencereport.infrastructure.output.repository.embedded.HealthSystemOrganizationCoordinationPK;
import net.pladema.violencereport.infrastructure.output.repository.embedded.InstitutionReportPlacePK;
import net.pladema.violencereport.infrastructure.output.repository.embedded.InstitutionReportReasonPK;
import net.pladema.violencereport.infrastructure.output.repository.embedded.MunicipalStateDevicePK;
import net.pladema.violencereport.infrastructure.output.repository.embedded.NationalStateDevicePK;
import net.pladema.violencereport.infrastructure.output.repository.embedded.ProvincialStateDevicePK;
import net.pladema.violencereport.infrastructure.output.repository.embedded.SexualViolenceActionPK;
import net.pladema.violencereport.infrastructure.output.repository.embedded.VictimKeeperReportPlacePK;
import net.pladema.violencereport.infrastructure.output.repository.embedded.ViolenceReportSnomedPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.HealthInstitutionOrganizationCoordination;
import net.pladema.violencereport.infrastructure.output.repository.entity.HealthSystemOrganizationCoordination;
import net.pladema.violencereport.infrastructure.output.repository.entity.InstitutionReportPlace;
import net.pladema.violencereport.infrastructure.output.repository.entity.InstitutionReportReason;
import net.pladema.violencereport.infrastructure.output.repository.entity.MunicipalStateDevice;
import net.pladema.violencereport.infrastructure.output.repository.entity.NationalStateDevice;
import net.pladema.violencereport.infrastructure.output.repository.entity.ProvincialStateDevice;
import net.pladema.violencereport.infrastructure.output.repository.entity.SexualViolenceAction;
import net.pladema.violencereport.infrastructure.output.repository.entity.VictimKeeperReportPlace;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceModality;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReport;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReportAggressor;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReportKeeper;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceType;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class SaveViolenceReport {

	private ViolenceReportRepository violenceReportRepository;

	private ViolenceReportKeeperRepository violenceReportKeeperRepository;

	private ViolenceTypeRepository violenceTypeRepository;

	private ViolenceModalityRepository violenceModalityRepository;

	private ViolenceReportAggressorRepository violenceReportAggressorRepository;

	private HealthSystemOrganizationCoordinationRepository healthSystemOrganizationCoordinationRepository;

	private HealthInstitutionOrganizationCoordinationRepository healthInstitutionOrganizationCoordinationRepository;

	private MunicipalStateDeviceRepository municipalStateDeviceRepository;

	private ProvincialStateDeviceRepository provincialStateDeviceRepository;

	private NationalStateDeviceRepository nationalStateDeviceRepository;

	private VictimKeeperReportPlaceRepository victimKeeperReportPlaceRepository;

	private InstitutionReportPlaceRepository institutionReportPlaceRepository;

	private InstitutionReportReasonRepository institutionReportReasonRepository;

	private SexualViolenceActionRepository sexualViolenceActionRepository;

	private SnomedService snomedService;

	@Transactional
	public Integer run(ViolenceReportBo violenceReport) {
		log.debug("Input parameters -> violenceReport {}", violenceReport);
		Integer result = violenceReportRepository.save(parseViolenceReport(violenceReport)).getId();
		saveKeeperData(violenceReport, result);
		saveViolenceType(violenceReport, result);
		saveViolenceModality(violenceReport, result);
		saveViolenceReportAggressor(violenceReport, result);
		saveHealthOrganizationCoordination(violenceReport, result);
		saveVictimAndKeeperReportPlaces(violenceReport, result);
		saveInstitutionReportRelatedData(violenceReport, result);
		saveSexualViolenceImplementedActions(violenceReport, result);
		log.debug("Output -> {}", result);
		return result;
	}

	private void saveSexualViolenceImplementedActions(ViolenceReportBo violenceReport, Integer reportId) {
		Boolean wasSexualViolence = violenceReport.getImplementedActions().getWasSexualViolence();
		if (wasSexualViolence)
			saveSexualViolenceActions(violenceReport, reportId);
	}

	private void saveSexualViolenceActions(ViolenceReportBo violenceReport, Integer reportId) {
		List<Short> implementedActionIds = violenceReport.getImplementedActions().getImplementedActionIds();
		if (implementedActionIds != null && !implementedActionIds.isEmpty())
			sexualViolenceActionRepository.saveAll(parseSexualViolenceActions(reportId, implementedActionIds));
	}

	private List<SexualViolenceAction> parseSexualViolenceActions(Integer reportId, List<Short> implementedActionIds) {
		return implementedActionIds.stream().map(actionId -> parseSexualViolenceAction(reportId, actionId)).collect(Collectors.toList());
	}

	private SexualViolenceAction parseSexualViolenceAction(Integer reportId, Short actionId) {
		SexualViolenceAction action = new SexualViolenceAction();
		action.setPk(new SexualViolenceActionPK(reportId, actionId));
		return action;
	}

	private void saveInstitutionReportRelatedData(ViolenceReportBo violenceReport, Integer reportId) {
		Boolean institutionReported = violenceReport.getImplementedActions().getReportWasDoneByInstitution();
		if (institutionReported)
			saveInstitutionReportReasonsAndPlaces(violenceReport, reportId);
	}

	private void saveInstitutionReportReasonsAndPlaces(ViolenceReportBo violenceReport, Integer reportId) {
		saveInstitutionReportReasons(violenceReport, reportId);
		saveInstitutionReportPlaces(violenceReport, reportId);
	}

	private void saveInstitutionReportPlaces(ViolenceReportBo violenceReport, Integer reportId) {
		List<Short> institutionReportPlaceIds = violenceReport.getImplementedActions().getInstitutionReportPlaceIds();
		if (institutionReportPlaceIds != null && ! institutionReportPlaceIds.isEmpty()) {
			String otherInstitutionReportPlace = violenceReport.getImplementedActions().getOtherInstitutionReportPlace();
			institutionReportPlaceRepository.saveAll(parseInstitutionReportPlaces(reportId, otherInstitutionReportPlace, institutionReportPlaceIds));
		}
	}

	private List<InstitutionReportPlace> parseInstitutionReportPlaces(Integer reportId, String otherInstitutionReportPlace, List<Short> institutionReportPlaceIds) {
		return institutionReportPlaceIds.stream().map(reportPlaceId -> parseInstitutionReportPlace(reportId, otherInstitutionReportPlace, reportPlaceId)).collect(Collectors.toList());
	}

	private InstitutionReportPlace parseInstitutionReportPlace(Integer reportId, String otherInstitutionReportPlace, Short reportPlaceId) {
		InstitutionReportPlace reportPlace = new InstitutionReportPlace();
		reportPlace.setPk(new InstitutionReportPlacePK(reportId, reportPlaceId));
		if (reportPlaceId.equals(EInstitutionReportPlace.OTHER.getId()))
			reportPlace.setOtherReportPlace(otherInstitutionReportPlace);
		return reportPlace;
	}

	private void saveInstitutionReportReasons(ViolenceReportBo violenceReport, Integer reportId) {
		List<Short> institutionReportReasonIds = violenceReport.getImplementedActions().getReportReasonIds();
		if (institutionReportReasonIds != null && !institutionReportReasonIds.isEmpty())
			institutionReportReasonRepository.saveAll(parseInstitutionReportReasons(reportId, institutionReportReasonIds));
	}

	private List<InstitutionReportReason> parseInstitutionReportReasons(Integer reportId, List<Short> institutionReportReasonIds) {
		return institutionReportReasonIds.stream().map(reportReasonId -> parseInstitutionReportReason(reportId, reportReasonId)).collect(Collectors.toList());
	}

	private InstitutionReportReason parseInstitutionReportReason(Integer reportId, Short institutionReportReasonId) {
		InstitutionReportReason reportReason = new InstitutionReportReason();
		reportReason.setPk(new InstitutionReportReasonPK(reportId, institutionReportReasonId));
		return reportReason;
	}

	private void saveVictimAndKeeperReportPlaces(ViolenceReportBo violenceReport, Integer reportId) {
		Boolean werePreviousVictimAndKeeperReports = violenceReport.getImplementedActions().getWerePreviousEpisodesWithVictimOrKeeper();
		if (werePreviousVictimAndKeeperReports)
			handleVictimAndKeeperReportPlaces(violenceReport, reportId);
	}

	private void handleVictimAndKeeperReportPlaces(ViolenceReportBo violenceReport, Integer reportId) {
		List<Short> reportPlaceIds = violenceReport.getImplementedActions().getReportPlaceIds();
		victimKeeperReportPlaceRepository.saveAll(parseVictimAndKeeperReportPlaces(reportId, reportPlaceIds));
	}

	private List<VictimKeeperReportPlace> parseVictimAndKeeperReportPlaces(Integer reportId, List<Short> reportPlaceIds) {
		return reportPlaceIds.stream().map(reportPlaceId -> parseVictimAndKeeperReportPlace(reportId, reportPlaceId)).collect(Collectors.toList());
	}

	private VictimKeeperReportPlace parseVictimAndKeeperReportPlace(Integer reportId, Short reportPlaceId) {
		VictimKeeperReportPlace reportPlace = new VictimKeeperReportPlace();
		reportPlace.setPk(new VictimKeeperReportPlacePK(reportId, reportPlaceId));
		return reportPlace;
	}

	private void saveHealthOrganizationCoordination(ViolenceReportBo violenceReport, Integer reportId) {
		CoordinationInsideHealthSectorBo coordinationInsideHealthSectorBo = violenceReport.getImplementedActions().getCoordinationInsideHealthSector();
		if (coordinationInsideHealthSectorBo != null)
			saveHealthOrganizationInsideHealthSystem(reportId, coordinationInsideHealthSectorBo);
		else
			saveHealthOrganizationOutsideHealthSystem(violenceReport, reportId);
	}

	private void saveHealthOrganizationInsideHealthSystem(Integer reportId, CoordinationInsideHealthSectorBo coordinationInsideHealthSectorBo) {
		saveOrganizationsWithinHealthSystem(reportId, coordinationInsideHealthSectorBo);
		saveOrganizationsWithinHealthInstitution(reportId, coordinationInsideHealthSectorBo);
	}

	private void saveHealthOrganizationOutsideHealthSystem(ViolenceReportBo violenceReport, Integer reportId) {
		saveMunicipalStateDevices(reportId, violenceReport);
		saveProvincialStateDevices(reportId, violenceReport);
		saveNationalStateDevices(reportId, violenceReport);
	}

	private void saveNationalStateDevices(Integer reportId, ViolenceReportBo violenceReport) {
		List<Short> nationalStateDeviceIds = violenceReport.getImplementedActions().getCoordinationOutsideHealthSector().getNationalGovernmentDeviceIds();
		if (nationalStateDeviceIds != null && !nationalStateDeviceIds.isEmpty())
			nationalStateDeviceRepository.saveAll(parseNationalStateDevices(reportId, nationalStateDeviceIds));
	}

	private void saveProvincialStateDevices(Integer reportId, ViolenceReportBo violenceReport) {
		List<Short> provincialStateDeviceIds = violenceReport.getImplementedActions().getCoordinationOutsideHealthSector().getProvincialGovernmentDeviceIds();
		if (provincialStateDeviceIds != null && !provincialStateDeviceIds.isEmpty())
			provincialStateDeviceRepository.saveAll(parseProvincialStateDevices(reportId, provincialStateDeviceIds));
	}

	private void saveMunicipalStateDevices(Integer reportId, ViolenceReportBo violenceReport) {
		List<Short> municipalStateDeviceIds = violenceReport.getImplementedActions().getCoordinationOutsideHealthSector().getMunicipalGovernmentDeviceIds();
		if (municipalStateDeviceIds != null && !municipalStateDeviceIds.isEmpty())
			municipalStateDeviceRepository.saveAll(parseMunicipalStateDevices(reportId, municipalStateDeviceIds));
	}

	private List<NationalStateDevice> parseNationalStateDevices(Integer reportId, List<Short> nationalStateDeviceIds) {
		return nationalStateDeviceIds.stream().map(nationalStateDeviceId -> parseNationalStateDevice(reportId, nationalStateDeviceId)).collect(Collectors.toList());
	}

	private NationalStateDevice parseNationalStateDevice(Integer reportId, Short nationalStateDeviceId) {
		NationalStateDevice nationalStateDevice = new NationalStateDevice();
		nationalStateDevice.setPk(new NationalStateDevicePK(reportId, nationalStateDeviceId));
		return nationalStateDevice;
	}

	private List<ProvincialStateDevice> parseProvincialStateDevices(Integer reportId, List<Short> provincialStateDeviceIds) {
		return provincialStateDeviceIds.stream().map(provincialStateDeviceId -> parseProvincialStateDevice(reportId, provincialStateDeviceId)).collect(Collectors.toList());
	}

	private ProvincialStateDevice parseProvincialStateDevice(Integer reportId, Short provincialStateDeviceId) {
		ProvincialStateDevice provincialStateDevice = new ProvincialStateDevice();
		provincialStateDevice.setPk(new ProvincialStateDevicePK(reportId, provincialStateDeviceId));
		return provincialStateDevice;
	}

	private List<MunicipalStateDevice> parseMunicipalStateDevices(Integer reportId, List<Short> municipalStateDeviceIds) {
		return municipalStateDeviceIds.stream().map(municipalStateDeviceId -> parseMunicipalStateDevice(reportId, municipalStateDeviceId)).collect(Collectors.toList());
	}

	private MunicipalStateDevice parseMunicipalStateDevice(Integer reportId, Short municipalStateDeviceId) {
		MunicipalStateDevice municipalStateDevice = new MunicipalStateDevice();
		municipalStateDevice.setPk(new MunicipalStateDevicePK(reportId, municipalStateDeviceId));
		return municipalStateDevice;
	}

	private void saveOrganizationsWithinHealthInstitution(Integer reportId, CoordinationInsideHealthSectorBo coordinationInsideHealthSectorBo) {
		Boolean isWithinHealthInstitution = coordinationInsideHealthSectorBo.getHealthInstitutionOrganization().getWithin();
		if (isWithinHealthInstitution) {
			CoordinationActionBo coordinationAction = coordinationInsideHealthSectorBo.getHealthInstitutionOrganization();
			if (coordinationAction.getOrganizations() != null && !coordinationAction.getOrganizations().isEmpty())
				healthInstitutionOrganizationCoordinationRepository.saveAll(parseHealthInstitutionOrganizations(reportId, coordinationAction));
		}
	}

	private List<HealthInstitutionOrganizationCoordination> parseHealthInstitutionOrganizations(Integer reportId, CoordinationActionBo coordinationAction) {
		return coordinationAction.getOrganizations().stream().map(organizationId -> parseHealthInstitutionOrganization(reportId, organizationId, coordinationAction.getOther())).collect(Collectors.toList());
	}

	private HealthInstitutionOrganizationCoordination parseHealthInstitutionOrganization(Integer reportId, Short organizationId, String otherOrganization) {
		HealthInstitutionOrganizationCoordination healthInstitutionOrganizationCoordination = new HealthInstitutionOrganizationCoordination();
		healthInstitutionOrganizationCoordination.setPk(new HealthInstitutionOrganizationCoordinationPK(reportId, organizationId));
		if (organizationId.equals(EHealthInstitutionOrganization.OTHERS.getId()))
			healthInstitutionOrganizationCoordination.setOtherHealthInstitutionOrganization(otherOrganization);
		return healthInstitutionOrganizationCoordination;
	}

	private void saveOrganizationsWithinHealthSystem(Integer reportId, CoordinationInsideHealthSectorBo coordinationInsideHealthSectorBo) {
		Boolean isWithinHealthSystem = coordinationInsideHealthSectorBo.getHealthSystemOrganization().getWithin();
		if (isWithinHealthSystem) {
			CoordinationActionBo coordinationAction = coordinationInsideHealthSectorBo.getHealthSystemOrganization();
			if (coordinationAction.getOrganizations() != null && !coordinationAction.getOrganizations().isEmpty())
				healthSystemOrganizationCoordinationRepository.saveAll(parseHealthSystemOrganizations(reportId, coordinationAction));
		}
	}

	private List<HealthSystemOrganizationCoordination> parseHealthSystemOrganizations(Integer reportId, CoordinationActionBo coordinationAction) {
		return coordinationAction.getOrganizations().stream().map(organizationId -> parseHealthSystemOrganization(reportId, organizationId, coordinationAction.getOther())).collect(Collectors.toList());
	}

	private HealthSystemOrganizationCoordination parseHealthSystemOrganization(Integer reportId, Short organizationId, String otherOrganization) {
		HealthSystemOrganizationCoordination healthSystemOrganizationCoordination = new HealthSystemOrganizationCoordination();
		healthSystemOrganizationCoordination.setPk(new HealthSystemOrganizationCoordinationPK(reportId, organizationId));
		if (organizationId.equals(EHealthSystemOrganization.OTHERS.getId()))
			healthSystemOrganizationCoordination.setOtherHealthSystemOrganization(otherOrganization);
		return healthSystemOrganizationCoordination;
	}

	private void saveViolenceReportAggressor(ViolenceReportBo violenceReport, Integer reportId) {
		List<ViolenceReportAggressorBo> aggressors = violenceReport.getAggressors();
		if (aggressors != null && !aggressors.isEmpty())
			violenceReportAggressorRepository.saveAll(parseViolenceAggressors(reportId, aggressors));
	}

	private List<ViolenceReportAggressor> parseViolenceAggressors(Integer reportId, List<ViolenceReportAggressorBo> aggressors) {
		return aggressors.stream().map(aggressor -> parseViolenceAggressor(reportId, aggressor)).collect(Collectors.toList());
	}

	private ViolenceReportAggressor parseViolenceAggressor(Integer reportId, ViolenceReportAggressorBo aggressor) {
		ViolenceReportAggressor reportAggressor = new ViolenceReportAggressor();
		reportAggressor.setReportId(reportId);
		reportAggressor.setFirstName(aggressor.getAggressorData().getFirstName());
		reportAggressor.setLastName(aggressor.getAggressorData().getLastName());
		reportAggressor.setAge(aggressor.getAggressorData().getAge());
		reportAggressor.setAddress(aggressor.getAggressorData().getAddress());
		reportAggressor.setMunicipalityId(aggressor.getAggressorData().getMunicipalityId());
		reportAggressor.setRelationshipWithVictimId(aggressor.getAggressorData().getRelationshipWithVictimId());
		reportAggressor.setOtherRelationShipWithVictim(aggressor.getAggressorData().getOtherRelationshipWithVictim());
		reportAggressor.setHasGuns(aggressor.getHasGuns());
		reportAggressor.setHasBeenTreated(aggressor.getHasBeenTreated());
		reportAggressor.setBelongsToSecurityForces(aggressor.getBelongsToSecurityForces());
		if (reportAggressor.getBelongsToSecurityForces() != null && reportAggressor.getBelongsToSecurityForces()) {
			reportAggressor.setInDuty(aggressor.getInDuty());
			if (reportAggressor.getInDuty())
				reportAggressor.setSecurityForceTypeId(aggressor.getSecurityForceTypeId());
		}
		reportAggressor.setLiveTogetherStatusId(aggressor.getLivesWithVictimId());
		reportAggressor.setRelationshipLengthId(aggressor.getRelationshipLengthId());
		reportAggressor.setViolenceFrequencyId(aggressor.getViolenceViolenceFrequencyId());
		reportAggressor.setCriminalRecordStatusId(aggressor.getHasPreviousEpisodesId());
		return reportAggressor;
	}

	private void saveViolenceModality(ViolenceReportBo violenceReport, Integer reportId) {
		List<Integer> violenceModalitySnomedIds = violenceReport.getEpisodeData().getViolenceModalitySnomedList().stream().map(this::getSnomedIdByConceptIdAndDescription).collect(Collectors.toList());
		if (!violenceModalitySnomedIds.isEmpty())
			violenceModalityRepository.saveAll(parseViolenceModalities(reportId, violenceModalitySnomedIds));
	}

	private List<ViolenceModality> parseViolenceModalities(Integer reportId, List<Integer> violenceModalitySnomedIds) {
		return violenceModalitySnomedIds.stream().map(snomedId -> parseViolenceModality(reportId, snomedId)).collect(Collectors.toList());
	}

	private ViolenceModality parseViolenceModality(Integer reportId, Integer snomedId) {
		ViolenceModality violenceModality = new ViolenceModality();
		violenceModality.setPk(new ViolenceReportSnomedPK(reportId, snomedId));
		return violenceModality;
	}

	private void saveViolenceType(ViolenceReportBo violenceReport, Integer reportId) {
		List<Integer> violenceTypeSnomedIds = violenceReport.getEpisodeData().getViolenceTypeSnomedList().stream().map(this::getSnomedIdByConceptIdAndDescription).collect(Collectors.toList());
		if (!violenceTypeSnomedIds.isEmpty())
			violenceTypeRepository.saveAll(parseViolenceTypes(reportId, violenceTypeSnomedIds));
	}

	private Integer getSnomedIdByConceptIdAndDescription(SnomedBo snomed) {
		return snomedService.getSnomedIdBySctidAndDescription(snomed.getSctid(), snomed.getPt());
	}

	private List<ViolenceType> parseViolenceTypes(Integer reportId, List<Integer> violenceTypeSnomedIds) {
		return violenceTypeSnomedIds.stream().map(snomedId -> parseViolenceType(reportId, snomedId)).collect(Collectors.toList());
	}

	private ViolenceType parseViolenceType(Integer reportId, Integer snomedId) {
		ViolenceType violenceType = new ViolenceType();
		violenceType.setPk(new ViolenceReportSnomedPK(reportId, snomedId));
		return violenceType;
	}

	private void saveKeeperData(ViolenceReportBo violenceReport, Integer result) {
		Boolean lackOfLegalCapacity = violenceReport.getVictimData().getLackOfLegalCapacity();
		if (lackOfLegalCapacity)
			saveKeeper(violenceReport, result);
	}

	private void saveKeeper(ViolenceReportBo violenceReport, Integer result) {
		ViolenceReportActorBo keeper = violenceReport.getVictimData().getKeeperData();
		violenceReportKeeperRepository.save(parseViolenceReportKeeper(keeper, result));
	}

	private ViolenceReportKeeper parseViolenceReportKeeper(ViolenceReportActorBo keeperData, Integer reportId) {
		ViolenceReportKeeper keeper = new ViolenceReportKeeper();
		keeper.setReportId(reportId);
		keeper.setFirstName(keeperData.getFirstName());
		keeper.setLastName(keeperData.getLastName());
		keeper.setAge(keeperData.getAge());
		keeper.setAddress(keeperData.getAddress());
		keeper.setMunicipalityId(keeperData.getMunicipalityId());
		keeper.setRelationshipWithVictimId(keeperData.getRelationshipWithVictimId());
		keeper.setOtherRelationshipWithVictim(keeperData.getOtherRelationshipWithVictim());
		return keeper;
	}

	private ViolenceReport parseViolenceReport(ViolenceReportBo violenceReportBo) {
		ViolenceReport violenceReport = new ViolenceReport();
		violenceReport.setInstitutionId(violenceReportBo.getInstitutionId());
		violenceReport.setPatientId(violenceReportBo.getPatientId());
		violenceReport.setSituationId(violenceReportBo.getSituationId());
		violenceReport.setEvolutionId(violenceReportBo.getEvolutionId());
		violenceReport.setCanReadAndWrite(violenceReportBo.getVictimData().getCanReadAndWrite());
		violenceReport.setHasIncome(violenceReportBo.getVictimData().getHasIncome());
		if (violenceReport.getHasIncome() != null && violenceReport.getHasIncome())
			violenceReport.setWorksAtFormalSector(violenceReportBo.getVictimData().getWorksAtFormalSector());
		violenceReport.setHasSocialPlan(violenceReportBo.getVictimData().getHasSocialPlan());
		violenceReport.setHasDisability(violenceReportBo.getVictimData().getHasDisability());
		if (violenceReport.getHasDisability() != null && violenceReport.getHasDisability())
			violenceReport.setDisabilityCertificateStatusId(violenceReportBo.getVictimData().getDisabilityCertificateStatusId());
		violenceReport.setIsInstitutionalized(violenceReportBo.getVictimData().getIsInstitutionalized());
		if (violenceReport.getIsInstitutionalized() != null && violenceReport.getIsInstitutionalized())
			violenceReport.setInstitutionalizedDetails(violenceReportBo.getVictimData().getInstitutionalizedDetails());
		violenceReport.setLackOfLegalCapacity(violenceReportBo.getVictimData().getLackOfLegalCapacity());
		violenceReport.setEpisodeDate(violenceReportBo.getEpisodeData().getEpisodeDate());
		violenceReport.setViolenceTowardsUnderageTypeId(violenceReportBo.getEpisodeData().getViolenceTowardsUnderageTypeId());
		violenceReport.setSchooled(violenceReportBo.getEpisodeData().getSchooled());
		if (violenceReport.getSchooled() != null && violenceReport.getSchooled())
			violenceReport.setSchoolLevelId(violenceReportBo.getEpisodeData().getSchoolLevelId());
		violenceReport.setRiskLevelId(violenceReportBo.getEpisodeData().getRiskLevelId());
		violenceReport.setCoordinationInsideHealthSector(violenceReportBo.getImplementedActions().getCoordinationInsideHealthSector() != null);
		if (violenceReportBo.getImplementedActions().getCoordinationInsideHealthSector() != null) {
			violenceReport.setCoordinationWithinHealthSystem(violenceReportBo.getImplementedActions().getCoordinationInsideHealthSector().getHealthSystemOrganization().getWithin());
			violenceReport.setCoordinationWithinHealthInstitution(violenceReportBo.getImplementedActions().getCoordinationInsideHealthSector().getHealthInstitutionOrganization().getWithin());
			violenceReport.setInternmentIndicatedStatusId(violenceReportBo.getImplementedActions().getCoordinationInsideHealthSector().getWereInternmentIndicatedId());
		}
		else
			violenceReport.setCoordinationWithOtherSocialOrganizations(violenceReportBo.getImplementedActions().getCoordinationOutsideHealthSector().getWithOtherSocialOrganizations());
		violenceReport.setWerePreviousEpisodeWithVictimOrKeeper(violenceReportBo.getImplementedActions().getWerePreviousEpisodesWithVictimOrKeeper());
		violenceReport.setInstitutionReported(violenceReportBo.getImplementedActions().getReportWasDoneByInstitution());
		violenceReport.setWasSexualViolence(violenceReportBo.getImplementedActions().getWasSexualViolence());
		violenceReport.setObservations(violenceReportBo.getObservation());
		return violenceReport;
	}

}
