import { Component } from '@angular/core';

@Component({
  selector: 'app-vademecum-popup',
  templateUrl: './vademecum-popup.component.html',
  styleUrls: ['./vademecum-popup.component.scss']
})
export class VademecumPopupComponent {
  isOpen = false;
  medicamentos = [
    { descripcion: 'ACENOCUMAROL 4 MG', presentacion: 'COMPRIMIDOS' },
    { descripcion: 'ACETAZOLAMIDA 250 MG', presentacion: 'COMPRIMIDOS' },
    { descripcion: 'ACICLOVIR 400 MG', presentacion: 'COMPRIMIDOS' },
    { descripcion: 'ACENOCUMAROL 4 MG', presentacion: 'COMPRIMIDOS' },
    { descripcion: 'ACETAZOLAMIDA 250 MG', presentacion: 'COMPRIMIDOS' },
    { descripcion: 'ACICLOVIR 400 MG', presentacion: 'COMPRIMIDOS' },
    { descripcion: 'ACENOCUMAROL 4 MG', presentacion: 'COMPRIMIDOS' },
    { descripcion: 'ACETAZOLAMIDA 250 MG', presentacion: 'COMPRIMIDOS' },
    { descripcion: 'ACICLOVIR 400 MG', presentacion: 'COMPRIMIDOS' },
   ];
  filteredMedicamentos = [...this.medicamentos];
  searchTerm = '';

  openPopup() {
    this.isOpen = true;
  }

  closePopup() {
    this.isOpen = false;
  }

  filterMedicamentos() {
    this.filteredMedicamentos = this.medicamentos.filter(med => 
      med.descripcion.toLowerCase().includes(this.searchTerm.toLowerCase()));
  }
}
