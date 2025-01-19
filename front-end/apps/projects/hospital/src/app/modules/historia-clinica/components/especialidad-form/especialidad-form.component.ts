import { Component, forwardRef } from '@angular/core';
import { ControlValueAccessor, FormBuilder, FormControl, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { ClinicalSpecialtyDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-especialidad-form',
	templateUrl: './especialidad-form.component.html',
	styleUrls: ['./especialidad-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => EspecialidadFormComponent),
			multi: true,
		}
	]
})
export class EspecialidadFormComponent implements ControlValueAccessor {

	specialties: ClinicalSpecialtyDto[];
	formClinicalSpecialty = this.formBuilder.group({
		clinicalSpecialty: new FormControl<ClinicalSpecialtyDto | null>(null, Validators.required),
	});

	onChangeSub: Subscription;

	constructor(
		private formBuilder: FormBuilder,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
	) {
		this.setProfessionalSpecialties();
	}

	private setProfessionalSpecialties() {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
			this.setSpecialtyFields(specialties);
		});
	}

	private setSpecialtyFields(specialtyArray: ClinicalSpecialtyDto[]) {
		this.specialties = specialtyArray;
		const preloadedSpecialty = this.specialties.find(specialty => this.formClinicalSpecialty.value.clinicalSpecialty?.id === specialty.id);
		this.formClinicalSpecialty.get('clinicalSpecialty').setValue(preloadedSpecialty || this.specialties[0]);
		this.formClinicalSpecialty.controls['clinicalSpecialty'].markAsTouched();
	}

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj)
			this.formClinicalSpecialty.setValue(obj);
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.formClinicalSpecialty.valueChanges
			.subscribe(value => {
				const toEmit = this.formClinicalSpecialty.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.formClinicalSpecialty.disable() : this.formClinicalSpecialty.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}
