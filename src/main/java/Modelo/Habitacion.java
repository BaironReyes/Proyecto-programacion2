package Modelo;

    import java.math.BigDecimal;

    public class Habitacion {
        private int id;
        private String numeroHabitacion;
        private String tipoHabitacion;
        private BigDecimal precio;
        private String estado;


        public Habitacion(String numeroHabitacion, String tipoHabitacion, BigDecimal precio, String estado) {
            this.numeroHabitacion = numeroHabitacion;
            this.tipoHabitacion = tipoHabitacion;
            this.precio = precio;
            this.estado = estado;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNumeroHabitacion() {
            return numeroHabitacion;
        }

        public void setNumeroHabitacion(String numeroHabitacion) {
            this.numeroHabitacion = numeroHabitacion;
        }

        public String getTipoHabitacion() {
            return tipoHabitacion;
        }

        public void setTipoHabitacion(String tipoHabitacion) {
            this.tipoHabitacion = tipoHabitacion;
        }

        public BigDecimal getPrecio() {
            return precio;
        }

        public void setPrecio(BigDecimal precio) {
            this.precio = precio;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }


