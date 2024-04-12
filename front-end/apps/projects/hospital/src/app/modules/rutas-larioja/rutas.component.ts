import { Component, OnInit } from '@angular/core';
 
const IMAGE_ITEM ="../../../assets/rutas-lr/";
const ICON = "../../../assets/icons/";

export interface RutasPeriodic {
  Miniature1: string;
  Miniature2: String;
  IconDR: string;
  IconDT: string;
  Orden: number;
  Nombre: string;
  PanelAbierto?: boolean; // Propiedad para controlar el estado del panel
}

const ELEMENT_DATA: RutasPeriodic[] = [
  { Miniature1: IMAGE_ITEM + "DR1.jpg", Miniature2: IMAGE_ITEM + "DT2.jpg",IconDR: ICON + "DR-active-icon.png", IconDT: ICON + "DT-inactive-icon.png",  Orden: 1, Nombre: " "+"Hipertensión arterial"},
  { Miniature1: IMAGE_ITEM + "DR2.jpg", Miniature2: IMAGE_ITEM + "DT2.jpg",IconDR: ICON + "DR-inactive-icon.png", IconDT: ICON + "DT-active-icon.png", Orden: 2, Nombre: "Diabetes"},
  { Miniature1: IMAGE_ITEM + "DR2.jpg", Miniature2: IMAGE_ITEM + "DT2.jpg",IconDR: ICON + "DR-inactive-icon.png", IconDT: ICON + "DT-active-icon.png", Orden: 3, Nombre: "IAM"},
  { Miniature1: IMAGE_ITEM + "DR2.jpg", Miniature2: IMAGE_ITEM + "DT2.jpg",IconDR: ICON + "DR-inactive-icon.png", IconDT: ICON + "DT-active-icon.png", Orden: 4, Nombre: "Cáncer colorrectal"},
  { Miniature1: IMAGE_ITEM + "DR2.jpg", Miniature2: IMAGE_ITEM + "DT2.jpg",IconDR: ICON + "DR-inactive-icon.png", IconDT: ICON + "DT-active-icon.png", Orden: 5, Nombre: "Actividad física"},
  { Miniature1: IMAGE_ITEM + "DR2.jpg", Miniature2: IMAGE_ITEM + "DT2.jpg",IconDR: ICON + "DR-inactive-icon.png", IconDT: ICON + "DT-active-icon.png", Orden: 6, Nombre: "Sonríe"},
  { Miniature1: IMAGE_ITEM + "DR2.jpg", Miniature2: IMAGE_ITEM + "DT2.jpg",IconDR: ICON + "DR-inactive-icon.png", IconDT: ICON + "DT-active-icon.png", Orden: 7, Nombre: "Celiaquía"},
  { Miniature1: IMAGE_ITEM + "DR2.jpg", Miniature2: IMAGE_ITEM + "DT2.jpg",IconDR: ICON + "DR-inactive-icon.png", IconDT: ICON + "DT-active-icon.png", Orden: 8, Nombre: "Persona gestante"},

];

@Component({
  selector: 'app-rutas',
  templateUrl: './rutas.component.html',
  styleUrls: ['./rutas.component.scss']
})
export class RutasComponent implements OnInit {
  
  data: RutasPeriodic[] = ELEMENT_DATA;

  displayedColumns: string[] = ['Miniature1', 'Miniature2', 'IconDR','IconDT','Orden', 'Nombre'];
  columnsToDisplay: string[] = this.displayedColumns.slice();
  panelOpenState: number | null = null;


  ngOnInit(): void {
  }
 
  togglePanel(orden: number): void {
    if (this.panelOpenState === orden) {
      this.panelOpenState = null; // Cierra el panel
    } else {
      this.panelOpenState = orden; // Abre el panel
    }
  }


  getImagePath(fileName: string): string {
    return IMAGE_ITEM + fileName;
  }
   
}
