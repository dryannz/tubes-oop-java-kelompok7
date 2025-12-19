package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID; // Untuk generate ID unik

public class sistemPendaftaran {
    private String idSistem;
    private List<kursus> daftarKursus;
    private List<pembayaran> daftarTransaksi;
    private List<siswa> daftarSiswa;

    // Konstruktor
    public sistemPendaftaran(String idSistem) {
        this.idSistem = idSistem;
        this.daftarKursus = new ArrayList<>();
        this.daftarTransaksi = new ArrayList<>();
        this.daftarSiswa = new ArrayList<>();
    }

    // Metode sesuai diagram: tambahKursus()
    public void tambahKursus(kursus kursus) {
        daftarKursus.add(kursus);
    }

    // Metode sesuai diagram: cariKursus()
    public kursus cariKursus(String kodeKursus) {
        for (kursus k : daftarKursus) {
            if (k.getKodeKursus().equalsIgnoreCase(kodeKursus)) {
                return k;
            }
        }
        return null; 
    }

    // Metode bantu: cariSiswa()
    public siswa cariSiswa(String idSiswa) {
        for (siswa s : daftarSiswa) {
            if (s.getIdSiswa().equalsIgnoreCase(idSiswa)) {
                return s;
            }
        }
        return null;
    }

    // Metode sesuai diagram: enrollSiswa() (Logika Bisnis Inti)
    public pembayaran enrollSiswa(String idSiswa, String kodeKursus, String metodePembayaran) 
            throws kuotaPenuhException, IllegalArgumentException {
        
        siswa siswa = cariSiswa(idSiswa);
        kursus kursus = cariKursus(kodeKursus);

        if (siswa == null) {
            throw new IllegalArgumentException("Siswa belum terdaftar di sistem.");
        }

        if (kursus == null) {
            throw new IllegalArgumentException("Error: Kursus dengan kode " + kodeKursus + " tidak ditemukan.");
        }
        
        // 1. Cek apakah siswa sudah terdaftar di kursus ini
        if (siswa.getDaftarKursus().contains(kodeKursus)) {
            throw new IllegalArgumentException("Siswa " + siswa.getName() + " sudah terdaftar di kursus " + kursus.getNama() + ".");
        }

        // 2. Cek Kuota dan Tambah Siswa (melempar KuotaPenuhException jika penuh)
        kursus.tambahSiswa(siswa); 
        
        // 3. Proses Pembayaran
        double biaya = kursus.getBiaya();
        String idTransaksi = "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String statusPembayaran;
        
        if (biaya > 0) {
            // Simulasi: Jika metode pembayaran kosong, status PENDING. Jika ada, SUKSES.
            if (metodePembayaran == null || metodePembayaran.isEmpty()) {
                statusPembayaran = "PENDING";
                // Hapus siswa dari kursus jika PENDING dan kursus berbayar
                kursus.getDaftarSiswa().remove(siswa); 
                kursus.jumlahSiswaTerdaftar--;
            } else if (metodePembayaran.equalsIgnoreCase("GRATIS")) {
                // Tambahan: kalau bayar "GRATIS" untuk kursus berbayar, PENDING juga
                statusPembayaran = "PENDING";
                kursus.getDaftarSiswa().remove(siswa);
                kursus.jumlahSiswaTerdaftar--;
            } else {
                statusPembayaran = "SUKSES"; // Simulasi sukses bayar
            }
        } else {
            statusPembayaran = "SUKSES"; // Kursus gratis langsung sukses
            metodePembayaran = "GRATIS";
        }

        pembayaran pembayaran = new pembayaran(
            idTransaksi, 
            biaya, 
            new Date(), 
            statusPembayaran, 
            metodePembayaran
        );
        
        daftarTransaksi.add(pembayaran);

        // 4. Update Siswa (hanya jika status SUKSES)
        if (statusPembayaran.equals("SUKSES")) {
            siswa.tambahKursus(kodeKursus);
            siswa.tambahRiwayat("Berhasil enroll di " + kursus.getNama() + ". Transaksi: " + idTransaksi);
        } else {
             siswa.tambahRiwayat("Pendaftaran di " + kursus.getNama() + " PENDING. Transaksi: " + idTransaksi);
        }

        return pembayaran;
    }

    // Metode bantu: tambahSiswa()
    public void tambahSiswa(siswa siswa) {
        if (cariSiswa(siswa.getIdSiswa()) == null) {
            daftarSiswa.add(siswa);
        } else {
            System.err.println("Siswa dengan ID " + siswa.getIdSiswa() + " sudah terdaftar.");
        }
    }

    // Getter untuk id sistem
    public String getidSistem() {
    return idSistem;
    }
  
    // Getter untuk data array kelas (untuk combo box GUI)
    public String[] getDaftarNamaKursus() {
        String[] namaKursus = new String[daftarKursus.size()];
        for (int i = 0; i < daftarKursus.size(); i++) {
            namaKursus[i] = daftarKursus.get(i).getNama() + " (" + daftarKursus.get(i).getKodeKursus() + ")";
        }
        return namaKursus;
    }
    
    // Getter yang tersisa
    public List<kursus> getDaftarKursus() { return daftarKursus; }
    public List<siswa> getDaftarSiswa() { return daftarSiswa; }
}