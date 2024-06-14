import { Component, EventEmitter, Input, Output } from '@angular/core';
import { GlobalCoordinatesDto, SaveInstitutionAddressDto, SaveInstitutionResponsibilityAreaDto } from '@api-rest/api-model';
import { GisService } from '@api-rest/services/gis.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { finalize, forkJoin } from 'rxjs';
import { InstitutionDescription } from '../institution-description/institution-description.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { GisLayersService } from '../../services/gis-layers.service';

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
	showPolygonError = false;
	constructor(private readonly gisService: GisService,
				private readonly gisLayersService: GisLayersService,
				private readonly snackBarService: SnackBarService
	) {}

	previousStepper = () => {
		this.previous.emit(true);
	}

	handleConfirm = () => {
		if (!this.gisLayersService.isPolygonCompleted) 
			return this.setPolygonError();

		this.saveAddressAndCoordinates(false);
		this.saveInstitutionArea();
	}

	saveAddressAndCoordinates = (removeDrawnPolygon: boolean) => {
		this.isSaving = true;
		forkJoin(
			[
				this.gisService.saveInstitutionCoordinates(this.institutionDescription.coordinates),
				this.gisService.saveInstitutionAddress(this.mapToSaveInstitutionAddressDto())
			]
		).pipe(finalize(() => this.isSaving = false))
		.subscribe((_) => {
			this.snackBarService.showSuccess("gis.status.UPDATE_DATA_SUCCESS");
			removeDrawnPolygon ? this.gisLayersService.removeDrawnPolygon(): null;
			this.confirmed.emit(true);
		});
	}

	private saveInstitutionArea = () => {
		this.gisService.saveInstitutionArea(this.mapToSaveInstitutionResponsibilityAreaDto()).subscribe();
	}

	private mapToSaveInstitutionResponsibilityAreaDto = (): SaveInstitutionResponsibilityAreaDto => {
		const coordinates: GlobalCoordinatesDto[] = this.mapToGlobalCoordinatesDto();
		return {
			responsibilityAreaPolygon: coordinates
		}
	}

	private mapToGlobalCoordinatesDto = (): GlobalCoordinatesDto[] => {
		const coordinates: GlobalCoordinatesDto[] = this.gisLayersService.polygonCoordinates.flatMap(
			coordinateSet => coordinateSet.map(
				([longitude, latitude]) => ({ latitude, longitude })
			)
		);
		return coordinates;
	}

	private setPolygonError = () => {
		this.showPolygonError = true;
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
