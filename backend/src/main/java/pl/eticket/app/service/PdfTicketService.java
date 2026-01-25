package pl.eticket.app.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eticket.app.entity.OrderItem;
import pl.eticket.app.entity.Ticket;
import pl.eticket.app.exception.ApiException;
import pl.eticket.app.repository.TicketRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfTicketService {
    private final TicketRepository ticketRepository;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.of("Europe/Warsaw"));

    @Transactional(readOnly = true)
    public byte[] generateTicketPdf(Long userId, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> ApiException.notFound("Ticket not found"));

        if (!ticket.getOrderItem().getOrder().getUser().getId().equals(userId)) {
            throw ApiException.forbidden("Access denied");
        }

        return generatePdf(ticket);
    }

    private byte[] generatePdf(Ticket ticket) {
        OrderItem item = ticket.getOrderItem();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD);
            Paragraph title = new Paragraph("BILET WSTĘPU", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            doc.add(title);

            Font eventFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph eventName = new Paragraph(item.getEventNameSnapshot(), eventFont);
            eventName.setAlignment(Element.ALIGN_CENTER);
            eventName.setSpacingAfter(30);
            doc.add(eventName);

            Image qrImage = generateQrImage(ticket.getCode().toString());
            qrImage.setAlignment(Element.ALIGN_CENTER);
            doc.add(qrImage);

            Paragraph codeText = new Paragraph(ticket.getCode().toString(), new Font(Font.COURIER, 10));
            codeText.setAlignment(Element.ALIGN_CENTER);
            codeText.setSpacingAfter(30);
            doc.add(codeText);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(80);
            table.setSpacingBefore(20);

            addRow(table, "Data:", DATE_FMT.format(item.getEventDateSnapshot()));
            addRow(table, "Miejsce:", item.getVenueNameSnapshot());
            addRow(table, "Adres:", item.getVenueAddressSnapshot());
            addRow(table, "Sektor:", item.getSectorNameSnapshot());
            if (item.getRowSnapshot() != null) {
                addRow(table, "Rząd:", item.getRowSnapshot().toString());
            }
            if (item.getSeatNumberSnapshot() != null) {
                addRow(table, "Miejsce:", item.getSeatNumberSnapshot().toString());
            }
            addRow(table, "Typ biletu:", item.getTicketTypeNameSnapshot());
            addRow(table, "Cena:", item.getUnitPriceGross() + " PLN");

            doc.add(table);
            doc.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private Image generateQrImage(String content) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 150, 150);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", imgBytes);

        return Image.getInstance(imgBytes.toByteArray());
    }

    private void addRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, new Font(Font.HELVETICA, 10, Font.BOLD)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, new Font(Font.HELVETICA, 10)));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
