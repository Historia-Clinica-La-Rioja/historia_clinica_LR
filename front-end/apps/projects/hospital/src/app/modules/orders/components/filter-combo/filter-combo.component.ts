import { Component } from '@angular/core';
import { StudyOrderWorkListFilterDto } from '@api-rest/api-model';
import { FilterServiceService } from '../../services/filter-service.service';

const SCOPE_OPTIONS = Object.freeze([
	{ id: 4, name: 'Guardia' },
	{ id: 0, name: 'Internación' },
  ]);

const STUDY_TYPE_OPTIONS = Object.freeze([
	{ id: 1, name: 'Rutina' },
	{ id: 2, name: 'Urgente' },
  ]);

const TRANSFER_OPTIONS = Object.freeze([
	{ id: 'requires', name: 'Requiere' },
	{ id: 'notRequires', name: 'No requiere' },
]);

@Component({
	selector: 'app-filter-combo',
	templateUrl: './filter-combo.component.html',
	styleUrls: ['./filter-combo.component.scss'],
}) export class FilterContainerComponent {

	scopeOptions = SCOPE_OPTIONS;
	studyTypeOptions = STUDY_TYPE_OPTIONS;
	transferOptions = TRANSFER_OPTIONS;

	constructor(
		readonly filterServiceService: FilterServiceService,
  	) { }
	filters: StudyOrderWorkListFilterDto = {
		categories: ["108252007"],
		notRequiresTransfer: false,
		patientTypeId: [1, 2, 3, 4, 5, 6, 7, 8],
		requiresTransfer: false,
		sourceTypeIds: [],
		studyTypeIds: [],
	};

	onSelectionChange(key: keyof StudyOrderWorkListFilterDto,  selectedIds) {
		this.filters[key] = selectedIds;
		this.filterServiceService.updateFilters(this.filters);
	}

	onTransferSelection(key: keyof StudyOrderWorkListFilterDto, selectedIds: any) {
		this.filters.requiresTransfer = selectedIds.includes('requires');
		this.filters.notRequiresTransfer = selectedIds.includes('notRequires');
		this.filterServiceService.updateFilters(this.filters);
	}
}
