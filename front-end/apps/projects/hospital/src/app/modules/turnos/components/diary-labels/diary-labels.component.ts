import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormArray, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { DiaryLabelDto } from '@api-rest/api-model';
import { DiaryLabelService } from '@api-rest/services/diary-label.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { COLOR, DIARY_LABEL_COLORS, getDiaryLabel } from '@turnos/constants/appointment';

@Component({
	selector: 'app-diary-labels',
	templateUrl: './diary-labels.component.html',
	styleUrls: ['./diary-labels.component.scss']
})
export class DiaryLabelsComponent implements OnInit {

	form: UntypedFormGroup;
	colorList: COLOR[] = Object.assign([], DIARY_LABEL_COLORS);
	diaryLabels: DiaryLabelDto[] = [];
	@Input() diaryId: number;
	@Output() labelsArray = new EventEmitter<any>();

	constructor(private dialog: MatDialog,
				private readonly diaryLabelService: DiaryLabelService) { }

	ngOnInit(): void {
		this.form = new UntypedFormGroup({
			labels: new UntypedFormArray([])
		});
		this.setDiaryLabels();
		this.form.controls.labels.valueChanges.subscribe(_ => this.labelsArray.emit(this.buildDiaryLabelDtoList()));
	}

	createLabel(color: COLOR, description?: string, id?: number) {
		this.colorList.splice(this.colorList.findIndex((c: COLOR) => c.id === color.id), 1);
		this.addCombo(color, description, id);
	}

	addCombo(color: COLOR, description?: string, id?: number): void {
		const array = this.form.get('labels') as UntypedFormArray;
		array.push(this.add(color, description, id));
	}

	
	getControl(key: string): any {
		return this.form.get(key);
	}

	removeLabel(index: number, color: COLOR) {
		this.dialog.open(DiscardWarningComponent, {
			data: {
				title: 'turnos.agenda-setup.DELETE_LABEL',
				content: 'turnos.agenda-setup.DELETE_SUBTITLE',
				contentBold: 'turnos.agenda-setup.DELETE_CONFIRMATION',
				okButtonLabel: 'turnos.agenda-setup.OK_BUTTON_DELETE',
				cancelButtonLabel: 'turnos.agenda-setup.CANCEL_BUTTON_DELETE',
				okBottonColor: 'warn',
				errorMode: true,
				buttonClose: true
			}
		}).afterClosed()
		.subscribe((result: boolean) => {
			if (result) {
				const labelsControl = this.form.get("labels") as UntypedFormArray;
				labelsControl.removeAt(index);
				this.colorList.push(color);
			}
		})
	}

	private buildDiaryLabelDtoList(): DiaryLabelDto[] {
		const diaryLabelList: DiaryLabelDto[] = [];
		this.form.controls.labels.value.forEach(value => {
			if (value.combo.description != null && value.combo.description.trim() != '') {
				diaryLabelList.push({
					id: value.combo.id,
					colorId: value.combo.color?.id,
					description: value.combo.description,
					diaryId: this.diaryId
				});
			}
		});
		return diaryLabelList;
	}

	private setDiaryLabels() {
		if (this.diaryId === null) return;

		this.diaryLabelService.getLabelsByDiary(this.diaryId)
			.subscribe((result: DiaryLabelDto[]) => {
				this.diaryLabels = result;
				this.diaryLabels.forEach((diaryLabel: DiaryLabelDto) => {
					const color: COLOR = {
						id: diaryLabel.colorId,
						color: getDiaryLabel(diaryLabel.colorId).color
					}
					this.createLabel(color, diaryLabel.description, diaryLabel.id);
				});
			});
	}

	private add(color: COLOR, description?: string, id?: number): UntypedFormGroup {
		return new UntypedFormGroup({
			combo: new UntypedFormControl({
				id: id ? id : null,
				color,
				description: description ? description : null,
			}),
		});
	}
}
