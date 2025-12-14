package src.model;

public class kursusGratis extends kursus {
    
    // Constructor
    public kursusGratis(String kodeKursus, String nama, String kuotaMaksimal) {
        super(kodeKursus, nama, kuotaMaksimal);
    }

    // Implementasi metode abstrak getBiaya()
    @Override
    public double getBiaya() {
        return 0.0; // Biaya selalu 0.0
    }
}
