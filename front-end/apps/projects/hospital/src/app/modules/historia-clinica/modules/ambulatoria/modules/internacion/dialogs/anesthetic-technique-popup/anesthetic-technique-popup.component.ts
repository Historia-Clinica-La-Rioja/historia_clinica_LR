import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { AnestheticTechniqueService, trachealIntubation_options } from '../../services/anesthetic-technique.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormGroup } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { MasterDataDto } from '@api-rest/api-model';
import { Observable, Subscription, forkJoin, take } from 'rxjs';
import { hasError } from '@core/utils/form.utils';

@Component({
  selector: 'app-anesthetic-technique-popup',
  templateUrl: './anesthetic-technique-popup.component.html',
  styleUrls: ['./anesthetic-technique-popup.component.scss']
})
export class AnestheticTechniquePopupComponent implements OnInit, OnDestroy {

  form: FormGroup;
  readonly hasError = hasError;
  enableTrachealIntubationOptions = false
  TRACHEAL_OPTIONS = trachealIntubation_options
  BOTH_OPTION:MasterDataDto  = {id: 3, description:'Ambas'}

  techniques: MasterDataDto[]
  trachealIntubations: MasterDataDto[]
  circuits: MasterDataDto[]
  breathings: MasterDataDto[]

  private suscriptionTrachealStatus: Subscription
  private suscriptionTrachealMethodStatus: Subscription

  constructor(
    public dialogRef: MatDialogRef<AnestheticTechniquePopupComponent>,
    @Inject(MAT_DIALOG_DATA) public readonly data: AnestheticTechniqueData,
    private readonly internacionMasterDataService: InternacionMasterDataService,
  ) { }

  ngOnInit(): void {
    this.form = this.data.anestheticTechniqueService.getForm()
    const techniqueType$: Observable<MasterDataDto[]> = this.internacionMasterDataService.getAnestheticTechniqueTypes()
    const trachealIntubationType$: Observable<MasterDataDto[]> = this.internacionMasterDataService.getTrachealIntubationTypes()
    const circuitType$: Observable<MasterDataDto[]> = this.internacionMasterDataService.getCircuitTypes()
    const breathingType$: Observable<MasterDataDto[]> = this.internacionMasterDataService.getBreathingTypes()

    forkJoin([techniqueType$,trachealIntubationType$,circuitType$,breathingType$]).pipe(take(1)).subscribe(
      ([techniqueTypes, trachealIntubationTypes, circuitTypes, breathingTypes]) => {
        this.techniques = techniqueTypes
        this.trachealIntubations = trachealIntubationTypes.concat(this.BOTH_OPTION)
        this.circuits = circuitTypes
        this.breathings= breathingTypes
      }
    )

    this.suscriptionTrachealStatus = this.data.anestheticTechniqueService.getTrachealIntubationStatus().subscribe(
      status => {
        this.enableTrachealIntubationOptions =  trachealIntubation_options.TRACHEAL_ENABLED === status
        if (!this.enableTrachealIntubationOptions){
          this.data.anestheticTechniqueService.resetTrachealIntubationMethod()
        }
        this.data.anestheticTechniqueService.handleValidatorTrachealIntubationMethod(status)
      }
    )

    this.suscriptionTrachealMethodStatus = this.data.anestheticTechniqueService.getTrachealIntubationMethodStatus().subscribe(
      trachealMethod => {
        if (trachealMethod?.description === this.BOTH_OPTION.description)
          this.data.anestheticTechniqueService.setTrachealIntubationMethodIds(this.trachealIntubations
            .filter(tracheal => tracheal.description !== this.BOTH_OPTION.description))
        if (trachealMethod == null)
          this.data.anestheticTechniqueService.setTrachealIntubationMethodIds(null)
      }
    )
  }

  close(): void {
    this.dialogRef.close()
    this.data.anestheticTechniqueService.resetForm();
  }

  addAnestheticTechnique(): void {
    if (this.form.valid) {
      this.data.anestheticTechniqueService.addToList();
      this.close()
    }
  }

  ngOnDestroy(): void {
    this.suscriptionTrachealStatus.unsubscribe()
    this.suscriptionTrachealMethodStatus.unsubscribe()
  }

}

interface AnestheticTechniqueData {
  anestheticTechniqueService: AnestheticTechniqueService,
  searchConceptsLocallyFF: boolean,
}