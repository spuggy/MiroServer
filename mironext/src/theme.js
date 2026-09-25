import { createTheme } from "@mui/material/styles";

// Direction A ("clean and calm"): white surfaces on a soft grey page, MiRo
// blue for actions (deepened from #43add5 so white text on it passes contrast),
// the four MiRo colours kept for brand moments and status.
export const miroColours = {
  red: "#d53f35",
  yellow: "#fbc726",
  blue: "#43add5",
  green: "#42b449",
};

const ink = "#1d2330";
const muted = "#5b6475";
const border = "#e6e8ec";
const primary = "#17739a";

const theme = createTheme({
  palette: {
    mode: "light",
    primary: { main: primary, dark: "#0f5876", light: "#eaf4f9", contrastText: "#ffffff" },
    secondary: { main: miroColours.red },
    success: { main: "#2f9a3e", light: "#e7f6e9", dark: "#1d6b2a" },
    warning: { main: "#d99a00", light: "#fff4d6", dark: "#7a5300" },
    info: { main: "#2a8fbb", light: "#e6f2f8", dark: "#0f5876" },
    error: { main: "#c2362d" },
    background: { default: "#f6f7f9", paper: "#ffffff" },
    text: { primary: ink, secondary: muted },
    divider: border,
  },
  typography: {
    fontFamily: "var(--font-figtree), system-ui, -apple-system, 'Segoe UI', sans-serif",
    fontSize: 14,
    h1: { fontSize: 30, fontWeight: 700, letterSpacing: "-0.3px", lineHeight: 1.2 },
    h2: { fontSize: 22, fontWeight: 700, letterSpacing: "-0.2px" },
    h3: { fontSize: 18, fontWeight: 600 },
    h4: { fontSize: 30, fontWeight: 700, letterSpacing: "-0.3px", color: ink },
    h5: { fontSize: 20, fontWeight: 600 },
    h6: { fontSize: 17, fontWeight: 600 },
    subtitle1: { color: muted },
    body1: { fontSize: 15 },
    body2: { fontSize: 14 },
    button: { textTransform: "none", fontWeight: 600, fontSize: 14 },
  },
  shape: { borderRadius: 10 },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: { backgroundColor: "#f6f7f9" },
        a: { color: primary, textDecoration: "none" },
        "a:hover": { color: "#0f5876" },
        "*:focus-visible": { outline: `3px solid ${primary}55`, outlineOffset: 2 },
      },
    },
    MuiButton: {
      defaultProps: { disableElevation: true },
      styleOverrides: {
        root: { borderRadius: 8, paddingInline: 16, minHeight: 36 },
        sizeLarge: { minHeight: 44, paddingInline: 20, fontSize: 15 },
        sizeSmall: { minHeight: 32, paddingInline: 12 },
        outlined: {
          borderColor: "#d9dde3",
          color: ink,
          backgroundColor: "#ffffff",
          "&:hover": { borderColor: "#c3c9d1", backgroundColor: "#f6f7f9" },
        },
        outlinedPrimary: {
          borderColor: primary,
          color: primary,
          "&:hover": { borderColor: "#0f5876", backgroundColor: "#eaf4f9" },
        },
        text: { color: primary },
      },
    },
    MuiIconButton: {
      styleOverrides: { root: { borderRadius: 8, color: "#6b7384" } },
    },
    MuiPaper: {
      styleOverrides: { rounded: { borderRadius: 14 } },
    },
    MuiCard: {
      defaultProps: { elevation: 0 },
      styleOverrides: { root: { border: `1px solid ${border}`, borderRadius: 14 } },
    },
    MuiDialog: {
      styleOverrides: { paper: { borderRadius: 14 } },
    },
    MuiDialogTitle: {
      styleOverrides: { root: { fontSize: 19, fontWeight: 700, paddingTop: 22 } },
    },
    MuiDialogActions: {
      styleOverrides: { root: { padding: "12px 24px 20px", gap: 4 } },
    },
    MuiTableCell: {
      styleOverrides: {
        root: {
          borderBottom: "1px solid #f0f2f4",
          fontSize: 15,
          paddingTop: 14,
          paddingBottom: 14,
        },
        head: {
          backgroundColor: "#fafbfc",
          borderBottom: "1px solid #eef0f3",
          color: muted,
          fontSize: 13,
          fontWeight: 600,
          paddingTop: 11,
          paddingBottom: 11,
        },
        sizeSmall: { paddingTop: 10, paddingBottom: 10 },
      },
    },
    MuiTableRow: {
      styleOverrides: {
        root: {
          "&:last-child td": { borderBottom: 0 },
          "&.MuiTableRow-hover:hover": { backgroundColor: "#fafbfc" },
        },
      },
    },
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          borderRadius: 10,
          backgroundColor: "#ffffff",
          "& .MuiOutlinedInput-notchedOutline": { borderColor: "#d9dde3" },
          "&:hover .MuiOutlinedInput-notchedOutline": { borderColor: "#b9c0ca" },
        },
      },
    },
    MuiTextField: {
      defaultProps: { variant: "outlined", size: "small" },
    },
    MuiChip: {
      styleOverrides: { root: { fontWeight: 600, borderRadius: 999 } },
    },
    MuiAlert: {
      styleOverrides: { root: { borderRadius: 10 } },
    },
    MuiMenu: {
      styleOverrides: { paper: { borderRadius: 10, border: `1px solid ${border}` } },
    },
    MuiLinearProgress: {
      styleOverrides: { root: { borderRadius: 4 } },
    },
  },
});

export default theme;
