import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormGroup } from '@angular/forms';
import { MasterDataDto, TimeDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { hasError } from '@core/utils/form.utils';
import { MedicationService } from '../../services/medicationService';
import { TimePickerData } from '@presentation/components/time-picker/time-picker.component';

@Component({
    selector: 'app-anesthetic-drug',
    templateUrl: './anesthetic-drug.component.html',
    styleUrls: ['./anesthetic-drug.component.scss']
})
export class AnestheticDrugComponent implements OnInit {

    form: FormGroup;
    viasArray: MasterDataDto[];
    anotherViaEnabled = false
    readonly hasError = hasError;
    private ANOTHER_VIA_ID = 7;
    selectedMedicationTime: TimeDto;
    timePickerData: TimePickerData;
    submittedForm = false;

    constructor(
        public dialogRef: MatDialogRef<AnestheticDrugComponent>,
        @Inject(MAT_DIALOG_DATA) public readonly data: AnestheticDrugData,
        readonly internacionMasterDataService: InternacionMasterDataService,
    ) {
        this.form = this.data.premedicationService.getForm();
        this.viasArray = this.data.vias
    }

    ngOnInit(): void {
        this.data.premedicationService.getViaInputStatus().subscribe
            (
                via => {
                    this.anotherViaEnabled = via?.description && via?.id === this.ANOTHER_VIA_ID
                    this.data.premedicationService.HandleValidatorRequiredViaNotes(via)
                }
            )
        this.initTimePickerData();
    }

    private initTimePickerData() {
        this.timePickerData = {
            hideLabel: true,
            isRequired: true
        }
    }

    close(): void {
        this.dialogRef.close()
        this.data.premedicationService.resetForm();
    }

    addDrug(): void {
        this.submittedForm = true;
        if (this.form.valid) {
            this.data.premedicationService.addToList();
            this.dialogRef.close();
        }
    }

    onSelectedTime(selectedTime: TimeDto) {
        this.form.controls.time.setValue(selectedTime);
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