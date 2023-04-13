import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { DOCUMENTS, } from '../../../../constants/summaries';
import { FormGroup, } from '@angular/forms';
import { TriageListDto, EmergencyCareHistoricDocumentDto, } from '@api-rest/api-model';
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
	public form: FormGroup;
	public activeDocument: any;

	public searchTriggered = false;
	public hasError = hasError;
	public minDate: Date;

	constructor(
		private readonly patientNameService: PatientNameService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly emergencyCareDocumentSearchService: EmergencyCareDocumentSearchService,
		private readonly newTriageService: NewTriageService
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
	}

	setActive(d: Item) {
		this.activeDocument = d.content
	}

	getFullName(firstName: string, nameSelfDetermination: string): string {
		return this.patientNameService.getPatientName(firstName, nameSelfDetermination)
	};

	private getHistoric() {
		this.emergencyCareDocumentSearchService.get(this.emergencyCareId)
			.subscribe((docs: EmergencyCareHistoricDocumentDto) => {
				this.documentHistoric = docs.triages.map(triage => map(triage.triage))
			})

		function map(d: TriageListDto): Item {
			return {
				summary: {
					createdBy: d.createdBy,
					icon: 'assignment_ind',
					createdOn: dateTimeDtoToDate(d.creationDate),
					title: 'TRIAGE'
				},
				content: GuardiaMapperService._mapTriageListDtoToTriage(d)
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

	},
	content: any
}
