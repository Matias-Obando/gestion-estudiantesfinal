package net.elpuig.daw2.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@WebServlet(urlPatterns = "/Report", asyncSupported = true)
public class Report extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("usuarioLogueado") == null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Debe iniciar sesión para generar informes.");
			return;
		}

		final AsyncContext ctxAsincrono = request.startAsync();
		ctxAsincrono.setTimeout(100000);

		ctxAsincrono.addListener(new AsyncListener() {
			public void onComplete(AsyncEvent event) throws IOException { System.out.println("Informe generado"); }
			public void onTimeout(AsyncEvent event) throws IOException { System.out.println("Timeout en reporte"); }
			public void onError(AsyncEvent event) throws IOException { System.out.println("Error: " + event.getThrowable().getMessage()); }
			public void onStartAsync(AsyncEvent event) throws IOException {}
		});

		ctxAsincrono.start(new Runnable() {
			@Override
			public void run() {
				String url = "jdbc:mysql://127.0.0.1:3306/dbalumnos?useSSL=false&serverTimezone=UTC";
				String user = "root";
				String pass = "";

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					try (Connection conn = DriverManager.getConnection(url, user, pass)) {

						String pathJrxml = getServletContext().getRealPath("/WEB-INF/informes/alumnos/Alumnos.jrxml");
						JasperReport jasperReport = JasperCompileManager.compileReport(pathJrxml);
						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);

						String tipoInforme = request.getParameter("optInformes");
						if (tipoInforme == null) tipoInforme = "application/pdf";

						if ("application/pdf".equals(tipoInforme)) {
							response.setContentType("application/pdf");
							response.setHeader("Content-Disposition", "inline; filename=informe.pdf");
							JRPdfExporter exporter = new JRPdfExporter();
							exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
							exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
							exporter.setConfiguration(new SimplePdfExporterConfiguration());
							exporter.exportReport();
							response.getOutputStream().flush();

						} else if (tipoInforme.contains("excel") || tipoInforme.contains("spreadsheetml")) {
							response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
							response.setHeader("Content-Disposition", "attachment; filename=informe.xlsx");
							JRXlsxExporter exporter = new JRXlsxExporter();
							exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
							exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
							exporter.exportReport();
							response.getOutputStream().flush();

						} else if (tipoInforme.contains("html")) {
							response.setContentType("text/html;charset=UTF-8");
							HtmlExporter exporter = new HtmlExporter();
							exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
							exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
							exporter.exportReport();

						} else if ("application/msword".equals(tipoInforme)) {
							response.setContentType("application/msword");
							response.setHeader("Content-Disposition", "attachment; filename=informe.doc");
							JRDocxExporter exporter = new JRDocxExporter();
							exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
							exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
							exporter.exportReport();
							response.getOutputStream().flush();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ctxAsincrono.complete();
				}
			}
		});
	}
}