import { Component, OnInit, Input } from '@angular/core';
import { EstudiosPopupComponent } from './estudios-popup/estudios-popup.component';
import { MatDialog } from '@angular/material/dialog';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { EdMontonAnswers } from '@api-rest/api-model';
import { EdMontonSummary } from '@api-rest/api-model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-adulto-mayor',
  templateUrl: './adulto-mayor.component.html',
  styleUrls: ['./adulto-mayor.component.scss']
})

export class AdultoMayorComponent implements OnInit {
  diccionarioRespuestas: any = {
		19: "Sí",
		20: "No",
		1: "Sin errores",
		2: "Errores minimos de espaciado",
		3: "Otros Errores",
		4: "0",
		5: "1-2", 
		6: "Mayor o igual a 3",
		7: "Excelente", 
		8: "Razonable", 
		9: "Mala", 
		10: "0 a 10 segundos",
		11: "11 a 20 segundos",
		12: "Más de 20 segundos", 
		13: "0 - 1", 
		14: "2 - 3",
		15: ">= 4", 
		16: "Siempre",
		17: "A veces",
		18: "Nunca",

		
	  };
	  
	  diccionarioPreguntas: any = {
		18: "ANIMO: ¿Se siente con frecuencia triste o deprimido? ",
		2: "COGNITIVO: Por favor imagina que este círculo pre-dibujado es un reloj. Me gustaría que pusiera losnúmeros en las posiciones correctas y luego poner las manilas o manecillas para indicar lahora “Las once con diez minutos",
		4: "ESTADO DE SALUD GENERAL: En el último año, ¿Cuántas veces ha estado hospitalizado?",
		5: "En general: ¿cómo describiría su salud?",
		9: "Independencia funcional: ¿Con cuántas de las siguientes actividades necesita ayuda? preparar la comida, comparas, transporte, comunicación telefónica, cuidado del hogar, lavado de ropa, manejo de dinero, tomar medicamentos",
		7: "Falta en el formulario",
		11: "Cuando Ud. necesita ayuda: ¿puede contar con alguien que esté dispuesto y disponible para atender sus necesidades o problemas?",


	};


	getEdMontonService: EdmontonService
  edmontonAnswers: EdMontonAnswers[] = [];
	edmontonSummary: EdMontonSummary;
  patientId: number;
  
  constructor(
    private dialog: MatDialog,
    private readonly route: ActivatedRoute,
    private edmontonService: EdmontonService

    ) { 
      this.getEdMontonService = edmontonService;
    }

  ngOnInit(): void {
    this.getEdmontonAnswers();
		//this.getEdmontonSummary();
  }

  showPopupFlag = false;

  mostrarPopup(): void {
    const dialogRef = this.dialog.open(EstudiosPopupComponent, {
      width: '550px', 
    });

    dialogRef.afterClosed().subscribe(result => {
    });
}

getEdmontonAnswers(): void {
  const institutionId = 2
  this.route.paramMap.subscribe(params => {
    this.patientId = Number(params.get('idPaciente'));
    
  })
  //api que trae el id de las respuestas y preguntas
  console.log("Paciente", this.patientId)
  console.log("institucion: ", institutionId)
  this.getEdMontonService.getEdMonton(institutionId, this.patientId)
    .subscribe(
    (response: EdMontonAnswers[]) => {
      this.edmontonAnswers = response;
      console.log(this.edmontonAnswers);
    },
    (error: any) => {
      console.error(error);
    }
    );
}


}
