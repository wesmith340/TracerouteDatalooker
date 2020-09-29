/*---------------------------------------------------------------------------------------------------*/
/*  TracerouteDatalooker package                                                                     */
/*  This program is an application to sift through data pertaining to traceroutes.                   */
/*  @author SVSU - CS 401 - Weston Smith                                                             */
/*---------------------------------------------------------------------------------------------------*/

package TracerouteDatalooker;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Traceroute Class
 * This class is designed to hold data pertaining to a single traceroute
 */
public class Traceroute {
    private LinkedList<Hop> hops;
    private String destinationName, destinationIP;
    private boolean siteReached;
    private int numGoodLinks;

    /**
     * No-Arg Constructor
     */
    public Traceroute () {
        hops = new LinkedList<>();
        siteReached = false;
        numGoodLinks = 0;
    }

    /**
     * Constructor
     * @param list
     */
    public Traceroute (LinkedList list) {
        hops = new LinkedList<>();
        numGoodLinks = 0;

        Iterator it = list.iterator();
        StringBuilder line = new StringBuilder((String)it.next());
        line.delete(0, 14); // Gets rid of "traceroute to"
        destinationName = line.substring(0, line.indexOf(" "));
        destinationIP = line.substring(line.indexOf(" ")+1, line.indexOf(","));
        while (it.hasNext()) {
            line = new StringBuilder((String)it.next());
            line.delete(0, 4); //Gets rid of line numbers
            hops.addLast(new Hop(line.toString()));
        }
        if (hops.getLast().containsIpAddress(destinationIP)) {
            siteReached = true;
        } else {
            siteReached = false;
        }
    }

    /**
     * This method returns whether of not this traceroute made it to the destination IP Address
     * @return boolean
     */
    public boolean isSiteReached() {
        return siteReached;
    }

    /**
     * This method returns the number of hops this traceroute took
     * @return int
     */
    public int getNumHops(){
        return hops.size();
    }

    /**
     *
     * @return
     */
    public int getNumGoodLinks() {
        return numGoodLinks;
    }
    /**
     * This method returns the average delay between links for this traceroute
     * @return double
     */
    public double getTotalLinkDelay(){
        double sum = 0;
        int i = 0;
        double prevDelay = 0;
        for (Hop currHop : hops) {
            if (currHop.isGoodHop() && (prevDelay !=0 || i==0) && currHop.getAveDelay()-prevDelay > 0) {
                sum += currHop.getAveDelay() - prevDelay;
                i++;
            }
            prevDelay = currHop.getAveDelay();
        }
        numGoodLinks = i;
        return sum;
    }

    /**
     * This method returns the average delay it took for the traceroute to reach it's destination
     * @return double
     */
    public double getAveDelay() {
        return hops.getLast().getAveDelay();
    }

    /**
     * This method returns a human readable String representation of this Traceroute
     * @return String
     */
    public String toString(){
        String message = "";
        message += String.format("Destination IP: %s\n", destinationIP);
        message += String.format("Destination Web Address: %s\n", destinationName);
        message += String.format("Number of Hops: %d\n", getNumHops());
        if (siteReached) {
            message += String.format("Delay: %f ms\n", hops.getLast().getAveDelay());
        } else {
            message += String.format("Delay: N/A\n");
        }

        message += String.format("Link times:\n");
        for (Hop hop : hops) {
            for (double delay : hop.getDelay()) {
                message += String.format("%-3.5f ms  ", delay);
            }
            message += "\n";
        }
        return message;
    }
}
