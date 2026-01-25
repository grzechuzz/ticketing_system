import React from 'react';
import { useAuth } from './context/AuthContext';
import LoginRegister from './components/LoginRegister';

export default function App() {
    const { isAuthenticated, user, logout } = useAuth();

    if (!isAuthenticated) {
        return <LoginRegister />;
    }

    return (
        <div style={{ padding: '20px', fontFamily: 'Arial' }}>
            <h1>Witaj, {user?.email}!</h1>
            <p>Udało Ci się zalogować.</p>
            <p>Reszta aplikacji (EventList, Cart itd.) pojawi się tutaj, gdy utworzysz odpowiednie pliki w folderze components.</p>
            <button
                onClick={logout}
                style={{ padding: '10px 20px', background: 'red', color: 'white', border: 'none', cursor: 'pointer' }}
            >
                Wyloguj się
            </button>
        </div>
    );
}