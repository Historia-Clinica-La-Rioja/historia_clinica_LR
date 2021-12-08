import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { ClinicalSpecialtyDto, HealthcareProfessionalCompleteDto, HealthcareProfessionalSpecialtyDto, ProfessionalDto } from '@api-rest/api-model';
import { ProfessionalService } from '@api-rest/services/professional.service';
import { SpecialtyService } from '@api-rest/services/specialty.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

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

	public formParent: FormGroup = new FormGroup({});
	professionsTypeahead: TypeaheadOption<ProfessionDto>[] = [];
	allProfessions: ProfessionDto[] = [];
	allSpecialties: ClinicalSpecialtyDto[] = [];
	initValueTypeaheadProfessions: TypeaheadOption<ProfessionDto>[] = [];
	initValueTypeaheadProfessionsSimple: ProfessionDto[] = [];
	initValueTypeaheadSpecialties: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
	dividerNewSpecialtiesAndProfessions = false;
	professional: ProfessionalDto;
	specialtiesTypeahead: TypeaheadOption<ClinicalSpecialtyDto>[];
	checkSpeciality: boolean = false;
	checkProfession: boolean = false;
	showAddProfessionsAndSpecialty: boolean = false;
	professionalSpecialtyDtos: HealthcareProfessionalSpecialtyDto[] = [];
	enabledConfirmButton: boolean = true;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { personId: number; professionalId?: number; ownLicense?: string; ownSpecialties?: number[], ownProfessions?: number[], id?: number[] },
		public dialog: MatDialogRef<EditProfessionsComponent>,
		private readonly professionalService: ProfessionalService,
		private readonly specialtyService: SpecialtyService,
		private readonly snackBarService: SnackBarService
	) { }

	ngOnInit(): void {

		this.initFormParent();

		this.professionalService.getList().subscribe((professions: ProfessionDto[]) => {
			this.allProfessions = professions;
			this.professionsTypeahead = this.allProfessions.map(d => this.toProfessionDtoTypeahead(d));
			this.specialtyService.getAll().subscribe((specialities: ClinicalSpecialtyDto[]) => {
				this.allSpecialties = specialities;
				this.specialtiesTypeahead = this.allSpecialties.map(d => this.toSpecialtyDtoTypeahead(d));
				this.initOwnSpecialtiesAndProfessions();
			});
		});

	}

	initOwnSpecialtiesAndProfessions(): void {
		if ((this.allSpecialties) && (this.allProfessions)) {
			this.setOwnSpecialtiesAndProfessions()
		}
	};

	initFormParent(): void {
		this.formParent = new FormGroup({
			license: new FormControl(this.data?.ownLicense ? this.data.ownLicense : null, [Validators.required]),
			specialtiesAndProfessions: new FormArray([], [Validators.required]),
		});
	}

	//**carga especialidades | profesiones previas*/
	setOwnSpecialtiesAndProfessions(): void {
		if (this.data?.ownLicense)
			for (let i = 0; i < this.data.ownProfessions.length; i++) {
				const elem = {
					clinicalSpecialtyId: this.data?.ownSpecialties[i],
					healthcareProfessionalId: (this.data?.professionalId) ? (this.data?.professionalId) : null,
					id: (this.data?.id[i]) ? (this.data?.id[i]) : null,
					professionalSpecialtyId: this.data?.ownProfessions[i]
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
		else {
			refprofessionsAndProfessionss.at(pointIndex).setValue({
				clinicalSpecialtyId: refPosArray.clinicalSpecialtyId,
				healthcareProfessionalId: (this.data?.professionalId) ? (this.data?.professionalId) : null,
				id: null,
				professionalSpecialtyId: null,
			});
		}
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
		else {
			refprofessionsAndProfessionss.at(pointIndex).setValue({
				clinicalSpecialtyId: null,
				healthcareProfessionalId: (this.data?.professionalId) ? (this.data?.professionalId) : null,
				id: null,
				professionalSpecialtyId: refPosArray.professionalSpecialtyId,
			});
		}
	}

	isDividerNewSpecialtiesAndProfessions(): boolean {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		const refArray = refprofessionsAndProfessionss.controls;
		return (refArray.length > 1) ? (this.dividerNewSpecialtiesAndProfessions = true) : (this.dividerNewSpecialtiesAndProfessions = false);
	}

	/**Guardar valores para mostrar en el app-typeahead*/
	saveInitValueTypeaheadProfessions(index: number): void {
		this.allProfessions.forEach((professionalSpecialtyId: ProfessionDto) => {
			if (professionalSpecialtyId.id === this.data?.ownProfessions[index]) {
				this.initValueTypeaheadProfessions.push(this.toProfessionDtoTypeahead(professionalSpecialtyId));
			}
		})
	}

	saveInitValueTypeaheadSpecialty(index: number): void {
		this.allSpecialties.forEach((speciality: ClinicalSpecialtyDto) => {
			if (speciality.id === this.data?.ownSpecialties[index]) {
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
		if (this.hasError('required', 'license') || this.isDisabledAddProfessionAndSpecialty())
			return true
		return false;
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

	hasError(type: string, control: string): boolean {
		return this.formParent.get(control).hasError(type);
	}

	public save(): void {

		this.buildProfessionalSpecialty();
		const professional: HealthcareProfessionalCompleteDto = this.buildCreateProfessionalDto();
		this.professionalSpecialtyDtos = [];

		if (this.data?.professionalId === undefined) {

			this.professionalService.addProfessional(professional).subscribe(_ => {
				this.snackBarService.showSuccess('pacientes.edit_professions.messages.SUCCESS');
				this.enabledConfirmButton = false;
				this.dialog.close(true);
			},
				error => {
					error?.text ?
						this.snackBarService.showError(error.text) : this.snackBarService.showError('pacientes.edit_professions.messages.ERROR');
				})
		}
		else {
			this.professionalService.editProfessional(this.data?.professionalId, professional).subscribe(_ => {
				this.snackBarService.showSuccess('pacientes.edit_professions.messages.SUCCESS');
				this.enabledConfirmButton = false;
				this.dialog.close(true);
			},
				error => {
					error?.text ?
						this.snackBarService.showError(error.text) : this.snackBarService.showError('pacientes.edit_professions.messages.ERROR');
				}
			);
		}
	}

	private buildProfessionalSpecialty(): void {
		const refprofessionsAndProfessionss = this.formParent.get('specialtiesAndProfessions') as FormArray;
		const refArray = refprofessionsAndProfessionss.controls;
		refArray.forEach(e => this.professionalSpecialtyDtos.push(e.value));
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
