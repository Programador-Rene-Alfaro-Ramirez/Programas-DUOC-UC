package mavenproject1.modelo;

public abstract class Decorator implements Component {
    protected Component componente;

    public Decorator(Component componente) {
        this.componente = componente;
    }

    @Override
    public double getPrecio() {
        return componente.getPrecio();
    }

    @Override
    public String getDescripcion() {
        return componente.getDescripcion();
    }
}
