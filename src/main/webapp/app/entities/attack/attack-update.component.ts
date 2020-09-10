import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAttack, Attack } from 'app/shared/model/attack.model';
import { AttackService } from './attack.service';
import { IWeapon } from 'app/shared/model/weapon.model';
import { WeaponService } from 'app/entities/weapon/weapon.service';

@Component({
  selector: 'jhi-attack-update',
  templateUrl: './attack-update.component.html'
})
export class AttackUpdateComponent implements OnInit {
  isSaving = false;
  weapons: IWeapon[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    attackModifier: [null, [Validators.required, Validators.max(100)]],
    critChance: [null, [Validators.required, Validators.max(100)]],
    critDamage: [null, [Validators.required, Validators.max(100)]],
    dieValue: [null, [Validators.required, Validators.max(100)]],
    diceCount: [null, [Validators.required, Validators.max(100)]],
    damageBonus: [null, [Validators.required, Validators.max(100)]],
    weapon: []
  });

  constructor(
    protected attackService: AttackService,
    protected weaponService: WeaponService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attack }) => {
      this.updateForm(attack);

      this.weaponService.query().subscribe((res: HttpResponse<IWeapon[]>) => (this.weapons = res.body || []));
    });
  }

  updateForm(attack: IAttack): void {
    this.editForm.patchValue({
      id: attack.id,
      name: attack.name,
      attackModifier: attack.attackModifier,
      critChance: attack.critChance,
      critDamage: attack.critDamage,
      dieValue: attack.dieValue,
      diceCount: attack.diceCount,
      damageBonus: attack.damageBonus,
      weapon: attack.weapon
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attack = this.createFromForm();
    if (attack.id !== undefined) {
      this.subscribeToSaveResponse(this.attackService.update(attack));
    } else {
      this.subscribeToSaveResponse(this.attackService.create(attack));
    }
  }

  private createFromForm(): IAttack {
    return {
      ...new Attack(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      attackModifier: this.editForm.get(['attackModifier'])!.value,
      critChance: this.editForm.get(['critChance'])!.value,
      critDamage: this.editForm.get(['critDamage'])!.value,
      dieValue: this.editForm.get(['dieValue'])!.value,
      diceCount: this.editForm.get(['diceCount'])!.value,
      damageBonus: this.editForm.get(['damageBonus'])!.value,
      weapon: this.editForm.get(['weapon'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttack>>): void {
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

  trackById(index: number, item: IWeapon): any {
    return item.id;
  }
}
