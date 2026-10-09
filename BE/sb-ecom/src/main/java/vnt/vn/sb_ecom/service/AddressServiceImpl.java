package vnt.vn.sb_ecom.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vnt.vn.sb_ecom.handler.ResourceNotFoundException;
import vnt.vn.sb_ecom.model.Address;
import vnt.vn.sb_ecom.model.Users;
import vnt.vn.sb_ecom.payload.AddressDTO;
import vnt.vn.sb_ecom.repository.AddressRepository;
import vnt.vn.sb_ecom.repository.UserRepository;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, Users user) {

        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);


        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {

        List<Address> addresses = addressRepository.findAll();
        List<AddressDTO> addressDTOList = addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());

        return addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressId)
        );
        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);

        return addressDTO;
    }

    @Override
    public List<AddressDTO> getUserAddresses(Users user) {

        List<Address> addresses = user.getAddresses();

        return addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {

        Address addressFromDB = addressRepository.findById((addressId)).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressId)
        );

        addressFromDB.setCity(addressDTO.getCity());
        addressFromDB.setCountry(addressDTO.getCountry());
        addressFromDB.setBuildingName(addressDTO.getBuildingName());
        addressFromDB.setPinCode(addressDTO.getPinCode());
        addressFromDB.setState(addressDTO.getState());
        addressFromDB.setStreet(addressDTO.getStreet());

        Address updatedAddress = addressRepository.save(addressFromDB);

        Users user = addressFromDB.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {

        Address addressFromDB = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        Users user = addressFromDB.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));

        userRepository.save(user);
        addressRepository.delete(addressFromDB);

        return "Address with id " + addressId + " has been deleted successfully.";
    }
}
