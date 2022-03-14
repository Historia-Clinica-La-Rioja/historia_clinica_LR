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
	private _possibleStartingScheduleHours;
	private _possibleEndingScheduleHours;
	private _selectedStartingHour = this.data.start;
	private _selectedEndingHour = this.data.end;

	constructor(
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly medicalConsultationMasterdataService: MedicalConsultationMasterdataService,
		@Inject(MAT_DIALOG_DATA) public data: NewAttentionElements
	) {
		this._possibleStartingScheduleHours = data.possibleScheduleHours.slice(0, data.possibleScheduleHours.length - 1);
		this.filterAppointmentEndingHours(this.selectedStartingHour);
	}


	ngOnInit(): void {

		this.medicalConsultationMasterdataService.getMedicalAttention()
			.subscribe(medicalAttentionTypes => {
				this.medicalAttentionTypes = medicalAttentionTypes;
				const medicalAttentionTypeDefaultValue = medicalAttentionTypes.find(medicalAttentionType => medicalAttentionType.id === this.data.medicalAttentionTypeId);
				this.form.controls.medicalAttentionType.setValue(medicalAttentionTypeDefaultValue);
			});

		this.form = this.formBuilder.group({
			overturnCount: [this.data.overturnCount, Validators.min(0)],
			medicalAttentionType: [null, Validators.required]
		});

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
			this.dialogRef.close([this.form.value, this.selectedStartingHour, this.selectedEndingHour]);
		}
	}

	closeDialog() {
		this.dialogRef.close([null, this.selectedStartingHour, this.selectedEndingHour]);
	}

	removeAttention() {
		this.dialogRef.close([REMOVEATTENTION, this.selectedStartingHour, this.selectedEndingHour]);
	}

	filterAppointmentEndingHours(selectedStartingHour: Date){
		this.selectedStartingHour = selectedStartingHour;
		this._possibleEndingScheduleHours = this.data.possibleScheduleHours.filter(appointmentDate => appointmentDate > this.selectedStartingHour);
	}

	set possibleStartingScheduleHours(possibleStartingScheduleHours: Date[]){
		this.possibleStartingScheduleHours = possibleStartingScheduleHours;
	}

	get possibleStartingScheduleHours(): Date[]{
		return this._possibleStartingScheduleHours;
	}

	set possibleEndingScheduleHours(possibleEndingScheduleHours: Date[]){
		this.possibleEndingScheduleHours = possibleEndingScheduleHours;
	}

	get possibleEndingScheduleHours(): Date[]{
		return this._possibleEndingScheduleHours;
	}

	set selectedStartingHour(startingHour: Date){
		this._selectedStartingHour = startingHour;
	}

	get selectedStartingHour(): Date{
		return this._selectedStartingHour;
	}

	set selectedEndingHour(endingHour: Date){
		this._selectedEndingHour = endingHour;
	}

	get selectedEndingHour(): Date{
		return this._selectedEndingHour;
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
