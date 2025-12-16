package app;

import gui.formPendaftaran;
import model.*;

public class mainGUI {
    public static void main(String[] args) {
        sistemPendaftaran sistem = new sistemPendaftaran("SYS001");
        
        // Tambah kursus
        sistem.tambahKursus(new kursusBerbayar("JAV01", "Java Programming", 50, 500000));
        sistem.tambahKursus(new kursusBerbayar("BAS02", "Basis Data", 25, 450000));
        sistem.tambahKursus(new kursusGratis("PRB03", "Proses Bisnis", 15, 0));
        
        // Jalankan GUI
        new formPendaftaran(sistem);
    }
}