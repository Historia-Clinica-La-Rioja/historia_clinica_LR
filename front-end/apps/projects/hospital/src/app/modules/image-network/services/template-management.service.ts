import { Injectable } from '@angular/core';
import { TemplateNamesDto, TextTemplateDto } from '@api-rest/api-model';
import { DocumentTemplateService } from '@api-rest/services/document-template.service';
import { ReportDetailsTemplateService } from '@api-rest/services/report-details-template.service';
import { BehaviorSubject, Observable, map, take, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TemplateManagementService {

  private  REPORT_DETAILS_RDI = 1

  constructor(
    private readonly documentTemplateService: DocumentTemplateService,
    private readonly reportDetailsTemplateService: ReportDetailsTemplateService,
  ) { }

  private templateSource$: BehaviorSubject<TemplateNamesDto[]> = new BehaviorSubject([])
  private templateImports$: Observable<TemplateNamesDto[]> = this.templateSource$.asObservable()


  getTemplatesImport(): Observable<TemplateNamesDto[]> {
    if (!(this.templateSource$.getValue().length > 0))
      { this.load() }
    return this.templateImports$
  }

  saveTemplate(templatesInfo: TextTemplateDto): Observable<boolean> {
    return this.reportDetailsTemplateService.saveTemplate(templatesInfo)
    .pipe(tap( _ => this.load()))
  }

  deleteTemplate(id: number): Observable<boolean> {
    return this.documentTemplateService.deleteTemplatesByUserInformer(this.REPORT_DETAILS_RDI, id)
    .pipe(tap( _ => this.load()))
  }

  existsImports(): Observable<boolean> {
    return this.getTemplatesImport().pipe(
      map(templates => templates.length > 0))
  }

  getOneTemplateImports(templateId: number): Observable<TextTemplateDto> {
    return this.reportDetailsTemplateService.getOneTemplate(templateId)
      .pipe(tap( _ => this.load()))
  }

  clearTemplateManagement(): void {
    this.templateSource$.next([])
  }

  private load(): void {
    this.documentTemplateService.getTemplatesByUserInformer(this.REPORT_DETAILS_RDI).pipe(take(1))
    .subscribe(
      template => this.templateSource$.next(template)
    )
  }
}
