import { Injectable } from '@angular/core';
import { DocumentSearchDto } from '@api-rest/api-model';
import { InternmentActionsService } from './internment-actions.service';
import { MatDialog } from "@angular/material/dialog";

@Injectable()
export class EditDocumentActionService {

	canConfirmedDocument = false;

	constructor(
		private readonly internmentActions: InternmentActionsService,
		private readonly dialog: MatDialog
	) { }

	setInformation(patientId: number, internmentEpisodeId: number) {
		this.internmentActions.setInternmentInformation(patientId, internmentEpisodeId);
	}

	editDocument(document: DocumentSearchDto) {
		if (document.documentType === "Anamnesis")
			this.internmentActions.openAnamnesis(document.id)
		if (document.documentType === "Nota de evolución" || document.documentType === "Nota de evolución de enfermería")
			this.internmentActions.openEvolutionNote(document.id, document.documentType)
		if (document.documentType === "Epicrisis")
			this.internmentActions.openEpicrisis(document.id)
	}

	editDraftEpicrisis(document: DocumentSearchDto, canConfirmedDocument: boolean) {
		this.canConfirmedDocument = canConfirmedDocument;
		this.internmentActions.openEpicrisis(document.id, true);
	}

}
