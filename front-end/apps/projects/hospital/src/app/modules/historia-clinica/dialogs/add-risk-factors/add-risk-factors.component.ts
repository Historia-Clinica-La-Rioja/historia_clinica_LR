import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Moment } from 'moment';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EvolutionNoteDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { anyMatch } from "@core/utils/array.utils";
import { PermissionsService } from "@core/services/permissions.service";

@Component({
	selector: 'app-add-risk-factors',
	templateUrl: './add-risk-factors.component.html',
	styleUrls: ['./add-risk-factors.component.scss']
})
export class AddRiskFactorsComponent implements OnInit {

	form: FormGroup;
	loading = false;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	isNursingEvolutionNote: boolean;

	constructor(
		public dialogRef: MatDialogRef<AddRiskFactorsComponent>,
		@Inject(MAT_DIALOG_DATA) public data,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly formBuilder: FormBuilder,
		private readonly translateService: TranslateService,
		private readonly permissionsService: PermissionsService
	) {
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(formBuilder, translateService);
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.isNursingEvolutionNote = !anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PROFESIONAL_DE_SALUD]) && anyMatch<ERole>(userRoles, [ERole.ENFERMERO]);
		})
	}

	ngOnInit(): void {
		this.form = this.factoresDeRiesgoFormService.getForm();
	}

	formHasNoValues(riskFactorsForm): boolean {
		return ((riskFactorsForm.bloodOxygenSaturation.value === null)
			&& (riskFactorsForm.diastolicBloodPressure.value === null)
			&& (riskFactorsForm.heartRate.value === null)
			&& (riskFactorsForm.respiratoryRate.value === null)
			&& (riskFactorsForm.systolicBloodPressure.value === null)
			&& (riskFactorsForm.temperature.value === null)
			&& (riskFactorsForm.bloodGlucose.value === null)
			&& (riskFactorsForm.glycosylatedHemoglobin.value === null)
			&& (riskFactorsForm.cardiovascularRisk.value === null));
	}

	submit() {
		const evolutionNote = this.buildEvolutionNote(this.form.value);
		if (evolutionNote) {
			this.loading = true;
			this.evolutionNoteService.createDocument(evolutionNote, this.data.internmentEpisodeId).subscribe(_ => {
				this.snackBarService.showSuccess('internaciones.internacion-paciente.risk-factors-summary.save.SUCCESS');
				this.dialogRef.close(true);
			}, error => {
				const errorMessages = error.errors.join() ? error.errors.join(', ')
					: 'internaciones.internacion-paciente.risk-factors-summary.save.ERROR';
				this.snackBarService.showError(errorMessages);
				this.loading = false;
			}
			);
		}

	}

	private buildEvolutionNote(riskFactorsForm): EvolutionNoteDto {
		const riskFactors = isNull(riskFactorsForm) ? undefined : {
			bloodOxygenSaturation: getEffectiveValue(riskFactorsForm.bloodOxygenSaturation),
			diastolicBloodPressure: getEffectiveValue(riskFactorsForm.diastolicBloodPressure),
			heartRate: getEffectiveValue(riskFactorsForm.heartRate),
			respiratoryRate: getEffectiveValue(riskFactorsForm.respiratoryRate),
			systolicBloodPressure: getEffectiveValue(riskFactorsForm.systolicBloodPressure),
			temperature: getEffectiveValue(riskFactorsForm.temperature),
			bloodGlucose: getEffectiveValue(riskFactorsForm.bloodGlucose),
			glycosylatedHemoglobin: getEffectiveValue(riskFactorsForm.glycosylatedHemoglobin),
			cardiovascularRisk: getEffectiveValue(riskFactorsForm.cardiovascularRisk)
		};

		return riskFactors ? { confirmed: true, riskFactors, isNursingEvolutionNote: this.isNursingEvolutionNote } : undefined;

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every((el: { value: number, effectiveTime: Moment }) => el.value === null);
		}

		function getEffectiveValue(controlValue: any) {
			return controlValue.value ? { value: controlValue.value, effectiveTime: controlValue.effectiveTime } : undefined;
		}
	}

}
