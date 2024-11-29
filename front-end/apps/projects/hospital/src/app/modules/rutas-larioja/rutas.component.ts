import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'; 

const IMAGE_ITEM = "../../../assets/rutas-lr/miniatures/";
const ICON = "../../../assets/icons/";
const PDF_BASE_PATH = '../../../assets/rutas-lr/'; 

export interface RutasPeriodic {
  routerLink?: string | null;
  Miniature1: string;
  Miniature2: string;
  IconDR: string;
  IconDT: string;
  Orden: number;
  Nombre: string;
  pdfDR?: string | null;
  pdfDT?: string | null;
  PanelAbierto?: boolean; // Propiedad para controlar el estado del panel
}

const ELEMENT_DATA: RutasPeriodic[] = [
  {
    //ITEMS RUTAS ASISTENCIALES
    Miniature1: IMAGE_ITEM + "DR1.jpg",
    Miniature2: IMAGE_ITEM + "DT1.png",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 1,
    Nombre: "Hipertensión arterial",
    pdfDR: PDF_BASE_PATH + "diagramas-de-ruta-pdf/Hipertension-RutaAsistencial.pdf",     
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: PDF_BASE_PATH + "documentos-tecnicos-pdf/Hipertension-RutaAsistencial-DocumentoTecnico.pdf",                                                                           // Ruta al DT
  },
  {
    Miniature1: IMAGE_ITEM + "DR2.jpg",
    Miniature2: IMAGE_ITEM + "DT-EPOC.jpg",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 2,
    Nombre: " EPOC",
    pdfDR: PDF_BASE_PATH + "diagramas-de-ruta-pdf/EPOC-RutaAsistencial.pdf",
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: PDF_BASE_PATH + "documentos-tecnicos-pdf/EPOC-RutaAsistencial-DocumentoTecnico.pdf",
  },
  {
    Miniature1: IMAGE_ITEM + "DR1.jpg",
    Miniature2: IMAGE_ITEM + "DT1.jpg",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 3,
    Nombre: " Diabetes",
    pdfDR: null,
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: null,
  },
  {
    Miniature1: IMAGE_ITEM + "DR1.jpg",
    Miniature2: IMAGE_ITEM + "DT1.jpg",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 4,
    Nombre: " IAM",
    pdfDR: null,
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: null, 
  },
  {
    Miniature1: IMAGE_ITEM + "DR-CC.png",
    Miniature2: IMAGE_ITEM + "DT-CC.png",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 5,
    Nombre: " Cáncer colorrectal",
    pdfDR: PDF_BASE_PATH + "diagramas-de-ruta-pdf/CancerColorectal-RutaAsistencial.pdf",
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: PDF_BASE_PATH + "documentos-tecnicos-pdf/CancerColorrectal-RutaAsistencial-DocumentoTecnico.pdf",
  },
  {
    Miniature1: IMAGE_ITEM + "DR-AF.png",
    Miniature2: IMAGE_ITEM + "DT-AF.png",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 6,
    Nombre: " Actividad física",
    pdfDR: PDF_BASE_PATH + "diagramas-de-ruta-pdf/ActividadFisica-RutaAsistencial.pdf",
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: PDF_BASE_PATH + "documentos-tecnicos-pdf/ActividadFisica-RutaAsistencial-DocumentoTecnico.pdf",
  },
  {
    Miniature1: IMAGE_ITEM + "DR1.jpg",
    Miniature2: IMAGE_ITEM + "DT1.jpg",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 7,
    Nombre: " Sonríe",
    pdfDR: null,
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: null,
  },
  {
    Miniature1: IMAGE_ITEM + "DR1.jpg",
    Miniature2: IMAGE_ITEM + "DT1.jpg",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 8,
    Nombre: " Celiaquía",
    pdfDR: null,
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: null,
  },
  {
    Miniature1: IMAGE_ITEM + "DR1.jpg",
    Miniature2: IMAGE_ITEM + "DT1.jpg",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 9,
    Nombre: " Persona gestante",
    pdfDR: null,
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: null,
  },
  {
    Miniature1: IMAGE_ITEM + "DR-O.png",
    Miniature2: IMAGE_ITEM + "DT-O.png",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 10,
    Nombre: " Odontología",
    pdfDR: PDF_BASE_PATH + "diagramas-de-ruta-pdf/Odontologia-RutaAsistencial.pdf",
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: PDF_BASE_PATH + "documentos-tecnicos-pdf/Odontología-RutaAsistencial-DocumentoTecnico.pdf",
  }

];

const ELEMENT_DATA_PREV: RutasPeriodic[] = [
  {
    Miniature1: IMAGE_ITEM + "DR-AM.png", // Ruta de la miniatura
    Miniature2: IMAGE_ITEM + "DT1.jpg",
    IconDR: ICON + "DR-active-icon.png",
    IconDT: ICON + "DT-active-icon.png",
    Orden: 1,
    Nombre: "Adulto mayor",
    pdfDR: PDF_BASE_PATH + "diagramas-de-ruta-pdf/AdultoMayor-RutaPreventiva.pdf", // Ruta al documento
    routerLink: '../../../assets/rutas-lr/', 
    pdfDT: null,    
  },

];

@Component({
  selector: 'app-rutas',
  templateUrl: './rutas.component.html',
  styleUrls: ['./rutas.component.scss']
})
export class RutasComponent implements OnInit {

  data: RutasPeriodic[] = ELEMENT_DATA;
  dataP: RutasPeriodic[]= ELEMENT_DATA_PREV;

  displayedColumns: string[] = ['Miniature1', 'Miniature2', 'IconDR', 'IconDT', 'Orden', 'Nombre', 'pdfDR', 'pdfDT'];
  columnsToDisplay: string[] = this.displayedColumns.slice();
  panelOpenState: number | null = null;

  constructor(private router: Router) {} 

  ngOnInit(): void {}

  togglePanel(orden: number): void {
  }

  getImagePath(fileName: string): string {
    return IMAGE_ITEM + fileName;
  }

  handleClickDR(index: number, isPreventiva: boolean = false) {
    const element = isPreventiva ? this.dataP[index] : this.data[index];
  
    if (element && element.pdfDR) { 
      console.log('Abriendo PDF:', element.pdfDR); // Log para depuración
      window.open(element.pdfDR, '_blank'); 
    } else if (element && element.routerLink) {
      console.log('Navegando a:', element.routerLink);
      this.router.navigate([element.routerLink]); 
    } 
  }

  handleClickDT(index: number, isPreventiva: boolean = false) {
    const element = isPreventiva ? this.dataP[index] : this.data[index];
  
    if (element && element.pdfDT) { 
      console.log('Abriendo PDF:', element.pdfDT); // Log para depuración
      window.open(element.pdfDT, '_blank'); 
    } else if (element && element.routerLink) {
      console.log('Navegando a:', element.routerLink);
      this.router.navigate([element.routerLink]); 
    } 
  }
    


}
