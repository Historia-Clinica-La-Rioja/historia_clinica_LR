import { Component, OnInit } from '@angular/core';
import { ButtonType } from '@presentation/components/button/button.component';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { GisLayersService, PatientTypeFilter } from '../../services/gis-layers.service';
import { Position, RadioGroupData, RadioGroupInputData } from '@presentation/components/radio-group/radio-group.component';

@Component({
	selector: 'app-patient-search',
	templateUrl: './patient-search.component.html',
	styleUrls: ['./patient-search.component.scss']
})
export class PatientSearchComponent implements OnInit {

	ButtonType = ButtonType;
	showDatePicker = false;
	isOutpatientClinic = false;
	initDateRange: DateRange = {
		start: new Date(),
		end: new Date(),
	}
	disableButton = false;
	dateRange: DateRange;
	isPatientsLoading = false;
	radioGroupInputData: RadioGroupInputData = {
		presentation: {
			title: '',
			data: [
				{
					value: PatientTypeFilter.REGISTERED, 
					description: 'gis.patient-search.filters.REGISTERED_PATIENTS'
				}, 
				{
					value: PatientTypeFilter.OUTPATIENT_CLINIC, 
					description: 'gis.patient-search.filters.TREATED_IN_OUTPATIENT_CLINIC'
				}
			],
			previousValueId: PatientTypeFilter.REGISTERED
		},
		alignments: {
			position: Position.COLUMN,
			optionsPosition: Position.COLUMN
		}
	}

	constructor(public readonly gisLayersService: GisLayersService) {}

	ngOnInit(): void {
		this.removePatientsAndListener();
	}

	toggleDatePicker = (value: RadioGroupData) => {
		this.removePatientsAndListener();
		this.showDatePicker = value.value === PatientTypeFilter.OUTPATIENT_CLINIC;
		this.isOutpatientClinic = this.showDatePicker;
	}

	searchPatients = () => {
		this.gisLayersService.toggleIsPatientLoading();
		(this.isOutpatientClinic) ? this.filterByOutpatientClinic() : this.filterByInstitution();
	}

	dateRangeChange = (dateRange: DateRange) => {
		this.dateRange = dateRange;
		this.disableButton = dateRange === null;
	}

	private removePatientsAndListener = () => {
		this.gisLayersService.removePatientFeatures();
		this.gisLayersService.removeMapMoveendListener();
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
