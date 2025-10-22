import { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker } from 'react-leaflet';
// Work around TS types by importing types explicitly
import type { LatLngExpression } from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { api } from '../lib/api';

interface MemoryData { id: number; category: string; title: string; detail: string; }

export function FamilyDashboard() {
  const [memories, setMemories] = useState<MemoryData[]>([]);
  const [category, setCategory] = useState('');
  const [title, setTitle] = useState('');
  const [detail, setDetail] = useState('');
  const [position, setPosition] = useState<LatLngExpression | null>(null);

  async function loadMemories() {
    const res = await api.get('/memory');
    setMemories(res.data);
  }

  async function addMemory(e: React.FormEvent) {
    e.preventDefault();
    await api.post('/memory', { category, title, detail });
    setCategory(''); setTitle(''); setDetail('');
    loadMemories();
  }

  useEffect(() => { loadMemories(); }, []);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const ev = new EventSource(`/api/location/stream${token ? `?token=${token}` : ''}`);
    ev.onmessage = (msg) => {
      try {
        const data = JSON.parse(msg.data);
        setPosition([data.lat, data.lng]);
      } catch {}
    };
    ev.addEventListener('location', (e: MessageEvent) => {
      try {
        const data = JSON.parse(e.data);
        setPosition([data.lat, data.lng]);
      } catch {}
    });
    return () => ev.close();
  }, []);

  return (
    <div className="min-h-screen p-4 space-y-6 bg-gray-50">
      <header className="text-2xl font-bold">Family Dashboard</header>
      <section className="grid md:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl shadow p-4">
          <h3 className="font-semibold mb-2">Live Location</h3>
          <div className="h-64">
            <MapContainer center={(position ?? [20, 78]) as LatLngExpression} zoom={position ? 14 : 4} style={{ height: '100%', width: '100%' }}>
              <TileLayer
                attribution={'&copy; OpenStreetMap contributors'}
                url={"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"}
              />
              {position && <Marker position={position} />}
            </MapContainer>
          </div>
        </div>
        <div className="bg-white rounded-xl shadow p-4">
          <h3 className="font-semibold mb-2">Memory Data</h3>
          <form onSubmit={addMemory} className="space-y-2">
            <input className="w-full border rounded px-3 py-2" placeholder="Category" value={category} onChange={e => setCategory(e.target.value)} />
            <input className="w-full border rounded px-3 py-2" placeholder="Title" value={title} onChange={e => setTitle(e.target.value)} />
            <textarea className="w-full border rounded px-3 py-2" placeholder="Detail" value={detail} onChange={e => setDetail(e.target.value)} />
            <button className="bg-blue-600 text-white rounded px-3 py-2">Add</button>
          </form>
          <ul className="mt-4 space-y-1">
            {memories.map(m => (
              <li key={m.id} className="text-sm text-gray-700">{m.category} • {m.title}: {m.detail}</li>
            ))}
          </ul>
        </div>
      </section>
    </div>
  );
}
