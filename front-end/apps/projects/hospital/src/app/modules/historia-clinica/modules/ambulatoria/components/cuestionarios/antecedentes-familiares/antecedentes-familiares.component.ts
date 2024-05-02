import { Component } from '@angular/core';

interface Pregunta {
  texto: string;
  
}

@Component({
  selector: 'app-antecedentes-familiares',
  templateUrl: './antecedentes-familiares.component.html',
  styleUrls: ['./antecedentes-familiares.component.scss']
})
export class AntecedentesFamiliaresComponent {
  preguntasFamilia: Pregunta[] = [
    { texto: 'Madre'},
    { texto: 'Padre',  },
    { texto: 'Madrastra', },
    { texto: 'Padrastro', },
    { texto: 'Hermanos', },
    { texto: 'Pareja',  },
    { texto: 'Hijos',  },
    { texto: 'Abuelos',  },
    { texto: 'Otros',  }
  ];

  otrosFamiliares: string = ''; // Propiedad para almacenar los otros familiares

  cambiarRespuesta(pregunta: any, respuesta: number) {
    // Lógica para cambiar la respuesta de la pregunta
  }

  getCheckboxClass(value: any) {
    // Lógica para obtener la clase del checkbox según  valor
  }el

  viveConOtros(): boolean {
    // Lógica para determinar si vive con otros familiares
    return true; // O false según tu lógica
  }
}
