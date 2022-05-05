import { Injectable } from '@angular/core';
import { DateTimeDto, DocumentSearchDto, LoggedUserDto } from "@api-rest/api-model";
import { differenceInHours } from "date-fns";
import { AccountService } from "@api-rest/services/account.service";
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { dateTimeDtoToDate, dateTimeDtoToStringDate } from "@api-rest/mapper/date-dto.mapper";

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
	) { }

	setInformation() {
		this.accountService.getInfo().subscribe((loggedUser: LoggedUserDto) => this.userId = loggedUser.id);
		this.internmentSummaryFacadeService.hasMedicalDischarge$.subscribe(m => this.hasMedicalDischarge = m);
	}

	setPatientDocuments(documents: DocumentSearchDto[]) {
		this.patientDocument = {
			hasAnamnesis: !!documents.find((document: DocumentSearchDto) => document.documentType === "Anamnesis"),
			hasEvolutionNote: !!documents.find((document: DocumentSearchDto) => document.documentType === "Nota de evolución"),
			hasEpicrisis: !!documents.find((document: DocumentSearchDto) => document.documentType === "Epicrisis")
		}
	}

	canDoActionInTheDocument(document: DocumentSearchDto): boolean {
		if (document.creator.id !== this.userId)
			return false;
		const createdOn = dateTimeDtoToDate(document.createdOn);
		if (differenceInHours(new Date(), (new Date(createdOn))) > 24)
			return false;
		if (document.documentType === "Anamnesis")
			if (this.patientDocument?.hasEvolutionNote)
				return false;
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
}

export interface DocumentSearch {
	document: DocumentSearchDto;
	canDoAction?: boolean;
	createdOn: string;
}

interface PatientDocument {
	hasAnamnesis: boolean;
	hasEvolutionNote: boolean;
	hasEpicrisis: boolean;
}
