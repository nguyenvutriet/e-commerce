
import {configureStore} from '@reduxjs/toolkit';
import { productReducer } from './reducer/ProductReducer';
import { errorReducer } from './reducer/errorReducer';
import { cartReducer } from './reducer/cartReducer';
import { authReducer } from './reducer/authReducer';
import { paymentMethodReducer } from './reducer/paymentMethodReducer';
import { adminReducer } from './reducer/adminReducer';
import { orderReducer } from './reducer/orderReducer';
import { sellerReducer } from './reducer/sellerReducer';


const user = localStorage.getItem("auth") 
    ? JSON.parse(localStorage.getItem("auth"))
    : null;

const cartItems = localStorage.getItem("cartItems") 
    ? JSON.parse(localStorage.getItem("cartItems"))
    : [];

const selectUserCheckoutAddress = localStorage.getItem("CHECKOUT_ADDRESS")
    ? JSON.parse(localStorage.getItem("CHECKOUT_ADDRESS"))
    : [];

const initialState = {
    auth: {user: user, selectUserCheckoutAddress: selectUserCheckoutAddress},
    cart: {cart: cartItems},
};


export const store = configureStore({
    reducer: {
        products: productReducer,
        errors: errorReducer,
        cart: cartReducer,
        auth: authReducer,
        payment: paymentMethodReducer,
        admin: adminReducer,
        order: orderReducer,
        seller: sellerReducer,
    },
    preloadedState: initialState


});

export default store;