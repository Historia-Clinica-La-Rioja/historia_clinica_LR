import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BedFiltersComponent } from './bed-filters.component';

describe('BedFiltersComponent', () => {
  let component: BedFiltersComponent;
  let fixture: ComponentFixture<BedFiltersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BedFiltersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BedFiltersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
