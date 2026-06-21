/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        brand: {
          50: "#f5f7ff",
          100: "#e8ecff",
          500: "#315efb",
          600: "#284fe0",
          900: "#1d2b6b",
        },
        accent: "#f97316",
      },
      boxShadow: {
        soft: "0 18px 45px rgba(49, 94, 251, 0.12)",
      },
    },
  },
  plugins: [],
};
