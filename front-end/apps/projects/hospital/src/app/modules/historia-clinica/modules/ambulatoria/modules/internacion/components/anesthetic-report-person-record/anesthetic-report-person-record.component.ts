import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AnestheticReportRecordService, PersonalRecordData, RecordForm } from '../../services/anesthetic-report-record.service';
import { AnestheticReportAddRecordComponent } from '../../dialogs/anesthetic-report-add-record/anesthetic-report-add-record.component';
import { FormGroup } from '@angular/forms';
import { ToFormGroup } from '@core/utils/form.utils';

@Component({
    selector: 'app-anesthetic-report-person-record',
    templateUrl: './anesthetic-report-person-record.component.html',
    styleUrls: ['./anesthetic-report-person-record.component.scss']
})
export class AnestheticReportPersonRecordComponent implements OnInit {

    @Input() service: AnestheticReportRecordService;
	searchConceptsLocallyFFIsOn = false;
    form: FormGroup<RecordForm>;
    personalRecordForm: FormGroup<ToFormGroup<PersonalRecordData>>;
    readonly ASAOptions = [1,2,3,4,5]

    constructor(
        private readonly dialog: MatDialog,
        private readonly featureFlagService: FeatureFlagService,
    ) { }

    ngOnInit(): void {
        this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
        this.form = this.service.getForm();
        this.personalRecordForm = this.service.getPersonalRecordForm();
    }

    addRecord(){
        this.dialog.open(AnestheticReportAddRecordComponent, {
            data: {
                anestheticReportRecordService: this.service,
                searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
            },
            autoFocus: false,
            width: '35%',
            disableClose: true,
        });
    }
}