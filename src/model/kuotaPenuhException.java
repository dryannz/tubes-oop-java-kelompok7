package model;

    public class kuotaPenuhException extends Exception {
    private String kodeKursusYangGagal;

    public kuotaPenuhException(String kodeKursusYangGagal, String pesanError) {
        super(pesanError);
        this.kodeKursusYangGagal = kodeKursusYangGagal;
    }
    public String getKodeKursusYangGagal() {
        return kodeKursusYangGagal;
    } 
    public String getPesanError() {
        return super.getMessage();
    }
}
