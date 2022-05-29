import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddWindowComponent } from './add-window.component';

describe('AddWindowComponent', () => {
  let component: AddWindowComponent;
  let fixture: ComponentFixture<AddWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddWindowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
