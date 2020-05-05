import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MedicationDto, SnomedDto, MasterDataInterface } from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
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
			def: 'clinicalStatus',
			header: 'internaciones.anamnesis.medicacion.table.columns.STATUS',
			text: v => this.clinicalStatus?.find(status => status.id === v.statusId).description
		},
		{
			def: 'note',
			header: 'internaciones.anamnesis.medicacion.table.columns.NOTE',
			text: v => v.note
		},
	];
	displayedColumns: string[] = [];
	dataSource: MatTableDataSource<MedicationDto>;

	constructor(
		private formBuilder: FormBuilder,
		private internacionMasterDataService: InternacionMasterDataService
	)
	{
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.dataSource = new MatTableDataSource<MedicationDto>(this.medications);
		this.form = this.formBuilder.group({
			note: [null, Validators.required],
			statusId: [null, Validators.required],
			snomed: [null, Validators.required]
		});

		this.internacionMasterDataService.getMedicationClinical().subscribe(clinicalStatus => {
			this.clinicalStatus = clinicalStatus;
		});

	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const medicacion: MedicationDto = {
				id: null,
				note: this.form.value.note,
				snomed: this.snomedConcept,
				statusId: this.form.value.statusId
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
		this.dataSource.data = pushTo<MedicationDto>(this.dataSource.data, medicacion);
		this.medications.push(medicacion);
	}

	remove(index: number): void {
		const toRemove = this.dataSource.data[index];
		if (toRemove.id == null) {
			this.dataSource.data = removeFrom<MedicationDto>(this.dataSource.data, index);
			this.medications = this.medications.filter(item => toRemove !== item);
		}
	}

}
