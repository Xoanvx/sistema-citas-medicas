import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Paciente {
  id: number;
  nombre: string;
  apellidos?: string;
  dni: string;
  correo: string;
}

@Component({
  selector: 'app-paciente-editar',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './paciente-editar.html',
  styleUrl: './paciente-editar.css',
})
export class PacienteEditar implements OnInit {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/pacientes';

  @Input() pacienteId?: number;
  mensaje = '';
  error = '';

  editarForm: FormGroup = this.fb.group({
    id: [null, [Validators.required, Validators.min(1)]],
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    apellidos: ['', [Validators.required, Validators.minLength(2)]],
    dni: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
    correo: ['', [Validators.required, Validators.email]],
  });

  ngOnInit(): void {
    if (this.pacienteId) {
      this.editarForm.patchValue({ id: this.pacienteId });
      this.cargarPaciente(this.pacienteId);
    }
  }

  cargarPaciente(idParam?: number): void {
    const id = idParam ?? this.editarForm.get('id')?.value;
    if (!id) {
      return;
    }

    this.mensaje = '';
    this.error = '';

    this.http.get<Paciente>(`${this.apiUrl}/${id}`).subscribe({
      next: (paciente) => {
        this.editarForm.patchValue({
          id: paciente.id,
          nombre: paciente.nombre,
          apellidos: paciente.apellidos ?? '',
          dni: paciente.dni,
          correo: paciente.correo,
        });
      },
      error: () => {
        this.error = 'No se encontró un paciente con ese ID.';
      },
    });
  }

  onSubmit(): void {
    if (this.editarForm.invalid) {
      this.editarForm.markAllAsTouched();
      return;
    }

    const { id, ...payload } = this.editarForm.value;
    this.mensaje = '';
    this.error = '';

    this.http.put<Paciente>(`${this.apiUrl}/${id}`, payload).subscribe({
      next: () => {
        this.mensaje = 'Datos del paciente actualizados correctamente.';
      },
      error: () => {
        this.error = 'Error al actualizar los datos del paciente.';
      },
    });
  }
}
