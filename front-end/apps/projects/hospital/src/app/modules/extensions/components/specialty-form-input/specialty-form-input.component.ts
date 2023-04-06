import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';

@Component({
	selector: 'app-specialty-form-input',
	templateUrl: './specialty-form-input.component.html',
	styleUrls: ['./specialty-form-input.component.scss']
})
export class SpecialtyFormInputComponent implements OnInit {

	@Input() label: string;
	@Input() hint: string;
	@Output() specialtyChange = new EventEmitter<string[]>();

	specialtyForm = new FormGroup({
		specialty: new FormControl(),
	});

	specialtyList = [];

	constructor(private readonly internacionMasterDataService: InternacionMasterDataService) { }

	ngOnInit(): void {
		this.internacionMasterDataService.getClinicalSpecialty().subscribe(specialties => this.specialtyList = [...specialties]);
		this.specialtyForm.valueChanges.subscribe(value => this.emitSpecialtyChange(value));
	}

	emitSpecialtyChange(value) {
		this.specialtyChange.emit(value.specialty ? [value.specialty] : null);
	}

	clear(control: AbstractControl): void {
		control.reset();
	}
}
