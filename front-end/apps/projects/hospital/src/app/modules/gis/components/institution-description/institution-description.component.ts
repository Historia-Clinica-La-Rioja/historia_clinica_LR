import { Component, Input } from '@angular/core';
import { GetSanitaryResponsibilityAreaInstitutionAddressDto, GlobalCoordinatesDto } from '@api-rest/api-model';
import { transformCoordinates } from '../../constants/coordinates.utils';

export interface InstitutionDescription {
	address: GetSanitaryResponsibilityAreaInstitutionAddressDto,
	title: string,
	institution: string,
	coordinates: GlobalCoordinatesDto,
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
	institutionDescription: InstitutionDescription;
	coordinatesToShow: string;
}
