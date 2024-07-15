import { Component, Input, OnInit } from '@angular/core';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { ItemsAndDescriptionData } from '@historia-clinica/utils/document-summary.model';

@Component({
	selector: 'app-surgical-professionals-and-description-summary',
	templateUrl: './surgical-professionals-and-description-summary.component.html',
	styleUrls: ['./surgical-professionals-and-description-summary.component.scss']
})
export class SurgicalProfessionalsAndDescriptionSummaryComponent implements OnInit {

	@Input() descriptionItemDataSummary: ItemsAndDescriptionData;

    firstProcedure: DescriptionItemData;
    remainingProcedures: DescriptionItemData[];

	constructor() { }

	ngOnInit(): void {
		if (this.descriptionItemDataSummary.items && this.descriptionItemDataSummary.items.length > 0) {
            this.firstProcedure = this.descriptionItemDataSummary.items[0];
            this.remainingProcedures = this.descriptionItemDataSummary.items.slice(1);
        } else {
            this.remainingProcedures = [];
        }
	}

}
