import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { DetailedInformation } from '@presentation/components/detailed-information/detailed-information.component';
import { PresentationModule } from '@presentation/presentation.module';
import { SummaryItem, SummaryListComponent } from '../summary-list/summary-list.component';

@Component({
	selector: 'app-summary-attention-card',
	templateUrl: './summary-attention-card.component.html',
	styleUrls: ['./summary-attention-card.component.scss'],
	standalone: true,
	imports: [
		PresentationModule,
		TranslateModule,
		SummaryListComponent
	]
})
export class SummaryAttentionCardComponent {

	@Input() dataList: SummaryItem[];
	@Input() enableCheckboxes: boolean = true;
	@Input() noDataMessage: string = 'presentation.selectable-card.NO_DATA'
	@Input() detailedInformation: DetailedInformation;
	@Input() hasHeader: boolean = false;

	@Output() checkedIdsOnChange: EventEmitter<number[]> = new EventEmitter();
	@Output() selectedIdOnChange: EventEmitter<number> = new EventEmitter();

	constructor() {}

	selectedId(id: number): void {
		this.selectedIdOnChange.emit(id);
	}

	checkedIds(ids: number[]): void {
		this.checkedIdsOnChange.emit(ids);
	}
}

