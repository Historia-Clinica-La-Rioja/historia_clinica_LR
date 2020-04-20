import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientInternmentCardComponent } from './patient-internment-card.component';

describe('PatientHospitalizationCardComponent', () => {
  let component: PatientInternmentCardComponent;
  let fixture: ComponentFixture<PatientInternmentCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PatientInternmentCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientInternmentCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
