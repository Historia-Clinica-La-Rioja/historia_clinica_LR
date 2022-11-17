import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProfessionalLicenseNumberDto } from '@api-rest/api-model';
import { ProfessionalLicenseService } from '@api-rest/services/professional-license.service';
import { ProfessionalSpecialties } from '@pacientes/routes/profile/profile.component';

@Component({
	selector: 'app-edit-license',
	templateUrl: './edit-license.component.html',
	styleUrls: ['./edit-license.component.scss']
})
export class EditLicenseComponent implements OnInit {

	form: FormGroup;
	professionSpecialtiesSinMatriculas: ProfessionalSpecialties[] = [];
	professionsWithLicense: ProfessionalLicenseNumberDto[] = [];
	confirmationValidation = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { personId: number, professionSpecialties: ProfessionalSpecialties[], healthcareProfessionalId: number },
		public dialog: MatDialogRef<EditLicenseComponent>,
		private formBuilder: FormBuilder,
		private readonly professionalLicenseService: ProfessionalLicenseService,

	) {

	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			professionalSpecialties: new FormArray([]),
		});

		if (this.data.healthcareProfessionalId) {
			this.professionalLicenseService.getLicenseNumberByProfessional(this.data.healthcareProfessionalId).subscribe((e: ProfessionalLicenseNumberDto[]) => {
				this.professionsWithLicense = e;
				this.setPreviousLicenses();
			})
		} else {
			this.addCombo();
		}

	}

	private setPreviousLicenses(): void {
		let length = this.professionsWithLicense?.length;

		do {
			this.addCombo();
			length--;
		} while (length > 0);
	}

	private getComboProfessionLicense(elem: ProfessionalLicenseNumberDto): any {
		return {
			id: elem?.id || null,
			licenseNumber: elem.licenseNumber,
			professionalProfessionId: elem.professionalProfessionId,
			typeId: elem.typeId,
			healthcareProfessionalSpecialtyId: elem?.healthcareProfessionalSpecialtyId || null,
			radioButtonOptionSpecialty: !!elem?.healthcareProfessionalSpecialtyId
		}
	}

	private add(): FormGroup {
		return new FormGroup({
			combo: new FormControl(this.professionsWithLicense.length ? this.getComboProfessionLicense(this.professionsWithLicense.shift()) : null, [Validators.required]),
		});
	}

	addCombo(): void {
		const array = this.form.get('professionalSpecialties') as FormArray;
		array.push(this.add());
	}

	getCtrl(key: string, form: FormGroup): any {
		return form.get(key);
	}

	delete(pointIndex: number): void {
		const array = this.form.get('professionalSpecialties') as FormArray;
		array.removeAt(pointIndex)
	}

	isDisableConfirmButton(): boolean {
		const array = this.form.get('professionalSpecialties') as FormArray;
		return array.at(array.length - 1)?.value.combo === null;
	}

	save(): void {
		this.confirmationValidation = !this.form.valid;
		if (this.form.valid) {
			const professional: ProfessionalLicenseNumberDto[] = this.buildCreateProfessionalLicenseNumberDto();
			this.dialog.close(professional);
		}
	}

	private convertToProfessionalLicenseNumberDto(combo: any): ProfessionalLicenseNumberDto {
		return {
			healthcareProfessionalSpecialtyId: combo.healthcareProfessionalSpecialtyId,
			id: combo.id,
			licenseNumber: combo.licenseNumber,
			professionalProfessionId: combo.professionalProfessionId,
			typeId: combo.typeId
		}
	}

	private buildCreateProfessionalLicenseNumberDto(): ProfessionalLicenseNumberDto[] {
		const array = this.form.get('professionalSpecialties') as FormArray;
		const refArray = array.value;
		return refArray.map((e => this.convertToProfessionalLicenseNumberDto(e.combo)))
	}
}
