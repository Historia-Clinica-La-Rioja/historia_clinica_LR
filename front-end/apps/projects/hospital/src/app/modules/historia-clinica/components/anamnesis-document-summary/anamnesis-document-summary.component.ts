import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { DocumentActionsService, DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { InternmentSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { AnamnesisAsViewFormat, AnamnesisDocumentSummaryService } from '@historia-clinica/services/anamnesis-document-summary.service';
import { Observable, forkJoin, map } from 'rxjs';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-anamnesis-document-summary',
    templateUrl: './anamnesis-document-summary.component.html',
    styleUrls: ['./anamnesis-document-summary.component.scss']
})
export class AnamnesisDocumentSummaryComponent {

    @Input() isPopUpOpen: boolean;
    @Input() internmentEpisodeId: number;
    @Input() set activeDocument (activeDocument: DocumentSearch) {
        this._activeDocument = activeDocument;
        this.fetchSummaryInfo();
    };
    @Output() resetActiveDocument = new EventEmitter<boolean>();

    _activeDocument: DocumentSearch;
    documentSummary$: Observable<{headerDescription: HeaderDescription, anamnesis: AnamnesisAsViewFormat}>;
    documentName = '';

    constructor(
        private readonly anamnesisService: AnamnesisService,
        private readonly anamnesisDocumentSummaryService: AnamnesisDocumentSummaryService,
		private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly documentActions: DocumentActionsService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
		private readonly translateService: TranslateService,
    ) {
        this.documentName = this.translateService.instant('internaciones.documents-summary.document-name.ANAMNESIS');
    }

    private fetchSummaryInfo(){
        if (this._activeDocument?.document?.id) {
            let anamnesis$ = this.anamnesisService.getAnamnesis(this._activeDocument.document?.id, this.internmentEpisodeId);
            let header$ = this.documentSummaryService.getDocumentHeader(this._activeDocument.document?.id, this.internmentEpisodeId);

            this.documentSummary$ = forkJoin([header$, anamnesis$]).pipe(map(([headerData, anamnesisData]) => {
                return {
                    headerDescription: this.documentSummaryMapperService.mapToHeaderDescription(headerData, 'Evaluación de ingreso', this._activeDocument),
                    anamnesis: this.anamnesisDocumentSummaryService.mapToAnamnesisAsViewFormat(anamnesisData),
                }
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
