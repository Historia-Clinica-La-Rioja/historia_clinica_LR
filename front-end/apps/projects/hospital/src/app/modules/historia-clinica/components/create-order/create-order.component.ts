import { Component, Input, OnInit } from '@angular/core';
import { ServiceRequestCategoryDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { MIN_DATE } from '@core/utils/date.utils';
import { CreateOrder } from '@historia-clinica/dialogs/add-study/add-study.component';
import { TemplateOrConceptOption } from '../template-concept-typeahead-search/template-concept-typeahead-search.component';

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

	setStudyCategory(studyCategoryId: string) {
		this.data.createOrderService.setStudyCategory(studyCategoryId);
	}

	handleStudySelected(study: TemplateOrConceptOption) {
		let concept: SnomedDto;
		if (study)
			concept = {
				pt: study.data.pt.term,
				sctid: study.data.conceptId,
			}
		this.data.createOrderService.setConcept(concept);
	}
}

