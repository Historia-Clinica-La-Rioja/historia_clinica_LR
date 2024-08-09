package net.pladema.violencereport.application;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.violencereport.domain.CoordinationActionBo;
import net.pladema.violencereport.domain.ViolenceReportActorBo;
import net.pladema.violencereport.domain.ViolenceReportAggressorBo;
import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.domain.ViolenceReportImplementedActionsBo;
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
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportAggressorRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportKeeperRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;

@Slf4j
@AllArgsConstructor
@Service
public class GetEditViolenceReportById {

	private ViolenceReportRepository violenceReportRepository;

	private ViolenceReportKeeperRepository violenceReportKeeperRepository;

	private ViolenceReportAggressorRepository violenceReportAggressorRepository;

	private HealthSystemOrganizationCoordinationRepository healthSystemOrganizationCoordinationRepository;

	private HealthInstitutionOrganizationCoordinationRepository healthInstitutionOrganizationCoordinationRepository;

	private MunicipalStateDeviceRepository municipalStateDeviceRepository;

	private ProvincialStateDeviceRepository provincialStateDeviceRepository;

	private NationalStateDeviceRepository nationalStateDeviceRepository;

	private VictimKeeperReportPlaceRepository victimKeeperReportPlaceRepository;

	private InstitutionReportReasonRepository institutionReportReasonRepository;

	private InstitutionReportPlaceRepository institutionReportPlaceRepository;

	private SexualViolenceActionRepository sexualViolenceActionRepository;

	public ViolenceReportBo run(Integer reportId) {
		log.debug("Input parameters -> reportId {}", reportId);
		ViolenceReportBo result = getViolenceReportBo(reportId);
		log.debug("Output -> {}", result);
		return result;
	}

	private ViolenceReportBo getViolenceReportBo(Integer reportId) {
		ViolenceReportBo result = violenceReportRepository.getViolenceReportDataWithoutEpisodeById(reportId);
		setKeeper(reportId, result);
		setAggressors(reportId, result);
		completeImplementedActions(reportId, result);
		return result;
	}

	private void completeImplementedActions(Integer reportId, ViolenceReportBo violenceReport) {
		ViolenceReportImplementedActionsBo implementedActions = violenceReport.getImplementedActions();
		if (implementedActions.getCoordinationInsideHealthSector() != null)
			getCoordinationInsideHealthSector(reportId, implementedActions);
		else
			getCoordinationOutsideHealthSector(reportId, implementedActions);
		if (implementedActions.getWerePreviousEpisodesWithVictimOrKeeper())
			implementedActions.setReportPlaceIds(getReportPlaceIds(reportId));
		if (implementedActions.getReportWasDoneByInstitution())
			getInstitutionReportPlaceAndReason(reportId, implementedActions);
		if (implementedActions.getWasSexualViolence())
			getSexualViolenceImplementedActions(reportId, implementedActions);
	}

	private void getInstitutionReportPlaceAndReason(Integer reportId, ViolenceReportImplementedActionsBo implementedActions) {
		implementedActions.setReportReasonIds(getReasonIdsByReportId(reportId));
		implementedActions.setInstitutionReportPlaceIds(getPlaceIdsByReportId(reportId));
		if (implementedActions.getInstitutionReportPlaceIds().contains(EInstitutionReportPlace.OTHER.getId()))
			implementedActions.setOtherInstitutionReportPlace(getOtherPlaceByReportId(reportId));
	}

	private String getOtherPlaceByReportId(Integer reportId) {
		return institutionReportPlaceRepository.getOtherPlaceByReportId(reportId);
	}

	private List<Short> getPlaceIdsByReportId(Integer reportId) {
		return institutionReportPlaceRepository.getIdsByReportId(reportId);
	}

	private List<Short> getReasonIdsByReportId(Integer reportId) {
		return institutionReportReasonRepository.getByReportId(reportId);
	}

	private List<Short> getReportPlaceIds(Integer reportId) {
		return victimKeeperReportPlaceRepository.getReportPlaceIds(reportId);
	}

	private void getCoordinationOutsideHealthSector(Integer reportId, ViolenceReportImplementedActionsBo implementedActions) {
		implementedActions.getCoordinationOutsideHealthSector().setMunicipalGovernmentDeviceIds(getMunicipalStateDeviceIds(reportId));
		implementedActions.getCoordinationOutsideHealthSector().setProvincialGovernmentDeviceIds(getProvincialStateDeviceIds(reportId));
		implementedActions.getCoordinationOutsideHealthSector().setNationalGovernmentDeviceIds(getNationalStateDeviceIds(reportId));
	}

	private List<Short> getNationalStateDeviceIds(Integer reportId) {
		return nationalStateDeviceRepository.getDeviceIdsByReportId(reportId);
	}

	private List<Short> getProvincialStateDeviceIds(Integer reportId) {
		return provincialStateDeviceRepository.getDeviceIdsByReportId(reportId);
	}

	private List<Short> getMunicipalStateDeviceIds(Integer reportId) {
		return municipalStateDeviceRepository.getDeviceIdsByReportId(reportId);
	}

	private void getCoordinationInsideHealthSector(Integer reportId, ViolenceReportImplementedActionsBo implementedActions) {
		Boolean withinHealthSystemOrganization = implementedActions.getCoordinationInsideHealthSector().getHealthSystemOrganization().getWithin();
		if (withinHealthSystemOrganization)
			 getHealthSystemOrganizations(reportId, implementedActions);
		Boolean withinHealthInstitutionOrganization = implementedActions.getCoordinationInsideHealthSector().getHealthInstitutionOrganization().getWithin();
		if (withinHealthInstitutionOrganization)
			getHealthInstitutionOrganizations(reportId, implementedActions);
	}

	private void getHealthInstitutionOrganizations(Integer reportId, ViolenceReportImplementedActionsBo implementedActions) {
		CoordinationActionBo healthInstitutionOrganization = implementedActions.getCoordinationInsideHealthSector().getHealthInstitutionOrganization();
		List<Short> organizations = healthInstitutionOrganizationCoordinationRepository.getOrganizationsByReportId(reportId);
		healthInstitutionOrganization.setOrganizations(organizations);
		boolean hasOtherOrganization = healthInstitutionOrganization.getOrganizations().contains(EHealthInstitutionOrganization.OTHERS.getId());
		if (hasOtherOrganization)
			setOtherHealthInstitutionOrganization(reportId, healthInstitutionOrganization);
	}

	private void setOtherHealthInstitutionOrganization(Integer reportId, CoordinationActionBo healthInstitutionOrganization) {
		String otherOrganization = healthInstitutionOrganizationCoordinationRepository.getOtherOrganizationByReportId(reportId);
		healthInstitutionOrganization.setOther(otherOrganization);
	}

	private void getHealthSystemOrganizations(Integer reportId, ViolenceReportImplementedActionsBo implementedActions) {
		CoordinationActionBo healthSystemOrganization = implementedActions.getCoordinationInsideHealthSector().getHealthSystemOrganization();
		List<Short> organizations = healthSystemOrganizationCoordinationRepository.getOrganizationsByReportId(reportId);
		healthSystemOrganization.setOrganizations(organizations);
		boolean hasOtherOrganization = healthSystemOrganization.getOrganizations().contains(EHealthSystemOrganization.OTHERS.getId());
		if (hasOtherOrganization)
			setOtherHealthSystemOrganization(reportId, healthSystemOrganization);
	}

	private void setOtherHealthSystemOrganization(Integer reportId, CoordinationActionBo healthSystemOrganization) {
		String otherOrganization = healthSystemOrganizationCoordinationRepository.getOtherOrganizationByReportId(reportId);
		healthSystemOrganization.setOther(otherOrganization);
	}

	private void getSexualViolenceImplementedActions(Integer reportId, ViolenceReportImplementedActionsBo implementedActions) {
		List<Short> sexualViolenceImplementedActions = sexualViolenceActionRepository.getByReportId(reportId);
		implementedActions.setImplementedActionIds(sexualViolenceImplementedActions);
	}

	private void setAggressors(Integer reportId, ViolenceReportBo violenceReport) {
		List<ViolenceReportAggressorBo> aggressors = violenceReportAggressorRepository.getAllByReportId(reportId);
		violenceReport.setAggressors(aggressors);
	}

	private void setKeeper(Integer reportId, ViolenceReportBo violenceReport) {
		Boolean lackOfLegalCapacity = violenceReport.getVictimData().getLackOfLegalCapacity();
		if (lackOfLegalCapacity)
			getKeeper(reportId, violenceReport);
	}

	private void getKeeper(Integer reportId, ViolenceReportBo violenceReport) {
		final Short NO_PROVINCE_ID = 99;
		ViolenceReportActorBo keeper = violenceReportKeeperRepository.getKeeperByReportId(reportId);
		if (keeper != null && keeper.getAddress().getMunicipalityId() == null)
			keeper.getAddress().setProvinceId(NO_PROVINCE_ID);
		violenceReport.getVictimData().setKeeperData(keeper);
	}

}
