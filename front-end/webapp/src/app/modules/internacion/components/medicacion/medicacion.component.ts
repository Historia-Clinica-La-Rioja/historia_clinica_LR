import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MedicationDto, SnomedDto, MasterDataInterface } from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { pushTo, removeFrom } from '@core/utils/array.utils';

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

	snomedConcept: SnomedDto;

	form: FormGroup;
	clinicalStatus: MasterDataInterface<string>[];

	//Mat table
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
		private formBuilder: FormBuilder
	)
	{
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			note: [null],
			snomed: [null, Validators.required]
		});

	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const medicacion: MedicationDto = {
				id: null,
				note: this.form.value.note,
				snomed: this.snomedConcept,
				suspended: null,
				statusId: null
			};
			this.add(medicacion);
			this.resetForm();
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let pt = selectedConcept ? selectedConcept.pt : '';
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

}
