import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PowerattackTestModule } from '../../../test.module';
import { AttackUpdateComponent } from 'app/entities/attack/attack-update.component';
import { AttackService } from 'app/entities/attack/attack.service';
import { Attack } from 'app/shared/model/attack.model';

describe('Component Tests', () => {
  describe('Attack Management Update Component', () => {
    let comp: AttackUpdateComponent;
    let fixture: ComponentFixture<AttackUpdateComponent>;
    let service: AttackService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PowerattackTestModule],
        declarations: [AttackUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AttackUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttackUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttackService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Attack(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Attack();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
