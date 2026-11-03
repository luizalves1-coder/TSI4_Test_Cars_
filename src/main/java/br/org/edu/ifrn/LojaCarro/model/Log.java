package br.org.edu.ifrn.LojaCarro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long idUsuario;
    Long idCarro;
    LocalDate horario;
    String operacao;
    String parametros;


}
