/*---------------------------------------------------------------------------------------------------*/
/*  TracerouteDatalooker package                                                                     */
/*  This program is an application to sift through data pertaining to traceroutes.                   */
/*  @author SVSU - CS 401 - Weston Smith                                                             */
/*---------------------------------------------------------------------------------------------------*/

package TracerouteDatalooker;

public class DatalogDriver {
    private String filename = "log_sample.txt";

    public DatalogDriver () {
        TracerouteDatalog log = new TracerouteDatalog();
        log.resultsToFile();
    }

    public static void main(String[] args) {
        DatalogDriver driver = new DatalogDriver();
        System.exit(0);
    }
}
