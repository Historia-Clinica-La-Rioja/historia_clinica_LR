import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from './snomed.service';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { dateISOParseDate } from '@core/utils/moment.utils';
import { TableColumnConfig } from '@presentation/components/document-section-table/document-section-table.component';
import { CellTemplates } from '@presentation/components/cell-templates/cell-templates.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

export interface Procedimiento {
	snomed: SnomedDto;
	performedDate?: Date;
}

export class ProcedimientosService {

	private form: UntypedFormGroup;
	private snomedConcept: SnomedDto;
	private readonly columns: ColumnConfig[];
	private readonly tableColumnConfig: TableColumnConfig[];
	private data: any[];
	private ECL = SnomedECL.PROCEDURE;
	private hasProcedure = new BehaviorSubject<boolean>(true);

	emitter = new Subject();
	procedimientos$ = this.emitter.asObservable()

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly dateFormatPipe: DateFormatPipe

	) {

		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			performedDate: [null],
		});

		this.columns = [
			{
				def: 'procedimiento',
				header: 'historia-clinica.procedimientos.PROCEDIMIENTO',
				text: (row) => row.snomed.pt
			},
			{
				def: 'fecha',
				header: 'historia-clinica.procedimientos.FECHA',
				text: (row: Procedimiento) => row.performedDate ? this.dateFormatPipe.transform(row.performedDate, 'date') : ''
			}

		];

		this.tableColumnConfig = [
			{
				def: 'procedimiento',
				header: 'historia-clinica.procedimientos.PROCEDIMIENTO',
				template: CellTemplates.SNOMED_PROBLEM
			},
			{
				def: 'fecha',
				header: 'historia-clinica.procedimientos.FECHA',
				template: CellTemplates.TEXT,
				text: (row) => row.performedDate ? this.dateFormatPipe.transform(dateISOParseDate(row.performedDate), 'date') : ''
			},
			{
				def: 'delete',
				template: CellTemplates.REMOVE_BUTTON,
				action: (rowIndex) => this.remove(rowIndex)
			}


		];
		this.data = [];
	}


	setConcept(selectedConcept: SnomedDto): void {
		this.hasProcedure.next(this.isEmpty());
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	setAndAddConcept(selectedConcept: SnomedDto): void {
		this.setConcept(selectedConcept)
		this.addToList();
	}

	setECL(SnomedECL): void {
		this.ECL = SnomedECL;
	}

	add(procedimiento: Procedimiento): boolean {
		this.hasProcedure.next(this.isEmpty());
		const currentItems = this.data.length;
		this.data = pushIfNotExists<Procedimiento>(this.data, procedimiento, this.compareSpeciality);
		this.emitter.next(this.data);
		return currentItems === this.data.length;
	}

	addControl(procedimiento: Procedimiento): void {
		this.hasProcedure.next(this.isEmpty());
		if (this.add(procedimiento)) {
			this.snackBarService.showError("Procedimiento duplicado");
		}
	}

	compareSpeciality(data: Procedimiento, data1: Procedimiento): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}

	addToList(): boolean {
		if (this.form.valid && this.snomedConcept) {
			const nuevoProcedimiento: Procedimiento = {
				snomed: this.snomedConcept,
				performedDate: this.form.value.performedDate || undefined
			};
			this.addControl(nuevoProcedimiento);
			this.resetForm();
			return true;
		}
		return false;
	}

	removeProcedimiento(index: number): void {
		this.hasProcedure.next(this.isEmpty());
		this.data = removeFrom<Procedimiento>(this.data, index);
	}

	resetForm(): void {
		this.hasProcedure.next(this.isEmpty());
		delete this.snomedConcept;
		this.form.reset();
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.ECL
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

	openSearchDialogAndAddConcept(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.ECL
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setAndAddConcept(selectedConcept));
		}
	}

	getForm(): UntypedFormGroup {
		return this.form;
	}

	getColumns(): ColumnConfig[] {
		return this.columns;
	}

	getProcedimientos(): Procedimiento[] {
		return this.data;
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}

	getFechaMax(): Date {
		return new Date();
	}

	remove(index: number): void {
		this.hasProcedure.next(false);
		this.data = removeFrom<Procedimiento>(this.data, index);
		this.emitter.next(this.data)
	}

	removeAll(): void {
		this.data = [];
	}

	getTableColumnConfig(): TableColumnConfig[] {
		return this.tableColumnConfig;
	}

	getECL(): SnomedECL {
		return this.ECL;
	}

	isEmpty(): boolean {
		return (!this.data || this.data.length === 0);
	}

	isEmptyProcedure(): Observable<boolean> {
		return this.hasProcedure.asObservable();
	}

	setProcedures(procedures: Procedimiento[]) {
		procedures.forEach(procedure => {
			this.form.controls.performedDate.setValue(procedure.performedDate);
			this.setAndAddConcept(procedure.snomed);
		});
	}

}
