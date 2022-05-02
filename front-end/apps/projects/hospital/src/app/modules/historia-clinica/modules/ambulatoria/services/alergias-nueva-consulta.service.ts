import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { TableColumnConfig } from "@presentation/components/document-section-table/document-section-table.component";
import { CellTemplates } from "@presentation/components/cell-templates/cell-templates.component";
import { SnackBarService } from '@presentation/services/snack-bar.service';

export interface Alergia {
	snomed: SnomedDto;
	criticalityId: number;
}

export class AlergiasNuevaConsultaService {

	private readonly columns: ColumnConfig[];
	private readonly tableColumnConfig: TableColumnConfig[];
	private form: FormGroup;
	private data: Alergia[] = [];
	private snomedConcept: SnomedDto;
	private criticalityTypes: any[];
	private readonly ECL = SnomedECL.ALLERGY;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService

	) {

		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			criticality: [null, Validators.required],
		});

		this.columns = [
			{
				def: 'problemType',
				header: 'ambulatoria.paciente.nueva-consulta.alergias.table.columns.ALLERGY',
				text: a => a.snomed.pt
			},
			{
				def: 'criticality',
				header: 'ambulatoria.paciente.nueva-consulta.alergias.table.columns.CRITICALITY',
				text: a => this.getDisplayName(a.criticalityId)
			}
		];

		this.tableColumnConfig = [
			{
				def: 'problemType',
				header: 'ambulatoria.paciente.nueva-consulta.alergias.table.columns.ALLERGY',
				template: CellTemplates.SNOMED_PROBLEM
			},
			{
				def: 'criticality',
				header: 'ambulatoria.paciente.nueva-consulta.alergias.table.columns.CRITICALITY',
				text: (row) => this.getDisplayName(row.criticalityId),
				template: CellTemplates.ALLERGY_CRITICALITY
			},
			{
				def: 'delete',
				template: CellTemplates.REMOVE_BUTTON,
				action: (rowIndex) => this.removeAlergia(rowIndex)
			}
		]

	}

	private getDisplayName(criticalityId) {
		return this.criticalityTypes.find(criticalityType => criticalityType.id === criticalityId)?.display;
	}

	setCriticalityTypes(criticalityTypes): void {
		this.criticalityTypes = criticalityTypes;
	}

	getColumns(): ColumnConfig[] {
		return this.columns;
	}

	getTableColumnConfig(): TableColumnConfig[] {
		return this.tableColumnConfig;
	}

	getAlergias(): Alergia[] {
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

	add(alergia: Alergia): boolean {
		const currentItems = this.data.length;
		this.data = pushIfNotExists<Alergia>(this.data, alergia, this.compareSpeciality);
		return currentItems === this.data.length;
	}

	addControl(alergia: Alergia): void {
		if (this.add(alergia))
			this.snackBarService.showError("Alergia duplicada");
	}

	compareSpeciality(data: Alergia, data1: Alergia): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}

	removeAlergia(index: number): void {
		this.data = removeFrom<Alergia>(this.data, index);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const alergia: Alergia = {
				snomed: this.snomedConcept,
				criticalityId: this.form.value.criticality,
			};
			this.addControl(alergia);
			this.resetForm();
		}
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

	getECL(): SnomedECL {
		return this.ECL;
	}

}
