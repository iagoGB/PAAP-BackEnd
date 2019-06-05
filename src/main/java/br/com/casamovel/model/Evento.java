package br.com.casamovel.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class Evento {
	
		private int evento_id;
		private int categoria_evento;
		private String titulo;
		private List<String> palestrante;
		private Date data_horario;
		private String localizacao;
		private double carga_horaria;
		private Date criado_em;
		private Date atualizado_em;
		
		public Evento() {
			
		}
		
		public Evento(
			int categoria_evento, 
			String titulo,
			Date data_horario, 
			String localizacao,
			double carga_horaria, 
			Date criado_em, 
			Date atualizado_em) 
		{
			
			this.categoria_evento = categoria_evento;
			this.titulo = titulo;
			this.data_horario = data_horario;
			this.localizacao = localizacao;
			this.carga_horaria = carga_horaria;
			this.criado_em = criado_em;
			this.atualizado_em = atualizado_em;
		}
		
		public int getEvento_id() {
			return evento_id;
		}
		public void setEvento_id(int evento_id) {
			this.evento_id = evento_id;
		}
		public int getCategoria_evento() {
			return categoria_evento;
		}
		public void setCategoria_evento(int categoria_evento) {
			this.categoria_evento = categoria_evento;
		}
		public String getTitulo() {
			return titulo;
		}
		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}
		public List<String> getPalestrante() {
			return palestrante;
		}
		public void setPalestrante(List<String> palestrante) {
			this.palestrante = palestrante;
		}
		public Date getData_horario() {
			return data_horario;
		}
		public void setData_horario(Date data_horario) {
			this.data_horario = data_horario;
		}
		public String getLocalizacao() {
			return localizacao;
		}
		public void setLocalizacao(String localizacao) {
			this.localizacao = localizacao;
		}
		public double getCarga_horaria() {
			return carga_horaria;
		}
		public void setCarga_horaria(double carga_horaria) {
			this.carga_horaria = carga_horaria;
		}
		public Date getCriado_em() {
			return criado_em;
		}
		public void setCriado_em(Date criado_em) {
			this.criado_em = criado_em;
		}
		public Date getAtualizado_em() {
			return atualizado_em;
		}
		public void setAtualizado_em(Date atualizado_em) {
			this.atualizado_em = atualizado_em;
		}

		
		
		
}
