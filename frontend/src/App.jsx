import React, { useState } from 'react';
import { useAuth } from './context/AuthContext';
import { styles as s } from './styles/styles';

import LoginRegister from './components/LoginRegister';
import EventList from './components/EventList';

export default function App() {
    const { isAuthenticated, logout, user } = useAuth();
    const [view, setView] = useState('list');

    if (!isAuthenticated) return <LoginRegister />;

    const loadEvent = async (id) => {
        alert(`Wybrano wydarzenie ID: ${id}.`);
    };

    return (
        <div style={s.container}>
            <div style={s.header}>
                <div><strong>eTicket</strong> | Zalogowany: {user?.email}</div>
                <div>
                    <button style={s.navBtn} onClick={() => setView('list')}>Wydarzenia</button>

                    <button style={s.navBtn} onClick={() => alert('bilety nanana')}>Bilety</button>
                    <button style={s.navBtn} onClick={() => alert('koszyk nanana')}>Koszyk</button>

                    <button style={{ ...s.navBtn, background: '#dc3545', color: '#fff' }} onClick={logout}>Wyloguj</button>
                </div>
            </div>

            {view === 'list' && <EventList onSelect={loadEvent} />}

        </div>
    );
}