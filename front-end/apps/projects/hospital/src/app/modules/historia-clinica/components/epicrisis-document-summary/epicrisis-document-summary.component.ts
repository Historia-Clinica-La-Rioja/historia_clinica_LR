import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { EpicrisisService } from '@api-rest/services/epicrisis.service';
import { DocumentActionsService, DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { InternmentSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { EpicrisisDocumentSummaryService, EpicrisisViewFormat } from '@historia-clinica/services/epicrisis-document-summary.service';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { TranslateService } from '@ngx-translate/core';
import { ButtonType } from '@presentation/components/button/button.component';
import { Observable, forkJoin, map } from 'rxjs';

const ACTION_TRIGGERED = true;
@Component({
    selector: 'app-epicrisis-document-summary',
    templateUrl: './epicrisis-document-summary.component.html',
    styleUrls: ['./epicrisis-document-summary.component.scss']
})
export class EpicrisisDocumentSummaryComponent implements OnInit {

    @Input() isPopUpOpen: boolean;
    @Input() internmentEpisodeId: number;
    @Input() set activeDocument(activeDocument: DocumentSearch) {
        this._activeDocument = activeDocument;
        this.fetchSummaryInfo();
    };
    @Output() resetActiveDocument = new EventEmitter<boolean>();

    _activeDocument: DocumentSearch;
    documentSummary$: Observable<{headerDescription: HeaderDescription, epicrisis: EpicrisisViewFormat}>;
    documentName = '';
    ButtonType = ButtonType;

    constructor(
        private readonly epicrisisService: EpicrisisService,
        private readonly epicrisisDocumentSummaryService: EpicrisisDocumentSummaryService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
        private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly documentActions: DocumentActionsService,
		private readonly translateService: TranslateService,
    ) { 
        this.documentName = this.translateService.instant('internaciones.documents-summary.document-name.EPICRISIS');
    }

    ngOnInit(): void {
    }

    private fetchSummaryInfo(){
        if (this._activeDocument?.document?.id) {
            let epicrisis$ = this.epicrisisService.getEpicrisis(this._activeDocument.document.id, this.internmentEpisodeId);
            let header$ = this.documentSummaryService.getDocumentHeader(this._activeDocument.document?.id, this.internmentEpisodeId);

            this.documentSummary$ = forkJoin([header$, epicrisis$]).pipe(map(([headerData, epicrisisData]) => {
                return {
                    headerDescription: this.documentSummaryMapperService.mapToHeaderDescription(headerData, this.documentName, this._activeDocument),
                    epicrisis: this.epicrisisDocumentSummaryService.mapEpicrisisAsViewFormat(epicrisisData),
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

    editDraft() {
        this.documentActions.editEpicrisisDraft(this._activeDocument.document);
		this.resetActiveDocument.emit(ACTION_TRIGGERED);
    }
}
