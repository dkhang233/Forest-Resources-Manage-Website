/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
      },
      fontFamily: {
        roboto: ["Roboto , sans-serif"],
        sans: ['Mulish', 'sans-serif'],
        mono: ['Rokkitt', 'monospace'],
      },
      container: {
        padding: "10px",
        center: true,
      }
    },
  },
  plugins: [],
}

