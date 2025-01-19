import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { MedicationInfoDto, SharedSnomedDto } from '@api-rest/api-model';
import { CommercialMedicationService } from '@api-rest/services/commercial-medication.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { listToTypeaheadOptions } from '@presentation/utils/typeahead.mapper.utils';
import { map } from 'rxjs';
import { MedicationToDispenseService } from '../../services/medication-to-dispense.service';

@Component({
	selector: 'app-pharmacos-to-dispense',
	templateUrl: './pharmacos-to-dispense.component.html',
	styleUrls: ['./pharmacos-to-dispense.component.scss']
})
export class PharmacosToDispenseComponent implements OnInit {

	form: FormGroup;
	ButtonType = ButtonType;
	typeaheadConcepts: TypeaheadOption<SharedSnomedDto>[] = [];
	isValidToAdd = false;

	@Input() medicationInfo: MedicationInfoDto;
	@Output() isValidToConfirm = new EventEmitter<boolean>();

	constructor(private readonly commercialMedicationService: CommercialMedicationService,
				private readonly medicationToDispenseService: MedicationToDispenseService,
	) {}

	ngOnInit(): void {
		this.createForm();
		this.setTypeaheadConcepts();
	}

	setConcept = (concept: SharedSnomedDto, index: number) => {
		const pharmaco = this.pharmacos.at(index) as FormGroup;
		(pharmaco) ? pharmaco.get('snomed').setValue(concept) : this.pharmacos.push(this.createPharmacoGroup(concept));
		this.checkIfValidPharmacoToDispense();
		this.mapToSaveMedicationStatementInstitutionalSupplyDto();
    }

	setQuantity = (quantity: number, index: number) => {
		const pharmaco = this.pharmacos.at(index) as FormGroup;
        pharmaco.get('quantity')?.setValue(quantity);
		this.checkIfValidPharmacoToDispense();
		this.mapToSaveMedicationStatementInstitutionalSupplyDto();
	}

	addPharmacoToDispense = () => {
        this.pharmacos.push(this.createPharmacoGroup(null));
		this.checkIfValidPharmacoToDispense();
    }

    removePharmacoToDispense = (index: number) => {
        this.pharmacos.removeAt(index);
		this.checkIfValidPharmacoToDispense();
		this.mapToSaveMedicationStatementInstitutionalSupplyDto();
    }

	get pharmacos(): FormArray {
        return this.form.get('pharmacos') as FormArray;
    }

	private mapToSaveMedicationStatementInstitutionalSupplyDto = () => {
		this.medicationToDispenseService.mapToSaveMedicationStatementInstitutionalSupplyDto(this.pharmacos.value, this.medicationInfo.id);
	}

	private setTypeaheadConcepts = () => {
		this.commercialMedicationService.getSuggestedCommercialMedicationSnomedListByGeneric(this.medicationInfo.snomed.sctid)
			.pipe(map(concepts => listToTypeaheadOptions(concepts, 'pt')))
			.subscribe({
				next: (result: TypeaheadOption<SharedSnomedDto>[]) => this.typeaheadConcepts = result
			});
	}

	private checkIfValidPharmacoToDispense = () => {
		this.isValidToAdd = this.form.valid;
		this.isValidToConfirm.emit(this.isValidConfirm());
	}

	private isValidConfirm = (): boolean => {
		return this.isValidToAdd && this.pharmacos.length > 0;
	}

	private createPharmacoGroup = (snomed: SharedSnomedDto): FormGroup => {
        return new FormGroup<PharmacoForm>({
            snomed: new FormControl(snomed, Validators.required),
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

export interface Pharmaco {
	snomed: SharedSnomedDto,
	quantity: number
}