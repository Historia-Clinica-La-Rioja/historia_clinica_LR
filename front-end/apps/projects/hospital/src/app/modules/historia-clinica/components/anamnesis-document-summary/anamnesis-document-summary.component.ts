import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DateTimeDto, ResponseAnamnesisDto } from '@api-rest/api-model';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';
import { DocumentActionsService, DocumentSearch } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service';
import { InternmentSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { AnamnesisAsViewFormat, AnamnesisDocumentSummaryService } from '@historia-clinica/services/anamnesis-document-summary.service';
import { Observable, tap } from 'rxjs';

@Component({
    selector: 'app-anamnesis-document-summary',
    templateUrl: './anamnesis-document-summary.component.html',
    styleUrls: ['./anamnesis-document-summary.component.scss']
})
export class AnamnesisDocumentSummaryComponent {

    @Input() isPopUpOpen: boolean;
    @Input() internmentEpisodeId: number;
    @Input() set activeDocument (activeDocument: DocumentSearch) {
        this._documentId = activeDocument.document?.id;
        this.editedOn = activeDocument.document?.editedOn;
        this._activeDocument = activeDocument;
        this.anamnesis = null;

        this.headerTestInfo = {
            title: "Evaluación de ingreso",
            edit: activeDocument.canDoAction.edit,
            delete: activeDocument.canDoAction.delete,
            headerDescriptionData: {
                scope: "Internación",
                specialty: "Traumatología",
                dateTime: "Fecha y hora",
                professional: activeDocument.document.creator.firstName,
                institution: "Nombre inst",
                sector: "Nombre sect",
                room: "Nombre sala",
                bed: "Nombre cama",
            },
        }

        if (this.internmentEpisodeId && this._documentId) {
            this.anamnesis$ = this.anamnesisService.getAnamnesis(this._documentId, this.internmentEpisodeId).pipe(tap(anamnesis => {
                this.anamnesis = this.anamnesisDocumentSummaryService.getAnamnesisAsViewFormat(anamnesis);
                this.isLoading = false;
            } ))
        }
    };
    @Output() resetActiveDocument = new EventEmitter<boolean>();

    headerTestInfo;
    
    _activeDocument: DocumentSearch;
    anamnesis: AnamnesisAsViewFormat;
    _documentId: number
    anamnesis$: Observable<ResponseAnamnesisDto>;
    isLoading = true;
    editedOn: DateTimeDto;
    constructor(
        private readonly anamnesisService: AnamnesisService,
        private readonly anamnesisDocumentSummaryService: AnamnesisDocumentSummaryService,
		private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly documentActions: DocumentActionsService,
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
