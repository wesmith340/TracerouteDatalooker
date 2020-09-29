/*---------------------------------------------------------------------------------------------------*/
/*  TracerouteDatalooker package                                                                     */
/*  This program is an application to sift through data pertaining to traceroutes.                   */
/*  @author SVSU - CS 401 - Weston Smith                                                             */
/*---------------------------------------------------------------------------------------------------*/

package TracerouteDatalooker;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * TracerouteDatalog Class
 * This class is designed to present data obtained from a list of traceroutes in a human readable format
 * @author SVSU - CS401 - Weston Smith
 */
public class TracerouteDatalog {
    private final Font font1     = new Font("Calibri", Font.PLAIN, 20);
    private final Font messageFont = new Font("Calibri", Font.PLAIN, 20);
    private final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private final int WIDTH  = 900;
    private final int HEIGHT = 850;

    private final int MAX_HOPS = 30;
    private final String pattern = "traceroute to";
    private LinkedList<Traceroute> routeList;

    public TracerouteDatalog() {
        routeList = new LinkedList<>();

        UIManager.put("OptionPane.messageFont", messageFont);
        UIManager.put("OptionPane.buttonFont", messageFont);

        JFileChooser fc = new JFileChooser();
        fc.setFont(font1);
        fc.setCurrentDirectory(new File("."));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocation((dim.width - WIDTH)/2, (dim.height - HEIGHT)/2);
        frame.setResizable(true);

        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // Use JFileChooser to get a file
            File file = fc.getSelectedFile();
            getFile(file);
        } else {
            JOptionPane.showMessageDialog(null,
                    "ERROR: File not Found\nShutting down",
                    "Error Message", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }
    /**
     * Constructor
     * @param filename
     */
    public TracerouteDatalog(String filename) {
        routeList = new LinkedList<>();
        getFile(new File(filename));
    }

    /**
     * This method retrieves the information from the file
     * @param file
     */
    private void getFile(File file) {
        try {
            Scanner scan = new Scanner(file);

            LinkedList<String> list = new LinkedList<>();
            if (scan.hasNext()) {
                list.addLast(scan.nextLine());
            }
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (line.contains(pattern)){ //Is line the start of a new Traceroute
                    routeList.add(new Traceroute(list));
                    list.clear();
                    list.addLast(line);
                } else {
                    list.addLast(line);
                }
            }
            routeList.add(new Traceroute((list)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("FILE_NOT_FOUND");
            System.exit(0);
        }
    }


    /**
     * This method returns the average number of hops for all traceroutes that reached their destination in the file
     * @return double
     */
    public double aveNumHops() {
        double ave = 0;
        Iterator it = routeList.iterator();
        int i = 0;
        while (it.hasNext()) {
            Traceroute route = (Traceroute)it.next();
            if (route.isSiteReached()) {
                ave += route.getNumHops();
                i++;
            }
        }
        return ave/i;
    }

    /**
     * This method returns the average delay between links for all traceroutes in the file
     * @return double
     */
    public double aveLinkDelay() {
        double ave = 0;
        int numTotal = 0;
        for (Traceroute route: routeList) {
            ave += route.getTotalLinkDelay();
            numTotal += route.getNumGoodLinks();
        }
        System.out.println(ave);
        System.out.println(numTotal);
        return ave/numTotal;
    }

    /**
     * This method returns the average delay to the destination for all traceroutes in the file
     * @return double
     */
    public double aveDelay() {
        double ave = 0;
        Iterator it = routeList.iterator();
        int i = 0;
        while (it.hasNext()) {
            Traceroute route = (Traceroute)it.next();
            if (route.isSiteReached()) {
                ave += route.getAveDelay();
                i++;
            }
        }
        return ave/i;
    }

    /**
     * This method formats the frequency of the number of hops to reach a destination
     * @return String
     */
    public String histData() {
        int[] numHops = new int[MAX_HOPS];
        for (int i = 0; i < MAX_HOPS; i++)
            numHops[i] = 0;
        for (Traceroute route : routeList) {
            if (route.isSiteReached())
                numHops[route.getNumHops()-1]++;
        }
        String msg = "";
        for (int i = 0; i < MAX_HOPS; i++)
            msg += i+1 +". " + numHops[i]+"\n";
        return msg;
    }

    /**
     * This method writes the results of the traceroute log to two files, ave.txt and hist.txt
     */
    public void resultsToFile() {
        String msg = "Average number of hops: "+aveNumHops()+"\n";
        msg+="Average Delay: "+aveDelay()+"\n";
        msg+="Average Link Delay: "+aveLinkDelay()+"\n";
        try {
            String path = System.getProperty("user.home")+"/Desktop/SmithTracerouteLog";
            File folder = new File(path);
            if (folder.mkdir()) {
                File ave = new File(path, "avg.txt");
                ave.createNewFile();

                FileWriter writer = new FileWriter(ave);
                writer.write(msg);
                writer.close();

                File hist = new File(path, "hist.txt");
                hist.createNewFile();

                writer = new FileWriter(hist);
                writer.write(histData());
                writer.close();
            } else {
                File ave = new File("avg.txt");
                ave.createNewFile();

                FileWriter writer = new FileWriter(ave);
                writer.write(msg);
                writer.close();

                File hist = new File("hist.txt");
                hist.createNewFile();

                writer = new FileWriter(hist);
                writer.write(histData());
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
        }
    }

}
