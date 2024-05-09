import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DateTimeDto } from '@api-rest/api-model';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary.service';
import { DocumentActionsService, DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { InternmentSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { AnamnesisAsViewFormat, AnamnesisDocumentSummaryService } from '@historia-clinica/services/anamnesis-document-summary.service';
import { Observable, forkJoin, tap } from 'rxjs';

@Component({
    selector: 'app-anamnesis-document-summary',
    templateUrl: './anamnesis-document-summary.component.html',
    styleUrls: ['./anamnesis-document-summary.component.scss']
})
export class AnamnesisDocumentSummaryComponent {

    @Input() isPopUpOpen: boolean;
    @Input() internmentEpisodeId: number;
    @Input() set activeDocument (activeDocument: DocumentSearch) {
        this.editedOn = activeDocument.document?.editedOn;
        this._documentId = activeDocument.document?.id;
        this._activeDocument = activeDocument;

        if (this.internmentEpisodeId && this._documentId) {
            let anamnesis$ = this.anamnesisService.getAnamnesis(this._documentId, this.internmentEpisodeId);
            let header$ = this.documentSummaryService.getDocumentHeader(this._documentId, this.internmentEpisodeId);

            this.hasData$ = forkJoin([header$, anamnesis$]).pipe(tap(masterdataInfo => {
                this.headerDescription = this.documentSummaryMapperService.mapToHeaderDescription(masterdataInfo[0], 'Evaluación de ingreso', this._activeDocument);
                this.anamnesis = this.anamnesisDocumentSummaryService.getAnamnesisAsViewFormat(masterdataInfo[1]);
                this.isLoading = false;
            }));
        }
    };
    @Output() resetActiveDocument = new EventEmitter<boolean>();

    headerDescription;
    
    anamnesis: AnamnesisAsViewFormat;
    _activeDocument: DocumentSearch;
    _documentId: number
    hasData$: Observable<any>;
    isLoading = true;
    editedOn: DateTimeDto;

    constructor(
        private readonly anamnesisService: AnamnesisService,
        private readonly anamnesisDocumentSummaryService: AnamnesisDocumentSummaryService,
		private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly documentActions: DocumentActionsService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
    ) { }

    delete() {
		this.documentActions.deleteDocument(this._activeDocument.document, this.internmentEpisodeId).subscribe(
			fieldsToUpdate => {
				if (fieldsToUpdate) {
					this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
					this.internmentSummaryFacadeService.updateInternmentEpisode();
				}
			}
		);
		this.resetActiveDocument.emit(true);
	}

	edit() {
		this.documentActions.editDocument(this._activeDocument.document, this.internmentEpisodeId);
		this.resetActiveDocument.emit(true);
	}
}
