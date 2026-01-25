import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { api } from '../api';
import { styles as s } from '../styles/styles';

export default function LoginRegister() {
    const [isLogin, setIsLogin] = useState(true);
    const [form, setForm] = useState({ email: '', password: '', repeatPassword: '', firstName: '', lastName: '' });
    const [error, setError] = useState('');
    const { login } = useAuth();

    const switchMode = () => {
        setIsLogin(!isLogin);
        setForm({ email: '', password: '', repeatPassword: '', firstName: '', lastName: '' });
        setError('');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!isLogin) {
            if (form.password !== form.repeatPassword) {
                setError('Hasla nie sa takie same');
                return;
            }
            if (form.password.length < 8) {
                setError('Haslo musi miec minimum 8 znakow');
                return;
            }
        }

        try {
            const endpoint = isLogin ? '/auth/login' : '/auth/register';
            const data = await api(endpoint, { method: 'POST', body: JSON.stringify(form) });
            login(data);
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div style={{ maxWidth: '400px', margin: '50px auto', border: '2px solid #333', padding: '30px', background: '#fff' }}>
            <h2 style={{ textAlign: 'center' }}>{isLogin ? 'Logowanie' : 'Rejestracja'}</h2>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={handleSubmit}>
                <input
                    placeholder="Email"
                    type="email"
                    style={s.input}
                    value={form.email}
                    onChange={e => setForm({ ...form, email: e.target.value })}
                    required
                />
                {!isLogin && (
                    <>
                        <input
                            placeholder="Imie"
                            style={s.input}
                            value={form.firstName}
                            onChange={e => setForm({ ...form, firstName: e.target.value })}
                            required
                        />
                        <input
                            placeholder="Nazwisko"
                            style={s.input}
                            value={form.lastName}
                            onChange={e => setForm({ ...form, lastName: e.target.value })}
                            required
                        />
                    </>
                )}
                <input
                    type="password"
                    placeholder={isLogin ? "Haslo" : "Haslo (min 8 znakow)"}
                    style={s.input}
                    value={form.password}
                    onChange={e => setForm({ ...form, password: e.target.value })}
                    required
                />
                {!isLogin && (
                    <input
                        type="password"
                        placeholder="Powtorz haslo"
                        style={s.input}
                        value={form.repeatPassword}
                        onChange={e => setForm({ ...form, repeatPassword: e.target.value })}
                        required
                    />
                )}
                <button type="submit" style={{ ...s.btn, width: '100%' }}>
                    {isLogin ? 'Zaloguj sie' : 'Zarejestruj sie'}
                </button>
            </form>
            <p style={{ textAlign: 'center', marginTop: '15px' }}>
                <button
                    onClick={switchMode}
                    style={{ background: 'none', border: 'none', color: 'blue', cursor: 'pointer', textDecoration: 'underline' }}
                >
                    {isLogin ? 'Nie mam konta - Rejestracja' : 'Mam konto - Logowanie'}
                </button>
            </p>
        </div>
    );
}
