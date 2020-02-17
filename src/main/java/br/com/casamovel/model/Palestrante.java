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

@Entity
public class Palestrante implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String nome;
	private String foto; 
	private String descricao;
	
	@OneToMany(mappedBy = "nome_palestrante_id", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private List<EventoPalestrante> eventos = new ArrayList<>();
	
	public Palestrante() {
		
	}

	public Palestrante(Long id, String foto) {
		super();
		this.id = id;
		this.foto = foto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Palestrante other = (Palestrante) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<EventoPalestrante> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventoPalestrante> eventos) {
		this.eventos = eventos;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void parse(NovoPalestranteDTO pDto) {
		
        setFoto("http://localhost:9999/foto-default");
        setDescricao(pDto.getDescricao());
        setNome(pDto.getNome());
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
	
}
