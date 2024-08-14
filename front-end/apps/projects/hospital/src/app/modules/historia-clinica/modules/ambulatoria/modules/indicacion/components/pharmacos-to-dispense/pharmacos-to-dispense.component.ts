import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { SharedSnomedDto } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';
import { ButtonType } from '@presentation/components/button/button.component';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
	selector: 'app-pharmacos-to-dispense',
	templateUrl: './pharmacos-to-dispense.component.html',
	styleUrls: ['./pharmacos-to-dispense.component.scss']
})
export class PharmacosToDispenseComponent implements OnInit {

	form: FormGroup;
	hasError = hasError;
	ButtonType = ButtonType;
	typeaheadConcepts: TypeaheadOption<SharedSnomedDto>[] = [];
	isValidToAdd = false;

	@Output() isValidToConfirm = new EventEmitter<boolean>();


	constructor() { }

	ngOnInit(): void {
		this.createForm();
	}

	setConcept = (concept: SharedSnomedDto, index: number) => {
        const pharmaco = this.pharmacos.at(index) as FormGroup;
        pharmaco.get('snomed')?.setValue(concept);
		this.checkIfValidPharmacoToDispense();
    }

	setQuantity = (quantity: number, index: number) => {
		const pharmaco = this.pharmacos.at(index) as FormGroup;
        pharmaco.get('quantity')?.setValue(quantity);
		this.checkIfValidPharmacoToDispense();
	}

	addPharmacoToDispense = () => {
        this.pharmacos.push(this.createPharmacoGroup());
		this.checkIfValidPharmacoToDispense();
    }

    removePharmacoToDispense = (index: number) => {
        this.pharmacos.removeAt(index);
		this.checkIfValidPharmacoToDispense();
    }

	get pharmacos(): FormArray {
        return this.form.get('pharmacos') as FormArray;
    }

	private checkIfValidPharmacoToDispense = () => {
		this.isValidToAdd = this.form.valid;
		this.isValidToConfirm.emit(this.isValidConfirm());
	}

	private isValidConfirm = (): boolean => {
		return this.isValidToAdd && this.pharmacos.length > 0;
	}

	private createPharmacoGroup = (): FormGroup => {
        return new FormGroup<PharmacoForm>({
            snomed: new FormControl(null, Validators.required),
            quantity: new FormControl(1, [Validators.required, Validators.min(1)])
        });
    }

	private createForm = () => {
		this.form = new FormGroup({
			pharmacos: new FormArray([]),
		});
		this.addPharmacoToDispense();
	}

}

interface PharmacoForm {
	snomed: FormControl<SharedSnomedDto>,
    quantity: FormControl<number>,
}