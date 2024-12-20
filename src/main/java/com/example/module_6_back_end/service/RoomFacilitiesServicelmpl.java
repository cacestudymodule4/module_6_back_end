package com.example.module_6_back_end.service;

import com.example.module_6_back_end.model.RoomFacilities;
import com.example.module_6_back_end.repository.RoomFacilitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomFacilitiesServicelmpl implements RoomFacilitiesService {
    @Autowired
    private RoomFacilitiesRepository roomFacilitiesRepository;

    @Override
    public Page<RoomFacilities> getAllRoomFacilities(Pageable pageable) {
        return roomFacilitiesRepository.findAllRoomFacilitiesOrderByIdDesc(pageable);
    }

    @Override
    public RoomFacilities getRoomFacilitiesById(Long id) {
        return roomFacilitiesRepository.findById(id).get();
    }

    @Override
    public void saveRoomFacilities(RoomFacilities roomFacilities) {
        roomFacilitiesRepository.save(roomFacilities);
    }

    @Override
    public void deleteRoomFacilitiesById(Long id) {
        roomFacilitiesRepository.deleteById(id);
    }

    @Override
    public Page<RoomFacilities> searchRoomFacilities(String facilitiesType, String facilitiesName, String ground, Pageable pageable) {
        return roomFacilitiesRepository.searchRoomFacilitiesBy(facilitiesType, facilitiesName, ground, pageable);
    }

    @Override
    public List<RoomFacilities> getListFacilities() {
        return  roomFacilitiesRepository.findAll();
    }
}
