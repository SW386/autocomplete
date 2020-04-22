import java.util.*;

/**
 *
 * This implementation uses a HashMap of prefixes as keys and reverse sorted Terms as values
 * to find the top term(s).
 *
 * @author Kevin Wen
 */

public class HashListAutocomplete implements Autocompletor {

    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;

    /**
     * Given arrays of words and weights, initialize myMap to a HashMap with all prefixes of less than MAX_PREFIX
     * as keys and a List pf Terms reverse sorted by weights as values.
     *
     *
     * @param terms
     *            - A list of words to form terms from
     * @param weights
     *            - A corresponding list of weights, such that terms[i] has
     *            weight[i].
     *
     * @return a HashListAutocomplete with the Term objects stored in MyMap created from
     *             terms[i] and weight[i]
     * @throws a
     *             NullPointerException if either argument passed in is null
     */

    public HashListAutocomplete(String[] terms, double[] weights) {

        if (terms == null || weights == null) {
            throw new NullPointerException("One or more arguments null");
        }

        initialize(terms, weights);
    }

    /**
     * Required by the Autocompletor interface. Returns a List containing the
     * k words in myTerms with the largest weight which match the given prefix,
     * in descending weight order. If less than k words exist matching the given
     * prefix (including if no words exist), then the array instead contains all
     * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
     * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
     * 2) should return {"air"}
     *
     * @param prefix
     *            - A prefix which all returned words must start with
     * @param k
     *            - The (maximum) number of words to be returned
     * @return An array of the k words with the largest weights among all words
     *         starting with prefix, in descending weight order. If less than k
     *         such words exist, return an array containing all those words If
     *         no such words exist, reutrn an empty array
     * @throws NullPointerException if prefix is null
     */

    @Override
    public List<Term> topMatches(String prefix, int k) {

        if (k <= 0 || !myMap.containsKey(prefix)) {
            return new ArrayList<Term>();
        }
        List<Term> all = myMap.get(prefix);
        List<Term> list = all.subList(0, Math.min(k, all.size()));
        return list;
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        myMap = new HashMap<String, List<Term>>();
        for (int i = 0; i < terms.length; i++) {
            int count = 0;
            Term term = new Term(terms[i], weights[i]);
            while (count <= terms[i].length() && count <= MAX_PREFIX) {
                String key = terms[i].substring(0, count);
                if (! myMap.containsKey(key)) {
                    List<Term> value = new ArrayList<Term>();
                    value.add(term);
                    myMap.put(key, value);
                } else {
                    List<Term> value = myMap.get(key);
                    value.add(term);
                }
                count++;
            }
        }

        for (String i : myMap.keySet()) {
            Collections.sort(myMap.get(i), Comparator.comparing(Term::getWeight).reversed());
        }
    }
    /**
     * calculates the Byte Size of the HashMap by adding up the byte size of each key and each term in the list
     * mapped by the key
     **/
    @Override
    public int sizeInBytes() {
        if (mySize == 0) {
            for (String key : myMap.keySet()) {
                mySize += BYTES_PER_CHAR*key.length();
                for (Term T : myMap.get(key)) {
                    mySize += BYTES_PER_DOUBLE +
                            BYTES_PER_CHAR*T.getWord().length();
                }
            }
        }
        return mySize;
    }
}
