package com.museumsystem.museumserver.utlis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
 
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import com.google.zxing.WriterException;
import com.museumsystem.museumserver.model.Ticket;
import com.museumsystem.museumserver.service.EmailManagerService;

public class PdfCreator {
	
	@Autowired
	EmailManagerService emailManagerService;
	
	private static final String TICKET_TITLE_PL = "Bilet wstępu do Muzeum";
	private static final String TICKET_TITLE_EN = "Museum entrance ticket";
	private static final String VALID_AT_PL = "Ważny w dniu";
	private static final String VALID_AT_EN = "Valid at";
	private static final String NR_OF_PEOPLE_PL = "Liczba osób";
	private static final String NR_OF_PEOPLE_EN = "Number of people";
	private static final String ADULTS_PL = "Osoby dorosłe";
	private static final String ADULTS_EN = "Adults";
	private static final String CHILDREN_PL = "Dzieci";
	private static final String CHILDREN_EN = "Children";

	private static final int TOP_MARGIN = 50;
	
	static InputStreamResource inputStreamResource;

	public static InputStreamResource getTicketPdf(Ticket ticket) {
		final String validDate = ticket.getDate();
		final Integer adultsNr = ticket.getAdultsNr();
		final Integer childrenNr = ticket.getChildrenNr();

		// Creating PDF document object
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		PDFont fontRegular = null;
		PDFont fontBold = null;
		Resource fontRegularRes = new ClassPathResource("/fonts/AbhayaLibre-Regular.ttf");
		Resource fontBoldRes = new ClassPathResource("/fonts/AbhayaLibre-Bold.ttf");
		//InputStream resourceInputStream = fontRegularRes.getInputStream();
		try {
			fontRegular = PDType0Font.load(document, fontRegularRes.getInputStream());
			fontBold = PDType0Font.load(document, fontBoldRes.getInputStream());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		PDFont font = fontBold;// = PDType1Font.COURIER_BOLD;
		int fontSize = 24;
		int padding = 0;
		try {
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.beginText();
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(TICKET_TITLE_PL) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN);
			contentStream.showText(TICKET_TITLE_PL);
			contentStream.endText();

			contentStream.beginText();
			padding += 30;
			font = fontRegular;
			fontSize = 14;
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(TICKET_TITLE_EN) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN
							- padding);
			contentStream.showText(TICKET_TITLE_EN);
			contentStream.endText();

			contentStream.beginText();
			padding += 110;
			font = fontRegular;
			fontSize = 14;
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(VALID_AT_PL + " / " + VALID_AT_EN) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN
							- padding);
			contentStream.showText(VALID_AT_PL + " / " + VALID_AT_EN);
			contentStream.endText();
			
			contentStream.beginText();
			padding += 15;
			font = fontBold;
			fontSize = 14;
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(validDate) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN
							- padding);
			contentStream.showText(validDate);
			contentStream.endText();
			
			contentStream.beginText();
			padding += 30;
			font = fontRegular;
			fontSize = 14;
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(NR_OF_PEOPLE_PL + " / " + NR_OF_PEOPLE_EN) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN
							- 190);
			contentStream.showText(NR_OF_PEOPLE_PL + " / " + NR_OF_PEOPLE_EN);
			contentStream.endText();
			
			contentStream.beginText();
			padding += 20;
			font = fontRegular;
			fontSize = 12;
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(ADULTS_PL + " / " + ADULTS_EN) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN
							- padding);
			contentStream.showText(ADULTS_PL + " / " + ADULTS_EN);
			contentStream.endText();
			
			contentStream.beginText();
			padding += 15;
			font = fontBold;
			fontSize = 12;
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(adultsNr.toString()) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN
							- padding);
			contentStream.showText(adultsNr.toString());
			contentStream.endText();
			
			contentStream.beginText();
			padding += 20;
			font = fontRegular;
			fontSize = 12;
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(CHILDREN_PL + " / " + CHILDREN_EN) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN
							- padding);
			contentStream.showText(CHILDREN_PL + " / " + CHILDREN_EN);
			contentStream.endText();
			
			contentStream.beginText();
			padding += 15;
			font = fontBold;
			fontSize = 12;
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(childrenNr.toString()) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN
							- padding);
			contentStream.showText(childrenNr.toString());
			contentStream.endText();
			
			String ticketJwt = JWTTokenManager.generateTicketToken(ticket, "qwerty");
			byte[] qrImage = QrCodeGenerator.generateQr(ticketJwt, 355, 355);
			PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, qrImage, "img");
			
			padding += 30;
			contentStream.drawImage(pdImage, 
					(page.getMediaBox().getWidth() - pdImage.getWidth()) / 2, 
					page.getMediaBox().getHeight() - pdImage.getHeight() - TOP_MARGIN - padding);
			
			contentStream.beginText();
			font = fontRegular;
			fontSize = 12;
			contentStream.setFont(font, fontSize);
			contentStream.newLineAtOffset(
					(page.getMediaBox().getWidth() - font.getStringWidth(Long.toString(ticket.getId())) / 1000 * fontSize) / 2,
					page.getMediaBox().getHeight()
							- font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize - TOP_MARGIN
							- padding - pdImage.getHeight());
			contentStream.showText(Long.toString(ticket.getId()));
			contentStream.endText();
			
			contentStream.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		document.addPage(page);

		// Saving the document
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			document.save(byteArrayOutputStream);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			inputStreamResource = new InputStreamResource(byteArrayInputStream);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("PDF created");

		// Closing the document
		try {
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return inputStreamResource;
	}
}
