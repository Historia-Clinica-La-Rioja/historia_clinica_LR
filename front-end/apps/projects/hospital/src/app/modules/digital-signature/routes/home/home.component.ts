import { Component, OnInit } from '@angular/core';
import { AppFeature, DigitalSignatureDocumentDto, DocumentDto, LoggedUserDto, PageDto } from '@api-rest/api-model.d';
import { DigitalSignatureService } from '@api-rest/services/digital-signature.service';
import { ItemListCard, SelectableCardIds } from '@presentation/components/selectable-card/selectable-card.component';
import { DocumentService } from '@api-rest/services/document.service';
import { finalize } from 'rxjs';
import { AccountService } from '@api-rest/services/account.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { DetailedInformation } from '@presentation/components/detailed-information/detailed-information.component';
import { URL_DOCUMENTS_SIGNATURE } from '../../../documents-signature/routes/home/home.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { DocumentSignatureService } from '../../../documents-signature/services/document-signature.service';
import { TextDialog, buildItemListCard, buildTextOption } from '../../mappers/digital-signature.mapper';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    documents: ItemListCard[] = [];
    digitalSignatureDocuments: DigitalSignatureDocumentDto[] = [];
    isLoading: boolean = true;
    selectedDocumentsId: number[] = [];
    hasProfessionalCuil: boolean = false;
    ROUTE_PREFIX: string;
    detailedInformation: DetailedInformation;
	readonly PAGE_SIZE = 5;
	elementsAmount: number;
    buttonBack = false;

    constructor(private readonly digitalSignature: DigitalSignatureService,
                private readonly documentService: DocumentService,
                private readonly account: AccountService,
                private readonly dialog: MatDialog,
                private readonly router: Router,
                private readonly contextService: ContextService,
                private readonly featureFlagService: FeatureFlagService,
				private readonly documentSignatureService: DocumentSignatureService
			) {
                    this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_DIGITAL).subscribe(isEnabledDigital =>{
                        this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_CONJUNTA).subscribe(isEnabledConjunta =>{
                          if(isEnabledConjunta && isEnabledDigital){
                            this.buttonBack = true;
                          }
                        })
                      })
                }

    ngOnInit(): void {
        this.ROUTE_PREFIX = `institucion/${this.contextService.institutionId}/`;
        this.account.getInfo().subscribe((result: LoggedUserDto) => {
            if (result.cuil)
                return this.setDocuments();

            this.openDiscardWarningDialog(buildTextOption('digital-signature.dialogs.cuil.NO_CUIL', 'digital-signature.dialogs.cuil.CONTENT', 'buttons.ACCEPT'))
                .afterClosed()
                .subscribe((_) => {
                    this.router.navigate([this.ROUTE_PREFIX]);
            });
        });
    }

    setDocuments(): void {
		const INITIAL_PAGE = 0;
        this.hasProfessionalCuil = true;
        this.fetchData(INITIAL_PAGE);
    }

	handlePageEvent(event: PageEvent): void {
		this.isLoading = true;
		this.fetchData(event.pageIndex);
	}

	private fetchData(pageIndex: number): void {
		this.digitalSignature.getPendingDocumentsByUser(pageIndex)
            .pipe(finalize(() => this.isLoading = false))
            .subscribe((documents: PageDto<DigitalSignatureDocumentDto>) => {
				if (!this.elementsAmount)
					this.elementsAmount = documents.totalElementsAmount;
                this.digitalSignatureDocuments = documents.content;
                this.documents = buildItemListCard(this.digitalSignatureDocuments);
            });
	}

    selectedIds(ids: number[]): void {
        this.selectedDocumentsId = ids;
    }

    downloadPdf(): void {
        this.selectedDocumentsId.forEach(selectedId => this.download({id: selectedId}));
    }

    sign(): void {
        if (!this.selectedDocumentsId.length) return;

        this.openDiscardWarningDialog(buildTextOption('digital-signature.dialogs.sign.TITLE', 'digital-signature.dialogs.sign.CONTENT', 'buttons.CONFIRM', 'buttons.CANCEL'))
            .afterClosed()
            .subscribe((result: boolean) => {
                if (result) {
                    this.digitalSignature.sign(this.selectedDocumentsId)
                        .subscribe((url: string) => window.open(url, "_blank"));
                }
            })

    }

    download(ids: SelectableCardIds): void {
        this.documentService.downloadUnnamedFile(ids.id);
    }

    seeDetails(ids: SelectableCardIds): void {
        this.documentService.getDocumentInfo(ids.id)
            .subscribe((document: DocumentDto) => {
				this.detailedInformation = this.documentSignatureService.buildDetailedInformation(document)
			});
    }

    goToBackDocumentsSignature(): void{
        this.router.navigate([`${this.ROUTE_PREFIX}${URL_DOCUMENTS_SIGNATURE}`]);
      }

    private openDiscardWarningDialog(options: TextDialog): MatDialogRef<DiscardWarningComponent> {
        return this.dialog.open(DiscardWarningComponent, {
            data: {
                title: options.title,
                content: options.content,
                okButtonLabel: options.okButtonLabel,
                cancelButtonLabel: options.cancelButtonLabel
            }
        })
    }
}
