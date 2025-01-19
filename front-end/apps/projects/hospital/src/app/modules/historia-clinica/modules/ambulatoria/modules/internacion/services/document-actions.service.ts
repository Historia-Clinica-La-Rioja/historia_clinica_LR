import { Injectable } from '@angular/core';
import { DocumentSearchDto, LoggedUserDto, PatientDischargeDto } from "@api-rest/api-model";
import { differenceInHours } from "date-fns";
import { AccountService } from "@api-rest/services/account.service";
import { dateTimeDtoToDate, dateTimeDtotoLocalDate } from "@api-rest/mapper/date-dto.mapper";
import { DeleteDocumentActionService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/delete-document-action.service";
import { EditDocumentActionService } from './edit-document-action.service';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { Observable } from 'rxjs';
import { InternmentFields } from './internment-summary-facade.service';
import { InternmentActionsService } from './internment-actions.service';

@Injectable()
export class DocumentActionsService {

	patientDocument: PatientDocument;
	userId: number;
	hasMedicalDischarge = false;
	hasPhysicalDischarge = false;
    hasAdministrativeDischarge = false;

	constructor(
		private readonly accountService: AccountService,
		private readonly deleteDocumentAction: DeleteDocumentActionService,
		private readonly editDocumentAction: EditDocumentActionService,
		private readonly internmentEpisodeService: InternmentEpisodeService,
		readonly internmentActions: InternmentActionsService,
	) {
		this.internmentActions.medicalDischarge$.subscribe(medicalDischarge => {
			if (medicalDischarge)
				this.hasMedicalDischarge = true;
		});
	}

	setInformation(patientId: number, internmentEpisodeId: number) {
		this.accountService.getInfo().subscribe((loggedUser: LoggedUserDto) => this.userId = loggedUser.id);
		this.internmentEpisodeService.getPatientDischarge(internmentEpisodeId)
			.subscribe((patientDischarge: PatientDischargeDto) => {
				this.hasMedicalDischarge = !!patientDischarge.medicalDischargeDate;
				this.hasPhysicalDischarge = !!patientDischarge.physicalDischargeDate;
                this.hasAdministrativeDischarge = !!patientDischarge.administrativeDischargeDate;
			});
		this.editDocumentAction.setInformation(patientId, internmentEpisodeId);
	}

	setPatientDocuments(documents: DocumentSearchDto[]) {
		this.patientDocument = {
			hasAnamnesis: !!documents.find((document: DocumentSearchDto) => document.documentType === "Anamnesis"),
			evolutionNotes: documents.filter((document: DocumentSearchDto) => document.documentType === "Nota de evolución" || document.documentType === "Nota de evolución de enfermería"),
			hasEpicrisis: !!documents.find((document: DocumentSearchDto) => document.documentType === "Epicrisis"),
			hasAnestheticPart: !!documents.find((document: DocumentSearchDto) => document.documentType === "Parte anestésico"),
		}
	}

	canEditDocument(document: DocumentSearchDto): boolean {
		if (!this.isCreatorDocumnt(document))
			return false;
		const createdOn = dateTimeDtotoLocalDate(document.createdOn);
		if (differenceInHours(new Date(), (new Date(createdOn))) > 24)
			return false;
        if(document.documentType === "Parte anestésico" || document.documentType === "Parte quirúrgico de internación" ) {
            if(this.hasMedicalDischarge && !this.hasAdministrativeDischarge){
                return true;
            }
        }
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

	deleteDocument(document: DocumentSearchDto, internmentEpisodeId: number): Observable<InternmentFields> {
		this.deleteDocumentAction.delete(document, internmentEpisodeId);
		return this.deleteDocumentAction.updateFields$;
	}

	editDocument(document: DocumentSearchDto, internmentEpisodeId: number) {
		this.editDocumentAction.editDocument(document, internmentEpisodeId)
	}

	editEpicrisisDraft(document: DocumentSearchDto) {
		this.editDocumentAction.editDraftEpicrisis(document, this.isCreatorDocumnt(document));
	}

	editAnestheticPartDraft(document: DocumentSearchDto) {
		this.editDocumentAction.editDraftAnesthetic(document, this.isCreatorDocumnt(document));
	}
}

export interface DocumentSearch {
	document: DocumentSearchDto;
	canDoAction?: DocumentAction;
}

interface DocumentAction {
	delete: boolean;
	edit: boolean;
}

interface PatientDocument {
	hasAnamnesis: boolean;
	evolutionNotes: DocumentSearchDto[];
	hasEpicrisis: boolean;
	hasAnestheticPart: boolean;
}
