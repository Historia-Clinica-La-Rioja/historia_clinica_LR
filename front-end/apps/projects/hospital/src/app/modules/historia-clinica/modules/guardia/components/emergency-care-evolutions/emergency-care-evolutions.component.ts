import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { DOCUMENTS, } from '../../../../constants/summaries';
import { UntypedFormGroup, } from '@angular/forms';
import { EmergencyCareHistoricDocumentDto, TriageDocumentDto, EmergencyCareEvolutionNoteDocumentDto, } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';
import { DocumentActionsService, DocumentSearch } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service";
import { PatientNameService } from "@core/services/patient-name.service";
import { DeleteDocumentActionService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/delete-document-action.service';
import { EditDocumentActionService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/edit-document-action.service';
import { GuardiaMapperService } from '@historia-clinica/modules/guardia/services/guardia-mapper.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareTypes } from '../../constants/masterdata';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { EmergencyCareDocumentSearchService } from '@api-rest/services/emergency-care-document-search.service';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { DocumentService } from '@api-rest/services/document.service';
import { NewEmergencyCareEvolutionNoteService } from '../../services/new-emergency-care-evolution-note.service';

@Component({
	selector: 'app-emergency-care-evolutions',
	templateUrl: './emergency-care-evolutions.component.html',
	styleUrls: ['./emergency-care-evolutions.component.scss'],
	providers: [DocumentActionsService, DeleteDocumentActionService, EditDocumentActionService]
})
export class EmergencyCareEvolutionsComponent implements OnInit, OnChanges {

	readonly HEADER: SummaryHeader = { matIcon: 'assignment', title: 'Evoluciones de guardia' }

	@Input('emergencyCareId') emergencyCareId: number

	documentHistoric: Item[];
	selectedTriage;
	emergencyCareType: EmergencyCareTypes | string;

	public documentsToShow: DocumentSearch[] = [];
	public readonly documentsSummary = DOCUMENTS;
	public form: UntypedFormGroup;
	public activeDocument: Item;

	public searchTriggered = false;
	public hasError = hasError;
	public minDate: Date;

	constructor(
		private readonly patientNameService: PatientNameService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly emergencyCareDocumentSearchService: EmergencyCareDocumentSearchService,
		private readonly newTriageService: NewTriageService,
		private readonly documentService: DocumentService,
		private readonly newEmergencyCareEvolutionNoteService: NewEmergencyCareEvolutionNoteService
	) {
	}

	ngOnChanges() {
		if (this.documentHistoric) {
			/* 	this.updateDocuments(); */
		}
		this.activeDocument = undefined;
	}


	ngOnInit(): void {
		this.emergencyCareEpisodeService.getAdministrative(this.emergencyCareId)
			.subscribe(
				administrative => {
					this.emergencyCareType = administrative.emergencyCareType?.id
				}
			);
		this.getHistoric();

		this.newTriageService.newTriage$.subscribe(_ => {
			this.getHistoric();
		})

		this.newEmergencyCareEvolutionNoteService.new$.subscribe( _ => this.getHistoric())
	}

	downloadDocument() {
		this.documentService.downloadFile({ filename: this.activeDocument.summary.docFileName, id: this.activeDocument.summary.docId });
	}

	setActive(d: Item) {
		this.activeDocument = d
	}

	getFullName(firstName: string, nameSelfDetermination: string): string {
		return this.patientNameService.getPatientName(firstName, nameSelfDetermination)
	};

	private getHistoric() {
		this.emergencyCareDocumentSearchService.get(this.emergencyCareId)
			.subscribe((docs: EmergencyCareHistoricDocumentDto) => {
				const triages = docs.triages.map(map);
				const evolutionsNotes = docs.evolutionNotes.map(evolutionNotesMapper);
				const allDocs = triages.concat(evolutionsNotes);
				this.documentHistoric = allDocs.sort((a, b) => b.summary.createdOn.getTime() - a.summary.createdOn.getTime() );
			})

		function evolutionNotesMapper(en: EmergencyCareEvolutionNoteDocumentDto): Item {
			return {
				summary: {
					createdBy: {
						firstName: en.professional.person.firstName,
						lastName: en.professional.person.lastName,
						nameSelfDetermination: en.professional.nameSelfDetermination
					},
					icon: 'assignment',
					createdOn: dateTimeDtoToDate(en.performedDate),
					title: 'NOTA DE EVOLUCION MEDICA',
					docId: en.documentId,
					docFileName: en.fileName
				},
				content: en
			}
		}

		function map(doc: TriageDocumentDto): Item {
			return {
				summary: {
					createdBy: doc.triage.createdBy,
					icon: 'assignment_ind',
					createdOn: dateTimeDtoToDate(doc.triage.creationDate),
					title: 'TRIAGE',
					docId: doc.documentId,
					docFileName: doc.fileName
				},
				content: GuardiaMapperService._mapTriageListDtoToTriage(doc.triage)
			}
		}
	}
}

interface Item {
	summary: {
		icon: string,
		title: string,
		subtitle?: string,
		createdOn: Date,
		createdBy: {
			firstName: string,
			lastName: string,
			nameSelfDetermination: string
		},
		docId: number,
		docFileName: string

	},
	content: any
}
