package br.com.casamovel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import br.com.casamovel.dto.palestrante.NovoPalestranteDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter 
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Palestrante implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String foto; 
	private String descricao;
	
	@OneToMany(mappedBy = "nome_palestrante_id", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private List<EventoPalestrante> eventos = new ArrayList<>();
	

	public Palestrante(Long id, String foto) {
		super();
		this.id = id;
		this.foto = foto;
	}

	public void parse(NovoPalestranteDTO pDto) {
        setFoto("http://localhost:9999/foto-default");
        setDescricao(pDto.getDescricao());
        setNome(pDto.getNome());
	}

	
}
