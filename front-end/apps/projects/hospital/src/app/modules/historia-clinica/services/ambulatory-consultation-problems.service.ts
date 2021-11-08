import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SnomedSemanticSearch, SnomedService } from './snomed.service';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { hasError } from '@core/utils/form.utils';
import { Observable, Subject } from 'rxjs';
import { TableColumnConfig } from '@presentation/components/document-section-table/document-section-table.component';
import { CellTemplates } from '@presentation/components/cell-templates/cell-templates.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { MatDialog } from '@angular/material/dialog';
import { EpidemiologicalReportComponent } from '@historia-clinica/modules/ambulatoria/dialogs/epidemiological-report/epidemiological-report.component';

export interface AmbulatoryConsultationProblem {
	snomed: SnomedDto;
	codigoSeveridad?: string;
	cronico?: boolean;
	fechaInicio?: Moment;
	fechaFin?: Moment;
	reportProblem?: any;
}

export class AmbulatoryConsultationProblemsService {

	private readonly form: FormGroup;
	private snomedConcept: SnomedDto;
	private readonly columns: TableColumnConfig[];
	private data: AmbulatoryConsultationProblem[];
	private errorSource = new Subject<string>();
	private _error$: Observable<string>;
	private severityTypes: any[];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly snowstormService: SnowstormService,
		private readonly dialog: MatDialog,

	) {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			severidad: [null],
			cronico: [null],
			fechaInicio: [newMoment()],
			fechaFin: [null]
		});

		this.columns = [
			{
				def: 'diagnosticos',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.PROBLEMA',
				template: CellTemplates.SNOMED_PROBLEM,
			},
			{
				def: 'severidad',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.SEVERIDAD',
				text: (row) => this.getSeverityDisplayName(row.codigoSeveridad),
				template: CellTemplates.PROBLEM_SEVERITY
			},
			{
				def: 'fecha',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.FECHA',
				template: CellTemplates.START_AND_END_DATE
			},
			{
				def: 'eliminar',
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

	add(problema: AmbulatoryConsultationProblem): boolean {
		const currentItems = this.data.length;
		this.data = pushIfNotExists<AmbulatoryConsultationProblem>(this.data, problema, this.compareSpeciality);
		return currentItems === this.data.length;
	}

	addControl(problema: AmbulatoryConsultationProblem): void {
		if (this.add(problema))
			this.snackBarService.showError("Problema duplicado");
	}

	compareSpeciality(data: AmbulatoryConsultationProblem, data1: AmbulatoryConsultationProblem): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}

	addToList(reportProblemIsOn: boolean) {
		if (this.form.valid && this.snomedConcept) {
			const nuevoProblema: AmbulatoryConsultationProblem = {
				snomed: this.snomedConcept,
				codigoSeveridad: this.form.value.severidad,
				cronico: this.form.value.cronico,
				fechaInicio: this.form.value.fechaInicio,
				fechaFin: this.form.value.fechaFin
			};
			if (reportProblemIsOn) {
				this.snowstormService.getIsReportable({ sctid: nuevoProblema.snomed.sctid, pt: nuevoProblema.snomed.pt }).subscribe(
					(isReportable: boolean) => {
						if (isReportable) {
							const dialogRef = this.dialog.open(EpidemiologicalReportComponent, {
								disableClose: true,
								autoFocus: false,
								data: nuevoProblema.snomed.pt.includes("dengue")
							});
							dialogRef.afterClosed().subscribe((reportProblem: boolean) => {
								nuevoProblema.reportProblem = reportProblem;
								if (nuevoProblema.reportProblem != null) {
									this.addControlAndResetForm(nuevoProblema);
								}
							})
						}
						else {
							this.addControlAndResetForm(nuevoProblema);
						}
					}
				);
			}
			else {
				this.addControlAndResetForm(nuevoProblema);
			}
		}
	}

	addProblemToList(problema: AmbulatoryConsultationProblem): void {
		this.add(problema);
		this.form.controls.severidad.setValue(problema.codigoSeveridad);
		this.form.controls.cronico.setValue(problema.cronico);
		this.form.controls.fechaInicio.setValue(problema.fechaInicio);
		this.form.controls.fechaFin?.setValue(problema.fechaFin);
		this.form.controls.snomed.setValue(problema.snomed.pt);
		this.snomedConcept = problema.snomed;
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.DIAGNOSIS
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

	getColumns(): ColumnConfig[] {
		return this.columns;
	}

	getProblemas(): AmbulatoryConsultationProblem[] {
		return this.data;
	}

	remove(index: number): void {
		this.data = removeFrom<AmbulatoryConsultationProblem>(this.data, index);
	}

	// custom validation was required because the [max] input of MatDatepicker
	// adds the old error when the value is changed dynamically
	checkValidFechaFin(): void {
		this.form.controls.fechaFin.setErrors(null);
		if (this.form.value.fechaFin) {
			if (this.form.value.fechaInicio) {
				const today = newMoment();
				const newFechaFin: Moment = this.form.value.fechaFin;
				if (newFechaFin.isBefore(this.form.value.fechaInicio, 'day')) {
					this.form.controls.fechaFin.setErrors({ min: true });
				}
				if (newFechaFin.isAfter(today)) {
					this.form.controls.fechaFin.setErrors({ max: true });
				}
			} else {
				this.form.controls.fechaFin.setErrors({ required_init_date: true });
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

	editProblem(): boolean {
		// tg-1302
		// in this case, there's one and only one health condition
		if (this.form.valid) {
			this.getProblemas()[0].snomed.pt = this.form.controls.snomed.value;
			this.getProblemas()[0].cronico = this.form.controls.cronico.value;
			this.getProblemas()[0].codigoSeveridad = this.form.controls.severidad.value;
			this.getProblemas()[0].fechaInicio = this.form.controls.fechaInicio.value;
			this.getProblemas()[0].fechaFin = this.form.controls.fechaFin.value;
			this.resetForm();
			return true;
		}
		return false;
	}

	private addControlAndResetForm(nuevoProblema: AmbulatoryConsultationProblem) {
		this.addControl(nuevoProblema);
		this.errorSource.next();
		this.resetForm();
	}
}

