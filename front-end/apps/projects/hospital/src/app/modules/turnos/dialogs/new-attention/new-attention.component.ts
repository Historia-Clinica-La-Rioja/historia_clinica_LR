import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MasterDataInterface } from '../../../api-rest/api-model';
import { MedicalConsultationMasterdataService } from '../../../api-rest/services/medical-consultation-masterdata.service';
import { MEDICAL_ATTENTION } from '../../constants/descriptions';
import { REMOVEATTENTION } from '@core/constants/validation-constants';

@Component({
	selector: 'app-new-attention',
	templateUrl: './new-attention.component.html',
	styleUrls: ['./new-attention.component.scss']
})
export class NewAttentionComponent implements OnInit {

	form: FormGroup;
	public readonly SPONTANEOUS = MEDICAL_ATTENTION.SPONTANEOUS;
	public medicalAttentionTypes: MasterDataInterface<number>[];
	possibleStartingScheduleHours: Date[];
	possibleEndingScheduleHours: Date[];

	constructor(
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly medicalConsultationMasterdataService: MedicalConsultationMasterdataService,
		@Inject(MAT_DIALOG_DATA) public data: NewAttentionElements
	) { }


	ngOnInit(): void {

		this.medicalConsultationMasterdataService.getMedicalAttention()
			.subscribe(medicalAttentionTypes => {
				this.medicalAttentionTypes = medicalAttentionTypes;
				const medicalAttentionTypeDefaultValue = medicalAttentionTypes.find(medicalAttentionType => medicalAttentionType.id === this.data.medicalAttentionTypeId);
				this.form.controls.medicalAttentionType.setValue(medicalAttentionTypeDefaultValue);
			});

		this.form = this.formBuilder.group({
			startingHour: [this.data.start, Validators.required],
			endingHour: [this.data.end, Validators.required],
			overturnCount: [this.data.overturnCount, Validators.min(0)],
			medicalAttentionType: [null, Validators.required]
		});

		this.possibleStartingScheduleHours = this.data.possibleScheduleHours.slice(0, this.data.possibleScheduleHours.length - 1);
		this.filterAppointmentEndingHours();
	}

	onSelectionChanged(): void {
		const medicalAttentionType = this.form.controls.medicalAttentionType.value;

		if (medicalAttentionType.description === MEDICAL_ATTENTION.SPONTANEOUS) {
			this.form.controls.overturnCount.disable();
		} else {
			this.form.controls.overturnCount.enable();
		}
	}

	submit() {
		if (this.form.valid) {
			this.dialogRef.close(this.form.value);
		}
	}

	closeDialog() {
		this.dialogRef.close();
	}

	removeAttention() {
		this.dialogRef.close(REMOVEATTENTION);
	}

	filterAppointmentEndingHours(){
		this.possibleEndingScheduleHours = this.data.possibleScheduleHours.filter(appointmentDate => appointmentDate > this.form.value.startingHour);

		if (this.form.value.endingHour <= this.form.value.startingHour)
			this.form.patchValue({ endingHour: this.possibleEndingScheduleHours[0] });
	}

}

export interface NewAttentionElements{
	start: Date,
	end: Date,
	overturnCount?: number,
	medicalAttentionTypeId?: number,
	isEdit?: boolean,
	possibleScheduleHours: Date[]
}
