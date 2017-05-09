package Pojos;

/**
 *
 * @author ariellugo92
 */
public class DiagonalCero {

    private float[][] arr;
    private int pos_orig;
    private int pos_camb;
    private String cambio;

    public DiagonalCero() {
    }

    public float[][] getArr() {
        return arr;
    }

    public void setArr(float[][] arr) {
        this.arr = arr;
    }

    public int getPos_orig() {
        return pos_orig;
    }

    public void setPos_orig(int pos_orig) {
        this.pos_orig = pos_orig;
    }

    public int getPos_camb() {
        return pos_camb;
    }

    public void setPos_camb(int pos_camb) {
        this.pos_camb = pos_camb;
    }

    public String getCambio() {
        return cambio;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }
}
