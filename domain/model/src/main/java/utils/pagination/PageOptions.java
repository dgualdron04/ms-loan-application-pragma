package utils.pagination;

import java.util.List;

public record PageOptions(
        int page,
        int size,
        List<SortOrder> sort
) { }
