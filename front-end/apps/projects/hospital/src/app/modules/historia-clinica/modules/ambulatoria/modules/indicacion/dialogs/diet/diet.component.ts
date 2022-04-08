import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { DietDto } from "@api-rest/api-model";
import { EIndicationStatus, EIndicationType } from "@api-rest/api-model";
import { getMonth, getYear } from "date-fns";

@Component({
	selector: 'app-diet',
	templateUrl: './diet.component.html',
	styleUrls: ['./diet.component.scss']
})

export class DietComponent implements OnInit {
	form: FormGroup;
	indicationDate: Date;

	constructor(
		private readonly formBuilder: FormBuilder,
		private dialogRef: MatDialogRef<DietComponent>
	) {	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			description: [null, [Validators.required]],
		});
	}

	save(): void {
		if (this.form.valid) {
			const diet = this.toDietDto();
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
}
