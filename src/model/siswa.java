package model;

import java.util.ArrayList;
import java.util.List;

public class siswa {
    private String idSiswa;
    private String nama;
    private String email;
    private String nomorTelepon;   
    private List<String> daftarKursus;
    private List<String> lihatRiwayatPendaftaran;
    
    // Constructor
    public siswa(String idSiswa, String nama, String email, String nomorTelepon) {
        this.idSiswa = idSiswa;
        this.nama = nama;
        this.email = email;
        this.nomorTelepon = nomorTelepon;

        this.daftarKursus = new ArrayList<>();
        this.lihatRiwayatPendaftaran = new ArrayList<>();
    }

    // membuat method getName()
    public String getName() {
        return this.nama;
    }

    // menambhakan method kursus apa yang berhasil diambil
    public void tambahKursus(String kodeKursus) {
        if (!daftarKursus.contains(kodeKursus)){
            daftarKursus.add(kodeKursus);
        }
    }

    // penambahan method untuk menambahkan riwayat pendaftaran
    public void tambahRiwayat(String riwayat) {
        lihatRiwayatPendaftaran.add(riwayat);
    }

    //getter
    public String getIdSiswa() {
        return idSiswa;
    } 
    public String getNama() {
        return nama;
    } 
    public List<String> getDaftarKursus() {
        return daftarKursus;
    }public List<String> getLihatRiwayatPendaftaran() {
        return lihatRiwayatPendaftaran;
    }
}

