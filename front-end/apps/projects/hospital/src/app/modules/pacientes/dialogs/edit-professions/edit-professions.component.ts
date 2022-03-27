import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ClinicalSpecialtyDto, HealthcareProfessionalCompleteDto, HealthcareProfessionalSpecialtyDto, ProfessionalDto } from '@api-rest/api-model';
import { ProfessionAndSpecialtyDto } from '@pacientes/routes/profile/profile.component';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

export interface ProfessionDto {
	description: string,
	id: number
}

export interface SpecialtyDto {
	name: string,
	id: number
}

export interface ProfessionalSpecialtyDtos {
	clinicalSpecialtyId: number,
	healthcareProfessionalId: number,
	id?: number,
	professionalSpecialtyId: number
}

export interface ProfessionalSpecialty {
	professionId?: number,
	specialtyId?: number
}

@Component({
	selector: 'app-edit-professions',
	templateUrl: './edit-professions.component.html',
	styleUrls: ['./edit-professions.component.scss']
})

export class EditProfessionsComponent implements OnInit {

	formParent: FormGroup = new FormGroup({});
	professionsTypeahead: TypeaheadOption<ProfessionDto>[] = [];
	initValueTypeaheadProfessions: TypeaheadOption<ProfessionDto>[] = [];
	initValueTypeaheadSpecialties: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
	dividerNewSpecialtiesAndProfessions = false;
	professional: ProfessionalDto;
	specialtiesTypeahead: TypeaheadOption<ClinicalSpecialtyDto>[];
	professionalSpecialtyDtos: HealthcareProfessionalSpecialtyDto[] = [];

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { personId: number; professionalId?: number; ownLicense?: string; specialtyId?: number[], id?: number[], allSpecialties: ClinicalSpecialtyDto[], allProfessions: ProfessionDto[], ownProfessionsAndSpecialties: ProfessionAndSpecialtyDto[] },
		public dialog: MatDialogRef<EditProfessionsComponent>,
	) { }

	ngOnInit(): void {
		this.initFormParent();
		this.professionsTypeahead = this.data.allProfessions.map(d => this.toProfessionDtoTypeahead(d));
		this.specialtiesTypeahead = this.data.allSpecialties.map(d => this.toSpecialtyDtoTypeahead(d));
		this.initOwnSpecialtiesAndProfessions();
	}

	initOwnSpecialtiesAndProfessions(): void {
		if ((this.data.allSpecialties) && (this.data.allProfessions)) {
			this.setOwnSpecialtiesAndProfessions()
		}
	}

	initFormParent(): void {
		this.formParent = new FormGroup({
			license: new FormControl(this.data?.ownLicense ? this.data.ownLicense : null, [Validators.required, Validators.pattern(/^([a-zA-Z1-9])+$/)]),
			specialtiesAndProfessions: new FormArray([], [Validators.required])
		});
	}

	//**carga especialidades | profesiones previas*/
	setOwnSpecialtiesAndProfessions(): void {
		if (this.data?.ownLicense)
			for (let i = 0; i < this.data.ownProfessionsAndSpecialties?.length; i++) {
				const elem = {
					clinicalSpecialtyId: this.data?.ownProfessionsAndSpecialties[i].specialtyId,
					healthcareProfessionalId: (this.data?.professionalId) ? (this.data?.professionalId) : null,
					id: (this.data?.id[i]) ? (this.data?.id[i]) : null,
					professionalSpecialtyId: this.data?.ownProfessionsAndSpecialties[i].professionId
				};
				this.saveInitValueTypeaheadProfessions(i);
				this.saveInitValueTypeaheadSpecialty(i);
				this.addOwnSpecialtiesAndProfessions(elem);

			}
		else
			this.addSpecialtiesAndProfessions();
	}

	setProfession($event: ProfessionDto, pointIndex: number): void {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		const refArray = refprofessionsAndProfessionss.controls;
		const refPosArray = refArray[pointIndex].value;

		if ($event != null) {
			refprofessionsAndProfessionss.at(pointIndex).setValue({
				clinicalSpecialtyId: (refPosArray?.clinicalSpecialtyId) ? (refPosArray.clinicalSpecialtyId) : null,
				healthcareProfessionalId: (this.data?.professionalId) ? (this.data?.professionalId) : null,
				id: null,
				professionalSpecialtyId: $event.id
			});
		}
		else if (refPosArray.clinicalSpecialtyId != null) {
			refprofessionsAndProfessionss.at(pointIndex).setValue({
				clinicalSpecialtyId: refPosArray.clinicalSpecialtyId,
				healthcareProfessionalId: (this.data?.professionalId) ? (this.data?.professionalId) : null,
				id: null,
				professionalSpecialtyId: null,
			});
		}
		else
			this.deleteSpecialtiesAndProfessions(pointIndex);
	}

	setSpeciality($event: SpecialtyDto, pointIndex: number): void {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		const refArray = refprofessionsAndProfessionss.controls;
		const refPosArray = refArray[pointIndex].value;
		if ($event != null) {
			refprofessionsAndProfessionss.at(pointIndex).setValue({
				clinicalSpecialtyId: $event.id,
				healthcareProfessionalId: (this.data?.professionalId) ? (this.data?.professionalId) : null,
				id: null,
				professionalSpecialtyId: (refPosArray?.professionalSpecialtyId) ? (refPosArray.professionalSpecialtyId) : null
			});
		}
		else if (refPosArray.professionalSpecialtyId != null) {
			refprofessionsAndProfessionss.at(pointIndex).setValue({
				clinicalSpecialtyId: null,
				healthcareProfessionalId: (this.data?.professionalId) ? (this.data?.professionalId) : null,
				id: null,
				professionalSpecialtyId: refPosArray.professionalSpecialtyId,
			});
		}
		else
			this.deleteSpecialtiesAndProfessions(pointIndex);
	}

	isDividerNewSpecialtiesAndProfessions(): boolean {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		const refArray = refprofessionsAndProfessionss.controls;
		return (refArray.length > 1) ? (this.dividerNewSpecialtiesAndProfessions = true) : (this.dividerNewSpecialtiesAndProfessions = false);
	}

	/**Guardar valores para mostrar en el app-typeahead*/
	saveInitValueTypeaheadProfessions(index: number): void {
		this.data.allProfessions.forEach((professionalSpecialtyId: ProfessionDto) => {
			if (professionalSpecialtyId.id === this.data?.ownProfessionsAndSpecialties[index].professionId) {
				this.initValueTypeaheadProfessions.push(this.toProfessionDtoTypeahead(professionalSpecialtyId));
			}
		})
	}

	saveInitValueTypeaheadSpecialty(index: number): void {
		this.data.allSpecialties.forEach((speciality: ClinicalSpecialtyDto) => {
			if (speciality.id === this.data?.ownProfessionsAndSpecialties[index].specialtyId) {
				this.initValueTypeaheadSpecialties.push(this.toSpecialtyDtoTypeahead(speciality));
			}
		})
	}

	initFormSpecialtiesAndProfessions(): FormGroup {
		return new FormGroup({
			professionalSpecialtyId: new FormControl(null, [Validators.required]),
			healthcareProfessionalId: new FormControl(null, []),
			id: new FormControl(null, []),
			clinicalSpecialtyId: new FormControl(null, [Validators.required]),
		});
	}

	initOwnFormSpecialtiesAndProfessions(elem: HealthcareProfessionalSpecialtyDto): FormGroup {
		return new FormGroup({
			clinicalSpecialtyId: new FormControl(elem.clinicalSpecialtyId, [Validators.required]),
			healthcareProfessionalId: new FormControl(elem.healthcareProfessionalId, []),
			id: new FormControl(elem?.id ? elem.id : null, []),
			professionalSpecialtyId: new FormControl(elem.professionalSpecialtyId, [Validators.required])
		});
	}

	/**agregar un nuevo combo en blanco */
	addSpecialtiesAndProfessions(): void {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		if (!this.isDisabledAddProfessionAndSpecialty())
			refprofessionsAndProfessionss.push(this.initFormSpecialtiesAndProfessions());
	}

	/**  Agregar profesiones y especialidades que tenga el profesional */
	addOwnSpecialtiesAndProfessions(elem: HealthcareProfessionalSpecialtyDto): void {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		refprofessionsAndProfessionss.push(this.initOwnFormSpecialtiesAndProfessions(elem));
	}

	deleteSpecialtiesAndProfessions(item: number): void {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		const refArray = refprofessionsAndProfessionss.controls;
		if (refArray.length > 0) {
			refprofessionsAndProfessionss.removeAt(item);
			this.initValueTypeaheadProfessions.splice(item, 1);
			this.initValueTypeaheadSpecialties.splice(item, 1);
		}
	}

	getCtrl(key: string, form: FormGroup): any {
		return form.get(key);
	}

	isDisableConfirmButton(): boolean {
		if (this.hasError('required', 'pattern', 'license') || this.isDisabledAddProfessionAndSpecialty() || this.emptySets())
			return true
		return false;
	}

	emptySets(): boolean {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		const refArray = refprofessionsAndProfessionss.controls;
		return (refArray.find(elem =>
			((elem.value.professionalSpecialtyId === null) || (elem.value.clinicalSpecialtyId === null))
		) ? true : false);
	}

	isDisabledAddProfessionAndSpecialty(): boolean {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		const refArray = refprofessionsAndProfessionss.controls;
		const i = refArray.length - 1;
		if (refArray[i])
			return !((refArray[i].value.professionalSpecialtyId) && (refArray[i].value.clinicalSpecialtyId));
		return false;
	}

	private toProfessionDtoTypeahead(professionDto: ProfessionDto): TypeaheadOption<ProfessionDto> {
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

	hasError(type: string, type_two: string, control: string): boolean {
		return (this.formParent.get(control).hasError(type) || this.formParent.get(control).hasError(type_two));
	}

	public save(): void {
		this.buildProfessionalSpecialty();
		const professional: HealthcareProfessionalCompleteDto = this.buildCreateProfessionalDto();
		this.dialog.close(professional);
	}

	private isEqual(e1: HealthcareProfessionalSpecialtyDto, e2: HealthcareProfessionalSpecialtyDto): boolean {
		return ((e1.clinicalSpecialtyId === e2.clinicalSpecialtyId) &&
			(e1.healthcareProfessionalId === e2.healthcareProfessionalId));
	}
	private isNotInArray(elem: HealthcareProfessionalSpecialtyDto): boolean {
		let a = true;
		this.professionalSpecialtyDtos.forEach(e => {
			if (this.isEqual(elem, e))
				a = false;
		});
		return a;
	}

	private buildProfessionalSpecialty(): void {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		const refArray = refprofessionsAndProfessionss.controls;
		refArray.forEach(elem => {
			if (this.isNotInArray(elem.value))
				this.professionalSpecialtyDtos.push(elem.value);
		});
	}

	private buildCreateProfessionalDto(): HealthcareProfessionalCompleteDto {
		return {
			id: (this.data?.professionalId) ? (this.data?.professionalId) : null,
			licenseNumber: this.formParent.get('license').value,
			personId: (this.data?.personId) ? (this.data?.personId) : null,
			professionalSpecialtyDtos: this.professionalSpecialtyDtos
		}
	}

}
