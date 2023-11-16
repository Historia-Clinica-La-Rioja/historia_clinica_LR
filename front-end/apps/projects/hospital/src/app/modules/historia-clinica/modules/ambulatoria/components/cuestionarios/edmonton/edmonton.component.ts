import { Input, Inject,Component, ChangeDetectionStrategy} from '@angular/core';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { ContextService } from '@core/services/context.service';
import { BasicPatientDto } from '@api-rest/api-model';
import Swal from 'sweetalert2';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CreateQuestionnaireDTO, QuestionnaireAnswerDTO } from '@api-rest/api-model';


@Component({
  selector: 'app-edmonton',
  templateUrl: './edmonton.component.html',
  styleUrls: ['./edmonton.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EdmontonComponent {
  
  private readonly routePrefix;
  totalSum: number;
  selectedCognitiveOption: number;
  selectedHealthStatusOption: number;
  selectedHealthStatusOptionDos: number;
  selectedFunctionIndOption: number;
  selectedSupportOption: number;
  selectedSupportSocOption: number;
  selectedMedicationOption: number;
  selectedMedicationOptionDos: number;
  selectedNutritionOption: number;
  selectedAnimoOption: number;
  selectedContingenciaOption: number;
  selectedRendimientoFuncOption: number; 
  selectedCalificacionFuncOption: number;
  submitted: boolean = false;
  @Input() patientId: number;
  sumaAcumulada: number; 
  selectedValues: number[] = [];
  cumulativeSum: number = 0;
  patientData: BasicPatientDto | undefined;
  datos: any;
  selectedCalificacion: string = '';
  patient: number;

  

  constructor(
    private edmontonService: EdmontonService,
    private readonly contextService: ContextService,
    @Inject (MAT_DIALOG_DATA) public data: any,
    ) 
    {
      this.patient = data.patientId;
      this.routePrefix = `${this.contextService.institutionId}`;
    }

    onOptionSelected(questionIndex: number, value: number): void {
      this.selectedValues[questionIndex] = value;
      this.calcularSuma();
    }
      
    calcularSuma(): void {
      const valueToSumMapping = {};
      for (let i = 1; i <= 20; i++) {
        if (i % 3 === 1) {
          valueToSumMapping[i] = 0;
        } else if (i % 3 === 2) {
          valueToSumMapping[i] = 1;
        } else {
          valueToSumMapping[i] = 2;
        }
      }
    
      this.sumaAcumulada = this.selectedValues.reduce((acc, value) => {
        const mappedSum = valueToSumMapping[value] || 0; // Usar el mapeo
        return acc + mappedSum;
      }, 0);
    
      if (this.sumaAcumulada >= 11) {
        this.selectedCalificacion = 'A33';
        this.sumaAcumulada = 25;
      } else if (this.sumaAcumulada >= 9) {
        this.selectedCalificacion = 'A32';
        this.sumaAcumulada = 24;
      } else if (this.sumaAcumulada >= 7) {
        this.selectedCalificacion = 'A31';
        this.sumaAcumulada = 23;
      } else if (this.sumaAcumulada >= 5) {
        this.selectedCalificacion = 'A30';
        this.sumaAcumulada = 22;
      } else {
        this.selectedCalificacion = 'A29'
        this.sumaAcumulada = 21;
      }
    }
    
    
    sharedLyric(answerId: number): string {
      switch (answerId) {
        case 1:
          return "A1";
        case 2:
          return "A2";
        case 3:
          return "A3";
        case 4: 
          return "A4";
        case 5: 
          return "A5";
        case 6:
          return "A6";
        case 7:
          return "A7";
        case 8:
          return "A8";
        case 9:
          return "A9";
        case 10:
          return "A10";
        case 11:
          return "A11";
        case 12: 
          return "A12";
        case 13:
          return "A13";
        case 14:
          return "A14";
        case 15:
          return "A15";
        case 16: 
          return "A16";
        case 17: 
          return "A17";
        case 18:
          return "A18"
        default:
          return ""; 
      }
    }
    value_1(questionId:number, answerId:number ): string {
      if (questionId === 13 && answerId === 19) {
        return "A19";
      } else if (questionId === 14 && answerId === 19) {
        return "A21";
      } else if (questionId === 16 && answerId === 19) {
        return "A23";
      } else if (questionId === 18 && answerId === 19) {
        return "A25";
      } else if (questionId === 20 && answerId === 19) {
        return "A27";
      } else {
          if(questionId === 13 && answerId === 20 ){
            return "A20";
          }else if (questionId === 14 && answerId === 20) {
            return "A22";
          }else if (questionId === 16 && answerId === 20) {
            return "A24";
          }else if (questionId === 18 && answerId === 20) {
            return "A26";
          }else if (questionId === 20 && answerId === 20) {
            return "A28";
          }
      }
    }

    construirDatos(): CreateQuestionnaireDTO {
    this.calcularSuma();
    const questionnaireAnswers: QuestionnaireAnswerDTO[] = [
      {
          questionId: 2,
          answerId: this.selectedCognitiveOption,
          value: this.sharedLyric(this.selectedCognitiveOption)  
      },
      {
          questionId: 4, 
          answerId: this.selectedHealthStatusOption,
          value: this.sharedLyric(this.selectedHealthStatusOption)
      },
      {
          questionId: 5, 
          answerId: this.selectedHealthStatusOptionDos,
          value: this.sharedLyric(this.selectedHealthStatusOptionDos)
      },
      {
          questionId: 7, 
          answerId: this.selectedFunctionIndOption,
          value: this.sharedLyric(this.selectedFunctionIndOption)
      },
      {
          questionId: 9, 
          answerId: this.selectedSupportSocOption,
          value: this.sharedLyric(this.selectedSupportSocOption)
      },
      {
          questionId: 11, 
          answerId: this.selectedRendimientoFuncOption,
          value: this.sharedLyric(this.selectedRendimientoFuncOption)
      },
      {
          questionId: 13, 
          answerId: this.selectedMedicationOption,
          value: ''
      },
      {
          questionId: 14, 
          answerId: this.selectedMedicationOptionDos,
          value: ''
      },
      {
          questionId: 16, 
          answerId: this.selectedNutritionOption,
          value: ''
      },
      {
          questionId: 18, 
          answerId: this.selectedAnimoOption,
          value: ''
      },
      {
          questionId: 20, 
          answerId: this.selectedContingenciaOption,
          value: ''    
      },
      
      {
        questionId: 22, 
        answerId: this.sumaAcumulada,
        value: this.selectedCalificacion
      },
    ];
    const questionnaireData: CreateQuestionnaireDTO = {
      questionnaire: questionnaireAnswers
    };
  
    return questionnaireData;
  }
  
 
  onSubmit(): void {
    this.submitted = true;
      
    if (
      this.selectedCognitiveOption === 0 ,
      this.selectedHealthStatusOption ===0,
      this.selectedHealthStatusOptionDos === 0,
      this.selectedFunctionIndOption === 0,
      this.selectedSupportSocOption === 0,
      this.selectedRendimientoFuncOption === 0,
      this.selectedMedicationOption === 0,
      this.selectedMedicationOptionDos === 0,
      this.selectedNutritionOption === 0 ,
      this.selectedAnimoOption ===0,
      this.selectedContingenciaOption===0
      
    ) {
      alert('Por favor, complete todas las selecciones requeridas.');
      return; 
    }
    Swal.fire({
      icon: 'question',
      iconColor: '#2687c5',
      title: '¿Está seguro de enviar el formulario?',
      text: 'Por favor, revise las opciones marcadas antes de presionar Enviar',
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: 'Enviar',
      confirmButtonColor: '#2687c5',
      denyButtonText: 'No enviar',
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          icon: 'info',
          iconColor: '#2687c5',
          title: 'Enviando...',
          text: 'Por favor, espere un momento.',
          allowOutsideClick: false,
          didOpen: () => {
            Swal.showLoading();
            setTimeout(() => {
              Swal.close();
              this.enviarFormulario(); 
            }, 2000);
          },
        });
      } else if (result.isDenied) {
        Swal.fire({
          icon: 'warning',
          iconColor: '#ff0000',
          title: 'Formulario cancelado',
          text: 'El formulario no ha sido enviado.',
          confirmButtonColor: '#2687c5',
          confirmButtonText: 'Aceptar',
        });
      }
    });
  }

  enviarFormulario(): void {
    const datos: CreateQuestionnaireDTO = this.construirDatos();
    this.edmontonService.crearEdMonton(this.routePrefix, this.patient, datos).subscribe(
      () => {
        console.log('Los datos se han enviado correctamente.');
      },
      (error) => {
        console.log('Error Los datos no se han enviado correctamente.');
      }
    );
  }
  
  
}



