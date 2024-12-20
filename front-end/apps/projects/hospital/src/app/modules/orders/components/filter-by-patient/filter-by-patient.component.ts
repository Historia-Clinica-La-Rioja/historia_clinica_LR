import { Component, EventEmitter, Output } from '@angular/core';

@Component({
	selector: 'app-filter-by-patient',
	templateUrl: './filter-by-patient.component.html',
	styleUrls: ['./filter-by-patient.component.scss']
})
export class FilterByPatientComponent {
	@Output() selectionChange = new EventEmitter<any[]>();

	allPatientType = [1, 2, 3, 4, 5, 6, 7, 8];
	temporalPatient = [8];
	isTemporaryPatient = false;
	onToggleChange(event: any): void {
		const isTemporaryPatient = event.checked;
		isTemporaryPatient ? this.selectionChange.emit(this.temporalPatient) : this.selectionChange.emit(this.allPatientType);
	}

}
