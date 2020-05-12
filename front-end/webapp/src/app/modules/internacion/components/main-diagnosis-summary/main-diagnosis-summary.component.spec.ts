import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MainDiagnosisSummaryComponent } from './main-diagnosis-summary.component';

describe('MainDiagnosisSummaryComponent', () => {
  let component: MainDiagnosisSummaryComponent;
  let fixture: ComponentFixture<MainDiagnosisSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MainDiagnosisSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MainDiagnosisSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
