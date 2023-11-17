import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SurgicalReportProfessionalInfoComponent } from './surgical-report-professional-info.component';

describe('SurgicalReportProfessionalInfoComponent', () => {
  let component: SurgicalReportProfessionalInfoComponent;
  let fixture: ComponentFixture<SurgicalReportProfessionalInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SurgicalReportProfessionalInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SurgicalReportProfessionalInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
