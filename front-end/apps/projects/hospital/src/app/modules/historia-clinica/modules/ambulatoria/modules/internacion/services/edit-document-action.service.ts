import { Injectable } from '@angular/core';
import { DocumentSearchDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { InternmentActionsService } from './internment-actions.service';
import { InternmentFields, InternmentSummaryFacadeService } from './internment-summary-facade.service';

@Injectable()
export class EditDocumentActionService {

	canConfirmedDocument = false;

	constructor(
		private readonly internmentActions: InternmentActionsService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly internmentStateService: InternmentStateService,
	) { }

	setInformation(patientId: number, internmentEpisodeId: number) {
		this.internmentActions.setInternmentInformation(patientId, internmentEpisodeId);
	}

	editDocument(document: DocumentSearchDto, internmentEpisodeId?: number) {
		if (document.documentType === "Anamnesis") {
			this.internmentActions.anamnesis$.subscribe(fieldsToUpdate => this.updateInternment(fieldsToUpdate));
			this.internmentActions.openAnamnesis(document.id);
		}
		if (document.documentType === "Nota de evolución" || document.documentType === "Nota de evolución de enfermería") {
			this.internmentStateService.getDiagnosesGeneralState(internmentEpisodeId).subscribe(diagnoses => {
				this.internmentActions.diagnosticos = diagnoses.filter(diagnosis => !diagnosis.main);
				this.internmentActions.evolutionNote$.subscribe(fieldsToUpdate => this.updateInternment(fieldsToUpdate));
				this.internmentActions.openEvolutionNote(document.id, document.documentType);
			});
		}
		if (document.documentType === "Epicrisis") {
			this.internmentActions.epicrisis$.subscribe(fieldsToUpdate => this.updateInternment(fieldsToUpdate));
			this.internmentActions.openEpicrisis(document.id);
		}
		if (document.documentType === "Parte quirúrgico de internación") {
			this.internmentActions.surgicalReport$.subscribe(fieldsToUpdate => this.updateInternment(fieldsToUpdate));
			this.internmentActions.openSurgicalReport(document.id);
		}
		if (document.documentType === "Parte anestésico") {
			this.internmentActions.anestheticReport$.subscribe(fieldsToUpdate => this.updateInternment(fieldsToUpdate));
			this.internmentActions.openAnestheticReport(document.id);
		}
	}

	editDraftEpicrisis(document: DocumentSearchDto, canConfirmedDocument: boolean) {
		this.canConfirmedDocument = canConfirmedDocument;
        this.internmentActions.epicrisis$.subscribe(fieldsToUpdate => this.updateInternment(fieldsToUpdate));
		this.internmentActions.openEpicrisis(document.id, true);
	}

	editDraftAnesthetic(document: DocumentSearchDto, canConfirmedDocument: boolean) {
		this.canConfirmedDocument = canConfirmedDocument;
		this.internmentActions.openAnestheticReport(document.id, true);
	}

	private updateInternment(fieldsToUpdate: InternmentFields) {
		if (fieldsToUpdate)
			this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
	}
}
