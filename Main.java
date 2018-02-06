//Forked from https://github.com/nichlock/NASA_Desktop_Image-of-the-Day
//This Java file gets the location of the NASA image of the day and downloads it to a new file either labeled img.jpg or img2.jpg
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Main
{
    public static void main(String args[])
    {
    	String imagelink = "";
        try 
        {
            URLConnection conn = new URL("https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss").openConnection();
            DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc=dBuilder.parse(conn.getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nList=doc.getElementsByTagName("item");
			
			for (int i = 0; i < nList.getLength(); i++)
			{
				Node nNode=nList.item(i);
			  
				if(nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement=(Element) nNode;
					String temp=eElement.getElementsByTagName("enclosure").item(0).getAttributes().getNamedItem("url")+"";
					imagelink=temp.replace("url=\"", "").replace("\"", "").trim();
				}
				
				saveImage(imagelink); // Save the image pulled from NASA's RSS feed
			}
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error while getting RSS.\nPerhaps try checking your internet connection?");
        }
    }
    
    public static void saveImage(String imageURL) // save the image to the specified location
    {
    	// For troubleshooting:
        System.out.println(imageURL.substring(0,4) + "s" + imageURL.substring(4));
        try {
            // Get the image from the URL, and set it to the image files
            URL url = new URL(imageURL.substring(0,4) + "s" + imageURL.substring(4));
			
			String urlFileName = url.getFile();
			String fileName = urlFileName.substring(urlFileName.lastIndexOf("/")+1, urlFileName.length());
			File imageFile = new File(fileName);
			if (!imageFile.exists())
			{	// NOTE: this will eventually fill up the heap and crash after about 20 images
				// java.lang.OutOfMemoryError: Java heap space
				ImageIO.write(ImageIO.read(url), "jpg", imageFile);
			}
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error while writing to the image.");
            ex.printStackTrace();
        }
    }
}
