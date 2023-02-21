package com.spaceData.spacemicroservice.reports.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.aspectj.apache.bcel.classfile.Module.Export;
import org.hibernate.engine.jdbc.StreamUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.spaceData.spacemicroservice.models.OurSolarSystemPlanet;
import com.spaceData.spacemicroservice.reports.dto.ReporteDto;
import com.spaceData.spacemicroservice.reports.dto.ReporteFiltroExoplanetasDto;
import com.spaceData.spacemicroservice.service.OurSolarSystemService;
import com.spaceData.spacemicroservice.utils.UFile;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportsService implements Serializable{

	private static final String REPORT_FOLDER = "reports";
	private static final String JASPER = ".jasper";

	@Autowired
	private OurSolarSystemService ourSolarSystemService;
	
	@Autowired
	private DataSource dataSource;

	public ResponseEntity<String> generateReport(HttpServletResponse response) throws Exception {

		byte[] content;

		String path = "c:\\report";
		List<OurSolarSystemPlanet> planetas = ourSolarSystemService.getAll();
		String rutaReporte = UFile.rutaFisicaWebApp() + File.separator + "space-microservice" + File.separator
				+ "exoplanets.jasper";

		if (planetas.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		;
		File file = ResourceUtils.getFile("classpath:exoplanets.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(
				"src" + File.separator + "main" + File.separator + "resources" + File.separator + "exoplanets.jrxml"));
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(planetas);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name_id", "Tierra");
		JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		JasperExportManager.exportReportToPdfFile(print, path + "\\exoplanets.pdf");
		return ResponseEntity.ok("Exportación Completa");

//		
//		content = ejecutarReporteComoPDF(rutaReporte, parameters);
//		enviarReportePdf(content, response);
//		
////		String a = UFile. rutaFisicaWebApp() + "ea";
////		
////		System.out.println(a);
//		return ResponseEntity.ok("Ok");
//		
	}

	@Transactional
	public byte[] ejecutarReporteComoPDF(String rutaArchivoReporteFuente, Map parametros) throws Exception {
		return ejecutarReporteComoPDFa(rutaArchivoReporteFuente, parametros);
	}

	@Transactional(readOnly = true)
	public byte[] ejecutarReporteComoPDFa(String rutaArchivoReporteFuente, Map parametros) throws Exception {
		Connection conexion;
//		conexion = ((SessionImpl) getSesionActual()).connection();
		return ejecutarReporteComoPDFb(rutaArchivoReporteFuente, parametros);
	}

	private byte[] ejecutarReporteComoPDFb(String rutaArchivoReporteFuente, Map parametros) throws Exception {
		byte[] bytReporte = null;
		bytReporte = JasperRunManager.runReportToPdf(rutaArchivoReporteFuente, parametros);
		return bytReporte;
	}

	public void enviarReportePdf(byte[] contenido, HttpServletResponse response) throws IOException {
		InputStream inputStream;

		String CONTENT_TYPE_APPLICATION_PDF_VALUE = "application/pdf";
		String HTTP_HEADER_CONTENT_DISPOSITION = "Content-Disposition";
		String CONTENT_DISPOSITION_INLINE_FORMAT = "inline; filename=\"%s.pdf\"";

		response.setContentType(CONTENT_TYPE_APPLICATION_PDF_VALUE);
		response.setHeader(HTTP_HEADER_CONTENT_DISPOSITION,
				String.format(CONTENT_DISPOSITION_INLINE_FORMAT, UUID.randomUUID()));
		response.setContentLength(contenido.length);

		inputStream = new BufferedInputStream(new ByteArrayInputStream(contenido));
		StreamUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();
	}

//	public ReporteDto obtenerReporte(ReporteFiltroExoplanetasDto dto) throws JRException, IOException, SQLException {
//		
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("name_id", dto.getName_id());
//		params.put("tipo", "PDF");
//		
////		String fileName = "reporte_exoplanetas";
////		String extension = ".pdf";
//		ReporteDto dtoResp = new ReporteDto();
////		dto.setFileName(params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name()) ? ".xlsx" : ".pdf" );
////		dtoResp.setFileName(fileName + extension);
//		ByteArrayOutputStream stream = export("exoplanets", params.get("tipo").toString(), params,
//				dataSource.getConnection());
//
//		byte[] bs = stream.toByteArray();
//		dtoResp.setStream(new ByteArrayInputStream(bs));
//		dtoResp.setLength(bs.length);
//
//		return dtoResp;
//	}
	
	public ReporteDto obtenerReporteOurSolarSystem(String planet) throws JRException, IOException, SQLException {
		
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(planet.equals("todos")) {
			params.put("name_id", null);
			params.put("tipo", "PDF");			
		}else {
			params.put("name_id", planet);
			params.put("tipo", "PDF");
		}

//		String fileName = "reporte_exoplanetas";
//		String extension = ".pdf";
		ReporteDto dtoResp = new ReporteDto();
//		dto.setFileName(params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name()) ? ".xlsx" : ".pdf" );
//		dtoResp.setFileName(fileName + extension);
		ByteArrayOutputStream stream = export("oursolarsystem", params.get("tipo").toString(), params,
				dataSource.getConnection());

		byte[] bs = stream.toByteArray();
		dtoResp.setStream(new ByteArrayInputStream(bs));
		dtoResp.setLength(bs.length);

		return dtoResp;
	}

	public ByteArrayOutputStream export(String fileName, String tipoReporte, Map<String, Object> params,
			Connection conn) throws JRException, IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ClassPathResource resource = new ClassPathResource(REPORT_FOLDER + File.separator + fileName + JASPER);

		InputStream inputStream = resource.getInputStream();
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, conn);
		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		return stream;
	}

}
