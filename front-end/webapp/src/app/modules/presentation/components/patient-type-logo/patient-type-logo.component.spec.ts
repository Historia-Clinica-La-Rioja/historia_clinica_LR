import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientTypeLogoComponent } from './patient-type-logo.component';

describe('PatientTypeLogoComponent', () => {
  let component: PatientTypeLogoComponent;
  let fixture: ComponentFixture<PatientTypeLogoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PatientTypeLogoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientTypeLogoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
