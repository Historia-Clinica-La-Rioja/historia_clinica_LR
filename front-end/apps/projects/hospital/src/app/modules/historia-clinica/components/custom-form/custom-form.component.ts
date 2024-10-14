import { Component, Input } from '@angular/core';
import { Templates } from '@historia-clinica/modules/ambulatoria/components/control-select-template/control-select-template.component';
import { ResultTemplate } from '@historia-clinica/modules/ambulatoria/services/control-templates.service';
import { CreateOrderService } from '@historia-clinica/services/create-order.service';

@Component({
	selector: 'app-custom-form',
	templateUrl: './custom-form.component.html',
	styleUrls: ['./custom-form.component.scss']
})
export class CustomFormComponent {

	procedureTemplateId: number;
	@Input() templates: Templates[];
	@Input() createOrderService: CreateOrderService;

	selectedProcedureTemplate(selectedTemplate : ResultTemplate, procedureTemplateId: number) {
		this.procedureTemplateId = procedureTemplateId;
		this.createOrderService.setObservations(selectedTemplate , procedureTemplateId);
	}

	changeValues(resultTemplate: ResultTemplate) {
		this.createOrderService.setObservations(resultTemplate, this.procedureTemplateId);
	}

}
