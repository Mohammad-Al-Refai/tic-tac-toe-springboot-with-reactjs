import { createBrowserRouter } from "react-router-dom";
import App from "../App";

export const AppRoutes = createBrowserRouter([
  {
    path: "/",
    element: <App />,
  },
]);
