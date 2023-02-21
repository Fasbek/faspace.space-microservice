package com.spaceData.spacemicroservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceData.spacemicroservice.repository.OurSolarSystemRepository;
import com.spaceData.spacemicroservice.clients.MoonClient;
import com.spaceData.spacemicroservice.models.OurSolarSystemPlanet;
import com.spaceData.spacemicroservice.models.Dto.DtoMoon;

@Service
public class OurSolarSystemService {
	
	@Autowired
	private OurSolarSystemRepository ourSolarSystemRepository; 
	
	@Autowired
	private MoonClient moonClient;
	
	public List<OurSolarSystemPlanet> getAll(){
		return ourSolarSystemRepository.findAll();
	}
	
	public OurSolarSystemPlanet getPlanetById(String id) {
		OurSolarSystemPlanet planet = ourSolarSystemRepository.findById(id).orElse(null);
		if(planet != null) {
			List<DtoMoon> lunas = moonClient.listarLunasPorPlaneta(id).getBody();
			planet.setListaMoons(lunas);
		}
		return planet;
	}
	
	public OurSolarSystemPlanet guardarDto(OurSolarSystemPlanet dto) {
		OurSolarSystemPlanet nuevoPlaneta = ourSolarSystemRepository.save(dto);
		return nuevoPlaneta;
	}
	
	public String eliminarPlaneta(String planetId) {
		ourSolarSystemRepository.deleteById(planetId); ;
		return "Borrado con Éxito";
	}
	
}
