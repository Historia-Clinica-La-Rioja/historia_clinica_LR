import { Component, Input } from '@angular/core';
import { EmergencyCareEvolutionNoteDocumentDto } from '@api-rest/api-model';
import { EvolutionNoteAsViewFormat, EvolutionNoteSummaryService } from '../../services/evolution-note-summary.service';

@Component({
	selector: 'app-emergency-care-evolution-note',
	templateUrl: './emergency-care-evolution-note.component.html',
	styleUrls: ['./emergency-care-evolution-note.component.scss']
})
export class EmergencyCareEvolutionNoteComponent {
    
    @Input() set content(evolutionNote: EmergencyCareEvolutionNoteDocumentDto) {
        this.evolutionNoteSummary = this.evolutionNoteSummaryService.mapEvolutionNoteAsViewFormat(evolutionNote);
    }

    evolutionNoteSummary: EvolutionNoteAsViewFormat;

    constructor(
		private readonly evolutionNoteSummaryService: EvolutionNoteSummaryService
	) { }
}
