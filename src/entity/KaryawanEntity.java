package entity;

/**
 * Class  (Model/Entity)
 */
public class KaryawanEntity {
    
    // Variabel HARUS SAMA PERSIS dengan kolom di Supabase
    private Integer id; 
    private String nama_karyawan;
    private String posisi;

    // --- GETTER & SETTER ---
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama_karyawan() {
        return nama_karyawan;
    }

    public void setNama_karyawan(String nama_karyawan) {
        this.nama_karyawan = nama_karyawan;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }
}