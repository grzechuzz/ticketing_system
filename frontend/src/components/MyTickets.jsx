import React, { useState, useEffect } from 'react';
import { api, API_BASE } from '../api';
import { styles as s } from '../styles/styles';

export default function MyTickets() {
    const [tickets, setTickets] = useState([]);

    useEffect(() => {
        api('/tickets').then(setTickets).catch(console.error);
    }, []);

    const downloadPdf = async (id) => {
        const token = localStorage.getItem('token');
        const res = await fetch(`${API_BASE}/tickets/${id}/pdf`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (res.ok) {
            const blob = await res.blob();
            const url = window.URL.createObjectURL(blob);
            window.open(url, '_blank');
        } else {
            alert('Blad pobierania PDF');
        }
    };

    return (
        <div>
            <h2>Moje Bilety</h2>
            {tickets.length === 0 ? (
                <p>Nie masz jeszcze zadnych biletow.</p>
            ) : (
                <table style={s.table}>
                    <thead>
                    <tr>
                        <th style={s.th}>Wydarzenie</th>
                        <th style={s.th}>Miejsce</th>
                        <th style={s.th}>Status</th>
                        <th style={s.th}>PDF</th>
                    </tr>
                    </thead>
                    <tbody>
                    {tickets.map(t => (
                        <tr key={t.id}>
                            <td style={s.td}><strong>{t.eventName}</strong></td>
                            <td style={s.td}>
                                {t.sectorName}
                                {t.row ? `, Rzad ${t.row}, Miejsce ${t.seatNumber}` : ' (stojacy)'}
                            </td>
                            <td style={s.td}>{t.status}</td>
                            <td style={s.td}>
                                <button onClick={() => downloadPdf(t.id)} style={s.btn}>Pobierz PDF</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}