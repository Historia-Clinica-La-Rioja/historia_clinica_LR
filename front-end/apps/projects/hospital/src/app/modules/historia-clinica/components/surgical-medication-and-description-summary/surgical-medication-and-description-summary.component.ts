import { Component, Input, OnInit } from '@angular/core';
import { ItemsAndDescriptionData } from '@historia-clinica/utils/document-summary.model';

@Component({
	selector: 'app-surgical-medication-and-description-summary',
	templateUrl: './surgical-medication-and-description-summary.component.html',
	styleUrls: ['./surgical-medication-and-description-summary.component.scss']
})
export class SurgicalMedicationAndDescriptionSummaryComponent implements OnInit {

	@Input() descriptionItemDataSummary: ItemsAndDescriptionData;
	constructor() { }

	ngOnInit(): void {
	}

}
