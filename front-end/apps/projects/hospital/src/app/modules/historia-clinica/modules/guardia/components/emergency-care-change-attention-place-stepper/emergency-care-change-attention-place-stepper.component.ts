import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EmergencyCarePatientDto } from '@api-rest/api-model';

@Component({
  selector: 'app-emergency-care-change-attention-place-stepper',
  templateUrl: './emergency-care-change-attention-place-stepper.component.html',
  styleUrls: ['./emergency-care-change-attention-place-stepper.component.scss']
})
export class EmergencyCareChangeAttentionPlaceStepperComponent {

	@Input() patient: EmergencyCarePatientDto;
	@Output() formEmitter: EventEmitter<boolean> = new EventEmitter<boolean>();

	constructor() { }

	save(){
		//eventemitter del form al dialogo
		this.formEmitter.emit(true);
	}

}
