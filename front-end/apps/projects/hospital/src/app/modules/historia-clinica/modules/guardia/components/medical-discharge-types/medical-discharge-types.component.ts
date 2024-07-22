import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, UntypedFormGroup, Validators } from '@angular/forms';
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

	@Input() form: UntypedFormGroup;

	dischargeTypes$: Observable<MasterDataInterface<number>[]>;
	dischargeTypesEnum = DischargeTypes;

	constructor(
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
	) { }

	ngOnInit() {
		this.dischargeTypes$ = this.emergencyCareMasterDataService.getDischargeType();

		this.form.controls.dischargeTypeId.valueChanges.subscribe(discharge => {
			this.updateDischargeTypeValidators(discharge);
		});
	}

	private updateDischargeTypeValidators(value: DischargeTypes): void {
		const autopsyControl = this.form.get('autopsy');
		const descriptionControl = this.form.get('otherDischargeDescription');

		this.setControlValidators(autopsyControl, value === this.dischargeTypesEnum.DEFUNCION);
		this.setControlValidators(descriptionControl, value === this.dischargeTypesEnum.OTRO);
	}

	private setControlValidators(control: AbstractControl, condition: boolean) {
		if (condition) {
			const validators = control === this.form.get('otherDischargeDescription')
			  ? [Validators.required, NoWhitespaceValidator()]
			  : [Validators.required];
			control.setValidators(validators);
		} else {
			control.setValue(null);
			control.clearValidators();
		}
		control.updateValueAndValidity();
	}

}
