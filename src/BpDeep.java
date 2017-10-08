import function.ActiveFunction;

/**
 * Created by Hopes on 04/10/2017.
 */
public class BpDeep {
    private int layerCount;//层数，包括输入与输出层

    private double[][] neurons_output;//每层神经元的输出
    private double[][][] weight;//每个神经元的下一层神经元对其影响的权重
    private double[][] biases;//每层的偏置

    private double[][] neuronError;

    private ActiveFunction af;

    public double learning_rate = 0.13;

    public double weight_range = 0.05;

    public BpDeep(int layerCount, int[] layerNeuronCount, double learning_rate, double weight_range, ActiveFunction af) {
        this.layerCount = layerCount;
        this.learning_rate = learning_rate;
        this.weight_range = weight_range;
        this.af = af;

        //建立Bp网络结构
        neurons_output = new double[layerCount][];
        weight = new double[layerCount][][];
        neuronError = new double[layerCount][];
        biases = new double[layerCount][];

        for (int i = 0; i < layerCount; i ++) {
            int in = layerNeuronCount[i];

            neurons_output[i] = new double[in];
            weight[i] = new double[in][];
            neuronError[i] = new double[in];
            biases[i] = new double[in];
        }

        init();
    }

    public void init() {

        //初始化偏置
        for (int i = 1; i < biases.length; i ++) {
            for (int j = 0; j < biases[i].length; j ++) {
                int sign = (int)(Math.random()* 2) == 0 ? -1 : 1;//随机正负号
                biases[i][j] = Math.random() * this.weight_range * sign;

            }
        }

        //初始化各个权重
        for (int i = 0; i < layerCount-1; i ++) {//输出层没有上面一层
            for (int j = 0; j < neurons_output[i].length; j ++) {
                int len = neurons_output[i+1].length;
                weight[i][j] = new double[len];
                for (int k = 0; k < len; k ++) {
                    int sign = (int)(Math.random()* 2) == 0 ? -1 : 1;//随机正负号
                    weight[i][j][k] = Math.random() * this.weight_range * sign;

                }
            }
        }

    }

    public void adjustWeight(double[] tar) {
        int topLayer = layerCount-1;
        for (int i = 0; i < neuronError[topLayer].length; i ++) {
            neuronError[topLayer][i] = neurons_output[topLayer][i] - tar[i];
        }

        for (int l = topLayer-1; l > 0; l --) {
            for (int i = 0; i < neuronError[l].length; i ++) {
                double tmp = 0;
                for (int j = 0; j < weight[l+1].length; j ++) {
                    tmp += neuronError[l+1][j] * af.derivative(neurons_output[l+1][j]) * weight[l][i][j];

                }
                neuronError[l][i] = tmp;
            }
        }

        //调整权重的值
        for (int l = topLayer-1; l >= 0; l --) {
            for (int i = 0; i < weight[l].length; i ++) {
                for (int j = 0; j < weight[l][i].length; j ++) {//neurons_output[l+1][j] * (1-neurons_output[l+1][j])
                    weight[l][i][j] -= this.learning_rate *
                            neuronError[l+1][j] * af.derivative(neurons_output[l+1][j]) * neurons_output[l][i];
                }
            }
        }

        //调整偏置的值
        for (int l = topLayer; l > 0; l --) {
            for (int i = 0; i < biases[l].length; i ++) {
                biases[l][i] -= this.learning_rate *
                        neuronError[l][i] * af.derivative(neurons_output[l][i]);
            }
        }
    }

    public void train(double[] input, double[] target) {
        for (int i = 0; i < neurons_output[0].length; i++) {
            neurons_output[0][i] = input[i];
        }
        computeOutput();


        adjustWeight(target);
    }

    public double[] getResult() {
        return neurons_output[layerCount-1];
    }

    public void computeOutput() {
        for (int i = 1; i < layerCount; i ++) {
            for (int j = 0; j < neurons_output[i].length; j ++) {
                int downlayer = i-1;
                double sum = biases[i][j];//该神经元的偏置

                for (int k = 0; k < neurons_output[downlayer].length; k ++) {
                    sum += weight[downlayer][k][j] * neurons_output[downlayer][k];
                }

                neurons_output[i][j] = af.activate(sum);
            }
        }
    }

    public double[] test(double input[]) {
        if (input.length != neurons_output[0].length) {
            System.out.println("Error!");
            System.exit(0);
        }


        for (int i = 0; i < neurons_output[0].length; i++) {
            neurons_output[0][i] = input[i];
        }
        computeOutput();

        return neurons_output[layerCount-1];
    }


    public void computeLoss(double[] standardOutput) {
        if (standardOutput.length != neurons_output[layerCount-1].length) {
            System.out.println("Error in computeOutput!");
            System.exit(0);
        }

        double loss = 0;
        for (int i = 0; i < standardOutput.length; i ++) {
            loss += (standardOutput[i]-neurons_output[layerCount-1][i]) * (standardOutput[i]-neurons_output[layerCount-1][i]);
        }

        System.out.println("Loss:" + loss/2);
    }


}
