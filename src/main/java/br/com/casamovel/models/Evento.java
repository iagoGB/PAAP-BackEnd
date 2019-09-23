package br.com.casamovel.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Entity
public class Evento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE )
	private Long evento_id;

	private String titulo;
	
	private String foto;
        
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
        private Date data_horario;
        
        private String local;
        
        @JsonFormat(pattern="HH:mm", timezone="GMT-3")
        private Date carga_horaria;

	@ManyToOne
	@JoinColumn(
			name="fk_categoria_id", 
			referencedColumnName = "categoria_id",
			foreignKey = @ForeignKey(name="fk_categoria_id"),
			nullable = false	
	)
	private Categoria categoria;

	

	@OneToMany(mappedBy = "usuario_id")
	List<EventoUsuario> usuarios;
	
	@OneToMany(mappedBy ="nome_palestrante_id" )
	List<EventoPalestrante> palestrantes;

	public Long getEvento_id() {
		return evento_id;
	}
	public void setEvento_id(Long evento_id) {
		this.evento_id = evento_id;
	}
	public List<EventoUsuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<EventoUsuario> usuarios) {
		this.usuarios = usuarios;
	}
	public Evento() {

	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}

        public Timestamp getData() {
            return (Timestamp) data_horario;
        }

        public void setData(Date data) {
            this.data_horario = data;
        }

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        public List<EventoPalestrante> getPalestrantes() {
            return palestrantes;
        }

        public void setPalestrantes(List<EventoPalestrante> palestrantes) {
            this.palestrantes = palestrantes;
        }

        public Date getCarga_horaria() {
            return carga_horaria;
        }

        public void setCarga_horaria(Date carga_horaria) {
            this.carga_horaria = carga_horaria;
        }
        
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((evento_id == null) ? 0 : evento_id.hashCode());
		result = prime * result + ((foto == null) ? 0 : foto.hashCode());
		result = prime * result + ((palestrantes == null) ? 0 : palestrantes.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		result = prime * result + ((usuarios == null) ? 0 : usuarios.hashCode());
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
		Evento other = (Evento) obj;
		if (categoria == null) {
			if (other.categoria != null)
				return false;
		} else if (!categoria.equals(other.categoria))
			return false;
		if (evento_id == null) {
			if (other.evento_id != null)
				return false;
		} else if (!evento_id.equals(other.evento_id))
			return false;
		if (foto == null) {
			if (other.foto != null)
				return false;
		} else if (!foto.equals(other.foto))
			return false;
		if (palestrantes == null) {
			if (other.palestrantes != null)
				return false;
		} else if (!palestrantes.equals(other.palestrantes))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		if (usuarios == null) {
			if (other.usuarios != null)
				return false;
		} else if (!usuarios.equals(other.usuarios))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "Evento{" + "evento_id=" + evento_id + ", titulo=" + titulo + ", foto=" + foto + ", categoria=" + categoria + ", usuarios=" + usuarios + ", palestrantes=" + palestrantes + '}';
    }
	
}
