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
import { SurgicalReportService } from '@api-rest/services/surgical-report.service';
import { AnesthethicReportService } from '@api-rest/services/anesthethic-report.service';

@Injectable()
export class DeleteDocumentActionService {

	updateFieldsSubject = new Subject<InternmentFields>();
	updateFields$ = this.updateFieldsSubject.asObservable();
	constructor(
		private readonly dialog: MatDialog,
		private readonly anamnesisService: AnamnesisService,
		private readonly epicrisisService: EpicrisisService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly surgicalReportService: SurgicalReportService,
		private readonly snackBarService: SnackBarService,
        private readonly anestheticReportService: AnesthethicReportService,
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
                const DELETE_DOCUMENT_TYPES = {
                    [DocumentTypes.ANAMNESIS]: (documentId: number, internmentEpisodeId: number, reason: string) => this.deleteAnamnesis(documentId, internmentEpisodeId, reason),
                    [DocumentTypes.EVOLUTION_NOTE]: (documentId: number, internmentEpisodeId: number, reason: string) => this.deleteEvolutionNote(documentId, internmentEpisodeId, reason),
                    [DocumentTypes.NURSE_EVOLUTION_NOTE]: (documentId: number, internmentEpisodeId: number, reason: string) => this.deleteEvolutionNote(documentId, internmentEpisodeId, reason),
                    [DocumentTypes.EPICRISIS]: (documentId: number, internmentEpisodeId: number, reason: string) => this.deleteEpicrisis(documentId, internmentEpisodeId, reason),
                    [DocumentTypes.SURGICAL_REPORT]: (documentId: number, internmentEpisodeId: number, reason: string) => this.deleteSurgicalReport(documentId, internmentEpisodeId, reason),
                    [DocumentTypes.ANESTHETIC_REPORT]: (documentId: number, internmentEpisodeId: number, reason: string) => this.deleteAnestheticReport(documentId, reason),
                }
				DELETE_DOCUMENT_TYPES[document.documentType](document.id, internmentEpisodeId, reason);
			}
		})
	}

	private deleteAnamnesis(documentId: number, internmentEpisodeId: number, reason: string) {
		this.anamnesisService.deleteAnamnesis(documentId, internmentEpisodeId, reason).subscribe(
			success => {
				this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
				this.updateFieldsSubject.next({ evolutionClinical: true, diagnosis: true, mainDiagnosis: true });
			},
			error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))


	}

	private deleteEvolutionNote(documentId: number, internmentEpisodeId: number, reason: string) {
		this.evolutionNoteService.deleteEvolutionDiagnosis(documentId, internmentEpisodeId, reason).subscribe(
			success => {
				this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
				this.updateFieldsSubject.next({evolutionClinical: true,diagnosis: true, mainDiagnosis: true, allergies: true, riskFactors: true,medications: true, bloodType: true,heightAndWeight: true, immunizations: true});
			},
			error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
	}

	private deleteEpicrisis(documentId: number, internmentEpisodeId: number, reason: string) {
		this.epicrisisService.deleteEpicrisis(documentId, internmentEpisodeId, reason).subscribe(
			success => {
				this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
				this.updateFieldsSubject.next({ evolutionClinical: true, diagnosis: true, mainDiagnosis: true });
			},
			error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
	}

	private deleteSurgicalReport(documentId: number, internmentEpisodeId: number, reason: string) {
		this.surgicalReportService.deleteSurgicalReport(documentId, internmentEpisodeId, reason).subscribe(
			success => {
				this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
				this.updateFieldsSubject.next({ evolutionClinical: true, diagnosis: true, mainDiagnosis: true });
			},
			error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
	}

    private deleteAnestheticReport(documentId: number, reason: string) {
        this.anestheticReportService.deleteAnestheticReport(documentId, reason).subscribe(
			success => {
				this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
				this.updateFieldsSubject.next({ evolutionClinical: true });
			},
			error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
	}
}

enum DocumentTypes {
    ANESTHETIC_REPORT = 'Parte anestésico',
    EVOLUTION_NOTE = 'Nota de evolución',
    NURSE_EVOLUTION_NOTE = 'Nota de evolución de enfermería',
    ANAMNESIS = 'Anamnesis',
    EPICRISIS = 'Epicrisis',
	SURGICAL_REPORT = 'Parte quirúrgico de internación',
}