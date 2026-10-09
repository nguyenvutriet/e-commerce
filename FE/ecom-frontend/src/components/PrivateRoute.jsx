import { useSelector } from "react-redux";
import { Navigate, Outlet, useLocation } from "react-router-dom";




export const PrivateRoute = ({publicPage = false, adminOnly = false}) => {

    const {user} = useSelector((state) => state.auth);
    const isAdmin = user && user?.roles?.includes("ROLE_ADMIN");
    const isSeller = user && user?.roles?.includes("ROLE_SELLER");
    const location = useLocation();

    // Public pages (login, register): redirect logged-in users to home
    if(publicPage){
        return user ? <Navigate to="/" /> : <Outlet />;
    }

    // Admin/Seller only pages
    if(adminOnly){
        if(!user) return <Navigate to="/login" replace />;

        if(isSeller && !isAdmin){
            const sellerAllowedPaths = ["/admin/orders", "/admin/products"];
            const sellerAllowed = sellerAllowedPaths.some(path =>
                location.pathname.startsWith(path)
            );
            if(!sellerAllowed){
                return <Navigate to="/" replace />;
            }
        }

        if(!isAdmin && !isSeller){
            return <Navigate to="/" replace />;
        }

        return <Outlet />;
    }

    // Regular private pages (checkout, etc.): only require login
    return user ? <Outlet /> : <Navigate to="/login" replace />;
};

export default PrivateRoute;
