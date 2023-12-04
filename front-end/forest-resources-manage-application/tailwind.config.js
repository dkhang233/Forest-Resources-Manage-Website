/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
    "./node_modules/tw-elements/dist/js/**/*.js",
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
  plugins: [require("tw-elements/dist/plugin")],
  darkMode: "class",
}

