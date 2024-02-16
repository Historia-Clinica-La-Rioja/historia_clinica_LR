import { Inject,Component, ChangeDetectionStrategy} from '@angular/core';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import Swal from 'sweetalert2';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CreateQuestionnaireDTO, QuestionnaireAnswerDTO } from '@api-rest/api-model';
import { MatDialog } from '@angular/material/dialog';

@Component({
selector: 'app-edmonton',
templateUrl: './edmonton.component.html',
styleUrls: ['./edmonton.component.scss'],
changeDetection: ChangeDetectionStrategy.OnPush
})
export class EdmontonComponent {
// private readonly routePrefix;
totalSum: number;
selectedCognitiveOption: number;
selectedHealthStatusOption: number;
selectedHealthStatusOptionDos: number;
selectedFunctionIndOption: number;
selectedSupportOption: number;
selectedSupportSocOption: number;
selectedMedicationOption: number;
selectedMedicationOptionDos: number;
selectedNutritionOption: number;
selectedAnimoOption: number;
selectedContingenciaOption: number;
selectedRendimientoFuncOption: number; 
selectedCalificacionFuncOption: number;
// @Input() patientId: number;
sumaAcumulada: number; 
selectedValues: number[] = [];
cumulativeSum: number = 0;
// patientData: BasicPatientDto | undefined;
datos: any;
selectedCalificacion: string = '';
patientId: number;
routePrefix: number;
submitted: boolean = false;

constructor(
private edmontonService: EdmontonService,
@Inject(MAT_DIALOG_DATA) public data: any,
private dialog: MatDialog 

){
this.patientId = data.patientId;
}

onOptionSelected(questionIndex: number, value: number): void {
this.selectedValues[questionIndex] = value;
this.calcularSuma();
}
calcularSuma(): void {
const valueToSumMapping = {};
for (let i = 1; i <= 20; i++) {
if (i % 3 === 1) {
valueToSumMapping[i] = 0;
} else if (i % 3 === 2) {
valueToSumMapping[i] = 1;
} else {
valueToSumMapping[i] = 2;
}
}
this.sumaAcumulada = this.selectedValues.reduce((acc, value) => {
const mappedSum = valueToSumMapping[value] || 0; // Usar el mapeo
return acc + mappedSum;
}, 0);
if (this.sumaAcumulada >= 11) {
this.selectedCalificacion = 'A33';
this.sumaAcumulada = 25;
} else if (this.sumaAcumulada >= 9) {
this.selectedCalificacion = 'A32';
this.sumaAcumulada = 24;
} else if (this.sumaAcumulada >= 7) {
this.selectedCalificacion = 'A31';
this.sumaAcumulada = 23;
} else if (this.sumaAcumulada >= 5) {
this.selectedCalificacion = 'A30';
this.sumaAcumulada = 22;
} else {
this.selectedCalificacion = 'A29'
this.sumaAcumulada = 21;
}
}
sharedLyric(optionId: number): string {
switch (optionId) {
case 1:
return "A1";
case 2:
return "A2";
case 3:
return "A3";
case 4: 
return "A4";
case 5: 
return "A5";
case 6:
return "A6";
case 7:
return "A7";
case 8:
return "A8";
case 9:
return "A9";
case 10:
return "A10";
case 11:
return "A11";
case 12: 
return "A12";
case 13:
return "A13";
case 14:
return "A14";
case 15:
return "A15";
case 16: 
return "A16";
case 17: 
return "A17";
case 18:
return "A18"
default:
return ""; 
}
}
value_1(itemId:number, optionId:number ): string {
if (itemId === 13 && optionId === 19) {
return "A19";
} else if (itemId === 14 && optionId === 19) {
return "A21";
} else if (itemId === 16 && optionId === 19) {
return "A23";
} else if (itemId === 18 && optionId === 19) {
return "A25";
} else if (itemId === 20 && optionId === 19) {
return "A27";
} else {
if(itemId === 13 && optionId === 20 ){
return "A20";
}else if (itemId === 14 && optionId === 20) {
return "A22";
}else if (itemId === 16 && optionId === 20) {
return "A24";
}else if (itemId === 18 && optionId === 20) {
return "A26";
}else if (itemId === 20 && optionId === 20) {
return "A28";
}
}
}

construirDatos(): CreateQuestionnaireDTO {
this.calcularSuma();
const questionnaireAnswers: QuestionnaireAnswerDTO[] = [
{
itemId: 2,
optionId: this.selectedCognitiveOption,
value: this.sharedLyric(this.selectedCognitiveOption) 
},
{
itemId: 4, 
optionId: this.selectedHealthStatusOption,
value: this.sharedLyric(this.selectedHealthStatusOption)
},
{
itemId: 5, 
optionId: this.selectedHealthStatusOptionDos,
value: this.sharedLyric(this.selectedHealthStatusOptionDos)
},
{
itemId: 7, 
optionId: this.selectedFunctionIndOption,
value: this.sharedLyric(this.selectedFunctionIndOption)
},
{
itemId: 9, 
optionId: this.selectedSupportSocOption,
value: this.sharedLyric(this.selectedSupportSocOption)
},
{
itemId: 11, 
optionId: this.selectedRendimientoFuncOption,
value: this.sharedLyric(this.selectedRendimientoFuncOption)
},
{
itemId: 13, 
optionId: this.selectedMedicationOption,
value: ''
},
{
itemId: 14, 
optionId: this.selectedMedicationOptionDos,
value: ''
},
{
itemId: 16, 
optionId: this.selectedNutritionOption,
value: ''
},
{
itemId: 18, 
optionId: this.selectedAnimoOption,
value: ''
},
{
itemId: 20, 
optionId: this.selectedContingenciaOption,
value: '' 
},
{
itemId: 22, 
optionId: this.sumaAcumulada,
value: this.selectedCalificacion
},
];
const questionnaireData: CreateQuestionnaireDTO = {
questionnaireId: 1,
answers: questionnaireAnswers
};
return questionnaireData;
}

onSubmit(): void {
this.submitted = true;
if (
this.selectedCognitiveOption === 0 ,
this.selectedHealthStatusOption ===0,
this.selectedHealthStatusOptionDos === 0,
this.selectedFunctionIndOption === 0,
this.selectedSupportSocOption === 0,
this.selectedRendimientoFuncOption === 0,
this.selectedMedicationOption === 0,
this.selectedMedicationOptionDos === 0,
this.selectedNutritionOption === 0 ,
this.selectedAnimoOption ===0,
this.selectedContingenciaOption===0
) {
alert('Por favor, complete todas las selecciones requeridas.');
return; 
}
Swal.fire({
icon: 'question',
iconColor: '#2687c5',
title: '¿Está seguro de enviar el formulario?',
text: 'Por favor, revise las opciones marcadas antes de presionar Enviar',
showDenyButton: true,
showCancelButton: true,
confirmButtonText: 'Enviar',
confirmButtonColor: '#2687c5',
denyButtonText: 'No enviar',
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
}, 2000);
},
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
const datos: CreateQuestionnaireDTO = this.construirDatos();
this.edmontonService.createEdmonton(this.patientId, datos).subscribe();
this.dialog.closeAll();
}
}

