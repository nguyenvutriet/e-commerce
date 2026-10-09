import { FaBoxOpen, FaHome, FaShoppingCart, FaStore, FaThList } from "react-icons/fa";
import { bannderImageOne, bannderImageThree, bannderImageTwo } from "./constant";


export const bannerLists = [
    {
        id: 1,
        image: bannderImageOne,
        title: "Home Comfort",
        subtitle: "Living Room",
        description: "Upgrade your space with cozy and stylish sofas",
        bg: "#FFC107",
    },
    {
        id: 2,
        image: bannderImageTwo,
        title: "Summer Sale",
        subtitle: "New Arrivals",
        description: "Shop the latest trends at unbeatable prices",
        bg: "#FF7043",
    },
    {
        id: 3,
        image: bannderImageThree,
        title: "Best Sellers",
        subtitle: "Top Picks",
        description: "Discover our most loved products by customers",
        bg: "#42A5F5",
    },
];

export const adminNavigation = [
    {
        name: "Dashboard",
        href: "/admin",
        icon: FaHome,
        current: true
    },
    {
        name: "Orders",
        href: "/admin/orders",
        icon: FaShoppingCart
    },
    {
        name: "Products",
        href: "/admin/products",
        icon: FaBoxOpen
    },
    {
        name: "Categories",
        href: "/admin/categories",
        icon: FaThList
    },
    {
        name: "Sellers",
        href: "/admin/sellers",
        icon: FaStore
    }
];

export const sellerNavigation = [
    {
        name: "Orders",
        href: "/admin/orders",
        icon: FaShoppingCart,
        current: true
    },
    {
        name: "Products",
        href: "/admin/products",
        icon: FaBoxOpen
    }
];

