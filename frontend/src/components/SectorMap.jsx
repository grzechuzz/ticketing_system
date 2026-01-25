import React, { useState, useEffect } from 'react';
import { api, API_BASE } from '../api';
import { styles as s } from '../styles/styles';

export default function SectorMap({ event, onBack, onAddToCart }) {
    const [selectedSector, setSelectedSector] = useState(null);
    const [seats, setSeats] = useState(null);
    const [selectedSeats, setSelectedSeats] = useState(new Set());
    const [quantity, setQuantity] = useState(1);
    const [ticketType, setTicketType] = useState(null);
    const [error, setError] = useState('');

    const [validationMsg, setValidationMsg] = useState('');
    const [suggestions, setSuggestions] = useState([]);

    useEffect(() => {
        setSeats(null);
        setSelectedSeats(new Set());
        setQuantity(1);
        setError('');
        setValidationMsg('');
        setSuggestions([]);
        if (selectedSector?.prices?.length > 0) {
            setTicketType(selectedSector.prices[0].ticketTypeId);
        }
    }, [selectedSector]);

    useEffect(() => {
        if (selectedSector && !selectedSector.standing) {
            api(`/events/${event.id}/sectors/${selectedSector.eventSectorId}/seats`)
                .then(setSeats)
                .catch(() => setError("Nie udalo sie pobrac miejsc"));
        }
    }, [selectedSector, event.id]);

    if (!selectedSector) {
        return (
            <div>
                <button onClick={onBack} style={s.navBtn}>Powrot do listy</button>
                <h2>{event.name} - Wybierz sektor</h2>
                <div style={{ position: 'relative', width: '700px', height: '450px', border: '2px solid #333', background: '#f5f5f5', margin: '20px auto' }}>
                    <div style={{ position: 'absolute', top: 10, left: '50%', transform: 'translateX(-50%)', background: '#333', color: '#fff', padding: '8px 40px', fontWeight: 'bold' }}>SCENA</div>
                    {event.sectors.map(sec => {
                        const active = sec.standing ? sec.availableCapacity > 0 : true;
                        return (
                            <div key={sec.eventSectorId} onClick={() => active && setSelectedSector(sec)}
                                 style={{
                                     position: 'absolute', left: (sec.positionX || 50) + 'px', top: (sec.positionY || 80) + 'px',
                                     width: (sec.width || 120) + 'px', height: (sec.height || 100) + 'px', border: '2px solid #333',
                                     background: active ? (sec.standing ? '#90EE90' : '#87CEEB') : '#ccc', cursor: active ? 'pointer' : 'not-allowed',
                                     display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', opacity: active ? 1 : 0.6
                                 }}
                            >
                                <span>{sec.name}</span>
                            </div>
                        );
                    })}
                </div>
            </div>
        );
    }

    const currentPrice = selectedSector.prices.find(p => p.ticketTypeId === ticketType);
    const pricePerItem = currentPrice?.priceGross || 0;
    const totalPrice = selectedSector.standing
        ? (pricePerItem * quantity).toFixed(2)
        : (pricePerItem * selectedSeats.size).toFixed(2);

    return (
        <div style={s.box}>
            <button onClick={() => setSelectedSector(null)} style={s.navBtn}>Wroc do mapy</button>
            <h2>{selectedSector.name} ({selectedSector.standing ? 'Stojacy' : 'Siedzacy'})</h2>
            {error && <div style={s.error}>{error}</div>}

            <div style={{ marginBottom: '20px' }}>
                <label><strong>Typ biletu: </strong></label>
                <select onChange={e => setTicketType(Number(e.target.value))} value={ticketType || ''} style={{ padding: '8px', fontSize: '14px' }}>
                    {selectedSector.prices.map(p => (
                        <option key={p.ticketTypeId} value={p.ticketTypeId}>{p.ticketTypeName} - {p.priceGross.toFixed(2)} PLN</option>
                    ))}
                </select>
            </div>

            {selectedSector.standing ? (
                <div>
                    <label><strong>Ilosc biletow: </strong></label>
                    <input type="number" min="1" max={selectedSector.availableCapacity} value={quantity}
                           onChange={e => setQuantity(Math.min(Number(e.target.value), selectedSector.availableCapacity))}
                           style={{ width: '80px', padding: '8px', fontSize: '16px' }}
                    />
                </div>
            ) : (
                <div>
                    {!seats ? <p>Ladowanie miejsc...</p> : (
                        <div style={{ background: '#fff', border: '1px solid #ccc', padding: '15px', overflowX: 'auto' }}>
                            <div style={{ background: '#333', color: '#fff', padding: '5px', textAlign: 'center', marginBottom: '15px', width: '60%', margin: '0 auto 15px' }}>SCENA</div>
                            {seats.rows.map(row => (
                                <div key={row.rowNumber} style={{ display: 'flex', alignItems: 'center', marginBottom: '5px' }}>
                                    <span style={{ width: '35px', fontWeight: 'bold', fontSize: '12px' }}>R{row.rowNumber}</span>
                                    <div style={{ display: 'flex', gap: '3px' }}>
                                        {row.seats.map(seat => {
                                            const isSelected = selectedSeats.has(seat.seatId);
                                            const isOccupied = seat.occupied;
                                            return (
                                                <div key={seat.seatId}
                                                     onClick={() => {
                                                         if (isOccupied) return;
                                                         const newSet = new Set(selectedSeats);
                                                         if (newSet.has(seat.seatId)) newSet.delete(seat.seatId);
                                                         else newSet.add(seat.seatId);
                                                         setSelectedSeats(newSet);
                                                     }}
                                                     style={{
                                                         width: '28px', height: '28px', border: '1px solid #333', borderRadius: '4px',
                                                         background: isOccupied ? '#666' : isSelected ? '#ffc107' : '#28a745',
                                                         color: isOccupied ? '#fff' : '#000', cursor: isOccupied ? 'not-allowed' : 'pointer',
                                                         display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '10px', fontWeight: 'bold'
                                                     }}
                                                >
                                                    {seat.seatNumber}
                                                </div>
                                            );
                                        })}
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                    <p>Wybrano miejsc: {selectedSeats.size}</p>
                </div>
            )}

            <div style={{ marginTop: '20px', padding: '15px', background: '#e9ecef', borderTop: '3px solid #007bff' }}>
                <h3 style={{ margin: '0 0 10px 0' }}>SUMA: {totalPrice} PLN</h3>
                <button
                    onClick={() => alert("nanana")}
                    style={{ ...s.btnGreen, fontSize: '16px', padding: '12px 30px' }}
                >
                    Dodaj do koszyka (Demo)
                </button>
            </div>
        </div>
    );
}