import { render, screen } from '@testing-library/react';
import App from './App';

jest.mock('./App.css', () => ({}));

test('renders the landing page', () => {
  render(<App />);
});