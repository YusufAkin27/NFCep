import { BrowserRouter, Routes, Route } from 'react-router-dom';
import TableEntry from './pages/TableEntry';
import TableMenu from './pages/TableMenu';
import './App.css';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<TableEntry />} />
        <Route path="/table/:tableNumber" element={<TableMenu />} />
      </Routes>
    </BrowserRouter>
  );
}
