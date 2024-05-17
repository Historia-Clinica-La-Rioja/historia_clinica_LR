import { Inject, Component } from '@angular/core';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edmonton',
  templateUrl: './edmonton.component.html',
  styleUrls: ['./edmonton.component.scss'],
})
export class EdmontonComponent {

  selectedCognitiveOption: string = "";
  selectedHealthStatusOption: string = "";
  selectedHealthStatusOptionDos: string = "";
  selectedFunctionIndOption: string = "";
  selectedSupportSocOption: string = "";
  selectedMedicationOption: string = "";
  selectedMedicationOptionDos: string = "";
  selectedNutritionOption: string = "";
  selectedAnimoOption: string = "";
  selectedContingenciaOption: string = "";
  selectedRendimientoFuncOption: string = "";
  patientId: number;
  routePrefix: number;
  submitted: boolean = false;
  calificationTotal: any;
  scoreTotal: number;

  constructor(
    private edmontonService: EdmontonService,

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
    selectedRendimientoFuncOption: any,


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

    // console.log("puntaje:", scoreFinal);
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
      this.selectedRendimientoFuncOption,
    );
  }

  isSubmitDisabled(): boolean {
    return !(

      this.selectedCognitiveOption &&
      this.selectedHealthStatusOption &&
      this.selectedHealthStatusOptionDos &&
      this.selectedFunctionIndOption &&
      this.selectedSupportSocOption &&
      this.selectedMedicationOption &&
      this.selectedMedicationOptionDos &&
      this.selectedNutritionOption &&
      this.selectedAnimoOption &&
      this.selectedContingenciaOption &&
      this.selectedRendimientoFuncOption


    );
  }

  mappingCognitive() {
    const cognitiveMap = {
      'A': 1,
      'B': 2,
      'C': 3
    };

    return cognitiveMap[this.selectedCognitiveOption] || undefined;
  }

  mappingHealhtStatus() {
    const healthMapping = {
      'D': 4,
      'F': 5,
      'G': 6
    };

    return healthMapping[this.selectedHealthStatusOption] || undefined;
  }

  mappingHealthStatusDos() {
    const healthDosMapping = {
      'H': 7,
      'I': 8,
      'J': 9
    };

    return healthDosMapping[this.selectedHealthStatusOptionDos] || undefined;

  }

  mappingRendFunctional() {
    const rendimientoMapping = {
      'Z': 10,
      'Z2': 11,
      'Z3': 12
    };

    return rendimientoMapping[this.selectedRendimientoFuncOption] || undefined;
  }


  mappingIndFunction() {
    const indepFunctionMapping = {

      'K': 13,
      'L': 14,
      'M': 15
    };

    return indepFunctionMapping[this.selectedFunctionIndOption] || undefined;
  }

  mappingSupportSocial() {
    const suppSocialMapping = {
      'N': 16,
      'N2': 17,
      'O': 18
    };

    return suppSocialMapping[this.selectedSupportSocOption] || undefined;
  }

  mappingMedication() {
    const medicationMapping = {
      'P': 19,
      'Q': 20
    };

    return medicationMapping[this.selectedMedicationOption] || undefined;
  }

  mappingMedicationDos() {
    const medicationMappingDos = {
      'R': 19,
      'S': 20
    };

    return medicationMappingDos[this.selectedMedicationOptionDos] || undefined;

  }

  mappingNutrition() {
    const nutritionMapping = {
      'T': 19,
      'U': 20
    };

    return nutritionMapping[this.selectedNutritionOption] || undefined;

  }

  mappingAnimo() {
    const animoMapping = {
      'V': 19,
      'W': 20,

    }

    return animoMapping[this.selectedAnimoOption] || undefined;
  }

  mappingContingencia() {
    const contingenciaMapping = {
      'X': 19,
      'Y': 20,

    }

    return contingenciaMapping[this.selectedContingenciaOption] || undefined;
  }

  mappingtotalScore() {
    const scoreTotalMapping = { 
      'A1': 21,
      'A2': 22,
      'A3': 23,
      'A4': 24,
      'A5': 25,
    }

return scoreTotalMapping[this.calificationTotal] || undefined;

  }

 construirDatos() {

     const datos = {
      "questionnaireId": 1,
      "answers": [

        {
          "itemId": 2,
          "optionId": this.mappingCognitive(),
          "value": "",
        },
        {
          "itemId": 4,
          "optionId": this.mappingHealhtStatus(),
          "value": "",
        },
        {
          "itemId": 5,
          "optionId": this.mappingHealthStatusDos(),
          "value": "",
        },
        {
          "itemId": 7,
          "optionId": this.mappingRendFunctional(),
          "value": "",
        },
        {
          "itemId": 9,
          "optionId": this.mappingIndFunction(),
          "value": "",
        },
        {
          "itemId": 11,
          "optionId": this.mappingSupportSocial(),
          "value": "",
        },
        {
          "itemId": 13,
          "optionId": this.mappingMedication(),
          "value": "",
        },

        {
          "itemId": 14,
          "optionId": this.mappingMedicationDos(),
          "value": "",
        },
        {
          "itemId": 16,
          "optionId": this.mappingNutrition(),
          "value": "",
        },

        {
          "itemId": 18,
          "optionId": this.mappingAnimo(),
          "value": "",
        },

        {
          "itemId": 20,
          "optionId": this.mappingContingencia(),
          "value": "",
        },

        {
          "itemId": 22,
          "optionId": this.mappingtotalScore(),
          "value": "",
        },
      ]
    }
     return datos;
  }

  ngOnInit(): void {
    window.addEventListener('offline', this.goOffline.bind(this));
    window.addEventListener('online', this.goOnline.bind(this));
  }

  onSubmit(): void {
    if (navigator.onLine) {
      this.goOnline();
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
        allowOutsideClick: false,

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
                Swal.fire({
                  icon: 'success',
                  iconColor: '#2687c5',
                  title: 'Enviado con éxito',
                  text: 'El formulario ha sido enviado correctamente.',
                  confirmButtonColor: '#2687c5',
                  confirmButtonText: 'Aceptar',
                  allowOutsideClick: false,

                });
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
            allowOutsideClick: false,

          });
        }
      });
    } else {
      this.goOffline();
      Swal.fire({
        icon: 'error',
        iconColor: '#ff0000',
        title: 'ERROR',
        text: 'No se detectó conexión a internet. Por favor, revise su conexión e inténtelo de nuevo.',
        confirmButtonColor: '#2687c5',
        allowOutsideClick: false,

      });
    }
  }

  goOnline(): void {
    document.body.classList.remove('offline');
    document.body.classList.add('online');
  }

  goOffline(): void {
    document.body.classList.remove('online');
    document.body.classList.add('offline');
  }


  enviarFormulario(): void {
    const questionnaireData = this.construirDatos();
    this.edmontonService.createEdmonton(this.patientId, questionnaireData).subscribe();
  }
}

