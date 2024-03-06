package com.webclient1c.release02.pages;
import java.io.InputStream;

import com.webclient1c.release02.services.PDFGenerator;
import com.webclient1c.release02.services.PDFStreamResponse;

import org.apache.tapestry5.StreamResponse;

public class PdfWriterPage {
	public StreamResponse onSubmit() {
		// Create PDF
		InputStream is = PDFGenerator.generatePDF("This is the content of a Dynamically Generated PDF");
		// Return response
		return new PDFStreamResponse(is,"MyDynamicSample");
	}
}
