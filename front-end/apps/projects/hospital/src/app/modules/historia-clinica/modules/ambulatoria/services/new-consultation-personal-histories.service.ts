import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SnomedDto, SnomedECL } from "@api-rest/api-model";
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subject } from 'rxjs';

export interface PersonalHistory {
    snomed: SnomedDto;
    type: PersonalHistoryType;
    startDate: Date;
    endDate?: Date;
    observations?: string;
}

@Injectable({
    providedIn: 'root'
})
export class NewConsultationPersonalHistoriesService {

    private form: FormGroup;
	private data: PersonalHistory[];
	private snomedConcept: SnomedDto;
    private readonly ECL = SnomedECL.PERSONAL_RECORD;

    dataEmitter = new Subject();
	data$ = this.dataEmitter.asObservable();
    
    constructor(
        private readonly snomedService: SnomedService,
        private readonly snackBarService: SnackBarService) 
    {
        this.form = new FormGroup<PersonalHistoriesForm>({
            snomed: new FormControl(null, Validators.required),
            type: new FormControl(null, Validators.required),
            startDate: new FormControl(null, Validators.required),
            endDate: new FormControl(null),
            observations: new FormControl(null),
        });

        this.data = [];
    }

    getPersonalHistories(): PersonalHistory[] {
        return this.data;
    }

    getSnomedConcept(): SnomedDto {
        return this.snomedConcept;
    }

    setConcept(selectedConcept: SnomedDto): void {
        this.snomedConcept = selectedConcept;
        const pt = selectedConcept ? selectedConcept.pt : '';
        this.form.controls.snomed.setValue(pt);
    }

    add(personalHistory: PersonalHistory): boolean {
        const currentItems = this.data.length;
        this.data = pushIfNotExists<any>(this.data, personalHistory, this.comparePersonalHistory);
        this.dataEmitter.next(this.data);
        return currentItems === this.data.length;
    }

    private comparePersonalHistory(data: PersonalHistory, data1: PersonalHistory): boolean {
        return data.snomed.sctid === data1.snomed.sctid;
    }

    addToList(): boolean {
        if (this.form.valid && this.snomedConcept) {
            const personalHistory: PersonalHistory = {
                snomed: this.snomedConcept,
                type: this.form.value.type,
                startDate: this.form.value.startDate,
                endDate: this.form.value.endDate,
                observations: this.form.value.observations || null,
            };
            if (this.add(personalHistory))
                this.snackBarService.showError("Antecedente personal duplicado");
            this.resetForm();
            return true;
        }
        return false;
    }

    remove(index: number): void {
        this.data = removeFrom<PersonalHistory>(this.data, index);
        this.dataEmitter.next(this.data)
    }


    getForm(): FormGroup {
        return this.form;
    }


    resetForm(): void {
        delete this.snomedConcept;
        this.form.reset();
    }

    openSearchDialog(searchValue: string): void {
        if (searchValue) {
            const search: SnomedSemanticSearch = {
                searchValue,
                eclFilter: this.ECL
            };
            this.snomedService.openConceptsSearchDialog(search)
                .subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
        }
    }

    getMaxDate(): Date {
        return new Date();
    }

    getMinDate(): Date {
        return this.form.value.startDate;
    }

    getECL(): SnomedECL {
        return this.ECL;
    }

    isEmpty(): boolean {
        return (!this.data || this.data.length === 0);
    }
}

export interface PersonalHistoriesForm {
    snomed: FormControl<SnomedDto>;
    type: FormControl<PersonalHistoryType>;
    startDate: FormControl<Date>;
    endDate?: FormControl<Date>;
    observations: FormControl<string>;
}

export interface PersonalHistoryType {
    description: string;
    id: number;
}