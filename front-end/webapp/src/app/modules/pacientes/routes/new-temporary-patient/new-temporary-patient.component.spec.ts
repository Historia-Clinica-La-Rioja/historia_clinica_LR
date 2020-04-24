import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewTemporaryPatientComponent } from './new-temporary-patient.component';

describe('NewTemporaryPatientComponent', () => {
  let component: NewTemporaryPatientComponent;
  let fixture: ComponentFixture<NewTemporaryPatientComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewTemporaryPatientComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewTemporaryPatientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
