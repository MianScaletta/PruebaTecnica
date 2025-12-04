import { Routes } from '@angular/router';
import { Empleado } from './empleado/empleado';
import { Polizas } from './polizas/polizas';
import { Inventario } from './inventario/inventario';
import { Inicio } from './inicio/inicio';

export const routes: Routes = [
    { path: 'inicio', component: Inicio },
    { path: 'empleado', component: Empleado },
    { path: 'polizas', component: Polizas },
    { path: 'inventario', component: Inventario },
    { path: '', redirectTo: '/inicio', pathMatch: 'full' }
];
