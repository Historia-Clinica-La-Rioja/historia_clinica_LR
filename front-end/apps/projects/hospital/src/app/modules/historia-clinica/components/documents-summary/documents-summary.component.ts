import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { DOCUMENTS, DOCUMENTS_SEARCH_FIELDS } from '../../constants/summaries';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, ValidationErrors, ValidatorFn } from '@angular/forms';
import {
	DocumentSearchFilterDto,
	EDocumentSearch,
	DocumentSearchDto,
	DocumentHistoricDto,
	MasterDataDto,
} from '@api-rest/api-model';
import { newDate } from '@core/utils/moment.utils';
import { hasError } from '@core/utils/form.utils';
import { pairwise, startWith } from 'rxjs/operators';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { DocumentActionsService, DocumentSearch } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service";
import { PatientNameService } from "@core/services/patient-name.service";
import { DeleteDocumentActionService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/delete-document-action.service';
import { EditDocumentActionService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/edit-document-action.service';
import { InternmentActionsService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-actions.service';
import { AmbulatoriaSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/services/ambulatoria-summary-facade.service';
import { InternacionMasterDataService } from "@api-rest/services/internacion-master-data.service";
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { fixDate } from '@core/utils/date/format';
import { ShowMoreConceptsPipe } from '@presentation/pipes/show-more-concepts.pipe';
import { capitalize } from '@core/utils/core.utils';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { convertDateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';

@Component({
	selector: 'app-documents-summary',
	templateUrl: './documents-summary.component.html',
	styleUrls: ['./documents-summary.component.scss'],
	providers: [DocumentActionsService, DeleteDocumentActionService, EditDocumentActionService]
})
export class DocumentsSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() set clinicalEvaluation(ce: DocumentHistoricDto) {
		this.documentHistoric = ce;
		if (this.documentHistoric) {
			this.updateDocuments();
		}
		this.activeDocument = undefined;
		this.internmentActions.popUpOpen$.subscribe(isOpened => this.isPopUpOpen = isOpened);
		this.ambulatoriaSummaryFacadeService.isNewConsultationOpen$.subscribe(isOpened => this.isPopUpOpen = isOpened);
	}
	@Input() patientId: number;
	@Input() internmentEpisodeAdmissionDatetime: Date;

	public searchFields: SearchField[] = DOCUMENTS_SEARCH_FIELDS;
	public documentTypes: MasterDataDto [] = [];
	public documentsToShow: DocumentData[] = [];
	public readonly documentsSummary = DOCUMENTS;
	public today = newDate();
	public form: UntypedFormGroup;
	public activeDocument: DocumentSearch;
	public documentHistoric: DocumentHistoricDto;
	public searchTriggered = false;
	public hasError = hasError;
	public minDate: Date;
	isPopUpOpen = false;

    public readonly DOCUMENT_TYPES = DocumentTypes;

	constructor(
		private formBuilder: UntypedFormBuilder,
		private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private changeDetectorRef: ChangeDetectorRef,
		private readonly documentActions: DocumentActionsService,
		private readonly patientNameService: PatientNameService,
		readonly internmentActions: InternmentActionsService,
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly internacionMasterDataService: InternacionMasterDataService
	) {
		this.form = this.formBuilder.group({
			text: [''],
			date: [null],
			field: [null],
			documentType: [null],
			mainDiagnosisOnly: [false],
			documentsWithoutDiagnosis: [false],
		}, {
			validators: this.filterFieldIsRequiredWhenInputIsSet()
		});
		this.internacionMasterDataService.getDocumentTypes().subscribe(dt => this.documentTypes = dt);
	}



	ngOnInit(): void {
		this.internmentSummaryFacadeService.initializeEvolutionNoteFilterResult(this.internmentEpisodeId);
		this.setInputResetBehaviour();
		this.documentActions.setInformation(this.patientId, this.internmentEpisodeId);
		this.minDate = this.internmentEpisodeAdmissionDatetime;
	}

	search(): void {
		if (this.form.valid) {
			this.searchTriggered = true;
			const searchFilter: DocumentSearchFilterDto = this.buildSearchFilter();
			this.internmentSummaryFacadeService.setSerchFilter(searchFilter);
		}
	}

	private buildSearchFilter(): DocumentSearchFilterDto {
		if (this.isDate(this.form.value.field) && !this.form.value.date) {
			return null;
		} else if (!this.isDate(this.form.value.field) && (this.form.value.text === '' || this.form.value.text === null)
				&& this.form.value.documentType === null) {
			return null;
		} else {
			return {
				plainText: this.getPlainText(),
				searchType: this.form.value.field,
			};
		}
	}

	private getPlainText(): string {
		if (this.isDate(this.form.value.field))
			return toApiFormat(fixDate(this.form.value.date))
		else if (this.form.value.field === 'DOCUMENT_TYPE')
			return this.form.value.documentType
		return this.form.value.text
	}

	private isDate(field): boolean {
		return field === 'CREATED_ON';
	}

	setActive(d: DocumentSearch) {
		this.activeDocument = {
			document: d.document,
			canDoAction: {
				delete: this.documentActions.canDeleteDocument(d.document),
				edit: this.documentActions.canEditDocument(d.document),
			}
		};
	}

	updateDocuments() {
		this.form.patchValue({ documentsWithoutDiagnosis: false })
		this.activeDocument = null;
		const documents = this.documentHistoric?.documents?.filter(document => {
			return this.form.value.mainDiagnosisOnly ? document.mainDiagnosis.length : true;
		});
		if (documents){
			this.documentActions.setPatientDocuments(documents);
			this.documentsToShow = documents.map(document => {
				return {
					documentSearch:	{ document },
					summaryAttentionData: this.buildSummaryAttentionData(document)
				}
			})
			this.changeDetectorRef.detectChanges();
		}
	}

	dateChanged(date: Date) {
		this.form.controls.date.setValue(date);
		this.search()
	}

	showDocumentsWithoutDiagnosis() {
		this.form.patchValue({ mainDiagnosisOnly: false })
		this.activeDocument = null;
		const documents = this.documentHistoric.documents.filter(document => {
			return this.form.value.documentsWithoutDiagnosis ? !document.diagnosis.length && !document.mainDiagnosis.length : true;
		});
		if (documents){
			this.documentActions.setPatientDocuments(documents);
			this.documentsToShow = documents.map(document => {
				return {
					documentSearch:	{ document },
					summaryAttentionData: this.buildSummaryAttentionData(document)
				}
			})
			this.changeDetectorRef.detectChanges();
		}
	}

	resetFilter(control: AbstractControl) {
		control.setValue(null);
		this.form.value.documentType = null;
		this.form.value.text = null;
		this.form.value.field = null;
		this.form.value.date = null;
		this.search();
	}

	setFilterValueAndSearchIfEmptyForm(control: AbstractControl, value: string) {
		control.setValue(value);
		if (this.form.value.text === '' && !this.form.value.field) {
			this.search();
		}
	}

	filterFieldIsRequiredWhenInputIsSet(): ValidatorFn {
		return (control: AbstractControl): ValidationErrors | null => {
			const text = control.get('text');
			const field = control.get('field');
			const error = (text.value !== '') && (field.value === null);
			field.setErrors(error ? { filterFieldIsRequiredWhenInputIsSet: true } : null);
			return null;
		};
	}

	delete(document: DocumentSearchDto) {
		this.documentActions.deleteDocument(document, this.internmentEpisodeId).subscribe(
			fieldsToUpdate => {
				if (fieldsToUpdate) {
					this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
					this.internmentSummaryFacadeService.updateInternmentEpisode();
				}
			}
		);
		this.activeDocument = undefined;
	}

	edit(document: DocumentSearchDto) {
		this.documentActions.editDocument(document, this.internmentEpisodeId);
		this.activeDocument = undefined;
	}

    resetActiveDocument() {
        this.activeDocument = undefined;
    }

	editDraftEpicrisis(document: DocumentSearchDto) {

		this.documentActions.editEpicrisisDraft(document);
		this.activeDocument = undefined;
	}

	private setInputResetBehaviour() {
		this.form.controls.field.valueChanges.pipe(
			startWith(null as string),
			pairwise()
		).subscribe(
			([oldValue, newValue]) => {
				if (oldValue && newValue) {
					this.form.controls.text.setValue('');
					this.form.controls.date.setValue(null);
				}
			}
		);
	}

	getFullName(firstName: string, nameSelfDetermination: string): string {
		return `${this.patientNameService.getPatientName(firstName, nameSelfDetermination)}`;
	}

	buildSummaryAttentionData(document: DocumentSearchDto): SummaryAttentionData {
		return {
			title: this.getDocumentType(document),
			problem: this.buildProblem(document),
			registerEditor: this.buildRegisterEditor(document)
		}
	}

	getDocumentType(document: DocumentSearchDto): string {
		if (document.documentType === this.DOCUMENT_TYPES.ANAMNESIS)
			return 'internaciones.documents-summary.document-name.ANAMNESIS';
		if ((document.documentType == this.DOCUMENT_TYPES.EPICRISIS || document.documentType == this.DOCUMENT_TYPES.ANESTHETIC_REPORT) && !document.confirmed)
			return document.documentType + ' (Borrador)';
		if (document.documentType !== this.DOCUMENT_TYPES.ANAMNESIS && document.confirmed)
			return document.documentType;
	}

	buildProblem(document: DocumentSearchDto): string {
		const showMoreConceptsPipe = new ShowMoreConceptsPipe();
		let problems: string[] = [];
		if (document.mainDiagnosis === null || document.mainDiagnosis === undefined || document.mainDiagnosis === '')
			problems = [...document.diagnosis];
		else
			problems = [document.mainDiagnosis, ...document.diagnosis];
		return problems.length ? capitalize(showMoreConceptsPipe.transform(problems)) : 'internaciones.documents-summary.NO_SNOMED_CONCEPT';
	}

	buildRegisterEditor(document: DocumentSearchDto): RegisterEditor {
		return {
			createdBy: this.getFullName(document.creator.firstName, document.creator.nameSelfDetermination) + " " + document.creator.lastName,
			date: convertDateTimeDtoToDate(document.createdOn)
		}
	}
}

export interface DocumentData {
	documentSearch: DocumentSearch,
	summaryAttentionData: SummaryAttentionData
}

export interface SummaryAttentionData {
	title: string,
	problem: string,
	registerEditor: RegisterEditor
}

export interface SearchField {
	field: EDocumentSearch;
	label: string;
}

enum DocumentTypes {
    ANESTHETIC_REPORT = 'Parte anestésico',
    EVOLUTION_NOTE = 'Nota de evolución',
    NURSE_EVOLUTION_NOTE = 'Nota de evolución de enfermería',
    ANAMNESIS = 'Anamnesis',
    EPICRISIS = 'Epicrisis',
	SURGICAL_REPORT = 'Parte quirúrgico de internación',
}
