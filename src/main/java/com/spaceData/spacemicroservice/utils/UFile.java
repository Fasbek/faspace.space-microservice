package com.spaceData.spacemicroservice.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import net.royal.spring.framework.util.UString;

public class UFile {

	private static final Log LOGGER = LogFactory.getLog(UFile.class);
	
	public static void guardarContenidoFileByte(String rutaComleta, byte[] archivo) throws IOException {
		WriteFile w = new WriteFile(rutaComleta, archivo);
		w.start();
	}

	public static boolean carpetaBorrar(File folder) {
		if (folder.isDirectory()) {
			String[] children = folder.list();
			for (int i = 0; i < children.length; i++) {
				LOGGER.debug(">>> Iterando file " + i);
				boolean success = carpetaBorrar(new File(folder, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		LOGGER.debug("Carpeta borrada satisfactoriamente");
		return folder.delete();
	}
	
//	public static Boolean existeArchivo(String rutaCompleta) {
//		if (UString.estaVacio(rutaCompleta))
//			return false;
//		return Files.exists(Paths.get(rutaCompleta));
//	}

//	public static Boolean existeArchivo(String ruta, String archivo) {
//		File f = new File(UString.obtenerSinNulo(ruta) + File.separator + UString.obtenerSinNulo(archivo));
//		return f.exists();
//	}

	public static String extraerMimeContentType(String nombreArchivo) {
		String mimeType;
		mimeType = URLConnection.guessContentTypeFromName(nombreArchivo);
		return mimeType;
	}

	public static String extraerNombreArchivo(String fileName) {
		String extension = "";
		if (fileName == null)
			return null;
		int i = fileName.lastIndexOf(File.separator);
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return extension;
	}

	public static String extraerExtension(String fileName) {
		String extension = "";
		if (fileName == null)
			return null;
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return extension;
	}

	/*
	 * public static void guardarContenidoDesdeBytes(String rutaCompleta, byte[]
	 * archivo) throws IOException { WriteFile w = new WriteFile(rutaCompleta,
	 * archivo); w.start(); }
	 */

	public static void guardarContenidoDesdeString(String rutaCompleta, String contenidoCadena) throws IOException {
		File archivo = new File(rutaCompleta);
		FileWriter fichero = new FileWriter(archivo);
		PrintWriter pw = new PrintWriter(fichero);
		pw.println(contenidoCadena);
		fichero.close();
	}

	public static boolean eliminarArchivo(String rutaCumpleta) {
		File fichero = new File(rutaCumpleta);
		if (!fichero.exists())
			return false;
		return fichero.delete();
	}

	public static boolean eliminarCarpeta(File folder) {
		if (folder.isDirectory()) {
			String[] children = folder.list();
			for (int i = 0; i < children.length; i++) {
				LOGGER.debug(">>> Iterando file " + i);
				boolean success = eliminarCarpeta(new File(folder, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		LOGGER.debug("Carpeta borrada satisfactoriamente");
		return folder.delete();
	}

	public static String ConvertirBytesToString(byte[] bytes) {
		try {
			return new String(bytes, 0, bytes.length, "ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] ConvertirInputStreamToBytes(InputStream input) throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}

	public static String ConvertirRutaCompletaToString(String rutaCompleta) throws IOException {
		StringBuilder contenido = new StringBuilder("");
		File archivo = new File(rutaCompleta);
		if (archivo.exists()) {
			FileReader fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null)
				contenido.append(linea);
			fr.close();
		}
		return contenido.toString();
	}

	public static byte[] ConvertirRutaCompletaToBytes(String rutaCompleta) {
		byte[] bytesrespaldo = null;
		File file = new File(rutaCompleta);
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			byte[] bytes = bos.toByteArray();
			bytesrespaldo = bytes;
			fis.close();
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return bytesrespaldo;
	}

	public static String ConvertirInputStreamToString(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public static String getSeparador() {
		return File.separator;
	}

	public static String archivoUnico() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
		String nombreCarpetaSession = df.format(new Date());
		return nombreCarpetaSession;
	}

//	public static String rutaFisicaWebServer() {
//		return rutaFisicaWeb("webserver");
//	}

	public static String rutaFisicaWebApp() {
		String rutaWS = System.getProperty("user.dir");
		String sos = System.getProperty("os.name");
		// String rutaIo =
		// FileSystems.getDefault().getPath("").toAbsolutePath().toString();
		// String rutaPA = Paths.get("").toAbsolutePath().toString();
		// System.out.println("rutaWS:"+rutaWS);
		// System.out.println("rutaIo:"+rutaIo);
		// System.out.println("rutaPA:"+rutaPA);
		// System.out.println("sos:"+sos);

		String search = File.separator + "bin";
		int index = rutaWS.lastIndexOf(search);
		if (index > -1) {
			// System.out.println("cuando es bin:windows");
			rutaWS = rutaWS.substring(0, index);
			// esto se aplica cuando va en un tomcat
			// System.out.println(rutaWS);
			return rutaWS;
		}

		// System.out.println("h3 -api:windows");
		search = "-api";
		index = rutaWS.lastIndexOf(search);
		if (index > -1) {
			// System.out.println("cuando es -api:windows");
			index = rutaWS.lastIndexOf(File.separator);
			rutaWS = rutaWS.substring(0, index);
			// esto se aplica cuando va en un tomcat
			// System.out.println(rutaWS);
			return rutaWS;
		}

		// System.out.println("h4 TOMCAT");
		search = "TOMCAT";
		index = rutaWS.toUpperCase().lastIndexOf(search);
		if (index > -1) {
			// System.out.println("cuando es TOMCAT");
			rutaWS = rutaWS + File.separator + "webapps";
			// System.out.println(rutaWS);
			return rutaWS;
		}

		// System.out.println("h5 jar eclipse:boot");
		int jarIndex = rutaWS.lastIndexOf(File.separator);
		if (jarIndex > -1) {
			// System.out.println("cuando es un jar eclipse:boot");
			// esto se aplica cuando es un jar
			if (sos.contains("Windows")) {
				// System.out.println("windows");
				rutaWS = rutaWS.substring(0, jarIndex);
				// System.out.println(rutaWS);
			} else {
				// System.out.println("linux");
			}
			return rutaWS;
		}
		return rutaWS;
	}

//	private static String rutaFisicaWeb(String tipo) {
//		// System.out.println("h1 main");
//		String sos = System.getProperty("os.name");
//		// System.out.println("sos");
//		// System.out.println(sos);
//		// System.out.println(System.getProperty("os.arch"));
//		
//		//Cambiar al implementar UString
////		if (UString.esNuloVacio(tipo))
////			return null;
////		String rutaWS = System.getProperty("user.dir");
////		
//		
//		// System.out.println("rutaWS");
//		// System.out.println(rutaWS);
//		if (tipo.equals("webserver")) {
//			// System.out.println("h2 webserver devolviendo");
//			// System.out.println(rutaWS);
//			String search = File.separator + "bin";
//			int index = rutaWS.lastIndexOf(search);
//			if (index > -1) {
//				// System.out.println("cuando es bin:windows");
//				rutaWS = rutaWS.substring(0, index);
//				// esto se aplica cuando va en un tomcat
//				// System.out.println(rutaWS);
//				return rutaWS;
//			}
//
//			// System.out.println("h3 -api:windows");
//			search = "-api";
//			index = rutaWS.lastIndexOf(search);
//			if (index > -1) {
//				// System.out.println("cuando es -api:windows");
//				index = rutaWS.lastIndexOf(File.separator);
//				rutaWS = rutaWS.substring(0, index);
//				// esto se aplica cuando va en un tomcat
//				// System.out.println(rutaWS);
//				return rutaWS;
//			}
//
//			// System.out.println("h4 TOMCAT");
//			search = "TOMCAT";
//			index = rutaWS.toUpperCase().lastIndexOf(search);
//			if (index > -1) {
//				// System.out.println("cuando es TOMCAT");
//				// System.out.println(rutaWS);
//				return rutaWS;
//			}
//
//			// System.out.println("h5 jar eclipse:boot");
//			int jarIndex = rutaWS.lastIndexOf(File.separator);
//			if (jarIndex > -1) {
//				// System.out.println("cuando es un jar eclipse:boot");
//				// esto se aplica cuando es un jar
//				if (sos.contains("Windows")) {
//					// System.out.println("windows");
//					rutaWS = rutaWS.substring(0, jarIndex);
//					// System.out.println(rutaWS);
//				} else {
//					// System.out.println("linux");
//				}
//				return rutaWS;
//			}
//			return rutaWS;
//		}
//		if (tipo.equals("webcontext")) {
//
//		}
//		if (tipo.equals("webapp")) {
//
//		}
//		return null;
//	}

	public static String obtenerContenidoFile(String rutaArchivo) throws IOException {

		StringBuilder contenido = new StringBuilder("");

		File archivo = new File(rutaArchivo);

		if (archivo.exists()) {
			FileReader fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);

			String linea;
			while ((linea = br.readLine()) != null)
				contenido.append(linea);

			fr.close();
		}

		return contenido.toString();
	}

	public static byte[] obtenerArregloByte(String ruta) throws IOException {
		byte[] bytesrespaldo = null;
		File file = new File(ruta);

		try {
			// LOGGER.debug("entro a try");
			FileInputStream fis = new FileInputStream(file);
			// LOGGER.debug("file::" + file.getName());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];

			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			byte[] bytes = bos.toByteArray();
			// LOGGER.debug("bytes::" + bytes);
			bytesrespaldo = bytes;
			fis.close();
		} catch (Exception e) {
			LOGGER.error(e);
		}

		return bytesrespaldo;
	}

	public static void guardarContenidoFile(String ruta, String nombre, byte[] archivo) throws IOException {
		File uploadedFile = new File(ruta + File.separator + nombre);
		if (uploadedFile.exists()) {
			uploadedFile.delete();
		}

		byte[] bytes = new byte[1024];
		bytes = archivo;

		new File(ruta + File.separator).mkdirs();

		Path path = Paths.get(ruta + File.separator + nombre);
		LOGGER.debug(ruta + File.separator + nombre);
		LOGGER.debug("bytes::" + bytes);
		Files.write(path, bytes);

	}

//	public static String quitarCaracteresExtranos(String nombreArchivo) throws IOException {
//		if (!UString.esNuloVacio(nombreArchivo)) {
//			
//			try {
//			
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)128), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)129), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)130), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)131), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)132), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)133), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)134), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)135), "");			
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)136), "");			
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)137), "");			
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)138), "");			
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)139), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)140), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)141), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)142), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)143), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)144), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)145), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)146), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)147), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)148), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)149), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)150), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)151), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)152), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)153), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)154), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)155), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)156), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)157), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)158), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)159), "");
//				//letra a con tilde 225
//				//nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)225), "");
//				//letra e con tilde 233
//				//nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)233), "");
//				//letra i con tilde 237
//				//nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)237), "");				
//				//letra o con tilde 243
//				//nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)243), "");
//				//letra u con tilde 243
//				//nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)250), "");				
//				
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)160), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)161), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)162), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)163), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)164), "n");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)165), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)166), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)167), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)168), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)169), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)170), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)171), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)172), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)173), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)174), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)175), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)176), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)177), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)178), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)179), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)180), "");	
//				
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)235), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)236), "");				
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)238), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)239), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)240), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)241), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)242), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)244), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)245), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)246), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)247), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)248), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)249), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)251), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)252), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)253), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)254), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)255), "");
//				
//				//NO ES NECESARIO QUITAR LAS COMAS
//				//nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)42), "");			
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)64), "");
//				//PORCENTAJE
//				//nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)37), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)34), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)65533), "");	
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)946), "");	
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)223), "");	
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)182), "");
//				
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)191), "");
//				//ERROR CON SIGNO DE INTERROGACION
//				//nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)63), "");
//				
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)33), "");
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)161), "");
//				
//				//OPCIONALES
//				//MAYOR
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)60), "");
//				//MENOR
//				nombreArchivo=nombreArchivo.replaceAll(Character.toString((char)62), "");
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			nombreArchivo=nombreArchivo.trim();	
//			
//			
//		}		
//		return nombreArchivo;
//	}

//	public static String obtenerNombreWebDescargar(String nombreArchivo) throws IOException {
//		if (!UString.esNuloVacio(nombreArchivo)) {
//			nombreArchivo=nombreArchivo.replace(",", "_");
//			nombreArchivo=nombreArchivo.trim();	
//		}		
//		return nombreArchivo;
//	}
	
	public static void eliminarContenidoFile(String ruta, String nombre, byte[] archivo) throws IOException {
		File uploadedFile = new File(ruta + File.separator + nombre);
		if (uploadedFile.exists()) {
			uploadedFile.delete();
		}
	}
}

class WriteFile extends Thread {

	String rutaComleta;
	byte[] archivo;

	public WriteFile(String rutaComleta, byte[] archivo) {
		this.rutaComleta = rutaComleta;
		this.archivo = archivo;
	}

	@Override
	public void run() {
		try {
			File uploadedFile = new File(rutaComleta);
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}
			InputStream stream;

			byte[] bytes = new byte[1024];
			bytes = archivo;

			stream = new ByteArrayInputStream(bytes);

			OutputStream out;

			out = new FileOutputStream(uploadedFile);

			int read = 0;

			while ((read = stream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			stream.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}