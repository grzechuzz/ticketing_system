package pl.eticket.app.dto.event;

import java.util.List;

public record SectorSeatsResponse(
    Long eventSectorId,
    String sectorName,
    Integer rowsCount,
    Integer seatsPerRow,
    List<RowInfo> rows
) { }
