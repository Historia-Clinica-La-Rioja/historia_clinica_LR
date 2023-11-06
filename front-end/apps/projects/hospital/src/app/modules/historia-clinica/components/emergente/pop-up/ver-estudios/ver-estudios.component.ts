import { Component, Input, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialogRef , MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-ver-estudios',
  templateUrl: './ver-estudios.component.html',
  styleUrls: ['./ver-estudios.component.scss']
})
export class VerEstudiosComponent {

  selectedOption: string = '';
  @Input() patientId: number;
  
  
  constructor(
    private dialogRef: MatDialogRef<VerEstudiosComponent>,
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
