package pl.eticket.app;

import pl.eticket.app.entity.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static Seat seat(Long id, int row, int seatNumber, Sector sector) {
        Seat seat = instantiate(Seat.class);
        seat.setId(id);
        seat.setRowNumber(row);
        seat.setSeatNumber(seatNumber);
        seat.setSector(sector);
        return seat;
    }

    public static Sector seatedSector(Long id, int rows, int seatsPerRow) {
        Sector sector = instantiate(Sector.class);
        sector.setId(id);
        sector.setName("Sector-" + id);
        sector.setIsStanding(false);
        sector.setRowsCount(rows);
        sector.setSeatsPerRow(seatsPerRow);
        return sector;
    }

    public static List<Seat> generateSeats(Sector sector, int rows, int seatsPerRow) {
        List<Seat> seats = new ArrayList<>();
        long idCounter = 1;
        for (int r = 1; r <= rows; r++) {
            for (int s = 1; s <= seatsPerRow; s++) {
                seats.add(seat(idCounter++, r, s, sector));
            }
        }
        return seats;
    }

    @SuppressWarnings("unchecked")
    private static <T> T instantiate(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate " + clazz.getSimpleName(), e);
        }
    }
}
