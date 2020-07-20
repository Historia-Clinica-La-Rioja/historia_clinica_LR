import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedDto } from '@api-rest/api-model';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SEMANTICS_CONFIG } from '../../../constants/snomed-semantics';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { pushTo } from '@core/utils/array.utils';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

export class ProblemasNuevaConsultaService {

	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	private form: FormGroup;
	private snomedConcept: SnomedDto;
	private readonly columns: ColumnConfig[];
	private data: any[];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService
	) {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			cronico: [null],
			fechaInicio: [null, Validators.required],
			fechaFin: [null]
		});

		this.columns = [
			{
				def: 'diagnosticos',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.DIAGNOSTICOS',
				text: (row) => row.snomed
			},
			{
				def: 'cronico',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.CRONICO',
				text: (row) => row.cronico ? 'Si' : 'No'
			},
			{
				def: 'fecha_inicio',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.FECHA_INICIO',
				text: (row) => momentFormat(row.fechaInicio, DateFormat.VIEW_DATE)
			},
			{
				def: 'fecha_fin',
				header: 'ambulatoria.paciente.nueva-consulta.problemas.FECHA_FIN',
				text: (row) => row.fechaFin ? momentFormat(row.fechaFin, DateFormat.VIEW_DATE) : ''
			}
		];

		this.data = [];
	}


	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	add(problema: any): void {
		this.data = pushTo<any>(this.data, problema);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			this.add(this.form.value);
			this.resetForm();
		}
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.SEMANTICS_CONFIG.diagnosis
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

	getFechaInicioMax(): Moment {
		return newMoment();
	}

	getFechaFinMin(): Moment {
		return this.form.value.fechaInicio ? this.form.value.fechaInicio : newMoment();
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

	getData(): any[] {
		return this.data;
	}
}
