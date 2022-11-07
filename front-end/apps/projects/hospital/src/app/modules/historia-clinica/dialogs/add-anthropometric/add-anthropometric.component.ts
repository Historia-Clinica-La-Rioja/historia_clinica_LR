import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AnthropometricDataDto, EvolutionNoteDto, MasterDataInterface } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { getError, hasError } from '@core/utils/form.utils';
import { anyMatch } from "@core/utils/array.utils";
import { PermissionsService } from "@core/services/permissions.service";

@Component({
	selector: 'app-add-anthropometric',
	templateUrl: './add-anthropometric.component.html',
	styleUrls: ['./add-anthropometric.component.scss']
})
export class AddAnthropometricComponent implements OnInit {

	getError = getError;
	hasError = hasError;

	form: FormGroup;
	loading = false;
	bloodTypes: MasterDataInterface<string>[];

	isNursingEvolutionNote: boolean;

	constructor(
		public dialogRef: MatDialogRef<AddAnthropometricComponent>,
		@Inject(MAT_DIALOG_DATA) public data,
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly permissionsService: PermissionsService
	) {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.isNursingEvolutionNote = !anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PROFESIONAL_DE_SALUD]) && anyMatch<ERole>(userRoles, [ERole.ENFERMERO]);
		})
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			bloodType: [null],
			height: [null, [Validators.min(0), Validators.max(1000), Validators.pattern('^[0-9]+$')]],
			weight: [null, [Validators.min(0), Validators.max(1000), Validators.pattern('^\\d*\\.?\\d+$')]]
		});

		const bloodTypes$ = this.internacionMasterDataService.getBloodTypes();
		bloodTypes$.subscribe(bloodTypes => this.bloodTypes = bloodTypes);
	}

	submit(): void {
		const evolutionNote: EvolutionNoteDto = this.buildEvolutionNote(this.form.value);
		if (evolutionNote) {
			this.loading = true;
			this.evolutionNoteService.createDocument(evolutionNote, this.data.internmentEpisodeId).subscribe(_ => {
				this.snackBarService.showSuccess('internaciones.internacion-paciente.anthropometric-summary.save.SUCCESS');
				const fieldsToUpdate = { bloodType: !!evolutionNote.anthropometricData.bloodType, heightAndWeight: !!evolutionNote.anthropometricData.height || !!evolutionNote.anthropometricData.weight }
				this.dialogRef.close(fieldsToUpdate);
			}, _ => {
				this.snackBarService.showError('internaciones.internacion-paciente.anthropometric-summary.save.ERROR');
				this.loading = false;
			}
			);
		}
	}

	formHasNoValues(formGroupValues: any): boolean {
		return Object.values(formGroupValues).every(el => el === null);
	}

	private buildEvolutionNote(anthropometricDataForm: any): EvolutionNoteDto {
		const anthropometricData: AnthropometricDataDto = this.formHasNoValues(anthropometricDataForm) ? undefined : {
			bloodType: anthropometricDataForm.bloodType ? {
				id: anthropometricDataForm.bloodType.id,
				value: anthropometricDataForm.bloodType.description
			} : undefined,
			height: getValue(anthropometricDataForm.height),
			weight: getValue(anthropometricDataForm.weight),
		};

		return anthropometricData ? { confirmed: true, anthropometricData, isNursingEvolutionNote: this.isNursingEvolutionNote } : undefined;

		function getValue(controlValue: any) {
			return controlValue ? { value: controlValue } : undefined;
		}
	}

}
