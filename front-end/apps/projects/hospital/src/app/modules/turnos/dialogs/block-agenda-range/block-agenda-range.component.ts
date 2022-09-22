import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DateDto, MasterDataInterface, TimeDto } from '@api-rest/api-model';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { DiaryService } from '@api-rest/services/diary.service';
import { hasError } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { AppointmentBlockMotivesFacadeService } from '@turnos/services/appointment-block-motives-facade.service';

@Component({
	selector: 'app-block-agenda-range',
	templateUrl: './block-agenda-range.component.html',
	styleUrls: ['./block-agenda-range.component.scss'],
})
export class BlockAgendaRangeComponent implements OnInit {

	hasError = hasError;
	blockForm: FormGroup;
	unblockForm: FormGroup;
	appointmentBlockMotives: MasterDataInterface<number>[];

	constructor(
		private readonly formBuilder: FormBuilder,
		@Inject(MAT_DIALOG_DATA) public data: any,
		private dialogRef: MatDialogRef<BlockAgendaRangeComponent>,
		private readonly diaryService: DiaryService,
		private snackBarService: SnackBarService,
		private appointmentBlockMotivesFacadeService: AppointmentBlockMotivesFacadeService
	) { }

	ngOnInit(): void {
		this.blockForm = this.formBuilder.group({
			control: [null, Validators.required],
			blockMotive: [null, Validators.required],
		});

		this.unblockForm = this.formBuilder.group({
			control: [null, Validators.required]
		});
		this.appointmentBlockMotives = this.appointmentBlockMotivesFacadeService.getAllAppointmentBlockMotives();
	}

	block() {
		const values = this.getValues(this.blockForm);
		this.diaryService.blockAgendaRangeDateTime(this.data.selectedAgenda.id, values)
			.subscribe(_ => {
				this.snackBarService.showSuccess('Rango horario bloqueado');
				this.dialogRef.close(true);
				},
				error => {
					this.snackBarService.showError(error.errors[0]);
				}
			);

	}

	unblock() {
		const values = this.getValues(this.unblockForm);
		this.diaryService.unblock(this.data.selectedAgenda.id, values)
			.subscribe(_ => {
				this.snackBarService.showSuccess('Rango horario desbloqueado');
				this.dialogRef.close(true);
			},
			error => {
				this.snackBarService.showError(error.message);
			}
		);
	}

	private getValues(form) {
		const value = form.value.control;
		const initDateDto: DateDto = dateToDateDto(value.initDate.toDate());
		const endDateDto: DateDto = dateToDateDto(value.endDate.toDate());
		const init = value.init;
		const end = value.end;
		const appointmentBlockMotiveId = form.value.blockMotive || null;
		const fullBlock = value.fullBlock;
		return { initDateDto, endDateDto, init, end, appointmentBlockMotiveId, fullBlock };
	}

	closeDialog() {
		this.dialogRef.close();
	}

}
