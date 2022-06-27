import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DateDto, TimeDto } from '@api-rest/api-model';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { DiaryService } from '@api-rest/services/diary.service';
import { hasError } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-block-agenda-range',
	templateUrl: './block-agenda-range.component.html',
	styleUrls: ['./block-agenda-range.component.scss'],
})
export class BlockAgendaRangeComponent implements OnInit {

	hasError = hasError;
	blockForm: FormGroup;
	unblockForm: FormGroup;

	constructor(
		private readonly formBuilder: FormBuilder,
		@Inject(MAT_DIALOG_DATA) public data: any,
		private dialogRef: MatDialogRef<BlockAgendaRangeComponent>,
		private readonly diaryService: DiaryService,
		private snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.blockForm = this.formBuilder.group({
			control: [null]
		})

		this.unblockForm = this.formBuilder.group({
			control: [null]
		})
	}

	block() {
		const values = this.getValues(this.blockForm);
		this.diaryService.blockAgendaRangeDateTime(this.data.agendaId, values)
			.subscribe(_ => {
				this.snackBarService.showSuccess('Rango horario bloqueado');
				this.dialogRef.close(true);
				},
				error => {
					this.snackBarService.showError(error.message);
				}
			);

	}

	unblock() {
		const values = this.getValues(this.unblockForm);
		this.diaryService.unblock(this.data.agendaId, values)
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
		const date = value.date.toDate();
		const dateDto: DateDto = dateToDateDto(date);
		const init = this.toTimeDto(value.init);
		const end = this.toTimeDto(value.end);
		return { dateDto, init, end };
	}

	private toTimeDto(time: string): TimeDto {
		const spredTime = time.split(':');
		return { hours: Number(spredTime[0]), minutes: Number(spredTime[1]) }
	}
}
