import { Component } from '@angular/core';
import { AntecedentesServices } from '@api-rest/services/antecedentes.service';


@Component({
  selector: 'app-background-family',
  templateUrl: './background-family.component.html',
  styleUrls: ['./background-family.component.scss']
})
export class BackgroundFamilyComponent {

  // edadMadre: number = undefined
  patientId: number;
  viveCon: any;
  educacionPadreOSustituto: "";
  educacionMadreOSustituto: "";
  trabajoMadre: any;
  horasFueraDeCasaMadre: any;
  edadPadre: any;
  estadoCivilPadre: any;
  trabajoPadre: any;
  horasFueraDeCasaPadre: any;
  hermanosVivos: any;
  hermanosMuertos: any;
  cantidadCuartos: any;
  energia: any;
  aguaSi: any;
  aguaNo: any;
  aguaFueraDelHogar: any;
  letrinaHogar: any;
  antecedentesData: any;
  counter1: number = undefined
  estadoCivilMadre = '';
 
  horasFueraDeCasa = { horas: 0 };

  trabajoRemunerado2 = {
    si: false,
    no: false
  };
  horasFueraDeCasa2 = { horas: 0 };

  // HERMANOS 
  hermanosEnVida = { vivo: 0, muertos: 0 };


  constructor(private antecedentesServices: AntecedentesServices) {

  }


  viveConOtros(): boolean {
    return true;
  }


  // FAMILIA 
  convivenciaMembers = {
    madre: {
      A: false,
      B: false,
      C: false
    },

    padre: {
      D: false,
      E: false,
      F: false
    },

    madrastra: {
      G: false,
      H: false,
      I: false
    },

    padrastro: {
      J: false,
      K: false,
      L: false
    },

    hermanos: {
      M: false,
      N: false,
      O: false
    },

    pareja: {
      P: false,
      D: false,
      R: false
    },

    hijos: {
      S: false,
      T: false,
      U: false
    },

    abuelos: {
      V: false,
      W: false,
      X: false
    },

    otros: {
      Y: false,
      Z: false,
      Z2: false
    },
  };


  // VIVE 
  hogarLugar = {

    institucion: {
      A2: false,
      B2: false
    },

    sinHogar: {
      C2: false,
      D2: false
    },

    personaSolitaria: {
      E2: false,
      F2: false
    },

    camaCompartida: {
      G2: false,
      H2: false
    }

  };


  // PADRES O SUSTITUTO

  educacion = {
    padre: {
      A3: false,
      B3: false,
      C3: false,
      D3: false,
      E3: false,
      F3: false
    },
    madre: {
      A4: false,
      B4: false,
      C4: false,
      D4: false,
      E4: false,
      F4: false
    }
  };

  // EDAD MADRE CONTADOR

  edadMadre = {
    madre: {
      counter1: undefined
    }
  };

  counterMadre() {
    return this.edadMadre.madre.counter1;
  }
////////////////////////////


// ESTADO CIVIL MADRE 

estadoCivil = {
  madre: { 
    E1: false,
    E2: false,
    E3: false,
    E4: false
  },

  padre: { 
    E5: false,
    E6: false,
    E7: false,
    E8: false
  }

  

  // SOLUCIONAR BUG QUE ENVIA LOS DATOS CORRECTAMENTE DE MANERA ASCENDENTE, EN CAMBIO REPITE EL PRIMER ID DE LA LISTA CUANDO
  // SE SELECCIONA DE MANERA DESCENDENTE
  // PASA LO MISMO PARA LAS SECCIONES DE PADRE COMO DE MADRE
 
};

trabajoRemunerado = { 
  madre: { 
    si: false, 
    no: false
  }
}

viviendaElectricidad = {

  electricidad: { 
    si: false, 
    no: false
  },
}

viviendaAgua = { 
  conexionAgua: {
    si: false,
    fueradelhogar: false
  },

  sinConexion: { 
    no: false,
   }
}
viviendaExcretas = { 
 
  conexionExcretas: {
    si: false,
    no: false,
    fueradelhogar: false,
    letrina: false
  },
}

optionIdConexionExcretas(conexionExcretas: any) {
  const mappingConexionAgua= {
    'si': 38, 
    'no': 39,
    'fueradelhogar': 40,
    'letrina': 41
  };
  
  const selectedOption = Object.keys(conexionExcretas).find(key => conexionExcretas[key]);
  return mappingConexionAgua[selectedOption] || undefined;

}
 
optionIdConexionAgua(conexionAgua: any, sinConexion: any) {
  const mappingConexionAgua = {
    'si': 38, 
    'no': 39,   
    'fueradelhogar': 40,
   };

   const selectedOptionAgua = Object.keys(conexionAgua).find(key => conexionAgua[key]);
  
   const selectedOptionSinConexion = Object.keys(sinConexion).find(key => sinConexion[key]);

  // Retorna el ID correspondiente
  return mappingConexionAgua[selectedOptionAgua] || mappingConexionAgua[selectedOptionSinConexion] || undefined;
}

optionIdViviendaElectricidad(electricidad: any) {
  const mappingElectricidad = {
    'si': 20, 
    'no': 19
  };
  
  const selectedOption = Object.keys(electricidad).find(key => electricidad[key]);
  return mappingElectricidad[selectedOption] || undefined;

}

  // TRABAJO REMUNERADO MAPPING

  optionIdTrabajoRemunerado(trabajo: any) {
    const mappingTrabajoRemunerado = {
      'si': 20, 
      'no': 19
    };
    
    const selectedOption = Object.keys(trabajo).find(key => trabajo[key]);
    return mappingTrabajoRemunerado[selectedOption] || undefined;

  }
  // ESTADO CIVIL MAPPING

  optionIdEstadoCivilMadre(estadocivil: any) {
    const mappingEstadoCivil = {
      'E1': 34,
      'E2': 35,
      'E3': 36,
      'E4': 37
    };
    
    const selectedOption = Object.keys(estadocivil).find(key => estadocivil[key]);
    return mappingEstadoCivil[selectedOption] || undefined;

  }

  optionIdEstadoCivilPadre(estadocivil2: any) {
    const mappingEstadoCivil2 = {
      'E5': 34,
      'E6': 35,
      'E7': 36,
      'E8': 37
    };
    
    const selectedOption = Object.keys(estadocivil2).find(key => estadocivil2[key]);
    return mappingEstadoCivil2[selectedOption] || undefined;

  }

  // FAMILIA MAPPING 

  optionIdFamilyMadre(member: any) {
    const mappingOptionFamily = {
      'A': 19,
      'B': 26,
      'C': 27
    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  optionIdFamilyPadre(member: any) {
    const mappingOptionFamily = {
      'D': 19,
      'E': 26,
      'F': 27,

    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  optionIdFamilyMadrastra(member: any) {
    const mappingOptionFamily = {
      'G': 19,
      'H': 26,
      'I': 27

    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  optionIdFamilyPadrastro(member: any) {
    const mappingOptionFamily = {
      'J': 19,
      'K': 26,
      'L': 27

    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  optionIdFamilyHermanos(member: any) {
    const mappingOptionFamily = {
      'M': 19,
      'N': 26,
      'O': 27

    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  optionIdFamilyPareja(member: any) {
    const mappingOptionFamily = {
      'P': 19,
      'D': 26,
      'R': 27

    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  optionIdFamilyHijos(member: any) {
    const mappingOptionFamily = {
      'S': 19,
      'T': 26,
      'U': 27

    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  optionIdFamilyAbuelos(member: any) {
    const mappingOptionFamily = {
      'V': 19,
      'W': 26,
      'X': 27

    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  optionIdFamilyOtros(member: any) {
    const mappingOptionFamily = {
      'Y': 19,
      'Z': 26,
      'Z2': 27

    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  // VIVE MAPPING

  OptionHogar(hogar: any) {
    const mappingInstitucion = {
      'A2': 19,
      'B2': 20
    };

    const selectedOption = Object.keys(hogar).find(key => hogar[key]);
    return mappingInstitucion[selectedOption] || undefined;
  }

  OptionSinHogar(hogar: any) {
    const mappingEnLaCalle = {
      'C2': 19,
      'D2': 20
    };

    const selectedOption = Object.keys(hogar).find(key => hogar[key]);
    return mappingEnLaCalle[selectedOption] || undefined;
  }

  OptionSolo(hogar: any) {
    const mappingSolo = {
      'E2': 19,
      'F2': 20
    };

    const selectedOption = Object.keys(hogar).find(key => hogar[key]);
    return mappingSolo[selectedOption] || undefined;
  }

  OptionCama(hogar: any) {
    const mappingCama = {
      'G2': 19,
      'H2': 20
    };

    const selectedOption = Object.keys(hogar).find(key => hogar[key]);
    return mappingCama[selectedOption] || undefined;
  }

  // PADRES/SUSTITUTO MAPPING 

  optionPadreAcargo(padre: any) {
    const mappingOptionFamilyPadre = {
      'A3': 28,
      'B3': 29,
      'C3': 30,
      'D3': 31,
      'E3': 32,
      'F3': 33
    };
    const selectedOption = Object.keys(padre).find(key => padre[key]);
    return mappingOptionFamilyPadre[selectedOption] || undefined;
  }

  optionMadreAcargo(madre: any) {
    const mappingOptionFamilyMadre = {
      'A4': 28,
      'B4': 29,
      'C4': 30,
      'D4': 31,
      'E4': 32,
      'F4': 33
    };
    const selectedOption = Object.keys(madre).find(key => madre[key]);
    return mappingOptionFamilyMadre[selectedOption] || undefined;
  }

  enviarFormulario(): void {
    const data = this.construirDatos();
    this.antecedentesServices.createAntecedecentes(this.patientId, data).subscribe();
    console.log(data, this.antecedentesServices, "data");
  }

  construirDatos() {
    const datos = {
      "questionnaireId": 2,
      "answers": [
        {
          "itemId": 24,
          "optionId": this.optionIdFamilyMadre(this.convivenciaMembers.madre),
          "value": "",
        },
        {
          "itemId": 25,
          "optionId": this.optionIdFamilyPadre(this.convivenciaMembers.padre),
          "value": ""
        },
        {
          "itemId": 26,
          "optionId": this.optionIdFamilyMadrastra(this.convivenciaMembers.madrastra),
          "value": ""
        },
        {
          "itemId": 27,
          "optionId": this.optionIdFamilyPadrastro(this.convivenciaMembers.padrastro),
          "value": ""
        },
        {
          "itemId": 28,
          "optionId": this.optionIdFamilyHermanos(this.convivenciaMembers.hermanos),
          "value": 1
        },
        {
          "itemId": 29,
          "optionId": this.optionIdFamilyPareja(this.convivenciaMembers.pareja),
          "value": 1
        },
        {
          "itemId": 54,
          "optionId": this.optionIdFamilyHijos(this.convivenciaMembers.hijos),
          "value": 1
        },
        {
          "itemId": 55,
          "optionId": this.optionIdFamilyAbuelos(this.convivenciaMembers.abuelos),
          "value": 1
        },
        {
          "itemId": 30,
          "optionId": this.optionIdFamilyOtros(this.convivenciaMembers.otros),
          "value": 1
        },
        {
          "itemId": 32,
          "optionId": this.OptionHogar(this.hogarLugar.institucion),
          "value": 1
        },
        {
          "itemId": 33,
          "optionId": this.OptionSinHogar(this.hogarLugar.sinHogar),
          "value": 1
        },
        {
          "itemId": 34,
          "optionId": this.OptionSolo(this.hogarLugar.personaSolitaria),
          "value": 1
        },
        {
          "itemId": 35,
          "optionId": this.OptionCama(this.hogarLugar.camaCompartida),
          "value": 1
        },
        {
          "itemId": 37,
          "optionId": this.optionPadreAcargo(this.educacion.padre),
          "value": 1
        },
        {
          "itemId": 38,
          "optionId": this.optionMadreAcargo(this.educacion.madre),
          "value": 1
        },
        {
          "itemId": 40,
          "optionId": this.optionIdTrabajoRemunerado(this.trabajoRemunerado.madre),
          "value": 1
        },

        // counter horas fuera de casa madre 
        {
          "itemId": 41,
          "optionId": 0,
          "value": 2
        },

        // counter edad madre
        {
          "itemId": 42,
          "optionId": 0,
          "value": this.counterMadre()
        },
        //
        {
          "itemId": 43,
          "optionId": this.optionIdEstadoCivilMadre(this.estadoCivil.madre),
          "value": 1
         },

       // counter edad padre

        {
          "itemId": 45,
          "optionId": 19,
          "value": 1
        },

        //
        {
          "itemId": 56,
          "optionId": this.optionIdEstadoCivilPadre(this.estadoCivil.padre),
          "value": 1
        },

        // counter horas fuera de casa padre 

        {
          "itemId": 57,
          "optionId": 47,
          "value": 5
        },
        //
        {
          "itemId": 46,
          "optionId": 47,
          "value": 1
        },
        {
          "itemId": 50,
          "optionId": 0,
          "value": 6
        },
        {
          "itemId": 51,
          "optionId": this.optionIdViviendaElectricidad(this.viviendaElectricidad.electricidad),
          "value": 7
        },
        {
          "itemId": 52,
          "optionId": this.optionIdConexionAgua(this.viviendaAgua.conexionAgua, this.viviendaAgua.sinConexion),
          "value": 8
        },
        {
          "itemId": 53,
          "optionId": this.optionIdConexionExcretas(this.viviendaExcretas.conexionExcretas),
          "value": 8
        },
      ]
    };
    return datos;
  }
}
