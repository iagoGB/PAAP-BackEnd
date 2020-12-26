package br.com.casamovel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.casamovel.dto.evento.NovoEventoDTO;
import br.com.casamovel.repository.PalestranteRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.var;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

//@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Evento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String keyword;

	private Boolean estaAberto = true;

	private String titulo;

	private String foto;

	private String qrCode;

	private LocalDateTime dataHorario;

	private String local;

	private Integer cargaHoraria;

	@ManyToOne
	@JoinColumn(name = "fk_categoria_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_categoria_id"), nullable = false)
	private Categoria categoria;

	@OneToMany(mappedBy = "evento_id") // acho que aqui é evento ID
	List<EventoUsuario> usuarios = new ArrayList<EventoUsuario>();

	@OneToMany(mappedBy = "evento_id", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	List<EventoPalestrante> palestrantes = new ArrayList<EventoPalestrante>();

	public void parse(NovoEventoDTO eDto, Categoria categoria, PalestranteRepository pr) {
		
        setFoto("default.png");
        setTitulo(eDto.getTitulo());
        setCategoria(categoria);
        setCargaHoraria(eDto.getCarga_horaria());
        setLocal(eDto.getLocal());
        setDataHorario(eDto.getData_horario());
        setEstaAberto(true);
        categoria.getEventos().add(this);
        //Relação palestrante e evento
        eDto.getPalestrantes().forEach( nome ->  {
        	var palestrante = pr.findByNome(nome);
        	var ep = new EventoPalestrante();
        	ep.setEvento_id(this);
        	ep.setNome_palestrante_id(palestrante);
        	this.palestrantes.add(ep);
        	palestrante.getEventos().add(ep);
        	System.out.println("Terminou de executar  relação de um palestrante com evento");
        	
        });
	}	
}
