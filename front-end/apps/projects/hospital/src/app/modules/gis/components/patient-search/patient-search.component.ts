import { Component } from '@angular/core';
import { ButtonType } from '@presentation/components/button/button.component';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';

enum PatientTypeFilter {
	REGISTERED = 'registered',
	OUTPATIENT_CLINIC = 'outpatient_clinic',
}

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
	dateRange: DateRange = {
		start: new Date(),
		end: new Date(),
	}

	toggleDatePicker = (value: boolean) => {
		this.showDatePicker = value;
		this.isOutpatientClinic = value;
	}

	searchPatients = () => {

	}

}
