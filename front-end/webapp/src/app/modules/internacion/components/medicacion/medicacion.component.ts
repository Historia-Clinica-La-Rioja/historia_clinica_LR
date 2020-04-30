import { Component, Input, OnInit } from '@angular/core';
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

	@Input() medications: MedicationDto[] = [];

	private snomedConcept: SnomedDto;

	form: FormGroup;
	clinicalStatus: MasterDataInterface<string>[];

	//Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.medicacion.table.columns.PROBLEM_TYPE',
			text: v => v.snomed.fsn
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
	dataSource = new MatTableDataSource<any>(this.medications);

	constructor(
		private formBuilder: FormBuilder,
		private internacionMasterDataService: InternacionMasterDataService
	)
	{
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
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
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let fsn = selectedConcept ? selectedConcept.fsn : '';
		this.form.controls.snomed.setValue(fsn);
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
