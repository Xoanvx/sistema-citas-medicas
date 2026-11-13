import { Component, EventEmitter, OnInit, Output, inject } from '@angular/core';
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
  selector: 'app-paciente-buscar',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './paciente-buscar.html',
  styleUrl: './paciente-buscar.css',
})
export class PacienteBuscar implements OnInit {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/pacientes';

  @Output() filtrarDni = new EventEmitter<string>();

  pacientes: Paciente[] = [];
  pacientesFiltrados: Paciente[] = [];

  buscarForm: FormGroup = this.fb.group({
    dni: ['', [Validators.required, Validators.pattern(/^\d{1,8}$/)]],
  });

  ngOnInit(): void {
    this.cargarPacientes();
  }

  cargarPacientes(): void {
    this.http.get<Paciente[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.pacientes = data;
        this.pacientesFiltrados = data;
      },
      error: (err) => console.error('Error al listar pacientes:', err),
    });
  }

  onSubmit(): void {
    if (this.buscarForm.invalid) {
      this.buscarForm.markAllAsTouched();
      return;
    }

    const dniBuscado = String(this.buscarForm.value.dni ?? '').trim();
    this.filtrarDni.emit(dniBuscado);
    this.pacientesFiltrados = this.pacientes.filter((p) =>
      p.dni?.includes(dniBuscado)
    );
  }

  limpiar(): void {
    this.buscarForm.reset();
    this.pacientesFiltrados = [...this.pacientes];
    this.filtrarDni.emit('');
  }
}
