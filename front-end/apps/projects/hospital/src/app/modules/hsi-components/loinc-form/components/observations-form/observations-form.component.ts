import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { LoincFormValues, LoincInput, LoincObservationValue } from '../../loinc-input.model';
import { LoincFormControlService } from './loinc-form-control.service';
import { ButtonService } from '@historia-clinica/modules/ambulatoria/services/button.service';

@Component({
	selector: 'app-observations-form',
	templateUrl: './observations-form.component.html',
	styleUrls: ['./observations-form.component.scss']
})
export class ObservationsFormComponent  implements OnInit {
	@Input() loincForm: LoincInput[] | null = [];
	@Output() valueChange: EventEmitter<LoincFormValues> = new EventEmitter<LoincFormValues>();
	@Input() values: LoincFormValues;

	loincFormControl: LoincFormControlService;

	constructor(
		private readonly buttonService: ButtonService) {
	}

	ngOnInit() {
		if (!this.loincForm) {
			return;
		}
		this.loincFormControl = new LoincFormControlService(
			this.loincForm as LoincInput[],
			this.values,
			this.buttonService
		);
		this.loincFormControl.valueChange$.subscribe(
			values => this.valueChange.emit(values)
		)
	}

	loincInputValueChange($event: LoincObservationValue) {
		this.loincFormControl.setValue($event);
	}
}
