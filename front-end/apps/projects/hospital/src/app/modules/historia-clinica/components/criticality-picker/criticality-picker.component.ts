import { Component, EventEmitter, forwardRef, Input, OnInit } from '@angular/core';
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
	criticalities: MasterDataDto[] = [];

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
		this.emergencyCareMasterDataService.getCriticalities().subscribe(criticalities => this.criticalities = criticalities);
	}

	createForm() {
		this.form = new FormGroup<CriticalityForm>({
			criticality: new FormControl<MasterDataDto>(null)
		});
	}

	writeValue(value: { criticality: MasterDataDto }) {
		if (value) {
			const preloadCriticalityId = value.criticality.id;
			this.form.controls.criticality.setValue(this.criticalities.find(criticality => criticality.id === preloadCriticalityId))
		}
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


