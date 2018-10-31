package cz.muni.fi.namesny.network;

import java.util.List;

public class Network {

    private Layer[] layers;
    private ActivationFunction activationFunction;
    private ActivationDerivative activationDerivative;

    public Network(int[] layerSizes,
                   ActivationFunction activationFunction,
                   ActivationDerivative activationDerivative) {

        this.layers = new Layer[layerSizes.length];
        this.activationFunction = activationFunction;

        for (int i = 0; i < layerSizes.length; i++) {
            Integer layerSize = layerSizes[i];
            Integer prevLayerSize = layerSize;

            if (i>0){
                prevLayerSize = layerSizes[i-1];
            }

            this.layers[i] = new Layer(layerSize, prevLayerSize);
        }
    }

    public double[] feedForward(double[] inputs) {

        double[] nextInputs = inputs;
        double[] layerResult;

        for (Layer layer : layers) {
            layerResult = layer.compute(nextInputs);
            layer.setError(layerResult);

            nextInputs = activate(layerResult);
        }

        return nextInputs;
    }

    public void backPropagate() {

    }

    public double[] activate(double[] input) {

        double[] result = new double[input.length];

        for (int i = 0; i < input.length; i++) {
            result[i] = activationFunction.compute(input[i]);
        }

        return result;
    }

    public double[] softmax(double[] input) {

        double totalInput = 0;

        for (double in : input) {
            totalInput += Math.exp(in);
        }

        double[] result = new double[input.length];

        for (int i = 0; i < input.length; i++) {
            result[i] = Math.exp(input[i]) / totalInput;
        }

        return result;
    }

    public double quadraticCost(double[] predicted, double[] actual) {

        double cost = 0.0d;

        if (predicted.length != actual.length) {
            throw new IllegalArgumentException("Vector lengths do not match");
        }

        for (int i = 0; i < predicted.length; i++) {
            cost += Math.pow(actual[i] - predicted[i], 2);
        }

        return cost/2.0d;

    }

}
