package com.s3pub.fdademo.fdademoservice.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.s3pub.fdademo.fdademoservice.domain.Manufacturer;
import com.s3pub.fdademo.fdademoservice.dto.ManufacturerDTO;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {

	ManufacturerMapper MAPPER = Mappers.getMapper( ManufacturerMapper.class );

	ManufacturerDTO toDto(Manufacturer manufacturer);

	Manufacturer toEntity(ManufacturerDTO manufacturer);
	
}
