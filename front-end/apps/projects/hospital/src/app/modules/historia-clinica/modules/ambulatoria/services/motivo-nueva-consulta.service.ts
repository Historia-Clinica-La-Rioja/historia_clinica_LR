import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { TableColumnConfig } from "@presentation/components/document-section-table/document-section-table.component";
import { CellTemplates } from "@presentation/components/cell-templates/cell-templates.component";
import { SnackBarService } from '@presentation/services/snack-bar.service';

export interface MotivoConsulta {
	snomed: SnomedDto;
}

export class MotivoNuevaConsultaService {

	private motivoConsulta: MotivoConsulta[] = [];
	private form: FormGroup;
	private readonly columns: ColumnConfig[];
	private readonly tableColumnConfig: TableColumnConfig[];
	private snomedConcept: SnomedDto;
	private readonly ECL = SnomedECL.CONSULTATION_REASON;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService

	) {
		this.form = this.formBuilder.group({
			snomed: [null]
		});

		this.columns = [
			{
				def: 'motivo',
				header: 'ambulatoria.paciente.nueva-consulta.motivo.table.columns.MOTIVO',
				text: a => a.snomed.pt
			}
		];

		this.tableColumnConfig = [
			{
				def: 'motivo',
				header: 'ambulatoria.paciente.nueva-consulta.motivo.table.columns.MOTIVO',
				template: CellTemplates.SNOMED_PROBLEM
			},
			{
				def: 'eliminar',
				template: CellTemplates.REMOVE_BUTTON,
				action: (rowIndex) => this.remove(rowIndex)
			},
		]
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	getECL(): SnomedECL {
		return this.ECL;
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

	getForm(): FormGroup {
		return this.form;
	}

	getMotivosConsulta(): MotivoConsulta[] {
		return this.motivoConsulta;
	}

	getColumns(): ColumnConfig[] {
		return this.columns;
	}

	getTableColumnConfig(): TableColumnConfig[] {
		return this.tableColumnConfig;
	}

	add(motivo: MotivoConsulta): boolean {
		const currentItems = this.motivoConsulta.length;
		this.motivoConsulta = pushIfNotExists<MotivoConsulta>(this.motivoConsulta, motivo, this.compareSpeciality);
		return currentItems === this.motivoConsulta.length;
	}

	addControl(motivo: MotivoConsulta): void {
		if (this.add(motivo))
			this.snackBarService.showError("Motivo duplicado");
	}

	compareSpeciality(data: MotivoConsulta, data1: MotivoConsulta): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}


	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const motivo: MotivoConsulta = {
				snomed: this.snomedConcept
			};
			this.addControl(motivo);
			this.resetForm();
		}
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}

	remove(index: number): void {
		this.motivoConsulta = removeFrom<MotivoConsulta>(this.motivoConsulta, index);
	}

	isEmpty(): boolean {
		return (!this.motivoConsulta || this.motivoConsulta.length === 0);
	}
}
