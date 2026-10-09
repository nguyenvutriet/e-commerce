import { Button, MenuItem } from "@headlessui/react";
import FormControl from "@mui/material/FormControl";
import FormHelperText from "@mui/material/FormHelperText";
import InputLabel from "@mui/material/InputLabel";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { updateOrderStatusFromDashboard } from "../../../store/action";
import toast from "react-hot-toast";
import Spinners from "../../shared/Spinners";


const ORDER_STATUSES = [
        "Pending",
        "Processing",
        "Shipped",
        "Delivered",
        "Cancelled",
        "Accepted"
];

const UpdateOrderForm = ({setOpen, selectedId, selectedItem, loader, setLoader}) => {
    
    const [orderStatus, setOrderStatus] = useState(selectedItem?.status || 'Accepted');
    const [error, setError] = useState("");
    const dispatch = useDispatch();
    const { user} = useSelector((state) => state.auth);
    const isAdmin = user && user?.roles?.includes("ROLE_ADMIN");

    const updateOrderStatus = (e) => {
        e.preventDefault();
        if(!orderStatus) {
            setError("Order status is required");
            return;
        }
        dispatch(updateOrderStatusFromDashboard(selectedId, orderStatus, toast, setLoader, isAdmin));
    }

    return (
        <div className='py-5 relative h-full'> 
            <form className='space-y-4' onSubmit={updateOrderStatus}>
                <FormControl fullWidth variant='outlined' error={!!error}>
                    <InputLabel id="order-status-label">Order Status</InputLabel>
                    <select 
                        labelId='order-status-label'
                        label='Order Status'
                        value={orderStatus}
                        onChange={(e) => {
                            setOrderStatus(e.target.value);
                            setError("");
                        }}>
                            {
                                ORDER_STATUSES.map((status) => (
                                    <MenuItem key={status} value={status}>
                                        {status}
                                    </MenuItem>
                                ))
                            }
                    </select>

                    {error && <FormHelperText>{error}</FormHelperText>}

                    <div className='flex w-full justify-between items-center absolute bottom-14 '>
                        <Button disabled={loader} onClick={() => setOpen(false)} variant='outlined' className='text-white py-[10px] px-4 text-sm font-medium'>
                            Cancel
                        </Button>

                        <Button disabled={loader} type='submit' variant='contained' className='bg-blue-500 text-white py-[10px] px-4 text-sm font-medium'>
                            {
                                loader ? (
                                    <div className='flex gap-2 items-center'>
                                        <Spinners /> Loading...
                                    </div>
                                ) : (
                                    "Update"
                                )
                            }
                        </Button>
                    </div>
                </FormControl>
            </form>
        </div>
    )
};

export default UpdateOrderForm;