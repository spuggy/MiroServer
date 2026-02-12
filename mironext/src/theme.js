import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    mode: "light",
    primary: {
      main: "#44aed6",
    },
    secondary: {
      main: "#d63f36",
    },
    background: {
      default: "#ffffff",
      paper: "#ffffff",
    },
    text: {
      primary: "#292929",
      secondary: "#6b6b6b",
    },
  },
  typography: {
    fontFamily: "Expressway, 'Trebuchet MS', Arial, sans-serif",
    fontSize: 14,
    h4: {
      fontWeight: 400,
      color: "#6b6b6b",
      letterSpacing: "1px",
    },
  },
  shape: {
    borderRadius: 4,
  },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          paddingTop: "5px",
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          border: "1px solid #e5e5e5",
          boxShadow: "none",
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        contained: {
          color: "#ffffff",
          textShadow: "0 -1px 0 rgba(0,0,0,0.2)",
          border: "1px solid #2688ad",
          backgroundImage: "linear-gradient(to bottom, #44aed6 0%, #288eb5 100%)",
          "&:hover": {
            backgroundImage: "none",
            backgroundColor: "#288eb5",
          },
        },
        outlined: {
          borderColor: "#d9d9d9",
          color: "#292929",
          backgroundColor: "#fff",
          "&:hover": {
            borderColor: "#c7c7c7",
            backgroundColor: "#f2f2f2",
          },
        },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        head: {
          backgroundColor: "#f7f7f7",
          borderTop: "1px solid #e5e5e5",
          borderBottom: "1px solid #e5e5e5",
          color: "#6b6b6b",
          fontWeight: 500,
        },
        body: {
          borderBottom: "1px solid #eeeeee",
        },
      },
    },
    MuiTableRow: {
      styleOverrides: {
        root: {
          "&:hover td": {
            backgroundColor: "#f9f9f9",
          },
        },
      },
    },
    MuiTextField: {
      defaultProps: {
        variant: "outlined",
        size: "small",
      },
    },
  },
});

export default theme;
