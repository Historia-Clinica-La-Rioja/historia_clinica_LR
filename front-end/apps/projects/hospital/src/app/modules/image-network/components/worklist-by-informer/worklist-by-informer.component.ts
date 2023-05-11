import { Component, OnInit } from '@angular/core';
import { Worklist } from '../worklist/worklist.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { IdentificationTypeDto, MasterDataDto, ModalityDto, WorklistDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { ModalityService } from '@api-rest/services/modality.service';
import { Observable } from 'rxjs';
import { WorklistService } from '@api-rest/services/worklist.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { MatSelectChange } from '@angular/material/select';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { InformerStatus, mapToState } from '../../utils/study.utils';

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

	readonly COMPLETED = InformerStatus.COMPLETED;
	readonly PENDING = InformerStatus.PENDING;

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
