import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiErrorMessageDto, AppFeature, LicenseDataDto, ProfessionalLicenseNumberDto, ValidatedLicenseDataDto, ValidatedLicenseNumberDto } from '@api-rest/api-model.d';
import { ProfessionalLicenseService } from '@api-rest/services/professional-license.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { processErrors } from '@core/utils/form.utils';
import { ProfessionalSpecialties } from '@pacientes/routes/profile/profile.component';
import { MatriculaService } from '@pacientes/services/matricula.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-edit-license',
	templateUrl: './edit-license.component.html',
	styleUrls: ['./edit-license.component.scss']
})
export class EditLicenseComponent implements OnInit {

	form: UntypedFormGroup;
	professionSpecialtiesSinMatriculas: ProfessionalSpecialties[] = [];
	professionsWithLicense: ProfessionalLicenseNumberDto[] = [];
	isHabilitarValidacionMatriculasSisaEnabled: boolean = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { personId: number, professionSpecialties: ProfessionalSpecialties[], healthcareProfessionalId: number },
		public dialog: MatDialogRef<EditLicenseComponent>,
		private formBuilder: UntypedFormBuilder,
		private readonly professionalLicenseService: ProfessionalLicenseService,
		private readonly licenseNumberService: MatriculaService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly snackBarService: SnackBarService
	) {

	}

	ngOnInit(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_VALIDACION_MATRICULAS_SISA)
			.subscribe((result: boolean) => this.isHabilitarValidacionMatriculasSisaEnabled = result);

		this.form = this.formBuilder.group({
			professionalSpecialties: new UntypedFormArray([]),
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

	private add(): UntypedFormGroup {
		return new UntypedFormGroup({
			combo: new UntypedFormControl(this.professionsWithLicense.length ? this.getComboProfessionLicense(this.professionsWithLicense.shift()) : null, [Validators.required]),
		});
	}

	addCombo(): void {
		const array = this.form.get('professionalSpecialties') as UntypedFormArray;
		array.push(this.add());
	}

	getCtrl(key: string, form: UntypedFormGroup): any {
		return form.get(key);
	}

	isDisableConfirmButton(): boolean {
		const array = this.form.get('professionalSpecialties') as UntypedFormArray;
		return array.at(array.length - 1)?.value.combo === null;
	}

	save(): void {
		if (this.form.valid) {
			const professional: ProfessionalLicenseNumberDto[] = this.buildCreateProfessionalLicenseNumberDto();
			this.dialog.close(professional);
		}
	}

	validateLicenseNumbers() {
		if (! this.isHabilitarValidacionMatriculasSisaEnabled)
			return this.save();

		const licenseData: LicenseDataDto[] = this.form.controls.professionalSpecialties.value.map(value => ({ licenseNumber: value.combo?.licenseNumber, licenseType: value.combo?.typeId }));
		this.professionalLicenseService.validateLicenseNumber(this.data.healthcareProfessionalId, licenseData)
			.subscribe((licensesData: ValidatedLicenseDataDto[]) => {
				this.licenseNumberService.setLicenseNumbers(licensesData);

				if (licensesData.some(combo => !combo.validLicenseNumber || !combo.validLicenseType)) return;

				this.save();
			},
			(error: ApiErrorMessageDto) => {
				processErrors(error, (msg) => this.snackBarService.showError(msg));
			});
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
		const array = this.form.get('professionalSpecialties') as UntypedFormArray;
		const refArray = array.value;
		return refArray.map((e => this.convertToProfessionalLicenseNumberDto(e.combo)))
	}
}
