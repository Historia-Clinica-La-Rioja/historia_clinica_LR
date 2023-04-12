import { ChangeDetectionStrategy, Component, OnInit, forwardRef } from '@angular/core';
import { FormBuilder, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
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
	],
	changeDetection: ChangeDetectionStrategy.OnPush
})
export class EspecialidadFormComponent implements OnInit {


	specialties: ClinicalSpecialtyDto[];
	defaultSpecialty: ClinicalSpecialtyDto;
	formEvolucion = this.formBuilder.group({
		clinicalSpecialty: [null, [Validators.required]],
	});


	onChangeSub: Subscription;

	constructor(
		private formBuilder: FormBuilder,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
	) {
		this.setProfessionalSpecialties()
	}

	private setProfessionalSpecialties() {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
			this.setSpecialtyFields(specialties);
		});
	}

	private setSpecialtyFields(specialtyArray: ClinicalSpecialtyDto[]) {
		this.specialties = specialtyArray;
		this.defaultSpecialty = specialtyArray[0];
		this.formEvolucion.get('clinicalSpecialty').setValue(this.defaultSpecialty);
		this.formEvolucion.controls['clinicalSpecialty'].markAsTouched();
	}


	ngOnInit(): void {
	}


	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj)
			this.formEvolucion.setValue(obj);
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.formEvolucion.valueChanges
			.subscribe(value => {
				const toEmit = this.formEvolucion.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.formEvolucion.disable() : this.formEvolucion.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}
