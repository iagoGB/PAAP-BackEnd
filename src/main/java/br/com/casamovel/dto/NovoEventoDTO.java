package br.com.casamovel.dto;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;


public class NovoEventoDTO {
	@NotNull @NotEmpty
	private Long categoria;
	@NotNull @NotEmpty
	private String titulo;
	@NotNull @NotEmpty @PastOrPresent
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT-3")
	private LocalDateTime data_horario;
	@NotNull @NotEmpty
	private String local;
	@NotNull @NotEmpty
	private int carga_horaria;
	@NotNull @NotEmpty
	private String[] palestrantes;

	public Long getCategoria() {
		return categoria;
	}

	public void setCategoria(Long categoria) {
		this.categoria = categoria;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}


	public String[] getPalestrantes() {
		return palestrantes;
	}

	public void setPalestrantes(String[] palestrantes) {
		this.palestrantes = palestrantes;
	}

	public LocalDateTime getData_horario() {
		return data_horario;
	}

	public void setData_horario(LocalDateTime data_horario) {
		this.data_horario = data_horario;
	}

	public int getCarga_horaria() {
		return carga_horaria;
	}

	public void setCarga_horaria(int carga_horaria) {
		this.carga_horaria = carga_horaria;
	}

	@Override
	public String toString() {
		return "NovoEventoDTO [categoria=" + categoria + ", titulo=" + titulo + ", data_horario=" + data_horario
				+ ", local=" + local + ", carga_horaria=" + carga_horaria + ", palestrantes="
				+ Arrays.toString(palestrantes) + "]";
	}
	
	
	
	
	
	
}
