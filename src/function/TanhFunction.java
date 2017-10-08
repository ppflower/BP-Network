package function;

/**
 * Created by Hopes on 06/10/2017.
 */
public class TanhFunction implements ActiveFunction {

    @Override
    public double activate(double d) {
        return Math.tanh(d);
    }

    @Override
    public double derivative(double d) {
        return (1 - d * d);
    }
}
