import { ChangeDetectionStrategy, Component, EventEmitter, forwardRef, Input, OnDestroy, Output } from '@angular/core';
import { ControlValueAccessor, FormArray, FormBuilder, FormControl, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { ClinicalSpecialtyDto, HealthcareProfessionalSpecialtyDto, ProfessionalProfessionsDto, ProfessionalSpecialtyDto } from '@api-rest/api-model';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-profession-specialties-form',
	templateUrl: './profession-specialties-form.component.html',
	styleUrls: ['./profession-specialties-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => ProfessionSpecialtiesFormComponent),
			multi: true,
		}
	],
	changeDetection: ChangeDetectionStrategy.OnPush

})

export class ProfessionSpecialtiesFormComponent implements ControlValueAccessor, OnDestroy {
	professionsTypeahead: TypeaheadOption<ProfessionalSpecialtyDto>[] = [];
	specialtiesTypeahead: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
	initValueTypeaheadSpecialties: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
	initValueTypeaheadProfessions: TypeaheadOption<ProfessionalSpecialtyDto> = null;
	onChangeSub: Subscription;
	@Input() allProfessions: ProfessionalSpecialtyDto[] = [];
	@Input() allSpecialties: ClinicalSpecialtyDto[] = [];
	@Input() confirmationValidation: boolean = false;
	@Input() professionalProfessionsDto?: ProfessionalProfessionsDto = null;
	@Output() isEmptyCombo = new EventEmitter<boolean>();
	onTouched = () => { };

	form = this.formBuilder.group({
		id: null,
		healthcareProfessionalId: null,
		profession: new FormControl(null, Validators.required),
		specialties: new FormArray([])
	});

	constructor(
		private formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.professionsTypeahead = this.allProfessions.map(d => this.toProfessionDtoTypeahead(d));
		this.specialtiesTypeahead = this.allSpecialties.map(d => this.toSpecialtyDtoTypeahead(d));
		if (!!this.professionalProfessionsDto)
			this.setPreviousSpeciality();

	}

	private setPreviousSpeciality(): void {
		this.initValueTypeaheadProfessions = this.toProfessionDtoTypeahead(this.professionalProfessionsDto.profession);
		this.form.controls.healthcareProfessionalId.setValue(this.professionalProfessionsDto.healthcareProfessionalId);
		this.form.controls.id.setValue(this.professionalProfessionsDto.id);
		this.professionalProfessionsDto.specialties.forEach((elem: HealthcareProfessionalSpecialtyDto) => {
			this.addSpecialties(elem);
			this.initValueTypeaheadSpecialties.push(this.toSpecialtyDtoTypeahead(elem.clinicalSpecialty));
		});
	}

	setProfession($event: ProfessionalSpecialtyDto): void {
		const arraySpecialties = this.form.get('specialties') as FormArray;

		this.form.controls.profession.setValue($event);
		if ($event === null) {
			this.isEmptyCombo.emit(true);
		} else {
			if (arraySpecialties.length === 0)
				this.addSpecialties();
		}
	}


	setSpeciality($event: ClinicalSpecialtyDto, pointIndex: number): void {
		const arraySpecialties = this.form.get('specialties') as FormArray;

		arraySpecialties.at(pointIndex).setValue({
			id: arraySpecialties.at(pointIndex).value.id,
			professionalProfessionId: arraySpecialties.at(pointIndex).value.professionalProfessionId,
			healthcareProfessionalId: arraySpecialties.at(pointIndex).value.healthcareProfessionalId,
			clinicalSpecialty: {
				id: $event ? $event.id : null,
				specialty: $event ? $event.name : null
			}
		});

		if ($event === null) {
			this.initValueTypeaheadSpecialties.splice(pointIndex, 1);
			if (arraySpecialties.length > 1) {
				arraySpecialties.removeAt(pointIndex)
			}
		}

	}

	private toProfessionDtoTypeahead(professionDto: ProfessionalSpecialtyDto): TypeaheadOption<ProfessionalSpecialtyDto> {
		return {
			compareValue: professionDto.description,
			value: professionDto
		};
	}

	private toSpecialtyDtoTypeahead(speciality: ClinicalSpecialtyDto): TypeaheadOption<ClinicalSpecialtyDto> {
		return {
			compareValue: speciality.name,
			value: speciality
		};
	}

	getCtrl(key: string, form: FormGroup): any {
		return form.get(key);
	}

	initFormSpecialties(elem?: HealthcareProfessionalSpecialtyDto): FormGroup {
		const arraySpecialties = this.form.get('specialties') as FormArray;
		let length = arraySpecialties?.length;

		return new FormGroup({
			id: new FormControl(elem ? elem.id : null, []),
			professionalProfessionId: new FormControl(elem ? elem.professionalProfessionId : null, []),
			healthcareProfessionalId: new FormControl(elem ? elem.healthcareProfessionalId : null, []),
			clinicalSpecialty: new FormGroup({
				id: new FormControl(elem ? elem.clinicalSpecialty.id : null, length === 0 ? Validators.required : null),
				specialty: new FormControl(elem ? elem.clinicalSpecialty.name : null)
			})
		});
	}

	addSpecialties(elem?: HealthcareProfessionalSpecialtyDto): void {
		const arraySpecialties = this.form.get('specialties') as FormArray;
		arraySpecialties.push(this.initFormSpecialties(elem));
	}

	hasErrorProfession(): boolean {
		return (this.confirmationValidation && this.form.controls.profession.value === null);
	}

	hasErrorSpecialty(): boolean {
		const arraySpecialties = this.form.get('specialties') as FormArray;
		let length = arraySpecialties?.length;
		return (this.confirmationValidation && length > 0 ? (arraySpecialties.at(arraySpecialties.length - 1).value.clinicalSpecialty.id === null) : false);
	}

	isDisableAddSpecialties(): boolean {
		const arraySpecialties = this.form.get('specialties') as FormArray;
		let length = arraySpecialties?.length;
		return (!this.form.value?.profession?.id || (length > 0 ? (arraySpecialties.at(arraySpecialties.length - 1).value.clinicalSpecialty.id === null) : false));
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
