import { Component, OnInit, Inject } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MasterDataInterface } from '../../../api-rest/api-model';
import { MedicalConsultationMasterdataService } from '../../../api-rest/services/medical-consultation-masterdata.service';
import { MEDICAL_ATTENTION } from '../../constants/descriptions';
import { REMOVEATTENTION } from '@core/constants/validation-constants';
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { AppFeature } from "@api-rest/api-model";

@Component({
	selector: 'app-new-attention',
	templateUrl: './new-attention.component.html',
	styleUrls: ['./new-attention.component.scss']
})
export class NewAttentionComponent implements OnInit {

	form: UntypedFormGroup;
	public readonly SPONTANEOUS = MEDICAL_ATTENTION.SPONTANEOUS;
	public medicalAttentionTypes: MasterDataInterface<number>[];
	possibleStartingScheduleHours: Date[];
	possibleEndingScheduleHours: Date[];

	availableForBooking: boolean;
	isEnableOnlineAppointments: boolean = false;
	existsBookeddAppointment = false;
	existsProtectedAppointments = false;
	availableForAppoitmentOnline = false;
	availbleForCareLine = false;

	constructor(
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly medicalConsultationMasterdataService: MedicalConsultationMasterdataService,
		@Inject(MAT_DIALOG_DATA) public data: NewAttentionElements,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.featureFlagService.isActive(AppFeature.BACKOFFICE_MOSTRAR_ABM_RESERVA_TURNOS).subscribe(isEnabled => this.isEnableOnlineAppointments = isEnabled);
	}


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
			medicalAttentionType: [null, Validators.required],
			availableForBooking: [this.data?.availableForBooking],
			protectedAppointmentsAllowed: [this.data?.protectedAppointmentsAllowed],
		});

		this.availableForBooking = this.data.availableForBooking;
		this.availbleForCareLine = this.data?.hasSelectedLinesOfCare ? this.data.protectedAppointmentsAllowed : false;

		this.possibleStartingScheduleHours = this.data.possibleScheduleHours.slice(0, this.data.possibleScheduleHours.length - 1);
		this.filterAppointmentEndingHours();

	}

	onSelectionChanged(): void {
		const medicalAttentionType = this.form.controls.medicalAttentionType.value;

		if (medicalAttentionType.description === MEDICAL_ATTENTION.SPONTANEOUS) {
			this.form.controls.availableForBooking.setValue(false);
			this.form.controls.protectedAppointmentsAllowed.setValue(false);
			this.form.controls.overturnCount.disable();
			this.form.controls.availableForBooking.disable();
		} else {
			this.form.controls.overturnCount.enable();
			this.form.controls.availableForBooking.enable();
		}
	}

	submit() {
		const medicalAttentionType = this.form.controls.medicalAttentionType.value;
		if (this.form.valid) {
			if (medicalAttentionType.description === MEDICAL_ATTENTION.SPONTANEOUS) {
				 this.form.controls.availableForBooking.setValue(false);
				this.form.controls.protectedAppointmentsAllowed.setValue(false);
			}
			else {
				 this.form.value.availableForBooking = this.availableForBooking;
				this.form.value.protectedAppointmentsAllowed = this.availbleForCareLine;
			}
			this.dialogRef.close(this.form.value);
		}
	}

	closeDialog() {
		this.dialogRef.close();
	}

	removeAttention() {
		this.dialogRef.close(REMOVEATTENTION);
	}

	filterAppointmentEndingHours() {
		this.possibleEndingScheduleHours = this.data.possibleScheduleHours.filter(appointmentDate => appointmentDate > this.form.value.startingHour);

		if (this.form.value.endingHour <= this.form.value.startingHour)
			this.form.patchValue({ endingHour: this.possibleEndingScheduleHours[0] });
	}

	changeAvailableForBooking(): void {
		this.availableForBooking = !this.availableForBooking;
	}

	changeAvailbleForCareLine() {
		this.availbleForCareLine = !this.availbleForCareLine;
	}
}

export interface NewAttentionElements {
	start: Date,
	end: Date,
	overturnCount?: number,
	medicalAttentionTypeId?: number,
	isEdit?: boolean,
	possibleScheduleHours: Date[],
	availableForBooking: boolean,
	hasSelectedLinesOfCare: boolean,
	protectedAppointmentsAllowed: boolean,
	diaryId?: number,
	openingHoursId?: number

}
