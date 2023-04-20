import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {InternacionMasterDataService} from '@api-rest/services/internacion-master-data.service';
import {MasterDataInterface} from "@api-rest/api-model";
import {TypeaheadOption} from "@presentation/components/typeahead/typeahead.component";

@Component({
	selector: 'app-specialty-form-input',
	templateUrl: './specialty-form-input.component.html',
	styleUrls: ['./specialty-form-input.component.scss']
})
export class SpecialtyFormInputComponent implements OnInit {

	@Input() label: string;
	@Output() specialtyChange = new EventEmitter<string[]>();

	specialtyForm = new FormGroup({
		specialty: new FormControl(),
	});

	specialtyList = [];

	constructor(private readonly internacionMasterDataService: InternacionMasterDataService) {
	}

	ngOnInit(): void {
		this.internacionMasterDataService.getClinicalSpecialty().subscribe(specialties => {
			this.specialtyList = this.toTypeaheadOptionList(specialties)
		});
		this.specialtyForm.valueChanges.subscribe(value => this.emitSpecialtyChange(value));
	}

	emitSpecialtyChange(value) {
		this.specialtyChange.emit(value?.name ? [value.name] : null);
	}

	toTypeaheadOptionList(prosBySpecialtyList: any []): TypeaheadOption<MasterDataInterface<string>>[] {
		return prosBySpecialtyList.map(s => {
			return {
				compareValue: s.name,
				value: s
			};
		});
	}
}
