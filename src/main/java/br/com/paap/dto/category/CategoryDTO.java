package br.com.paap.dto.category;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryDTO {
	Long id;
	String name;
	String urlImage;	
}
