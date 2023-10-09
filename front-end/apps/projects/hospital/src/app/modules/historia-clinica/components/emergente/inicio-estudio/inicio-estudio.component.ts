import { Component, OnInit } from '@angular/core';
import { EstudiosPopupComponent } from '../pop-up/estudios-popup.component';
import { MatDialog } from '@angular/material/dialog';
  
@Component({
  selector: 'app-adulto-mayor',
  templateUrl: './inicio-estudio.component.html',
  styleUrls: ['./inicio-estudio.component.scss']
})
export class AdultoMayorComponent implements OnInit {
  patientId: number;
  private readonly routePrefix: string;

  constructor(
    private dialog: MatDialog  ) { }

  ngOnInit(): void {
   }

  mostrarPopup(): void {
    const dialogRef = this.dialog.open(EstudiosPopupComponent, {
      width: '800px',
      data: {
        patientId: this.patientId,
        institutionId: this.routePrefix
      }
    });

    dialogRef.afterClosed().subscribe(result => {
     });
  }
}
