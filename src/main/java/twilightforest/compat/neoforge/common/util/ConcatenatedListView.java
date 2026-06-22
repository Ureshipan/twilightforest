package twilightforest.compat.neoforge.common.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

public class ConcatenatedListView<E> extends AbstractList<E> {
    private final List<? extends List<? extends E>> lists;

    private ConcatenatedListView(List<? extends List<? extends E>> lists) {
        this.lists = lists;
    }

    @SafeVarargs
    public static <E> List<E> of(List<? extends E>... lists) {
        return new ConcatenatedListView<>(List.of(lists));
    }

    public static <E> List<E> of(Collection<? extends List<? extends E>> lists) {
        return new ConcatenatedListView<>(List.copyOf(lists));
    }

    @Override
    public E get(int index) {
        for (List<? extends E> list : lists) {
            if (index < list.size()) return list.get(index);
            index -= list.size();
        }
        throw new IndexOutOfBoundsException(index);
    }

    @Override
    public int size() {
        return lists.stream().mapToInt(List::size).sum();
    }
}
