import { Menu, MenuItem } from "@mui/material";
import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { BiUser } from "react-icons/bi";
import { useDispatch, useSelector } from "react-redux";
import { FaShoppingCart, FaUserShield } from "react-icons/fa";
import { IoExitOutline } from "react-icons/io5";
import BackDrop from "./BackDrop";
import { logOutUser } from "../../store/action";

const UserMenu = () => {
    const id = React.useId();
    const buttonId = `${id}-button`;
    const menuId = `${id}-menu`;
    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const { user } = useSelector((state) => state.auth);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const isAdmin = user && user?.roles.includes("ROLE_ADMIN");
    const isSeller = user && user?.roles.includes("ROLE_SELLER");

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const logOutHandler = () => {
        dispatch(logOutUser(navigate));
    };
    const handleClose = () => {
        setAnchorEl(null);
    }



    return (
        <div className="relative z-30">
            <div
                className='flex flex-row items-center gap-1 rounded-full cursor-pointer hover:bg-slate-700 transition p-1'
                onClick={handleClick}
            >
                <div className="w-9 h-9 rounded-full bg-gradient-to-br from-purple-500 to-blue-500 flex items-center justify-center text-white shadow-md">
                    <BiUser size={20} />
                </div>
            </div>

            <Menu
                sx={{width:"400px" }}
                id={menuId}
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                slotProps={{
                list: {
                    'aria-labelledby': buttonId,
                    sx:{width:160},
                },
                }}
            >
                <Link to="/profile">
                    <MenuItem className="flex gap-2 " onClick={handleClose}>
                        <BiUser className="text-slate-700" />
                        <span className="font-bold text-[16px] mt-1 text-slate-800">{user?.username}</span> 
                    </MenuItem>
                </Link>

                <Link to="/profile/orders">
                    <MenuItem className="flex gap-2 " onClick={handleClose}>
                        <FaShoppingCart className="text-xl" />
                        <span className="font-semibold">Order</span> 
                    </MenuItem>
                </Link>

                { (isAdmin || isSeller) && (
                    <Link to={isAdmin ? "/admin" : "/admin/orders"}>
                        <MenuItem className="flex gap-2" 
                            onClick={handleClose}>
                                <FaUserShield className='text-xl'/>
                                <span className='font-semibold'>
                                    {isAdmin ? "Admin Panel" : "Seller Panel"}
                                </span>
                        </MenuItem>
                    </Link> 
                )}  
                
                
                <MenuItem className="flex gap-2 " onClick={logOutHandler}>
                    <div className="font-semibold w-full flex gap-2 items-center bg-gradient-to-r from-blue-500 to-purple-500 px-4 py-1 text-white rounded-md">
                        <IoExitOutline className="text-xl" />
                        <span className="font-bold text-[16px] mt-1">Logout</span> 
                    </div>
                    
                </MenuItem>

            </Menu>
            {open && <BackDrop />}
        </div>
    );
};

export default UserMenu;