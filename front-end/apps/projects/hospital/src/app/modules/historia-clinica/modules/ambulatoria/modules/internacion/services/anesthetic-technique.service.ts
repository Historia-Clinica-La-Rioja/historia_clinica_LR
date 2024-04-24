import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AnestheticTechniqueDto, MasterDataDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnestheticTechniqueService {

  private form:FormGroup<AnestheticTechniqueForm>

  private readonly ECL = SnomedECL.ANESTHESIA;
  private anestheticTechniqueList: AnestheticTechniqueData[] = []
  private dataEmitter = new BehaviorSubject<AnestheticTechniqueData[]>(this.anestheticTechniqueList);
  snomedConcept: SnomedDto
  trachealIntubationBothIds: number[] = []

  constructor(
    private readonly snomedService: SnomedService,
    private readonly snackBarService: SnackBarService,
  ) {
    this.form = new FormGroup<AnestheticTechniqueForm> ({
      snomed: new FormControl(null, Validators.required),
      circuit: new FormControl(null),
      technique: new FormControl(null),
      trachealIntubation: new FormControl (null),
      trachealIntubationMethod: new FormControl(null),
      breathing: new FormControl(null),
    });

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

  addToList(): boolean {
    if (this.form.valid && this.snomedConcept) {
        const newAnestheticTechnique: AnestheticTechniqueData = {
            snomed: this.snomedConcept,
            circuit: this.form.value.circuit,
            technique: this.form.value.technique,
            trachealIntubation: this.form.value.trachealIntubation  === trachealIntubation_options.TRACHEAL_ENABLED ,
            trachealIntubationMethod: this.form.value.trachealIntubationMethod,
            breathing: this.form.value.breathing,
            trachealIntubationBothIds: this.trachealIntubationBothIds
        };
        if (this.handleAddAnestheticTechnique(newAnestheticTechnique))
            this.snackBarService.showError("Medicacion duplicada");
        this.resetForm();
        return true;
    }
    return false;
}


  remove(index: number): void {
    this.anestheticTechniqueList = removeFrom<AnestheticTechniqueData>(this.anestheticTechniqueList, index);
    this.dataEmitter.next(this.anestheticTechniqueList);
  }

  getForm(): FormGroup<AnestheticTechniqueForm> {
    return this.form;
  }

  getECL(): SnomedECL {
    return this.ECL;
  }

  resetTrachealIntubationMethod(): void {
    this.form.controls.trachealIntubationMethod.setValue(null)
  }

  handleValidatorTrachealIntubationMethod(trachealOption:trachealIntubation_options): void {
    if (trachealOption === trachealIntubation_options.TRACHEAL_ENABLED)
        this.getForm().get('trachealIntubationMethod').setValidators(Validators.required)
    else
    {
        this.getForm().get('trachealIntubationMethod').clearValidators()
        this.getForm().get('trachealIntubationMethod').updateValueAndValidity()
    }
  }

  resetForm(): void {
    delete this.snomedConcept;
    this.trachealIntubationBothIds = []
    this.form.reset();
  }

  getAnestheticTechniqueDto(): AnestheticTechniqueDto[] {
    return this.anestheticTechniqueList.map(anestheticTechnique => {
      return {
        snomed: anestheticTechnique.snomed,
        breathingId: anestheticTechnique.breathing ? anestheticTechnique.breathing.id : null ,
        circuitId: anestheticTechnique.circuit ? anestheticTechnique.circuit.id : null ,
        techniqueId: anestheticTechnique.technique ? anestheticTechnique.technique.id : null,
        trachealIntubation: anestheticTechnique.trachealIntubation,
        trachealIntubationMethodIds: anestheticTechnique?.trachealIntubationBothIds?.length > 0 ?  anestheticTechnique.trachealIntubationBothIds :
        anestheticTechnique?.trachealIntubationBothIds?.length == 0 && anestheticTechnique.trachealIntubation ? [anestheticTechnique.trachealIntubationMethod.id]: null,
      }
    }
    )
  }

  getAnestheticTechniqueList(): Observable<AnestheticTechniqueData[]> {
    return this.dataEmitter.asObservable()
  }

  getTrachealIntubationStatus(): Observable<trachealIntubation_options> {
    return this.form.controls.trachealIntubation.valueChanges
  }

  getTrachealIntubationMethodStatus(): Observable<MasterDataDto> {
    return this.form.controls.trachealIntubationMethod.valueChanges
  }

  setConcept(selectedConcept: SnomedDto): void {
    this.snomedConcept = selectedConcept;
    const pt = selectedConcept ? selectedConcept.pt : '';
    this.form.controls.snomed.setValue(pt)
  }

  setTrachealIntubationMethodIds(TrachealMethods: MasterDataDto[]): void {
    this.trachealIntubationBothIds = TrachealMethods ? TrachealMethods.map(tracheal => tracheal.id) : null
  }

  private handleAddAnestheticTechnique(anestheticTechnique: AnestheticTechniqueData): boolean {
    const currentItems = this.anestheticTechniqueList.length;
    this.anestheticTechniqueList = pushIfNotExists<any>(this.anestheticTechniqueList, anestheticTechnique,
      (first:AnestheticTechniqueData , second: AnestheticTechniqueData) => first.snomed.sctid === second.snomed.sctid );
    this.dataEmitter.next(this.anestheticTechniqueList);
    return currentItems === this.anestheticTechniqueList.length;
  }

}

export interface AnestheticTechniqueForm {
  snomed: FormControl<string>,
  circuit: FormControl<MasterDataDto>,
  technique: FormControl<MasterDataDto>,
  trachealIntubation: FormControl<trachealIntubation_options>,
  trachealIntubationMethod: FormControl<MasterDataDto>,
  breathing: FormControl<MasterDataDto>,
}

export interface AnestheticTechniqueData {
  snomed: SnomedDto,
  circuit: MasterDataDto,
  technique: MasterDataDto,
  trachealIntubation: boolean,
  trachealIntubationMethod: MasterDataDto,
  breathing: MasterDataDto,
  trachealIntubationBothIds: number[]
}

export enum trachealIntubation_options {
  TRACHEAL_ENABLED,
  TRACHEAL_DISABLED
}