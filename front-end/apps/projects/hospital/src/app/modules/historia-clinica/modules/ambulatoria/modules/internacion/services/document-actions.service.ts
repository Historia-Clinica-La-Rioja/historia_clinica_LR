import { Injectable } from '@angular/core';
import { DateTimeDto, DocumentSearchDto, LoggedUserDto } from "@api-rest/api-model";
import { differenceInHours } from "date-fns";
import { AccountService } from "@api-rest/services/account.service";
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { dateTimeDtoToDate, dateTimeDtoToStringDate } from "@api-rest/mapper/date-dto.mapper";
import { MatDialog } from "@angular/material/dialog";
import { EvolutionNoteService } from "@api-rest/services/evolution-note.service";
import { EpicrisisService } from "@api-rest/services/epicrisis.service";
import { DocumentDeletionReasonComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/document-deletion-reason/document-deletion-reason.component";
import { AnamnesisService } from "@api-rest/services/anamnesis.service";
import { SnackBarService } from "@presentation/services/snack-bar.service";

@Injectable({
	providedIn: 'root'
})
export class DocumentActionsService {

	patientDocument: PatientDocument;
	userId: number;
	hasMedicalDischarge = false;

	constructor(
		private readonly accountService: AccountService,
		private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly dialog: MatDialog,
		private readonly anmanesisService: AnamnesisService,
		private readonly epicrisisService: EpicrisisService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
	) { }

	setInformation() {
		this.accountService.getInfo().subscribe((loggedUser: LoggedUserDto) => this.userId = loggedUser.id);
		this.internmentSummaryFacadeService.hasMedicalDischarge$.subscribe(m => this.hasMedicalDischarge = m);
	}

	setPatientDocuments(documents: DocumentSearchDto[]) {
		this.patientDocument = {
			hasAnamnesis: !!documents.find((document: DocumentSearchDto) => document.documentType === "Anamnesis"),
			evolutionNotes: documents.filter((document: DocumentSearchDto) => document.documentType === "Nota de evolución"),
			hasEpicrisis: !!documents.find((document: DocumentSearchDto) => document.documentType === "Epicrisis")
		}
	}

	canDoActionInTheDocument(document: DocumentSearchDto): boolean {
		if (document.creator.id !== this.userId)
			return false;
		const createdOn = dateTimeDtoToDate(document.createdOn);
		if (differenceInHours(new Date(), (new Date(createdOn))) > 24)
			return false;
		if (document.documentType === "Anamnesis") {
			const hasENAfterAnmanesis = !!this.patientDocument.evolutionNotes.find(e => dateTimeDtoToDate(document.createdOn) < dateTimeDtoToDate(e.createdOn));
			if (hasENAfterAnmanesis)
				return false;
		}
		if (document.documentType === "Nota de evolución")
			if (this.patientDocument?.hasEpicrisis)
				return false;
		if (document.documentType === "Epicrisis")
			if (this.hasMedicalDischarge)
				return false;
		return true;
	}

	loadTime(creteadOn: DateTimeDto): string {
		const date = new Date(dateTimeDtoToStringDate(creteadOn));
		let minutes: number | string = date.getMinutes();
		if (minutes < 10) {
			minutes = minutes.toString();
			minutes = `0${minutes}`;
		}
		return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()} - ${date.getHours()}:${minutes}hs`;
	}

	deleteDocument(document: DocumentSearchDto, internmentEpisodeId: number) {
		const dialogRef = this.dialog.open(DocumentDeletionReasonComponent, {
			data: {
				title: 'internaciones.dialogs.actions-document.DELETE_TITLE',
			},
			width: "50vh",
			autoFocus: false,
			disableClose: true
		});
		dialogRef.afterClosed().subscribe(reason => {
			if (reason) {
				switch (document.documentType) {
					case "Anamnesis":
						this.anmanesisService.deleteAnamnesis(document.id, internmentEpisodeId, reason).subscribe(
							success => {
								this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
								this.updateInformation();
							},
							error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
						break;
					case "Nota de evolución":
						this.evolutionNoteService.deleteEvolutionDiagnosis(document.id, internmentEpisodeId, reason).subscribe(
							success => {
								this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
								this.updateInformation();
							},
							error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
						break;
					case "Nota de evolución de enfermería":
						this.evolutionNoteService.deleteEvolutionDiagnosis(document.id, internmentEpisodeId, reason).subscribe(
							success => {
								this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
								this.updateInformation();
							},
							error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
						break;
					case "Epicrisis":
						this.epicrisisService.deleteEpicrisis(document.id, internmentEpisodeId, reason).subscribe(
							success => {
								this.snackBarService.showSuccess("internaciones.delete-document.messages.SUCCESS");
								this.updateInformation();
							},
							error => this.snackBarService.showError("internaciones.delete-document.messages.ERROR"))
						break;
				}
			}
		})
	}

	private updateInformation() {
		this.internmentSummaryFacadeService.setFieldsToUpdate({ evolutionClinical: true });
		this.internmentSummaryFacadeService.updateInternmentEpisode();
	}
}

export interface DocumentSearch {
	document: DocumentSearchDto;
	canDoAction?: boolean;
	createdOn: string;
}

interface PatientDocument {
	hasAnamnesis: boolean;
	evolutionNotes: DocumentSearchDto[];
	hasEpicrisis: boolean;
}
