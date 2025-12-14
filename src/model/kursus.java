package model;

import java.util.ArrayList;
import java.util.List;

public abstract class kursus {
    protected String kodeKursus;
    protected String nama;
    protected String kuotaMaksimal; 
    protected int jumlahSiswaTerdaftar;
    protected List<siswa> daftarSiswa;

    // Constructor
    public kursus(String kodeKursus, String nama, String kuotaMaksimal) {
        this.kodeKursus = kodeKursus;
        this.nama = nama;
        this.kuotaMaksimal = kuotaMaksimal;
        this.jumlahSiswaTerdaftar = 0;
        this.daftarSiswa = new ArrayList<>();
    }

    // methode abstract
    public abstract  double getBiaya();

    // method untuk mengecek ketersediaan kursus
    public boolean cekKetersediaan() {
        // Mengubah String kuotaMaksimal ke Integer untuk perbandingan
        try {
            int kuota = Integer.parseInt(kuotaMaksimal);
            return jumlahSiswaTerdaftar < kuota;
        } catch (NumberFormatException e) {
            // Penanganan jika kuotaMaksimal tidak valid (String bukan angka)
            System.err.println("Error: Kuota Maksimal bukan angka yang valid: " + kuotaMaksimal);
            return false; 
        }
    }

    // method untuk menambahkan siswa ke dalam kursus
    public void tambahSiswa(siswa siswa) throws kuotaPenuhException {
        if (cekKetersediaan()) {
            daftarSiswa.add(siswa);
            jumlahSiswaTerdaftar++;
        } else {
            throw new kuotaPenuhException(
                this.kodeKursus, "Maaf,Kursus " + this.nama + " sudah penuh mencapai kuota maksimal. (" + this.kuotaMaksimal + " siswa)"
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
    public String getKuotaMaksimal() {
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
