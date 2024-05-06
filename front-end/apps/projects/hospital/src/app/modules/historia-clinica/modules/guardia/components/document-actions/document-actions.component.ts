import { Component, Input } from '@angular/core';
import { DocumentService } from '@api-rest/services/document.service';
import { Item } from '../emergency-care-evolutions/emergency-care-evolutions.component';
import { EmergencyCareEpisodeAdministrativeDischargeService } from '@api-rest/services/emergency-care-episode-administrative-service.service';
import { Observable } from 'rxjs/internal/Observable';
import { EmergencyCareEvolutionNoteService } from '@api-rest/services/emergency-care-evolution-note.service';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { NotaDeEvolucionData, NotaDeEvolucionDockPopupComponent } from '@historia-clinica/components/nota-de-evolucion-dock-popup/nota-de-evolucion-dock-popup.component';
import { EmergencyCareEvolutionNoteDto } from '@api-rest/api-model';

@Component({
	selector: 'app-document-actions',
	templateUrl: './document-actions.component.html',
	styleUrls: ['./document-actions.component.scss']
})
export class DocumentActionsComponent {

	private _episodeId: number;
	readonly TRIAGE = 'TRIAGE';
	hasAdministrativeDischarge$: Observable<boolean>;
	notaDeEvolucionDialogRef: DockPopupRef;
	@Input() patientId: number;
	@Input() document: Item;
	@Input() set episodeId(episodeId: number) {
		this._episodeId = episodeId;
		this.setAdministrativeDischarge();
	};

	constructor(
		private readonly documentService: DocumentService,
		private readonly emergencyCareEpisodeAdministrativeDischargeService: EmergencyCareEpisodeAdministrativeDischargeService,
		private readonly emergencyCareEvolutionNoteService: EmergencyCareEvolutionNoteService,
		private readonly dockPopupService: DockPopupService,
	) { }

	downloadDocument() {
		this.documentService.downloadFile({ filename: this.document.summary.docFileName, id: this.document.summary.docId });
	}

	editDocument() {
		this.emergencyCareEvolutionNoteService.getByDocumentId(this._episodeId, this.document.summary.docId).subscribe(evolutionNoteData => {
			this.openEvolutionNote(evolutionNoteData);
		});
	}

	private openEvolutionNote(evolutionNoteData: EmergencyCareEvolutionNoteDto) {
		if (!this.notaDeEvolucionDialogRef) {
			this.notaDeEvolucionDialogRef = this.dockPopupService.open(NotaDeEvolucionDockPopupComponent,  this.getNotaDeEvolucionData(evolutionNoteData));
			this.notaDeEvolucionDialogRef.afterClosed().subscribe(_ => {
				delete this.notaDeEvolucionDialogRef;
			})
		} else {
			if (this.notaDeEvolucionDialogRef.isMinimized())
				this.notaDeEvolucionDialogRef.maximize();
		}
	}

	private getNotaDeEvolucionData(emergencyCareEvolutionNote: EmergencyCareEvolutionNoteDto): NotaDeEvolucionData {
		return {
			patientId: this.patientId,
			episodeId: this._episodeId,
			editMode: true,
			emergencyCareEvolutionNote,
			documentId: this.document.summary.docId,
		}
	}

	private setAdministrativeDischarge() {
		this.hasAdministrativeDischarge$ = this.emergencyCareEpisodeAdministrativeDischargeService.hasAdministrativeDischarge(this._episodeId);
	}

}
