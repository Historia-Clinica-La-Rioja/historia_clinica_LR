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

  selectedCognitiveOption: string = '';
  selectedHealthStatusOption: string = '';
  selectedHealthStatusOptionDos: string = '';
  selectedFunctionIndOption: string = '';
  selectedSupportSocOption: string = '';
  selectedMedicationOption: string = '';
  selectedMedicationOptionDos: string = '';
  selectedNutritionOption: string = '';
  selectedAnimoOption: string = '';
  selectedContingenciaOption: string = '';
  selectedRendimientoFuncOption: string = '';
  patientId: number;
  routePrefix: number;
  submitted: boolean = false;
  calificationTotal: any;
  scoreTotal: number;

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
    let scoreFinal =
      (selectedCognitiveOption === 'A' ? 0 : selectedCognitiveOption === 'B' ? 1 : selectedCognitiveOption === 'C' ? 2 : 0) +
      (selectedHealthStatusOption === 'D' ? 0 : selectedHealthStatusOption === 'F' ? 1 : selectedHealthStatusOption === 'G' ? 2 : 0) +
      (selectedHealthStatusOptionDos === 'H' ? 0 : selectedHealthStatusOptionDos === 'I' ? 1 : selectedHealthStatusOptionDos === 'J' ? 2 : 0) +
      (selectedFunctionIndOption === 'K' ? 0 : selectedFunctionIndOption === 'L' ? 1 : selectedFunctionIndOption === 'M' ? 2 : 0) +
      (selectedSupportSocOption === 'N' ? 0 : selectedSupportSocOption === 'N2' ? 1 : selectedSupportSocOption === 'O' ? 2 : 0) +
      (selectedMedicationOption === 'P' ? 1 : selectedMedicationOption === 'Q' ? 0 : 0) +
      (selectedMedicationOptionDos === 'R' ? 1 : selectedMedicationOptionDos === 'S' ? 0 : 0) +
      (selectedNutritionOption === 'T' ? 1 : selectedNutritionOption === 'U' ? 0 : 0) +
      (selectedAnimoOption === 'V' ? 1 : selectedAnimoOption === 'W' ? 0 : 0) +
      (selectedContingenciaOption === 'X' ? 1 : selectedContingenciaOption === 'Y' ? 0 : 0) +
      (selectedRendimientoFuncOption === 'Z' ? 0 : selectedRendimientoFuncOption === 'Z2' ? 1 : selectedRendimientoFuncOption === 'Z3' ? 2 : 0);

    this.scoreTotal = scoreFinal;

    if (scoreFinal <= 4) {
      this.calificationTotal = 'A1';
    } else if (scoreFinal >= 5 && scoreFinal <= 6) {
      this.calificationTotal = 'A2';
    } else if (scoreFinal >= 7 && scoreFinal <= 8) {
      this.calificationTotal = 'A3';
    } else if (scoreFinal >= 9 && scoreFinal <= 10) {
      this.calificationTotal = 'A4';
    } else if (scoreFinal >= 11) {
      this.calificationTotal = 'A5';
    }

    console.log("puntaje:", scoreFinal);
    return scoreFinal;
  }

  parametersOptions(): void {
    this.calculateTotal(
      this.selectedCognitiveOption,
      this.selectedHealthStatusOption,
      this.selectedHealthStatusOptionDos,
      this.selectedFunctionIndOption,
      this.selectedSupportSocOption,
      this.selectedMedicationOption,
      this.selectedMedicationOptionDos,
      this.selectedNutritionOption,
      this.selectedAnimoOption,
      this.selectedContingenciaOption,
      this.selectedRendimientoFuncOption
    );
  }
}























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
