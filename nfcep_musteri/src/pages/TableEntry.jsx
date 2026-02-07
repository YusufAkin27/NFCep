import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function TableEntry() {
  const [tableNumber, setTableNumber] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    const num = tableNumber.trim();
    if (!num) {
      setError('Masa numarası girin');
      return;
    }
    setError('');
    navigate(`/table/${encodeURIComponent(num)}`);
  };

  return (
    <div className="table-entry">
      <h1>NFCep</h1>
      <p className="subtitle">Masa numaranızı girin</p>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={tableNumber}
          onChange={(e) => setTableNumber(e.target.value)}
          placeholder="Örn: 5"
          autoFocus
          autoComplete="off"
        />
        {error && <p className="error">{error}</p>}
        <button type="submit">Menüye git</button>
      </form>
    </div>
  );
}
