import React, { useState } from 'react';
import { useAuth } from './context/AuthContext';
import { api } from './api';
import { styles as s } from './styles/styles';

import LoginRegister from './components/LoginRegister';
import EventList from './components/EventList';
import SectorMap from './components/SectorMap';
import Cart from './components/Cart';
import MyTickets from './components/MyTickets';

export default function App() {
    const { isAuthenticated, logout, user } = useAuth();
    const [view, setView] = useState('list');
    const [selectedEvent, setSelectedEvent] = useState(null);

    if (!isAuthenticated) return <LoginRegister />;

    const loadEvent = async (id) => {
        const data = await api(`/events/${id}`);
        setSelectedEvent(data);
        setView('map');
    };

    return (
        <div style={s.container}>
            <div style={s.header}>
                <div><strong>eTicket</strong> | Zalogowany: {user?.email}</div>
                <div>
                    <button style={s.navBtn} onClick={() => setView('list')}>Wydarzenia</button>
                    <button style={s.navBtn} onClick={() => setView('tickets')}>Bilety</button>
                    <button style={s.navBtn} onClick={() => setView('cart')}>Koszyk</button>
                    <button style={{ ...s.navBtn, background: '#dc3545', color: '#fff' }} onClick={logout}>Wyloguj</button>
                </div>
            </div>

            {view === 'list' && <EventList onSelect={loadEvent} />}
            {view === 'map' && selectedEvent && (
                <SectorMap
                    event={selectedEvent}
                    onBack={() => setView('list')}
                    onAddToCart={() => setView('cart')}
                />
            )}
            {view === 'cart' && <Cart onClose={() => setView('list')} />}
            {view === 'tickets' && <MyTickets />}
        </div>
    );
}
