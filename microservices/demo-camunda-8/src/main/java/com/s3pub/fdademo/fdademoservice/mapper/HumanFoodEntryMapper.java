package com.s3pub.fdademo.fdademoservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.s3pub.fdademo.fdademoservice.domain.HumanFoodEntry;
import com.s3pub.fdademo.fdademoservice.dto.HumanFoodEntryDTO;

@Mapper(componentModel = "spring")
public interface HumanFoodEntryMapper {

	HumanFoodEntryMapper MAPPER = Mappers.getMapper( HumanFoodEntryMapper.class );

	HumanFoodEntryDTO toDto(HumanFoodEntry entry);
	HumanFoodEntry toEntity(HumanFoodEntryDTO entry);
}
