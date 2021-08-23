import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { SnomedDto } from "@api-rest/api-model";
import { TableColumnConfig } from "@presentation/components/document-section-table/document-section-table.component";
import { Observable, Subject } from "rxjs";
import { SnomedSemanticSearch, SnomedService } from "@historia-clinica/services/snomed.service";
import { CellTemplates } from "@presentation/components/cell-templates/cell-templates.component";
import { pushTo, removeFrom } from "@core/utils/array.utils";
import { Moment } from "moment";
import { newMoment } from "@core/utils/moment.utils";
import { ColumnConfig } from "@presentation/components/document-section/document-section.component";
import { SEMANTICS_CONFIG } from "@historia-clinica/constants/snomed-semantics";
import { hasError } from '@core/utils/form.utils';

export interface OtherOdontologyDiagnostic {
	snomed: SnomedDto;
	codigoSeveridad: string;
	cronico?: boolean;
	fechaInicio: Moment;
	fechaFin?: Moment;
}

export class OtherDiagnosticsNewConsultationService {

	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	private readonly form: FormGroup;
	private snomedConcept: SnomedDto;
	private readonly tableColumnConfig: TableColumnConfig[];
	private data: OtherOdontologyDiagnostic[];
	private errorSource = new Subject<string>();
	private _error$: Observable<string>;
	private severityTypes: any[];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService
	) {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			severidad: [null, Validators.required],
			cronico: [null],
			fechaInicio: [null, Validators.required],
			fechaFin: [null]
		});

		this.tableColumnConfig = [
			{
				def: 'diagnostics',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.PROBLEMA',
				template: CellTemplates.SNOMED_PROBLEM,
			},
			{
				def: 'severity',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.SEVERIDAD',
				text: (row) => this.getSeverityDisplayName(row.codigoSeveridad),
				template: CellTemplates.PROBLEM_SEVERITY
			},
			{
				def: 'dates',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.FECHA',
				template: CellTemplates.START_AND_END_DATE
			},
			{
				def: 'delete',
				template: CellTemplates.REMOVE_BUTTON,
				action: (rowIndex) => this.remove(rowIndex)
			},
		];

		this.data = [];
	}

	getSeverityDisplayName(codigoSeveridad) {
		return (codigoSeveridad && this.severityTypes) ?
			this.severityTypes.find(severityType => severityType.code === codigoSeveridad)?.display
			: '';
	}

	setSeverityTypes(severityTypes): void {
		this.severityTypes = severityTypes;
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	addDiagnostic(diagnostic: OtherOdontologyDiagnostic): void {
		this.data = pushTo<OtherOdontologyDiagnostic>(this.data, diagnostic);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const newDiagnostic: OtherOdontologyDiagnostic = {
				snomed: this.snomedConcept,
				codigoSeveridad: this.form.value.severidad,
				cronico: this.form.value.cronico,
				fechaInicio: this.form.value.fechaInicio,
				fechaFin: this.form.value.fechaFin
			};
			this.addDiagnostic(newDiagnostic);
			this.errorSource.next();
			this.resetForm();
		}
	}

	addDiagnosticToList(diagnostic: OtherOdontologyDiagnostic): void {
		this.addDiagnostic(diagnostic);
		this.form.controls.severidad.setValue(diagnostic.codigoSeveridad);
		this.form.controls.cronico.setValue(diagnostic.cronico);
		this.form.controls.fechaInicio.setValue(diagnostic.fechaInicio);
		this.form.controls.fechaFin?.setValue(diagnostic.fechaFin);
		this.form.controls.snomed.setValue(diagnostic.snomed.pt);
		this.snomedConcept = diagnostic.snomed;
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.SEMANTICS_CONFIG.diagnosis //TODO: change to custom ECL for other odontology diagnostics
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

	getFechaInicioMax(): Moment {
		return newMoment();
	}

	getForm(): FormGroup {
		return this.form;
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}

	getTableColumnConfig(): ColumnConfig[] {
		return this.tableColumnConfig;
	}

	getDiagnostics(): OtherOdontologyDiagnostic[] {
		return this.data;
	}

	remove(index: number): void {
		this.data = removeFrom<OtherOdontologyDiagnostic>(this.data, index);
	}

	// custom validation was required because the [max] input of MatDatepicker
	// adds the old error when the value is changed dynamically
	checkValidEndDate(): void {
		this.form.controls.fechaFin.setErrors(null);
		const today = newMoment();
		if (this.form.value.fechaFin) {
			const newEndDate: Moment = this.form.value.fechaFin;
			if (newEndDate.isBefore(this.form.value.fechaInicio)) {
				this.form.controls.fechaFin.setErrors({min: true});
			}
			if (newEndDate.isAfter(today)) {
				this.form.controls.fechaFin.setErrors({max: true});
			}
		}
	}

	hasError(type: string, controlName: string): boolean {
		return hasError(this.form, type, controlName);
	}

	get error$(): Observable<string> {
		if (!this._error$) {
			this._error$ = this.errorSource.asObservable();
		}
		return this._error$;
	}

	setError(errorMsg: string): void {
		this.errorSource.next(errorMsg);
	}
}
