package com.fizzbuzz.vroom.core.persist;

import com.googlecode.objectify.Ref;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2013 Fizz Buzz LLC
 */

public class OfyUtils {
    public static <DAO extends BaseDao> List<Long> getIdsFromRefs(List<Ref<DAO>> refs) {
        List<Long> ids = new ArrayList<>();
        for (Ref<DAO> ref : refs) {
            ids.add(ref.getKey().getId());
        }
        return ids;
    }


}
