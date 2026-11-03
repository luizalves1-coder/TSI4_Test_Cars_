
package br.org.edu.ifrn.LojaCarro.controllers;
import br.org.edu.ifrn.LojaCarro.model.Usuario;
import br.org.edu.ifrn.LojaCarro.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    // Cria um usuário para os testes
    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        // Ajuste os campos abaixo conforme sua classe Usuario
        // Exemplo:
        // usuario.setNome("Maria");
        // usuario.setEmail("maria@email.com");

        return usuario;
    }

    @Test
    void deveListarUsuarios() throws Exception {
        when(usuarioService.listar())
                .thenReturn(List.of(criarUsuario()));

        mockMvc.perform(get("/usuario/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(usuarioService).listar();
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        when(usuarioService.findById(1L))
                .thenReturn(criarUsuario());

        mockMvc.perform(get("/usuario/findById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(usuarioService).findById(1L);
    }

    @Test
    void deveSalvarUsuario() throws Exception {
        Usuario usuario = criarUsuario();

        when(usuarioService.save(any(Usuario.class)))
                .thenReturn(usuario);

        mockMvc.perform(post("/usuario/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(usuarioService).save(any(Usuario.class));
    }

    @Test
    void deveDeletarUsuario() throws Exception {
        doNothing().when(usuarioService).deleteById(1L);

        mockMvc.perform(delete("/usuario/delete/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService).deleteById(1L);
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        Usuario usuario = criarUsuario();

        when(usuarioService.update(any(Usuario.class), eq(1L)))
                .thenReturn(usuario);

        mockMvc.perform(put("/usuario/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(usuarioService).update(any(Usuario.class), eq(1L));
    }
}
