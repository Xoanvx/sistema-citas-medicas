package pe.edu.utp.citasmedicas.model;

public class Cita {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private String estado;

    public Cita() {}
    public Cita(Long id, Long pacienteId, Long medicoId, String estado) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
