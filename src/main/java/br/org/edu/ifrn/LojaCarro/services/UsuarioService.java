package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.Usuario;
import br.org.edu.ifrn.LojaCarro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario update(Usuario novo, Long id) {
        Usuario antigo =  usuarioRepository.findById(id).orElse(null);
        antigo.setNome(novo.getNome());
        antigo.setCargo(novo.getCargo());
        antigo.setSalario(novo.getSalario());

        return usuarioRepository.save(antigo);
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).get();
    }
}
