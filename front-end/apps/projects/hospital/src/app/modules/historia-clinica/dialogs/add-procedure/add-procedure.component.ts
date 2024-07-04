import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatRadioChange } from '@angular/material/radio';
import { ServiceRequestCategoryDto, SnomedDto } from '@api-rest/api-model';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';
import { CreateOrderService } from '@historia-clinica/services/create-order.service';

@Component({
	selector: 'app-add-procedure',
	templateUrl: './add-procedure.component.html',
	styleUrls: ['./add-procedure.component.scss']
})

export class AddProcedureComponent implements OnInit {
	studyCategoryOptions = [];
	study_status = STUDY_STATUS;

	constructor(
		public dialogRef: MatDialogRef<AddProcedureComponent>,
		@Inject(MAT_DIALOG_DATA) public readonly data: CreateOrder,
		private requestMasterDataService: RequestMasterDataService,

	) { }

	ngOnInit() {
		this.requestMasterDataService.categories().subscribe((categories: ServiceRequestCategoryDto[]) => {
			this.studyCategoryOptions = categories;
		});
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
