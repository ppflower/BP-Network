import function.SigmoidalFunction;
import function.TanhFunction;

import java.util.Arrays;


/**
 * Created by Hopes on 05/10/2017.
 */
public class Main {

    public static void main(String[] args) {
        learnHandwriting();
    }


    public static void learnSignBySigmoid() {
        BpDeep bpDeep = new BpDeep(4, new int[]{1,10,10,1}, 0.13, 0.05, new SigmoidalFunction());

        bpDeep.init();

        double[][] trainingSet = generateTrainingSet_Sig();


        System.out.println("------------------------Training Set-------------------------");
        int time = 200000;
        while (time > 0) {
            for (int i = 0; i < trainingSet.length; i ++) {
                bpDeep.train(new double[]{trainingSet[i][0]}, new double[]{trainingSet[i][2]});
                double result = bpDeep.getResult()[0] * 2 - 1;
//                System.out.println("sin(" + trainingSet[i][0] + ")=" + result + "/" + (trainingSet[i][1]) + "，误差为" + (trainingSet[i][1]-result));
            }
            time --;
        }

        System.out.println("------------------------Test Set-------------------------");

        double[][] testSet = generateTrainingSet_Sig();
        double sum = 0;
        for (int i = 0; i < testSet.length; i ++) {
            double[] res = bpDeep.test(new double[]{testSet[i][0]});
            double result = res[0] * 2 - 1;
            double loss = Math.abs(testSet[i][1] - result);
            sum += loss;
            System.out.println("sin(" + testSet[i][0] + ")=" + result + "/" + testSet[i][1] + "，误差为" + loss);
        }
        System.out.println(sum/100);

    }

    public static void learnSignByTanh() {
        BpDeep bpDeep = new BpDeep(4, new int[]{1,10,10,1}, 0.13, 0.05, new TanhFunction());

        bpDeep.init();

        double[][] trainingSet = generateTrainingSet_Tan();


        System.out.println("------------------------Training Set-------------------------");
        int time = 10000;
        while (time > 0) {
            for (int i = 0; i < trainingSet.length; i ++) {
                bpDeep.train(new double[]{trainingSet[i][0]}, new double[]{trainingSet[i][1]});
                double result = bpDeep.getResult()[0];
            }
            time --;
        }

        System.out.println("------------------------Test Set-------------------------");

        double[][] testSet = generateTrainingSet_Tan();
        double sum = 0;
        for (int i = 0; i < testSet.length; i ++) {
            double[] res = bpDeep.test(new double[]{testSet[i][0]});
            double result = res[0];
            double loss = Math.abs(testSet[i][1] - result);
            sum += loss;
            System.out.println("sin(" + testSet[i][0] + ")=" + result + "/" + testSet[i][1] + "，误差为" + loss);
        }
        System.out.println(sum/100);
    }

    public static double[][] generateTrainingSet_Sig() {
        double[][] trainingSet = new double[100][3];
        for (int i = 0; i < trainingSet.length; i ++) {
            double x = Math.random() * Math.PI * 2 - Math.PI;//随机产生输入的数
            double y = Math.sin(x);

            trainingSet[i][0] = x;
            trainingSet[i][1] = y;
            trainingSet[i][2] = (y + 1) / 2;//映射到0-1范围内的结果
        }
        return trainingSet;
    }

    public static double[][] generateTrainingSet_Tan() {
        double[][] trainingSet = new double[100][3];
        for (int i = 0; i < trainingSet.length; i ++) {
            double x = Math.random() * Math.PI * 2 - Math.PI;//随机产生输入的数
            double y = Math.sin(x);

            trainingSet[i][0] = x;
            trainingSet[i][1] = y;
            trainingSet[i][2] = y;
        }
        return trainingSet;
    }


    public static void learnHandwriting() {
        String[] map = {"苟", "利", "国", "家", "生", "死", "以", "岂", "因", "祸", "福", "避", "趋", "之"};


//        BpDeep bpDeep = new BpDeep(4, new int[]{784,200,50, 14}, 0.15, 0.15, new SigmoidalFunction());
        BpDeep bpDeep = new BpDeep(4, new int[]{784,90,74, 14}, 1.05, 0.18, new SigmoidalFunction());

        double[][][] trainingSet = generateTrainingSet_Hand();

//        System.exit(0);

        System.out.println("------------------------Training Set-------------------------");
        int time = 60;//迭代次数
        while (time > 0) {

            double loss = 0;
            for (int i = 0; i < trainingSet.length; i ++) {//遍历训练集
                if (i == trainingSet.length-1) {
                    bpDeep.computeLoss(trainingSet[i][1]);
                }

                bpDeep.train(trainingSet[i][0], trainingSet[i][1]);

            }
            System.out.println("第" + time + "遍结束");


            time --;
        }

        System.out.println("------------------------Test Set-------------------------");

        double[][][] testSet = generateTestSet_Hand();

        int total = testSet.length;
        int rightCount = 0;

        for (int i = 0; i < testSet.length; i ++) {
            double[] res = bpDeep.test(testSet[i][0]);
            int max = 0;
            for (int j = 0; j < res.length; j ++) {
                if (res[max] < res[j]) {
                    max = j;
                }
            }

            if (max == (int)testSet[i][2][0]-1) {
                rightCount ++;
            }
//            System.out.println("输入" + map[(int)testSet[i][2][0]-1] + "，识别为" + map[max]);
        }

        System.out.println("准确率为" + (((double) rightCount) / total));


    }

    public static double[][][] generateTrainingSet_Hand() {
        int trainingCount = 200;


        System.out.println("------------------------ 正在生成训练集 -------------------------");

        int[] sampleCount = new int[15];
        for (int i = 1; i < 15; i ++) {
            sampleCount[i] = 0;
        }

        double[][] standard = new double[14][14];
        for (int i = 0; i < standard.length; i ++) {
            standard[i][i] = 1;
//            System.out.println(Arrays.toString(standard[i]));
        }

        int total = trainingCount * 14;//每个汉字 trainingCount 个样本
        double[][][] result = new double[total][3][];
        for (int i = 0; i < total; i ++) {
            //生成目录号
            int dir = (int)(Math.random() * 14) + 1;
            while (sampleCount[dir] == trainingCount) {
                dir = (int)(Math.random() * 14) + 1;
            }


//            int pic = (int)(Math.random() * trainingCount);//生成字的序号
            int pic = sampleCount[dir];

            sampleCount[dir] ++;

            String path = "TRAIN/" + dir + "/" + pic + ".bmp";
            result[i][0] = BmpReader.read(path);//设置标准输入：二维数组转化成的一维数组

            //设置标准输出，长度为14的一维数组，最大的数字表示所指的数
            result[i][1] = standard[dir-1];

            result[i][2] = new double[]{ dir };//存储字的信息:第几个字

        }



        System.out.println("------------------------ 训练集生成完毕 -------------------------");
        return result;

    }

    public static double[][][] generateTestSet_Hand() {
        int trainingCount = 200;
        int testCount = 250 - trainingCount;

        System.out.println("------------------------ 正在生成测试集 -------------------------");

        int[] sampleCount = new int[15];
        for (int i = 1; i < 15; i ++) {
            sampleCount[i] = 0;
        }

        double[][] standard = new double[14][14];
        for (int i = 0; i < standard.length; i ++) {
            standard[i][i] = 1;
        }

        int total = testCount*14;//每个汉字 testCount 个样本
        double[][][] result = new double[total][3][];
        for (int i = 0; i < total; i ++) {
            //生成目录号
            int dir = (int)(Math.random() * 14) + 1;
            while (sampleCount[dir] == testCount) {
                dir = (int)(Math.random() * 14) + 1;
            }


            int pic = sampleCount[dir] + trainingCount;//生成字的序号 trainingCount-249

            sampleCount[dir] ++;

            String path = "TRAIN/" + dir + "/" + pic + ".bmp";
            result[i][0] = BmpReader.read(path);


            //设置结果
            result[i][1] = standard[dir-1];
            result[i][2] = new double[] {dir};

        }



        System.out.println("------------------------ 测试集生成完毕 -------------------------");
        return result;
    }

}
