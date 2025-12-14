package src.model;

public class kursusBerbayar extends kursus {
    private double biayaKursus;

    // Constructor
    public kursusBerbayar(String kodeKursus, String nama, String kuotaMaksimal, double biayaKursus) {
        super(kodeKursus, nama, kuotaMaksimal);
        this.biayaKursus = biayaKursus;
    }

    // implementasi metode abstrak untuk getBiaya()
    @Override
    public double getBiaya() {
        return biayaKursus;
    }
}
