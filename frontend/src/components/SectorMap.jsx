import React, { useState} from 'react';
import { styles as s } from '../styles/styles';

export default function SectorMap({ event, onBack, onAddToCart }) {
    const [selectedSector, setSelectedSector] = useState(null);

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
                            <div
                                key={sec.eventSectorId}
                                onClick={() => active && setSelectedSector(sec)}
                                style={{
                                    position: 'absolute',
                                    left: (sec.positionX || 50) + 'px',
                                    top: (sec.positionY || 80) + 'px',
                                    width: (sec.width || 120) + 'px',
                                    height: (sec.height || 100) + 'px',
                                    border: '2px solid #333',
                                    background: active ? (sec.standing ? '#90EE90' : '#87CEEB') : '#ccc',
                                    cursor: active ? 'pointer' : 'not-allowed',
                                    display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
                                    opacity: active ? 1 : 0.6
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

    return (
        <div style={s.box}>
            <button onClick={() => setSelectedSector(null)} style={s.navBtn}>Wroc do mapy</button>
            <h2>Wybrano: {selectedSector.name}</h2>
            <p>Szczegóły wyboru miejsc wkrótce...</p>
        </div>
    );
}