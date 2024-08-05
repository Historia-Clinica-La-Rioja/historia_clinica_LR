import { Component } from '@angular/core';
import { ButtonType } from '@presentation/components/button/button.component';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { GisLayersService, PatientTypeFilter } from '../../services/gis-layers.service';

@Component({
	selector: 'app-patient-search',
	templateUrl: './patient-search.component.html',
	styleUrls: ['./patient-search.component.scss']
})
export class PatientSearchComponent {

	ButtonType = ButtonType;
	PatientTypeFilter = PatientTypeFilter;
	showDatePicker = false;
	isOutpatientClinic = false;
	initDateRange: DateRange = {
		start: new Date(),
		end: new Date(),
	}
	disableButton = false;
	dateRange: DateRange;

	constructor(private readonly gisLayersService: GisLayersService) {}

	toggleDatePicker = (value: boolean) => {
		this.showDatePicker = value;
		this.isOutpatientClinic = value;
	}

	searchPatients = () => {
		(this.isOutpatientClinic) ? this.filterByOutpatientClinic() : this.filterByInstitution();
	}

	dateRangeChange = (dateRange: DateRange) => {
		this.dateRange = dateRange;
		this.disableButton = dateRange === null;
	}

	private filterByInstitution = () => {
		this.gisLayersService.filterByInstitution();
		this.gisLayersService.detectWhenMapMoves();	
	}

	private filterByOutpatientClinic = () => {
		this.gisLayersService.filterByOutpatientClinic(this.dateRange);
		this.gisLayersService.detectWhenMapMoves();
	}
}
