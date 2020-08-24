import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BedMappingComponent } from './bed-mapping.component';

describe('BedMappingComponent', () => {
  let component: BedMappingComponent;
  let fixture: ComponentFixture<BedMappingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BedMappingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BedMappingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
