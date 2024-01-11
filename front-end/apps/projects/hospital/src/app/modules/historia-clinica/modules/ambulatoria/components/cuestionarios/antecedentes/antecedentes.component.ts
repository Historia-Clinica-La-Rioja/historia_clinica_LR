import { Component, OnInit } from '@angular/core';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FamilyRecordService } from '@historia-clinica/modules/ambulatoria/services/antecedentes-familiares.service';
//import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CreateQuestionnaireDTO, QuestionnaireAnswerDTO } from '@api-rest/api-model';
import Swal from 'sweetalert2';

interface Pregunta {
  texto: string;
  respuesta: number;
  amarillo: boolean;
  value: number;
}

interface PreguntaNivelDeInstruccion {
  texto: string;
  respuestas: number[];
  amarillo: boolean;
  value: number;
}

interface PreguntaVivienda{
  texto: string;
  respuestas: number[];
  amarillo: boolean;
  value: number;

}

@Component({
  selector: 'app-antecedentes',
  templateUrl: './antecedentes.component.html',
  styleUrls: ['./antecedentes.component.scss']
})
export class AntecedentesComponent implements OnInit {
  otrosFamiliares: string = '';
  institucion: string = '';
  estadoCivilPadre: string = '';
  estadoCivilMadre: string = '';
  edadPadre: number;
  edadMadre: number;
  trabajoRemuneradoPadre: string = '';
  horasFueraDeLaCasaPadre: number;
  trabajoRemuneradoMadre: string = '';
  horasFueraDeLaCasaMadre: number;
  hermanosVivos: number;
  hermanosMuertos: number;
  numeroDeCuartos: number;
  energiaElectrica: string = '';
  patientId: number;

  estadosCivilesMadre: string[] = ['Casada', 'Soltera', 'Unión estable', 'Otros'];
  estadosCivilesPadre: string[] = ['Casado', 'Soltero', 'Unión estable', 'Otros'];

  preguntasFamilia: Pregunta[] = [
    { texto: 'Madre', respuesta: 0, value: 1, amarillo: true},
    { texto: 'Padre', respuesta: 0, value: 2, amarillo: true},
    { texto: 'Madrastra', respuesta: 0, value: 3,  amarillo: true},
    { texto: 'Padrastro', respuesta: 0, value: 4, amarillo: true},
    { texto: 'Hermanos', respuesta: 0,  value: 5, amarillo: false},
    { texto: 'Pareja', respuesta: 0,  value: 6, amarillo: false},
    { texto: 'Hijos', respuesta: 0, value: 7, amarillo: true},
    { texto: 'Abuelos', respuesta: 0, value: 8, amarillo: true},
    { texto: 'Otros', respuesta: 0, value: 9, amarillo: true}
  ];

  preguntasVive: Pregunta[] = [
    { texto: 'En la institución', respuesta: 0, value: 10,  amarillo: true},
    { texto: 'En la calle', respuesta: 0, value: 11, amarillo: true},
    { texto: 'Solo', respuesta: 0, value: 12, amarillo: true},
    { texto: 'Comparte la cama', respuesta: 0, value: 13, amarillo: true}
  ];

  preguntasVivienda: PreguntaVivienda[] = [
    { texto: 'Conectado a la red', respuestas: [0, 0], value: 14, amarillo: false},
    { texto: 'No conectado a la red', respuestas: [0, 0], value: 15, amarillo: true},
    { texto: 'Fuera del hogar', respuestas: [0, 0], value: 16,  amarillo: true},
    { texto: 'Letrina', respuestas: [0, 0], value: 17,  amarillo: true},
  ];

  preguntasNivelDeInstruccion: PreguntaNivelDeInstruccion[] = [
    { texto: 'Analfabeto', respuestas: [0, 0], value: 14, amarillo: true},
    { texto: 'Primario incompleto', respuestas: [0, 0], value: 15, amarillo: true},
    { texto: 'Primario', respuestas: [0, 0], value: 16, amarillo: true},
    { texto: 'Secundario incompleto', respuestas: [0, 0], value: 17,  amarillo: true},
    { texto: 'Secundario', respuestas: [0, 0], value: 18, amarillo: false},
    { texto: 'Universitario/Terciario', respuestas: [0, 0], value: 19,  amarillo: true},
  ];


  constructor(
    private antecedentesFamiliares: FamilyRecordService,
    //@Inject(MAT_DIALOG_DATA) public data: any
  ) { 
    //this.patientId = data.patientId;
  }



  ngOnInit(): void {
  }

  cambiarRespuesta(pregunta: Pregunta, respuesta: number) {
    pregunta.respuesta = respuesta;
  }

  getCheckboxClass(value: number): string {
    return (value === 5 || value === 6 || value === 14 ||value === 18 || value === 19) ? '' : 'checkbox-color';
  }

  cambiarRespuestaNivelDeInstruccion(pregunta: PreguntaNivelDeInstruccion, indice: number, respuesta: number) {
    // Verificar si ya hay una respuesta seleccionada en la misma columna
    this.preguntasNivelDeInstruccion.forEach((p) => {
      p.respuestas[indice] = 0;
    });
  
    // Establecer la respuesta seleccionada
    pregunta.respuestas[indice] = respuesta;
  }

  cambiarRespuestaVivienda(pregunta: PreguntaVivienda, indice: number, respuesta: number) {
    // Verificar si ya hay una respuesta seleccionada en la misma columna
    this.preguntasVivienda.forEach((p) => {
      p.respuestas[indice] = 0;
    });
  
    // Establecer la respuesta seleccionada
    pregunta.respuestas[indice] = respuesta;
  }

  viveConOtros(){
    if ((this.preguntasFamilia[8].respuesta == 2) || (this.preguntasFamilia[8].respuesta == 3))
      return 1;
  }

  viveEnInstitucion(){
    if (this.preguntasVive[0].respuesta == 2)
      return 1;
  }
  
  cambiarRespuestaPreguntaFamilia(i: number): number{
    switch(this.preguntasFamilia[i].respuesta){
      case 1:
        return 19;
      case 2:
        return 26;
      case 3:
        return 27;
    }
  }

  cambiarRespuestaPreguntaVive(i:number): number{
    switch(this.preguntasVive[i].respuesta){
      case 1:
        return 19;
      case 2:
        return 20;
    }
  }

  cambiarRespuestaTrabajoRemuneradoMadre(): number{
    switch(this.trabajoRemuneradoMadre){
      case "1":
        return 20;
      case "2":
        return 19
    }
  }

  cambiarEstadoCivilMadre():number{
    console.log("Estado civil madre: ", this.estadoCivilMadre);
    switch(this.estadoCivilMadre){
      case 'Soltera':
        return 34;
      case 'Casada':
        return 35;
      case 'Unión estable':
        return 36;
      case 'Otros':
        return 37;
    }
  }

  cambiarRespuestaTrabajoRemuneradoPadre(): number{
    switch(this.trabajoRemuneradoPadre){
      case "1":
        return 20;
      case "2":
        return 19
    }
  }

  cambiarEstadoCivilPadre():number{
    switch(this.estadoCivilPadre){
      case 'Soltero':
        return 34;
      case 'Casado':
        return 35;
      case 'Unión estable':
        return 36;
      case 'Otros':
        return 37;
    }
  }

  cambiarEnergiaElectrica():number{
    switch(this.energiaElectrica){
      case "1":
        return 20;
      case "2":
        return 19;
    }
  }

  RespuestaNivelDeInstruccionMadre():number{
    for (var i = 0; i <= 5; i++) {
      if (this.preguntasNivelDeInstruccion[i].respuestas[1] == 1){
        break;
      }
    }
    switch(i){
      case 0:
        return 28;
      case 1:
        return 29;
      case 2:
        return 30;
      case 3:
        return 31;
      case 4:
        return 32;
      case 5:
        return 33;
    }
  }

  RespuestaNivelDeInstruccionPadre():number{
    for (var l = 0; l <= 5; l++) {
      if (this.preguntasNivelDeInstruccion[l].respuestas[0] == 1){
        break;
      }
    }
    switch(l){
      case 0:
        return 28;
      case 1:
        return 29;
      case 2:
        return 30;
      case 3:
        return 31;
      case 4:
        return 32;
      case 5:
        return 33;
    }
  }

  RespuestasAgua(): number{
    for (var j = 0; j <= 2; j++) {
      console.log("Agua:", j, this.preguntasVivienda[j].respuestas[0]);
      if (this.preguntasVivienda[j].respuestas[0] == 1){
        break;
      }
    }
    console.log("Respuesta agua", j);
    switch(j){
      case 0:
        return 38;
      case 1:
        return 39;
      case 2:
        return 40;
    }
  }

  RespuestasExcretas(): number{
    for (var k = 0; k <= 3; k++) {
      if (this.preguntasVivienda[k].respuestas[1] == 1){
        break;
      }
    }
    switch(k){
      case 0:
        return 38;
      case 1:
        return 39;
      case 2:
        return 40;
      case 3:
        return 41;
    }
  }

  crearCuestionario(): CreateQuestionnaireDTO{
      const questionnaireAnswers: QuestionnaireAnswerDTO[] = [
        //Preguntas convive con
        { questionId: 24, answerId: this.cambiarRespuestaPreguntaFamilia(0), value: "1"},
        { questionId: 25, answerId: this.cambiarRespuestaPreguntaFamilia(1), value: "1"},
        { questionId: 26, answerId: this.cambiarRespuestaPreguntaFamilia(2), value: "1"},
        { questionId: 27, answerId: this.cambiarRespuestaPreguntaFamilia(3), value: "1"},
        { questionId: 28, answerId: this.cambiarRespuestaPreguntaFamilia(4), value: "1"},
        { questionId: 29, answerId: this.cambiarRespuestaPreguntaFamilia(5), value: "1"},
        { questionId: 54, answerId: this.cambiarRespuestaPreguntaFamilia(6), value: "1"},
        { questionId: 55, answerId: this.cambiarRespuestaPreguntaFamilia(7), value: "1"},
        { questionId: 30, answerId: this.cambiarRespuestaPreguntaFamilia(8), value: "1"},
        //Preguntas vive en
        { questionId: 32, answerId: this.cambiarRespuestaPreguntaVive(0), value: "1"},
        { questionId: 33, answerId: this.cambiarRespuestaPreguntaVive(1), value: "1"},
        { questionId: 34, answerId: this.cambiarRespuestaPreguntaVive(2), value: "1"},
        { questionId: 35, answerId: this.cambiarRespuestaPreguntaVive(3), value: "1"},
        //Preguntas nivel de instrucción madre
        { questionId: 37, answerId: this.RespuestaNivelDeInstruccionMadre(), value: "1"},
        //Preguntas nivel de instrucción padre
        { questionId: 38, answerId: this.RespuestaNivelDeInstruccionPadre(), value: "1"},
        //Preguntas Madre
        { questionId: 40, answerId: this.cambiarRespuestaTrabajoRemuneradoMadre(), value: "1"},
        { questionId: 41, answerId: 0, value: this.edadMadre.toString()}, //¿Es la edad o la cantidad de hs fuera?
        { questionId: 42, answerId: 0, value: this.horasFueraDeLaCasaMadre.toString()},
        { questionId: 43, answerId: this.cambiarEstadoCivilMadre(), value: "1"},
        //Preguntas Padre
        { questionId: 58, answerId: this.cambiarRespuestaTrabajoRemuneradoPadre(), value: "1"},
        { questionId: 57, answerId: 0, value: this.edadPadre.toString()}, //¿Es la edad o la cantidad de hs fuera?
        { questionId: 45, answerId: 0, value: this.horasFueraDeLaCasaPadre.toString()},
        { questionId: 56, answerId: this.cambiarEstadoCivilPadre(), value: "1"},
        //Hermanos
        { questionId: 47, answerId: 0, value: this.hermanosVivos.toString()},
        { questionId: 48, answerId: 0, value: this.hermanosMuertos.toString()},
        //Vivienda
        { questionId: 50, answerId: 0, value: this.numeroDeCuartos.toString()},
        { questionId: 51, answerId: this.cambiarEnergiaElectrica(), value: "1"},
        //Agua
        { questionId: 52, answerId: this.RespuestasAgua(), value: "1"},
        //Excretas
        { questionId: 53, answerId: this.RespuestasExcretas(), value: "1"},
      ]
    
      const questionnaireData: CreateQuestionnaireDTO = {
        questionnaire: questionnaireAnswers
      };

      return questionnaireData;
  }

  enviarDatos(){
    const data: CreateQuestionnaireDTO = this.crearCuestionario();
    console.log("Datos a enviar:", data);
    this.antecedentesFamiliares.enviarFamilyRecord(1, data).subscribe(
      response => {
        console.log('Éxito:', response);
        Swal.fire({
          title: "¡Formulario enviado con éxito!",
          text: "Cuestionario guardado correctamente.",
          icon: "success"
        });
      },
      error => {
        console.error('Error:', error);
        Swal.fire({
          icon: "error",
          title: "Error",
          text: "La información no pudo guardarse."
        });
      }
    );
  }

}

@NgModule({
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  // otras configuraciones del módulo
})
export class AntecedentesModule {
  // declaraciones y exportaciones del módulo
}


