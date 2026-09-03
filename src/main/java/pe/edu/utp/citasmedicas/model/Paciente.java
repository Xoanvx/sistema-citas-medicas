package pe.edu.utp.citasmedicas.model;

public class Paciente {
    private Long id;
    private String nombre;
    private String dni;
    private String correo;

    public Paciente() {}
    public Paciente(Long id, String nombre, String dni, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.correo = correo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
