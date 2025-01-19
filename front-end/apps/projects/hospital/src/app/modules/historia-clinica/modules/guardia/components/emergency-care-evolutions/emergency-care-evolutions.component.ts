import { ChangeDetectorRef, Component, Input, OnChanges, OnInit } from '@angular/core';
import { EmergencyCareHistoricDocumentDto, TriageDocumentDto, EmergencyCareEvolutionNoteDocumentDto, EEmergencyCareEvolutionNoteType } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';
import { DocumentActionsService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service";
import { PatientNameService } from "@core/services/patient-name.service";
import { DeleteDocumentActionService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/delete-document-action.service';
import { EditDocumentActionService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/edit-document-action.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareTypes } from '../../constants/masterdata';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { EmergencyCareDocumentSearchService } from '@api-rest/services/emergency-care-document-search.service';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { NewEmergencyCareEvolutionNoteService } from '../../services/new-emergency-care-evolution-note.service';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
	selector: 'app-emergency-care-evolutions',
	templateUrl: './emergency-care-evolutions.component.html',
	styleUrls: ['./emergency-care-evolutions.component.scss'],
	providers: [DocumentActionsService, DeleteDocumentActionService, EditDocumentActionService]
})
export class EmergencyCareEvolutionsComponent implements OnInit, OnChanges {

	readonly HEADER: SummaryHeader = { matIcon: 'assignment', title: 'Evoluciones de guardia' }

	@Input('emergencyCareId') emergencyCareId: number;
	@Input() patientId: number;

    public readonly DOCUMENT_TYPES = EmergencyCaraDocumentTypes;
	documentHistoric: Item[];
	selectedTriage;
	emergencyCareType: EmergencyCareTypes | string;
	public activeDocument: Item;

	public searchTriggered = false;
	public hasError = hasError;
	public minDate: Date;

	constructor(
		private readonly translateService: TranslateService,
		private readonly patientNameService: PatientNameService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly emergencyCareDocumentSearchService: EmergencyCareDocumentSearchService,
		private readonly newTriageService: NewTriageService,
		private readonly newEmergencyCareEvolutionNoteService: NewEmergencyCareEvolutionNoteService,
        private changeDetectorRef: ChangeDetectorRef,
	) {
	}

	ngOnChanges() {
		this.resetActiveDocument();
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

	setActive(d: Item) {
		this.activeDocument = d
	}

    resetActiveDocument() {
        this.activeDocument = undefined;
    }

	getFullName(firstName: string, nameSelfDetermination: string): string {
		return this.patientNameService.getPatientName(firstName, nameSelfDetermination)
	};

	private getHistoric() {
        this.resetActiveDocument();
		this.emergencyCareDocumentSearchService.get(this.emergencyCareId)
			.subscribe((docs: EmergencyCareHistoricDocumentDto) => {
				const triages = docs.triages.map(triage => this.map(triage));
				const evolutionsNotes = docs.evolutionNotes.map(evolutionNote => this.evolutionNotesMapper(evolutionNote));
				const allDocs = triages.concat(evolutionsNotes);
				this.documentHistoric = allDocs.sort((a, b) => b.summary.createdOn.getTime() - a.summary.createdOn.getTime() );
                this.changeDetectorRef.detectChanges();
			})	
	}

    private evolutionNotesMapper(en: EmergencyCareEvolutionNoteDocumentDto): Item {
        const fullName = this.getFullName(en.professional.person.firstName, en.professional.nameSelfDetermination) + " " +  en.professional.person.lastName;
        return {
            summary: {
                createdBy: {
                    firstName: en.professional.person.firstName,
                    lastName: en.professional.person.lastName,
                    nameSelfDetermination: en.professional.nameSelfDetermination
                },
                registerEditor: this.buildRegisterEditor(fullName, dateTimeDtoToDate(en.performedDate)),
                icon: 'assignment',
                createdOn: dateTimeDtoToDate(en.performedDate),
				title: en.type === EEmergencyCareEvolutionNoteType.DOCTOR ? this.translateService.instant('internaciones.documents-summary.document-name.DOCTOR_EVOLUTION_NOTE') : this.translateService.instant('guardia.actions.EVOLUTION_NOTE_BY_NURSE'),
                docId: en.documentId,
                docFileName: en.fileName,
                specialty: en.clinicalSpecialtyName
            },
            content: en
        }
    }

    private map(doc: TriageDocumentDto): Item {
        const fullName = this.getFullName(doc.triage.createdBy.firstName, doc.triage.createdBy.nameSelfDetermination) + " " +  doc.triage.createdBy.lastName;
        return {
            summary: {
                createdBy: doc.triage.createdBy,
                registerEditor: this.buildRegisterEditor(fullName, dateTimeDtoToDate(doc.triage.creationDate),),
                icon: 'assignment_ind',
                createdOn: dateTimeDtoToDate(doc.triage.creationDate),
                title: this.translateService.instant('internaciones.documents-summary.document-name.TRIAGE'),
                docId: doc.documentId,
                docFileName: doc.fileName
            },
            content: doc.triage,
        }
    }

	buildRegisterEditor(createdBy: string, date: Date): RegisterEditor {
		return {
			createdBy: createdBy,
			date: date
		}
	}
}

export interface Item {
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
		specialty?: string,
		registerEditor: RegisterEditor,
		docId: number,
		docFileName: string

	},
	content: any
}

enum EmergencyCaraDocumentTypes {
    TRIAGE = 'Triage',
    EVOLUTION_NOTE = 'Nota de evolución médica',
	NURSING_EVOLUTION_NOTE = 'Nota de evolución de enfermería',
}