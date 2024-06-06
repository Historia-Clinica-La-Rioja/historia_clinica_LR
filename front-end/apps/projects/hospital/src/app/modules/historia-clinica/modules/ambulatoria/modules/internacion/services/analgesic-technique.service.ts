import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AnalgesicTechniqueDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { PREMEDICATION } from '@historia-clinica/constants/validation-constants';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnalgesicTechniqueService {

  snomedConcept:SnomedDto;
  private form:FormGroup<AnalgesicTechniqueForm>;

  private dosisErrorSource = new Subject<string | void>();
	private _dosisError$ = this.dosisErrorSource.asObservable();

  private readonly ECL = SnomedECL.MEDICINE;
  private analgesicTechniqueList: AnalgesicTechniqueData[] = [];
  private dataEmitter = new BehaviorSubject<AnalgesicTechniqueData[]>(this.analgesicTechniqueList);

  constructor(
    private readonly snomedService: SnomedService,
    private readonly snackBarService: SnackBarService,
		private readonly translateService: TranslateService,
  ) {
    this.form = new FormGroup<AnalgesicTechniqueForm> ({
      snomed: new FormControl(null, Validators.required),
      dosis: new FormControl(null, [Validators.required, Validators.min(PREMEDICATION.MIN.dosis)]),
      unit: new FormControl(null, Validators.required),
      cateter: new FormControl(Cateter_options.CATETER_DISABLED, Validators.required),
      cateterNote: new FormControl(null),
      injectionNote: new FormControl(null, Validators.required)
    });

    this.form.controls.dosis.valueChanges.subscribe( _ => {
			this.checkDosisError();
		});
  }


  addToList(): boolean {
    if (this.form.valid && this.snomedConcept) {
        const newAnalgesicTechnique: AnalgesicTechniqueData = {
            snomed: this.snomedConcept,
            dosis: this.form.value.dosis,
            unit: this.form.value.unit,
            cateter: this.form.value.cateter === Cateter_options.CATETER_ENABLED,
            cateterNote: this.form.value.cateterNote,
            injectionNote: this.form.value.injectionNote,
        };
        if (this.handleAddATechnique(newAnalgesicTechnique))
            this.snackBarService.showError("Medicacion duplicada");
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


getForm(): FormGroup<AnalgesicTechniqueForm> {
  return this.form;
}


getAnalgesicTechniqueList(): Observable<AnalgesicTechniqueData[]> {
  return this.dataEmitter.asObservable()
}

getECL(): SnomedECL {
  return this.ECL;
}

resetForm(): void {
  delete this.snomedConcept;
  this.form.reset({cateter: Cateter_options.CATETER_DISABLED});
}

get dosisError$(): Observable<string | void> {
  return this._dosisError$;
}

remove(index: number): void {
  this.analgesicTechniqueList = removeFrom<AnalgesicTechniqueData>(this.analgesicTechniqueList, index);
  this.dataEmitter.next(this.analgesicTechniqueList);
}

setConcept(selectedConcept: SnomedDto): void {
  this.snomedConcept = selectedConcept;
  const pt = selectedConcept ? selectedConcept.pt : '';
  this.form.controls.snomed.setValue(pt)
}


getAnalgesicTechniqueDto(): AnalgesicTechniqueDto[]{
  return this.analgesicTechniqueList.map(aTechnique => {
      return {
          snomed: aTechnique.snomed,
          dosage: {
              chronic: null,
              diary: null,
              quantity: {
                  unit: aTechnique.unit,
                  value: aTechnique.dosis
              },
          },
          catheter: aTechnique.cateter,
          catheterNote: aTechnique.cateterNote,
          injectionNote: aTechnique.injectionNote
      }
  })
}

private handleAddATechnique(analgesicTechnique: AnalgesicTechniqueData): boolean {
  const currentItems = this.analgesicTechniqueList.length;
  this.analgesicTechniqueList = pushIfNotExists<any>(this.analgesicTechniqueList, analgesicTechnique,
    (first:AnalgesicTechniqueData , second: AnalgesicTechniqueData) => first.snomed.sctid === second.snomed.sctid );
  this.dataEmitter.next(this.analgesicTechniqueList);
  return currentItems === this.analgesicTechniqueList.length;
}

private checkDosisError() {
  if (this.form.controls.dosis.hasError('min')) {
      this.translateService.get('forms.MIN_ERROR', { min: PREMEDICATION.MIN.dosis }).subscribe(
          (errorMsg: string) => this.dosisErrorSource.next(errorMsg)
      );
  }
}

}


export interface AnalgesicTechniqueData {
  snomed: SnomedDto,
  dosis: number,
  unit: string,
  cateter: boolean,
  cateterNote: string,
  injectionNote: string
}

export interface AnalgesicTechniqueForm {
  snomed: FormControl<string>;
  dosis: FormControl<number>;
  unit: FormControl<string>;
  cateter: FormControl<Cateter_options>;
  cateterNote: FormControl<string>;
  injectionNote: FormControl<string>;
}

export enum Cateter_options {
  CATETER_ENABLED,
  CATETER_DISABLED
}