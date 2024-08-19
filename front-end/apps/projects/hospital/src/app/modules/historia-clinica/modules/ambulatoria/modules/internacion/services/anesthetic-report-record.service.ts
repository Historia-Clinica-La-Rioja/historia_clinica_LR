import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HealthConditionDto, ProcedureDescriptionDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { ToFormGroup } from '@core/utils/form.utils';
import { SnomedService, SnomedSemanticSearch } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportRecordService {

    private readonly ECL = SnomedECL.PERSONAL_RECORD;
    private form: FormGroup<RecordForm>;
    private personalRecordForm: FormGroup<ToFormGroup<PersonalRecordData>>;
	snomedConcept: SnomedDto;

    private recordList: SnomedDto[] = [];

    private dataEmitter = new BehaviorSubject<SnomedDto[]>(this.recordList);
	private data$ = this.dataEmitter.asObservable();

	private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();
    private isEmptyRecordSource = new BehaviorSubject<boolean>(true);
	isEmptyRecord$ = this.isEmptyRecordSource.asObservable();

    constructor(
        private readonly snomedService: SnomedService,
        private readonly snackBarService: SnackBarService,
    ) {
        this.form = new FormGroup<RecordForm>({
            snomed: new FormControl(null, Validators.required)
        });

        this.personalRecordForm = new FormGroup<ToFormGroup<PersonalRecordData>>({
            observations: new FormControl(null),
            asa: new FormControl(null)
        });
    }

    private handleAddRecord(record: SnomedDto): boolean {
        const currentItems = this.recordList.length;
        this.recordList = pushIfNotExists<any>(this.recordList, record, this.compareRecord);
        this.dataEmitter.next(this.recordList);
		this.isEmptySource.next(this.isEmpty());
		this.isEmptyRecordSource.next(this.hasRecords());
        return currentItems === this.recordList.length;
    }

    private compareRecord(data: SnomedDto, data2: SnomedDto): boolean {
        return data.sctid === data2.sctid;
    }

    addToList(): boolean {
        if (this.form.valid && this.snomedConcept) {
            if (this.handleAddRecord(this.snomedConcept))
                this.snackBarService.showError("Antecedente duplicado");
            this.resetForm();
            return true;
        }
        return false;
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

    setConcept(selectedConcept: SnomedDto): void {
        this.snomedConcept = selectedConcept;
        const pt = selectedConcept ? selectedConcept.pt : '';
        this.form.controls.snomed.setValue(pt);
    }

    remove(index: number): void {
		this.recordList = removeFrom<SnomedDto>(this.recordList, index);
		this.dataEmitter.next(this.recordList);
		this.isEmptySource.next(this.isEmpty());
		this.isEmptyRecordSource.next(this.hasRecords());
	}

    getRecord(): Observable<SnomedDto[]> {
        return this.data$;
    }

    getRecordAsHealthConditionDto(): HealthConditionDto[] {
        return this.mapToHealthConditionDto();
    }

    private mapToHealthConditionDto(): HealthConditionDto[] {
        return this.recordList.map(record => {
            return {
                snomed: record
            }
        })
    }

    getECL(): SnomedECL {
        return this.ECL;
    }

    getForm(): FormGroup {
        return this.form;
    }

    getPersonalRecordForm(): FormGroup {
        return this.personalRecordForm;
    }

    getPersonalRecordData(): PersonalRecordData{
        return {
            observations: this.personalRecordForm.value.observations,
            asa: this.personalRecordForm.value.asa
        }
    }

    resetForm(): void {
        delete this.snomedConcept;
        this.form.reset();
    }

    isEmpty(): boolean {
        return !(!!this.recordList.length || !!this.personalRecordForm.value.asa || !!this.personalRecordForm.value.observations)
    }

    hasRecords(): boolean {
        return !(!!this.recordList.length);
    }

	setData(records: HealthConditionDto[], procedures: ProcedureDescriptionDto) {
        this.recordList = records.map(record => record.snomed);
        this.dataEmitter.next(this.recordList);

		if (procedures) {
			if (procedures.note) {
				this.personalRecordForm.get('observations').setValue(procedures.note);
			}
			if (procedures.asa) {
				this.personalRecordForm.get('asa').setValue(procedures.asa);
			}
		}

        this.isEmptySource.next(this.isEmpty());
		this.isEmptyRecordSource.next(this.hasRecords());
    }
}

export interface RecordForm {
    snomed: FormControl<string>;
}

export interface PersonalRecordData {
    observations: string;
    asa: number;
}
