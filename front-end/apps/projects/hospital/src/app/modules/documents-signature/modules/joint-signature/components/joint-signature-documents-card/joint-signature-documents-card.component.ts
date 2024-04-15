import { Component, OnInit, ViewChild, } from '@angular/core';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { ItemListCard, SelectableCardIds } from '@presentation/components/selectable-card/selectable-card.component';
import { buildHeaderInformation, buildItemListCard } from '../../mappers/joint-signature.mapper';
import { ElectronicSignatureInvolvedDocumentDto, PageDto } from '@api-rest/api-model';
import { JointSignatureService } from '@api-rest/services/joint-signature.service';
import { map, tap } from 'rxjs';
import { INITIAL_PAGE, PAGE_SIZE, PAGE_SIZE_OPTIONS } from '../../constants/joint-signature.constants';
import { MatPaginator } from '@angular/material/paginator';

@Component({
	selector: 'app-joint-signature-documents-card',
	templateUrl: './joint-signature-documents-card.component.html',
	styleUrls: ['./joint-signature-documents-card.component.scss']
})
export class JointSignatureDocumentsCardComponent implements OnInit {
	@ViewChild('paginator') paginator: MatPaginator;

	headerInformation: Detail[] = [];
	isLoading: boolean;
	documents: ItemListCard[] = [];
	jointSignatureDocuments: ElectronicSignatureInvolvedDocumentDto[];
	selectedDocumentId: number;
	elementsAmount: number;
	pageSize = PAGE_SIZE;

	readonly INITIAL_PAGE = INITIAL_PAGE;
	readonly PAGE_SIZE_OPTIONS = PAGE_SIZE_OPTIONS;

	constructor(
		private readonly jointSignatureService: JointSignatureService,
	) { }

	ngOnInit(): void {
		this.setDocuments(this.INITIAL_PAGE);
	}

	setDocuments(pageIndex: number): void {
		this.isLoading = true;
		this.selectedDocumentId = undefined;
		this.headerInformation = [];
		this.setPageInfo(pageIndex);
		this.jointSignatureService.getProfessionalInvolvedDocumentList(this.pageSize, pageIndex)
			.pipe(
				tap(result => this.elementsAmount = result.totalElementsAmount),
				map((documents: PageDto<ElectronicSignatureInvolvedDocumentDto>) => documents.content)
			)
			.subscribe((documents: ElectronicSignatureInvolvedDocumentDto[]) => {
				this.jointSignatureDocuments = documents;
				this.documents = buildItemListCard(this.jointSignatureDocuments);
				this.isLoading = false;
			}, _ => this.isLoading = false);
	}

	handlePageEvent(event) {
		this.setDocuments(event.pageIndex);
	}

	setPageInfo(pageNumber: number) {
		if (this.paginator) {
        	this.paginator.pageIndex = pageNumber;
			this.pageSize = this.paginator.pageSize;
		}
    }

	seeDetails(ids: SelectableCardIds): void {
		this.selectedDocumentId = ids.id;
		this.headerInformation = buildHeaderInformation(this.jointSignatureDocuments.find(item => item.documentId === ids.id));
	}
}
