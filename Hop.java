/*---------------------------------------------------------------------------------------------------*/
/*  TracerouteDatalooker package                                                                     */
/*  This program is an application to sift through data pertaining to traceroutes.                   */
/*  @author SVSU - CS 401 - Weston Smith                                                             */
/*---------------------------------------------------------------------------------------------------*/

package TracerouteDatalooker;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Hop Class
 * This class stores the data related to one hop
 */
public class Hop {
    private final String IP_PATTERN = "^\\([0-9]+.[0-9]+.[0-9]+.[0-9]+\\)$";
    private final String DELAY_PATTERN = "^?[0-9]+.[0-9]{3}$";
    private LinkedList<String> ipAddress;
    private LinkedList<Double> delay;

    /**
     * No-Arg Constructor
     */
    public Hop() {
        ipAddress = new LinkedList<>();
        delay = new LinkedList<>();
    }

    /**
     * Constructor
     * @param line
     */
    public Hop(String line) {
        ipAddress = new LinkedList<>();
        delay = new LinkedList<>();

        StringTokenizer token = new StringTokenizer(line);
        while (token.hasMoreTokens()) {
            String item = token.nextToken(" ");
            if (item.matches(IP_PATTERN)) {
                ipAddress.add(item);
            } else if (item.matches(DELAY_PATTERN)) {
                delay.add(Double.parseDouble(item));
            }
        }
    }

    /**
     * This method returns true if any probe returned data to the client
     * @return boolean
     */
    public boolean isGoodHop(){
        return delay.size() != 0;
    }

    /**
     * This method returns true if this hop contains the String parameter as an IP Address
     * @param ip
     * @return boolean
     */
    public boolean containsIpAddress(String ip) {
        if (ipAddress.contains(ip)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method returns a list of delays for this hop
     * @return LinkedList
     */
    public LinkedList<Double> getDelay() {
        return delay;
    }
    /**
     * This method returns the average of the delays or 0 if there are none
     * @return double
     */
    public double getAveDelay() {
        if (delay.size() != 0) {
            double sum = 0;
            for (double time : delay){
                sum += time;
            }
            return sum / delay.size();
        }
        return 0;
    }

    /**
     * This method returns a human readable string representation of this hop
     * @return
     */
    public String toString(){
        String msg = "";
        Iterator it = delay.iterator();
        while (it.hasNext()) {
            msg += ((double) it.next()) + " ms  ";
        }
        return msg;
    }
}
