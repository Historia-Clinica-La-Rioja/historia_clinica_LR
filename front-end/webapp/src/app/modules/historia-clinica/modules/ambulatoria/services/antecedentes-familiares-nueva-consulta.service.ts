import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedService, SnomedSemanticSearch } from '../../../services/snomed.service';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SnomedDto } from '@api-rest/api-model';
import { pushTo } from '@core/utils/array.utils';
import { SEMANTICS_CONFIG } from '../../../constants/snomed-semantics';

export class AntecedentesFamiliaresNuevaConsultaService {

	private readonly columns: ColumnConfig[];
	private form: FormGroup;
	private data: any[];
	private snomedConcept: SnomedDto;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService) {

		this.form = this.formBuilder.group({
			snomed: [null, Validators.required]
		});

		this.columns = [
			{
				def: 'problemType',
				header: 'ambulatoria.paciente.nueva-consulta.antecedentes-familiares.table.columns.ANTECEDENTE_FAMILIAR',
				text: af => af.snomed.pt
			}
		];

		this.data = [];
	}


	getColumns(): ColumnConfig[] {
		return this.columns;
	}

	getData(): any[] {
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

	add(antecedente: any): void {
		this.data = pushTo<any>(this.data, antecedente);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			this.add(this.form.value);
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
				eclFilter: this.SEMANTICS_CONFIG.familyRecord
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

}
