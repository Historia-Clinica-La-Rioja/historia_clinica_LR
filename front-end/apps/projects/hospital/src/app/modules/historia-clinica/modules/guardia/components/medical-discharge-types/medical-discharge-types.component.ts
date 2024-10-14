import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AbstractControl, FormBuilder, UntypedFormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MasterDataInterface } from '@api-rest/api-model';
import { DischargeTypes } from '@api-rest/masterdata';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { hasError, NoWhitespaceValidator } from '@core/utils/form.utils';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-medical-discharge-types',
  templateUrl: './medical-discharge-types.component.html',
  styleUrls: ['./medical-discharge-types.component.scss']
})
export class MedicalDischargeTypesComponent implements OnInit {

	hasError = hasError;

	@Output() dischargeTypeForm = new EventEmitter<UntypedFormGroup>();

	form: UntypedFormGroup;
	dischargeTypes$: Observable<MasterDataInterface<number>[]>;
	dischargeTypesEnum = DischargeTypes;

	constructor(
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private formBuilder: FormBuilder
	) { }

	ngOnInit() {
		this.form = this.formBuilder.group({
			autopsy: [null],
			dischargeTypeId: [DischargeTypes.ALTA_MEDICA, Validators.required],
			otherDischargeDescription: [null]
		});
		this.dischargeTypes$ = this.emergencyCareMasterDataService.getDischargeType();

		this.updateDischargeTypeValidators();
  		this.emitForm();
		this.setFormValueChanges();
	}

	private setFormValueChanges() {
		this.form.valueChanges.subscribe(() => {
			this.updateDischargeTypeValidators();
			this.emitForm();
		});
	}

	private updateDischargeTypeValidators() {
		const dischargeType = this.form.get('dischargeTypeId').value;
		const autopsyControl = this.form.get('autopsy');
		const descriptionControl = this.form.get('otherDischargeDescription');

		this.setControlValidators(autopsyControl, dischargeType === this.dischargeTypesEnum.DEFUNCION);
		this.setControlValidators(descriptionControl, dischargeType === this.dischargeTypesEnum.OTRO);
	}

	private setControlValidators(control: AbstractControl, condition: boolean) {
		const validators = this.getValidatorsForControl(control, condition);
		control.setValidators(validators);
		control.markAsTouched();
		control.updateValueAndValidity({ emitEvent: false });
	}

	private getValidatorsForControl(control: AbstractControl, condition: boolean): ValidatorFn[] {
		if (condition) {
			return control === this.form.get('otherDischargeDescription')
				? [Validators.required, NoWhitespaceValidator()]
				: [Validators.required];
		}
		return [];
	}

	private emitForm() {
		this.dischargeTypeForm.emit(this.form.valid ? this.form : this.createInvalidForm());
	}

	private createInvalidForm(): UntypedFormGroup {
		return this.formBuilder.group({
			autopsy: [null],
			dischargeTypeId: [null],
			otherDischargeDescription: [null]
		});
	}

}
