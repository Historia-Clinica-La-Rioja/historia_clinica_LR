import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicalCoverageComponent } from './medical-coverage.component';

describe('MedicalCoverageComponent', () => {
  let component: MedicalCoverageComponent;
  let fixture: ComponentFixture<MedicalCoverageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MedicalCoverageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicalCoverageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
