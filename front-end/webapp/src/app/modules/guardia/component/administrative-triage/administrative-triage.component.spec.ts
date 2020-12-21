import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrativeTriageComponent } from './administrative-triage.component';

describe('AdministrativeTriageComponent', () => {
  let component: AdministrativeTriageComponent;
  let fixture: ComponentFixture<AdministrativeTriageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdministrativeTriageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdministrativeTriageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
