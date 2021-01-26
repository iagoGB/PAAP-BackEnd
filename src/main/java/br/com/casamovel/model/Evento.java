package br.com.casamovel.model;


import br.com.casamovel.dto.evento.NovoEventoDTO;
import br.com.casamovel.repository.PalestranteRepository;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Builder
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

	@OneToMany(mappedBy = "eventoID") // acho que aqui é evento ID
	List<EventoUsuario> usuarios = new ArrayList<EventoUsuario>();

	@OneToMany(mappedBy = "evento_id", cascade = { CascadeType.ALL })
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
		//TODO - Verificar se o relacionamento esta realmente funcional em ambos os lados
		setPalestrantes(
			eDto.getPalestrantes().stream()
			.map(nome -> this.findPalestrante(nome, pr))
			.map(palestrante -> this.setRelationship(palestrante))
			.collect(Collectors.toList())
		);
	}	

	public static Evento parseFrom(NovoEventoDTO eDto, Categoria categoria, PalestranteRepository pr) {
		var evento = new Evento();
        evento.setFoto("default.png");
        evento.setTitulo(eDto.getTitulo());
        evento.setCategoria(categoria);
        evento.setCargaHoraria(eDto.getCarga_horaria());
        evento.setLocal(eDto.getLocal());
        evento.setDataHorario(eDto.getData_horario());
        evento.setEstaAberto(true);
        categoria.getEventos().add(evento);
		//Relação palestrante e evento
		//TODO - Verificar se o relacionamento esta realmente funcional em ambos os lados
		evento.setPalestrantes(
			eDto.getPalestrantes().stream()
			.map(nome -> evento.findPalestrante(nome, pr))
			.map(palestrante -> evento.setRelationship(palestrante))
			.collect(Collectors.toList())
		);
		return evento;
	}	

	private Palestrante findPalestrante(String nome, PalestranteRepository pr){
		return pr.findByNome(nome).orElseThrow(() -> new RuntimeException(String.format("Palestrante %s não encontrado(a)", nome)));

	}
	private EventoPalestrante setRelationship(Palestrante palestrante){
		return new EventoPalestrante(palestrante,this);
	}
	// this.palestrantes.add(ep);
	// palestrante.getEventos().add(ep);
}
