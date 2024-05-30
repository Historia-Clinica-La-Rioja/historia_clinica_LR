import { Component, Input } from '@angular/core';
import { GetSanitaryResponsibilityAreaInstitutionAddressDto, GlobalCoordinatesDto, SaveInstitutionAddressDto } from '@api-rest/api-model';
import { GisService } from '@api-rest/services/gis.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { finalize, forkJoin } from 'rxjs';
import { transformCoordinates } from '../../constants/coordinates.utils';

export interface InstitutionDescription {
	address: GetSanitaryResponsibilityAreaInstitutionAddressDto,
	title: string,
	institution: string,
	coordinates: GlobalCoordinatesDto
}

@Component({
	selector: 'app-institution-description',
	templateUrl: './institution-description.component.html',
	styleUrls: ['./institution-description.component.scss']
})
export class InstitutionDescriptionComponent {

	@Input() set setInstitutionDescription(institutionDescription: InstitutionDescription) {
		this.institutionDescription = institutionDescription;
		this.coordinatesToShow = transformCoordinates(institutionDescription.coordinates);
	}
	@Input() showActions: boolean = true;

	institutionDescription: InstitutionDescription;
	isSaving: boolean = false;
	ButtonType = ButtonType;
	coordinatesToShow: string;

	constructor(private readonly gisService: GisService,
				private readonly snackBarService: SnackBarService
	) { }

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
