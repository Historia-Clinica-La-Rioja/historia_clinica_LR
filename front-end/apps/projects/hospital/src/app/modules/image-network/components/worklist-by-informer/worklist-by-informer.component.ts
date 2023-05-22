import { Component, OnInit } from '@angular/core';
import { State, Worklist } from '../worklist/worklist.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { IdentificationTypeDto, MasterDataDto, ModalityDto, WorklistDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ModalityService } from '@api-rest/services/modality.service';
import { Observable } from 'rxjs';
import { WorklistService } from '@api-rest/services/worklist.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { MatSelectChange } from '@angular/material/select';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';

const DERIVED = 2;
const PENDING = 3;
@Component({
	selector: 'app-worklist-by-informer',
	templateUrl: './worklist-by-informer.component.html',
	styleUrls: ['./worklist-by-informer.component.scss']
})
export class WorklistByInformerComponent implements OnInit {

	worklists: Worklist[] = [];
	modalityId: number;
	nameSelfDeterminationFF = false;
	modalities$: Observable<ModalityDto[]>;
	identificationTypes: IdentificationTypeDto[] = [];
	worklistStatus: MasterDataDto[] = [];

	constructor(
		private readonly featureFlagService: FeatureFlagService,
		private readonly modalityService: ModalityService,
		private readonly worklistService: WorklistService,
		private readonly personMasterData: PersonMasterDataService,
	) { }

	ngOnInit(): void {
		this.personMasterData.getIdentificationTypes().subscribe(types => this.identificationTypes = types);
		this.modalities$ = this.modalityService.getModalitiesByStudiesCompleted();
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
		this.worklistService.getWorklistStatus().subscribe(status => this.worklistStatus = status);
	}

	setWorklist(modalitySelected: MatSelectChange) {
		this.modalityId = modalitySelected.value;
		this.worklistService.getByModalityAndInstitution(this.modalityId).subscribe((worklist: WorklistDto[]) => {
			this.worklists = this.mapToWorklist(worklist);
		});
	}

	private mapToWorklist(worklist: WorklistDto[]): Worklist[] {
		return worklist.map(w => {
			return {
				patientInformation: {
					fullName: w.patientFullName,
					identification: `${this.getIdentificationType(w.patientIdentificationTypeId)} ${w.patientIdentificationNumber} ID - ${w.patientId}`,
				},
				state: mapToState(w.statusId),
				date: dateTimeDtotoLocalDate(w.actionTime),
				appointmentId: w.appointmentId
			}
		})
	}

	private getIdentificationType(id: number): string {
		return this.identificationTypes.find(identificationType => identificationType.id === id).description
	}
}

export function mapToState(statusId: number): State {
	if (statusId === PENDING) {
		return { description: 'image-network.worklist.status.PENDING', color: Color.YELLOW }
	}

	if (statusId === DERIVED) {
		return { description: 'image-network.worklist.status.DERIVED', color: Color.GREY }
	}

	return { description: 'image-network.worklist.status.COMPLETED', color: Color.GREEN }
}
