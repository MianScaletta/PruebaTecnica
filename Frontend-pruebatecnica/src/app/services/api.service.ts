import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

const API_BASE = 'http://localhost:8080/api';

export interface EmpleadoDTO {
    idEmpleado?: string;
    nombre: string;
    apellido: string;
    puesto: string;
}

export interface InventarioDTO {
    sku?: string;
    nombre: string;
    cantidad: number;
}

export interface PolizaDTO {
    idPoliza?: string;
    idEmpleado?: string;
    sku: string;
    cantidad: number;
    fecha?: string;
}

interface ApiResponse<T> {
    meta?: any;
    data?: T;
}

function extractData<T>(res: ApiResponse<T> | any): T {
    if (!res) return res as T;

    const payload = (res as any).data;

    if (payload && typeof payload === 'object') {
        const keys = Object.keys(payload);
        if (keys.length === 1) {
            return payload[keys[0]] as T;
        }

        const candidates = ['Empleados', 'Polizas', 'Inventarios', 'EmpleadoDTO', 'PolizaDTO', 'data'];
        for (const k of candidates) {
            if (payload[k] !== undefined) return payload[k] as T;
        }
    }

    return payload as T;
}

function normalizePoliza(raw: any): PolizaDTO {
    if (!raw) return raw as PolizaDTO;

    if (raw.Poliza) raw = raw.Poliza;
    if (raw.data && Object.keys(raw).length === 1) raw = raw.data;

    const idPoliza = raw.idPolizas;
    const idEmpleado = raw.empleadoGenero;
    const sku = raw.sku;
    const cantidad = raw.cantidad;
    const fecha = raw.fecha;

    return {
        idPoliza: idPoliza,
        idEmpleado: idEmpleado,
        sku: sku,
        cantidad: cantidad,
        fecha: fecha,
    } as PolizaDTO;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
    constructor(private http: HttpClient) { }

    /* Empleado */
    listEmpleados(): Observable<EmpleadoDTO[]> {
        return this.http.get<ApiResponse<EmpleadoDTO[]>>(`${API_BASE}/empleados`).pipe(
            map(res => extractData<EmpleadoDTO[]>(res)),
            catchError(this.handleError)
        );
    }

    createEmpleado(e: EmpleadoDTO): Observable<EmpleadoDTO> {
        return this.http.post<ApiResponse<EmpleadoDTO>>(`${API_BASE}/empleados`, e).pipe(
            map(res => extractData<EmpleadoDTO>(res)),
            catchError(this.handleError)
        );
    }

    updateEmpleado(id: string, e: EmpleadoDTO): Observable<EmpleadoDTO> {
        return this.http.put<ApiResponse<EmpleadoDTO>>(`${API_BASE}/empleados/${id}`, e).pipe(
            map(res => extractData<EmpleadoDTO>(res)),
            catchError(this.handleError)
        );
    }

    deleteEmpleado(id: string): Observable<void> {
        return this.http.delete<ApiResponse<any>>(`${API_BASE}/empleados/${id}`).pipe(
            map(() => undefined),
            catchError(this.handleError)
        );
    }

    /* Inventario */
    listInventario(): Observable<InventarioDTO[]> {
        return this.http.get<ApiResponse<InventarioDTO[]>>(`${API_BASE}/inventarios`).pipe(
            map(res => extractData<InventarioDTO[]>(res)),
            catchError(this.handleError)
        );
    }

    createInventario(i: InventarioDTO): Observable<InventarioDTO> {
        return this.http.post<ApiResponse<InventarioDTO>>(`${API_BASE}/inventarios`, i).pipe(
            map(res => extractData<InventarioDTO>(res)),
            catchError(this.handleError)
        );
    }

    updateInventario(sku: string, i: InventarioDTO): Observable<InventarioDTO> {
        return this.http.put<ApiResponse<InventarioDTO>>(`${API_BASE}/inventarios/${sku}`, i).pipe(
            map(res => extractData<InventarioDTO>(res)),
            catchError(this.handleError)
        );
    }

    deleteInventario(sku: string): Observable<void> {
        return this.http.delete<ApiResponse<any>>(`${API_BASE}/inventarios/${sku}`).pipe(
            map(() => undefined),
            catchError(this.handleError)
        );
    }

    /* Polizas */
    listPolizas(): Observable<PolizaDTO[]> {
        return this.http.get<ApiResponse<any[]>>(`${API_BASE}/polizas`).pipe(
            map(res => extractData<any[]>(res)),
            map(arr => (arr || []).map(normalizePoliza)),
            catchError(this.handleError)
        );
    }

    createPoliza(p: PolizaDTO): Observable<PolizaDTO> {
        const payload: any = {
            empleadoGenero: p.idEmpleado,
            sku: p.sku,
            cantidad: p.cantidad,
            fecha: p.fecha,
        };
        return this.http.post<ApiResponse<any>>(`${API_BASE}/polizas`, payload).pipe(
            map(res => extractData<any>(res)),
            map(obj => normalizePoliza(obj)),
            catchError(this.handleError)
        );
    }

    updatePoliza(id: string, p: PolizaDTO): Observable<PolizaDTO> {
        const payload: any = {
            empleadoGenero: p.idEmpleado,
            sku: p.sku,
            cantidad: p.cantidad,
            fecha: p.fecha,
        };
        return this.http.put<ApiResponse<any>>(`${API_BASE}/polizas/${id}`, payload).pipe(
            map(res => extractData<any>(res)),
            map(obj => normalizePoliza(obj)),
            catchError(this.handleError)
        );
    }

    deletePoliza(id: string): Observable<void> {
        return this.http.delete<ApiResponse<any>>(`${API_BASE}/polizas/${id}`).pipe(
            map(() => undefined),
            catchError(this.handleError)
        );
    }

    private handleError(error: HttpErrorResponse) {
        const apiBody: any = error.error;
        let message = error.message || 'Unknown error';

        const tryExtractMessage = (obj: any): string | null => {
            if (!obj) return null;
            if (typeof obj === 'string') return obj;
            const candidates = ['idmensaje'];
            for (const key of candidates) {
                if (obj[key] != null) return String(obj[key]);
            }
            const keys = Object.keys(obj || {});
            if (keys.length === 1 && typeof obj[keys[0]] === 'string') return obj[keys[0]];
            return null;
        };

        if (apiBody) {
            const payload = apiBody.data;
            const mensajeObj = payload?.Mensaje;
            const extracted = tryExtractMessage(mensajeObj);
            if (extracted) {
                message = extracted;
            } else {
                try {
                    message = JSON.stringify(apiBody);
                } catch (e) {
                    /* ignore */
                }
            }
        }

        console.error('API Error', { httpError: error, message });
        return throwError(() => new Error(message));
    }
}
