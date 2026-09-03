package pe.edu.utp.citasmedicas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(jsonPath("$[0].nombre").value("Juan Perez"));
    }

    @Test
    void testListarTodos_MetodoInvalido_Fallido() throws Exception {
        mockMvc.perform(patch("/pacientes"))
                .andExpect(status().isMethodNotAllowed());
    }

    // --- GET /pacientes/{id} ---
    @Test
    void testBuscarPorId_Exitoso() throws Exception {
        mockMvc.perform(get("/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("12345678"));
    }

    @Test
    void testBuscarPorId_SegundoPaciente_Exitoso() throws Exception {
        mockMvc.perform(get("/pacientes/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maria Lopez"));
    }

    @Test
    void testBuscarPorId_NoExiste_Fallido() throws Exception {
        mockMvc.perform(get("/pacientes/999"))
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
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testCrear_ValidarNombreRetornado_Exitoso() throws Exception {
        String json = "{\"nombre\":\"Ana\",\"dni\":\"11223344\",\"correo\":\"ana@test.com\"}";
        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void testCrear_DniVacio_Fallido() throws Exception {
        String jsonInvalido = "{\"nombre\":\"Sin Dni\",\"dni\":\"\",\"correo\":\"error@test.com\"}";
        mockMvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    // --- DELETE /pacientes/{id} ---
    @Test
    void testEliminar_Exitoso() throws Exception {
        mockMvc.perform(delete("/pacientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminar_SegundoPaciente_Exitoso() throws Exception {
        mockMvc.perform(delete("/pacientes/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminar_NoExiste_Fallido() throws Exception {
        mockMvc.perform(delete("/pacientes/888"))
                .andExpect(status().isNotFound());
    }
}
