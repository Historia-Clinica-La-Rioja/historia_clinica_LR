import {Component, Input} from '@angular/core';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';


@Component({
  selector: 'app-ed-monton',
  templateUrl: './ed-monton.component.html',
  styleUrls: ['./ed-monton.component.scss']
})
export class EdmontonFormComponent {
  @Input() institutionId: number;
  @Input() patientId: number;
  edmontonForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private edmontonService: EdmontonService
    ) {

    this.edmontonForm = this.formBuilder.group({
      selectedCognitiveOption: ['', Validators.required],
      selectedHealthStatusOption: ['', Validators.required],
      selectedHealthStatusOptionDos: ['', Validators.required],
      selectedFunctionIndOption: ['', Validators.required],
      selectedMedicationOption: ['', Validators.required],
      selectedMedicationOptionDos: ['', Validators.required],
      selectedNutritionOption: ['', Validators.required],
      selectedAnimoOption: ['', Validators.required],
      selectedContingenciaOption: ['', Validators.required],
      selectedRendimientoFuncOption: ['', Validators.required],
      selectedCalificacionFuncOption: ['', Validators.required],
    });
  }

  submitForm() {
    if (this.edmontonForm.valid) {
      const edmontonData = this.edmontonForm.value;
      this.edmontonService.crearEdMonton(this.institutionId, this.patientId, edmontonData)
        .subscribe(() => {
          console.log("Success")
        }, (error) => {
          console.log("Error")
        });
    }
  }

}