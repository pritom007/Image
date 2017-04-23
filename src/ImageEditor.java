
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UTFDataFormatException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.function.BinaryOperator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Pritom Kumar Mondal
 */
public class ImageEditor extends JPanel {

    private BufferedImage image;

    public ImageEditor() throws FileNotFoundException, IOException {
 
        /**
         * 
         * Read the data from a text file
         * 
         * Format: Name Email
         * 
         * N.B: Chinese Character is not preferred
         * 
         */
		try (InputStream fis = new FileInputStream("//file name with path ");
			 InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			 BufferedReader br = new BufferedReader(isr);
			) {
	        		// Declaring the variable that will read the data from file 
	    			String line;
	    			// This variable will act as id/number that you will put in the edited image
					int count=0;
					//loop through the data 
					while ((line = br.readLine()) != null) {
						String[] fields=line.split("	");
						//just make it look more organized 
						System.out.println(count+"		"+fields[0].trim()+"		"+fields[1].trim()+"			"+binaryIdGenerator(count, 6));
						//the image you want to edit
						image = ImageIO.read(new File("..//your desired file name & path"));
						//here you can code for your preferable data
						if(count>=1)
							image = process(image,fields[0],count);
						count++;
					}
			  }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(), image.getHeight());
    }
    public BufferedImage process(BufferedImage old,String personName,int ticket) {
        int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(old, 0, 0, null);
        g2d.setPaint(Color.WHITE);
        g2d.setFont(new Font("Bell MT", 3, 16));
        
        FontMetrics fm = g2d.getFontMetrics();
        
        // Getting the X,Y coordinate 
        int x = img.getWidth();
        int y = fm.getHeight();
        
        /**
         *  
         * Here set your desired x,y position 
         * N.B:This part is hard coded 
         * Back side image name part
         * 
         */
        
        g2d.drawString(personName.trim(), x-35-fm.stringWidth(personName), y+123);
        g2d.setPaint(Color.black);
        
        // N.B:This part is hard coded
        // back side serial part
        g2d.drawString(binaryIdGenerator(ticket, 6), x-100, y+223);
        //save the image to your desired directory
        save(img,"..//somename"+ticket);
        //save(img,"filename"+ticket);
        
        return img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    private static void create() throws FileNotFoundException, IOException {
    	
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new ImageEditor());
        f.pack();
        f.setVisible(true);
        
       /**
        * since there are too many pictures 
        * better to dispose the frame
        * otherwise you can comment this following line 
        */
        f.dispose();
    }
    
    /**
     * 
     * This method is for saving the image
     * @param BufferedImage,String(file name)
     * @return void
     * 
     * */
    private static void save(BufferedImage image, String fileName) {
    	
    	File file = new File(fileName + "." + "png");
    	BufferedImage image1 = (BufferedImage)image;
    	try {
    	    ImageIO.write(image1, "png", file);  // ignore returned boolean
    	}catch(IOException e) {
    		System.out.println("Write error for " + file.getPath() +": " + e.getMessage());
    	 }
    }
    
    /**
     * 
     * This function gives binary number according to your preference
     * 
     * @param int,int
     * first int is the desired number/id
     * second int is for mask/ how long the number would be 
     * @return String
     * returns a binary number as string
     *  
     * */
    public static String binaryIdGenerator(int number, int groupSize) {
    	
        StringBuilder result = new StringBuilder();
        for(int i = 5; i >= 0 ; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");

            if (i % groupSize == 0)
                result.append(" ");
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }
    
    // Program starts from here 
    public static void main(String[] args) {
    	for (int i = 0; i < 40; i++) {
			System.out.println(binaryIdGenerator(i, 6));
		}
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					create();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
    }//main function ends here
}
