import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SnomedDto } from '@api-rest/api-model';
import { SEMANTICS_CONFIG } from '../../../constants/snomed-semantics';
import { pushTo } from '@core/utils/array.utils';

export interface Alergia {
	snomed: SnomedDto;
	criticalityId: number;
}

export class AlergiasNuevaConsultaService {

	private readonly columns: ColumnConfig[];
	private form: FormGroup;
	private data: Alergia[] = [];
	private snomedConcept: SnomedDto;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;
	private criticalityTypes: any[];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService) {

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

	add(alergia: Alergia): void {
		this.data = pushTo<Alergia>(this.data, alergia);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const alergia: Alergia = {
				snomed: this.snomedConcept,
				criticalityId: this.form.value.criticality,
			};
			this.add(alergia);
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
				eclFilter: this.SEMANTICS_CONFIG.allergy
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

}
