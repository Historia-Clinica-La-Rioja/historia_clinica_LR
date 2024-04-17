import { Inject, Component } from '@angular/core';
// import { EdmontonService } from '@api-rest/services/edmonton.service';
// import Swal from 'sweetalert2';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-edmonton',
  templateUrl: './edmonton.component.html',
  styleUrls: ['./edmonton.component.scss'],
})
export class EdmontonComponent {

  selectedCognitiveOption: number;
  selectedHealthStatusOption: number;
  selectedHealthStatusOptionDos: number;
  selectedFunctionIndOption: number;
  selectedSupportSocOption: number;
  selectedMedicationOption: number;
  selectedMedicationOptionDos: number;
  selectedNutritionOption: number;
  selectedAnimoOption: number;
  selectedContingenciaOption: number;
  selectedRendimientoFuncOption: number;
  patientId: number;
  routePrefix: number;
  submitted: boolean = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {

    this.patientId = data.patientId;
  }

  calculateTotal(
    selectedCognitiveOption: any,
    selectedHealthStatusOption: any,
    selectedHealthStatusOptionDos: any,
    selectedFunctionIndOption: any,
    selectedSupportSocOption: any,
    selectedMedicationOption: any,
    selectedMedicationOptionDos: any,
    selectedNutritionOption: any,
    selectedAnimoOption: any,
    selectedContingenciaOption: any,
    selectedRendimientoFuncOption: any
  ): number {

    let cognitiveScore = 0;

    switch (true) {
      case selectedCognitiveOption === 'A':
      case selectedHealthStatusOption === 'D':
      case selectedHealthStatusOptionDos === 'H':
      case selectedFunctionIndOption === 'K':
      case selectedSupportSocOption === 'N':
      case selectedMedicationOption === 'Q':
      case selectedMedicationOptionDos === 'S':
      case selectedNutritionOption === 'U':
      case selectedAnimoOption === 'W':
      case selectedRendimientoFuncOption === 'Z':
      case selectedContingenciaOption === 'Y':
        cognitiveScore = 0;
        break;

      case selectedCognitiveOption === 'B':
      case selectedHealthStatusOption === 'F':
      case selectedHealthStatusOptionDos === 'I':
      case selectedFunctionIndOption === 'L':
      case selectedSupportSocOption === 'N2':
      case selectedMedicationOption === 'P':
      case selectedMedicationOptionDos === 'R':
      case selectedNutritionOption === 'T':
      case selectedAnimoOption === 'V':
      case selectedContingenciaOption === 'X':
      case selectedRendimientoFuncOption === 'Z2':
        cognitiveScore = 1;
        break;

      case selectedCognitiveOption === 'C':
      case selectedHealthStatusOption === 'G':
      case selectedHealthStatusOptionDos === 'J':
      case selectedFunctionIndOption === 'M':
      case selectedSupportSocOption === 'O':
      case selectedRendimientoFuncOption === 'Z3':
        cognitiveScore = 2;
        break;

      default:
        break;
    }

    console.log("puntaje:", cognitiveScore);
    return cognitiveScore;
  }

}
  

   //hay valores donde SI se imprimen en 0 en vez de mandarse 1. corroborar motivo.


















// IsSubmitDisabled(): boolean {
//   return !(
//     this.selectedCognitiveOption &
//     this.selectedHealthStatusOption &
//     this.selectedHealthStatusOptionDos &&
//     this.selectedFunctionIndOption &&
//     this.selectedSupportSocOption &&
//     this.selectedRendimientoFuncOption &&
//     this.selectedMedicationOption &&
//     this.selectedMedicationOptionDos &&
//     this.selectedNutritionOption &&
//     this.selectedAnimoOption &&
//     this.selectedContingenciaOption
//   );
// }









// construirDatos() {
//   const questionnaireAnswers = {
//     "questionnaireId": 1,
//     "answers": [

//       {
//         itemId: 2,
//         optionId: this.selectedCognitiveOption,
//         value: this.sharedLyric(this.selectedCognitiveOption)
//         },
//         {
//         itemId: 4,
//         optionId: this.selectedHealthStatusOption,
//         value: this.sharedLyric(this.selectedHealthStatusOption)
//         },
//       {
//         itemId: 5,
//         optionId: this.selectedHealthStatusOptionDos,
//         value: this.sharedLyric(this.selectedHealthStatusOptionDos)
//       },
//       {
//         itemId: 7,
//         optionId: this.selectedFunctionIndOption,
//         value: this.sharedLyric(this.selectedFunctionIndOption)
//       },
//       {
//         itemId: 9,
//         optionId: this.selectedSupportSocOption,
//         value: this.sharedLyric(this.selectedSupportSocOption)
//       },
//       {
//         itemId: 11,
//         optionId: this.selectedRendimientoFuncOption,
//         value: this.sharedLyric(this.selectedRendimientoFuncOption)
//       },
//       {
//         itemId: 13,
//         optionId: this.selectedMedicationOption,
//         value: ''
//       },
//       {
//         itemId: 14,
//         optionId: this.selectedMedicationOptionDos,
//         value: ''
//       },
//       {
//         itemId: 16,
//         optionId: this.selectedNutritionOption,
//         value: ''
//       },
//       {
//         itemId: 18,
//         optionId: this.selectedAnimoOption,
//         value: ''
//       },
//       {
//         itemId: 20,
//         optionId: this.selectedContingenciaOption,
//         value: ''
//       },
//       {
//         itemId: 22,
//         optionId: this.sumaAcumulada,
//         value: this.selectedCalificacion
//       },
//     ]
//   }

//   return questionnaireAnswers;

// }


// onSubmit(): void {
//   this.submitted = true;

//   if (
//     this.selectedCognitiveOption === 0,
//     this.selectedHealthStatusOption === 0,
//     this.selectedHealthStatusOptionDos === 0,
//     this.selectedFunctionIndOption === 0,
//     this.selectedSupportSocOption === 0,
//     this.selectedRendimientoFuncOption === 0,
//     this.selectedMedicationOption === 0,
//     this.selectedMedicationOptionDos === 0,
//     this.selectedNutritionOption === 0,
//     this.selectedAnimoOption === 0,
//     this.selectedContingenciaOption === 0

//   ) {
//     alert('Por favor, complete todas las selecciones requeridas.');
//     return;
//   }
// Swal.fire({
//   icon: 'question',
//   iconColor: '#2687c5',
//   title: '¿Está seguro de enviar el formulario?',
//   text: 'Por favor, revise las opciones marcadas antes de presionar Enviar',
//   showDenyButton: true,
//   showCancelButton: true,
//   confirmButtonText: 'Enviar',
//   confirmButtonColor: '#2687c5',
//   denyButtonText: 'No enviar',
// }).then((result) => {
//   if (result.isConfirmed) {
//     Swal.fire({
//       icon: 'info',
//       iconColor: '#2687c5',
//       title: 'Enviando...',
//       text: 'Por favor, espere un momento.',
//       allowOutsideClick: false,
//       didOpen: () => {
//         Swal.showLoading();
//         setTimeout(() => {
//           Swal.close();
//           this.enviarFormulario();
//         }, 2000);
//       },
//     });
//   } else if (result.isDenied) {
//     Swal.fire({
//       icon: 'warning',
//       iconColor: '#ff0000',
//       title: 'Formulario cancelado',
//       text: 'El formulario no ha sido enviado.',
//       confirmButtonColor: '#2687c5',
//       confirmButtonText: 'Aceptar',
//     });
//   }
// });
// }

//   enviarFormulario(): void {
//     const questionnaireData = this.construirDatos();
//     this.edmontonService.createEdmonton(this.patientId, questionnaireData).subscribe();

//     console.log(this.enviarFormulario, this.edmontonService, questionnaireData)
//   }

// }
