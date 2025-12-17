package app;

import gui.*;
import model.*;

public class mainGUI {
    public static void main(String[] args) {
        sistemPendaftaran sistem = new sistemPendaftaran("SYS001");
        
        // Tambah kursus
        sistem.tambahKursus(new kursusBerbayar("JAV01", "Java Programming", 5, 500000));
        sistem.tambahKursus(new kursusBerbayar("BAS02", "Basis Data", 7, 450000));
        sistem.tambahKursus(new kursusGratis("PRB03", "Proses Bisnis", 3, 0));
        
        // Jalankan GUI
        new formPendaftaran(sistem);
    }
}