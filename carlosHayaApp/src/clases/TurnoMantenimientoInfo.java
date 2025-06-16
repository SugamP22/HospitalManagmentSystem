package clases;

import java.util.Date; 

public class TurnoMantenimientoInfo {
    private int salaId;
    private String tipoSala;
    private boolean limpia;
    private Date fechaMantenimiento; 

    public TurnoMantenimientoInfo(int salaId, String tipoSala, boolean limpia, Date fechaMantenimiento) {
        this.salaId = salaId;
        this.tipoSala = tipoSala;
        this.limpia = limpia;
        this.fechaMantenimiento = fechaMantenimiento;
    }

 
    public int getSalaId() {
        return salaId;
    }

    public String getTipoSala() {
        return tipoSala;
    }

    public boolean isLimpia() {
        return limpia;
    }

    public Date getFechaMantenimiento() {
        return fechaMantenimiento;
    }
}
