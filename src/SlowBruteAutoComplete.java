import java.util.*;

/**
 * From running the Benchmark, we know that SlowBruteAutocomplete is the slowest of the 3
 * For the time complexity of this class we can compute:
 * O(N) to search through the N terms and find M matches to the prefix
 * O(MlogM) to sort the M matches by weight
 * O(k) to return the k elements with the heaviest weights
 */

public class SlowBruteAutoComplete extends BruteAutocomplete {

    /**
     * @param terms array of terms where terms[i] corresponds to weights[i]
     * @param weights weights and terms are used to create a list of Term objects
     */

    public SlowBruteAutoComplete(String[] terms, double[] weights) {
        super(terms, weights);
    }

    /**
     * @param prefix for each Term to be compared to find the number of Matches, M
     * @param k the number of matches to be returned
     * @return a list of size k or M (number of matches) sorted by heaviest weight. The size of the list is the smaller
     *          of the two variables
     */
    @Override
    public List<Term> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException("Illegal value of k:"+k);
        }

        List<Term> list = new ArrayList<>();
        for (Term t : super.myTerms) {
            if (t.getWord().startsWith(prefix)) {
                list.add(t);
            }
        }
        Collections.sort(list, Comparator.comparing(Term::getWeight).reversed());
        int minVal = Math.min(k, list.size());
        return list.subList(0, minVal);
    }
}
