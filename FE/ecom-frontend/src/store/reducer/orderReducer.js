const initialState = {
    
    adminOrders: null,
    pagination: {},
}

export const orderReducer = (state = initialState, action) => {
    switch (action.type) {
        case "GET_ADMIN_ORDERS":
            return {
                ...state,
                adminOrders: action.payload.content,
                pagination: {
                    ...state.pagination,
                    pageNumber: action.payload.pageNumber,
                    pageSize: action.payload.pageSize,
                    totalElements: action.payload.totalElements,
                    totalPages: action.payload.totalPages,
                    lastPage: action.payload.lastPage,
                }
            }
        default:
            return state;
    }
};