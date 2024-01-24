import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MasterDataDto, SnomedDto, SnomedECL, TimeDto } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { PREMEDICATION } from '@historia-clinica/constants/validation-constants';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportPremedicationAndFoodIntakeService {

    private readonly ECL = SnomedECL.MEDICINE;
    private form: FormGroup;
	snomedConcept: SnomedDto;
	private posibleTimes: TimeDto[] = [];
    private readonly possibleMinutes = [0,5,10,15,20,25,30,35,40,45,50,55];

    private dosisErrorSource = new Subject<string | void>();
	private _dosisError$ = this.dosisErrorSource.asObservable();

    private premedicationList: PremedicationAndFoodIntakeData[] = [];

    private dataEmitter = new BehaviorSubject<PremedicationAndFoodIntakeData[]>(this.premedicationList);
	private data$ = this.dataEmitter.asObservable();

    constructor(
        private readonly snomedService: SnomedService,
        private readonly snackBarService: SnackBarService,
		private readonly translateService: TranslateService,
    ) { 
        this.form = new FormGroup<PremedicationForm>({
            snomed: new FormControl(null, Validators.required),
            dosis: new FormControl(null, [Validators.min(PREMEDICATION.MIN.dosis)]),
            unit: new FormControl(null),
            via: new FormControl(null),
            time: new FormControl(null),
            lastFoodIntake: new FormControl(null),
        });

        this.form.controls.dosis.valueChanges.subscribe(_ => {
			this.checkDosisError();
		});

        this.posibleTimes = this.generateInitialTimes();
    }

    private checkDosisError() {
        if (this.form.controls.dosis.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: PREMEDICATION.MIN.dosis }).subscribe(
                (errorMsg: string) => this.dosisErrorSource.next(errorMsg)
            );
        }
    }

    private add(premedication: PremedicationAndFoodIntakeData): boolean {
        const currentItems = this.premedicationList.length;
        this.premedicationList = pushIfNotExists<any>(this.premedicationList, premedication, this.comparePremedication);
        this.dataEmitter.next(this.premedicationList);
        return currentItems === this.premedicationList.length;
    }

    private comparePremedication(data: PremedicationAndFoodIntakeData, data2: PremedicationAndFoodIntakeData): boolean {
        return data.snomed.sctid === data2.snomed.sctid;
    }

    addToList(): boolean {
        if (this.form.valid && this.snomedConcept) {
            const premedicationData: PremedicationAndFoodIntakeData = {
                snomed: this.snomedConcept,
                dosis: this.form.value.dosis,
                unit: this.form.value.unit,
                via: this.form.value.via,
                time: this.form.value.time,
                lastFoodIntake: this.form.value.lastFoodIntake,
            };
            if (this.add(premedicationData))
                this.snackBarService.showError("Premedicacion duplicada");
            this.resetForm();
            return true;
        }
        return false;
    }

    remove(index: number): void {
        this.premedicationList = removeFrom<PremedicationAndFoodIntakeData>(this.premedicationList, index);
        this.dataEmitter.next(this.premedicationList);
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

    getPremedication(): Observable<PremedicationAndFoodIntakeData[]> {
        return this.data$;
    }

    getECL(): SnomedECL {
        return this.ECL;
    }

    getForm(): FormGroup {
        return this.form;
    }

    resetForm(): void {
        delete this.snomedConcept;
        this.form.reset();
    }

    isEmpty(): boolean {
        return !(!!this.premedicationList.length)
    }

    get dosisError$(): Observable<string | void> {
		return this._dosisError$;
	}

    get possibleTimesList(): TimeDto[] {
        return this.posibleTimes;
    }

    private generateInitialTimes(): TimeDto[] {
		const initializedTimes = [];
		for (let currentHour = 0; currentHour < 24; currentHour++) {
            for (let minute of this.possibleMinutes){
                initializedTimes.push({
                    hours: currentHour,
                    minutes: minute
                });
            }
		}
		return initializedTimes;
	}
}

export interface PremedicationForm {
    snomed: FormControl<SnomedDto>;
    dosis: FormControl<number>;
    unit: FormControl<string>;
    via: FormControl<MasterDataDto>;
    time: FormControl<TimeDto>;
    lastFoodIntake: FormControl<TimeDto>;
}

export interface PremedicationAndFoodIntakeData {
    snomed: SnomedDto,
    dosis: number,
    unit: string,
    via: MasterDataDto,
    time: TimeDto,
    lastFoodIntake: TimeDto
}