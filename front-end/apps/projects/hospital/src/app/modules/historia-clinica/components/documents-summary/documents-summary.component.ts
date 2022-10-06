import { ChangeDetectorRef, Component, Input, OnChanges, OnInit } from '@angular/core';
import { DOCUMENTS, DOCUMENTS_SEARCH_FIELDS } from '../../constants/summaries';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Moment } from 'moment';
import {
	DocumentSearchFilterDto,
	EDocumentSearch,
	DocumentSearchDto,
	DocumentHistoricDto
} from '@api-rest/api-model';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { hasError } from '@core/utils/form.utils';
import { pairwise, startWith } from 'rxjs/operators';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { DocumentActionsService, DocumentSearch } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/document-actions.service";
import { PatientNameService } from "@core/services/patient-name.service";
import { DeleteDocumentActionService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/delete-document-action.service';
import { EditDocumentActionService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/edit-document-action.service';
import { fromStringToDate } from "@core/utils/date.utils";

@Component({
	selector: 'app-documents-summary',
	templateUrl: './documents-summary.component.html',
	styleUrls: ['./documents-summary.component.scss'],
	providers: [DocumentActionsService, DeleteDocumentActionService, EditDocumentActionService]
})
export class DocumentsSummaryComponent implements OnInit, OnChanges {

	@Input() internmentEpisodeId: number;
	@Input() clinicalEvaluation: DocumentHistoricDto;
	@Input() patientId: number;
	@Input() internmentEpisodeAdmissionDatetime: string;

	public searchFields: SearchField[] = DOCUMENTS_SEARCH_FIELDS;
	public documentsToShow: DocumentSearch[] = [];
	public readonly documentsSummary = DOCUMENTS;
	public today: Moment = newMoment();
	public form: FormGroup;
	public activeDocument: DocumentSearch;
	public documentHistoric: DocumentHistoricDto;
	public searchTriggered = false;
	public hasError = hasError;
	public minDate: Date;

	constructor(
		private formBuilder: FormBuilder,
		private internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private changeDetectorRef: ChangeDetectorRef,
		private readonly documentActions: DocumentActionsService,
		private readonly patientNameService: PatientNameService,
	) {
		this.form = this.formBuilder.group({
			text: [''],
			date: [null],
			field: [null],
			mainDiagnosisOnly: [false],
			documentsWithoutDiagnosis: [false],
		}, {
			validators: this.filterFieldIsRequiredWhenInputIsSet()
		});
	}

	ngOnChanges() {
		this.documentHistoric = this.clinicalEvaluation;
		if (this.documentHistoric) {
			this.updateDocuments();
		}
		this.activeDocument = undefined;
	}

	ngOnInit(): void {
		this.internmentSummaryFacadeService.initializeEvolutionNoteFilterResult(this.internmentEpisodeId);
		this.setInputResetBehaviour();
		this.documentActions.setInformation(this.patientId, this.internmentEpisodeId);
		this.minDate = fromStringToDate(this.internmentEpisodeAdmissionDatetime);
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
		} else if (!this.isDate(this.form.value.field) && this.form.value.text === '') {
			return null;
		} else {
			return {
				plainText: this.isDate(this.form.value.field) ? momentFormat(this.form.value.date, DateFormat.API_DATE)
					: this.form.value.text,
				searchType: this.form.value.field,
			};
		}
	}

	private isDate(field): boolean {
		return field === 'CREATED_ON';
	}

	setActive(d: DocumentSearch) {
		this.activeDocument = {
			document: d.document,
			canDoAction: {
				delete: this.documentActions.canDeleteDocument(d.document),
				edit: this.documentActions.canEditDocument(d.document)
			},
			createdOn: d.createdOn,
			editedOn: d.document.editedOn ? this.documentActions.loadTime(d.document.editedOn) : null
		};
	}

	updateDocuments() {
		this.form.patchValue({ documentsWithoutDiagnosis: false })
		this.activeDocument = null;
		const documents = this.documentHistoric.documents.filter(document => {
			return this.form.value.mainDiagnosisOnly ? document.mainDiagnosis.length : true;
		});
		this.documentActions.setPatientDocuments(documents);
		this.documentsToShow = documents.map(document => {
			return { document, createdOn: this.documentActions.loadTime(document.createdOn) }
		})
		this.changeDetectorRef.detectChanges();
	}

	showDocumentsWithoutDiagnosis() {
		this.form.patchValue({ mainDiagnosisOnly: false })
		this.activeDocument = null;
		const documents = this.documentHistoric.documents.filter(document => {
			return this.form.value.documentsWithoutDiagnosis ? !document.diagnosis.length && !document.mainDiagnosis.length : true;
		});
		this.documentActions.setPatientDocuments(documents);
		this.documentsToShow = documents.map(document => {
			return { document, createdOn: this.documentActions.loadTime(document.createdOn) }
		});
		this.changeDetectorRef.detectChanges();
	}

	viewEvolutionNote(): boolean {
		return !!(this.activeDocument?.document.notes || this.activeDocument?.document.procedures.length > 0);
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
		this.documentActions.editDocument(document);
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

}

export interface SearchField {
	field: EDocumentSearch;
	label: string;
}

