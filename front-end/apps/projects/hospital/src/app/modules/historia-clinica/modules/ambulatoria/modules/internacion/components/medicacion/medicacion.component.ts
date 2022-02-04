import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MedicationDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SnomedService, SnomedSemanticSearch } from '@historia-clinica/services/snomed.service';

@Component({
	selector: 'app-medicacion',
	templateUrl: './medicacion.component.html',
	styleUrls: ['./medicacion.component.scss']
})
export class MedicacionComponent implements OnInit {

	private medicationsValue: MedicationDto[];

	@Output() medicationsChange = new EventEmitter();

	@Input()
	set medications(medications: MedicationDto[]) {
		this.medicationsValue = medications;
		this.medicationsChange.emit(this.medicationsValue);
	}

	get medications(): MedicationDto[] {
		return this.medicationsValue;
	}

	@Input() hideSuspended: boolean;
	@Input() title = 'internaciones.anamnesis.medicacion.MEDICATION';

	snomedConcept: SnomedDto;

	form: FormGroup;

	// Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.medicacion.table.columns.MEDICATION',
			text: v => v.snomed.pt
		},
		{
			def: 'note',
			header: 'internaciones.anamnesis.medicacion.table.columns.NOTE',
			text: v => v.note
		},
	];
	displayedColumns: string[] = [];

	constructor(
		private formBuilder: FormBuilder,
		private snomedService: SnomedService
	) {
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			note: [null],
			suspended: [false],
			snomed: [null, Validators.required]
		});

	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const medicacion: MedicationDto = {
				note: this.form.value.note,
				snomed: this.snomedConcept,
				suspended: this.form.value.suspended
			};
			this.add(medicacion);
			this.resetForm();
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	add(medicacion: MedicationDto): void {
		this.medications = pushTo<MedicationDto>(this.medications, medicacion);
	}

	remove(index: number): void {
		this.medications = removeFrom<MedicationDto>(this.medications, index);
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.MEDICINE
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}
}
