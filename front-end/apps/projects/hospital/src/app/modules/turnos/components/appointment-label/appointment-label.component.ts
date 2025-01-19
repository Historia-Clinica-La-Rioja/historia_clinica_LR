import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { AppointmentDto, DiaryLabelDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { DiaryLabelService } from '@api-rest/services/diary-label.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { getDiaryLabel } from '@turnos/constants/appointment';

@Component({
	selector: 'app-appointment-label',
	templateUrl: './appointment-label.component.html',
	styleUrls: ['./appointment-label.component.scss']
})
export class AppointmentLabelComponent implements OnInit {

	@Input() set appointment(appointment: AppointmentDto) {
		if (!appointment) return;

		this.appointmentId = appointment.id;
		this.diaryLabel = appointment.diaryLabelDto;
		if (this.diaryLabel) {
			const diaryLabel: DiaryLabelDto = this.buildDiaryLabel();
			this.setSelectedDiaryLabel(diaryLabel);
		}
	}
	@Input() diaryId: number;
	appointmentId: number;
	diaryLabel: DiaryLabelDto;
	formLabel: UntypedFormGroup;
	isLabelSelectorVisible: boolean = false;
	selectedDiaryLabelId: number;
	diaryLabels: DiaryLabelDto[] = [];


	constructor(private readonly appointmentService: AppointmentsService,
				private readonly snackBarService: SnackBarService,
				private readonly diaryLabelService: DiaryLabelService) { }

	ngOnInit(): void {
		this.formLabel = new UntypedFormGroup({
			color: new UntypedFormControl(''),
			description: new UntypedFormControl('')
		});
		this.setDiaryLabels();
	}

	updateLabel(value?: DiaryLabelDto) {
		this.appointmentService.setAppointmentLabel(value ? value.id: null, this.appointmentId)
		.subscribe({
			next: (result: boolean) => {
				if (result) {
					this.snackBarService.showSuccess('turnos.appointment.labels.UPDATED_LABEL');
					this.setSelectedDiaryLabel(value);
				}
			},
			error: () => this.snackBarService.showError('turnos.appointment.labels.ERROR_UPDATE_LABEL')
		})
	}

	private buildDiaryLabel(): DiaryLabelDto {
		return {
			colorId: this.diaryLabel.colorId,
			description: this.diaryLabel.description,
			id: this.diaryLabel.id,
			diaryId: this.diaryLabel.diaryId
		}
	}

	private setSelectedDiaryLabel(diaryLabel?: DiaryLabelDto) {
		this.isLabelSelectorVisible = diaryLabel ? true : false;
		this.selectedDiaryLabelId = diaryLabel ? diaryLabel.id: null;
		this.formLabel.get('color').setValue(diaryLabel ? getDiaryLabel(diaryLabel.colorId): null);
		this.formLabel.get('description').setValue(diaryLabel ? diaryLabel.description: null);
	}

	private setDiaryLabels() {
		this.diaryLabelService.getLabelsByDiary(this.diaryId)
			.subscribe((result: DiaryLabelDto[]) => this.diaryLabels = result);
	}
}
