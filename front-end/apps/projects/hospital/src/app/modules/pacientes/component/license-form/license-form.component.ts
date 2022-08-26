import { ChangeDetectionStrategy, Component, forwardRef, Input, OnChanges, OnDestroy } from '@angular/core';
import { ControlValueAccessor, FormBuilder, FormControl, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { ClinicalSpecialtyDto } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';
import { ProfessionalSpecialties } from '@pacientes/routes/profile/profile.component';
import { Subscription } from 'rxjs';

const MAX_LENGTH = 15;

@Component({
	selector: 'app-license-form',
	templateUrl: './license-form.component.html',
	styleUrls: ['./license-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => LicenseFormComponent),
			multi: true,
		}
	],
	changeDetection: ChangeDetectionStrategy.OnPush
})

export class LicenseFormComponent implements ControlValueAccessor, OnDestroy, OnChanges {

	hasError = hasError;
	onTouched = () => { };
	RADIO_OPTION_NATIONAL = 1;
	RADIO_OPTION_PROVINCE = 2;
	specialtiesOption: ClinicalSpecialtyDto[] = [];
	@Input() professionSpecialties: ProfessionalSpecialties[] = [];
	@Input() confirmationValidation = false;
	onChangeSub: Subscription;


	form = this.formBuilder.group({
		id: null,
		licenseNumber: new FormControl(null, [Validators.required, Validators.pattern(/^([a-zA-Z1-9])+$/), Validators.maxLength(MAX_LENGTH)]),

		typeId: this.RADIO_OPTION_NATIONAL,
		professionalProfessionId: new FormControl(null, [Validators.required]),
		healthcareProfessionalSpecialtyId: new FormControl(null, []),
		radioButtonOptionSpecialty: new FormControl(false, [])
	});

	constructor(
		private formBuilder: FormBuilder,

	) { }

	ngOnChanges(): void {
		if (this.confirmationValidation)
			this.form.markAllAsTouched();
	}

	ngOnInit(): void {

		this.form.controls.professionalProfessionId?.valueChanges.subscribe((professionalProfessionId: number) => {
			this.form.controls.healthcareProfessionalSpecialtyId.setValue(null);
			this.specialtiesOption = this.professionSpecialties.find((elem: ProfessionalSpecialties) => elem.profession.id === professionalProfessionId)?.specialties;
		});

		this.form.controls.radioButtonOptionSpecialty?.valueChanges.subscribe((value: boolean) => {
			if (value) {
				this.form.controls.healthcareProfessionalSpecialtyId.setValidators([Validators.required]);
				this.form.controls.healthcareProfessionalSpecialtyId.updateValueAndValidity();
			}
			else {
				this.form.controls.healthcareProfessionalSpecialtyId.removeValidators([Validators.required]);
				this.form.controls.healthcareProfessionalSpecialtyId.updateValueAndValidity();
				this.form.controls.healthcareProfessionalSpecialtyId.setValue(null);
			}
		});

	}

	writeValue(obj: any): void {
		if (obj)
			this.form.setValue(obj);
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.form.valueChanges
			.subscribe(value => {
				const toEmit = this.form.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.form.disable() : this.form.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}
