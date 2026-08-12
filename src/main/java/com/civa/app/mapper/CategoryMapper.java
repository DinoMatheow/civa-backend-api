package com.civa.app.mapper;

import org.mapstruct.Mapper;

import com.civa.app.domain.Category;
import com.civa.app.dto.CategoryDto;


@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto categoryDto);
    
    
}
