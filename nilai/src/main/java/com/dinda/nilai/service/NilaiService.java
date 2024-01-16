/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dinda.nilai.service;

import com.dinda.nilai.entity.Nilai;
import com.dinda.nilai.repository.NilaiRepository;
import com.dinda.nilai.vo.Mahasiswa;
import com.dinda.nilai.vo.Matakuliah;
import com.dinda.nilai.vo.ResponseTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ASUS
 */
@Service
public class NilaiService {

    @Autowired
    private NilaiRepository nilaiRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void insert(Nilai nilai) {
        Optional<Nilai> mhsOptional = nilaiRepository.findNilaiById(nilai.getId());
        if (mhsOptional.isPresent()) {
            throw new IllegalStateException("Matakuliah sudah ada");
        }
        nilaiRepository.save(nilai);
    }

    public List<Nilai> getAll() {
        return nilaiRepository.findAll();
    }

    public ResponseTemplate getNilaiById(Long id) {
        ResponseTemplate vo = new ResponseTemplate();
        Nilai nilai = (Nilai) nilaiRepository.findById(id).get();
        Mahasiswa mahasiswa = restTemplate.getForObject("http://localhost:9001/api/v1/mahasiswa/" + nilai.getIdmahasiswa(), Mahasiswa.class);
        Matakuliah matakuliah = restTemplate.getForObject("http://localhost:9004/api/v1/matakuliah/" + nilai.getIdmatakuliah(), Matakuliah.class);

        vo.setNilai(nilai);
        vo.setMahasiswa(mahasiswa);
        vo.setMatakuliah(matakuliah);
        return vo;
    }

    public void update(Nilai nilai) {
        Long id = nilai.getId();
        Optional<Nilai> existingNilaiOptional = nilaiRepository.findById(id);

        if (existingNilaiOptional.isPresent()) {
            Nilai existingNilai = (Nilai) existingNilaiOptional.get();

            existingNilai.setNilai(nilai.getNilai());
            existingNilai.setIdmahasiswa(nilai.getIdmahasiswa());
            existingNilai.setIdmatakuliah(nilai.getIdmatakuliah());

            nilaiRepository.save(existingNilai);
        } else {
            throw new IllegalStateException("Mahasiswa dengan id " + id + " tidak ditemukan");
        }
    }

    public void delete(Long id) {
        nilaiRepository.deleteById(id);
    }
}