import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MasterDataDto, AnestheticSubstanceDto, SnomedDto, SnomedECL, TimeDto, DateDto } from '@api-rest/api-model';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { NoWhitespaceValidator } from '@core/utils/form.utils';
import { newDate } from '@core/utils/moment.utils';
import { PREMEDICATION } from '@historia-clinica/constants/validation-constants';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class MedicationService {

    private readonly ECL = SnomedECL.MEDICINE;
    private form: FormGroup;
	snomedConcept: SnomedDto;

    private dosisErrorSource = new Subject<string | void>();
	private _dosisError$ = this.dosisErrorSource.asObservable();

    private medicationList: MedicationData[] = [];

    private dataEmitter = new BehaviorSubject<MedicationData[]>(this.medicationList);
	private data$ = this.dataEmitter.asObservable();
    private ANOTHER_VIA_ID = 7;

	private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();

    constructor(
        private readonly snomedService: SnomedService,
        private readonly snackBarService: SnackBarService,
		private readonly translateService: TranslateService,
    ) {
        this.form = new FormGroup<MedicationForm>({
            snomed: new FormControl(null, Validators.required),
            dosis: new FormControl(null, [Validators.required, Validators.min(PREMEDICATION.MIN.dosis)]),
            unit: new FormControl(null, Validators.required),
            via: new FormControl(null, Validators.required),
            time: new FormControl(null, Validators.required),
            viaNote: new FormControl(null)
        });

        this.form.controls.dosis.valueChanges.subscribe(_ => {
			this.checkDosisError();
		});
    }

    private checkDosisError() {
        if (this.form.controls.dosis.hasError('min')) {
            this.translateService.get('forms.MIN_ERROR', { min: PREMEDICATION.MIN.dosis }).subscribe(
                (errorMsg: string) => this.dosisErrorSource.next(errorMsg)
            );
        }
    }

    private handleAddMedication(premedication: MedicationData): boolean {
        const currentItems = this.medicationList.length;
        this.medicationList = pushIfNotExists<any>(this.medicationList, premedication, this.compareByEqualPremedication);
        this.dataEmitter.next(this.medicationList);
		this.isEmptySource.next(this.isEmpty());
        return currentItems === this.medicationList.length;
    }

    private compareByEqualPremedication(data: MedicationData, data2: MedicationData): boolean {
        return data.snomed.sctid === data2.snomed.sctid;
    }

    addToList(): boolean {
        if (this.form.valid && this.snomedConcept) {
            const medicationData: MedicationData = {
                snomed: this.snomedConcept,
                dosis: this.form.value.dosis,
                unit: this.form.value.unit,
                via: this.form.value.via,
                time: this.form.value.time,
				date: dateToDateDto(newDate()),
                viaNote: this.form.value.viaNote,
            };
            if (this.handleAddMedication(medicationData))
                this.snackBarService.showError("Medicacion duplicada");
            this.resetForm();
            return true;
        }
        return false;
    }

    remove(index: number): void {
        this.medicationList = removeFrom<MedicationData>(this.medicationList, index);
        this.dataEmitter.next(this.medicationList);
		this.isEmptySource.next(this.isEmpty());
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

    getAnestheticSubstanceDto(): AnestheticSubstanceDto[] {
        return this.mapToAnestheticSubstanceDto();
    }

    private mapToAnestheticSubstanceDto(): AnestheticSubstanceDto[]{
        return this.medicationList.map(medication => {
            return {
                snomed: medication.snomed,
                dosage: {
                    chronic: null,
                    diary: null,
                    quantity: {
                        unit: medication.unit,
                        value: medication.dosis
                    },
                    startDateTime: {
						time: medication.time ? medication.time : null,
						date: medication.date ? medication.date : null
					}
                },
                viaId: medication.via?.id,
                viaNote: medication.viaNote
            }
        })
    }

    getViaInputStatus():Observable<MasterDataDto> {
        return this.getForm().get('via').valueChanges
    }


    HandleValidatorRequiredViaNotes(viaData: MasterDataDto): void{
        if (viaData?.description && viaData?.id === this.ANOTHER_VIA_ID) {
            this.getForm().get('viaNote').setValidators([Validators.required, NoWhitespaceValidator()]);
        } else {
            this.getForm().get('viaNote').clearValidators();
            this.getForm().get('viaNote').updateValueAndValidity();
            this.getForm().get('viaNote').setValue(null)
        }
    }

    getMedication(): Observable<MedicationData[]> {
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
        return !(!!this.medicationList.length)
    }

    get dosisError$(): Observable<string | void> {
		return this._dosisError$;
	}

	setData(medications: AnestheticSubstanceDto[], viasType: MasterDataDto[]): void {
		let newList = medications.map(medication => ({
			snomed: medication.snomed,
			dosis: medication.dosage ? medication.dosage.quantity.value : null,
			unit: medication.dosage ? medication.dosage.quantity.unit : null,
			via: {
				id: medication.viaId,
				description: medication.viaId ? this.getViaDescription(viasType, medication.viaId) : null,
			},
			time: medication.dosage ? medication.dosage.startDateTime.time : null,
			date: medication.dosage.startDateTime.date ? medication.dosage.startDateTime.date : null,
			viaNote: medication.viaNote,
		}));

		this.medicationList = [...newList];

		this.dataEmitter.next(this.medicationList);
		this.isEmptySource.next(this.isEmpty());
	}

	private getViaDescription(viasArray: MasterDataDto[], viaId: number): string {
		return viasArray.filter(via => via.id == viaId)[0].description;
	}
}

export interface MedicationForm {
    snomed: FormControl<SnomedDto>;
    dosis: FormControl<number>;
    unit: FormControl<string>;
    via: FormControl<MasterDataDto>;
    time: FormControl<TimeDto>;
    viaNote?: FormControl<string>;
}

export interface MedicationData {
    snomed: SnomedDto,
    dosis: number,
    unit: string,
    via: MasterDataDto,
    time: TimeDto,
	date: DateDto,
    viaNote?: string
}
