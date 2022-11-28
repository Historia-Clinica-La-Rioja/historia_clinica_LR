import { Injectable } from '@angular/core';
import { DocumentSearchDto } from "@api-rest/api-model";
import { DocumentActionReasonComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/document-action-reason/document-action-reason.component";
import { MatDialog } from "@angular/material/dialog";
import { AnamnesisService } from "@api-rest/services/anamnesis.service";
import { EpicrisisService } from "@api-rest/services/epicrisis.service";
import { EvolutionNoteService } from "@api-rest/services/evolution-note.service";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { Subject } from 'rxjs';

@Injectable()
export class DeleteDocumentActionService {

	updateFieldsSubject = new Subject<InternmentFields>();
	updateFields$ = this.updateFieldsSubject.asObservable();

	constructor(
		private readonly dialog: MatDialog,
		private readonly anamnesisService: AnamnesisService,
		private readonly epicrisisService: EpicrisisService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService
	) { }

	delete(document: DocumentSearchDto, internmentEpisodeId: number) {
		const dialogRef = this.dialog.open(DocumentActionReasonComponent, {
			data: {
				title: 'internaciones.dialogs.actions-document.DELETE_TITLE',
				subtitle: 'internaciones.dialogs.actions-document.SUBTITLE',
			},
			width: "50vh",
			autoFocus: false,
			disableClose: true
		});
		dialogRef.afterClosed().subscribe(reason => {
			if (reason) {
				switch (document.documentType) {
					case "Anamnesis":
						this.deleteAnamnesis(document.id, internmentEpisodeId, reason);
						break;
					case "Nota de evolución":
						this.deleteEvolutionNote(document.id, internmentEpisodeId, reason);
						break;
					case "Nota de evolución de enfermería":
						this.deleteEvolutionNote(document.id, internmentEpisodeId, reason);
						break;
					case "Epicrisis":
						this.deleteEpicrisis(document.id, internmentEpisodeId, reason);
						break;
				}
			}
		})
	}

	private deleteAnamnesis(documentId: number, internmentEpisodeId: number, reason: string) {
		this.anamnesisService.deleteAnamnesis(documentId, internmentEpisodeId, reason).subscribe(
			success => {
				this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
				this.updateFieldsSubject.next({ evolutionClinical: true });
			},
			error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))


	}

	private deleteEvolutionNote(documentId: number, internmentEpisodeId: number, reason: string) {
		this.evolutionNoteService.deleteEvolutionDiagnosis(documentId, internmentEpisodeId, reason).subscribe(
			success => {
				this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
				this.updateFieldsSubject.next({ evolutionClinical: true });
			},
			error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
	}

	private deleteEpicrisis(documentId: number, internmentEpisodeId: number, reason: string) {
		this.epicrisisService.deleteEpicrisis(documentId, internmentEpisodeId, reason).subscribe(
			success => {
				this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
				this.updateFieldsSubject.next({ evolutionClinical: true });
			},
			error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
	}

}
