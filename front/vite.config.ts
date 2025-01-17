import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/reglements': {
        target: 'http://localhost:8092',
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
