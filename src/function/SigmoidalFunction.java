package function;

/**
 * Created by Hopes on 05/10/2017.
 */
public class SigmoidalFunction implements ActiveFunction {

    @Override
    public double activate(double d) {
        return (1 / (1 + Math.exp(-d)));
    }

    @Override
    public double derivative(double d) {
        return d * (1 - d);
    }



}
