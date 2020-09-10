import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICharacter, Character } from 'app/shared/model/character.model';
import { CharacterService } from './character.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-character-update',
  templateUrl: './character-update.component.html'
})
export class CharacterUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    baseAttack: [null, [Validators.required, Validators.max(100)]],
    user: []
  });

  constructor(
    protected characterService: CharacterService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ character }) => {
      this.updateForm(character);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(character: ICharacter): void {
    this.editForm.patchValue({
      id: character.id,
      name: character.name,
      baseAttack: character.baseAttack,
      user: character.user
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const character = this.createFromForm();
    if (character.id !== undefined) {
      this.subscribeToSaveResponse(this.characterService.update(character));
    } else {
      this.subscribeToSaveResponse(this.characterService.create(character));
    }
  }

  private createFromForm(): ICharacter {
    return {
      ...new Character(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      baseAttack: this.editForm.get(['baseAttack'])!.value,
      user: this.editForm.get(['user'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharacter>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
