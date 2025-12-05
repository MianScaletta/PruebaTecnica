import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, PolizaDTO } from '../services/api.service';
import { EmpleadoDTO, InventarioDTO } from '../services/api.service';
import { FormGroup, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-polizas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './polizas.html',
  styleUrls: ['./polizas.css'],
})
export class Polizas implements OnInit {
  polizaForm = new FormGroup({
    idEmpleado: new FormControl<string | null>(null, [Validators.required]),
    sku: new FormControl<string | null>(null, [Validators.required]),
    cantidad: new FormControl<number | null>(null, [Validators.required, Validators.min(1)]),
    fecha: new FormControl<string | null>(null, [Validators.required])
  });


  public items = signal<PolizaDTO[]>([]);
  public empleados = signal<EmpleadoDTO[]>([]);
  public inventarios = signal<InventarioDTO[]>([]);
  public validationMessage: string | null = null;

  public editingId: string | null = null;

  constructor(private api: ApiService) { }

  ngOnInit(): void {
    this.load();
    this.loadEmpleadoAndInventario();
  }

  public load() {
    this.api.listPolizas().subscribe({
      next: (data) => { this.items.set(data) },
      error: (err) => console.error('Error cargando pólizas', err),
    });
  }

  public loadEmpleadoAndInventario() {
    this.api.listEmpleados().subscribe({
      next: (data) => (this.empleados.set(data)),
      error: (err) => console.error('Error cargando empleados', err),
    });

    this.api.listInventario().subscribe({
      next: (data) => (this.inventarios.set(data)),
      error: (err) => console.error('Error cargando inventario', err),
    });
  }

  public addPoliza() {
    if (this.polizaForm.invalid) {
      this.polizaForm.markAsUntouched();
      this.validationMessage = 'Por favor complete todos los campos para continuar';
      return;
    }

    const formValue = this.polizaForm.value;

    if (this.isCantidadExceedsStockReactive()) {
      const msg = 'Cantidad excede stock disponible (' + this.getStockForSku(formValue.sku) + ')';
      this.validationMessage = msg;
      alert(msg)
      try { window.alert(msg); } catch (e) { /* ignore */ }
      return;
    }


    const payload: PolizaDTO = {
      idEmpleado: String(formValue.idEmpleado),
      sku: String(formValue.sku),
      cantidad: Number(formValue.cantidad),
      fecha: new Date(String(formValue.fecha)).toISOString(),
    };

    this.api.createPoliza(payload).subscribe({
      next: () => {
        this.load();
        this.resetForm();
        this.validationMessage = null;
      },
      error: (err) => console.error('Error creando póliza', err),
    });
  }

  public editPoliza(item: PolizaDTO) {
    this.editingId = item.idPoliza ?? null;
    this.polizaForm.patchValue({
      idEmpleado: item.idEmpleado,
      sku: item.sku,
      cantidad: item.cantidad,
      fecha: item.fecha ? item.fecha : null
    });
  }

  public updatePoliza() {

    if (this.polizaForm.invalid) {
      this.polizaForm.markAsUntouched();
      this.validationMessage = 'Por favor complete todos los campos para continuar';
      return;
    }


    if (this.editingId == null) return;

    const id = this.editingId;

    const formValue = this.polizaForm.value;

    if (this.isCantidadExceedsStockReactive()) {
      const msg = 'Cantidad excede stock disponible (' + this.getStockForSku(formValue.sku) + ')';
      this.validationMessage = msg;
      try { window.alert(msg); } catch (e) { }
      return;
    }

    const payload: PolizaDTO = {
      idEmpleado: String(formValue.idEmpleado),
      sku: String(formValue.sku),
      cantidad: Number(formValue.cantidad),
      fecha: formValue.fecha!,
    };

    this.api.updatePoliza(id, payload).subscribe({
      next: () => {
        this.cancelEdit();
        this.load();
        this.validationMessage = null;
      },
      error: (err) => console.error('Error actualizando póliza', err),
    });
  }

  public deletePoliza(id: string) {
    this.api.deletePoliza(id).subscribe({
      next: () => { this.load() },
      error: (err) => console.error('Error eliminando póliza', err),
    });
  }

  public cancelEdit() {
    this.editingId = null;
    this.resetForm();
  }

  public resetForm() {
    this.polizaForm.reset();
  }

  public getStockForSku(sku?: string | null): number {
    if (!sku) return 0;
    const found = this.inventarios().find(i => String(i.sku) === String(sku));
    return found ? Number(found.cantidad) : 0;
  }

  public empleadoNombre(id?: string | null): string {
    if (!id) return '';
    const found = this.empleados().find(e => String(e.idEmpleado) === String(id));
    if (!found) return String(id);
    return `${found.nombre}${found.apellido ? ' ' + found.apellido : ''}`.trim();
  }

  public inventarioNombre(sku?: string | null): string {
    if (!sku) return '';
    const found = this.inventarios().find(i => String(i.sku) === String(sku));
    if (!found) return String(sku);
    return found.nombre ?? String(sku);
  }

  public isCantidadExceedsStockReactive(): boolean {
    const sku = this.polizaForm.get('sku')?.value;
    const cantidad = this.polizaForm.get('cantidad')?.value;

    if (!sku || !cantidad) return false;

    const stock = this.getStockForSku(sku);
    const qty = Number(cantidad);

    if (isNaN(qty)) return false;
    return qty > stock;
  }
}
