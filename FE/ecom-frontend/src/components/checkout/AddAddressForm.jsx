import { useForm } from "react-hook-form";
import InputFeild from "../shared/InputField";
import { FaAddressCard } from "react-icons/fa";
import { useDispatch, useSelector } from "react-redux";
import Spinners from "../shared/Spinners";
import toast from "react-hot-toast";
import { addUpdateUserAddress } from "../../store/action";
import { useEffect } from "react";



const AddAddressForm = ({address, setOpenAddressModal}) => {
    const dispatch = useDispatch();
    const {btnLoader} = useSelector((state) => state.errors);

    const {
        register,
        handleSubmit,
        reset,
        setValue,
        formState: { errors },
    
    } = useForm({
        mode: "onTouched",
    });

    const onSaveAddressHandler = async (data) => {
        console.log("Address data");
        dispatch(addUpdateUserAddress(
            data, 
            toast,
            address?.addressId, 
            setOpenAddressModal
        ));
    };

    useEffect(() => {
        if(address?.addressId){
            setValue("buildingName", address?.buildingName);
            setValue("city", address?.city);
            setValue("state", address?.state);
            setValue("pinCode", address?.pinCode);
            setValue("street", address?.street);
            setValue("country", address?.country);
        }
    }, [address]);

    return (
        <div className="">
            <form onSubmit={handleSubmit(onSaveAddressHandler)} 
                className="">
                    <div className="flex justify-center items-centermb-4 font-semibold text-2xl text-slate-800 py-2 px-4">
                        <FaAddressCard className="mr-2 text-2xl" />
                        {!address?.addressId ? "Add Address" : "Update Address"}
                    </div>


                    <div className="flex flex-col gap-4">
                        <InputFeild label="Buillding Name"
                                    required
                                    id="building"
                                    type="text"
                                    message="*Buillding Name is required"
                                    placeholder="Enter your building name"
                                    register={register}
                                    errors={errors}
                        />

                        <InputFeild label="City"
                                    required
                                    id="city"
                                    type="text"
                                    message="*City is required"
                                    placeholder="Enter your city"
                                    register={register}
                                    errors={errors}
                        />

                        <InputFeild label="State"
                                    required
                                    id="state"
                                    type="text"
                                    message="*State is required"
                                    placeholder="Enter your state"
                                    register={register}
                                    errors={errors}
                        />

                        <InputFeild label="Pincode"
                                    required
                                    id="pinCode"
                                    type="text"
                                    message="*Pincode is required"
                                    placeholder="Enter your pincode"
                                    register={register}
                                    errors={errors}
                        />

                        <InputFeild label="Street"
                                    required
                                    id="street"
                                    type="text"
                                    message="*Street is required"
                                    placeholder="Enter your street"
                                    register={register}
                                    errors={errors}
                        />

                        <InputFeild label="Country"
                                    required
                                    id="country"
                                    type="text"
                                    message="*Country is required"
                                    placeholder="Enter your country"
                                    register={register}
                                    errors={errors}
                        />


                        <button disabled={btnLoader} 
                            className="text-white bg-[#3e66ea] px-4 py-2 rounded-md mt-4"
                            type="submit">
                            { btnLoader ? (
                                <>
                                    <Spinners/> Loading...
                                </>
                            ) : (
                                <>Save</>
                            )}
                        </button>


                    </div>
            </form>
        </div>
    )
}

export default AddAddressForm