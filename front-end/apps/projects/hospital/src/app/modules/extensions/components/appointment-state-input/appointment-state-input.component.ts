import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MedicalConsultationMasterdataService} from "@api-rest/services/medical-consultation-masterdata.service";

@Component({
	selector: 'app-appointment-state-input',
	templateUrl: './appointment-state-input.component.html',
	styleUrls: ['./appointment-state-input.component.scss']
})
export class AppointmentStateInputComponent implements OnInit {

	@Input() label: string;
	@Output() appointmentStateChange = new EventEmitter<string[]>();

	appointmentStateForm = new FormGroup({
		state: new FormControl(),
	});

	appointmentStateList = [];

	constructor(private readonly medicalConsultationMasterdataService: MedicalConsultationMasterdataService) {
	}

	ngOnInit(): void {
		this.medicalConsultationMasterdataService.getAppointmentState().subscribe(states => {
			this.appointmentStateList = states.map(state => {
				console.log(state)
				return {
					compareValue: state.description,
					value: state.description
				}
			})
		});
		this.appointmentStateForm.valueChanges.subscribe(value => this.emitAppointmentStateChange(value));
	}

	emitAppointmentStateChange(value) {
		this.appointmentStateChange.emit(value ? [value] : null);
	}

}
