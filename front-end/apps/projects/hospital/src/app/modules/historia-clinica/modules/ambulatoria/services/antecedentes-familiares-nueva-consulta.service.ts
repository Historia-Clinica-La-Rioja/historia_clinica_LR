import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { CellTemplates } from '@presentation/components/cell-templates/cell-templates.component';
import { TableColumnConfig } from '@presentation/components/document-section-table/document-section-table.component';
import { SnackBarService } from "@presentation/services/snack-bar.service";

export interface AntecedenteFamiliar {
	snomed: SnomedDto;
	fecha: Moment;
}

export class AntecedentesFamiliaresNuevaConsultaService {

	private readonly columns: ColumnConfig[];
	private form: FormGroup;
	private data: AntecedenteFamiliar[];
	private snomedConcept: SnomedDto;
	private readonly tableColumnConfig: TableColumnConfig[];
	private readonly ECL = SnomedECL.FAMILY_RECORD;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService) {

		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			fecha: [null, Validators.required]
		});

		this.columns = [
			{
				def: 'problemType',
				header: 'ambulatoria.paciente.nueva-consulta.antecedentes-familiares.table.columns.ANTECEDENTE_FAMILIAR',
				text: af => af.snomed.pt
			},
			{
				def: 'fecha',
				header: 'ambulatoria.paciente.nueva-consulta.antecedentes-familiares.table.columns.FECHA',
				text: (row) => momentFormat(row.fecha, DateFormat.VIEW_DATE)
			},
		];
		this.tableColumnConfig = [
			{
				def: 'problemType',
				header: 'ambulatoria.paciente.nueva-consulta.antecedentes-familiares.table.columns.ANTECEDENTE_FAMILIAR',
				template: CellTemplates.TEXT,
				text: v => v.snomed.pt
			},
			{
				def: 'fecha',
				header: 'ambulatoria.paciente.nueva-consulta.antecedentes-familiares.table.columns.FECHA',
				template: CellTemplates.TEXT,
				text: (row) => momentFormat(row.fecha, DateFormat.VIEW_DATE)
			},
			{
				def: 'eliminar',
				template: CellTemplates.REMOVE_BUTTON,
				action: (rowIndex) => this.remove(rowIndex)
			}
		]
		this.data = [];
	}


	getColumns(): ColumnConfig[] {
		return this.columns;
	}

	getAntecedentesFamiliares(): AntecedenteFamiliar[] {
		return this.data;
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	add(antecedente: AntecedenteFamiliar): boolean {
		const currentItems = this.data.length;
		this.data = pushIfNotExists<any>(this.data, antecedente, this.compareAntecedenteFamiliar);
		return currentItems === this.data.length;
	}

	compareAntecedenteFamiliar(data: AntecedenteFamiliar, data1: AntecedenteFamiliar): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const antecedente: AntecedenteFamiliar = {
				snomed: this.snomedConcept,
				fecha: this.form.value.fecha
			};
			if (this.add(antecedente))
				this.snackBarService.showError("Antecedente familiar duplicado");
			this.resetForm();
		}
	}

	remove(index: number): void {
		this.data = removeFrom<AntecedenteFamiliar>(this.data, index);
	}


	getForm(): FormGroup {
		return this.form;
	}


	resetForm(): void {
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

	getMaxFecha(): Moment {
		return newMoment();
	}

	getTableColumnConfig(): TableColumnConfig[] {
		return this.tableColumnConfig;
	}

	getECL(): SnomedECL {
		return this.ECL;
	}

}
