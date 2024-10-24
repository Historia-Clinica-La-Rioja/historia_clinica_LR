import { Component, Input, OnInit } from '@angular/core';
import { ServiceRequestCategoryDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { MIN_DATE } from '@core/utils/date.utils';
import { CreateOrder } from '@historia-clinica/dialogs/add-procedure/add-procedure.component';

const DEFAULT_STUDY = {
	id: "71388002",
	description: "Otros procedimientos y prácticas"
};

@Component({
	selector: 'app-create-order',
	templateUrl: './create-order.component.html',
	styleUrls: ['./create-order.component.scss']
})
export class CreateOrderComponent implements OnInit {

	minDate = MIN_DATE;
	maxDate = new Date()
	PROCEDURE = SnomedECL.PROCEDURE;
	DEFAULT_STUDY = DEFAULT_STUDY;
	@Input() data: CreateOrder;
	@Input() studyCategoryOptions: ServiceRequestCategoryDto[];
	@Input() problems: SnomedDto[];

	ngOnInit() {
		this.setStudyCategory(DEFAULT_STUDY.id);
	}

	dateChanged(date: Date) {
		this.data.createOrderService.setDate(date);
	}

	setConcept(conceptProblem: SnomedDto) {
		this.data.createOrderService.setConcept(conceptProblem);
	}

	setProblem(healthProblem: SnomedDto) {
		this.data.createOrderService.setProblem(healthProblem);
	}

	setStudyCategory(studyCategoryId: number | string) {
		this.data.createOrderService.setStudyCategory(studyCategoryId);
	}

}

