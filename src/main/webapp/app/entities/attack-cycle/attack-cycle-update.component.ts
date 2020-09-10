import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAttackCycle, AttackCycle } from 'app/shared/model/attack-cycle.model';
import { AttackCycleService } from './attack-cycle.service';
import { IAttack } from 'app/shared/model/attack.model';
import { AttackService } from 'app/entities/attack/attack.service';
import { ICharacter } from 'app/shared/model/character.model';
import { CharacterService } from 'app/entities/character/character.service';

type SelectableEntity = IAttack | ICharacter;

@Component({
  selector: 'jhi-attack-cycle-update',
  templateUrl: './attack-cycle-update.component.html'
})
export class AttackCycleUpdateComponent implements OnInit {
  isSaving = false;
  attacks: IAttack[] = [];
  characters: ICharacter[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    attacks: [],
    character: []
  });

  constructor(
    protected attackCycleService: AttackCycleService,
    protected attackService: AttackService,
    protected characterService: CharacterService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attackCycle }) => {
      this.updateForm(attackCycle);

      this.attackService.query().subscribe((res: HttpResponse<IAttack[]>) => (this.attacks = res.body || []));

      this.characterService.query().subscribe((res: HttpResponse<ICharacter[]>) => (this.characters = res.body || []));
    });
  }

  updateForm(attackCycle: IAttackCycle): void {
    this.editForm.patchValue({
      id: attackCycle.id,
      name: attackCycle.name,
      attacks: attackCycle.attacks,
      character: attackCycle.character
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attackCycle = this.createFromForm();
    if (attackCycle.id !== undefined) {
      this.subscribeToSaveResponse(this.attackCycleService.update(attackCycle));
    } else {
      this.subscribeToSaveResponse(this.attackCycleService.create(attackCycle));
    }
  }

  private createFromForm(): IAttackCycle {
    return {
      ...new AttackCycle(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      attacks: this.editForm.get(['attacks'])!.value,
      character: this.editForm.get(['character'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttackCycle>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IAttack[], option: IAttack): IAttack {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
