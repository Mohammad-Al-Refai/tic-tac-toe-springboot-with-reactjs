import ReactDOM from "react-dom/client";
import "./core.css";
import "./App.css";
import { RouterProvider } from "react-router-dom";
import { AppRoutes } from "./routes/AppRoutes.tsx";
ReactDOM.createRoot(document.getElementById("root")!).render(
  <>
    <RouterProvider router={AppRoutes} />
  </>
);
