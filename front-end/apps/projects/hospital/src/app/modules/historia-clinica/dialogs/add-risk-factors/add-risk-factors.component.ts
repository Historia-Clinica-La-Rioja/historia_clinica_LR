import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EvolutionNoteDto } from '@api-rest/api-model';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-add-risk-factors',
	templateUrl: './add-risk-factors.component.html',
	styleUrls: ['./add-risk-factors.component.scss']
})
export class AddRiskFactorsComponent implements OnInit {

	form: FormGroup;
	loading = false;

	constructor(
		public dialogRef: MatDialogRef<AddRiskFactorsComponent>,
		@Inject(MAT_DIALOG_DATA) public data,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			heartRate: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			respiratoryRate: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			temperature: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			bloodOxygenSaturation: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			systolicBloodPressure: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
			diastolicBloodPressure: this.formBuilder.group({
				value: [null, Validators.min(0)],
				effectiveTime: [newMoment()],
			}),
		});
	}

	setRiskFactorEffectiveTime(newEffectiveTime: Moment, formField: string): void {
		(this.form.controls[formField] as FormGroup).controls.effectiveTime.setValue(newEffectiveTime);
	}

	formHasNoValues(riskFactorsForm): boolean {
		return ((riskFactorsForm.bloodOxygenSaturation.value === null)
			&& (riskFactorsForm.diastolicBloodPressure.value === null)
			&& (riskFactorsForm.heartRate.value === null)
			&& (riskFactorsForm.respiratoryRate.value === null)
			&& (riskFactorsForm.systolicBloodPressure.value === null)
			&& (riskFactorsForm.temperature.value === null));
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
			temperature: getEffectiveValue(riskFactorsForm.temperature)
		};

		return riskFactors ? { confirmed: true, riskFactors } : undefined;

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every((el: { value: number, effectiveTime: Moment }) => el.value === null);
		}

		function getEffectiveValue(controlValue: any) {
			return controlValue.value ? { value: controlValue.value, effectiveTime: controlValue.effectiveTime } : undefined;
		}
	}

}
