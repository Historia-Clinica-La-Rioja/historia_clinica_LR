import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DietDto } from "@api-rest/api-model";
import { EIndicationStatus, EIndicationType } from "@api-rest/api-model";
import { getMonth, getYear, isSameDay, isToday } from "date-fns";
import { dateDtoToDate } from "@api-rest/mapper/date-dto.mapper";
import { openConfirmDialog } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";

@Component({
	selector: 'app-diet',
	templateUrl: './diet.component.html',
	styleUrls: ['./diet.component.scss']
})

export class DietComponent implements OnInit {
	form: FormGroup;
	indicationDate: Date;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { entryDate:Date, actualDate:Date, patientId: number, professionalId: number},
		private readonly formBuilder: FormBuilder,
		private dialogRef: MatDialogRef<DietComponent>,
		private readonly dialog: MatDialog,
	) {	}

	ngOnInit(): void {
		this.indicationDate = this.data.actualDate;
		this.form = this.formBuilder.group({
			description: [null, [Validators.required]],
		});
	}

	save(): void {
		if (this.form.valid) {
			const diet = this.toDietDto();
			const dietIndicationDate = dateDtoToDate(diet.indicationDate);
			if (!isToday(dietIndicationDate) && isSameDay(dietIndicationDate, this.data.actualDate)) {
				openConfirmDialog(this.dialog, dietIndicationDate).subscribe(confirm => {
						if (confirm === true) {
							this.dialogRef.close(diet);
						}
					});
			}
			else
				this.dialogRef.close(diet);
		}
	}

	private toDietDto(): DietDto {
		return {
			id: 0,
			patientId: this.data.patientId,
			type: EIndicationType.DIET,
			status: EIndicationStatus.INDICATED,
			professionalId: this.data.professionalId,
			createdBy: null,
			indicationDate: {
				year: getYear(this.indicationDate),
				month: getMonth(this.indicationDate) + 1,
				day: this.indicationDate.getDate()
			},
			createdOn: null,
			description: this.form.value.description
		}
	}

	setIndicationDate(d: Date){
		this.indicationDate = d;
	}
}
