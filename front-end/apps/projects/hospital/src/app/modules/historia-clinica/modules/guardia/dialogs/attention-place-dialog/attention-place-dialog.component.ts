import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MasterDataInterface } from '@api-rest/api-model';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { hasError } from '@core/utils/form.utils';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-attention-place-dialog',
  templateUrl: './attention-place-dialog.component.html',
  styleUrls: ['./attention-place-dialog.component.scss']
})
export class AttentionPlaceDialogComponent {

  places$: Observable<MasterDataInterface<number>[]>;
  form: FormGroup;
  hasError = hasError;
  
  constructor(private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
              private readonly formBuilder: FormBuilder) 
    {
      this.places$ = this.emergencyCareMasterDataService.getEmergencyEpisodeSectorType();
      this.setForm();
    }
    
  private setForm() {
    this.form = this.formBuilder.group({
      place: [null, Validators.required]
    });
  }

  verifyPlaceType() {
  }
}
