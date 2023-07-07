import { Component, OnInit, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { EdMontonAnswers } from '@api-rest/api-model';
import { EdMontonSummary } from '@api-rest/api-model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-get-test',
  templateUrl: './get-test.component.html',
  styleUrls: ['./get-test.component.scss']
})

export class GetTestComponent implements OnInit {
  @Input() patientId: number;
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
    13: "¿Usa 5 o más medicamentos en el día a día?",
    14: "¿En ocasiones, ¿se le olvida tomarse los medicamentos?",
    16: "Recientemente, ¿ha perdido peso como para que su ropa le quede suelta?",
    20: "¿Tiene algún problema con el control para orinar, es decir puede contener la orina si así lo desea? ",
	
	


	};
  getEdMontonService: EdmontonService
  edmontonAnswers: EdMontonAnswers[] = [];
	edmontonSummary: EdMontonSummary;

  
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



    getEdmontonAnswers(): void {
      const institutionId = 2
      
      //api que trae el id de las respuestas y preguntas
      console.log("Paciente", this.patientId)
      console.log("institucion: ", institutionId)
      console.log("estoy en get")
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
