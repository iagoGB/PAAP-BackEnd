package br.com.casamovel.dto.palestrante;

import java.util.ArrayList;
import java.util.List;

import br.com.casamovel.model.Palestrante;

public class DetalhePalestranteDTO {
	Long id;
	String nome;
	String desc;
	String foto;
	List<String> eventosLista = new ArrayList<>();
	
	public DetalhePalestranteDTO(Long id, String nome, String desc, String foto, List<String> listaEventos) {
		this.id = id;
		this.nome = nome;
		this.desc = desc;
		this.foto = foto;
		this.eventosLista = listaEventos;
	}
	
	public DetalhePalestranteDTO(Palestrante palestrante) {
		this.id = palestrante.getId();
		this.foto = palestrante.getFoto();
		this.nome = palestrante.getNome();
		this.desc = palestrante.getDescricao();
		this.eventosLista = this.convertListToString(palestrante);
		System.out.println("exec: " + this.eventosLista.toString());
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public List<String> getEventosLista() {
		return eventosLista;
	}
	public void setEventosLista(List<String> eventosLista) {
		this.eventosLista = eventosLista;
	}

	private List<String> convertListToString(Palestrante palestrante) {
		
		List<String> result = new ArrayList<>();
		palestrante.getEventos().forEach(p -> {
			result.add(p.getEvento_id().getTitulo());
		});
		System.out.println("Passou pelo conversor lista de String");
		return result;
	}
	
	public static DetalhePalestranteDTO parse(Palestrante palestrante) {
		return new DetalhePalestranteDTO(palestrante);
	}
	
	
	
}
