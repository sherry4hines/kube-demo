package com.s3pub.fdademo.fdademoservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.s3pub.fdademo.fdademoservice.domain.Manufacturer;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long>, JpaSpecificationExecutor<Manufacturer> {

	Optional<Manufacturer> findByFeiNumber(String feiNumber);

	@Query(value = "select mn from Manufacturer mn where mn.manufacturerLegalName = :name and mn.manufacturerAddress1 = :address " + 
	"and mn.manufacturerCity = :city and mn.manufacturerCountry = :country ")
	Optional<Manufacturer> findFirstByNameAndAddress(
			@Param("name") String name, @Param("address") String address, 
			@Param("city") String city, @Param("country") String country);
}
