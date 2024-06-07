import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SaveInstitutionAddressDto } from '@api-rest/api-model';
import { GisService } from '@api-rest/services/gis.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { finalize, forkJoin } from 'rxjs';
import { InstitutionDescription } from '../institution-description/institution-description.component';
import { ButtonType } from '@presentation/components/button/button.component';

@Component({
	selector: 'app-responsability-area',
	templateUrl: './responsability-area.component.html',
	styleUrls: ['./responsability-area.component.scss']
})
export class ResponsabilityAreaComponent {

	@Input() set setInstitutionDescription(institutionDescription: InstitutionDescription) {
		this.institutionDescription = institutionDescription;
	}
	@Output() confirmed = new EventEmitter<boolean>();
	@Output() previous = new EventEmitter<boolean>();
	isSaving = false;
	institutionDescription: InstitutionDescription;
	ButtonType = ButtonType;

	constructor(private readonly gisService: GisService,
				private readonly snackBarService: SnackBarService
	) { }

	previousStepper = () => {
		this.previous.emit(true);
	}

	confirm = () => {
		this.isSaving = true;
		forkJoin(
			[
				this.gisService.saveInstitutionCoordinates(this.institutionDescription.coordinates),
				this.gisService.saveInstitutionAddress(this.mapToSaveInstitutionAddressDto())
			]
		).pipe(finalize(() => this.isSaving = false))
		.subscribe((_) => {
			this.snackBarService.showSuccess("gis.status.UPDATE_DATA_SUCCESS");
			this.confirmed.emit(true);
		});
	}

	private mapToSaveInstitutionAddressDto = (): SaveInstitutionAddressDto => {
		return {
			stateId: this.institutionDescription.address.state.id,
			departmentId: this.institutionDescription.address.department.id,
			cityId: this.institutionDescription.address.city.id,
			streetName: this.institutionDescription.address.streetName,
			houseNumber: this.institutionDescription.address.houseNumber
		}
	}
		
}
