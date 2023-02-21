package com.spaceData.spacemicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spaceData.spacemicroservice.models.OurSolarSystemPlanet;

@Repository
public interface OurSolarSystemRepository extends JpaRepository<OurSolarSystemPlanet, String>{
//	@Query(value="ALTER TABLE oursolarsystemplanet ALTER COLUMN ELECT * FROM Moons t WHERE t.moon_id=:id", nativeQuery=true)
//	void actualizarById(String planet);
}
