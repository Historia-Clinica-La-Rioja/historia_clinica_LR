import { Component, Input, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialogRef , MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-pop-up',
  templateUrl: './estudios-popup.component.html',
  styleUrls: ['./estudios-popup.component.scss']
})

export class EstudiosPopupComponent {
  selectedOption: string = '';
  @Input() patientId: number;
  
  
  constructor(
    private dialogRef: MatDialogRef<EstudiosPopupComponent>,
    private readonly route: ActivatedRoute,
    @Inject(MAT_DIALOG_DATA) private data: any
    ) {
      this.patientId = data.patientId;
      console.log("Datos del diálogo:", this.data);

  }


  usuario(){
    this.route.paramMap.subscribe(params => {
    this.patientId = Number(params.get('idPaciente'));
    });   
  }

  closeModal(simpleClose: boolean, completed?: boolean): void {
    if (simpleClose) {
      this.dialogRef.close();  
    } else {
     }
  }
}
