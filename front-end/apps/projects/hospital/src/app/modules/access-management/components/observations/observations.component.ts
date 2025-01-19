import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-observations',
	templateUrl: './observations.component.html',
	styleUrls: ['./observations.component.scss']
})
export class ObservationsComponent {

	form: FormGroup<ObservationsForm>;
	@Input()
	set oldObservations(observations: string) {
		this.form.controls.observations.setValue(observations);
	}
	@Input()
	set disabled (disable: boolean) {
		if (disable) this.form.disable();
	};

	@Output() newObservations = new EventEmitter<string>();

	constructor() {
		this.createForm();
		this.subscribeToFormChangesAndEmit()
	}

	private createForm() {
		this.form = new FormGroup<ObservationsForm>({
			observations: new FormControl(null),
		});
	}

	private subscribeToFormChangesAndEmit() {
		this.form.controls.observations.valueChanges.subscribe(changes =>
			this.newObservations.emit(changes)
		);
	}

}

interface ObservationsForm {
	observations: FormControl<string>;
}
