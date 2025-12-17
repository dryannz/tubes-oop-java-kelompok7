package gui;

import model.kuotaPenuhException;
import javax.swing.*;
import java.awt.*;

public class dataKursus extends JDialog {
    
    public dataKursus(JFrame parent, kuotaPenuhException exception) {
        super(parent, "Kuota Penuh", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(231, 76, 60)); // Merah
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("KUOTA PENUH");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // Konten
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        content.add(new JLabel("Maaf, pendaftaran tidak dapat dilanjutkan."));
        content.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tampilkan pesan error dari exception
        content.add(new JLabel("Detail Kursus:"));
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Ambil pesan dari exception
        content.add(new JLabel("• Status: " + exception.getPesanError()));
        content.add(new JLabel("• Kode Kursus: " + exception.getKodeKursusYangGagal()));
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(new JLabel("Silakan pilih kursus lain."));
        
        // Tombol OK
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(e -> dispose());
        
        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(btnOK, BorderLayout.SOUTH);
    }
}