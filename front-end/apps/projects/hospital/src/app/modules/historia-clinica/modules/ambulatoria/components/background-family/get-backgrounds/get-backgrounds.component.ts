import { Component, OnInit } from '@angular/core';
import { QuestionnairesResponses } from '@api-rest/api-model';
import { AntecedentesServices } from '@api-rest/services/antecedentes.service';
import { BasicPatientDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { BackgroundFamilyComponent } from '../background-family.component';


@Component({
  selector: 'app-get-backgrounds',
  templateUrl: './get-backgrounds.component.html',
  styleUrls: ['./get-backgrounds.component.scss']
})
export class GetBackgroundsComponent implements OnInit {

  patientId: number;
  patientData: BasicPatientDto | undefined;
  lastBackgroundsFamily: QuestionnairesResponses[] = [];
  routePrefix: string;
  loadProfessionalId: any;

  constructor(
    private dialog: MatDialog,
    private antecedentesServices: AntecedentesServices,
    private readonly contextService: ContextService,
    private readonly route: ActivatedRoute,
    
  ) {
    this.routePrefix = `${this.contextService.institutionId}`;
    this.route.paramMap.subscribe(params => {
      this.patientId = Number(params.get('idPaciente'));

      console.log('patienId antes del dialogo', this.patientId)
    });
  }

  ngOnInit(): void {

    this.loadLastBackgroundsQuestionnaires();
  }

  loadLastBackgroundsQuestionnaires(): void {
    this.antecedentesServices.getAllByPatientId(this.patientId).subscribe(
      (questionnaires: QuestionnairesResponses[]) => {
        const familyQuestionnaires = questionnaires.filter(
          (questionnaire) => questionnaire.questionnaireTypeId === 2
        );
        familyQuestionnaires.sort((a, b) => {
          return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
        });
        this.lastBackgroundsFamily = familyQuestionnaires;

      })
  }

  downloadBackgroundsPdf(questionnaireId: number): void {
    this.antecedentesServices.getPdf(questionnaireId).subscribe(
      (pdfBlob: Blob) => {
        const url = window.URL.createObjectURL(pdfBlob);
        window.open(url);
      },
      (error) => {
        console.error('Error downloading the questionnaire PDF:', error);
      }
    );
  }

  profesionalId(): void {
    this.antecedentesServices.getAllByPatientId(this.patientId).subscribe(info => {
      if (info.length > 0) {
        this.loadProfessionalId = info[0].createdByFullName;
      }
    });
  }


  mostrarPopup(): void {
    const dialogRef = this.dialog.open(BackgroundFamilyComponent, {
      width: '100%',
      height: '120%',
      minWidth: '400px',
      minHeight: '300px',
      maxWidth: '1000px',
      maxHeight: '800px',
      data: {
        patientId: this.patientId,
      },
    });
    
     dialogRef.afterClosed().subscribe(result => {
    });
  }

}
