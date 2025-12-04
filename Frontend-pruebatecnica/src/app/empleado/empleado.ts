
import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService, EmpleadoDTO } from '../services/api.service';

@Component({
  selector: 'app-empleado',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './empleado.html',
  styleUrls: ['./empleado.css'],
})
export class Empleado implements OnInit {

  empleadoForm = new FormGroup({
    nombre: new FormControl<string | null>(null, [Validators.required]),
    apellido: new FormControl<string | null>(null, [Validators.required]),
    puesto: new FormControl<string | null>(null, [Validators.required]),
  });

  public items = signal<EmpleadoDTO[]>([]);

  public validationMessage: string | null = null;
  public deleteMessage: string | null = null;

  public editingId: string | null = null;

  constructor(private api: ApiService) { }

  ngOnInit(): void {
    this.load();
  }

  public load() {
    this.api.listEmpleados().subscribe({
      next: (data) => { this.items.set(data) },
      error: (err) => console.error('Error cargando empleados', err),
    });
  }

  public addItem() {

    if (this.empleadoForm.invalid) {
      this.empleadoForm.markAsUntouched();
      this.validationMessage = 'Por favor complete todos los campos para continuar';
      return;
    }

    const formValue = this.empleadoForm.value;


    if (!formValue.nombre || !formValue.apellido || !formValue.puesto) return;
    if (!this.validateNoDigits(formValue.nombre) || !this.validateNoDigits(formValue.apellido) || !this.validateNoDigits(formValue.puesto)) {
      console.error('Validación: Los campos no pueden contener números');
      return;
    }
    const newModel: EmpleadoDTO = {
      nombre: formValue.nombre!.trim(),
      apellido: formValue.apellido!.trim(),
      puesto: formValue.puesto!.trim(),
    };
    this.api.createEmpleado(newModel).subscribe({
      next: () => {
        this.load();
        this.resetForm();
        this.validationMessage = null;
      },
      error: (err) => console.error('Error creando empleado', err),
    });
  }

  public editItem(item: EmpleadoDTO) {
    this.editingId = item.idEmpleado ?? null;
    this.empleadoForm.patchValue({
      nombre: item.nombre,
      apellido: item.apellido,
      puesto: item.puesto
    });
  }

  public updateItem() {
    if (this.empleadoForm.invalid) {
      this.empleadoForm.markAsUntouched();
      this.validationMessage = 'Por favor complete todos los campos para continuar';
      return;
    }

    const formModel = this.empleadoForm.value;

    if (this.editingId == null) return;
    const id = this.editingId;
    const formValue = this.empleadoForm.value;
    if (!this.validateNoDigits(formModel.nombre) || !this.validateNoDigits(formModel.apellido) || !this.validateNoDigits(formModel.puesto)) {
      console.error('Validación: Los campos no pueden contener números');
      return;
    }

    const payload: EmpleadoDTO = {
      nombre: String(formValue.nombre),
      apellido: String(formValue.apellido),
      puesto: String(formValue.puesto)
    };
    this.api.updateEmpleado(id, payload).subscribe({
      next: () => {
        this.cancelEdit();
        this.load();
        this.validationMessage = null;
      },
      error: (err) => console.error('Error actualizando empleado', err),
    });
  }

  public deleteItem(id?: string) {
    this.api.deleteEmpleado(id!).subscribe({
      next: () => {
        this.load();
        this.deleteMessage = null;
      },
      error: (err) => {
        console.error('Error eliminando empleado', err);
        this.deleteMessage = 'No se puede eliminar: esta asociado a una poliza';
        setTimeout(() => this.deleteMessage = null, 6000);
      },
    });
  }

  public cancelEdit() {
    this.editingId = null;
    this.resetForm();
  }

  public resetForm() {
    this.empleadoForm.reset({ nombre: '', apellido: '', puesto: '' })
  }

  private validateNoDigits(value?: string | null): boolean {
    if (!value) return false;
    return /^[^0-9]+$/.test(value.trim());
  }
}
