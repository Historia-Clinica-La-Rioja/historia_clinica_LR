package net.pladema.violencereport.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportBo {

	private Integer id;

	private Integer patientId;

	private Short situationId;

	private Short evolutionId;

	private Integer institutionId;

	private ViolenceReportVictimBo victimData;

	private ViolenceEpisodeDetailBo episodeData;

	private List<ViolenceReportAggressorBo> aggressors;

	private ViolenceReportImplementedActionsBo implementedActions;

	private String observation;

	public ViolenceReportBo(Boolean canReadAndWrite, Boolean hasIncome, Boolean worksAtFormalSector, Boolean hasSocialPlan, Boolean hasDisability,
							Short disabilityCertificateStatusId, Boolean isInstitutionalized, String institutionalizedDetails, Boolean lackOfLegalCapacity,
							Boolean coordinationInsideHealthSector, Boolean coordinationWithinHealthSystem, Boolean coordinationWithinHealthInstitution,
							Short internmentIndicatedStatusId, Boolean coordinationWithOtherSocialOrganizations, Boolean werePreviousEpisodeWithVictimOrKeeper,
							Boolean institutionReported, Boolean wasSexualViolence, String observations) {
		victimData = initializeVictimData(canReadAndWrite, hasIncome, worksAtFormalSector, hasSocialPlan, hasDisability, disabilityCertificateStatusId, isInstitutionalized, institutionalizedDetails, lackOfLegalCapacity);
		implementedActions = initializeImplementedActions(coordinationInsideHealthSector, coordinationWithinHealthSystem, coordinationWithinHealthInstitution, internmentIndicatedStatusId, coordinationWithOtherSocialOrganizations, werePreviousEpisodeWithVictimOrKeeper, institutionReported, wasSexualViolence);
		this.observation = observations;
	}

	private ViolenceReportImplementedActionsBo initializeImplementedActions(Boolean coordinationInsideHealthSector, Boolean coordinationWithinHealthSystem,
																			Boolean coordinationWithinHealthInstitution, Short internmentIndicatedStatusId,
																			Boolean coordinationWithOtherSocialOrganizations, Boolean werePreviousEpisodeWithVictimOrKeeper,
																			Boolean institutionReported, Boolean wasSexualViolence) {
		ViolenceReportImplementedActionsBo result = new ViolenceReportImplementedActionsBo();
		if (coordinationInsideHealthSector) {
			CoordinationInsideHealthSectorBo coordinationInsideHealthSectorBo = initializeCoordinationInsideHealthSectorBo(coordinationWithinHealthSystem, coordinationWithinHealthInstitution, internmentIndicatedStatusId);
			result.setCoordinationInsideHealthSector(coordinationInsideHealthSectorBo);
		}
		if (coordinationWithOtherSocialOrganizations != null) {
			CoordinationOutsideHealthSectorBo coordinationOutsideHealthSectorBo = initializeCoordinationOutsideHealthSector(coordinationWithOtherSocialOrganizations);
			result.setCoordinationOutsideHealthSector(coordinationOutsideHealthSectorBo);
		}
		result.setWerePreviousEpisodesWithVictimOrKeeper(werePreviousEpisodeWithVictimOrKeeper);
		result.setReportWasDoneByInstitution(institutionReported);
		result.setWasSexualViolence(wasSexualViolence);
		return result;
	}

	private CoordinationOutsideHealthSectorBo initializeCoordinationOutsideHealthSector(Boolean coordinationWithOtherSocialOrganizations) {
		CoordinationOutsideHealthSectorBo coordinationOutsideHealthSectorBo = new CoordinationOutsideHealthSectorBo();
		coordinationOutsideHealthSectorBo.setWithOtherSocialOrganizations(coordinationWithOtherSocialOrganizations);
		return coordinationOutsideHealthSectorBo;
	}

	private CoordinationInsideHealthSectorBo initializeCoordinationInsideHealthSectorBo(Boolean coordinationWithinHealthSystem, Boolean coordinationWithinHealthInstitution,
																						Short internmentIndicatedStatusId) {
		CoordinationActionBo healthSystemOrganization = new CoordinationActionBo();
		healthSystemOrganization.setWithin(coordinationWithinHealthSystem);
		CoordinationActionBo healthInstitutionOrganization = new CoordinationActionBo();
		healthInstitutionOrganization.setWithin(coordinationWithinHealthInstitution);
		CoordinationInsideHealthSectorBo coordinationInsideHealthSectorBo = new CoordinationInsideHealthSectorBo();
		coordinationInsideHealthSectorBo.setHealthSystemOrganization(healthSystemOrganization);
		coordinationInsideHealthSectorBo.setHealthInstitutionOrganization(healthInstitutionOrganization);
		coordinationInsideHealthSectorBo.setWereInternmentIndicatedId(internmentIndicatedStatusId);
		return coordinationInsideHealthSectorBo;
	}

	private ViolenceReportVictimBo initializeVictimData(Boolean canReadAndWrite, Boolean hasIncome, Boolean worksAtFormalSector, Boolean hasSocialPlan,
														Boolean hasDisability, Short disabilityCertificateStatusId, Boolean isInstitutionalized,
														String institutionalizedDetails, Boolean lackOfLegalCapacity) {
		ViolenceReportVictimBo result = new ViolenceReportVictimBo();
		result.setCanReadAndWrite(canReadAndWrite);
		result.setHasIncome(hasIncome);
		result.setWorksAtFormalSector(worksAtFormalSector);
		result.setHasSocialPlan(hasSocialPlan);
		result.setHasDisability(hasDisability);
		result.setDisabilityCertificateStatusId(disabilityCertificateStatusId);
		result.setIsInstitutionalized(isInstitutionalized);
		result.setInstitutionalizedDetails(institutionalizedDetails);
		result.setLackOfLegalCapacity(lackOfLegalCapacity);
		return result;
	}

	public ViolenceReportBo(Integer id, Integer patientId) {
		this.id = id;
		this.patientId = patientId;
	}

	public ViolenceReportBo(Integer id, Short situationId, Integer patientId) {
		this.id = id;
		this.situationId = situationId;
		this.patientId = patientId;
	}
}
