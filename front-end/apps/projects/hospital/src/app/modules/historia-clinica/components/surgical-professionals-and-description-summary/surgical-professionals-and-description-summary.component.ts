import { Component, Input } from '@angular/core';
import { ItemsAndDescriptionData } from '@historia-clinica/utils/document-summary.model';

@Component({
	selector: 'app-surgical-professionals-and-description-summary',
	templateUrl: './surgical-professionals-and-description-summary.component.html',
	styleUrls: ['./surgical-professionals-and-description-summary.component.scss']
})
export class SurgicalProfessionalsAndDescriptionSummaryComponent {

	@Input() descriptionItemDataSummary: ItemsAndDescriptionData;

	constructor() { }

}
