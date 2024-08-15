import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { AntecedentesServices } from '@api-rest/services/antecedentes.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-background-family',
  templateUrl: './background-family.component.html',
  styleUrls: ['./background-family.component.scss']
})
export class BackgroundFamilyComponent {

  patientId: number;
  viveCon: any;
  educacionPadreOSustituto: "";
  educacionMadreOSustituto: "";
  trabajoMadre: any;
  horasFueraDeCasaMadre: any;
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
  estadoCivilMadre = '';
  submitted: boolean;
 
  constructor(
    private antecedentesServices: AntecedentesServices, 
    private readonly route: ActivatedRoute,

    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
     this.route.paramMap.subscribe(params => {
     this.patientId = Number(params.get('idPaciente'));

    });
    // console.log('Patient ID:', this.patientId);  
  }

  
  isCheckboxAChecked() {
    return this.convivenciaMembers.madre.A;
  }

  isCheckboxBDisabled() {
    return this.isCheckboxAChecked();
  }

  isCheckboxCDisabled() {
    return this.isCheckboxAChecked();
  }
  isCheckboxDChecked() {
    return this.convivenciaMembers.padre.D;
  }

  isCheckboxEDisabled() {
    return this.isCheckboxDChecked();
  }

  isCheckboxFDisabled() {
    return this.isCheckboxDChecked();
  }
  isMadrastraCheckboxGChecked() {
    return this.convivenciaMembers.madrastra.G;
  }
  isMadrastraCheckboxHDisabled() {
    return this.isMadrastraCheckboxGChecked();
  }
  isMadrastraCheckboxIDisabled() {
    return this.isMadrastraCheckboxGChecked();
  }

  isPadrastroCheckboxJChecked() {
    return this.convivenciaMembers.padrastro.J;
  }
  isPadrastroCheckboxKDisabled() {
    return this.isPadrastroCheckboxJChecked();
  }
  isPadrastroCheckboxLDisabled() {
    return this.isPadrastroCheckboxJChecked();
  }

  isParejaCheckboxPChecked() {
    return this.convivenciaMembers.pareja.P;
  }
  isParejaCheckboxDDisabled() {
    return this.isParejaCheckboxPChecked();
  }
  isParejaCheckboxRDisabled() {
    return this.isParejaCheckboxPChecked();
  }

  isHijosCheckboxSChecked() {
    return this.convivenciaMembers.hijos.S;
  }
  isHijosCheckboxTDisabled() {
    return this.isHijosCheckboxSChecked();
  }
  isHijosCheckboxUDisabled() {
    return this.isHijosCheckboxSChecked();
  }

  isAbuelosCheckboxVChecked() {
    return this.convivenciaMembers.abuelos.V;
  }
  isAbuelosCheckboxWDisabled() {
    return this.isAbuelosCheckboxVChecked();
  }
  isAbuelosCheckboxXDisabled() {
    return this.isAbuelosCheckboxVChecked();
  }

  isOtrosCheckboxYChecked() {
    return this.convivenciaMembers.otros.Y;
  }
  isOtrosCheckboxZDisabled() {
    return this.isOtrosCheckboxYChecked();
  }
  isOtrosCheckboxZ2Disabled() {
    return this.isOtrosCheckboxYChecked();
  }
  isCheckboxMChecked() {
    return this.convivenciaMembers.hermanos.M;
  }
  isCheckboxNDisabled() {
    return this.isCheckboxMChecked();
  }
  isCheckboxODisabled() {
    return this.isCheckboxMChecked();
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
  };

  trabajoRemunerado = {
    madre: {
      si: false,
      no: false
    }
  }

  trabajoRemunerado2 = {
    padre: {
      si: false,
      no: false
    }
  };


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

  horasFueraDeCasa = {
    horasMadre: {
      H1: 0,
    },
    horasPadre: {
      H2: 0
    }

  }

  edadMadre = {
    madre: {
      counter1: 0
    },
  };

  edadPadre = {
    padre: {
      counter2: 0
    }
  };

  hermanosEnVida = {
    vivo: 0,

    ido: {
      desvivido: 0,
    }
  };

  numeroCuartos = {
    cuartos: {
      cantidad: 0
    }
  };

  optionIdCuartos(cuartos: any) {
    const mappingCantidadCuartos = {
      'cantidad': 0,
    };

    const selectedOption = mappingCantidadCuartos[cuartos.cantidad];
    return selectedOption || undefined;
  }

  optionIdHermanosidos(hermanosEnVida: any) {
    const mappingHermanosdesvividos = {
      'desvivido': 0,
    };

    const selectedOption = mappingHermanosdesvividos[hermanosEnVida.ido];
    return selectedOption || undefined;
  }
  optionIdHermanosVivos(hermanosEnVida: any) {
    const mappingHermanosVivos = {
      'vivo': 0,
    };

    const selectedOption = mappingHermanosVivos[hermanosEnVida.vivo];
    return selectedOption || undefined;
  }

  optionIdEdadPadre(edadPadre: any) {
    const mappingEdadPadre = {
      'counter2': 0,
    };

    const selectedOption = mappingEdadPadre[edadPadre.padre.counter2];
    return selectedOption || undefined;
  }

  optionIdEdadMadre(edadMadre: any) {
    const mappingEdadMadre = {
      'counter1': 0,
    };

    const selectedOption = mappingEdadMadre[edadMadre.madre.counter1];
    return selectedOption || undefined;
  }

  optionIdHorasPadre(horasFueraDeCasa: any) {
    const mappingHorasfueradeCasaDos = {
      'H2': 0,
    };

    const selectedOption = Object.keys(horasFueraDeCasa).find(key => horasFueraDeCasa[key]);
    return mappingHorasfueradeCasaDos[selectedOption] || undefined;
  }

  optionIdHorasMadre(horasFueraDeCasa: any) {
    const mappingHorasfueradeCasa = {
      'H1': 0,
    };

    const selectedOption = Object.keys(horasFueraDeCasa).find(key => horasFueraDeCasa[key]);
    return mappingHorasfueradeCasa[selectedOption] || undefined;
  }

  optionIdConexionExcretas(conexionExcretas: any) {
    const mappingConexionAgua = {
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

  optionIdTrabajoRemuneradoPadre(trabajo: any) {
    const mappingTrabajoRemuneradoPadre = {
      'si': 20,
      'no': 19
    };

    const selectedOption = Object.keys(trabajo).find(key => trabajo[key]);
    return mappingTrabajoRemuneradoPadre[selectedOption] || undefined;
  }


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
        {
          "itemId": 41,
          "optionId": this.optionIdHorasMadre(this.horasFueraDeCasa),
          "value": this.horasFueraDeCasa.horasMadre.H1

        },
        {
          "itemId": 42,
          "optionId": this.optionIdEdadMadre(this.edadMadre),
          "value": this.edadMadre.madre.counter1
        },
        {
          "itemId": 43,
          "optionId": this.optionIdEstadoCivilMadre(this.estadoCivil.madre),
          "value": 1
        },
        {
          "itemId": 45,
          "optionId": this.optionIdEdadPadre(this.edadPadre),
          "value": this.edadPadre.padre.counter2
        },
        {
          "itemId": 56,
          "optionId": this.optionIdEstadoCivilPadre(this.estadoCivil.padre),
          "value": 1
        },

        {
          "itemId": 57,
          "optionId": this.optionIdHorasPadre(this.horasFueraDeCasa.horasPadre),
          "value": this.horasFueraDeCasa.horasPadre.H2
        },
        {
          "itemId": 58,
          "optionId": this.optionIdTrabajoRemuneradoPadre(this.trabajoRemunerado2.padre),
          "value": 1
        },
        {
          "itemId": 47,
          "optionId": this.optionIdHermanosVivos(this.hermanosEnVida.vivo),
          "value": this.hermanosEnVida.vivo
        },
        {
          "itemId": 48,
          "optionId": this.optionIdHermanosidos(this.hermanosEnVida.ido),
          "value": this.hermanosEnVida.ido.desvivido
        },

        {
          "itemId": 50,
          "optionId": this.optionIdCuartos(this.numeroCuartos.cuartos),
          "value": this.numeroCuartos.cuartos.cantidad,
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
                title: 'Enviado exitosamente',
                confirmButtonColor: '#2687c5',
                confirmButtonText: 'Aceptar',
              }).then(() => {
                window.location.reload();
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
    const questionnaireData = this.construirDatos();
    this.antecedentesServices.createAntecedecentes(this.patientId, questionnaireData).subscribe();
    
  }
}
