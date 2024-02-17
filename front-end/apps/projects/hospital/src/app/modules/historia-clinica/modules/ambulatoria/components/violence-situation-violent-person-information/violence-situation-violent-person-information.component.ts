import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewViolentPersonInfomationComponent } from '../../dialogs/new-violent-person-infomation/new-violent-person-infomation.component';
import { CustomViolenceReportAggressorDto, ViolenceAggressorsNewConsultationService } from '../../services/violence-aggressors-new-consultation.service';
import { Observable } from 'rxjs';
import { ViolenceReportAggressorDto } from '@api-rest/api-model';

@Component({
	selector: 'app-violence-situation-violent-person-information',
	templateUrl: './violence-situation-violent-person-information.component.html',
	styleUrls: ['./violence-situation-violent-person-information.component.scss']
})
export class ViolenceSituationViolentPersonInformationComponent {
	aggressorsList: ViolenceReportAggressorDto[];
	@Input() confirmForm: Observable<boolean>;
	@Output() aggressorsListInfo = new EventEmitter<any>();
	constructor(private readonly dialog: MatDialog, 
				private readonly violenceAggressorsNewConsultationService: ViolenceAggressorsNewConsultationService) {
		this.setAggressors();
	}

	ngOnChanges(changes: SimpleChanges) {
		if(!changes.confirmForm.isFirstChange()){
			this.aggressorsListInfo.emit(this.aggressorsList);
		}
	}
	
	setAggressors() {
		this.violenceAggressorsNewConsultationService.violenceAggressors$.subscribe((concepts: CustomViolenceReportAggressorDto[]) => this.aggressorsList = this.mapAggressor(concepts));
	}

	openNewViolentPerson() {
		this.dialog.open(NewViolentPersonInfomationComponent, {
			autoFocus: false,
			disableClose: true,
			width: '50%',
		})
	}

	mapAggressor(concepts: CustomViolenceReportAggressorDto[]): ViolenceReportAggressorDto[] {
		const list = [];
		concepts.forEach(concept => {
			list.push({
				aggressorData: {
					actorPersonalData: {
					firstName: concept.aggressorData.actorPersonalData.firstName,
					lastName: concept.aggressorData.actorPersonalData.lastName,
					address: concept.aggressorData.actorPersonalData.address,
					age: concept.aggressorData.actorPersonalData.age,
					municipality: concept.aggressorData.actorPersonalData.municipality,
					},
					relationshipWithVictim: concept.aggressorData.relationshipWithVictim,
					otherRelationshipWithVictim: concept.aggressorData.otherRelationshipWithVictim,
				},
				hasBeenTreated: concept.hasBeenTreated,
				hasGuns: concept.hasGuns,
				hasPreviousEpisodes: concept.hasPreviousEpisodes,
				livesWithVictim: concept.livesWithVictim,
				relationshipLength: concept.relationshipLength,
				securityForceRelatedData: {
					belongsToSecurityForces: concept.securityForceRelatedData.belongsToSecurityForces,
					inDuty: concept.securityForceRelatedData.inDuty,
					securityForceTypes: concept.securityForceRelatedData.securityForceTypes,
				},
				violenceViolenceFrequency: concept.violenceViolenceFrequency,
			});
		});
		return list;
	}
}