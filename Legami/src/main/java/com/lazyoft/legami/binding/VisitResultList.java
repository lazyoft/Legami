package com.lazyoft.legami.binding;

import java.util.ArrayList;
import java.util.List;

public class VisitResultList {
    List<VisitResult> results;

    public VisitResultList() {
        results = new ArrayList<VisitResult>();
    }

    public void add(VisitResult result) {
        results.add(result);
    }

    public VisitResult getCurrentResult() {
        if(results.size() > 0)
            return results.get(results.size() - 1);
        return null;
    }

    public List<VisitResult> getResults() {
        return results;
    }
}
