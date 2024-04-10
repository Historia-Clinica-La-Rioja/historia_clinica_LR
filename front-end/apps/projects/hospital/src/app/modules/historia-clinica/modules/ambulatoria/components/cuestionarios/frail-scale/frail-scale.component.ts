import { Component, Inject, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { FrailService } from '@api-rest/services/fragility-test.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-frail-scale',
  templateUrl: './frail-scale.component.html',
  styleUrls: ['./frail-scale.component.scss']
})
export class FrailScaleComponent implements OnInit {

  selectedFatigability: string = '';
  selectedEndurance: string = '';
  selectedAmbulation: string = '';
  selectedComorbidity: string[] = [];
  selectedWeightLoss: string = '';
  selectedCalificacion: string = '';
  selectedComorbidityCount: string = '';
  totalScore: any;
  patientId: any;
  routePrefix: number;
  submitted: boolean = false;

  constructor(
    private frailService: FrailService,
    @Inject(MAT_DIALOG_DATA) public data: any,

  ){ 
    this.patientId = data.patientId
  }

  ngOnInit(): void {
  }

  isSubmitDisabled(): boolean {
    return !(
      this.selectedFatigability &&
      this.selectedEndurance &&
      this.selectedAmbulation &&
      this.selectedWeightLoss &&
      this.selectedCalificacion &&
      this.selectedComorbidityCount
    );
  }


  calculateTotal(): number {
    let fatibilityScore = (this.selectedFatigability === 'A' || this.selectedFatigability === 'B') ? 1 : 0;
    let endurance = (this.selectedEndurance === 'A1') ? 1 : 0;
    let ambulation = (this.selectedAmbulation === 'A2') ? 1 : 0;
    let comorbidityCount = (this.selectedComorbidityCount === 'A3') ? 1 : 0;
    let weightloss = (this.selectedWeightLoss === 'A4') ? 1 : 0;

    let totalScore = fatibilityScore + endurance + ambulation + weightloss + comorbidityCount;

    if (totalScore >= 3) {
      this.selectedCalificacion = 'B5';
      totalScore = 50;
    } else if(totalScore >= 0 && totalScore <= 2) {
      this.selectedCalificacion = 'A5';
      totalScore = 49;
    }

    return totalScore;
  }
  
  mappingFatibility() {
    const fatibilityMap = {
      'A': 42,
      'B': 43,
      'C': 44,
      'D': 45,
      'E': 46,
    };

    return fatibilityMap[this.selectedFatigability] || undefined;
  }

  mappingEndurance() {
    const enduranceMap = {
      'A1': 20,
      'B1': 19,
    };

    return enduranceMap[this.selectedEndurance] || undefined;
  }

  mappingAmbulation() {
    const ambulationMap = {
      'A2' : 20,
      'B2' : 19,
    };

    return ambulationMap[this.selectedAmbulation] || undefined;
  }

  mappingWeightloss() {
    const weightlossMap = {
      'A4' : 47,
      'B4' : 48,
    };

    return weightlossMap[this.selectedWeightLoss] || undefined;
  }


  mappingComorbidity() {
    const comorbidityMap = {
      'A3' : 20,
      'B3' : 19,
    };

    return comorbidityMap[this.selectedComorbidityCount] || undefined;
  }


  construirDatos() {
    const totalScore = this.calculateTotal();
    const datos = {
      "questionnaire": [  
        {
          questionId: 60,
          answerId: this.mappingFatibility(),
          value: "",
        },
        {
          questionId: 62,
          answerId: this.mappingEndurance(),
          value: "",
        },
        {
          questionId: 64,
          answerId: this.mappingAmbulation(),
          value: "",
        },
        {
          questionId: 66,
          answerId: this.mappingComorbidity(),
          value: "",
        },
        {
          questionId: 68,
          answerId: this.mappingWeightloss(),
          value: "",
        },
        {
          questionId: 70,
          answerId: totalScore,
          value: "",
        },
      ],
    };
    return datos;
  }

  
  submitForm(): void {
    this.submitted = true;
  
    Swal.fire({
      icon: 'question',
      iconColor: '#2687c5',
      title: '¿Está seguro de enviar el formulario?',
      text: 'Por favor, revise las opciones marcadas antes de presionar Enviar',
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
    this.frailService.createFrail(this.patientId, this.construirDatos()).subscribe();
  }

}
