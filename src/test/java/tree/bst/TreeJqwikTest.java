package tree.bst;

import list.List;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.Tuple;

import static list.List.list;

public class TreeJqwikTest {

    private Tree<Integer> buildTree(Integer[] xs, int i, Tree<Integer> acc) {
        return i >= xs.length
                ? acc
                : buildTree(xs, i + 1, acc.insert(xs[i]));
    }

    private Tree<Integer> buildTree(Integer... xs) {
        return buildTree(xs, 0, Tree.tree());
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer, Boolean>> memberData() {
        return java.util.List.of(
                Tuple.of(5, true),
                Tuple.of(10, true),
                Tuple.of(99, false),
                Tuple.of(-1, false)
        );
    }

    @Property
    @FromData("memberData")
    boolean memberWorks(@ForAll Integer x,
                        @ForAll Boolean expected) {

        Tree<Integer> t = buildTree(5, 3, 8, 10);

        return t.member(x) == expected;
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], Integer>> sizeData() {
        return java.util.List.of(
                Tuple.of(new Integer[]{}, 0),
                Tuple.of(new Integer[]{1}, 1),
                Tuple.of(new Integer[]{1,2,3}, 3),
                Tuple.of(new Integer[]{5,3,8,1}, 4)
        );
    }

    @Property
    @FromData("sizeData")
    boolean sizeWorks(@ForAll Integer[] xs,
                      @ForAll Integer expected) {

        return buildTree(xs).size() == expected;
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], Integer>> heightData() {
        return java.util.List.of(
                Tuple.of(new Integer[]{}, 0),
                Tuple.of(new Integer[]{5}, 1),
                Tuple.of(new Integer[]{5,3}, 2),
                Tuple.of(new Integer[]{5,3,1}, 3)
        );
    }

    @Property
    @FromData("heightData")
    boolean heightWorks(@ForAll Integer[] xs,
                        @ForAll Integer expected) {

        return buildTree(xs).height() == expected;
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer, Boolean>> deleteData() {
        return java.util.List.of(
                Tuple.of(3, false),
                Tuple.of(5, false),
                Tuple.of(8, false)
        );
    }

    @Property
    @FromData("deleteData")
    boolean deleteWorks(@ForAll Integer x,
                        @ForAll Boolean expected) {

        Tree<Integer> t = buildTree(5,3,8).delete(x);

        return t.member(x) == expected;
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], Integer>> findMinData() {
        return java.util.List.of(
                Tuple.of(new Integer[]{5}, 5),
                Tuple.of(new Integer[]{5,3,8,1,9}, 1),
                Tuple.of(new Integer[]{10,7,15,2}, 2)
        );
    }

    @Property
    @FromData("findMinData")
    boolean findMinWorks(@ForAll Integer[] xs,
                         @ForAll Integer expected) {

        return buildTree(xs).findMin().equals(expected);
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], Integer>> findMaxData() {
        return java.util.List.of(
                Tuple.of(new Integer[]{5}, 5),
                Tuple.of(new Integer[]{5,3,8,1,9}, 9),
                Tuple.of(new Integer[]{10,7,15,20}, 20)
        );
    }

    @Property
    @FromData("findMaxData")
    boolean findMaxWorks(@ForAll Integer[] xs,
                         @ForAll Integer expected) {

        return buildTree(xs).findMax().equals(expected);
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], List<Integer>>> inorderData() {
        return java.util.List.of(
                Tuple.of(
                        new Integer[]{5,3,8,1,4},
                        list(1,3,4,5,8)
                ),

                Tuple.of(
                        new Integer[]{10,5,15},
                        list(5,10,15)
                )
        );
    }

    @Property
    @FromData("inorderData")
    boolean inorderWorks(@ForAll Integer[] xs,
                         @ForAll List<Integer> expected) {

        return buildTree(xs).inOrder().equals(expected);
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], Integer>> sizeLeafData() {
        return java.util.List.of(
                Tuple.of(new Integer[]{5}, 1),
                Tuple.of(new Integer[]{5,3,8}, 2),
                Tuple.of(new Integer[]{5,3,1}, 1)
        );
    }

    @Property
    @FromData("sizeLeafData")
    boolean sizeLeafWorks(@ForAll Integer[] xs,
                          @ForAll Integer expected) {

        return buildTree(xs).sizeLeaf() == expected;
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], Integer>> sizeInnerData() {
        return java.util.List.of(
                Tuple.of(new Integer[]{5}, 0),
                Tuple.of(new Integer[]{5,3,8}, 1),
                Tuple.of(new Integer[]{5,3,1}, 2)
        );
    }

    @Property
    @FromData("sizeInnerData")
    boolean sizeInnerWorks(@ForAll Integer[] xs,
                           @ForAll Integer expected) {

        return buildTree(xs).sizeInner() == expected;
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], Integer>> sizeHalfData() {
        return java.util.List.of(
                Tuple.of(new Integer[]{5}, 0),
                Tuple.of(new Integer[]{5,3,1}, 2),
                Tuple.of(new Integer[]{5,3,8}, 0)
        );
    }

    @Property
    @FromData("sizeHalfData")
    boolean sizeHalfWorks(@ForAll Integer[] xs,
                          @ForAll Integer expected) {

        return buildTree(xs).sizeHalf() == expected;
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], Integer>> sizeFullData() {
        return java.util.List.of(
                Tuple.of(new Integer[]{5}, 0),
                Tuple.of(new Integer[]{5,3,8}, 1),
                Tuple.of(new Integer[]{5,3,1}, 0)
        );
    }

    @Property
    @FromData("sizeFullData")
    boolean sizeFullWorks(@ForAll Integer[] xs,
                          @ForAll Integer expected) {

        return buildTree(xs).sizeFull() == expected;
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer[], Integer>> sizeEmptyData() {
        return java.util.List.of(
                Tuple.of(new Integer[]{5}, 2),
                Tuple.of(new Integer[]{5,3,8}, 4),
                Tuple.of(new Integer[]{5,3,1}, 4)
        );
    }

    @Property
    @FromData("sizeEmptyData")
    boolean sizeEmptyWorks(@ForAll Integer[] xs,
                           @ForAll Integer expected) {

        return buildTree(xs).sizeEmpty() == expected;
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer, Integer>> findEqData() {
        return java.util.List.of(
                Tuple.of(3, 3),
                Tuple.of(8, 8),
                Tuple.of(99, null)
        );
    }

    @Property
    @FromData("findEqData")
    boolean findEqWorks(@ForAll Integer x,
                        @ForAll Integer expected) {

        Tree<Integer> t = buildTree(5,3,8);

        Integer result = t.findEq(x);

        return result.equals(expected);
    }

    @Data
    public Iterable<Tuple.Tuple2<Integer, Integer>> lookupEqData() {
        return java.util.List.of(
                Tuple.of(3, 3),
                Tuple.of(8, 8),
                Tuple.of(99, -1)
        );
    }

    @Property
    @FromData("lookupEqData")
    boolean lookupEqWorks(@ForAll Integer x,
                          @ForAll Integer expected) {

        Tree<Integer> t = buildTree(5,3,8);

        return t.lookupEq(x)
                .getOrElse(-1)
                .equals(expected);
    }

    @Data
    public Iterable<Tuple.Tuple1<Boolean>> isEmptyData() {
        return java.util.List.of(
                Tuple.of(true)
        );
    }

    @Property
    @FromData("isEmptyData")
    boolean isEmptyWorks(@ForAll Boolean expected) {

        return Tree.<Integer>tree().isEmpty() == expected;
    }

    @Property
    boolean insertIncreasesSize(
            @ForAll @IntRange(min = 0, max = 100) Integer x) {

        Tree<Integer> t = buildTree(5,3,8);

        return t.insert(x).size() == t.size() + 1;
    }
}