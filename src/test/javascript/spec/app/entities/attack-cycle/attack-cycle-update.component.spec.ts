import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PowerattackTestModule } from '../../../test.module';
import { AttackCycleUpdateComponent } from 'app/entities/attack-cycle/attack-cycle-update.component';
import { AttackCycleService } from 'app/entities/attack-cycle/attack-cycle.service';
import { AttackCycle } from 'app/shared/model/attack-cycle.model';

describe('Component Tests', () => {
  describe('AttackCycle Management Update Component', () => {
    let comp: AttackCycleUpdateComponent;
    let fixture: ComponentFixture<AttackCycleUpdateComponent>;
    let service: AttackCycleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PowerattackTestModule],
        declarations: [AttackCycleUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AttackCycleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttackCycleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttackCycleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AttackCycle(123);
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
        const entity = new AttackCycle();
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
