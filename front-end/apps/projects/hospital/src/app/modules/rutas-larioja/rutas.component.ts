import { Component, OnInit } from '@angular/core';
 
@Component({
  selector: 'app-rutas',
  templateUrl: './rutas.component.html',
  styleUrls: ['./rutas.component.scss']
})
export class RutasComponent implements OnInit {

  Grafic: string;
  Nombre: string;
  Orden: number;
  Sexo: string;
  PanelAbierto?: boolean; // Propiedad para controlar el estado del panel


  constructor() { }

  ngOnInit(): void {
  }


 
  displayedColumns: string[] = ['Grafic', 'Orden', 'Nombre', 'Sexo'];
  columnsToDisplay: string[] = this.displayedColumns.slice();
  panelOpenState: number | null = null;

 
  togglePanel(orden: number): void {
    if (this.panelOpenState === orden) {
      this.panelOpenState = null; // Cierra el panel
    } else {
      this.panelOpenState = orden; // Abre el panel
    }
  }

   
}
