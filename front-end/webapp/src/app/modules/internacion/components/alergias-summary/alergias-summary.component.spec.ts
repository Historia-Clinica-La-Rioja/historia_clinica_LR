import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlergiasSummaryComponent } from './alergias-summary.component';

describe('AlergiasSummaryComponent', () => {
  let component: AlergiasSummaryComponent;
  let fixture: ComponentFixture<AlergiasSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlergiasSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlergiasSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
