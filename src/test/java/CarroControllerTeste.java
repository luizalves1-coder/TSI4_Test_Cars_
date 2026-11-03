
package br.org.edu.ifrn.LojaCarro.controllers;
import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.services.CarroService;
import br.org.edu.ifrn.LojaCarro.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarroController.class)
class CarroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarroService carroService;

    @MockBean
    private UsuarioService usuarioService;

    private Carro criarCarro() {
        Carro carro = new Carro();
        carro.setId(1L);
        carro.setModelo("Civic");
        carro.setPreco(150000.0);
        return carro;
    }

    @Test
    void deveSalvarCarro() throws Exception {
        Carro carro = criarCarro();

        when(carroService.save(any(Carro.class))).thenReturn(carro);

        mockMvc.perform(post("/carro/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Civic"))
                .andExpect(jsonPath("$.preco").value(150000.0));
    }

    @Test
    void deveAtualizarCarroPorId() throws Exception {
        Carro carro = criarCarro();

        when(carroService.update(any(Carro.class))).thenReturn(carro);

        mockMvc.perform(put("/carro/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Civic"));
    }

    @Test
    void deveDeletarCarroPorId() throws Exception {
        doNothing().when(carroService).deleteById(1L);

        mockMvc.perform(delete("/carro/1"))
                .andExpect(status().isNoContent());

        verify(carroService).deleteById(1L);
    }

    @Test
    void devePesquisarCarroPorId() throws Exception {
        when(carroService.findById(1L))
                .thenReturn(Optional.of(criarCarro()));

        mockMvc.perform(get("/carro/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Civic"));
    }

    @Test
    void deveRetornar404QuandoCarroNaoExiste() throws Exception {
        when(carroService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/carro/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarTodosOsCarros() throws Exception {
        when(carroService.findAll())
                .thenReturn(List.of(criarCarro()));

        mockMvc.perform(get("/carro/listarCarros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].modelo").value("Civic"));
    }

    @Test
    void devePesquisarCarroPorModelo() throws Exception {
        when(carroService.findByModelo("Civic"))
                .thenReturn(Optional.of(criarCarro()));

        mockMvc.perform(post("/carro/getCarro")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Civic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Civic"));
    }

    @Test
    void deveDeletarCarroPorModelo() throws Exception {
        when(carroService.deleteByModelo("Civic"))
                .thenReturn(criarCarro());

        mockMvc.perform(post("/carro/deleteCarro")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Civic"))
                .andExpect(status().isOk())
                .andExpect(content().string("Carro deletado: Civic"));
    }

    @Test
    void deveAtualizarPrecoPorModelo() throws Exception {
        Carro carro = criarCarro();
        carro.setPreco(140000.0);

        when(carroService.updateByModelo("Civic", 140000.0))
                .thenReturn(carro);

        mockMvc.perform(post("/carro/updateCarro")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Civic,140000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preco").value(140000.0));
    }

    @Test
    void deveRetornarErroParaPayloadInvalido() throws Exception {
        mockMvc.perform(post("/carro/updateCarro")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Civic"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void deveRetornarBomDia() throws Exception {
        mockMvc.perform(post("/carro/teste")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Maria"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bom dia, Maria"));
    }
}
