//我在程序中打印出了每一个坐标的RGB值，你自己整理整理，求个平均值，
//放到你的那个二维数组里。
//自己用画图工具做一个小图片，注意图片的名字和程序中一致哦~
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
public class Test{
    public static void main(String args[]) {
        File file = new File("TRAIN/1/3.bmp");
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

        int[][] img = new int[height][width];

        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j ++){
                //System.out.print(bi.getRGB(jw, ih));
                int pixel=bi.getRGB(i, j);
//                System.out.println("i="+i+",j="+j+":("+rgb[0]+","+rgb[1]+","+rgb[2]+")");
                if (pixel == -1) {
                    img[j][i] = '\0';
                } else {
                    img[j][i] = 1;
                }
            }
        }

        for(int i = 0;i < height; i ++){
            for(int j = 0;j < width; j ++){
                System.out.print(img[i][j]);
            }
            System.out.println();
        }
    }
}