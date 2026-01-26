import React, { useState, useEffect } from 'react';
import { api } from '../api';
import { styles as s } from '../styles/styles';

export default function Cart({ onClose }) {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api('/orders/cart/current')
            .then(setCart)
            .catch(() => setCart(null))
            .finally(() => setLoading(false));
    }, []);

    const checkout = async () => {
        if (!cart) return;
        try {
            await api(`/orders/${cart.id}/checkout`, { method: 'POST' });
            alert('Sukces! Bilety kupione!');
            onClose();
        } catch (e) {
            alert('Blad: ' + e.message);
        }
    };

    const removeItem = async (itemId) => {
        try {
            await api(`/orders/${cart.id}/items/${itemId}`, { method: 'DELETE' });
            const newCart = await api('/orders/cart/current');
            setCart(newCart);
        } catch (e) {
            console.error(e);
        }
    };

    if (loading) return <div style={s.box}>Ladowanie koszyka...</div>;

    if (!cart || !cart.items || cart.items.length === 0) {
        return (
            <div style={s.box}>
                <h2>Koszyk</h2>
                <p>Koszyk jest pusty.</p>
                <button onClick={onClose} style={s.navBtn}>Wroc do wydarzen</button>
            </div>
        );
    }

    return (
        <div style={s.box}>
            <h2>Twoj Koszyk</h2>
            <table style={s.table}>
                <thead>
                <tr>
                    <th style={s.th}>Wydarzenie</th>
                    <th style={s.th}>Szczegoly</th>
                    <th style={s.th}>Cena</th>
                    <th style={s.th}>Usun</th>
                </tr>
                </thead>
                <tbody>
                {cart.items.map(item => (
                    <tr key={item.id}>
                        <td style={s.td}><strong>{item.eventName}</strong></td>
                        <td style={s.td}>
                            {item.sectorName}
                            {item.row ? ` | Rzad ${item.row}, Miejsce ${item.seatNumber}` : ` | Ilosc: ${item.quantity || 1}`}
                            <br /><small style={{ color: '#666' }}>{item.ticketTypeName}</small>
                        </td>
                        <td style={s.td}>{item.totalPriceGross.toFixed(2)} PLN</td>
                        <td style={s.td}>
                            <button onClick={() => removeItem(item.id)} style={s.btnRed}>X</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div style={{ marginTop: '20px', padding: '15px', background: '#d4edda', borderRadius: '5px' }}>
                <h3 style={{ margin: 0 }}>Do zaplaty: {cart.totalPriceGross.toFixed(2)} PLN</h3>
            </div>

            <div style={{ marginTop: '15px' }}>
                <button onClick={checkout} style={{ ...s.btnGreen, fontSize: '16px', padding: '12px 30px' }}>
                    ZAPLAC
                </button>
                <button onClick={onClose} style={{ ...s.navBtn, marginLeft: '10px' }}>Wroc</button>
            </div>
        </div>
    );
}
