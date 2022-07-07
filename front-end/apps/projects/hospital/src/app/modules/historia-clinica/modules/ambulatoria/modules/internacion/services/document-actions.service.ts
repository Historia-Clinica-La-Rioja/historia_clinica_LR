import { Injectable } from '@angular/core';
import { DateTimeDto, DocumentSearchDto, LoggedUserDto } from "@api-rest/api-model";
import { differenceInHours } from "date-fns";
import { AccountService } from "@api-rest/services/account.service";
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { dateTimeDtoToDate, dateTimeDtoToStringDate } from "@api-rest/mapper/date-dto.mapper";
import { DeleteDocumentActionService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/delete-document-action.service";
import { EditDocumentActionService } from './edit-document-action.service';

@Injectable({
	providedIn: 'root'
})
export class DocumentActionsService {

	patientDocument: PatientDocument;
	userId: number;
	hasMedicalDischarge = false;
	hasPhysicalDischarge = false;

	constructor(
		private readonly accountService: AccountService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly deleteDocumentAction: DeleteDocumentActionService,
		private readonly editDocumentAction: EditDocumentActionService,
	) { }

	setInformation(patientId: number, internmentEpisodeId: number) {
		this.accountService.getInfo().subscribe((loggedUser: LoggedUserDto) => this.userId = loggedUser.id);
		this.internmentSummaryFacadeService.hasMedicalDischarge$.subscribe(m => this.hasMedicalDischarge = m);
		this.internmentSummaryFacadeService.hasPhysicalDischarge$.subscribe(p => this.hasPhysicalDischarge = p);
		this.editDocumentAction.setInformation(patientId, internmentEpisodeId);
	}

	setPatientDocuments(documents: DocumentSearchDto[]) {
		this.patientDocument = {
			hasAnamnesis: !!documents.find((document: DocumentSearchDto) => document.documentType === "Anamnesis"),
			evolutionNotes: documents.filter((document: DocumentSearchDto) => document.documentType === "Nota de evolución" || document.documentType === "Nota de evolución de enfermería"),
			hasEpicrisis: !!documents.find((document: DocumentSearchDto) => document.documentType === "Epicrisis")
		}
	}

	canEditDocument(document: DocumentSearchDto): boolean {
		if (!this.isCreatorDocumnt(document))
			return false;
		const createdOn = dateTimeDtoToDate(document.createdOn);
		if (differenceInHours(new Date(), (new Date(createdOn))) > 24)
			return false;
		if (this.hasMedicalDischarge)
			return false;
		return true;
	}

	isCreatorDocumnt(document: DocumentSearchDto): boolean {
		return (document.creator.userId === this.userId);
	}

	canDeleteDocument(document: DocumentSearchDto): boolean {
		if (!this.canEditDocument(document))
			return false;
		if (document.documentType === "Anamnesis") {
			if (this.hasPhysicalDischarge) {
				return false;
			}
			const hasENAfterAnmanesis = !!this.patientDocument.evolutionNotes.find(e => dateTimeDtoToDate(document.createdOn) < dateTimeDtoToDate(e.createdOn));
			if ((hasENAfterAnmanesis) || (this.patientDocument?.hasEpicrisis))
				return false;
		}
		if ((document.documentType === "Nota de evolución") || (document.documentType === "Nota de evolución de enfermería"))
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
		this.deleteDocumentAction.delete(document, internmentEpisodeId);
	}

	editDocument(document: DocumentSearchDto) {
		this.editDocumentAction.editDocument(document)
	}

	editEpicrisisDraft(document: DocumentSearchDto) {
		this.editDocumentAction.editDraftEpicrisis(document,this.isCreatorDocumnt(document));
	}

}

export interface DocumentSearch {
	document: DocumentSearchDto;
	canDoAction?: DocumentAction;
	createdOn: string;
	editedOn?: string;
}

interface DocumentAction {
	delete: boolean;
	edit: boolean;
}

interface PatientDocument {
	hasAnamnesis: boolean;
	evolutionNotes: DocumentSearchDto[];
	hasEpicrisis: boolean;
}
