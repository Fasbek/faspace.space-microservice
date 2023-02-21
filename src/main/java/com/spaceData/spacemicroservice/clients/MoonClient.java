package com.spaceData.spacemicroservice.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spaceData.spacemicroservice.models.Dto.DtoMoon;


//@FeignClient(name="moons-service", url="${msvc.moons.url}",path="/moons")
@FeignClient(name="moons-service", url="http://localhost:8002",path="/moons")
public interface MoonClient {
	
	@GetMapping("{id}")
	public ResponseEntity<List<DtoMoon>> listarLunasPorPlaneta(@PathVariable String id);
	
}
