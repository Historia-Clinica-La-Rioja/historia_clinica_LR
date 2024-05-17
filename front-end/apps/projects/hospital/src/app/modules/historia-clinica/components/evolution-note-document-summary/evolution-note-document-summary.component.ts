import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HospitalizationDocumentHeaderDto, ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { DocumentActionsService, DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { InternmentSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { EvolutionNoteAsViewFormat, EvolutionNoteDocumentSummaryService } from '@historia-clinica/services/evolution-note-document-summary.service';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { Observable, forkJoin, tap } from 'rxjs';

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

    headerDescription: HeaderDescription;

    evolutionNote: EvolutionNoteAsViewFormat;
    _activeDocument: DocumentSearch;
    hasData$: Observable<[HospitalizationDocumentHeaderDto, ResponseEvolutionNoteDto]>;
    isLoading = true;

    constructor(
        private readonly evolutionNoteService: EvolutionNoteService,
        private readonly evolutionNoteDocumentSummaryService: EvolutionNoteDocumentSummaryService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
        private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly documentActions: DocumentActionsService,
    ) { }

    ngOnInit(): void {
    }

    private fetchSummaryInfo(){
        if (this.internmentEpisodeId && this._activeDocument?.document?.id) {
            let evolutionNote$ = this.evolutionNoteService.getEvolutionDiagnosis(this._activeDocument.document?.id, this.internmentEpisodeId);
            let header$ = this.documentSummaryService.getDocumentHeader(this._activeDocument.document?.id, this.internmentEpisodeId);

            this.hasData$ = forkJoin([header$, evolutionNote$]).pipe(tap(([headerData, evolutionNoteData]) => {
                this.headerDescription = this.documentSummaryMapperService.mapToHeaderDescription(headerData, 'Nota de evolución', this._activeDocument);
                this.evolutionNote = this.evolutionNoteDocumentSummaryService.mapEvolutionNoteAsViewFormat(evolutionNoteData);
                this.isLoading = false;
            }));
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
