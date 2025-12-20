package gui;

import model.sistemPendaftaran;

public class formPendaftaran {

    private JTextField inputNama, inputEmail, inputTelepon;
    private JComboBox<String> comboKursus;
    private JComboBox<String> comboMetode; 
    private JLabel labelBiaya, labelSistemInfo;
    
    public formPendaftaran(sistemPendaftaran sistem) {
        //TODO Auto-generated constructor stub
    }
    
    private void handleDaftar(ActionEvent e) {
        // 1. Validasi input
        String nama = inputNama.getText().trim();
        String email = inputEmail.getText().trim();
        String telepon = inputTelepon.getText().trim();

        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Harap lengkapi semua data!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

    public void setTitle(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTitle'");
    }

    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVisible'");
    }
    
}
