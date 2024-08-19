import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AnesthethicReportService } from '@api-rest/services/anesthethic-report.service';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { DocumentActionsService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { InternmentSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { AnestheticReportDocumentSummaryService, AnestheticReportViewFormat } from '@historia-clinica/services/anesthetic-report-document-summary.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { TranslateService } from '@ngx-translate/core';
import { ButtonType } from '@presentation/components/button/button.component';
import { Observable, forkJoin, map } from 'rxjs';

const ACTION_TRIGGERED = true;
@Component({
	selector: 'app-anesthetic-report-document-summary',
	templateUrl: './anesthetic-report-document-summary.component.html',
	styleUrls: ['./anesthetic-report-document-summary.component.scss']
})
export class AnestheticReportDocumentSummaryComponent implements OnInit {
    @Input() isPopUpOpen: boolean;
    @Input() internmentEpisodeId: number;
    @Input() set activeDocument (activeDocument: DocumentSearch) {
        this._activeDocument = activeDocument;
		this.isDraft = !this._activeDocument.document.confirmed
        this.fetchSummaryInfo();
    };
    @Output() resetActiveDocument = new EventEmitter<boolean>();

    _activeDocument: DocumentSearch;
    documentSummary$: Observable<{headerDescription: HeaderDescription, anestheticReport: AnestheticReportViewFormat}>;
	isDraft = false;
    ButtonType = ButtonType.FLAT;
    documentName = '';

	constructor(
		private readonly anestheticReportService: AnesthethicReportService,
		private readonly anestheticReportDocumentSummaryService: AnestheticReportDocumentSummaryService,
		private readonly documentActions: DocumentActionsService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
		private readonly translateService: TranslateService,
        private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
     ) {
        this.documentName = this.translateService.instant('internaciones.documents-summary.document-name.ANESTHETIC_REPORT');
    }

    ngOnInit(): void {
        this.fetchSummaryInfo();
    }

    private fetchSummaryInfo(){
        if (this._activeDocument?.document?.id) {
            let anestheticReport$ = this.anestheticReportService.getAnestheticReport(this._activeDocument.document.id);
            let header$ = this.documentSummaryService.getDocumentHeader(this._activeDocument.document?.id, this.internmentEpisodeId);

            this.documentSummary$ = forkJoin([header$, anestheticReport$]).pipe(map(([headerData, anestheticReportData]) => {
                return {
                    headerDescription: this.documentSummaryMapperService.mapToHeaderDescription(headerData, this.documentName, this._activeDocument),
                    anestheticReport: this.anestheticReportDocumentSummaryService.mapToAnestheticReportViewFormat(anestheticReportData),
            }}));
        }
    }

    delete() {
		this.documentActions.deleteDocument(this._activeDocument.document, this.internmentEpisodeId).subscribe(
			fieldsToUpdate => {
				if (fieldsToUpdate) {
					this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
					this.internmentSummaryFacadeService.updateInternmentEpisode();
				}
                this.resetActiveDocument.emit(ACTION_TRIGGERED);
			}
		);
	}

	edit() {
		this.documentActions.editDocument(this._activeDocument.document, this.internmentEpisodeId);
		this.resetActiveDocument.emit(ACTION_TRIGGERED);
	}

	openEditDraft() {
		this.documentActions.editAnestheticPartDraft(this._activeDocument.document)
	}
}
