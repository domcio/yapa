package pl.edu.agh.yapa.monitoring;

/**
 * Created with IntelliJ IDEA.
 * User: Dominik
 * Date: 19.05.14
 * Time: 23:54
 */
public class SpikeMain {
    public static void main(String[] args) {

        String line = null;
        java.io.BufferedReader in = new java.io.BufferedReader(
                new java.io.InputStreamReader(System.in));
        try {
            do {
                System.out.println("(ne) new template (nt) new type (nj) new job");
                System.out
                        .print("(a) show ads (e) show templates (t) show types (j) show jobs (x) exit\n==> ");
                System.out.flush();
                line = in.readLine();
                if (line.equals("t")) {
                    DBUtils.listTable(DBUtils.TYPES_TABLE);
                } else if (line.equals("j")) {
                    DBUtils.listTable(DBUtils.JOBS_TABLE);
                } else if (line.equals("e")) {
                    DBUtils.listTable(DBUtils.TEMPLATES_TABLE);
                } else if (line.equals("a")) {
                    DBUtils.listAds();
                } else if (line.equals("ne")) {
                    DBUtils.listAds();
                } else if (line.equals("nt")) {
                    DBUtils.listAds();
                } else if (line.equals("nj")) {
                    DBUtils.listAds();
                }
            }
            while (!line.equals("x"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
