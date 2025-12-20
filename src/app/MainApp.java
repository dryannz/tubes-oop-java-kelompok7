package app;

// Import kelas-kelas dari package model
import model.*; // Untuk testing data siswa
// Import kelas dari package gui 
import gui.*; 

public class MainApp { 
    
    /**
     * Method main: Titik masuk utama (Entry Point) aplikasi Java.
     */
    public static void main(String[] args) {
        
        // 1. INISIASI SISTEM MODEL
        sistemPendaftaran sistem = new sistemPendaftaran("SISTEM_KELOMPOK_7"); 

        // 2. INISIASI DATA KURSUS AWAL
        
        // Kursus Berbayar: kursusBerbayar(String kode, String nama, int kuota, double biaya) 
        kursus javaOop = new kursusBerbayar("K001", "Java Fundamental OOP", 15, 500000.0); 
        kursus webDev = new kursusBerbayar("K002", "Fullstack Web Development", 20, 750000.0); 
        
       // Kursus Gratis: kursusGratis(String kode, String nama, int kuota) [cite: 85, 57]
        kursus gitDasar = new kursusGratis("K003", "Pengantar Git & GitHub", 100); 

       // Tambahkan kursus-kursus ke sistem [cite: 86]
        sistem.tambahKursus(javaOop); 
        sistem.tambahKursus(webDev); 
        sistem.tambahKursus(gitDasar); 
        
        // Tambahkan data siswa untuk testing (opsional)
        siswa siswa1 = new siswa("S001", "Titis Novianti", "titis@mail.com", "081111"); 
        sistem.tambahSiswa(siswa1); 
        
        // 3. MENAMPILKAN GUI
        
    // Menjalankan GUI di Event Dispatch Thread (EDT) [cite: 87]
        javax.swing.SwingUtilities.invokeLater(() -> {
            
       // Membuat FormPendaftaran dan memasukkan sistemPendaftaran [cite: 87]
            formPendaftaran form = new formPendaftaran(sistem); 
            form.setTitle(sistem.getIdSistem() + " - Pendaftaran Kursus"); 
            form.setVisible(true); 
        });
    }
}