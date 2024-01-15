import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FamilyRecordService } from '@historia-clinica/modules/ambulatoria/services/antecedentes-familiares.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-get-antecedentes',
  templateUrl: './get-antecedentes.component.html',
  styleUrls: ['./get-antecedentes.component.scss']
})
export class GetAntecedentesComponent implements OnInit {
  submitted: boolean;
  consultation: number;
  patientId: any;
  questionnaireId: string;
  name: string;

  constructor(
    private antecedentesFamiliares: FamilyRecordService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { 
    this.patientId = data.patientId;
    this.name = data.name;
  }

  ngOnInit(): void {
    this.antecedentesFamiliares.getQuestionnaireId('2', this.patientId.toString()).subscribe(
      (id) => {
        if (id) {
          this.questionnaireId = id.toString();
        } else {
          console.error('No se pudo obtener questionnaireId.');
        }
      },
      (error) => {
        console.error('Error al obtener questionnaireId:', error);
      }
    );
  }
  descargarPDF(): void {
    if (this.questionnaireId) {
      const nombrePaciente = this.name;  // Reemplaza con el nombre real del paciente
      const fechaActual = this.obtenerFechaActual();

      const nombreArchivo = `${nombrePaciente} - ${fechaActual} - AntecedentesFamiliares.pdf`;

      this.antecedentesFamiliares.getPdf('2', this.questionnaireId).subscribe(
        (pdfBlob) => {
          const pdfUrl = URL.createObjectURL(pdfBlob);
          const downloadLink = document.createElement('a');
          downloadLink.href = pdfUrl;
          downloadLink.download = nombreArchivo;
          document.body.appendChild(downloadLink);
          downloadLink.click();
          document.body.removeChild(downloadLink);
        },
        (error) => {
          console.error('Error:', error);
          Swal.fire({
            icon: "error",
            title: "Error",
            text: "El PDF no pudo descargarse."
          });
        }
      );
    } else {
      console.error('questionnaireId no está disponible.');
    }
  }

  obtenerFechaActual(): string {
    const fecha = new Date();
    const dia = fecha.getDate();
    const mes = fecha.getMonth() + 1;
    const anio = fecha.getFullYear();

    // Formatea la fecha como DD-MM-YYYY
    return `${dia}-${mes}-${anio}`;
  }
}
