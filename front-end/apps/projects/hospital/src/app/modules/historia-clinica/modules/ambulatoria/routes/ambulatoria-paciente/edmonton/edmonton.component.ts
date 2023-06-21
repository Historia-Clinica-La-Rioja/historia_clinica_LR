import { Input ,Component, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { ContextService } from '@core/services/context.service';
import { PatientService } from '@api-rest/services/patient.service';
import { BasicPatientDto } from '@api-rest/api-model';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-edmonton',
  templateUrl: './edmonton.component.html',
  styleUrls: ['./edmonton.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EdmontonComponent {
  
  patientId: number;
  totalSum: number = 0;
  selectedCognitiveOption: number = 0;
  selectedHealthStatusOption: number = 0;
  selectedHealthStatusOptionDos: number = 0;
  selectedFunctionIndOption: number = 0;
  selectedSupportSocOption: number = 0;
  selectedMedicationOption: number = 0;
  selectedMedicationOptionDos: number = 0;
  selectedNutritionOption: number = 0;
  selectedAnimoOption: number = 0;
  selectedContingenciaOption: number = 0;
  selectedRendimientoFuncOption: number = 0; 
  selectedCalificacionFuncOption: number = 0;
  selectedOptions: any[] = [
    { value: 1, sum: 0 },
    { value: 2, sum: 1 },
    { value: 3, sum: 2 },

    { value: 4, sum: 0 },
    { value: 5, sum: 1 },
    { value: 6, sum: 2 },

    { value: 7, sum: 0 },
    { value: 8, sum: 1 },
    { value: 9, sum: 2 },

    { value: 10, sum: 0 },
    { value: 11, sum: 1 },
    { value: 12, sum: 2 },

    { value: 13, sum: 0 },
    { value: 14, sum: 1 },
    { value: 15, sum: 2 },

    { value: 19, sum: 0 },
    { value: 20, sum: 1 },
    
  ];

  
  constructor(
    private cdr: ChangeDetectorRef,
    private edmontonService: EdmontonService,
    private readonly contextService: ContextService,
    private readonly route: ActivatedRoute,
    ) {}

  onOptionSelected(): void {
    this.cdr.markForCheck();  
  }

  updateSum(): void {
    this.totalSum = 0;
    this.selectedOptions.forEach(option => {
      if (option.value === this.selectedCognitiveOption) {
        this.totalSum += option.sum;
      }
      if (option.value === this.selectedHealthStatusOption) {
        this.totalSum += option.sum;
      }
      if (option.value === this.selectedHealthStatusOptionDos) {
        this.totalSum += option.sum;
      }
      if (option.value === this.selectedFunctionIndOption) {
        this.totalSum += option.sum;
      }
      if (option.value === this.selectedSupportSocOption) {
        this.totalSum += option.sum;
      }
      if (option.value === this.selectedMedicationOption) {
        this.totalSum += option.sum;
      }
      if (option.value === this.selectedMedicationOptionDos) {
        this.totalSum += option.sum;
      }
      if (option.value === this.selectedNutritionOption) {
        this.totalSum += option.sum;
      }
      if (option.value === this.selectedAnimoOption) {
        this.totalSum += option.sum;
      }
      if (option.value === this.selectedContingenciaOption) {
        this.totalSum += option.sum;
      }
      
      
      // Repite lo anterior para cada una de las opciones
    });
    alert("Total sum: " + this.totalSum);
  }
  
  //   this.totalSum = +this.selectedCognitiveOption + 
  //                   +this.selectedHealthStatusOption + 
  //                   +this.selectedHealthStatusOptionDos+
  //                   +this.selectedFunctionIndOption+
  //                   +this.selectedSupportSocOption+
  //                   +this.selectedMedicationOption+
  //                   +this.selectedMedicationOptionDos+
  //                   +this.selectedNutritionOption+
  //                   +this.selectedAnimoOption+
  //                   +this.selectedContingenciaOption+
  //                   +this.selectedRendimientoFuncOption+
  //                   +this.selectedCalificacionFuncOption;
  //   alert("Total sum: " + this.totalSum);
  
  onRadioChange(event): void {
    const sum = parseInt(event.target.getAttribute('sum'), 10);
    this.totalSum += sum;
  }
  

 
  onSubmit(): void {

    this.route.paramMap.subscribe(params => {
      this.patientId = Number(params.get('idPaciente'));
    })
    
    const datos = {
      edMonton: [
        {
          questionId: 2, 
          answerId: this.selectedCognitiveOption
        },
        {
          questionId: 4, 
          answerId: this.selectedHealthStatusOption
        },
        {
          questionId: 5, 
          answerId: this.selectedHealthStatusOptionDos
        },
        {
          questionId: 7, 
          answerId: this.selectedFunctionIndOption
        },
        {
          questionId: 9, 
          answerId: this.selectedSupportSocOption
        },
        {
          questionId: 11, 
          answerId: this.selectedMedicationOption
        },
        {
          questionId: 13, 
          answerId: this.selectedMedicationOptionDos
        },
        {
          questionId: 14, 
          answerId: this.selectedNutritionOption
        },
        {
          questionId: 16, 
          answerId: this.selectedAnimoOption
        },
        {
          questionId: 18, 
          answerId: this.selectedContingenciaOption
        },
        {
          questionId: 20, 
          answerId: this.selectedRendimientoFuncOption
        },
      ]
    };
    console.log(datos)
    this.edmontonService.crearEdMonton(this.patientId, this.contextService.institutionId, datos).subscribe(result => {
      alert('okk');
    });
  }
  
  
}



