import { Component } from '@angular/core';
import { ElectronicJointSignatureInvolvedDocumentListFilterDto } from '@api-rest/api-model';

@Component({
	selector: 'app-joint-signature-documents',
	templateUrl: './joint-signature-documents.component.html',
	styleUrls: ['./joint-signature-documents.component.scss']
})
export class JointSignatureDocumentsComponent {
	selectedFilterOptions: ElectronicJointSignatureInvolvedDocumentListFilterDto = {};

	constructor() { }

	setSelectedFilterOptions(filters) {
		this.selectedFilterOptions = filters;
	}
}
