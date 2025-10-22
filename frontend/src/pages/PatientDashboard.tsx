import { useEffect, useMemo, useState } from 'react';
import { api } from '../lib/api';
import { useNavigate } from 'react-router-dom';

interface MemoryData { id: number; category: string; title: string; detail: string; }
interface Reminder { id: number; text: string; done: boolean; remindAt?: string }

export function PatientDashboard() {
  const [memories, setMemories] = useState<MemoryData[]>([]);
  const [reminders, setReminders] = useState<Reminder[]>([]);
  const [newReminder, setNewReminder] = useState('');
  const navigate = useNavigate();

  async function load() {
    const [m, r] = await Promise.all([
      api.get('/memory'),
      api.get('/reminders'),
    ]);
    setMemories(m.data);
    setReminders(r.data);
  }

  useEffect(() => { load(); }, []);

  const fact = useMemo(() => {
    if (!memories.length) return 'Welcome back!';
    const m = memories[Math.floor(Math.random() * memories.length)];
    return `${m.title}: ${m.detail}`;
  }, [memories]);

  async function addReminder(e: React.FormEvent) {
    e.preventDefault();
    if (!newReminder.trim()) return;
    await api.post('/reminders', { text: newReminder });
    setNewReminder('');
    const r = await api.get('/reminders');
    setReminders(r.data);
  }

  return (
    <div className="min-h-screen p-4 space-y-6 bg-gray-50">
      <section className="bg-white rounded-xl shadow p-4">
        <h2 className="text-xl font-semibold">Hi there</h2>
        <p className="text-gray-700 mt-2">{fact}</p>
      </section>

      <section className="bg-white rounded-xl shadow p-4">
        <div className="flex items-center justify-between mb-3">
          <h3 className="font-semibold">Quiz</h3>
          <button className="text-blue-600" onClick={() => navigate('/quiz')}>Start</button>
        </div>
        <p className="text-sm text-gray-600">Answer multiple-choice questions to reinforce your memories.</p>
      </section>

      <section className="bg-white rounded-xl shadow p-4">
        <h3 className="font-semibold mb-2">Reminders</h3>
        <form onSubmit={addReminder} className="flex gap-2">
          <input className="flex-1 border rounded px-3 py-2" placeholder="Add a reminder" value={newReminder} onChange={e => setNewReminder(e.target.value)} />
          <button className="bg-blue-600 text-white rounded px-3 py-2">Add</button>
        </form>
        <ul className="mt-3 space-y-1">
          {reminders.map(r => (
            <li key={r.id} className="text-sm text-gray-700">{r.text}</li>
          ))}
        </ul>
      </section>
    </div>
  );
}
