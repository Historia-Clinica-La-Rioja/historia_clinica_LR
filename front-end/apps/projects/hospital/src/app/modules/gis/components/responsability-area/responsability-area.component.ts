import { AfterViewInit, Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { GlobalCoordinatesDto, SaveInstitutionAddressDto, SaveInstitutionResponsibilityAreaDto } from '@api-rest/api-model';
import { GisService } from '@api-rest/services/gis.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { finalize, forkJoin, Observable } from 'rxjs';
import { InstitutionDescription } from '../institution-description/institution-description.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { GisLayersService } from '../../services/gis-layers.service';
import Control from 'ol/control/Control';

@Component({
	selector: 'app-responsability-area',
	templateUrl: './responsability-area.component.html',
	styleUrls: ['./responsability-area.component.scss']
})
export class ResponsabilityAreaComponent implements AfterViewInit {

	@Input() set setInstitutionDescription(institutionDescription: InstitutionDescription) {
		this.institutionDescription = institutionDescription;
	}
	@Input() editMode: boolean = false;
	@Output() confirmed = new EventEmitter<boolean>();
	@Output() previous = new EventEmitter<boolean>();
	@Output() cancelSelected = new EventEmitter<boolean>();
	isSaving = false;
	institutionDescription: InstitutionDescription;
	ButtonType = ButtonType;
	showUndo = false;
	showRemoveAndCreate = false;

	@ViewChild('undoRef') undoRef;
	@ViewChild('removeAndCreateRef') removeAndCreateRef;
	
	constructor(public readonly gisLayersService: GisLayersService,
				private readonly gisService: GisService,
				private readonly snackBarService: SnackBarService
	) {
		this.gisLayersService.addPolygonInteraction();
		this.gisLayersService.removeLocationClic();
		this.gisLayersService.showUndo$.subscribe((undo: boolean) => this.showUndo = undo);
		this.gisLayersService.showRemoveAndCreate$.subscribe((removeAndCreate: boolean) => this.showRemoveAndCreate = removeAndCreate);
	}

	ngAfterViewInit(): void {
		this.gisLayersService.addControls(new Control({element: this.undoRef._elementRef.nativeElement}), new Control({element: this.removeAndCreateRef._elementRef.nativeElement}));
	}

	previousStepper = () => {
		this.previous.emit(true);
	}

	save = (saveArea: boolean) => {
		this.gisLayersService.removeDrawnPolygon();
		this.gisLayersService.removeAreaLayer();
		this.gisLayersService.toggleActions(false, false);
		this.isSaving = true;

		forkJoin(this.getForkJoinObservables(saveArea))
		.pipe(finalize(() => this.isSaving = false))
		.subscribe((_) => {
			if (!this.gisLayersService.isPolygonCompleted) 
				this.snackBarService.showSuccess("gis.status.UPDATE_DATA_SUCCESS");

			this.confirmed.emit(true);
		});
	}

	removeLastPoint = () => {
		this.gisLayersService.removeLastPoint();
	}

	removeAndAddInteraction = () => {
		this.gisLayersService.removeAndAddInteraction();
	}

	cancel = () => {
		this.gisLayersService.removeDrawnPolygon();
		this.gisLayersService.toggleActions(false, false);
		this.gisLayersService.removeControls();
		this.cancelSelected.emit(true);
	}

	private getForkJoinObservables = (saveArea: boolean): Observable<number>[] => {
		const observables = [
			this.gisService.saveInstitutionCoordinates(this.gisLayersService.coordinateToGlobalCoordinateDto()),
			this.gisService.saveInstitutionAddress(this.mapToSaveInstitutionAddressDto())
		];
		  
		if (saveArea) 
			observables.push(this.gisService.saveInstitutionArea(this.mapToSaveInstitutionResponsibilityAreaDto()));
		return observables;
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
