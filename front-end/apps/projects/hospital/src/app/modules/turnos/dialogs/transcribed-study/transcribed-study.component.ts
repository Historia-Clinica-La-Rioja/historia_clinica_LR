import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { AppFeature, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';

@Component({
  selector: 'app-transcribed-study',
  templateUrl: './transcribed-study.component.html',
  styleUrls: ['./transcribed-study.component.scss']
})
export class TranscribedStudyComponent implements OnInit {

  searchConceptsLocallyFFIsOn= false;
  readonly transcribedEcl= SnomedECL.PROCEDURE
  private snomedConcept:SnomedDto

  constructor(
		private readonly featureFlagService: FeatureFlagService,
		private readonly snomedService: SnomedService,
    private dialogRef: MatDialogRef<TranscribedStudyComponent>,
  ) { }

  ngOnInit(): void {
    this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
  }


openSearchDialog(searchValue: string): void {
  if (searchValue) {
      const search: SnomedSemanticSearch = {
          searchValue,
          eclFilter: this.transcribedEcl
      };
      this.snomedService.openConceptsSearchDialog(search)
          .subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
  }
}


setConcept(selectedConcept: SnomedDto): void {
  this.snomedConcept = selectedConcept;
  this.dialogRef.close(this.snomedConcept)
}

addStudy(): void {
  this.dialogRef.close(this.snomedConcept)
}

close(): void {
  this.dialogRef.close()
}

}
