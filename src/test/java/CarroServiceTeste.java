
package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.CarroException;
import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.model.Log;
import br.org.edu.ifrn.LojaCarro.repository.CarroRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarroServiceTest {

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private LogService logService;

    @InjectMocks
    private CarroService carroService;

    // Cria um carro válido para os testes
    private Carro criarCarro() {
        Carro carro = new Carro("Fit", 2024, 80000.0);
        carro.setId(1L);
        return carro;
    }

    // 1. Testar salvamento do carro
    @Test
    void deveSalvarCarroERegistrarLog() {
        Carro carro = criarCarro();

        when(carroRepository.save(any(Carro.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Carro resultado = carroService.save(carro);

        assertNotNull(resultado);
        assertEquals("Fit", resultado.getModelo());
        assertEquals(80000.0, resultado.getPreco());

        verify(carroRepository).save(carro);
        verify(logService).save(any(Log.class));
    }

    // 2. Testar exclusão por ID
    @Test
    void deveDeletarCarroPorId() {
        carroService.deleteById(1L);

        verify(carroRepository).deleteById(1L);
    }


    // 3. Testar pesquisa por ID
    @Test
    void deveBuscarCarroPorId() {
        Carro carro = criarCarro();

        when(carroRepository.findById(1L))
                .thenReturn(Optional.of(carro));

        Optional<Carro> resultado = carroService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Fit", resultado.get().getModelo());
    }

    // 4. Testar listagem de carros
    @Test
    void deveListarTodosOsCarros() {
        List<Carro> carros = List.of(criarCarro());

        when(carroRepository.findAll()).thenReturn(carros);

        List<Carro> resultado = carroService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Fit", resultado.get(0).getModelo());

        verify(carroRepository).findAll();
    }

    // 5. Testar modelo vazio
    @Test
    void deveLancarExcecaoParaModeloVazio() {
        assertThrows(CarroException.class,
                () -> carroService.findByModelo(""));

        assertThrows(CarroException.class,
                () -> carroService.findByModelo("   "));

        assertThrows(CarroException.class,
                () -> carroService.findByModelo(null));

        verify(carroRepository, never())
                .findFirstByModelo(anyString());
    }

    // 6. Testar atualização por ID
    @Test
    void deveAtualizarCarro() {
        Carro carro = criarCarro();
        carro.setPreco(85000.0);

        when(carroRepository.existsById(1L)).thenReturn(true);
        when(carroRepository.save(any(Carro.class)))
                .thenReturn(carro);

        Carro resultado = carroService.update(carro);

        assertNotNull(resultado);
        assertEquals(85000.0, resultado.getPreco());

        verify(carroRepository).existsById(1L);
        verify(carroRepository).save(carro);
    }
}
