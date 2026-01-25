const API_BASE = 'http://localhost:8080/api';

export async function api(endpoint, options = {}) {
    const token = localStorage.getItem('token');
    const headers = {
        'Content-Type': 'application/json',
        ...(token && { Authorization: `Bearer ${token}` }),
    };

    const response = await fetch(`${API_BASE}${endpoint}`, { ...options, headers });
    if (response.status === 204) return null;

    const contentType = response.headers.get('content-type');
    if (contentType?.includes('application/json')) {
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'API Error');
        return data;
    }
    if (!response.ok) throw new Error('API Error');
    return response;
}

export { API_BASE };
