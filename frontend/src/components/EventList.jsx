import React, { useState, useEffect } from 'react';
import { api } from '../api';
import { styles as s } from '../styles/styles';

export default function EventList({ onSelect }) {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        api('/events').then(setEvents).catch(console.error);
    }, []);

    return (
        <div>
            <h2>Lista Wydarzen</h2>
            <table style={s.table}>
                <thead>
                <tr>
                    <th style={s.th}>Data</th>
                    <th style={s.th}>Nazwa</th>
                    <th style={s.th}>Miejsce</th>
                    <th style={s.th}>Akcja</th>
                </tr>
                </thead>
                <tbody>
                {events.map(ev => (
                    <tr key={ev.id}>
                        <td style={s.td}>{new Date(ev.eventStart).toLocaleDateString('pl-PL')}</td>
                        <td style={s.td}><strong>{ev.name}</strong></td>
                        <td style={s.td}>{ev.venueName}, {ev.venueCity}</td>
                        <td style={s.td}>
                            <button style={s.btn} onClick={() => onSelect(ev.id)}>Kup bilet</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
