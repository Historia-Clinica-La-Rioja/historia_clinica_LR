import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EvolutionNoteDto } from '@api-rest/api-model';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-add-vital-signs',
	templateUrl: './add-vital-signs.component.html',
	styleUrls: ['./add-vital-signs.component.scss']
})
export class AddVitalSignsComponent implements OnInit {

	form: FormGroup;
	loading = false;

	constructor(
		public dialogRef: MatDialogRef<AddVitalSignsComponent>,
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

	setVitalSignEffectiveTime(newEffectiveTime: Moment, formField: string): void {
		(this.form.controls[formField] as FormGroup).controls.effectiveTime.setValue(newEffectiveTime);
	}

	formHasNoValues(vitalSignsForm): boolean {
		return ((vitalSignsForm.bloodOxygenSaturation.value === null)
			 && (vitalSignsForm.diastolicBloodPressure.value === null)
			 && (vitalSignsForm.heartRate.value === null)
			 && (vitalSignsForm.respiratoryRate.value === null)
			 && (vitalSignsForm.systolicBloodPressure.value === null)
			 && (vitalSignsForm.temperature.value === null));
	}

	submit() {
		const evolutionNote = this.buildEvolutionNote(this.form.value);
		if (evolutionNote) {
			this.loading = true;
			this.evolutionNoteService.createDocument(evolutionNote, this.data.internmentEpisodeId).subscribe(_ => {
					this.snackBarService.showSuccess('internaciones.internacion-paciente.vital-signs-summary.save.SUCCESS');
					this.dialogRef.close(true);
				}, error => {
				const errorMessages = error.errors.join() ? error.errors.join(', ')
					: 'internaciones.internacion-paciente.vital-signs-summary.save.ERROR';
				this.snackBarService.showError(errorMessages);
				this.loading = false;
				}
			);
		}

	}

	private buildEvolutionNote(vitalSignsForm): EvolutionNoteDto {
		const vitalSigns = isNull(vitalSignsForm) ? undefined : {
			bloodOxygenSaturation: getEffectiveValue(vitalSignsForm.bloodOxygenSaturation),
			diastolicBloodPressure: getEffectiveValue(vitalSignsForm.diastolicBloodPressure),
			heartRate: getEffectiveValue(vitalSignsForm.heartRate),
			respiratoryRate: getEffectiveValue(vitalSignsForm.respiratoryRate),
			systolicBloodPressure: getEffectiveValue(vitalSignsForm.systolicBloodPressure),
			temperature: getEffectiveValue(vitalSignsForm.temperature)
		};

		return vitalSigns ? { confirmed: true, vitalSigns } : undefined;

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every((el: { value: number, effectiveTime: Moment }) => el.value === null);
		}

		function getEffectiveValue(controlValue: any) {
			return controlValue.value ? { value: controlValue.value, effectiveTime: controlValue.effectiveTime } : undefined;
		}
	}

}
