import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { dashboardProductsAction, fetchProducts } from "../store/action";


const useProductFilter = () => {
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();
    

    useEffect(() => {
        const params = new URLSearchParams(searchParams);

        const currentPage = searchParams.get("page") ? Number(searchParams.get("page")) : 1;

        params.set("pageNumber", currentPage - 1);

        const sortOrder = searchParams.get("sortby") || "asc";
        const category = searchParams.get("category") || null;
        const keyword = searchParams.get("keyword") || null;
        params.set("sortby", "price");
        params.set("sortOrder", sortOrder);

        if(category) {
            params.set("category", category);
        }

        if(keyword){
            params.set("keyword", keyword);
        }

        const queryString = params.toString();
        console.log("QUERY STRING", queryString);

        dispatch(fetchProducts(queryString));



    }, [searchParams, dispatch]);
};

export const useDashboardProductFilter = () => {
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();
    const { user} = useSelector((state) => state.auth);
    const isAdmin = user && user?.roles?.includes("ROLE_ADMIN");

    useEffect(() => {
        const params = new URLSearchParams(searchParams);

        const currentPage = searchParams.get("page") ? Number(searchParams.get("page")) : 1;

        params.set("pageNumber", currentPage - 1);

        const sortOrder = searchParams.get("sortby") || "asc";
        const category = searchParams.get("category") || null;
        const keyword = searchParams.get("keyword") || null;
        params.set("sortby", "price");
        params.set("sortOrder", sortOrder);

        if(category) {
            params.set("category", category);
        }

        if(keyword){
            params.set("keyword", keyword);
        }

        const queryString = params.toString();
        dispatch(dashboardProductsAction(queryString, isAdmin));

    }, [searchParams, dispatch]);
};

export default useProductFilter;