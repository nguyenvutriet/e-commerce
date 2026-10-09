package vnt.vn.sb_ecom.service;

import jakarta.validation.Valid;
import vnt.vn.sb_ecom.model.Users;
import vnt.vn.sb_ecom.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, Users user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddresses(Users user);

    AddressDTO updateAddress(Long addressId, @Valid AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
