import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AntecedentesFamiliaresSummaryComponent } from './antecedentes-familiares-summary.component';

describe('AntecedentesFamiliaresSummaryComponent', () => {
  let component: AntecedentesFamiliaresSummaryComponent;
  let fixture: ComponentFixture<AntecedentesFamiliaresSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AntecedentesFamiliaresSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AntecedentesFamiliaresSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
