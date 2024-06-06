import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AnestheticSubstanceDto, NewDosageDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FluidAdministrationService {

  snomedConcept: SnomedDto
  fluidAdministrationDataList: FluidAdministrationData[] = [];
  private form: FormGroup<FluidAdministrationForm>
  private readonly ECL = SnomedECL.MEDICINE;
  private dataEmitter = new BehaviorSubject<FluidAdministrationData[]>(this.fluidAdministrationDataList);

  constructor(
    private readonly snomedService: SnomedService,
    private readonly snackBarService: SnackBarService
  ) {
    this.form = new FormGroup<FluidAdministrationForm>({
      snomed: new FormControl(null, Validators.required),
      amount: new FormControl(null),
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
      const newAnestheticTechnique: FluidAdministrationData = {
        snomed: this.snomedConcept,
        amount: this.form.value.amount,
      };
      if (this.handleAddFluidAdministration(newAnestheticTechnique))
        this.snackBarService.showError("Medicacion duplicada");
      this.resetForm();
      return true;
    }
    return false;
  }

  resetForm(): void {
    delete this.snomedConcept;
    this.form.reset();
  }


  remove(index: number): void {
    this.fluidAdministrationDataList = removeFrom<FluidAdministrationData>(this.fluidAdministrationDataList, index);
    this.dataEmitter.next(this.fluidAdministrationDataList);
  }

  getFluidAdministrationList(): Observable<FluidAdministrationData[]> {
    return this.dataEmitter.asObservable()
  }

  getForm(): FormGroup<FluidAdministrationForm> {
    return this.form;
  }

  getECL(): SnomedECL {
    return this.ECL;
  }

  setConcept(selectedConcept: SnomedDto): void {
    this.snomedConcept = selectedConcept;
    const pt = selectedConcept ? selectedConcept.pt : '';
    this.form.controls.snomed.setValue(pt)
  }

  getFluidAdministrationDto(): AnestheticSubstanceDto[] {
    return this.fluidAdministrationDataList.map(fluidAdministrationData => {
          return {
            snomed: fluidAdministrationData.snomed,
            dosage: this.setNewDosageDto(fluidAdministrationData)
          }
        }
    )
  }

  private setNewDosageDto(fluidAdministrationData: FluidAdministrationData): NewDosageDto {
    return fluidAdministrationData.amount ? {
          chronic: null,
          diary: null,
          quantity: {
            unit: 'ml',
            value: fluidAdministrationData.amount
          },
        }
        : null;
  }

  private handleAddFluidAdministration(anestheticTechnique: FluidAdministrationData): boolean {
    const currentItems = this.fluidAdministrationDataList.length;
    this.fluidAdministrationDataList = pushIfNotExists<any>(this.fluidAdministrationDataList, anestheticTechnique,
      (first: FluidAdministrationData, second: FluidAdministrationData) => first.snomed.sctid === second.snomed.sctid);
    this.dataEmitter.next(this.fluidAdministrationDataList);
    return currentItems === this.fluidAdministrationDataList.length;
  }

}


export interface FluidAdministrationForm {
  snomed: FormControl<string>,
  amount: FormControl<number>,
}

export interface FluidAdministrationData {
  snomed: SnomedDto,
  amount: number,
}