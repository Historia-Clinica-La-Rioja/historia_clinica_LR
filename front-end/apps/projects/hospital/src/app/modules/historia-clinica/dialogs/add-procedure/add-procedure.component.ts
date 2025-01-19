import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatRadioChange } from '@angular/material/radio';
import { ServiceRequestCategoryDto, SnomedDto } from '@api-rest/api-model';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { STUDY_STATUS_ENUM } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { COMPLETE_NOW, CreateOrderService } from '@historia-clinica/services/create-order.service';

@Component({
	selector: 'app-add-procedure',
	templateUrl: './add-procedure.component.html',
	styleUrls: ['./add-procedure.component.scss']
})

export class AddProcedureComponent implements OnInit {
	studyCategoryOptions = [];
	study_status = STUDY_STATUS_ENUM;
	completeNow = COMPLETE_NOW;
	radioControl : FormGroup<RadioControl>;
	isLinear = false;

	constructor(
		public dialogRef: MatDialogRef<AddProcedureComponent>,
		@Inject(MAT_DIALOG_DATA) public readonly data: CreateOrder,
		private requestMasterDataService: RequestMasterDataService,

	) {
		this.radioControl = new FormGroup<RadioControl>({
			controlButton: new FormControl<STUDY_STATUS_ENUM | null>(null)});

	 }

	ngOnInit() {
		this.requestMasterDataService.categoriesWithoutDiagnosticImaging().subscribe((categories: ServiceRequestCategoryDto[]) => {
			this.studyCategoryOptions = categories;
		});

		this.radioControl.controls.controlButton.setValue(this.study_status.FINAL);
		this.data.createOrderService.setCreationStatus(this.study_status.FINAL);

		this.data.createOrderService.hasTemplate$.subscribe(_ => {
			this.radioControl.controls.controlButton.setValue(this.study_status.FINAL);
			this.data.createOrderService.setCreationStatus(this.study_status.FINAL);
		})
	}

	close() {
		this.data.createOrderService.resetForm();
		this.dialogRef.close()
	}

	setCreationStatus($event: MatRadioChange) {
		this.data.createOrderService.setCreationStatus($event.value);
	}

	addOrder() {
		this.data.createOrderService.addToList();
		this.close();
	}

}

export interface CreateOrder {
	patientId: number,
	createOrderService: CreateOrderService,
	problems: SnomedDto[]
}

interface RadioControl {
	controlButton: FormControl<STUDY_STATUS_ENUM | null>;
}
