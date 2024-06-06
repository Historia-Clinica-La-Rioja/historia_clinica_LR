import { Component } from '@angular/core';
import { SelectedFilterOption } from '@presentation/components/filters/filters.component';
import { SIGNATURE_STATUS_FILTER, SIGNATURE_STATUS_KEY } from '../../constants/joint-signature.constants';

@Component({
	selector: 'app-joint-signature-documents',
	templateUrl: './joint-signature-documents.component.html',
	styleUrls: ['./joint-signature-documents.component.scss']
})
export class JointSignatureDocumentsComponent {

	selectedFilterOption: string;
	filter = SIGNATURE_STATUS_FILTER;

	constructor() { }

	handleFilterChange(event: SelectedFilterOption[]): void {
		this.selectedFilterOption = event.length ? event.find(filter => filter.key === SIGNATURE_STATUS_KEY).value : null;
	}
}
