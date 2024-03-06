package com.webclient1c.release02.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PDFGenerator {

	public static InputStream generatePDF(String teststring) {
		// step 1: creation of a document-object
		Document document = new Document();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a file
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			document.add(new Paragraph(teststring));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}
		// step 5: we close the document
		document.close();
		return new ByteArrayInputStream(baos.toByteArray());
	}
}