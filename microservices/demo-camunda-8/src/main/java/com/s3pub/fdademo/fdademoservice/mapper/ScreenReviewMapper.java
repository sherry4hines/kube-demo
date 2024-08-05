package com.s3pub.fdademo.fdademoservice.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.s3pub.fdademo.fdademoservice.domain.ScreenReview;
import com.s3pub.fdademo.fdademoservice.dto.ScreenReviewDTO;

@Mapper(componentModel = "spring")
public interface ScreenReviewMapper {

	ScreenReviewMapper MAPPER = Mappers.getMapper( ScreenReviewMapper.class );

	ScreenReviewDTO toDto(ScreenReview review);
	ScreenReview toEntity(ScreenReviewDTO review);
}
