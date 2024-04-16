import { Component, Input, OnInit, ViewChild, } from '@angular/core';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { ItemListCard, SelectableCardIds } from '@presentation/components/selectable-card/selectable-card.component';
import { buildHeaderInformation, buildItemListCard } from '../../mappers/joint-signature.mapper';
import { ElectronicSignatureInvolvedDocumentDto, PageDto, RejectDocumentElectronicJointSignatureDto } from '@api-rest/api-model';
import { JointSignatureService } from '@api-rest/services/joint-signature.service';
import { map, tap } from 'rxjs';
import { INITIAL_PAGE, PAGE_SIZE, PAGE_SIZE_OPTIONS } from '../../constants/joint-signature.constants';
import { MatDialog } from '@angular/material/dialog';
import { RejectSignatureComponent } from '../../dialogs/reject-signature/reject-signature.component';
import { MatPaginator } from '@angular/material/paginator';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';

@Component({
	selector: 'app-joint-signature-documents-card',
	templateUrl: './joint-signature-documents-card.component.html',
	styleUrls: ['./joint-signature-documents-card.component.scss']
})
export class JointSignatureDocumentsCardComponent implements OnInit {
	@ViewChild('paginator') paginator: MatPaginator;

	@Input() set setFilter(filter: string) {
		this.filter = filter;
		this.setDocuments(this.INITIAL_PAGE);
	};

	filter: string;
	headerInformation: Detail[] = [];
	isLoading: boolean;
	documents: ItemListCard[] = [];
	jointSignatureDocuments: ElectronicSignatureInvolvedDocumentDto[];
	selectedDocumentId: number;
	elementsAmount: number;
	pageSize = PAGE_SIZE;
	selectedDocumentsId: number[] = [];

	readonly INITIAL_PAGE = INITIAL_PAGE;
	readonly PAGE_SIZE_OPTIONS = PAGE_SIZE_OPTIONS;

	constructor(private dialog: MatDialog, private readonly snackBarService: SnackBarService,
		private readonly jointSignatureService: JointSignatureService
	) { }

	ngOnInit(): void {
		this.setDocuments(this.INITIAL_PAGE);
	}

	setDocuments(pageIndex: number): void {
		this.isLoading = true;
		this.selectedDocumentId = undefined;
		this.headerInformation = [];
		this.setPageInfo(pageIndex);
		this.jointSignatureService.getProfessionalInvolvedDocumentList(this.pageSize, pageIndex, this.filter)
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

	openPopUpRejectSignature() {
		const dialogRef = this.dialog.open(RejectSignatureComponent, {
			data: {
				amountSignatures: this.selectedDocumentsId.length,
			},
			width: '420px',
			autoFocus: false,
			disableClose: true,
		})
		dialogRef.afterClosed().subscribe(reason => {
			if (reason) {
				this.rejectSignature(reason);
			}
		})
	}

	rejectSignature(reasonRejection: RejectDocumentElectronicJointSignatureDto) {
		let message = " ";
		reasonRejection.documentIds = this.selectedDocumentsId;
		this.jointSignatureService.rejectDocumentElectronicJointSignature(reasonRejection).subscribe(res => {
			if (this.selectedDocumentsId.length > 1) {
				message = 'firma-conjunta.reject-signature.REJECTS_SUCCESS';
			} else {
				message = 'firma-conjunta.reject-signature.REJECT_SUCCESS';
			}
			this.snackBarService.showSuccess(message);
		})
	}

	signDocument() {
		let message = " ";
		this.jointSignatureService.signDocumentElectronicJointSignature(this.selectedDocumentsId).subscribe(res => {
			if (this.selectedDocumentsId.length > 1) {
				message = 'firma-conjunta.sign-document.SIGNATURES_SUCCESS';
			} else {
				message = 'firma-conjunta.sign-document.SIGNATURE_SUCCESS';
			}
			this.snackBarService.showSuccess(message);
		}, error => {
			this.snackBarService.showError(error.text);
		})
	}

	openSingDocument() {
		if (this.selectedDocumentsId.length > 0) {
			let title = this.selectedDocumentsId?.length > 1 ? 'firma-conjunta.sign-document.TITLE2' : 'firma-conjunta.sign-document.TITLE'
			let param = this.selectedDocumentsId?.length > 1 ? this.selectedDocumentsId.length : null;
			const warnignComponent = this.dialog.open(DiscardWarningComponent,
				{
					disableClose: true,
					data: {
						title: title,
						okButtonLabel: 'firma-conjunta.sign-document.BUTTON_SIGN',
						paramTranslate: param,
					},
				});
			warnignComponent.afterClosed().subscribe(confirmed => {
				if (confirmed) {
					this.signDocument()
				}
			});
		}
	}

	selectedIds(ids: number[]) {
		this.selectedDocumentsId = ids;
	}
}
