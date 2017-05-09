package Controladores;

import Pojos.DiagonalCero;
import Pojos.Resultados;
import Pojos.TransformacionElemental;

/**
 *
 * @author ariellugo92
 */
public class metodosGaussControlador {

    /**
     * *
     * Metodo para obtener la matriz nose y la matriz ampliada
     *
     * @param arr_nums Arreglo bidimensional de float que contiene la matriz
     * @param num_incog Numero de incognitas del sistema de ecuaciones
     * @return Retorna un String con las 2 matrices
     */
    public String getMatrizAmpliada(float[][] arr_nums, int num_incog) {
        String texto = "Paso 1) No me acuerdo el nombre (Ax = b) \n\n";
        int k = num_incog + 1;

        // matriz ampliada Ax = b
        for (int i = 0; i < num_incog; i++) {
            texto += "| ";
            for (int j = 0; j < k; j++) {
                float valor = arr_nums[i][j];
                if (j == 0) {
                    texto += valor;
                }

                if (j > 0 && j < num_incog) {
                    texto += "  " + valor;
                }

                if (j == num_incog) {
                    texto += " | X" + (i + 1) + " | " + valor;
                }
            }
            texto += " | \n";
        }

        texto += "\nPaso 2) Matriz aumentada (A = [a/b]) \n\n";

        // matriz ampliada A = [a/b]
        texto += this.getMatrizAumentada(arr_nums, num_incog, false);

        return texto;
    }

    // metodo para generar una matriz aumentada
    public String getMatrizAumentada(float[][] matriz, int tam, boolean flag) {
        String texto = "";

        for (int i = 0; i < tam; i++) {
            texto += "| ";
            for (int j = 0; j < (tam + 1); j++) {
                float valor = matriz[i][j];
                if (j == 0) {
                    texto += (flag) ? valor + "X" + (j + 1) : valor;
                }

                if (j > 0 && j < tam) {
                    texto += (flag) ? "  " + valor + "X" + (j + 1) : "  " + valor;
                }

                if (j == tam) {
                    texto += " | " + valor;
                }
            }
            texto += " | \n";
        }

        return texto;
    }

    // metodo para validar que el elemento en la diagonal sea distinto cero
    private DiagonalCero comprobadaQueDiagonalNoTieneCero(float[][] matriz, int pos, int tam) {
        float[][] matriz_temp = matriz;

        float valor = matriz[pos][pos];
        int pos_j = 0;
        String encontro = "";
        if (valor == 0) {
            // si es igual a cero, vamos a buscar que un elemento que este por
            // debajo de la columna de el que sea distinto de cero
            for (int i = (pos + 1); i < tam; i++) {
                float valor_debajo = matriz[i][pos];
                if (valor_debajo != 0) {
                    // si el valor que esta debajo es distinto de cero
                    // entonces cambiar la fila encontrada por la fila que tiene el valor de cero
                    for (int j = 0; j < (tam + 1); j++) {
                        float pos_orig = matriz_temp[pos][j];
                        float pos_camb = matriz_temp[i][j];
                        matriz[pos][j] = pos_camb;
                        matriz[i][j] = pos_orig;
                    }
                    encontro = "encontro";
                    pos_j = i;
                    break;
                }
            }
            encontro = (encontro.isEmpty()) ? "no encontro" : "encontro";
        }

        encontro = (!encontro.isEmpty() || !encontro.equals("encontro") || !encontro.equals("no encontro")) ? 
                    encontro : "sin cambio";
        
        DiagonalCero dc = new DiagonalCero();
        dc.setArr(matriz);
        dc.setPos_orig(pos);
        dc.setPos_camb(pos_j);
        dc.setCambio(encontro);

        return dc;
    }

    // metodo para dividir una matriz por un numero de tal manera que quede en 1 el elemento
    private float[][] getMatrizPivoteCambioadoUno(float[][] matriz, int pos, int tam, float num_divisor) {
        for (int i = pos; i < (tam + 1); i++) {
            float valor = matriz[pos][i];
            float valor_nuevo = (num_divisor / valor);
            matriz[pos][i] = valor_nuevo;
        }

        return matriz;
    }

    public boolean getEstado = false;

    /**
     * *
     * Metodo para realizar las transformaciones elementales paso a paso
     *
     * @param arr_nums Matriz a la que se le realizaran las transformaciones
     * @param num_incog Numero de incognitas del sistema de ecuaciones
     *
     * @return Retorna un objeto el cual contiene la matriz transformada y el
     * texto a mostrar
     */
    public TransformacionElemental getTransformacionesElementales(float[][] arr_nums, int num_incog) {
        String texto = "";

        for (int i = 0; i < num_incog; i++) {
            // recorriendo la diagonal principal
            DiagonalCero dc = this.comprobadaQueDiagonalNoTieneCero(arr_nums, i, num_incog);
            String cambio = dc.getCambio();
            arr_nums = dc.getArr();

            // quiere decir que el sistema tiene infinitas soluciones
            if (cambio.equals("no encontro")) {
                getEstado = true;
                return null;
            }

            // si encontro quiere decir que hizo un cambio de filas
            if (cambio.equals("encontro")) {
                texto += "Transformacion dado que el pivote es 0\t f" + dc.getPos_orig() + " <> f" + dc.getPos_camb() + "\n\n";
                texto += getMatrizAumentada(dc.getArr(), num_incog, false);
            }

            // si el pivote en el que estemos no es uno, lo transformamos a uno
            float valor_pivote = arr_nums[i][i];
            if (valor_pivote != 1f && valor_pivote != 0f) {
                texto += "Transformacion dado que el pivote es distinto de uno, f" + i + " > "
                        + valor_pivote + "/f" + i + "\n\n";
                arr_nums = getMatrizPivoteCambioadoUno(arr_nums, i, num_incog, valor_pivote);
                texto += getMatrizAumentada(arr_nums, num_incog, false);
            }

            // una vez teniendo el pivote en 1, empezamos hacer cero lo que esta debajo de este
            int cont = 0;
            for (int j = (i + 1); j < num_incog; j++) {
                float valor_abajo_pivote = arr_nums[j][i];
                if (valor_abajo_pivote != 0f) {
                    cont++;
                    if (cont == 1) {
                        texto += "Transformacion";
                    }

                    for (int k = 0; k < (num_incog + 1); k++) {
                        // variables que cambian segun la posicion de la columna
                        float valor_fila_pivote = arr_nums[i][k];
                        float valor_fila_abojo_pivote = arr_nums[j][k];
                        // variables para tomar y reemplazar el valor de la columna
                        float valor_fila_pivote_mult = (valor_fila_pivote * valor_abajo_pivote);
                        float valor_positivo = (valor_fila_pivote_mult - valor_fila_abojo_pivote);

                        arr_nums[j][k] = valor_positivo;
                    }
                    texto += "\tf" + (j + 1) + " > " + valor_abajo_pivote + "f" + (i + 1)
                            + " - f" + (j + 1) + "\n";
                }
            }

            if (cont > 0) {
                texto += "\n" + getMatrizAumentada(arr_nums, num_incog, false) + "\n";
            }
        }

        //llenando el objeto
        TransformacionElemental te = new TransformacionElemental();
        te.setTexto(texto);
        te.setMatriz(arr_nums);

        return te;
    }

    // metodo para obtener el valor de las incognitas
    public float[] valorIncognitas(float[][] arr_nums, int tam) {
        float[] vals = new float[tam];

        int j = 0;
        for (int i = tam; i > 0; i--) {
            int i_menos = (i - 1);
            float ultimo_elemento_col = arr_nums[i_menos][tam];
            // valor de Xn
            float valor_Xn = arr_nums[i_menos][i_menos];
            // inicioamos hacia atras

            if (j > 0) {
                for (int k = 0; k < j; k++) {
                    float valor_par = arr_nums[i_menos][(tam - (k + 1))];
                    float valor_par_Xn = vals[(k)];
                    float valor_par_mult = (valor_par * valor_par_Xn);
                    if (valor_par_mult != 0f) {
                        ultimo_elemento_col = (valor_par_Xn > 0)
                                ? (ultimo_elemento_col - valor_par_mult)
                                : (ultimo_elemento_col + valor_par_mult);
                    }
                }
            }

            float nuevo = (ultimo_elemento_col / valor_Xn);
            vals[j] = nuevo;

            j++;
        }

        return vals;
    }

    /***
     * Metod para obtener la matriz resultante y el valor de las incognitas
     * 
     * @param arr_nums Matriz resultante de las transformaciones elementales
     * @param tam Numero de incognitas del sistema de ecuaciones
     * @return Devuelve un objeto que contiene el texto a mostrar y un arregle de float con las incognitas
     */
    public Resultados resultados(float[][] arr_nums, int tam) {
        String texto = "Sistema Equivalente \n\n";

        texto += this.getMatrizAumentada(arr_nums, tam, true);
        texto += "\nObteniendo el valor de las incognitas \n\n";
        float[] incognitas = this.valorIncognitas(arr_nums, tam);
        int j = tam;
        for (int i = 0; i < tam; i++) {
            texto += "Incognita X" + (i + 1) + ": " + incognitas[(j - 1)] + "\n";
            j--;
        }

        Resultados res = new Resultados();
        res.setTexto(texto);
        res.setIncognitas(incognitas);

        return res;
    }
    
    /***
     * Metodo para comprobar que las incognitas son las correctas
     * 
     * @param sistema_ecuaciones Sistema de ecuaciones original
     * @param valor_incognitas Valor encontrado de las incognitas
     * @param tam Numero de incognitas
     * @return 
     */
    public String getTextoComprobado(float [][] sistema_ecuaciones, float [] valor_incognitas, int tam){
        String texto = "";
        
        for (int i = 0; i < tam; i++) {
            texto += "Comprobando sustituyendo el valor de las incognitas en la fila " + (i + 1) + "\n\n";
            float valor_der = 0f;
            int k = (tam - 1);
            for (int j = 0; j < tam; j++) {
                float valor = sistema_ecuaciones[i][j];
                float valor_incognita = valor_incognitas[k];
                float valor_result = valor * valor_incognita;
                
                texto += (valor_result >= 0) ? 
                        "+ " + valor + "(" + valor_incognita + ") " : 
                        "- " + (Math.abs(valor)) + "(" + valor_incognita + ") ";
                valor_der += valor_result;
                k--;
            }
            
            texto += " = " + sistema_ecuaciones[i][tam] + "\n";
            texto += valor_der + " = " + sistema_ecuaciones[i][tam] + "\n\n";
        }
        
        return texto;
    }

}
