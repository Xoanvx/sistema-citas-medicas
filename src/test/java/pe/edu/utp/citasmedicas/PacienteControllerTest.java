package pe.edu.utp.citasmedicas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.citasmedicas.model.Paciente;
import pe.edu.utp.citasmedicas.repository.PacienteRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PacienteRepository pacienteRepository;

    private Long idPaciente1;
    private Long idPaciente2;

    @BeforeEach
    void setUp() {
        pacienteRepository.deleteAll();
        Paciente p1 = pacienteRepository.save(new Paciente(null, "Juan Perez", "12345678", "juan@test.com"));
        Paciente p2 = pacienteRepository.save(new Paciente(null, "Maria Lopez", "87654321", "maria@test.com"));
        idPaciente1 = p1.getId();
        idPaciente2 = p2.getId();
    }

    // --- GET /pacientes ---
    @Test
    void testListarTodos_Exitoso() throws Exception {
        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testListarTodos_ValidaContenido() throws Exception {
        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Perez"))
                .andExpect(jsonPath("$[0].dni").value("12345678"));
    }

    @Test
    void testListarTodos_MetodoInvalido_Fallido() throws Exception {
        mockMvc.perform(patch("/pacientes"))
                .andExpect(status().isMethodNotAllowed());
    }

    // --- GET /pacientes/{id} ---
    @Test
    void testBuscarPorId_Exitoso() throws Exception {
        mockMvc.perform(get("/pacientes/" + idPaciente1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    void testBuscarPorId_SegundoPaciente_Exitoso() throws Exception {
        mockMvc.perform(get("/pacientes/" + idPaciente2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maria Lopez"))
                .andExpect(jsonPath("$.correo").value("maria@test.com"));
    }

    @Test
    void testBuscarPorId_NoExiste_Fallido() throws Exception {
        mockMvc.perform(get("/pacientes/999999"))
                .andExpect(status().isNotFound());
    }

    // --- POST /pacientes ---
    @Test
    void testCrear_Exitoso() throws Exception {
        String json = "{\"nombre\":\"Carlos\",\"dni\":\"44556677\",\"correo\":\"carlos@test.com\"}";
        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    @Test
    void testCrear_ValidarNombreRetornado_Exitoso() throws Exception {
        String json = "{\"nombre\":\"Ana\",\"dni\":\"11223344\",\"correo\":\"ana@test.com\"}";
        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.dni").value("11223344"));
    }

    @Test
    void testCrear_DniVacio_Fallido() throws Exception {
        String jsonInvalido = "{\"nombre\":\"Sin Dni\",\"dni\":\"\",\"correo\":\"error@test.com\"}";
        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    // --- PUT /pacientes/{id} ---
    @Test
    void testActualizar_Exitoso() throws Exception {
        String jsonActualizar = "{\"nombre\":\"Juan Actualizado\",\"dni\":\"12345678\",\"correo\":\"juan_nuevo@test.com\"}";
        mockMvc.perform(put("/pacientes/" + idPaciente1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonActualizar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"))
                .andExpect(jsonPath("$.correo").value("juan_nuevo@test.com"));
    }

    @Test
    void testActualizar_NoExiste_Fallido() throws Exception {
        String jsonActualizar = "{\"nombre\":\"No Existe\",\"dni\":\"99999999\",\"correo\":\"noexiste@test.com\"}";
        mockMvc.perform(put("/pacientes/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonActualizar))
                .andExpect(status().isNotFound());
    }

    // --- DELETE /pacientes/{id} ---
    @Test
    void testEliminar_Exitoso() throws Exception {
        mockMvc.perform(delete("/pacientes/" + idPaciente1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminar_SegundoPaciente_Exitoso() throws Exception {
        mockMvc.perform(delete("/pacientes/" + idPaciente2))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminar_NoExiste_Fallido() throws Exception {
        mockMvc.perform(delete("/pacientes/999999"))
                .andExpect(status().isNotFound());
    }
}
