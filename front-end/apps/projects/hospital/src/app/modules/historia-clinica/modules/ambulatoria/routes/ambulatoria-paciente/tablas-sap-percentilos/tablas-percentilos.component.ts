import { Component, OnInit } from '@angular/core';

const IMAGE_ITEM = '../../../../../assets/tableSAP/'; 
console.log (IMAGE_ITEM);

export interface PeriodicElement {
  Grafic: string;
  Nombre: string;
  Orden: number;
  Sexo: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { Grafic: IMAGE_ITEM + '2.jpeg',   Orden: 1,  Nombre: 'Peso, longitud y perimetro cefalico', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '26.jpeg',  Orden: 2,  Nombre: 'Peso, longitud y perimetro cefalico', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '5.jpeg',   Orden: 3,  Nombre: 'Peso 0 a 6 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '29.jpeg',  Orden: 4,  Nombre: 'Peso 0 a 6 años', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '9.jpeg',   Orden: 5,  Nombre: 'Longitud Corporal- Talla 0 a 6 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '33.jpeg',  Orden: 6,  Nombre: 'Longitud Corporal- Talla 0 a 6 años', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '7.jpeg',   Orden: 7,  Nombre: 'Peso nacimiento - 19 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '31.jpeg',  Orden: 8,  Nombre: 'Peso nacimiento - 19 años', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '11.jpeg',  Orden: 9,  Nombre: 'Estatura nacimiento - 19 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '35.jpeg',  Orden: 10, Nombre: 'Estatura nacimiento - 19 años', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '13.jpeg',  Orden: 11, Nombre: 'Perimetro cefálico nacimiento - 5 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '37.jpeg',  Orden: 12, Nombre: 'Perimetro cefálico nacimiento - 5 años', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '19.jpeg',  Orden: 13, Nombre: 'IMC Nacimiento - 5 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '43.jpeg',  Orden: 14, Nombre: 'IMC Nacimiento - 5 años', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '20.jpeg',  Orden: 15, Nombre: 'IMC 5 - 19 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '44.jpeg',  Orden: 16, Nombre: 'IMC 5 - 19 años', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '49a.jpeg', Orden: 17, Nombre: 'SIND de DOWN - Peso y longitud - 0 a 36 meses', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '49b.jpeg', Orden: 18, Nombre: 'SIND de DOWN - Peso y longitud - 2 a 18 meses', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '49c.jpeg', Orden: 19, Nombre: 'SIND de DOWN - Peso y longitud - 0 a 36 meses', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '49d.jpeg', Orden: 20, Nombre: 'SIND de DOWN - Peso y longitud - 2 a 18 meses', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '50a.jpeg', Orden: 21, Nombre: 'SIND de TURNER - Talla 0 a 20 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '51a.jpeg', Orden: 22, Nombre: 'ACONDROPLASIA - Peso 0 a 17 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '51b.jpeg', Orden: 23, Nombre: 'ACONDROPLASIA - Talla 0 a 18 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '51c.jpeg', Orden: 24, Nombre: 'ACONDROPLASIA - Perimetro Cefálico', Sexo: 'NIÑAS' },   
  { Grafic: IMAGE_ITEM + '51d.jpeg', Orden: 25, Nombre: 'ACONDROPLASIA - Peso 0 a 17 años', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '51e.jpeg', Orden: 26, Nombre: 'ACONDROPLASIA - Talla 0 a 18 años', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '51f.jpeg', Orden: 27, Nombre: 'ACONDROPLASIA - Perimetro Cefálico', Sexo: 'NIÑOS' },
];

@Component({
  selector: 'app-tablas-percentilos',
  templateUrl: './tablas-percentilos.component.html',
  styleUrls: ['./tablas-percentilos.component.scss']
})
export class TablasPercentilosComponent implements OnInit {

  displayedColumns: string[] = ['Grafic', 'Orden', 'Nombre', 'Sexo'];
  
  getImagePath(fileName: string): string {
    return IMAGE_ITEM + fileName;
  }
  
  columnsToDisplay: string[] = this.displayedColumns.slice();
  data: PeriodicElement[] = ELEMENT_DATA;
  panelOpenState: number | null = null;

  ngOnInit() {}

}
