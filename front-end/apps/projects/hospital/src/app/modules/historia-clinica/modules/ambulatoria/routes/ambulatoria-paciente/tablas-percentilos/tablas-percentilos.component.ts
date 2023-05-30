import { Component, OnInit } from '@angular/core';

const IMAGE_ITEM = '../../../../../assets/images/'; 
console.log (IMAGE_ITEM);

export interface PeriodicElement {
  Grafic: string;
  Nombre: string;
  Orden: number;
  Sexo: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { Grafic: IMAGE_ITEM + 'empty-profile.png', Orden: 1, Nombre: 'Peso, longitud y perimetro cefalico', Sexo: 'NIÑAS' },
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
