import { Injectable } from '@angular/core';
import { CoordinationInsideHealthSectorDto, CoordinationOutsideHealthSectorDto, EAggressorRelationship, EHealthInstitutionOrganization, EHealthSystemOrganization, EInstitutionReportPlace, EViolenceTowardsUnderageType, InstitutionReportDto, PageDto, SexualViolenceDto, SnomedDto, VictimKeeperReportDto, ViolenceEpisodeDetailDto, ViolenceReportAggressorDto, ViolenceReportDto, ViolenceReportFilterOptionDto, ViolenceReportImplementedActionsDto, ViolenceReportSituationDto, ViolenceReportSituationEvolutionDto, ViolenceReportVictimDto } from '@api-rest/api-model';
import { BehaviorSubject, Subject } from 'rxjs';
import { ViolenceReportService } from './violence-report.service';
import { DetailedInformation } from '@presentation/components/detailed-information/detailed-information.component';
import { AggressorRelationship, Areas, BasicOption, BasicOptions, BasicTwoOptions, Complaints, CriminalRecordStatus, Devices, DisabilityCertificateStatus, EscolarizationLevels, Establishments, FormOption, ImplementedActions, InstitutionOptions, InternmentIndication, LiveTogetherStatus, MunicipalDevices, NationalDevices, Organizations, OrganizationsExtended, ProvincialDevices, RelationOption, RelationOptionDescription, RelationshipLengths, RiskLevels, Sectors, StateOptions, ValueOption, ViolenceFrequencys, ViolenceTypes } from '@historia-clinica/modules/ambulatoria/constants/violence-masterdata';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { dateToViewDate } from '@core/utils/date.utils';
import { Articulation } from '@historia-clinica/modules/ambulatoria/components/violence-situation-implemented-actions/violence-situation-implemented-actions.component';

@Injectable({
    providedIn: 'root'
})
export class ViolenceReportFacadeService {

    violenceSituations$ = new BehaviorSubject<PageDto<ViolenceReportSituationDto>>(null);
    violenceSituation$ = new Subject<ViolenceReportDto>();
    evolutions$ = new Subject<ViolenceReportSituationEvolutionDto[]>();
    detailedInformation$ = new Subject<DetailedInformation>();
    filters$ = new Subject<ViolenceReportFilterOptionDto>();
	isNewViolenceSituation$: Subject<any> = new BehaviorSubject<boolean>(false);

    constructor(private readonly violenceReportService: ViolenceReportService) {}

    setAllPatientViolenceSituations(patientId: number, mustBeLimited: boolean) {
        this.violenceSituations$.next(null);
        this.violenceReportService.getLimitedPatientViolenceSituations(patientId, mustBeLimited)
            .subscribe((result: PageDto<ViolenceReportSituationDto>) => this.violenceSituations$.next(result));
    }

    setViolenceSituation(situationId: number, patientId: number) {
        this.violenceReportService.getViolenceReport(situationId, patientId)
            .subscribe((result: ViolenceReportDto) => this.violenceSituation$.next(result));
    }

    setEvolutions = (patientId: number, filterData: string) => {
        this.evolutions$.next([]);
        this.detailedInformation$.next(null);
        this.violenceReportService.getEvolutions(patientId, filterData)
			.subscribe({
				next: (result: ViolenceReportSituationEvolutionDto[]) => this.evolutions$.next(result)
			});
    }

    getEvolutionData = (patientId: number, situationId: number, evolutionId: number) => {
        this.violenceReportService.getEvolutionData(patientId, situationId, evolutionId)
            .subscribe({
                next: (result: ViolenceReportDto) => this.detailedInformation$.next(this.mapToDetailedInformation(result, situationId, evolutionId)),
            })
    }

    setPatientFilters = (patientId: number) => {
        this.violenceReportService.getPatientFilters(patientId)
            .subscribe((result: ViolenceReportFilterOptionDto) => {
                this.filters$.next(result)
            });
    }

	setIsNewViolenceSituationOpen(isNewViolenceSituationOpen: boolean){
		this.isNewViolenceSituation$.next(isNewViolenceSituationOpen);
	}

	download(patientId: number, situationId: number, evolutionId: number) {
		this.violenceReportService.download(patientId, situationId, evolutionId);
	}

    private mapToDetailedInformation(result: ViolenceReportDto, situationId: number, evolutionId: number) {
		const detailed = {
			id: null,
			title: `Situación #${situationId} | ` + this.parseEvolutionText(evolutionId),
			oneColumn: [
				{
					icon: 'cancel',
					title: 'ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.TITLE',
					value: this.buildExpandedPersonInformation(result.victimData)
				},
				{
					icon: 'cancel',
					title: 'ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.new-violence-episode.TITLE',
					value: this.buildNewViolenceEpisode(result.episodeData)
				},
				{
					icon: 'cancel',
					title: 'ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.TITLE',
					value: this.buildViolentPersonInformation(result.aggressorData)
				},
				{
					icon: 'cancel',
					title: 'ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.TITLE',
					value: this.buildImplementedActions(result.implementedActions)
				},
				{
					icon: 'cancel',
					title: 'ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.relevant-information.TITLE',
					value: [result.observation ? result.observation : 'Sin información']
				}
			]
		}
		return detailed;
	}

	public parseEvolutionText(evolutionId: number): string {
		return evolutionId !== 0 ? `Evolución ${evolutionId}` : 'Inicio de abordaje';
	}

	private buildImplementedActions = (actions: ViolenceReportImplementedActionsDto): string[] => {
		const array: string[] = [];
		this.buildCoordinationInsideHealthHealthSector(array, actions.healthCoordination.coordinationInsideHealthSector);
		this.buildCoordinationOutsideHealthSector(array, actions.healthCoordination.coordinationOutsideHealthSector);
		this.buildPersonComplaint(array, actions.victimKeeperReport);
		this.buildInstitutionComplaint(array, actions.institutionReport);
		this.buildSexualComplaint(array, actions.sexualViolence)
		return array;
	}

	private buildSexualComplaint = (array: string[], sexualViolenceDto: SexualViolenceDto) => {
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_12');
		const violence: BasicOption = BasicTwoOptions.find(opt => opt.value === sexualViolenceDto.wasSexualViolence);
		array.push(violence.text);
		if (violence.value) {
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_13');
			const actions: ValueOption[] = ImplementedActions.filter(opt => sexualViolenceDto.implementedActions.find(org => org === opt.value));
			actions.forEach(ac => array.push(ac.text));
		}
	}

	private buildCoordinationOutsideHealthSector = (array: string[], coordinationOutsideHealthSector: CoordinationOutsideHealthSectorDto) => {
		if (coordinationOutsideHealthSector) {
			array.push(Articulation.OUT);
			array.push(FormOption.YES);
			this.buildMunicipalDevices(array, coordinationOutsideHealthSector);
			this.buildProvincialDevices(array, coordinationOutsideHealthSector);
			this.buildNationalDevices(array, coordinationOutsideHealthSector);
			if (coordinationOutsideHealthSector.withOtherSocialOrganizations)
				array.push(Devices.SOCIAL_ORGANIZATION);
		}
	}

	private buildInstitutionComplaint = (array: string[], institutionReportDto: InstitutionReportDto) => {
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_8');
		const iComplaint: BasicOption = BasicTwoOptions.find(opt => opt.value === institutionReportDto.reportWasDoneByInstitution);
		array.push(iComplaint.text);
		if (iComplaint.value) {
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_9');
			const orgs: ValueOption[] = Complaints.filter(opt => institutionReportDto.reportReasons.find(org => org === opt.value));
			orgs.forEach(org => array.push(org.text));
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_10');
			const authOrgs: ValueOption[] = OrganizationsExtended.filter(opt => institutionReportDto.institutionReportPlaces.find(org => org === opt.value));
			authOrgs.forEach(org => array.push(org.value === EInstitutionReportPlace.OTHER ? `${org.text}: ${institutionReportDto.otherInstitutionReportPlace}`: org.text));
		}
	}

	private buildPersonComplaint = (array: string[], victimKeeperReportDto: VictimKeeperReportDto) => {
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_6');
		const pComplaint: BasicOption = BasicTwoOptions.find(opt => opt.value === victimKeeperReportDto.werePreviousEpisodesWithVictimOrKeeper);
		array.push(pComplaint.text);
		if (pComplaint.value) {
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_7');
			const orgs: ValueOption[] = Organizations.filter(opt => victimKeeperReportDto.reportPlaces.find(org => org === opt.value));
			orgs.forEach(org => array.push(org.text));
		}
	}

	private buildNationalDevices = (array: string[], coordinationOutsideHealthSector: CoordinationOutsideHealthSectorDto) => {
		if (coordinationOutsideHealthSector.nationalGovernmentDevices.length) {
			array.push(`${Devices.NATIONAL_DEVICES}:`);
			coordinationOutsideHealthSector.nationalGovernmentDevices.forEach(device => {
				const nDevice: ValueOption = NationalDevices.find(opt => opt.value === device);
				array.push(nDevice.text);
			});
		}
	}

	private buildProvincialDevices = (array: string[], coordinationOutsideHealthSector: CoordinationOutsideHealthSectorDto) => {
		if (coordinationOutsideHealthSector.provincialGovernmentDevices.length) {
			array.push(`${Devices.PROVINCIAL_DEVICES}:`);
			coordinationOutsideHealthSector.provincialGovernmentDevices.forEach(device => {
				const pDevice: ValueOption = ProvincialDevices.find(opt => opt.value === device);
				array.push(pDevice.text);
			});
		}
	}

	private buildMunicipalDevices = (array: string[], coordinationOutsideHealthSector: CoordinationOutsideHealthSectorDto) => {
		if (coordinationOutsideHealthSector.municipalGovernmentDevices.length) {
			array.push(`${Devices.MUNICIPAL_DEVICES}:`);
			coordinationOutsideHealthSector.municipalGovernmentDevices.forEach(device => {
				const mDevice: ValueOption = MunicipalDevices.find(opt => opt.value === device);
				array.push(mDevice.text);
			});
		}
	}

	private buildCoordinationInsideHealthHealthSector = (array: string[], coordinationInsideHealthSector: CoordinationInsideHealthSectorDto) => {
		if (coordinationInsideHealthSector) {
			array.push(Articulation.IN);
			array.push(FormOption.YES);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.SUBTITLE_1');
			const artIns: BasicOption = BasicTwoOptions.find(opt => opt.value === coordinationInsideHealthSector.healthSystemOrganization.within)
			array.push(artIns.text);
			if (artIns.value) {
				array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_1');
				const areas: ValueOption[] = Areas.filter(opt => coordinationInsideHealthSector.healthSystemOrganization.organizations.find(org => org === opt.value));
				areas.map(area => array.push(area.value === EHealthSystemOrganization.OTHERS ? `${area.text}: ${coordinationInsideHealthSector.healthSystemOrganization.other}`: area.text));
			}
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.SUBTITLE_2');
			const artEst: BasicOption = BasicTwoOptions.find(opt => opt.value === coordinationInsideHealthSector.healthInstitutionOrganization.within);
			array.push(artEst.text);
			if (artEst.value) {
				array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_3');
				const establishments: ValueOption[] = Establishments.filter(opt => coordinationInsideHealthSector.healthInstitutionOrganization.organizations.find(org => org === opt.value));
				establishments.map(est => array.push(est.value === EHealthInstitutionOrganization.OTHERS ? `${est.text}: ${coordinationInsideHealthSector.healthInstitutionOrganization.other}`: est.text));
			}
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.implemented-actions.questions.QUESTION_5');
			const indicated = InternmentIndication.find(opt => opt.value === coordinationInsideHealthSector.wereInternmentIndicated);
			array.push(indicated.text);
		}
	}

	private buildViolentPersonInformation = (aggressorData: ViolenceReportAggressorDto[]): string[] => {
		const array: string[] = [];
		aggressorData.forEach((aggressor: ViolenceReportAggressorDto) => {
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.LASTNAME');
			array.push(aggressor.aggressorData.actorPersonalData.lastName);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.NAME');
			array.push(aggressor.aggressorData.actorPersonalData.firstName);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.AGE');
			array.push(aggressor.aggressorData.actorPersonalData.age.toString());
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.ADDRESS');
			array.push(aggressor.aggressorData.actorPersonalData.address);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.MUNICIPALTY');
			array.push(aggressor.aggressorData.actorPersonalData.municipality.description ? aggressor.aggressorData.actorPersonalData.municipality.description : 'Sin información');
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.QUESTION_1');
			const guns: BasicOption = BasicOptions.find(opt => opt.value === aggressor.hasGuns);
			array.push(guns.text);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.QUESTION_2');
			const technique: BasicOption = BasicOptions.find(opt => opt.value === aggressor.hasBeenTreated);
			array.push(technique.text);
			this.buildBelongsToSecurityForces(array, aggressor);
			this.buildRelationWithAggressor(array, aggressor);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.QUESTION_7');
			const lives: ValueOption = LiveTogetherStatus.find(opt => opt.value === aggressor.livesWithVictim);
			array.push(lives.text);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.QUESTION_8');
			const relationLength: ValueOption = RelationshipLengths.find(opt => opt.value === aggressor.relationshipLength);
			array.push(relationLength.text);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.QUESTION_9');
			const frequency: ValueOption = ViolenceFrequencys.find(opt => opt.value === aggressor.violenceViolenceFrequency);
			array.push(frequency.text);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.QUESTION_10');
			const previous: ValueOption = CriminalRecordStatus.find(opt => opt.value === aggressor.hasPreviousEpisodes);
			array.push(previous.text);
		});
		return array;
	}

	private buildRelationWithAggressor = (array: string[], aggressor: ViolenceReportAggressorDto) => {
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.QUESTION_5');
		const relation: ValueOption = AggressorRelationship.find(opt => opt.value === aggressor.aggressorData.relationshipWithVictim);
		array.push(relation.value === EAggressorRelationship.ACQUAINTANCE ? `${relation.text}: ${aggressor.aggressorData.otherRelationshipWithVictim}`: relation.text);
	}

	private buildBelongsToSecurityForces = (array: string[], aggressor: ViolenceReportAggressorDto) => {
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.QUESTION_3');
		const belongs: BasicOption = BasicOptions.find(opt => opt.value === aggressor.securityForceRelatedData.belongsToSecurityForces);
		array.push(belongs.text);
		if (belongs.value) {
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.STATE');
			const state: BasicOption = StateOptions.find(opt => opt.value === aggressor.securityForceRelatedData.inDuty);
			array.push(state.text);
			if (state.value) {
				array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.violent-person-information.questions.QUESTION_4');
				const institution: ValueOption = InstitutionOptions.find(i => i.value === aggressor.securityForceRelatedData.securityForceTypes);
				array.push(institution.text);
			}
		}
	}

	private buildExpandedPersonInformation = (victimData: ViolenceReportVictimDto): string[] => {
		const array: string[] = [];
		const canReadAndWrite: BasicOption = BasicOptions.find(opt => opt.value === victimData.canReadAndWrite);
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.questions.QUESTION_1');
		array.push(canReadAndWrite.text);
		this.buildIncome(array, victimData);
		const socialPlan: BasicOption = BasicOptions.find(opt => opt.value === victimData.hasSocialPlan);
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.questions.QUESTION_3');
		array.push(socialPlan.text);
		this.buildDisabilityData(array, victimData);
		this.buildInstitutionalizedData(array, victimData);
		this.buildLackOfLegalCapacity(array, victimData);
		return array;
	}

	private buildIncome = (array: string[], victimData: ViolenceReportVictimDto) => {
		const income: BasicOption = BasicOptions.find(opt => opt.value === victimData.incomeData.hasIncome);
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.questions.QUESTION_2');
		array.push(income.text);
		if (income.value) {
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.questions.QUESTION_9');
			const sector: BasicOption = Sectors.find(opt => opt.value === victimData.incomeData.worksAtFormalSector);
			array.push(sector.text);
		}
	}

	private buildNewViolenceEpisode = (episode: ViolenceEpisodeDetailDto): string[] => {
		const array: string[] = [];
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.new-violence-episode.questions.QUESTION_1');
		array.push(dateToViewDate(dateDtoToDate(episode.episodeDate)));
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.new-violence-episode.questions.QUESTION_2');
		episode.violenceTypeSnomedList.forEach((type: SnomedDto) => array.push(type.pt));
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.new-violence-episode.questions.QUESTION_3');
		episode.violenceModalitySnomedList.forEach((mod: SnomedDto) => array.push(mod.pt));
		this.buildViolenceTowardsUnderage(array, episode);
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.new-violence-episode.questions.QUESTION_7');
		array.push(RiskLevels.find(rl => rl.value === episode.riskLevel).text);
		return array;
	}

	private buildViolenceTowardsUnderage = (array: string[], episode: ViolenceEpisodeDetailDto) => {
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.new-violence-episode.questions.QUESTION_4');
		const violence: ValueOption = ViolenceTypes.find(vt => vt.value === episode.violenceTowardsUnderage.type);
		array.push(violence.text);
		if (violence.value === EViolenceTowardsUnderageType.DIRECT_VIOLENCE || violence.value === EViolenceTowardsUnderageType.INDIRECT_VIOLENCE) {
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.new-violence-episode.questions.QUESTION_5');
			const escolarized: BasicOption = BasicOptions.find(opt => opt.value === episode.violenceTowardsUnderage.schooled);
			array.push(escolarized.text);
			const schoolLevel: ValueOption = EscolarizationLevels.find(e => e.value === episode.violenceTowardsUnderage.schoolLevel);
			if (schoolLevel) {
				array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.new-violence-episode.questions.QUESTION_6');
				array.push(schoolLevel.text);
			}
		}
	}

	private buildDisabilityData = (array: string[], victimData: ViolenceReportVictimDto) => {
		const disability: BasicOption = BasicOptions.find(opt => opt.value === victimData.disabilityData.hasDisability);
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.questions.QUESTION_4');
		array.push(disability.text);
		if (disability.value) {
			const certificate: ValueOption = DisabilityCertificateStatus.find(opt => opt.value === victimData.disabilityData.disabilityCertificateStatus);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.questions.QUESTION_5');
			array.push(certificate.text);
		}
	}

	private buildInstitutionalizedData = (array: string[], victimData: ViolenceReportVictimDto) => {
		const institutionalized: BasicOption = BasicOptions.find(opt => opt.value === victimData.institutionalizedData.isInstitutionalized);
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.questions.QUESTION_6');
		array.push(institutionalized.text);
		if (institutionalized.value) {
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.questions.QUESTION_7');
			array.push(victimData.institutionalizedData.institutionalizedDetails);
		}
	}

	private buildLackOfLegalCapacity = (array: string[], victimData: ViolenceReportVictimDto) => {
		const lackOfLegalCapacity: BasicOption = BasicOptions.find(opt => opt.value === victimData.lackOfLegalCapacity);
		array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.questions.QUESTION_8');
		array.push(lackOfLegalCapacity.text);
		if (victimData.lackOfLegalCapacity) {
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.LASTNAME');
			array.push(victimData.keeperData.actorPersonalData.lastName);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.NAME');
			array.push(victimData.keeperData.actorPersonalData.firstName);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.AGE');
			array.push(victimData.keeperData.actorPersonalData.age.toString());
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.ADDRESS');
			array.push(victimData.keeperData.actorPersonalData.address);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.MUNICIPALTY');
			array.push(victimData.keeperData.actorPersonalData.municipality.description ? victimData.keeperData.actorPersonalData.municipality.description : 'Sin información');
			const relationship: ValueOption = RelationOption.find(opt => opt.value === victimData.keeperData.relationshipWithVictim);
			array.push('ambulatoria.paciente.violence-situations.violence-situation-history.detailed-information.expanded-person-information.RELATIONSHIP_WITH_VICTIM');
			array.push(relationship.text === RelationOptionDescription.OTHER ? `${relationship.text}: ${victimData.keeperData.otherRelationshipWithVictim}`: relationship.text);
		}
	}
}
