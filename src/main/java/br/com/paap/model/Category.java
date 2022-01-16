package br.com.paap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categoria")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String name;
	
	private String urlImage;

	@OneToMany(mappedBy = "category")
	@Builder.Default
	List<Event> events = new ArrayList<>();

}
