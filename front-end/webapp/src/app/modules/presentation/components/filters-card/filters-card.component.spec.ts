import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltersCardComponent } from './filters-card.component';

describe('FiltersCardComponent', () => {
  let component: FiltersCardComponent;
  let fixture: ComponentFixture<FiltersCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FiltersCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FiltersCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
