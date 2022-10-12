package container;

import servlet.HttpFilter;

import java.util.LinkedList;
import java.util.List;

public class FilterContainer {

    public static final List<HttpFilter> FilterContainer = new LinkedList<>();

    public static void pushFilter(HttpFilter filter) {
        FilterContainer.add(filter);
    }

}
