package model;

import java.util.Date;

public class pembayaran {
    private String idTransaksi;
    private double jumlahBayar;
    private Date tanggal;
    private String status;
    private String metodePembayaran; 

    // Konstruktor
    public pembayaran(String idTransaksi, double jumlahBayar, Date tanggal, String status, String metodePembayaran) {
        this.idTransaksi = idTransaksi;
        this.jumlahBayar = jumlahBayar;
        this.tanggal = tanggal;
        this.status = status;
        this.metodePembayaran = metodePembayaran;
    }

    // Metode sesuai diagram: getName()
    public String getName() {
        return this.idTransaksi; 
    }

    // Getter
    public String getIdTransaksi() { 
        return idTransaksi; 
    }
    public double getJumlahBayar() { 
        return jumlahBayar; 
    }
    public Date getTanggal() { 
        return tanggal; 
    }
    public String getStatus() { 
        return status; 
    }
    public String getMetodePembayaran() { 
        return metodePembayaran; 
    }
}