import { Component, OnDestroy } from '@angular/core';
import { EAggressorRelationship, ECriminalRecordStatus, ELiveTogetherStatus, ERelationshipLength, ESecurityForceType, EViolenceFrequency, ViolenceReportActorDto, ViolenceReportAggressorDto, ViolenceReportDto } from '@api-rest/api-model';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';
import { AggressorRelationship, CriminalRecordStatus, FormOption, InstitutionOptions, LiveTogetherStatus, RelationshipLengths, ViolenceFrequencys } from '@historia-clinica/modules/ambulatoria/constants/violence-masterdata';
import { CustomViolenceReportAggressorDto, ViolenceAggressorsNewConsultationService } from '@historia-clinica/modules/ambulatoria/services/violence-aggressors-new-consultation.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-violent-person-list',
	templateUrl: './violent-person-list.component.html',
	styleUrls: ['./violent-person-list.component.scss']
})
export class ViolentPersonListComponent implements OnDestroy {
	aggressorsList: CustomViolenceReportAggressorDto[];
	violenceSituationSub: Subscription;
	constructor(private readonly violenceAggressorsNewConsultationService: ViolenceAggressorsNewConsultationService,
				private readonly violenceSituationFacadeService: ViolenceReportFacadeService) {
		this.setAggressors();
		this.setViolenceSituation();
	}

	ngOnDestroy(): void {
		this.violenceSituationSub.unsubscribe();
	}

	removeAggressor(index: number) {
		this.violenceAggressorsNewConsultationService.removeAggressor(index);
	}

	setAggressors() {
		this.violenceAggressorsNewConsultationService.violenceAggressors$.subscribe((concepts: CustomViolenceReportAggressorDto[]) => this.aggressorsList = concepts);
	}

	getDescriptionBasicOptions(value: boolean): string {
		if (value === null || value === undefined)
			return FormOption.WITHOUT_DATA;
		if (value)
			return FormOption.YES;
		if (!value)
			return FormOption.NO;
	}

	getDescriptionSecurityForceType(value: ESecurityForceType): string {
		return InstitutionOptions.find(type => type.value === value).text;
	}

	getDescriptionAggressorRelationship(value: ViolenceReportActorDto<EAggressorRelationship>): string {
		const relationshipWithVictim: EAggressorRelationship = value.relationshipWithVictim;
		let result: string = AggressorRelationship.find(relation => relation.value === relationshipWithVictim).text;
		if (relationshipWithVictim === EAggressorRelationship.ACQUAINTANCE)
			result = `${result} - ${value.otherRelationshipWithVictim}`;
		return result;
	}

	getDescriptionLivesWithVictim(value: ELiveTogetherStatus): string {
		return LiveTogetherStatus.find(state => state.value === value).text;
	}

	getDescriptionRelationshipLength(value: ERelationshipLength): string {
		return RelationshipLengths.find(length => length.value === value).text;
	}

	getDescriptionViolenceFrequency(value: EViolenceFrequency): string {
		return ViolenceFrequencys.find(frequency => frequency.value === value).text;
	}

	getDescriptionHasPreviousEpisodes(value: ECriminalRecordStatus): string {
		return CriminalRecordStatus.find(record => record.value === value).text;
	}

	private mapAggressor(aggressorsList: ViolenceReportAggressorDto[]): CustomViolenceReportAggressorDto[] {
		const list = [];
		aggressorsList.forEach(
			agr => {
				list.push(
					{
						aggressorData: {
							actorPersonalData: {
								firstName: agr.aggressorData.actorPersonalData.firstName,
								lastName: agr.aggressorData.actorPersonalData.lastName,
								age: agr.aggressorData.actorPersonalData.age,
								address:{
									municipality: {
										id: agr.aggressorData.actorPersonalData.address.municipality.id,
										provinceId: agr.aggressorData.actorPersonalData.address.municipality.provinceId,
										description: agr.aggressorData.actorPersonalData.address.municipality.description
									},
									city:{
										id: agr.aggressorData.actorPersonalData.address.city.id,
										description: agr.aggressorData.actorPersonalData.address.city.description,
									},
									homeAddress: agr.aggressorData.actorPersonalData.address.homeAddress,
								}
						
							},
							relationshipWithVictim: agr.aggressorData.relationshipWithVictim,
							otherRelationshipWithVictim: agr.aggressorData.otherRelationshipWithVictim,
						  },
						hasBeenTreated: agr.hasBeenTreated,
						hasGuns: agr.hasGuns,
						hasPreviousEpisodes: agr.hasPreviousEpisodes,
						livesWithVictim: agr.livesWithVictim,
						relationshipLength: agr.relationshipLength,
						securityForceRelatedData: {
							belongsToSecurityForces: agr.securityForceRelatedData.belongsToSecurityForces,
							inDuty: agr.securityForceRelatedData.inDuty,
							securityForceTypes: agr.securityForceRelatedData.securityForceTypes,
						  },
						violenceViolenceFrequency: agr.violenceViolenceFrequency,
						descriptionMunicipality: agr.aggressorData.actorPersonalData.address.municipality.description ? agr.aggressorData.actorPersonalData.address.municipality.description : FormOption.WITHOUT_DATA,
						descriptionLocality: agr.aggressorData.actorPersonalData.address.city.description ? agr.aggressorData.actorPersonalData.address.city.description: FormOption.WITHOUT_DATA,
					}
				)
			}
		)
		return list;
	}

	private setViolenceSituation() {
		this.violenceSituationSub = this.violenceSituationFacadeService.violenceSituation$
			.subscribe((result: ViolenceReportDto) => {
				this.aggressorsList = this.mapAggressor(result.aggressorData);
				this.violenceAggressorsNewConsultationService.violenceAggressors = this.aggressorsList;
				this.violenceAggressorsNewConsultationService.violenceAggressors$.next(this.violenceAggressorsNewConsultationService.violenceAggressors);
			});
	}
}