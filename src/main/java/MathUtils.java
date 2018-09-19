/**
 * Contains some utility function about computation
 *
 * @author wenzheli
 * @date Sep 19 2018
 */
public class MathUtils {

    /**
     * Draw sample from multinomial distribution
     * @return the index
     */
    public static int getSample(double[] p){
        int n = p.length;
        for (int i = 1; i < n; i++)
            p[i] += p[i-1];
        double u = Math.random() * p[n-1];
        int idx;
        for (idx = 0; idx < n; idx++){
            if (u <= p[idx])
                break;
        }

        return idx;
    }
}
