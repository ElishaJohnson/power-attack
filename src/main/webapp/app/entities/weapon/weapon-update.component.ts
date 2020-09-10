import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IWeapon, Weapon } from 'app/shared/model/weapon.model';
import { WeaponService } from './weapon.service';
import { ICharacter } from 'app/shared/model/character.model';
import { CharacterService } from 'app/entities/character/character.service';

@Component({
  selector: 'jhi-weapon-update',
  templateUrl: './weapon-update.component.html'
})
export class WeaponUpdateComponent implements OnInit {
  isSaving = false;
  characters: ICharacter[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    attackModifier: [null, [Validators.required, Validators.max(100)]],
    critChance: [null, [Validators.required, Validators.max(100)]],
    critDamage: [null, [Validators.required, Validators.max(100)]],
    dieValue: [null, [Validators.required, Validators.max(100)]],
    diceCount: [null, [Validators.required, Validators.max(100)]],
    damageBonus: [null, [Validators.required, Validators.max(100)]],
    character: []
  });

  constructor(
    protected weaponService: WeaponService,
    protected characterService: CharacterService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weapon }) => {
      this.updateForm(weapon);

      this.characterService.query().subscribe((res: HttpResponse<ICharacter[]>) => (this.characters = res.body || []));
    });
  }

  updateForm(weapon: IWeapon): void {
    this.editForm.patchValue({
      id: weapon.id,
      name: weapon.name,
      attackModifier: weapon.attackModifier,
      critChance: weapon.critChance,
      critDamage: weapon.critDamage,
      dieValue: weapon.dieValue,
      diceCount: weapon.diceCount,
      damageBonus: weapon.damageBonus,
      character: weapon.character
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const weapon = this.createFromForm();
    if (weapon.id !== undefined) {
      this.subscribeToSaveResponse(this.weaponService.update(weapon));
    } else {
      this.subscribeToSaveResponse(this.weaponService.create(weapon));
    }
  }

  private createFromForm(): IWeapon {
    return {
      ...new Weapon(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      attackModifier: this.editForm.get(['attackModifier'])!.value,
      critChance: this.editForm.get(['critChance'])!.value,
      critDamage: this.editForm.get(['critDamage'])!.value,
      dieValue: this.editForm.get(['dieValue'])!.value,
      diceCount: this.editForm.get(['diceCount'])!.value,
      damageBonus: this.editForm.get(['damageBonus'])!.value,
      character: this.editForm.get(['character'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeapon>>): void {
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

  trackById(index: number, item: ICharacter): any {
    return item.id;
  }
}
