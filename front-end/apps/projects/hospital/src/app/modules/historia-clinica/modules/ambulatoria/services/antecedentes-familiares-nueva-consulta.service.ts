import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { CellTemplates } from '@presentation/components/cell-templates/cell-templates.component';
import { TableColumnConfig } from '@presentation/components/document-section-table/document-section-table.component';

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

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService) {

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

	add(antecedente: AntecedenteFamiliar): void {
		this.data = pushTo<any>(this.data, antecedente);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const antecedente: AntecedenteFamiliar = {
				snomed: this.snomedConcept,
				fecha: this.form.value.fecha
			};
			this.add(antecedente);
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
				eclFilter: SnomedECL.FAMILY_RECORD
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

}
