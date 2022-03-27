import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HCEAllergyDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { pushIfNotExists } from '@core/utils/array.utils';
import { ShowAllergiesComponent } from '../dialogs/show-allergies/show-allergies.component';

@Injectable({
	providedIn: 'root'
})
export class PatientAllergiesService {

	criticalAllergies: HCEAllergyDto[] = [];
	limitAllergies = 2;

	constructor(
		private readonly dialog: MatDialog,
		private readonly hceGeneralStateService: HceGeneralStateService,

	) { }

	updateCriticalAllergies(patientId: number) {
		this.criticalAllergies = [];
		this.hceGeneralStateService.getCriticalAllergies(patientId)
			.subscribe(allergies => {
				allergies.forEach(allergy => {
					this.criticalAllergies = pushIfNotExists<HCEAllergyDto>(this.criticalAllergies, allergy, this.compareAllergy);
				})
			});
	}

	compareAllergy(data: HCEAllergyDto, data1: HCEAllergyDto): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}

	openAllergies() {
		this.dialog.open(ShowAllergiesComponent, {
			disableClose: true,
			autoFocus: false,
			width: '35%',
			data: {
				allergies: this.criticalAllergies
			}
		});
	}
}
