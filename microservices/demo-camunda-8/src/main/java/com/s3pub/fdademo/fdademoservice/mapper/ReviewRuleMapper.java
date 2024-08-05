package com.s3pub.fdademo.fdademoservice.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.s3pub.fdademo.fdademoservice.domain.ReviewRule;
import com.s3pub.fdademo.fdademoservice.dto.ReviewRuleDTO;

@Mapper(componentModel = "spring")
public interface ReviewRuleMapper {

	ReviewRuleMapper MAPPER = Mappers.getMapper( ReviewRuleMapper.class );

	ReviewRuleDTO toDto(ReviewRule entry);
	ReviewRule toEntity(ReviewRuleDTO entry);
}