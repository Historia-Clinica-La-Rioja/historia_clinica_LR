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
  { Grafic: IMAGE_ITEM + '2.jpeg', Orden: 1, Nombre: 'Peso, longitud y perimetro cefalico', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '26.jpeg', Orden: 2, Nombre: 'Peso, longitud y perimetro cefalico', Sexo: 'NIÑOS' },
  { Grafic: IMAGE_ITEM + '5.jpeg', Orden: 3, Nombre: 'Peso 0 a 6 años', Sexo: 'NIÑAS' },
  { Grafic: IMAGE_ITEM + '29.jpeg', Orden: 4, Nombre: 'Peso 0 a 6 años', Sexo: 'NIÑOS' },


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
