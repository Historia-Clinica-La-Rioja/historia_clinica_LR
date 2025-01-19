import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '../register-editor-info/register-editor-info.component';
import { NoWhitespaceValidator } from '@core/utils/form.utils';

@Component({
	selector: 'app-add-observations',
	templateUrl: './add-observations.component.html',
	styleUrls: ['./add-observations.component.scss']
})
export class AddObservationsComponent implements OnInit {

	formObservations: FormGroup<FormObservations>;
	editObservations = true;
	showEditObservations = false;
	_observation = '';
	disabled$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
	REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
	@Input() set observation(observation: string) {
		if (observation) {
			this.showEditObservations = true;
			this.editObservations = false;
			this._observation = observation;
		}
	};
	@Input() registerEditor?: RegisterEditor;
	@Output() observationEmmiter: EventEmitter<string> = new EventEmitter<string>();
	constructor(
		private readonly formBuilder: FormBuilder,

	) { }

	ngOnInit() {
		this.formObservations = this.formBuilder.group({
			observation: new FormControl(this._observation, { validators: [Validators.required, NoWhitespaceValidator()] })
		});
	}

	setShowEditObservations() {
		this.showEditObservations = !this.showEditObservations;
	}

	cancelObservation() {
		if (this._observation) {
			this.formObservations.controls.observation.setValue(this._observation)
			this.editObservations = false
		}
		else {
			this.formObservations.controls.observation.setValue(null);
			this.showEditObservations = !this.showEditObservations;
		}
	}

	setEditObservations() {
		this.editObservations = true;
	}

	updateObservation() {
		this._observation = this.formObservations.value.observation;
		this.editObservations = false;
		this.observationEmmiter.emit(this._observation);
	}

}

interface FormObservations {
	observation: FormControl<string | null>;
}
