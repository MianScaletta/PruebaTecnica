
import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService, InventarioDTO } from '../services/api.service';

@Component({
  selector: 'app-inventario',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './inventario.html',
  styleUrls: ['./inventario.css'],
})
export class Inventario implements OnInit {

  inventarioForm = new FormGroup({
    nombre: new FormControl<string | null>(null, [Validators.required]),
    cantidad: new FormControl<number | null>(null, [Validators.required, Validators.min(1)])
  });

  public items = signal<InventarioDTO[]>([]);

  public validationMessage: string | null = null;
  public deleteMessage: string | null = null;

  public editingSku: string | null = null;

  constructor(private api: ApiService) { }

  ngOnInit(): void {
    this.load();
  }

  public load() {
    this.api.listInventario().subscribe({
      next: (data) => { this.items.set(data) },
      error: (err) => console.error('Error cargando inventario', err),
    });
  }

  public addItem() {

    if (this.inventarioForm.invalid) {
      this.inventarioForm.markAsUntouched();
      this.validationMessage = 'Por favor complete todos los campos para continuar';
      return;
    }

    const formValue = this.inventarioForm.value;

    if (!formValue.nombre || formValue.cantidad == null) return;
    if (!this.validateNoDigits(formValue.nombre)) {
      console.error('Validación: Nombre no puede contener números');
      return;
    }

    const payload: InventarioDTO = {
      nombre: formValue.nombre!.trim(),
      cantidad: Number(formValue.cantidad),
    };
    this.api.createInventario(payload).subscribe({
      next: () => {
        this.load();
        this.resetForm();
        this.validationMessage = null;
      },
      error: (err) => console.error('Error creando inventario', err),
    });
  }

  public editItem(item: InventarioDTO) {
    this.editingSku = item.sku ?? null;
    this.inventarioForm.patchValue({
      nombre: item.nombre,
      cantidad: item.cantidad
    });
  }

  public updateItem() {
    if (this.inventarioForm.invalid) {
      this.inventarioForm.markAsUntouched();
      this.validationMessage = 'Por favor complete todos los campos para continuar';
      return;
    }

    if (this.editingSku == null) return;

    const sku = this.editingSku;
    const formValue = this.inventarioForm.value;

    if (!this.validateNoDigits(formValue.nombre)) {
      console.error('Validación: Nombre no puede contener números');
      return;
    }

    const payload: InventarioDTO = {
      nombre: String(formValue.nombre),
      cantidad: Number(formValue.cantidad)
    };
    this.api.updateInventario(sku, payload).subscribe({
      next: () => {
        this.cancelEdit();
        this.load();
        this.validationMessage = null;
      },
      error: (err) => console.error('Error actualizando inventario', err),
    });
  }

  public deleteItem(sku: string) {
    this.api.deleteInventario(sku).subscribe({
      next: () => {
        this.load();
        this.deleteMessage = null;
      },
      error: (err) => {
        console.error('Error eliminando inventario', err);
        this.deleteMessage = 'No se puede eliminar: esta asociado a una poliza';
        setTimeout(() => this.deleteMessage = null, 6000);
      },
    });
  }

  public cancelEdit() {
    this.editingSku = null;
    this.resetForm();
  }

  public resetForm() {
    this.inventarioForm.reset({ nombre: '', cantidad: undefined })
  }

  private validateNoDigits(value?: string | null): boolean {
    if (!value) return false;
    return /^[^0-9]+$/.test(value.trim());
  }
}
