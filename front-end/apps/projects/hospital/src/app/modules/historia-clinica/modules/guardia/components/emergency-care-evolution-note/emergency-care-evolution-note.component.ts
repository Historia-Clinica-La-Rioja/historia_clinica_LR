import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AppFeature, EmergencyCareEvolutionNoteDto } from '@api-rest/api-model';
import { EvolutionNoteAsViewFormat, EvolutionNoteSummaryService } from '../../services/evolution-note-summary.service';
import { NotaDeEvolucionDockPopupComponent, NotaDeEvolucionData } from '@historia-clinica/components/nota-de-evolucion-dock-popup/nota-de-evolucion-dock-popup.component';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { Observable, forkJoin, map } from 'rxjs';
import { Item } from '../emergency-care-evolutions/emergency-care-evolutions.component';
import { EmergencyCareEvolutionNoteService } from '@api-rest/services/emergency-care-evolution-note.service';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { EmergencyCareEpisodeAdministrativeDischargeService } from '@api-rest/services/emergency-care-episode-administrative-service.service';
import { DocumentService } from '@api-rest/services/document.service';
import { TranslateService } from '@ngx-translate/core';
import { HeaderDescription } from '@historia-clinica/utils/document-summary.model';
import { DocumentsSummaryService } from '@api-rest/services/documents-summary.service';
import { DocumentsSummaryMapperService } from '@historia-clinica/services/documents-summary-mapper.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
	selector: 'app-emergency-care-evolution-note',
	templateUrl: './emergency-care-evolution-note.component.html',
	styleUrls: ['./emergency-care-evolution-note.component.scss']
})
export class EmergencyCareEvolutionNoteComponent {
    
    @Input() set content(evolutionNote: Item) {
        this._evolutionNote = evolutionNote;
        this.fetchSummaryInfo();
    }
    @Input() patientId: number;
	@Input() set episodeId(episodeId: number) {
		this._episodeId = episodeId;
        this.fetchSummaryInfo();
	};
	@Output() resetActiveDocument = new EventEmitter<void>();
    private _episodeId: number;
	private evolutionNoteDialogRef: DockPopupRef;
    evolutionNoteSummary: EvolutionNoteAsViewFormat;
     _evolutionNote: Item;
    private documentName = '';
    documentSummary$: Observable<HeaderDescription>;
    isPopUpOpened = false;
	HABILITAR_EDICION_DOCUMENTOS_DE_GUARDIA = false;

    constructor(
		private readonly evolutionNoteSummaryService: EvolutionNoteSummaryService,
		private readonly emergencyCareEvolutionNoteService: EmergencyCareEvolutionNoteService,
		private readonly dockPopupService: DockPopupService,
        private readonly emergencyCareEpisodeAdministrativeDischargeService: EmergencyCareEpisodeAdministrativeDischargeService,
        private readonly documentService: DocumentService,
        private readonly translateService: TranslateService,
        private readonly documentSummaryService: DocumentsSummaryService,
        private readonly documentSummaryMapperService: DocumentsSummaryMapperService,
		private readonly featureFlag: FeatureFlagService
	) { 
        this.documentName = this.translateService.instant('guardia.documents-summary.document-name.EVOLUTION_NOTE');
		this.setFeatureFlags();
    }

    private fetchSummaryInfo(){
        if (this._evolutionNote && this._episodeId) {
            this.evolutionNoteSummary = this.evolutionNoteSummaryService.mapEvolutionNoteAsViewFormat(this._evolutionNote.content);
            
            let header$ = this.documentSummaryService.getEmergencyCareDocumentHeader(this._evolutionNote.summary.docId, this._episodeId);
            let medicalDischarge$ = this.emergencyCareEpisodeAdministrativeDischargeService.hasAdministrativeDischarge(this._episodeId);

            this.documentSummary$ = forkJoin([header$, medicalDischarge$]).pipe(map(([headerData, hasMedicalDischarge]) => {
                return this.documentSummaryMapperService.mapEmergencyCareToHeaderDescription(headerData, this.documentName, this.canEditEmergencyCareEvolutionNote(hasMedicalDischarge), false, true);
            }));
        }
    }
    
    downloadDocument() {
		this.documentService.downloadFile({ filename: this._evolutionNote.summary.docFileName, id: this._evolutionNote.summary.docId });
	}

    editDocument() {
		this.emergencyCareEvolutionNoteService.getByDocumentId(this._episodeId, this._evolutionNote.summary.docId).subscribe(evolutionNoteData => {
			this.openEvolutionNote(evolutionNoteData);
			this.resetActiveDocument.next();
		});
	}

	private canEditEmergencyCareEvolutionNote = (hasMedicalDischarge: boolean): boolean => {
		return !hasMedicalDischarge && this.HABILITAR_EDICION_DOCUMENTOS_DE_GUARDIA;
	}

	private setFeatureFlags = () => {
		this.featureFlag.isActive(AppFeature.HABILITAR_EDICION_DOCUMENTOS_DE_GUARDIA).subscribe((isActive: boolean) => this.HABILITAR_EDICION_DOCUMENTOS_DE_GUARDIA = isActive);
	}

	private openEvolutionNote(evolutionNoteData: EmergencyCareEvolutionNoteDto) {
		if (!this.evolutionNoteDialogRef) {
			this.evolutionNoteDialogRef = this.dockPopupService.open(NotaDeEvolucionDockPopupComponent,  this.getEvolutionNoteData(evolutionNoteData));
			this.evolutionNoteDialogRef.afterClosed().subscribe(_ => {
				delete this.evolutionNoteDialogRef;
			})
		} else {
			if (this.evolutionNoteDialogRef.isMinimized())
				this.evolutionNoteDialogRef.maximize();
		}
	}

	private getEvolutionNoteData(emergencyCareEvolutionNote: EmergencyCareEvolutionNoteDto): NotaDeEvolucionData {
		return {
			patientId: this.patientId,
			episodeId: this._episodeId,
			editMode: true,
			emergencyCareEvolutionNote,
			documentId: this._evolutionNote.summary.docId,
		}
	}
}