import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormGroup } from '@angular/forms';
import { MasterDataDto, TimeDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { hasError } from '@core/utils/form.utils';
import { MedicationService } from '../../services/medicationService';

@Component({
    selector: 'app-anesthetic-drug',
    templateUrl: './anesthetic-drug.component.html',
    styleUrls: ['./anesthetic-drug.component.scss']
})
export class AnestheticDrugComponent implements OnInit {

    form: FormGroup;
    possibleTimesList: TimeDto[];
    viasArray: MasterDataDto[];
    anotherViaEnabled = false
	readonly hasError = hasError;

    constructor(
        public dialogRef: MatDialogRef<AnestheticDrugComponent>,
        @Inject(MAT_DIALOG_DATA) public readonly data: AnestheticDrugData,
        readonly internacionMasterDataService: InternacionMasterDataService,
    ) {
        this.form = this.data.premedicationService.getForm();
        this.possibleTimesList = this.data.premedicationService.possibleTimesList;
        this.viasArray = this.data.vias
    }

    ngOnInit(): void {
        this.data.premedicationService.getViaInputStatus().subscribe
        (
          via => {
                this.anotherViaEnabled =  via?.description && via?.description === 'Otra'
                this.data.premedicationService.HandleValidatorRequiredViaNotes(via)
            }
        )
    }

    close(): void {
        this.dialogRef.close()
        this.data.premedicationService.resetForm();
    }

    addDrug(): void {
        if (this.form.valid){
            this.data.premedicationService.addToList();
            this.dialogRef.close();
        }
    }
}

interface AnestheticDrugData {
    premedicationService: MedicationService,
    searchConceptsLocallyFF: boolean,
    vias: MasterDataDto[],
    presentationConfig: PresentationDrugData
}

interface PresentationDrugData {
    title?: string,
    label?: string
}