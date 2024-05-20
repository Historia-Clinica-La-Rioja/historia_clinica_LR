import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { DocumentActionsService, DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { InternmentSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { EvolutionNoteAsViewFormat, EvolutionNoteDocumentSummaryService } from '@historia-clinica/services/evolution-note-document-summary.service';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { TranslateService } from '@ngx-translate/core';
import { Observable, forkJoin, map } from 'rxjs';

@Component({
    selector: 'app-evolution-note-document-summary',
    templateUrl: './evolution-note-document-summary.component.html',
    styleUrls: ['./evolution-note-document-summary.component.scss']
})
export class EvolutionNoteDocumentSummaryComponent implements OnInit {

    @Input() isPopUpOpen: boolean;
    @Input() internmentEpisodeId: number;
    @Input() set activeDocument(activeDocument: DocumentSearch) {
        this._activeDocument = activeDocument;
        this.fetchSummaryInfo();
    };
    @Output() resetActiveDocument = new EventEmitter<boolean>();

    _activeDocument: DocumentSearch;
    documentSummary$: Observable<{headerDescription: HeaderDescription, evolutionNote: EvolutionNoteAsViewFormat}>;

    documentName = '';

    constructor(
        private readonly evolutionNoteService: EvolutionNoteService,
        private readonly evolutionNoteDocumentSummaryService: EvolutionNoteDocumentSummaryService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
        private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly documentActions: DocumentActionsService,
		private readonly translateService: TranslateService,
    ) {
        this.documentName = this.translateService.instant('internaciones.documents-summary.document-name.EVOLUTION_NOTE');
    }

    ngOnInit(): void {
    }

    private fetchSummaryInfo(){
        if (this._activeDocument?.document?.id) {
            let evolutionNote$ = this.evolutionNoteService.getEvolutionDiagnosis(this._activeDocument.document?.id, this.internmentEpisodeId);
            let header$ = this.documentSummaryService.getDocumentHeader(this._activeDocument.document?.id, this.internmentEpisodeId);

            this.documentSummary$ = forkJoin([header$, evolutionNote$]).pipe(map(([headerData, evolutionNoteData]) => {
                return {
                    headerDescription: this.documentSummaryMapperService.mapToHeaderDescription(headerData, this.documentName, this._activeDocument),
                    evolutionNote: this.evolutionNoteDocumentSummaryService.mapEvolutionNoteAsViewFormat(evolutionNoteData),
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
			}
		);
		this.resetActiveDocument.emit(true);
	}

	edit() {
		this.documentActions.editDocument(this._activeDocument.document, this.internmentEpisodeId);
		this.resetActiveDocument.emit(true);
	}
}
