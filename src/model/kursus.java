package model;

import java.util.ArrayList;
import java.util.List;

public abstract class kursus {
    protected String kodeKursus;
    protected String nama;
    protected int kuotaMaksimal; // Berubah dari String ke int
    protected int jumlahSiswaTerdaftar;
    protected List<siswa> daftarSiswa;

    // Constructor diperbarui untuk menerima int
    public kursus(String kodeKursus, String nama, int kuotaMaksimal) {
        this.kodeKursus = kodeKursus;
        this.nama = nama;
        this.kuotaMaksimal = kuotaMaksimal;
        this.jumlahSiswaTerdaftar = 0;
        this.daftarSiswa = new ArrayList<>();
    }

    // method abstract
    public abstract double getBiaya();

    // Method lebih sederhana tanpa perlu try-catch parsing
    public boolean cekKetersediaan() {
        return jumlahSiswaTerdaftar < kuotaMaksimal;
    }

    // method untuk menambahkan siswa ke dalam kursus
    public void tambahSiswa(siswa siswa) throws kuotaPenuhException {
        if (cekKetersediaan()) {
            daftarSiswa.add(siswa);
            jumlahSiswaTerdaftar++;
        } else {
            throw new kuotaPenuhException(
                this.kodeKursus, 
                "Maaf, Kursus " + this.nama + " sudah penuh mencapai kuota maksimal. (" + this.kuotaMaksimal + " siswa)"
            );
        }
    }

    // getter
    public String getKodeKursus() {
        return kodeKursus;
    } 

    public String getNama() {
        return nama;
    }

    public int getKuotaMaksimal() { // Return type berubah menjadi int
        return kuotaMaksimal;
    }  

    public int getJumlahSiswaTerdaftar() {
        return jumlahSiswaTerdaftar;
    }

    public List<siswa> getDaftarSiswa() {
        return daftarSiswa;
    }

    @Override
    public String toString() {
        return this.nama + " (" + this.kodeKursus + ")";
    }
}