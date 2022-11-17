import { Injectable } from '@angular/core';
import { DocumentSearchDto } from '@api-rest/api-model';
import { InternmentActionsService } from './internment-actions.service';
import { MatDialog } from "@angular/material/dialog";
import { InternmentFields, InternmentSummaryFacadeService } from './internment-summary-facade.service';

@Injectable()
export class EditDocumentActionService {

	canConfirmedDocument = false;

	constructor(
		private readonly internmentActions: InternmentActionsService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly dialog: MatDialog
	) { }

	setInformation(patientId: number, internmentEpisodeId: number) {
		this.internmentActions.setInternmentInformation(patientId, internmentEpisodeId);
	}

	editDocument(document: DocumentSearchDto) {
		if (document.documentType === "Anamnesis") {
			this.internmentActions.anamnesis$.subscribe(fieldsToUpdate => this.updateInternment(fieldsToUpdate));
			this.internmentActions.openAnamnesis(document.id);
		}
		if (document.documentType === "Nota de evolución" || document.documentType === "Nota de evolución de enfermería") {
			this.internmentActions.evolutionNote$.subscribe(fieldsToUpdate => this.updateInternment(fieldsToUpdate));
			this.internmentActions.openEvolutionNote(document.id, document.documentType);
		}
		if (document.documentType === "Epicrisis") {
			this.internmentActions.epicrisis$.subscribe(fieldsToUpdate => this.updateInternment(fieldsToUpdate));
			this.internmentActions.openEpicrisis(document.id);
		}
	}

	editDraftEpicrisis(document: DocumentSearchDto, canConfirmedDocument: boolean) {
		this.canConfirmedDocument = canConfirmedDocument;
		this.internmentActions.openEpicrisis(document.id, true);
	}

	private updateInternment(fieldsToUpdate: InternmentFields) {
		if (fieldsToUpdate)
			this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
	}
}
