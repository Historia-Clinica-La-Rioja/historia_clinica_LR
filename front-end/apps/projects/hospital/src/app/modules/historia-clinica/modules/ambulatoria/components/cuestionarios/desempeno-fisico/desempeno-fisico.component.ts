import { Component, Inject, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { PhysicalPerformanceService } from '@api-rest/services/physical-performance.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-desempeno-fisico',
  templateUrl: './desempeno-fisico.component.html',
  styleUrls: ['./desempeno-fisico.component.scss']
})
export class DesempenoFisicoComponent implements OnInit {

  selectedoptionA: string = '';
  selectedoptionB: string = '';
  selectedoptionC: string = '';
  selectedoptionD: string = '';
  selectedoptionE: string = '';
  selectedoptionB2: string = '';
  selectedoptionE2: string = '';
  counterB2: number;
  counterC2: number = undefined;
  counterC3: number = undefined;
  counterE1: number = undefined;
  counterA1: number;
  counterA2: number;
  totalScore: number = 0;
  Calculefinal: any;
  finalScore: number = 0;
  calculatePoints: number;
  ticketNum1: number = 0;
  iconActive: boolean;
  iconImageSource: string = 'assets/icons/icon-07.png';
  iconImageSource2: string = 'assets/icons/icon-08.png';
  iconImageSource3: string = 'assets/icons/icon-12.png'
  customIconSource2: string = 'assets/icons/icon-09.png';
  customIconSource: string = 'assets/icons/icon-10.png';
  customIconSource3: string = 'assets/icons/icon-11.png';

  patientId: number;
  questionnaireId: number;

  constructor(private physicalService: PhysicalPerformanceService,

    @Inject(MAT_DIALOG_DATA) public data: any,

  ) {
    this.patientId = data.patientId

  }

  ngOnInit(): void {
  }

  onInputDesempenoChange(option: string, counter: number, inputType: string): void {
    if (inputType === 'A') {
      this.selectedoptionA = option;
      this.counterA1 = counter;
    } else if (inputType === 'B') {
      this.selectedoptionB = option;
      this.counterA2 = counter;
    }
  }

  onInputDesempenoChange2(event: any): void {
    this.counterE1 = event.target.value;
    this.calculePoints(); 
  }

  isSubmitDisabled(): boolean {
    return !(
      this.selectedoptionA === '1A'
    )
  }

  isCounterDisabled(): boolean {
    return !(

      this.selectedoptionA === '1A' || this.selectedoptionA === '2A'

    )
  }
  isCounterDisabled2(): boolean {
    return !(

      this.selectedoptionB == '1B' || this.selectedoptionB === '2B'

    )

  }

  isSubmitDisabled3(): boolean {
    return !(

      this.selectedoptionB === '1B'
    )

  }

  isCounterDisabled3(): boolean {
    return !(
      this.selectedoptionB === '1B'
    )
  }

  isCounterDisabledCounterC(): boolean {
    return !(
      this.selectedoptionB2 !== '1C'
    )
  }

  isCounterDisabled4(): boolean {
    return !(
      this.selectedoptionC !== 'D3'
    )

  }

  isCounterDisabled5(): boolean {
    return !(
      this.selectedoptionD !== 'E3'

    )
  }

  isSubmitDisabled4(): boolean {
    return !(
      this.selectedoptionC !== 'D3'

    )
  }

  isSubmitDisabled5(): boolean {
    return !(
      this.selectedoptionE === '1D'

    )
  }

  isCounterDisabled6(): boolean {
    return !(
      this.selectedoptionE === '1D'

    )
  }

  isCounterDisabled7(): boolean {
    return !(
      this.selectedoptionE2 === '1FINAL'

    )
  }

  calculePoints(): number {
    let balance1 = (this.selectedoptionA === '1A') ? 1 : 0;
    let balance2 = (this.selectedoptionB === '1B') ? 1 : 0;


    if (this.selectedoptionA === '2A' || this.selectedoptionA === '3A') {
      this.iconImageSource = this.customIconSource;
    } else {
      this.iconImageSource = 'assets/icons/icon-07.png';
    }

    if (this.selectedoptionA === '2A' || this.selectedoptionA === '3A' || this.selectedoptionB === '2B' || this.selectedoptionB === '3B') {
      this.iconImageSource2 = this.customIconSource2;
    } else {
      this.iconImageSource2 = 'assets/icons/icon-08.png';
    }

    if (this.selectedoptionE === '2D' || this.selectedoptionE === '3D') {
      this.iconImageSource3 = this.customIconSource3;
    } else {
      this.iconImageSource3 = 'assets/icons/icon-12.png';
    }


    let counterB2Points = 0;
    if (this.counterB2 < 3.0) {
      counterB2Points = 0;
    } else if (this.counterB2 >= 3.0 && this.counterB2 < 10) {
      counterB2Points = 1;
    } else if (this.counterB2 >= 10 && this.counterB2 <= 15) {
      counterB2Points = 2;
    }

  
    let counterA1NoPoints = 0;

    if (this.counterA1 < 0) {
      counterA1NoPoints = 0;
    };

    let counterA2NoPoints = 0;
    if (this.counterA2 < 0) {
      counterA2NoPoints = 0;
    }


    let counterC2NoPoints = 0;
    if (this.counterC2 < 0) {
      counterC2NoPoints = 0;
    }

    let counterC3Points = 0;

    if (this.counterC3 < 4.82) {
      counterC3Points = 4;

    } else if (this.counterC3 >= 4.82 && this.counterC3 < 6.2) {
      counterC3Points = 3;

    } else if (this.counterC3 >= 6.2 && this.counterC3 < 8.7) {
      counterC3Points = 2;

    } else if (this.counterC3 >= 8.7) {
      counterC3Points = 1;
    }


    let counterE1Points = 0;
     if (this.counterE1 <= 11.19) {
       counterE1Points = 4
    } else if (this.counterE1 >= 11.2 && this.counterE1 <= 13.69) {
      counterE1Points = 3;
     } else if (this.counterE1 > 13.69 && this.counterE1 <= 16.69) {
      counterE1Points = 2;
    } else if (this.counterE1 > 16.69 && this.counterE1 <= 60) {
      counterE1Points = 1;
    } else if (this.counterE1 > 60.00) {
      counterE1Points = 0;
    }
    

    let totalScore = balance1 + balance2 + counterB2Points+ counterA1NoPoints + counterA2NoPoints + counterC2NoPoints
      + counterC3Points + counterE1Points;

    this.finalScore = totalScore;

    this.Calculefinal = (this.finalScore >= 8) ? '1F' : '2F';

    return totalScore;

  }
  submitForm(): void {
    const textColor = this.finalScore >= 8 ? '#2ba05a' : '#ff6054';

    Swal.fire({
      icon: 'question',
      iconColor: '#2687c5',
      title: '¿Está seguro de enviar el formulario?',
      text: `DESEMPEÑO FISICO: ${this.Calculefinal === '1F' ? 'ALTO' : 'BAJO'} - Puntaje final: ${this.finalScore}/12`,
      html: `<span style="color: ${textColor}">DESEMPEÑO FISICO: ${this.Calculefinal === '1F' ? 'ALTO' : 'BAJO'} - Puntaje final: ${this.finalScore}/12</span>`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: 'Enviar',
      confirmButtonColor: '#2687c5',
      denyButtonText: 'Cancelar',

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
              document.getElementById('contenidoData').style.display = 'none';


              Swal.fire({
                icon: 'success',
                iconColor: '#2687c5',
                title: 'Enviado exitosamente',
                confirmButtonColor: '#2687c5',
                confirmButtonText: 'Aceptar',
              });
            }, 2000);
          }

        });
      }
    });
  }

  // PRIMERA SECCION

  mappingBalance1() {
    const firstBalanceMap = {
      '1A': 20,
      '2A': 19,
      '3A': 51,
    };
    return firstBalanceMap[this.selectedoptionA] || undefined;
  }

  mappingCounter1() {
    const firstCounterMap = {
      '4A': 0,
    };
    return firstCounterMap[this.selectedoptionA] || undefined;
  }

  mappingBalance2() {
    const secondBalanceMap = {

      '1B': 20,
      '2B': 19,
      '3B': 51,
    }
    return secondBalanceMap[this.selectedoptionB] || undefined;
  }

  mappingCounter2() {
    const secondCounterMap = {
      'B2': 0,
    };
    return secondCounterMap[this.selectedoptionB] || undefined;
  }

  mappingBalance3() {
    const thirdBalanceMap = {
      '1C': 51,
      '2C': 20,

    }

    return thirdBalanceMap[this.selectedoptionB2] || undefined;
  }


  mappingCounter3() {
    const thirdCounterMap = {
      '4C': 0,
    };
    return thirdCounterMap[this.selectedoptionB2] || undefined;
  }


  // SEGUNDA SECCION

  mappingProgress1() {
    const firstProgressMap = {
      'D1': 20,
      'D3': 51,
    }

    return firstProgressMap[this.selectedoptionC] || undefined;
  }

  mappingCounter4() {
    const fourCounterMap = {
      'D4': 0,
    }
    return fourCounterMap[this.selectedoptionC] || undefined;
  }

  mappingProgress2() {
    const secondProgressMap = {
      'E1': 20,
      'E3': 51,
    }

    return secondProgressMap[this.selectedoptionD] || undefined;
  }
  mappingCounter5() {
    const fiveCounterMap = {
      'E4': 0,
    }
    return fiveCounterMap[this.counterC3] || undefined;
  }


  // TERCERA SECCION 

  mappingMarch1() {
    const firstMarchMap = {
      '1D': 20,
      '2D': 19,
      '3D': 51,
    }

    return firstMarchMap[this.selectedoptionE] || undefined;
  }

  mappingMarch2() {
    const sixMarchMap = {
      '1FINAL': 20,
      '2FINAL': 19,
      '3FINAL': 51,
    }
    return sixMarchMap[this.selectedoptionE2] || undefined;
  }

  mappingCounter6() {
    const sixCounterMap = {
      'F4': 0,
    }
    return sixCounterMap[this.counterE1] || undefined;
  }

  construirDatos() {
    const datos = {
      "questionnaireId": 4,
      "answers": [


        // PRIMERA SECCION
        {
          "itemId": 72,
          "optionId": this.mappingBalance1(),
          "value": ""
        },

        {
          "itemId": 73,
          "optionId": this.mappingCounter1(),
          "value": this.counterA1
        },
        {
          "itemId": 74,
          "optionId": this.mappingBalance2(),
          "value": ""
        },
        {
          "itemId": 75,
          "optionId": this.mappingCounter2(),
          "value": this.counterA2
        },
        {
          "itemId": 76,
          "optionId": this.mappingBalance3(),
          "value": ""
        },
        {
          "itemId": 77,
          "optionId": this.mappingCounter3(),
          "value": this.counterB2
        },

        // SEGUNDA SECCION

        {
          "itemId": 79,
          "optionId": this.mappingProgress1(),
          "value": ""
        },
        {
          "itemId": 80,
          "optionId": this.mappingCounter4(),
          "value": this.counterC2
        },
        {
          "itemId": 81,
          "optionId": this.mappingProgress2(),
          "value": ""
        },
        {
          "itemId": 82,
          "optionId": this.mappingCounter5(),
          "value": this.counterC3
        },

        // TERCERA SECCION 

        {
          "itemId": 84,
          "optionId": this.mappingMarch1(),
          "value": ""
        },

        {
          "itemId": 85,
          "optionId": this.mappingMarch2(),
          "value": ""
        },

        {
          "itemId": 86,
          "optionId": this.mappingCounter6(),
          "value": this.counterE1
        },

      ]
    };
    return datos;
  }

  enviarFormulario(): void {
    this.physicalService.createPhysical(this.patientId, this.construirDatos()).subscribe();
  }

}
