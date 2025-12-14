package app;

import model.*;

public class main {
    public static void main(String[] args) {

        // 1. Buat sistem pendaftaran
        sistemPendaftaran sistem = new sistemPendaftaran("S001");

        // 2. Buat kursus berbayar (kuota String sesuai implementasi kamu)
        kursus kursusJava = new kursusBerbayar(
                "K001",
                "Java OOP",
                1,        // kuota hanya 1 → biar exception kepicu
                500000
        );

        sistem.tambahKursus(kursusJava);

        // 3. Buat siswa
        siswa siswaPertama = new siswa(
                "ST01",
                "Andrew",
                "Andrewt@email.com",
                "08123456789"
        );

        siswa siswaKedua = new siswa(
                "ST02",
                "Milan",
                "Nzmilan@email.com",
                "08987654321"
        );

        // 4. Tambahkan siswa ke sistem
        sistem.tambahSiswa(siswaPertama);
        sistem.tambahSiswa(siswaKedua);

        // 5. Proses enroll siswa
        try {
            // enroll pertama → sukses
            pembayaran p1 = sistem.enrollSiswa(
                    "ST01",
                    "K001",
                    "TRANSFER"
            );
            System.out.println("Transaksi 1: " + p1.getStatus());

            // enroll kedua → kuota penuh
            pembayaran p2 = sistem.enrollSiswa(
                    "ST02",
                    "K001",
                    "TRANSFER"
            );
            System.out.println("Transaksi 2: " + p2.getStatus());

        } catch (kuotaPenuhException e) {
            System.out.println("Gagal enroll: kuota penuh");
            System.out.println("Kursus : " + e.getKodeKursusYangGagal());
            System.out.println("Pesan  : " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Error data: " + e.getMessage());
        }

        System.out.println("Program selesai.");
    }
}
