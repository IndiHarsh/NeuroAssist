import { useEffect, useState } from 'react';
import { api } from '../lib/api';

interface MCQ { id: number; question: string; options: string[]; correctIndex: number }

export function QuizPage() {
  const [questions, setQuestions] = useState<MCQ[]>([]);
  const [current, setCurrent] = useState(0);
  const [selected, setSelected] = useState<number | null>(null);
  const [score, setScore] = useState(0);

  useEffect(() => {
    (async () => {
      const res = await api.post('/mcq/generate?count=5');
      setQuestions(res.data);
    })();
  }, []);

  function submitAnswer() {
    if (selected === null) return;
    if (selected === questions[current].correctIndex) setScore(s => s + 1);
    setSelected(null);
    setCurrent(c => c + 1);
  }

  if (!questions.length) return <div className="min-h-screen grid place-items-center">Loading...</div>;
  if (current >= questions.length) return (
    <div className="min-h-screen grid place-items-center">
      <div className="bg-white rounded-xl shadow p-6 text-center">
        <h2 className="text-xl font-semibold mb-2">Quiz Complete</h2>
        <p>Your score: {score} / {questions.length}</p>
      </div>
    </div>
  );

  const q = questions[current];

  return (
    <div className="min-h-screen grid place-items-center p-4 bg-gray-50">
      <div className="w-full max-w-xl bg-white rounded-xl shadow p-6">
        <div className="text-sm text-gray-500">Question {current + 1} of {questions.length}</div>
        <h2 className="text-xl font-semibold mt-2">{q.question}</h2>
        <div className="mt-4 grid gap-2">
          {q.options.map((opt, idx) => (
            <button key={idx} className={`border rounded px-3 py-2 text-left ${selected===idx?'border-blue-600 bg-blue-50':''}`} onClick={() => setSelected(idx)}>
              {opt}
            </button>
          ))}
        </div>
        <div className="mt-4 flex justify-end">
          <button className="bg-blue-600 text-white rounded px-4 py-2" onClick={submitAnswer}>Next</button>
        </div>
      </div>
    </div>
  );
}
