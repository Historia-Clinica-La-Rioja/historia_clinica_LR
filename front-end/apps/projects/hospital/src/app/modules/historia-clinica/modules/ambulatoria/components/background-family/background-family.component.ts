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
  }

  familyMembers = [
    { name: 'Madre', key: 'madre' },
    { name: 'Padre', key: 'padre' },
    { name: 'Madrastra', key: 'madrastra' },
    { name: 'Padrastro', key: 'padrastro' },
    { name: 'Hermanos', key: 'hermanos' },
    { name: 'Pareja', key: 'pareja' },
    { name: 'Hijos', key: 'hijos' },
    { name: 'Abuelos', key: 'abuelos' },
    { name: 'Otros', key: 'otros' }
  ];

  convivenciaMembers = {
    madre: { A: false, B: false, C: false },
    padre: { D: false, E: false, F: false },
    madrastra: { G: false, H: false, I: false },
    padrastro: { J: false, K: false, L: false },
    hermanos: { M: false, N: false, O: false },
    pareja: { P: false, D: false, R: false },
    hijos: { S: false, T: false, U: false },
    abuelos: { V: false, W: false, X: false },
    otros: { Y: false, Z: false, Z2: false }
    
  };
  
  onCheckboxChange(memberKey: string, checkboxValue: string) {

  }

  isCheckboxDisabled(memberKey: string, dependentCheckbox: string): boolean {
    return this.convivenciaMembers[memberKey][dependentCheckbox];
  }

  getCheckboxCBackground(memberKey: string): string {
    return this.convivenciaMembers[memberKey]?.A ? '#cccecf38' : '#fff11c';
  }

  onNoCheckboxChange(lugarKey: string): void {
 }

isSiCheckboxDisabled(lugarKey: string): boolean {
  return this.hogarLugar[lugarKey]?.[this.lugares.find(l => l.key === lugarKey)?.noKey];
}

getCheckboxSiStyles(lugarKey: string): {[key: string]: string} {
  if (this.isSiCheckboxDisabled(lugarKey)) {
    return {
      'background': '#cccecf38',
     };
  } else {
    return {
      'background-color': '#fff11c'
    };
  }
}

  // VIVE
  hogarLugar = {
    institucion: { A2: false, B2: false },
    sinHogar: { C2: false, D2: false },
    personaSolitaria: { E2: false, F2: false },
    camaCompartida: { G2: false, H2: false }
  };

  lugares = [
    { itemId: 32, label: 'En institución', key: 'institucion', noKey: 'A2', siKey: 'B2' },
    { itemId: 33, label: 'En la calle', key: 'sinHogar', noKey: 'C2', siKey: 'D2' },
    { itemId: 34, label: 'Solo', key: 'personaSolitaria', noKey: 'E2', siKey: 'F2' },
    { itemId: 35, label: 'Comparte la cama', key: 'camaCompartida', noKey: 'G2', siKey: 'H2' }
  ];

  optionMappingHogar = {
    A2: 19, B2: 20,
    C2: 19, D2: 20,
    E2: 19, F2: 20,
    G2: 19, H2: 20
  };

  // PADRES O SUSTITUTO
  educacion = {
    padre: { A3: false, B3: false, C3: false, D3: false, E3: false, F3: false },
    madre: { A4: false, B4: false, C4: false, D4: false, E4: false, F4: false }
  };

  opcionesPadre = ['A3', 'B3', 'C3', 'D3', 'E3', 'F3'];
  opcionesMadre = ['A4', 'B4', 'C4', 'D4', 'E4', 'F4'];

  optionMapping: { [key: string]: number } = {
    'A3': 28, 'B3': 29, 'C3': 30, 'D3': 31, 'E3': 32, 'F3': 33,
    'A4': 28, 'B4': 29, 'C4': 30, 'D4': 31, 'E4': 32, 'F4': 33
  };

  // ESTADO CIVIL  
  estadoCivil = {
    madre: { E1: false, E2: false, E3: false, E4: false },
    padre: { E5: false, E6: false, E7: false, E8: false }
  };

  estadoCivilOptions = [
    { key: 'E1', label: 'Soltera' },
    { key: 'E2', label: 'Casada' },
    { key: 'E3', label: 'Unión estable' },
    { key: 'E4', label: 'Otros' }
  ];
  

  // TRABAJO REMUNERADO
  trabajoRemunerado = { madre: { si: false, no: false } };
  trabajoRemunerado2 = { padre: { si: false, no: false } };

  // VIVIENDA
  viviendaElectricidad = { electricidad: { si: false, no: false } };

  viviendaAgua = {
    conexionAgua: { si: false, fueradelhogar: false },
    sinConexion: { no: false }
  };

  viviendaExcretas = {
    conexionExcretas: { si: false, no: false, fueradelhogar: false, letrina: false }
  };

  // HORAS FUERA DE CASA
  horasFueraDeCasa = { horasMadre: { H1: 0 }, horasPadre: { H2: 0 } };

  // EDAD
  edadMadre = { madre: { counter1: 0 } };
  edadPadre = { padre: { counter2: 0 } };

  // HERMANOS EN VIDA
  hermanosEnVida = { vivo: 0, ido: { desvivido: 0 } };

  // NÚMERO DE CUARTOS
  numeroCuartos = { cuartos: { cantidad: 0 } };


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

  optionIdEstadoCivil(estadocivil: any) {
    const mappingEstadoCivil = {
      'E1': 34,
      'E2': 35,
      'E3': 36,
      'E4': 37
    };
  
    const selectedOption = Object.keys(estadocivil).find(key => estadocivil[key]);
    return mappingEstadoCivil[selectedOption] || undefined;
  }
  // FAMILIA MAPPING 

  optionIdFamily(member: any) {
    const mappingOptionFamily = {
      'A': 19, 'B': 26, 'C': 27, 'D': 19, 'E': 26,
      'F': 27, 'G': 19, 'H': 26, 'I': 27, 'J': 19,
      'K': 26, 'L': 27, 'M': 19, 'N': 26, 'O': 27, 'P': 19, 'Q': 26, 'R': 27,
      'S': 19, 'T': 26, 'U': 27, 'V': 19, 'W': 26, 'X': 27, 'Y': 19, 'Z': 26, 'Z2': 27
    };
    const selectedOption = Object.keys(member).find(key => member[key]);
    return mappingOptionFamily[selectedOption] || undefined;
  }

  // VIVE MAPPING

  getOptionHogar(hogar: any): number | undefined {
  const selectedOption = Object.keys(hogar).find(key => hogar[key]);
  return this.optionMappingHogar[selectedOption] || undefined;
}

  // PADRES/SUSTITUTO MAPPING 

  getOptionEducacionPadres(familyMember: any, opciones: string[]): number | undefined {
    const selectedOption = opciones.find(option => familyMember[option]);
    return this.optionMapping[selectedOption] || undefined;
  }

  construirDatos() {
    const datos = {
      "questionnaireId": 2,
      "answers": [
        {
          "itemId": 24,
          "optionId": this.optionIdFamily(this.convivenciaMembers.madre),
          "value": "",
        },
        {
          "itemId": 25,
          "optionId": this.optionIdFamily(this.convivenciaMembers.padre),
          "value": ""
        },
        {
          "itemId": 26,
          "optionId": this.optionIdFamily(this.convivenciaMembers.madrastra),
          "value": ""
        },
        {
          "itemId": 27,
          "optionId": this.optionIdFamily(this.convivenciaMembers.padrastro),
          "value": ""
        },
        {
          "itemId": 28,
          "optionId": this.optionIdFamily(this.convivenciaMembers.hermanos),
          "value": 1
        },
        {
          "itemId": 29,
          "optionId": this.optionIdFamily(this.convivenciaMembers.pareja),
          "value": 1
        },
        {
          "itemId": 54,
          "optionId": this.optionIdFamily(this.convivenciaMembers.hijos),
          "value": 1
        },
        {
          "itemId": 55,
          "optionId": this.optionIdFamily(this.convivenciaMembers.abuelos),
          "value": 1
        },
        {
          "itemId": 30,
          "optionId": this.optionIdFamily(this.convivenciaMembers.otros),
          "value": 1
        },
        {
          "itemId": 32,
          "optionId": this.getOptionHogar(this.hogarLugar.institucion),
          "value": 1
        },
        {
          "itemId": 33,
          "optionId": this.getOptionHogar(this.hogarLugar.sinHogar),
          "value": 1
        },
        {
          "itemId": 34,
          "optionId": this.getOptionHogar(this.hogarLugar.personaSolitaria),
          "value": 1
        },
        {
          "itemId": 35,
          "optionId": this.getOptionHogar(this.hogarLugar.camaCompartida),
          "value": 1
        },
        {
          "itemId": 37,
          "optionId": this.getOptionEducacionPadres(this.educacion.padre, this.opcionesPadre),
          "value": 1
        },
        {
          "itemId": 38,
          "optionId": this.getOptionEducacionPadres(this.educacion.madre, this.opcionesMadre),
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
          "optionId": this.optionIdEstadoCivil(this.estadoCivil.madre),
          "value": 1
        },
        {
          "itemId": 45,
          "optionId": this.optionIdEdadPadre(this.edadPadre),
          "value": this.edadPadre.padre.counter2
        },
        {
          "itemId": 56,
          "optionId": this.optionIdEstadoCivil(this.estadoCivil.padre),
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
