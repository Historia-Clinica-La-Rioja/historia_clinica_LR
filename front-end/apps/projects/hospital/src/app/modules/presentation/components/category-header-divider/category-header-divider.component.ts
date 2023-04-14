import { Component, Input } from '@angular/core';
import { EIndicationType, ERole } from '@api-rest/api-model';
import { ActionsButtonService } from '@historia-clinica/modules/ambulatoria/modules/indicacion/services/actions-button.service';
import { Title } from '../indication/indication.component';

@Component({
	selector: 'app-category-header-divider',
	templateUrl: './category-header-divider.component.html',
	styleUrls: ['./category-header-divider.component.scss']
})
export class CategoryHeaderDividerComponent {

	@Input() header: Title;
	@Input() buttonIndication?: EIndicationType;
	@Input() canEdit: ERole[] = [];

	constructor(
		readonly actionsButtonService: ActionsButtonService,

	) { }

	openDialog() {
		this.actionsButtonService.openDialog(this.buttonIndication);
	}
}
