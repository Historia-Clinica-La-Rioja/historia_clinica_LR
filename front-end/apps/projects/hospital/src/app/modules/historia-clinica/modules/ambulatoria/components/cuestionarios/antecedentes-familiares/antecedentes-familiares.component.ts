import { Component } from '@angular/core';

interface Pregunta {
  texto: string;
  respuesta: number;
  value: number;
  amarillo: boolean;
}

@Component({
  selector: 'app-antecedentes-familiares',
  templateUrl: './antecedentes-familiares.component.html',
  styleUrls: ['./antecedentes-familiares.component.scss']
})
export class AntecedentesFamiliaresComponent {
  preguntasFamilia: Pregunta[] = [
    { texto: 'Madre', respuesta: 0, value: 1, amarillo: true },
    { texto: 'Padre', respuesta: 0, value: 2, amarillo: true },
    { texto: 'Madrastra', respuesta: 0, value: 3, amarillo: true },
    { texto: 'Padrastro', respuesta: 0, value: 4, amarillo: true },
    { texto: 'Hermanos', respuesta: 0, value: 5, amarillo: false },
    { texto: 'Pareja', respuesta: 0, value: 6, amarillo: false },
    { texto: 'Hijos', respuesta: 0, value: 7, amarillo: true },
    { texto: 'Abuelos', respuesta: 0, value: 8, amarillo: true },
    { texto: 'Otros', respuesta: 0, value: 9, amarillo: true }
  ];

  otrosFamiliares: string = ''; // Propiedad para almacenar los otros familiares

  cambiarRespuesta(pregunta: any, respuesta: number) {
    // Lógica para cambiar la respuesta de la pregunta
  }

  getCheckboxClass(value: any) {
    // Lógica para obtener la clase del checkbox según el valor
  }

  viveConOtros(): boolean {
    // Lógica para determinar si vive con otros familiares
    return true; // O false según tu lógica
  }
}
