import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewPatientDetailComponent } from './view-patient-detail.component';

describe('ViewPatientDetailComponent', () => {
  let component: ViewPatientDetailComponent;
  let fixture: ComponentFixture<ViewPatientDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewPatientDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewPatientDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
