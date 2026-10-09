
const initialState = {
    user: null,
    address: [],
    selectedUserCheckoutAddress: null,
    clientSecret: null,
}

export const authReducer = (state = initialState, action) => {

    switch(action.type){
        case "LOGIN_USER":
            return {
                ...state,
                user: action.payload,
            }
        
        case "USER_ADDRESS":
            return {
                ...state,
                address: action.payload, 
            } 
        
        case "SELECT_CHECKOUT_ADDRESS":
            return {
                ...state,
                selectedUserCheckoutAddress: action.payload,
            }

        case "REMOVE_CHECKOUT_ADDRESS":
            return {
                ...state,
                selectedUserCheckoutAddress: null,
            }
        
        
        case "LOGOUT_USER":
            return {
                ...state,
                user: null,
                address: null,
            }

        case "CLIENT_SECRET":
            return {
                ...state,
                clientSecret: action.payload,
            }
        case "REMOVE_CLIENT_SECRET_ADDRESS":
            return {
                ...state,
                clientSecret: null,
                selectedUserCheckoutAddress: null,
            }
        case "CLEAR_CART":
            return {
                ...state,
                cart: [],
                totalPrice: 0,
                cartId: null,
            }

        default:
            return state;
    }
}

