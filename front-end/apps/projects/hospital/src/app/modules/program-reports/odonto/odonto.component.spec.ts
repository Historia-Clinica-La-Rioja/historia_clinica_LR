import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OdontoComponent } from './odonto.component';

describe('OdontoComponent', () => {
  let component: OdontoComponent;
  let fixture: ComponentFixture<OdontoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OdontoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OdontoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
