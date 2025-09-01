package mavenproject1.model;

public abstract class DescuentoDecorator implements Component {
    protected Component componente;

    public DescuentoDecorator(Component componente) {
        this.componente = componente;
    }
}
