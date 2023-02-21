package com.spaceData.spacemicroservice.controller;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemStream.ItemSkippedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaceData.spacemicroservice.models.OurSolarSystemPlanet;
import com.spaceData.spacemicroservice.models.Dto.DtoCarrusel;
import com.spaceData.spacemicroservice.models.Filtro.FiltroAppComponent;
import com.spaceData.spacemicroservice.reports.dto.ReporteDto;
import com.spaceData.spacemicroservice.reports.dto.ReporteFiltroExoplanetasDto;
import com.spaceData.spacemicroservice.reports.service.ReportsService;
import com.spaceData.spacemicroservice.service.OurSolarSystemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import net.sf.jasperreports.engine.JRException;
//Controller
@RestController
@RequestMapping("/oursolarsystem")
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = {"http://faspace.click", "https://faspace.click", "http://178.128.205.140", "https://178.128.205.140", "http://localhost:4003", "https://faspace.netlify.app"})
public class OurSolarSystemController {

	@Autowired
	private OurSolarSystemService ourSolarSystemService;

	@Autowired
	private ReportsService reportsService;
	
	@Autowired
	private HttpServletResponse response;
	
	@GetMapping("/listarplanetas")
	public ResponseEntity<List<OurSolarSystemPlanet>> listarPlanetas(){
		List<OurSolarSystemPlanet> planetas = ourSolarSystemService.getAll();
		if(planetas.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(planetas);
	}
	
	@CircuitBreaker(name="moonsCB", fallbackMethod="fallBackGetMoons")
	@GetMapping("/obtenerdto/{id}")
	public ResponseEntity<OurSolarSystemPlanet> obtenerDto(@PathVariable("id") String id){
		OurSolarSystemPlanet planeta = ourSolarSystemService.getPlanetById(id);
		if(planeta == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(planeta);
	}
	
	@PostMapping("/exoplanets")
	public List<Object> getExoplanets(@RequestBody FiltroAppComponent filtro){
		
		String url = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=select+pl_name,hostname,"
				+ "soltype,cb_flag,sy_mnum,sy_pnum,sy_snum,discoverymethod,disc_year,disc_pubdate,disc_locale,disc_facility,disc_telescope,"
				+ "disc_instrument,pl_orbper,pl_orbpererr1,pl_orbpererr2,pl_orbsmax,pl_orbsmaxerr1,pl_orbsmaxerr2,"
				+ "pl_rade,pl_radeerr1,pl_radeerr2,pl_radj,pl_radjerr1,pl_radjerr2,pl_masse,pl_masseerr1,pl_masseerr2,"
				+ "pl_massj,pl_massjerr1,pl_massjerr2,pl_dens,pl_denserr1,pl_denserr2,pl_trandur,pl_trandurerr1,pl_trandurerr2,"
				+ "pl_orbeccen,pl_orbeccenerr1,pl_orbeccenerr2,pl_eqt,pl_eqterr1,pl_eqterr2,pl_orbincl,pl_orbinclerr1,pl_orbinclerr2,"
				+ "pl_tranmid,pl_tranmiderr1,pl_tranmiderr2,pl_ratdor,pl_ratdorerr1,pl_ratdorerr2,pl_occdep,pl_occdeperr1,pl_occdeperr2,"
				+ "pl_orbtper,pl_orbtpererr1,pl_orbtpererr2,pl_rvamp,pl_rvamperr1,pl_rvamperr2,pl_trueobliq,pl_trueobliqerr1,pl_trueobliqerr2,"
				+ "st_spectype,st_teff,st_tefferr1,st_tefferr2,st_rad,st_raderr1,st_raderr2,st_mass,st_masserr1,st_masserr2,st_met,st_met,st_meterr1,st_meterr2,"
				+ "st_metratio,st_lum,st_logg,"
				+ "st_age,st_ageerr1,st_ageerr2,st_dens,st_denserr1,st_denserr2,st_vsin,st_vsinerr1,st_vsinerr2,st_rotp,st_rotperr1,st_rotperr2,"
				+ "sy_pm,sy_pmerr1,sy_pmerr2,sy_pmra,sy_pmraerr1,sy_pmraerr2,sy_pmdec,sy_pmdecerr1,sy_pmdecerr2,sy_dist,sy_disterr1,sy_disterr2,"
				+ "sy_plx,sy_plxerr1,sy_plxerr2,pl_nnotes,rastr,decstr,ra,dec,glat,"
//				+ "glon,glonerr1,glonerr2,elat,elaterr1,elaterr2,elon,elonerr1,elonerr2,sy_bmag,sy_vmag,sy_jmag,sy_hmag,"
				+ "glon,elat,elon,sy_bmag,sy_vmag,sy_jmag,sy_hmag,"
				+ "sy_kmag,sy_umag,sy_gmag,sy_rmag,sy_imag,sy_zmag,sy_w1mag,sy_w2mag,sy_w3mag,sy_w4mag,sy_gaiamag,sy_icmag,sy_tmag,sy_kepmag,"
				+ "rowupdate,pl_pubdate,releasedate+from+ps+where+disc_facility+like+'%"+filtro.getTelescopio()+"%'+and+pl_rade>="+filtro.getRademin()+"+and+pl_rade<"+
				filtro.getRademax() + "+and+disc_year+like+'%"+filtro.getYear() +"%'&format=json";
		RestTemplate restTemplate = new RestTemplate();
		
		Object[] exoplanetas = restTemplate.getForObject(url, Object[].class);
		
		return Arrays.asList(exoplanetas);
	}
	
	@GetMapping("/listarCarruselHeaderTess")
	public List<DtoCarrusel> listarCarruselHeaderTess(){
		
		List carrusel = new ArrayList();
		
		String urlSelect = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=select+count(*)" + "\"count\"";
		String urlEnd = ",disc_facility,default_flag+from+ps+where+default_flag=1+and+disc_facility+like+'%TESS%'+or+disc_facility+like+'%Kepler%'+and+default_flag=1+or+disc_facility+like+'%K2%'+and+default_flag=1+and+soltype+like+'%Published Confirmed%'+group+by+disc_facility,+default_flag&format=json";
		String url = urlSelect + urlEnd;
		
		RestTemplate restTemplate = new RestTemplate();
		List<DtoCarrusel> items = restTemplate.getForObject(url, List.class);
		
//		List<DtoCarrusel> itemsInd = items.stream().map(obj -> obj.id)
		
//		List itemsInd = IntStream.range(0, items.size())
//				.mapToObj(i -> new DtoCarrusel(i, items.get(i).getDisc_facility(), items.get(i).getCount()))
//				.collect(Collectors. toList());
//		
//		this.list = StreamUtils.zipWithIndex(names.stream())
//				  .map(i -> new Robot(i.getIndex(), i.getValue()))
//				  .collect(collectingAndThen(toList(), Collections::unmodifiableList));
				
//		List<Robot> robots = IntStream.range(0, names.size())
//        .mapToObj(i -> new Robot(i, names.get(i))
//        .collect(toList());
		
		return items;
	}
	
//	@PostMapping("/guardardto")
//	public ResponseEntity<OurSolarSystemPlanet> guardarDto(@RequestPart("file") MultipartFile file, @RequestPart("dto") String dto) throws IOException {
//		
//		ObjectMapper mapper = new ObjectMapper();
//		OurSolarSystemPlanet planet = mapper.readValue(dto, OurSolarSystemPlanet.class);
//		
////		JsonNode rootNode = mapper.readTree(dto);
////		System.out.println(rootNode);
//		
//		if(!file.isEmpty()) {
//			byte[] bt = prueba(file, planet);
//			planet.setTexture(bt);
//		}
//		
//		OurSolarSystemPlanet nuevoPlaneta = ourSolarSystemService.guardarDto(planet);
//		return ResponseEntity.ok(nuevoPlaneta);
//	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardardto")
	public ResponseEntity<OurSolarSystemPlanet> guardarDto( @RequestBody() OurSolarSystemPlanet dto) throws IOException {
		
//		ObjectMapper mapper = new ObjectMapper();
//		OurSolarSystemPlanet planet = mapper.readValue(dto, OurSolarSystemPlanet.class);
		
//		JsonNode rootNode = mapper.readTree(dto);
//		System.out.println(rootNode);
		
//		if(!file.isEmpty()) {
//			byte[] bt = prueba(file, planet);
//			planet.setTexture(bt);
//		}
		
		OurSolarSystemPlanet nuevoPlaneta = ourSolarSystemService.guardarDto(dto);
		return ResponseEntity.ok(nuevoPlaneta);
	}
	
	@PreAuthorize("hasRole('ADMIN')")	
	@DeleteMapping("/eliminarplaneta/{id}")
	public ResponseEntity<String> eliminarPlaneta(@PathVariable("id") String id){
		
		String res = ourSolarSystemService.eliminarPlaneta(id);
		
		return ResponseEntity.ok(res);
	}
	
	public byte[] prueba (MultipartFile file, OurSolarSystemPlanet dto) throws IOException {
		
		byte [] bs = file.getBytes();
		dto.setTexture(bs);
		return bs;		
	}
	
	private ResponseEntity<OurSolarSystemPlanet> fallBackGetMoons(@PathVariable("id") String id, RuntimeException e){
		return new ResponseEntity("El planeta no puede mostrar por el momento sus lunas o no tiene.", HttpStatus.OK);
	}
	
//	@PostMapping("/exoplanets/pdf")
//	public ResponseEntity<Resource> exoplanetsPdf(@RequestBody ReporteFiltroExoplanetasDto dtoR) throws Exception {
//		
//		ReporteDto dto = reportsService.obtenerReporte(dtoR);
//		
//		InputStreamResource streamResource = new InputStreamResource(dto.getStream());
//		MediaType mediaType = null;
////		if(params.get("Tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
////			mediaType = MediaType.APPLICATION_OCTET_STREAM;
////		}else {	
////			mediaType = MediaType.APPLICATION_PDF;
////		}
//		mediaType = MediaType.APPLICATION_PDF;
//		return ResponseEntity.ok().header("Content-Disposition", "inLine; filename=\"" + dto.getFileName() + "\"")
//				.contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
//	}
	
	@GetMapping("/ourplanets/pdf/{planet}")
	public ResponseEntity<Resource> ourSolarSystemPdf(@PathVariable("planet") String planeta) throws Exception {
		
		String planet = "Tierra";
		ReporteDto dto = reportsService.obtenerReporteOurSolarSystem("todos");
		
		InputStreamResource streamResource = new InputStreamResource(dto.getStream());
		MediaType mediaType = null;
//		if(params.get("Tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
//			mediaType = MediaType.APPLICATION_OCTET_STREAM;
//		}else {	
//			mediaType = MediaType.APPLICATION_PDF;
//		}
		mediaType = MediaType.APPLICATION_PDF;
		return ResponseEntity.ok().header("Content-Disposition", "inLine; filename=\"" + dto.getFileName() + "\"")
				.contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
	}
}
