package ch.idsia.utils;

public class QInstance {

    private double[] estado;
    private double[] estadoSiguiente;
    private double[] accion;
    private double refuerzo;

    public QInstance(double estado, double estadoSiguiente, double accion, double refuerzo) {
        this.estado = new double[1];
        this.estadoSiguiente = new double[1];
        this.accion = new double[1];

        this.estado[0] = estado;
        this.estadoSiguiente[0] = estadoSiguiente;
        this.accion[0] = accion;
        this.refuerzo = refuerzo;
    }

    public double[] getEstado() {
        return estado;
    }

    public void setEstado(double[] estado) {
        this.estado = estado;
    }

    public double[] getEstadoSiguiente() {
        return estadoSiguiente;
    }

    public void setEstadoSiguiente(double[] estadoSiguiente) {
        this.estadoSiguiente = estadoSiguiente;
    }

    public double[] getAccion() {
        return accion;
    }

    public void setAccion(double[] accion) {
        this.accion = accion;
    }

    public double getRefuerzo() {
        return refuerzo;
    }

    public void setRefuerzo(double refuerzo) {
        this.refuerzo = refuerzo;
    }
}
