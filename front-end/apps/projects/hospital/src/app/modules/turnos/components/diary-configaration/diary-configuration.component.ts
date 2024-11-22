import { ChangeDetectionStrategy, Component, forwardRef, OnInit } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { DiaryBookingRestrictionDto, EDiaryBookingRestrictionType } from '@api-rest/api-model';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { getError, hasError } from '@core/utils/form.utils';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';

@Component({
	selector: 'app-diary-configuration',
	templateUrl: './diary-configuration.component.html',
	styleUrls: ['./diary-configuration.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => DiaryConfigurationComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => DiaryConfigurationComponent),
		},
	],
	changeDetection: ChangeDetectionStrategy.OnPush
})
export class DiaryConfigurationComponent extends AbstractCustomForm  implements OnInit {

	getError = getError;
	hasError = hasError;


	createForm() {

		this.form = new FormGroup<ConfigurationTypeCustomForm>({
			configurationType: new FormControl(CONFIGARATION_TYPES.at(0).id),
			daysRange: new FormControl(null,[Validators.min(RANGOS.MIN.range), Validators.max(RANGOS.MAX.range), Validators.pattern(PATTERN_INTEGER_NUMBER)])
		});
	}

	writeValue(obj: DiaryBookingRestrictionDto): void {
		if (obj) {
			this.form.patchValue({
                configurationType: obj.restrictionType,
                daysRange: obj.days
            });
		}
	}

	configurationTypeArray = CONFIGARATION_TYPES
	enableRangeDays = false

	constructor() {
		super();
	}

	ngOnInit(): void {
		this.createForm();
		this.form.controls.configurationType.valueChanges.subscribe(
			value =>
			{
				this.enableRangeDays = value === EDiaryBookingRestrictionType.RESTRICTED_BY_DAYS_AMOUNT
				this.form.controls.daysRange.setValue(null)
			}
		)
	}

}


interface ConfigurationTypeCustomForm {
	configurationType: FormControl<string>;
	daysRange: FormControl<number>;
}


export enum CONFIGURATION_TYPES_ENUM {
	SIN_RESTRICCIONES = 'SIN RESTRICCIONES',
    HABILITACION_POR_MES = 'HABILITACION POR MES',
    HABILITACION_POR_RANGO_DE_DIAS = 'HABILITACION POR RANGO DE DIAS',
}

const CONFIGARATION_TYPES = [
	{
		id: EDiaryBookingRestrictionType.UNRESTRICTED,
		description: CONFIGURATION_TYPES_ENUM.SIN_RESTRICCIONES
	},
	{
		id: EDiaryBookingRestrictionType.RESTRICTED_BY_CURRENT_MONTH,
		description: CONFIGURATION_TYPES_ENUM.HABILITACION_POR_MES
	},
	{
		id: EDiaryBookingRestrictionType.RESTRICTED_BY_DAYS_AMOUNT,
		description: CONFIGURATION_TYPES_ENUM.HABILITACION_POR_RANGO_DE_DIAS
	},

]

const RANGOS = {
	MIN: { range: 1},
    MAX: { range: 90}
}