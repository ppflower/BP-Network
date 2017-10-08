import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BmpReader {

    public static double[] read(String filepath) {
        File file = new File(filepath);
        BufferedImage bi=null;
        try{
            bi = ImageIO.read(file);
        }catch(Exception e){
            e.printStackTrace();
        }

        if (bi == null) {
            System.out.println("Error");
        }

        int width = 28;
        int height = 28;

        double[][] img = new double[height][width];

        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j ++){
                int pixel=bi.getRGB(i, j);
                if (pixel == -1) {
                    img[j][i] = 0;
                } else {
                    img[j][i] = 1;
                }
            }
        }

//        for(int i = 0;i < height; i ++){
//            for(int j = 0;j < width; j ++){
//                System.out.print(img[i][j]==0?' ':'@');
//            }
//            System.out.println();
//        }

        //转化成一维数组
        return transform(img);
    }

    public static double[] transform(double[][] img) {
        double[] result = new double[784];
        for (int i = 0; i < 28; i ++) {
            for (int j = 0; j < 28; j ++) {
                result[i*28+j] = img[i][j];
            }
        }
        return result;
    }

}
