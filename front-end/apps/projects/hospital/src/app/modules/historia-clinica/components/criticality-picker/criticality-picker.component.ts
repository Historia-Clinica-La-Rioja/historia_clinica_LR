import { Component, EventEmitter, forwardRef, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { MasterDataDto } from '@api-rest/api-model';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';

@Component({
	selector: 'app-criticality-picker',
	templateUrl: './criticality-picker.component.html',
	styleUrls: ['./criticality-picker.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => CriticalityPickerComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => CriticalityPickerComponent),
		},
	],
})
export class CriticalityPickerComponent extends AbstractCustomForm implements OnInit {

	form: FormGroup<CriticalityForm>;
	criticalities$: Observable<MasterDataDto[]>;

	@Input()
	set isRequired(isRequired: boolean) {
		isRequired ? this.addRequiredValidator() : this.removeRequiredValidator();
	};

	@Input()
	set submitParentFormEvent(event: EventEmitter<void>) {
		if (event)
			super.subscribeToSubmitParentForm(event);
	};

	constructor(
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
	) {
		super();
		this.createForm();
	}

	ngOnInit(): void {
		this.criticalities$ = this.emergencyCareMasterDataService.getCriticalities();
	}

	createForm() {
		this.form = new FormGroup<CriticalityForm>({
			criticality: new FormControl<MasterDataDto>(null)
		});
	}

	private addRequiredValidator() {
		this.form.controls.criticality.addValidators(Validators.required);
		this.form.controls.criticality.updateValueAndValidity();
	}

	private removeRequiredValidator() {
		this.form.controls.criticality.removeValidators(Validators.required);
		this.form.controls.criticality.updateValueAndValidity();
	}

}

interface CriticalityForm {
	criticality: FormControl<MasterDataDto>;
}


