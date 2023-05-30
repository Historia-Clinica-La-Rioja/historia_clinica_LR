import { Component, OnInit } from '@angular/core';

export interface PeriodicElement {
  Nombre: string;
  Orden: number;
  Sexo: string;
}
const ELEMENT_DATA: PeriodicElement[] = [
  { Orden: 1, Nombre: 'Peso, longitud y perimetro cefalico', Sexo: 'NIÑAS' },
  { Orden: 2, Nombre: 'Peso, longitud y perimetro cefalico', Sexo: 'NIÑOS' },
  { Orden: 3, Nombre: 'Peso 0 a 6 años', Sexo: 'NIÑAS' },
  { Orden: 4, Nombre: 'Peso 0 a 6 años', Sexo: 'NIÑOS' },
  { Orden: 5, Nombre: 'Longitud Corporal-Talla 0 a 6 años', Sexo: 'NIÑAS' },
  { Orden: 6, Nombre: 'Longitud Corporal-Talla 0 a 6 años', Sexo: 'NIÑOS' },
  { Orden: 7, Nombre: 'Peso nacimiento-19 años', Sexo: 'NIÑAS' },
  { Orden: 8, Nombre: 'Peso nacimiento-19 años', Sexo: 'NIÑOS' },
  { Orden: 9, Nombre: 'Estatura nacimiento-19 años', Sexo: 'NIÑAS' },
  { Orden: 10, Nombre: 'Estatura nacimiento-19 años', Sexo: 'NIÑOS' },
  { Orden: 11, Nombre: 'Perimetro Cefálico nacimiento-5años', Sexo: 'NIÑAS' },
  { Orden: 12, Nombre: 'Perimetro Cefálico nacimiento-5años', Sexo: 'NIÑOS' },
  { Orden: 13, Nombre: 'IMC Nacimiento-5 años (Percentilos)', Sexo: 'NIÑAS' },
  { Orden: 14, Nombre: 'IMC Nacimiento-5 años (Percentilos)', Sexo: 'NIÑOS' },
  { Orden: 15, Nombre: 'IMC 5 - 19 años (Percentilos)', Sexo: 'NIÑAS' },
  { Orden: 16, Nombre: 'IMC 5 - 19 años (Percentilos)', Sexo: 'NIÑOS' },
  { Orden: 17, Nombre: 'SIND de DOWN - Peso y longitud 0-36 meses/Peso y longitud 2-18 años', Sexo: 'NIÑAS' },
  { Orden: 18, Nombre: 'SIND de DOWN - Peso y longitud 0-36 meses/Peso y longitud 2-18 años', Sexo: 'NIÑOS' },
  { Orden: 19, Nombre: 'SIND de TURNER - Talla 0-20 años', Sexo: 'NIÑAS' },
  { Orden: 20, Nombre: 'ACONDROPLASIA - Peso 0-17 años/Talla 0-18 años/Perimetro Cefálico', Sexo: 'NIÑAS' },
  { Orden: 21, Nombre: 'ACONDROPLASIA - Peso 0-17 años/Talla 0-18 años/Perimetro Cefálico', Sexo: 'NIÑOS' },
  
];

@Component({
  selector: 'app-tablas-percentilos',
  templateUrl: './tablas-percentilos.component.html',
  styleUrls: ['./tablas-percentilos.component.scss']
})
export class TablasPercentilosComponent implements OnInit {

  displayedColumns: string[] = ['Orden', 'Nombre', 'Sexo'];
  columnsToDisplay: string[] = this.displayedColumns.slice();
  data: PeriodicElement[] = ELEMENT_DATA;
  panelOpenState: number | null = null; // Propiedad para controlar el estado de expansión del panel


  ngOnInit() {}

}