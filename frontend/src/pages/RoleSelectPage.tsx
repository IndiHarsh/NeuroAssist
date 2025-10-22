import { useNavigate } from 'react-router-dom';
import { useAuth } from '../store/auth';

export function RoleSelectPage() {
  const navigate = useNavigate();
  const { token } = useAuth();
  if (!token) navigate('/');

  return (
    <div className="min-h-screen grid place-items-center bg-gray-50 p-4">
      <div className="w-full max-w-md bg-white rounded-xl shadow p-6">
        <h2 className="text-xl font-semibold mb-4 text-center">Who are you?</h2>
        <div className="grid grid-cols-1 gap-3">
          <button className="border rounded-lg p-4 hover:bg-blue-50" onClick={() => navigate('/patient')}>Patient</button>
          <button className="border rounded-lg p-4 hover:bg-blue-50" onClick={() => navigate('/family')}>Family Member</button>
        </div>
      </div>
    </div>
  );
}
