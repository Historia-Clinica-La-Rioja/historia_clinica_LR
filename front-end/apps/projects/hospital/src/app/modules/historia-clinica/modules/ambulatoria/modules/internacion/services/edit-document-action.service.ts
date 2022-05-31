import { Injectable } from '@angular/core';
import { DocumentSearchDto } from '@api-rest/api-model';
import { InternmentActionsService } from './internment-actions.service';
import { DocumentActionReasonComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/document-action-reason/document-action-reason.component";
import { Observable } from "rxjs";
import { MatDialog } from "@angular/material/dialog";
@Injectable({
	providedIn: 'root'
})
export class EditDocumentActionService {

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

	editDraftEpicrisis(document: DocumentSearchDto) {
		const isDraft = true;
		this.internmentActions.openEpicrisis(document.id, isDraft);
	}

	openEditReason(): Observable<string> {
		const dialogRef = this.dialog.open(DocumentActionReasonComponent, {
			data: {
				title: 'internaciones.dialogs.actions-document.EDIT_TITLE',
			},
			width: "50vh",
			autoFocus: false,
			disableClose: true
		});
		return dialogRef.afterClosed();
	}

}
