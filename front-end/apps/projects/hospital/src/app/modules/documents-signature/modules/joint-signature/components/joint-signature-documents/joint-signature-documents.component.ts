import { Component } from '@angular/core';
import { Filter, SelectedFilterOption } from '@presentation/components/filters/filters.component';
import { SIGNATURE_STATUS_FILTER, SIGNATURE_STATUS_KEY } from '../../constants/joint-signature.constants';
import { JointSignatureService } from '@api-rest/services/joint-signature.service';
import { EElectronicSignatureStatus } from '@api-rest/api-model';
import { deepClone } from '@core/utils/core.utils';

@Component({
	selector: 'app-joint-signature-documents',
	templateUrl: './joint-signature-documents.component.html',
	styleUrls: ['./joint-signature-documents.component.scss']
})
export class JointSignatureDocumentsComponent {
	selectedFilterOption: string;
	filter: Filter[] = SIGNATURE_STATUS_FILTER;

	constructor(private joinSignatureService: JointSignatureService) {
		this.joinSignatureService.getElectronicJointSignatureDocumentPossibleStatusesController().subscribe(documentsState => {
			let filterAux = SIGNATURE_STATUS_FILTER;
			filterAux[0].options = documentsState;
			filterAux[0].defaultValue = documentsState.filter(s => s.id === EElectronicSignatureStatus.PENDING);
			this.filter = deepClone(filterAux);
		})
	}

	handleFilterChange(event: SelectedFilterOption[]): void {
		this.selectedFilterOption = event.length ? event.find(filter => filter.key === SIGNATURE_STATUS_KEY).value : null;
	}
}
