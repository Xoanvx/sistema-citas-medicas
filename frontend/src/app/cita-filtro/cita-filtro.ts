import { Component, EventEmitter, OnInit, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Cita {
  id: number;
  pacienteId: number;
  medicoId: number;
  fecha?: string;
  hora?: string;
  estado: string;
}

@Component({
  selector: 'app-cita-filtro',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cita-filtro.html',
  styleUrl: './cita-filtro.css',
})
export class CitaFiltro implements OnInit {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/citas';

  @Output() filtrarFechas = new EventEmitter<{ fechaInicio: string; fechaFin: string }>();

  citas: Cita[] = [];
  citasFiltradas: Cita[] = [];
  mensaje = '';

  filtroForm: FormGroup = this.fb.group({
    fechaInicio: ['', [Validators.required]],
    fechaFin: ['', [Validators.required]],
    pacienteId: [null],
  });

  ngOnInit(): void {
    this.cargarTodas();
  }

  cargarTodas(): void {
    this.http.get<Cita[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.citas = data;
        this.citasFiltradas = data;
      },
      error: (err) => console.error('Error al listar citas:', err),
    });
  }

  onSubmit(): void {
    if (this.filtroForm.invalid) {
      this.filtroForm.markAllAsTouched();
      return;
    }

    const { fechaInicio, fechaFin, pacienteId } = this.filtroForm.value;
    this.filtrarFechas.emit({ fechaInicio, fechaFin });

    const endpoint = pacienteId ? `${this.apiUrl}/paciente/${pacienteId}` : this.apiUrl;
    this.http.get<Cita[]>(endpoint).subscribe({
      next: (data) => {
        this.citasFiltradas = data.filter((cita) => {
          if (!cita.fecha) return false;
          return cita.fecha >= fechaInicio && cita.fecha <= fechaFin;
        });
      },
      error: (err) => console.error('Error al filtrar citas:', err),
    });
  }

  anularCita(id: number): void {
    this.http.delete(`${this.apiUrl}/${id}`).subscribe({
      next: () => {
        this.mensaje = `Cita ${id} anulada correctamente.`;
        this.citasFiltradas = this.citasFiltradas.filter((c) => c.id !== id);
        this.citas = this.citas.filter((c) => c.id !== id);
      },
      error: (err) => console.error('Error al anular cita:', err),
    });
  }
}
