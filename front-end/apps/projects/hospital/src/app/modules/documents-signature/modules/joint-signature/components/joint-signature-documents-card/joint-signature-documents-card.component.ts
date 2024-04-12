import { Component, Input } from '@angular/core';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { ItemListCard, SelectableCardIds } from '@presentation/components/selectable-card/selectable-card.component';
import { buildHeaderInformation } from '../../mappers/joint-signature.mapper';
import { ElectronicSignatureInvolvedDocumentDto } from '@api-rest/api-model';

@Component({
	selector: 'app-joint-signature-documents-card',
	templateUrl: './joint-signature-documents-card.component.html',
	styleUrls: ['./joint-signature-documents-card.component.scss']
})
export class JointSignatureDocumentsCardComponent {

	@Input() itemList: ItemListCard[] = [];
	@Input() documentId: number;
	@Input() headerInformation: Detail[] = [];
	@Input() documents: ElectronicSignatureInvolvedDocumentDto[];

	constructor() { }

	seeDetails(ids: SelectableCardIds): void {
		this.documentId = ids.id;
		this.headerInformation = buildHeaderInformation(this.documents.find(item => item.documentId === ids.id));
	}
}
